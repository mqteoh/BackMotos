package co.edu.unbosque.BackMotos.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.FinalizarReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.UsoRepuestoDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.Repuesto;
import co.edu.unbosque.BackMotos.entity.RepuestoReparacion;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;
import co.edu.unbosque.BackMotos.repository.TecnicoRepository;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

@Service
public class TallerService {

	private static final EnumSet<EstadoReparacion> PENDIENTES = EnumSet.of(EstadoReparacion.AGENDADA,
			EstadoReparacion.EN_PROCESO);

	private final ReparacionRepository reparacionRepository;
	private final RepuestoRepository repuestoRepository;
	private final TecnicoRepository tecnicoRepository;
	private final Clock reloj;

	public TallerService(ReparacionRepository reparacionRepository, RepuestoRepository repuestoRepository,
			TecnicoRepository tecnicoRepository, Clock reloj) {
		this.reparacionRepository = reparacionRepository;
		this.repuestoRepository = repuestoRepository;
		this.tecnicoRepository = tecnicoRepository;
		this.reloj = reloj;
	}

	@Transactional(readOnly = true)
	public List<ReparacionDTO> agenda(UsuarioAutenticado usuario, Integer idSucursal, List<EstadoReparacion> estados,
			LocalDate desde, LocalDate hasta) {
		if (desde != null && hasta != null && desde.isAfter(hasta)) {
			throw new BadRequestException("La fecha inicial no puede ser posterior a la final.");
		}
		Integer sede = usuario.getRol() == Rol.DUENO ? idSucursal : usuario.getIdSucursal();
		Collection<EstadoReparacion> filtro = estados == null || estados.isEmpty() ? PENDIENTES : estados;
		return reparacionRepository
				.agenda(sede, filtro, desde == null ? null : desde.atStartOfDay(),
						hasta == null ? null : hasta.plusDays(1).atStartOfDay())
				.stream().map(ReparacionDTO::de).toList();
	}

	@Transactional(readOnly = true)
	public List<ReparacionDTO> asignadas(UsuarioAutenticado usuario) {
		return reparacionRepository.findByTecnicoIdEmpleadoOrderByFechaAgendadaDesc(usuario.getId()).stream()
				.map(ReparacionDTO::de).toList();
	}

	@Transactional(readOnly = true)
	public ReparacionDTO detalle(UsuarioAutenticado usuario, Integer idReparacion) {
		return ReparacionDTO.de(visible(usuario, idReparacion));
	}

	@Transactional(readOnly = true)
	public List<ReparacionDTO> historial(String placa) {
		return reparacionRepository.findByMotoPlacaOrderByFechaAgendada(placa.trim().toUpperCase(Locale.ROOT)).stream()
				.map(ReparacionDTO::de).toList();
	}

	@Transactional
	public ReparacionDTO tomar(UsuarioAutenticado usuario, Integer idReparacion) {
		Reparacion reparacion = visible(usuario, idReparacion);
		if (reparacion.getEstado() != EstadoReparacion.AGENDADA) {
			throw new ConflictException("Solo se pueden tomar reparaciones agendadas; esta está " + texto(reparacion) + ".");
		}
		Tecnico tecnico = tecnicoRepository.findById(usuario.getId())
				.orElseThrow(() -> new ConflictException("Solo un técnico puede tomar reparaciones."));
		reparacion.setTecnico(tecnico);
		reparacion.setEstado(EstadoReparacion.EN_PROCESO);
		return ReparacionDTO.de(reparacionRepository.saveAndFlush(reparacion));
	}

	@Transactional
	public ReparacionDTO registrarRepuesto(UsuarioAutenticado usuario, Integer idReparacion, UsoRepuestoDTO datos) {
		Reparacion reparacion = enCursoDelTecnico(usuario, idReparacion);
		Repuesto repuesto = repuestoRepository.findById(datos.idRepuesto()).orElseThrow(
				() -> new ResourceNotFoundException("No existe el repuesto con id " + datos.idRepuesto() + "."));
		BigDecimal precio = datos.precioUnitario() == null ? repuesto.getPrecio() : datos.precioUnitario();
		reparacion.getRepuestos().stream()
				.filter(u -> u.getRepuesto().getIdRepuesto().equals(repuesto.getIdRepuesto())).findFirst()
				.ifPresentOrElse(u -> {
					u.setCantidad(datos.cantidad());
					u.setPrecioUnitario(precio);
				}, () -> reparacion.agregarRepuesto(repuesto, datos.cantidad(), precio));
		return ReparacionDTO.de(reparacionRepository.saveAndFlush(reparacion));
	}

	@Transactional
	public ReparacionDTO quitarRepuesto(UsuarioAutenticado usuario, Integer idReparacion, Integer idRepuesto) {
		Reparacion reparacion = enCursoDelTecnico(usuario, idReparacion);
		RepuestoReparacion uso = reparacion.getRepuestos().stream()
				.filter(u -> u.getRepuesto().getIdRepuesto().equals(idRepuesto)).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("La reparación no tiene registrado ese repuesto."));
		reparacion.getRepuestos().remove(uso);
		return ReparacionDTO.de(reparacionRepository.saveAndFlush(reparacion));
	}

	@Transactional
	public ReparacionDTO finalizar(UsuarioAutenticado usuario, Integer idReparacion, FinalizarReparacionDTO datos) {
		Reparacion reparacion = enCursoDelTecnico(usuario, idReparacion);
		if (datos.costo() == null) {
			throw new ConflictException("Para finalizar una reparación se necesita el costo de la mano de obra.");
		}
		reparacion.setCosto(datos.costo());
		if (datos.descripcion() != null && !datos.descripcion().isBlank()) {
			reparacion.setDescripcion(datos.descripcion().trim());
		}
		reparacion.setFechaFinalizacion(LocalDateTime.now(reloj).truncatedTo(ChronoUnit.SECONDS));
		reparacion.setEstado(EstadoReparacion.FINALIZADA);
		return ReparacionDTO.de(reparacionRepository.saveAndFlush(reparacion));
	}

	private Reparacion visible(UsuarioAutenticado usuario, Integer idReparacion) {
		Reparacion reparacion = reparacionRepository.findById(idReparacion).orElseThrow(
				() -> new ResourceNotFoundException("No existe la reparación con id " + idReparacion + "."));
		if (usuario.getRol() != Rol.DUENO
				&& !reparacion.getSucursal().getIdSucursal().equals(usuario.getIdSucursal())) {
			throw new ConflictException("La reparación " + idReparacion + " pertenece a la "
					+ reparacion.getSucursal().getNombre() + ".");
		}
		return reparacion;
	}

	private Reparacion enCursoDelTecnico(UsuarioAutenticado usuario, Integer idReparacion) {
		Reparacion reparacion = visible(usuario, idReparacion);
		if (reparacion.getEstado() != EstadoReparacion.EN_PROCESO) {
			throw new ConflictException("La reparación debe estar en proceso; esta está " + texto(reparacion) + ".");
		}
		if (reparacion.getTecnico() == null || !reparacion.getTecnico().getIdEmpleado().equals(usuario.getId())) {
			throw new ConflictException("Solo el técnico asignado puede modificar esta reparación.");
		}
		return reparacion;
	}

	private String texto(Reparacion reparacion) {
		return reparacion.getEstado().name().replace('_', ' ').toLowerCase(Locale.ROOT);
	}
}

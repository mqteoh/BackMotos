package co.edu.unbosque.BackMotos.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.AgendarReparacionDTO;
import co.edu.unbosque.BackMotos.dto.AgendarReparacionDTO.MotoExterna;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.Sucursal;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.ModeloRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;

@Service
public class ReparacionService {

	private static final EnumSet<EstadoReparacion> PENDIENTES = EnumSet.of(EstadoReparacion.AGENDADA,
			EstadoReparacion.EN_PROCESO);

	private final ReparacionRepository reparacionRepository;
	private final MotoRepository motoRepository;
	private final ModeloRepository modeloRepository;
	private final SucursalRepository sucursalRepository;
	private final ClienteRepository clienteRepository;
	private final VentaMotoRepository ventaMotoRepository;
	private final Clock reloj;

	public ReparacionService(ReparacionRepository reparacionRepository, MotoRepository motoRepository,
			ModeloRepository modeloRepository, SucursalRepository sucursalRepository,
			ClienteRepository clienteRepository, VentaMotoRepository ventaMotoRepository, Clock reloj) {
		this.reparacionRepository = reparacionRepository;
		this.motoRepository = motoRepository;
		this.modeloRepository = modeloRepository;
		this.sucursalRepository = sucursalRepository;
		this.clienteRepository = clienteRepository;
		this.ventaMotoRepository = ventaMotoRepository;
		this.reloj = reloj;
	}

	@Transactional
	public ReparacionDTO agendar(Integer idCliente, AgendarReparacionDTO datos) {
		Sucursal sucursal = sucursalRepository.findById(datos.idSucursal())
				.orElseThrow(() -> new ResourceNotFoundException("No existe la sede con id " + datos.idSucursal() + "."));
		String placa = datos.placa().trim().toUpperCase(Locale.ROOT);
		Moto moto = motoRepository.findById(placa).orElseGet(() -> registrarExterna(placa, datos));
		if (moto.getOrigen() == OrigenMoto.INVENTARIO && !ventaMotoRepository.existsByMotoPlaca(placa)) {
			throw new ConflictException("La moto " + placa + " es del inventario y aún no se ha vendido.");
		}
		if (reparacionRepository.existsByMotoPlacaAndEstadoIn(placa, PENDIENTES)) {
			throw new ConflictException("La moto " + placa + " ya tiene un servicio pendiente en el taller.");
		}
		Reparacion reparacion = new Reparacion();
		reparacion.setFechaAgendada(datos.fechaAgendada().truncatedTo(ChronoUnit.MINUTES));
		reparacion.setDescripcion(datos.descripcion().trim());
		reparacion.setKilometrajeIngreso(datos.kilometrajeIngreso());
		reparacion.setEstado(EstadoReparacion.AGENDADA);
		reparacion.setMoto(moto);
		reparacion.setCliente(clienteRepository.getReferenceById(idCliente));
		reparacion.setSucursal(sucursal);
		Integer id = reparacionRepository.saveAndFlush(reparacion).getIdReparacion();
		return ReparacionDTO.de(reparacionRepository.findById(id).orElseThrow());
	}

	@Transactional(readOnly = true)
	public List<ReparacionDTO> deCliente(Integer idCliente) {
		return reparacionRepository.findByClienteIdClienteOrderByFechaAgendadaDesc(idCliente).stream()
				.map(ReparacionDTO::de).toList();
	}

	@Transactional
	public ReparacionDTO cancelarDeCliente(Integer idCliente, Integer idReparacion) {
		Reparacion reparacion = reparacionRepository.findById(idReparacion)
				.filter(r -> r.getCliente().getIdCliente().equals(idCliente))
				.orElseThrow(() -> new ResourceNotFoundException("No tiene una reparación con id " + idReparacion + "."));
		if (reparacion.getEstado() != EstadoReparacion.AGENDADA) {
			throw new ConflictException("Solo se pueden cancelar reparaciones que aún no han empezado.");
		}
		reparacion.setEstado(EstadoReparacion.CANCELADA);
		return ReparacionDTO.de(reparacionRepository.saveAndFlush(reparacion));
	}

	private Moto registrarExterna(String placa, AgendarReparacionDTO datos) {
		MotoExterna externa = datos.motoExterna();
		if (externa == null) {
			throw new BadRequestException(
					"La placa " + placa + " no está registrada. Envíe la marca, el modelo, el año y el color de la moto.");
		}
		Modelo modelo = modeloRepository.findById(new ModeloId(externa.idMarca(), externa.nroModelo()))
				.orElseThrow(() -> new ResourceNotFoundException("No existe el modelo indicado para la moto."));
		Moto moto = new Moto();
		moto.setPlaca(placa);
		moto.setModelo(modelo);
		moto.setOrigen(OrigenMoto.EXTERNA);
		moto.setAnio(externa.anio());
		moto.setColor(externa.color().trim());
		moto.setKilometraje(datos.kilometrajeIngreso() == null ? 0 : datos.kilometrajeIngreso());
		moto.setFechaIngreso(LocalDate.now(reloj));
		return motoRepository.saveAndFlush(moto);
	}
}

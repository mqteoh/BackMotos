package co.edu.unbosque.BackMotos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.NuevaReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO.TecnicoParticipante;
import co.edu.unbosque.BackMotos.dto.UsoRepuestoDTO;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.Repuesto;
import co.edu.unbosque.BackMotos.entity.RepuestoReparacion;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.VentaMoto;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;
import co.edu.unbosque.BackMotos.repository.TecnicoRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

/**
 * Taller: el técnico registra las reparaciones ya hechas, con los técnicos que participaron y los repuestos usados.
 * El técnico solo ve y corrige las de su sede; el dueño ve todas.
 */
@Service
public class TallerService {

	/** Acceso a la base para las reparaciones. */
	private final ReparacionRepository reparacionRepository;
	/** Acceso a la base para las motos. */
	private final MotoRepository motoRepository;
	/** Acceso a la base para las ventas de motos. */
	private final VentaMotoRepository ventaMotoRepository;
	/** Acceso a la base para los repuestos. */
	private final RepuestoRepository repuestoRepository;
	/** Acceso a la base para los técnicos. */
	private final TecnicoRepository tecnicoRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param reparacionRepository acceso a la base para las reparaciones
	 * @param motoRepository acceso a la base para las motos
	 * @param ventaMotoRepository acceso a la base para las ventas de motos
	 * @param repuestoRepository acceso a la base para los repuestos
	 * @param tecnicoRepository acceso a la base para los técnicos
	 */
	public TallerService(ReparacionRepository reparacionRepository, MotoRepository motoRepository,
			VentaMotoRepository ventaMotoRepository, RepuestoRepository repuestoRepository,
			TecnicoRepository tecnicoRepository) {
		this.reparacionRepository = reparacionRepository;
		this.motoRepository = motoRepository;
		this.ventaMotoRepository = ventaMotoRepository;
		this.repuestoRepository = repuestoRepository;
		this.tecnicoRepository = tecnicoRepository;
	}

	/**
	 * Reparaciones de la sede entre dos fechas. El técnico siempre ve su sede; el dueño puede elegir.
	 *
	 * @param usuario    usuario con sesión iniciada
	 * @param idSucursal sede pedida por el dueño, o null para todas
	 * @param desde      primer día, o null
	 * @param hasta      último día, o null
	 * @return reparaciones de la más reciente a la más antigua
	 */
	@Transactional(readOnly = true)
	public List<ReparacionDTO> listar(UsuarioAutenticado usuario, Integer idSucursal, LocalDate desde, LocalDate hasta) {
		if (desde != null && hasta != null && desde.isAfter(hasta)) {
			throw new BadRequestException("La fecha inicial no puede ser posterior a la final.");
		}
		Integer sede = usuario.getRol() == Rol.DUENO ? idSucursal : usuario.getIdSucursal();
		return aDTOs(reparacionRepository.buscar(sede, desde, hasta));
	}

	/**
	 * Reparaciones en las que participó el técnico con sesión iniciada.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return reparaciones en las que participó el técnico con sesión iniciada
	 */
	@Transactional(readOnly = true)
	public List<ReparacionDTO> mias(UsuarioAutenticado usuario) {
		return aDTOs(reparacionRepository.deTecnico(usuario.getId()));
	}

	/**
	 * Detalle de una reparación; el técnico solo ve las de su sede.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param idReparacion identificador de la reparación
	 * @return detalle de una reparación; el técnico solo ve las de su sede
	 */
	@Transactional(readOnly = true)
	public ReparacionDTO detalle(UsuarioAutenticado usuario, Integer idReparacion) {
		return aDTO(visible(usuario, idReparacion));
	}

	/**
	 * Historial de reparaciones de una moto, de cualquier sede.
	 *
	 * @param placa placa de la moto
	 * @return historial de reparaciones de una moto, de cualquier sede
	 */
	@Transactional(readOnly = true)
	public List<ReparacionDTO> historial(String placa) {
		return aDTOs(reparacionRepository.findByMotoPlacaOrderByFechaDescIdReparacionDesc(normalizar(placa)));
	}

	/**
	 * Técnicos que se pueden agregar a una reparación: los de la sede del técnico, o todos para el dueño.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return técnicos ordenados por nombre
	 */
	@Transactional(readOnly = true)
	public List<TecnicoParticipante> tecnicos(UsuarioAutenticado usuario) {
		List<Tecnico> tecnicos = usuario.getRol() == Rol.DUENO ? tecnicoRepository.findAll()
				: tecnicoRepository.findBySucursalIdSucursal(usuario.getIdSucursal());
		return tecnicos.stream().map(TecnicoParticipante::de)
				.sorted((a, b) -> a.nombre().compareToIgnoreCase(b.nombre())).toList();
	}

	/**
	 * Registra una reparación ya hecha.
	 *
	 * @param usuario técnico o dueño que la registra
	 * @param datos   moto, fecha, trabajo, costo, técnicos y repuestos
	 * @return la reparación guardada
	 */
	@Transactional
	public ReparacionDTO registrar(UsuarioAutenticado usuario, NuevaReparacionDTO datos) {
		Reparacion reparacion = new Reparacion();
		reparacion.setMoto(motoVendida(datos.placa()));
		copiar(usuario, datos, reparacion);
		Integer id = reparacionRepository.saveAndFlush(reparacion).getIdReparacion();
		return aDTO(reparacionRepository.findById(id).orElseThrow());
	}

	/**
	 * Corrige una reparación. Solo puede hacerlo el dueño o un técnico que participó en ella.
	 *
	 * @param usuario      técnico o dueño
	 * @param idReparacion reparación a corregir
	 * @param datos        datos completos de la reparación
	 * @return la reparación corregida
	 */
	@Transactional
	public ReparacionDTO actualizar(UsuarioAutenticado usuario, Integer idReparacion, NuevaReparacionDTO datos) {
		Reparacion reparacion = visible(usuario, idReparacion);
		if (usuario.getRol() == Rol.TECNICO
				&& reparacion.getTecnicos().stream().noneMatch(t -> t.getIdEmpleado().equals(usuario.getId()))) {
			throw new ConflictException("Solo un técnico que participó puede corregir esta reparación.");
		}
		if (!reparacion.getMoto().getPlaca().equals(normalizar(datos.placa()))) {
			reparacion.setMoto(motoVendida(datos.placa()));
		}
		copiar(usuario, datos, reparacion);
		return aDTO(reparacionRepository.saveAndFlush(reparacion));
	}

	/**
	 * Copia los datos a la reparación; la fecha no puede ser anterior a la venta de la moto.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param datos datos de la reparación
	 * @param reparacion reparación que se modifica
	 */
	private void copiar(UsuarioAutenticado usuario, NuevaReparacionDTO datos, Reparacion reparacion) {
		VentaMoto venta = ventaMotoRepository.findById(reparacion.getMoto().getPlaca()).orElseThrow();
		if (datos.fecha().isBefore(venta.getFechaVenta().toLocalDate())) {
			throw new ConflictException("La reparación no puede ser anterior a la venta de la moto ("
					+ venta.getFechaVenta().toLocalDate() + ").");
		}
		reparacion.setFecha(datos.fecha());
		reparacion.setDescripcion(datos.descripcion().trim());
		reparacion.setCosto(datos.costo());
		Set<Tecnico> tecnicos = tecnicos(usuario, datos.idTecnicos());
		reparacion.getTecnicos().retainAll(tecnicos);
		reparacion.getTecnicos().addAll(tecnicos);
		ponerRepuestos(reparacion, datos.repuestos() == null ? List.of() : datos.repuestos());
	}

	/**
	 * Valida los técnicos: deben existir y ser de la misma sede; el técnico que registra se agrega solo.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param pedidos técnicos que llegan en la petición
	 * @return los técnicos de la reparación
	 */
	private Set<Tecnico> tecnicos(UsuarioAutenticado usuario, List<Integer> pedidos) {
		Set<Integer> ids = new LinkedHashSet<>(pedidos == null ? List.of() : pedidos);
		if (usuario.getRol() == Rol.TECNICO) {
			ids.add(usuario.getId());
		}
		if (ids.isEmpty()) {
			throw new BadRequestException("Indique al menos un técnico que haya hecho la reparación.");
		}
		Set<Tecnico> tecnicos = new LinkedHashSet<>(tecnicoRepository.findAllById(ids));
		if (tecnicos.size() != ids.size()) {
			throw new ResourceNotFoundException("Uno de los técnicos indicados no existe.");
		}
		Set<Integer> sedes = tecnicos.stream().map(t -> t.getSucursal().getIdSucursal()).collect(Collectors.toSet());
		if (sedes.size() > 1) {
			throw new ConflictException("Los técnicos de una reparación deben ser de la misma sede.");
		}
		if (usuario.getRol() == Rol.TECNICO && !sedes.contains(usuario.getIdSucursal())) {
			throw new ConflictException("Solo puede agregar técnicos de su sede.");
		}
		return tecnicos;
	}

	/**
	 * Deja en la reparación los repuestos pedidos: agrega, cambia o quita según la lista.
	 *
	 * @param reparacion reparación que se modifica
	 * @param usos repuestos con su cantidad
	 */
	private void ponerRepuestos(Reparacion reparacion, List<UsoRepuestoDTO> usos) {
		Set<Integer> vistos = new HashSet<>();
		for (UsoRepuestoDTO uso : usos) {
			if (!vistos.add(uso.idRepuesto())) {
				throw new BadRequestException("Cada repuesto debe aparecer una sola vez; ajuste la cantidad.");
			}
		}
		Map<Integer, Repuesto> repuestos = repuestoRepository.findAllById(vistos).stream()
				.collect(Collectors.toMap(Repuesto::getIdRepuesto, Function.identity()));
		reparacion.getRepuestos().removeIf(r -> !vistos.contains(r.getRepuesto().getIdRepuesto()));
		Map<Integer, RepuestoReparacion> actuales = reparacion.getRepuestos().stream()
				.collect(Collectors.toMap(r -> r.getRepuesto().getIdRepuesto(), Function.identity()));
		for (UsoRepuestoDTO uso : usos) {
			Repuesto repuesto = repuestos.get(uso.idRepuesto());
			if (repuesto == null) {
				throw new ResourceNotFoundException("No existe el repuesto con id " + uso.idRepuesto() + ".");
			}
			BigDecimal precio = uso.precioUnitario() == null ? repuesto.getPrecio() : uso.precioUnitario();
			RepuestoReparacion actual = actuales.get(uso.idRepuesto());
			if (actual == null) {
				reparacion.agregarRepuesto(repuesto, uso.cantidad(), precio);
			} else {
				actual.setCantidad(uso.cantidad());
				actual.setPrecioUnitario(precio);
			}
		}
	}

	/**
	 * Busca la moto y revisa que la haya vendido el concesionario.
	 *
	 * @param placa placa de la moto
	 * @return la moto
	 */
	private Moto motoVendida(String placa) {
		String normalizada = normalizar(placa);
		Moto moto = motoRepository.findById(normalizada)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la moto con placa " + normalizada + "."));
		if (!ventaMotoRepository.existsById(normalizada)) {
			throw new ConflictException("El taller solo atiende motos vendidas por el concesionario; la "
					+ normalizada + " no se ha vendido.");
		}
		return moto;
	}

	/**
	 * Busca la reparación; solo el dueño puede ver las de otras sedes.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param idReparacion identificador de la reparación
	 * @return la reparación
	 */
	private Reparacion visible(UsuarioAutenticado usuario, Integer idReparacion) {
		Reparacion reparacion = reparacionRepository.findById(idReparacion).orElseThrow(
				() -> new ResourceNotFoundException("No existe la reparación con id " + idReparacion + "."));
		if (usuario.getRol() != Rol.DUENO && reparacion.getSucursal() != null
				&& !reparacion.getSucursal().getIdSucursal().equals(usuario.getIdSucursal())) {
			throw new ConflictException("La reparación " + idReparacion + " pertenece a la "
					+ reparacion.getSucursal().getNombre() + ".");
		}
		return reparacion;
	}

	/**
	 * Arma el DTO con el cliente que compró la moto.
	 *
	 * @param reparacion reparación que se convierte
	 * @return el DTO de la reparación
	 */
	private ReparacionDTO aDTO(Reparacion reparacion) {
		return ReparacionDTO.de(reparacion, ventaMotoRepository.findById(reparacion.getMoto().getPlaca()).orElse(null));
	}

	/**
	 * Arma los DTO buscando las ventas una sola vez.
	 *
	 * @param reparaciones reparaciones que se convierten
	 * @return los DTO de las reparaciones
	 */
	private List<ReparacionDTO> aDTOs(List<Reparacion> reparaciones) {
		Map<String, VentaMoto> ventas = ventaMotoRepository.findAll().stream()
				.collect(Collectors.toMap(VentaMoto::getPlaca, Function.identity()));
		return reparaciones.stream().map(r -> ReparacionDTO.de(r, ventas.get(r.getMoto().getPlaca()))).toList();
	}

	/**
	 * Quita los espacios y pasa la placa a mayúsculas.
	 *
	 * @param placa placa como llega
	 * @return la placa normalizada
	 */
	private String normalizar(String placa) {
		return placa.trim().toUpperCase(Locale.ROOT);
	}
}

package co.edu.unbosque.BackMotos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReservaDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.entity.VentaMoto;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.ReservaRepository;
import co.edu.unbosque.BackMotos.repository.VentaAccesorioRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;
import jakarta.persistence.EntityManager;

/** Operaciones del dueño sobre reservas, ventas y reparaciones. */
@Service
public class OperacionesAdminService {

	/** Acceso a la base para las reservas. */
	private final ReservaRepository reservaRepository;
	/** Acceso a la base para las ventas de motos. */
	private final VentaMotoRepository ventaMotoRepository;
	/** Acceso a la base para las ventas de accesorios. */
	private final VentaAccesorioRepository ventaAccesorioRepository;
	/** Acceso a la base para las reparaciones. */
	private final ReparacionRepository reparacionRepository;
	/** Ejecuta SQL directo en la base. */
	private final JdbcTemplate jdbc;
	/** Permite refrescar entidades después de los triggers de la base. */
	private final EntityManager entityManager;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param reservaRepository acceso a la base para las reservas
	 * @param ventaMotoRepository acceso a la base para las ventas de motos
	 * @param ventaAccesorioRepository acceso a la base para las ventas de accesorios
	 * @param reparacionRepository acceso a la base para las reparaciones
	 * @param jdbc ejecuta SQL directo en la base
	 * @param entityManager permite refrescar entidades después de los triggers de la base
	 */
	public OperacionesAdminService(ReservaRepository reservaRepository, VentaMotoRepository ventaMotoRepository,
			VentaAccesorioRepository ventaAccesorioRepository, ReparacionRepository reparacionRepository,
			JdbcTemplate jdbc, EntityManager entityManager) {
		this.reservaRepository = reservaRepository;
		this.ventaMotoRepository = ventaMotoRepository;
		this.ventaAccesorioRepository = ventaAccesorioRepository;
		this.reparacionRepository = reparacionRepository;
		this.jdbc = jdbc;
		this.entityManager = entityManager;
	}

	/**
	 * Reservas de un estado, o todas si el estado es null.
	 *
	 * @param estado estado de las reservas; null para todas
	 * @return reservas de un estado, o todas si el estado es null
	 */
	@Transactional(readOnly = true)
	public List<ReservaDTO> reservas(EstadoReserva estado) {
		List<Reserva> reservas = estado == null ? reservaRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaReserva"))
				: reservaRepository.findByEstadoOrderByFechaVencimiento(estado);
		return reservas.stream().map(ReservaDTO::de).toList();
	}

	/**
	 * Reparaciones de todas las sedes o de una, entre dos fechas opcionales.
	 *
	 * @param idSucursal sede, o null para todas
	 * @param desde      primer día, o null
	 * @param hasta      último día, o null
	 * @return reparaciones de la más reciente a la más antigua
	 */
	@Transactional(readOnly = true)
	public List<ReparacionDTO> reparaciones(Integer idSucursal, LocalDate desde, LocalDate hasta) {
		Map<String, VentaMoto> ventas = ventaMotoRepository.findAll().stream()
				.collect(Collectors.toMap(VentaMoto::getPlaca, Function.identity()));
		return reparacionRepository.buscar(idSucursal, desde, hasta).stream()
				.map(r -> ReparacionDTO.de(r, ventas.get(r.getMoto().getPlaca()))).toList();
	}

	/**
	 * Borra una reserva. Si está activa primero la cancela para liberar la moto.
	 * No borra reservas concretadas.
	 *
	 * @param idReserva identificador de la reserva
	 */
	@Transactional
	public void eliminarReserva(Integer idReserva) {
		Reserva reserva = reservaRepository.findById(idReserva)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la reserva con id " + idReserva + "."));
		if (reserva.getEstado() == EstadoReserva.CONCRETADA) {
			throw new ConflictException("La reserva ya se convirtió en venta; elimine primero la venta.");
		}
		if (reserva.getEstado() == EstadoReserva.ACTIVA) {
			reserva.setEstado(EstadoReserva.CANCELADA);
			reservaRepository.saveAndFlush(reserva);
		}
		reservaRepository.delete(reserva);
		reservaRepository.flush();
	}

	/**
	 * Borra la venta de una moto. La base deja la moto disponible otra vez
	 * y aquí se cancela la reserva que esa venta había concretado.
	 *
	 * @param placa placa de la moto vendida
	 */
	@Transactional
	public void eliminarVentaMoto(String placa) {
		String normalizada = placa.trim().toUpperCase(Locale.ROOT);
		VentaMoto venta = ventaMotoRepository.findById(normalizada)
				.orElseThrow(() -> new ResourceNotFoundException("No existe una venta de la moto " + normalizada + "."));
		Integer idCliente = venta.getCliente().getIdCliente();
		ventaMotoRepository.delete(venta);
		ventaMotoRepository.flush();
		reservaRepository.findByMotoPlacaAndEstado(normalizada, EstadoReserva.CONCRETADA)
				.filter(r -> r.getCliente().getIdCliente().equals(idCliente)).ifPresent(r -> {
					r.setEstado(EstadoReserva.CANCELADA);
					reservaRepository.saveAndFlush(r);
				});
		entityManager.clear();
	}

	/**
	 * Borra una venta de accesorios con sus líneas.
	 *
	 * @param idVentaAccesorio identificador de la venta de accesorios
	 */
	@Transactional
	public void eliminarVentaAccesorio(Integer idVentaAccesorio) {
		ventaAccesorioRepository.delete(ventaAccesorioRepository.findById(idVentaAccesorio).orElseThrow(
				() -> new ResourceNotFoundException("No existe la venta de accesorios con id " + idVentaAccesorio + ".")));
		ventaAccesorioRepository.flush();
	}

	/**
	 * Borra una reparación con sus técnicos y repuestos.
	 *
	 * @param idReparacion identificador de la reparación
	 */
	@Transactional
	public void eliminarReparacion(Integer idReparacion) {
		reparacionRepository.delete(reparacionRepository.findById(idReparacion).orElseThrow(
				() -> new ResourceNotFoundException("No existe la reparación con id " + idReparacion + ".")));
		reparacionRepository.flush();
	}

	/**
	 * Llama a sp_vencer_reservas() y devuelve cuántas reservas venció.
	 *
	 * @return cuántas reservas se vencieron
	 */
	@Transactional
	public int vencerReservas() {
		entityManager.flush();
		Integer vencidas = jdbc.queryForObject("SELECT sp_vencer_reservas()", Integer.class);
		entityManager.clear();
		return vencidas == null ? 0 : vencidas;
	}
}

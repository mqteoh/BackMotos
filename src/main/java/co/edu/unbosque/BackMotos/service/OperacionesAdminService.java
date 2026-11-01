package co.edu.unbosque.BackMotos.service;

import java.util.EnumSet;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReservaDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
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

@Service
public class OperacionesAdminService {

	private final ReservaRepository reservaRepository;
	private final VentaMotoRepository ventaMotoRepository;
	private final VentaAccesorioRepository ventaAccesorioRepository;
	private final ReparacionRepository reparacionRepository;
	private final JdbcTemplate jdbc;
	private final EntityManager entityManager;

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

	@Transactional(readOnly = true)
	public List<ReservaDTO> reservas(EstadoReserva estado) {
		List<Reserva> reservas = estado == null ? reservaRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaReserva"))
				: reservaRepository.findByEstadoOrderByFechaVencimiento(estado);
		return reservas.stream().map(ReservaDTO::de).toList();
	}

	@Transactional(readOnly = true)
	public List<ReparacionDTO> reparaciones(Integer idSucursal, List<EstadoReparacion> estados) {
		EnumSet<EstadoReparacion> filtro = estados == null || estados.isEmpty()
				? EnumSet.allOf(EstadoReparacion.class)
				: EnumSet.copyOf(estados);
		return reparacionRepository.agenda(idSucursal, filtro, null, null).stream().map(ReparacionDTO::de).toList();
	}

	@Transactional
	public void eliminarReserva(Integer idReserva) {
		Reserva reserva = reservaRepository.findById(idReserva)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la reserva con id " + idReserva + "."));
		if (reserva.getEstado() == EstadoReserva.CONCRETADA) {
			throw new ConflictException("La reserva ya se convirtió en venta; elimine primero la venta.");
		}
		reservaRepository.delete(reserva);
		reservaRepository.flush();
	}

	@Transactional
	public void eliminarVentaMoto(Integer idVentaMoto) {
		VentaMoto venta = ventaMotoRepository.findById(idVentaMoto)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la venta con id " + idVentaMoto + "."));
		Reserva reserva = venta.getReserva();
		ventaMotoRepository.delete(venta);
		ventaMotoRepository.flush();
		if (reserva != null) {
			reserva.setEstado(EstadoReserva.CANCELADA);
			reservaRepository.saveAndFlush(reserva);
		}
	}

	@Transactional
	public void eliminarVentaAccesorio(Integer idVentaAccesorio) {
		ventaAccesorioRepository.delete(ventaAccesorioRepository.findById(idVentaAccesorio).orElseThrow(
				() -> new ResourceNotFoundException("No existe la venta de accesorios con id " + idVentaAccesorio + ".")));
		ventaAccesorioRepository.flush();
	}

	@Transactional
	public void eliminarReparacion(Integer idReparacion) {
		reparacionRepository.delete(reparacionRepository.findById(idReparacion).orElseThrow(
				() -> new ResourceNotFoundException("No existe la reparación con id " + idReparacion + ".")));
		reparacionRepository.flush();
	}

	@Transactional
	public int vencerReservas() {
		entityManager.flush();
		Integer vencidas = jdbc.queryForObject("CALL sp_vencer_reservas()", Integer.class);
		entityManager.clear();
		return vencidas == null ? 0 : vencidas;
	}
}

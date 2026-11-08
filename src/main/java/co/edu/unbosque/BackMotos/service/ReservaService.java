package co.edu.unbosque.BackMotos.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ReservaDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.MotoDisponibleRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReservaRepository;

/** Reservas de motos hechas por los clientes. */
@Service
public class ReservaService {

	/** Días que dura una reserva. */
	public static final int DIAS_VIGENCIA = 7;

	/** Acceso a la base para las reservas. */
	private final ReservaRepository reservaRepository;
	/** Acceso a la base para las motos. */
	private final MotoRepository motoRepository;
	/** Acceso a la base para las motos disponibles. */
	private final MotoDisponibleRepository motoDisponibleRepository;
	/** Acceso a la base para los clientes. */
	private final ClienteRepository clienteRepository;
	/** Reloj para saber la fecha y hora actual. */
	private final Clock reloj;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param reservaRepository acceso a la base para las reservas
	 * @param motoRepository acceso a la base para las motos
	 * @param motoDisponibleRepository acceso a la base para las motos disponibles
	 * @param clienteRepository acceso a la base para los clientes
	 * @param reloj reloj para saber la fecha y hora actual
	 */
	public ReservaService(ReservaRepository reservaRepository, MotoRepository motoRepository,
			MotoDisponibleRepository motoDisponibleRepository, ClienteRepository clienteRepository, Clock reloj) {
		this.reservaRepository = reservaRepository;
		this.motoRepository = motoRepository;
		this.motoDisponibleRepository = motoDisponibleRepository;
		this.clienteRepository = clienteRepository;
		this.reloj = reloj;
	}

	/**
	 * Reserva una moto disponible por DIAS_VIGENCIA días; el trigger la marca RESERVADA.
	 *
	 * @param idCliente identificador del cliente
	 * @param placa placa de la moto
	 * @return la reserva creada
	 */
	@Transactional
	public ReservaDTO reservarParaCliente(Integer idCliente, String placa) {
		String normalizada = placa.trim().toUpperCase(Locale.ROOT);
		if (!motoDisponibleRepository.existsById(normalizada)) {
			throw new ConflictException("La moto " + normalizada + " no está disponible para reservar.");
		}
		Reserva reserva = new Reserva();
		reserva.setFechaReserva(LocalDateTime.now(reloj).truncatedTo(ChronoUnit.SECONDS));
		reserva.setFechaVencimiento(LocalDate.now(reloj).plusDays(DIAS_VIGENCIA));
		reserva.setEstado(EstadoReserva.ACTIVA);
		reserva.setMoto(motoRepository.getReferenceById(normalizada));
		reserva.setCliente(clienteRepository.getReferenceById(idCliente));
		Integer id = reservaRepository.saveAndFlush(reserva).getIdReserva();
		return ReservaDTO.de(reservaRepository.findById(id).orElseThrow());
	}

	/**
	 * Reservas del cliente, de la más reciente a la más antigua.
	 *
	 * @param idCliente identificador del cliente
	 * @return reservas del cliente, de la más reciente a la más antigua
	 */
	@Transactional(readOnly = true)
	public List<ReservaDTO> deCliente(Integer idCliente) {
		return reservaRepository.findByClienteIdClienteOrderByFechaReservaDesc(idCliente).stream().map(ReservaDTO::de)
				.toList();
	}

	/**
	 * Reservas activas de una sede, o de todas si la sede es null.
	 *
	 * @param idSucursal identificador de la sede
	 * @return reservas activas de una sede, o de todas si la sede es null
	 */
	@Transactional(readOnly = true)
	public List<ReservaDTO> activasDeSede(Integer idSucursal) {
		List<Reserva> activas = idSucursal == null
				? reservaRepository.findByEstadoOrderByFechaVencimiento(EstadoReserva.ACTIVA)
				: reservaRepository.findByEstadoAndMotoSucursalIdSucursalOrderByFechaVencimiento(EstadoReserva.ACTIVA,
						idSucursal);
		return activas.stream().map(ReservaDTO::de).toList();
	}

	/**
	 * Cancela una reserva activa del propio cliente; la moto vuelve a estar disponible.
	 *
	 * @param idCliente identificador del cliente
	 * @param idReserva identificador de la reserva
	 * @return la reserva cancelada
	 */
	@Transactional
	public ReservaDTO cancelarDeCliente(Integer idCliente, Integer idReserva) {
		Reserva reserva = reservaRepository.findById(idReserva)
				.filter(r -> r.getCliente().getIdCliente().equals(idCliente))
				.orElseThrow(() -> new ResourceNotFoundException("No tiene una reserva con id " + idReserva + "."));
		if (reserva.getEstado() != EstadoReserva.ACTIVA) {
			throw new ConflictException("Solo se pueden cancelar reservas activas.");
		}
		reserva.setEstado(EstadoReserva.CANCELADA);
		return ReservaDTO.de(reservaRepository.saveAndFlush(reserva));
	}
}

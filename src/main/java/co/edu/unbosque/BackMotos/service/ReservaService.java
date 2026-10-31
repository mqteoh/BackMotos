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

@Service
public class ReservaService {

	public static final int DIAS_VIGENCIA = 7;

	private final ReservaRepository reservaRepository;
	private final MotoRepository motoRepository;
	private final MotoDisponibleRepository motoDisponibleRepository;
	private final ClienteRepository clienteRepository;
	private final Clock reloj;

	public ReservaService(ReservaRepository reservaRepository, MotoRepository motoRepository,
			MotoDisponibleRepository motoDisponibleRepository, ClienteRepository clienteRepository, Clock reloj) {
		this.reservaRepository = reservaRepository;
		this.motoRepository = motoRepository;
		this.motoDisponibleRepository = motoDisponibleRepository;
		this.clienteRepository = clienteRepository;
		this.reloj = reloj;
	}

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

	@Transactional(readOnly = true)
	public List<ReservaDTO> deCliente(Integer idCliente) {
		return reservaRepository.findByClienteIdClienteOrderByFechaReservaDesc(idCliente).stream().map(ReservaDTO::de)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<ReservaDTO> activasDeSede(Integer idSucursal) {
		List<Reserva> activas = idSucursal == null
				? reservaRepository.findByEstadoOrderByFechaVencimiento(EstadoReserva.ACTIVA)
				: reservaRepository.findByEstadoAndMotoSucursalIdSucursalOrderByFechaVencimiento(EstadoReserva.ACTIVA,
						idSucursal);
		return activas.stream().map(ReservaDTO::de).toList();
	}

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

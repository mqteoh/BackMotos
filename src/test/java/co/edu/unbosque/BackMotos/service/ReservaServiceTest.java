package co.edu.unbosque.BackMotos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.BackMotos.dto.ReservaDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.MotoDisponibleRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReservaRepository;

class ReservaServiceTest {

	private ReservaRepository reservas;
	private MotoRepository motos;
	private MotoDisponibleRepository disponibles;
	private ClienteRepository clientes;
	private ReservaService servicio;

	@BeforeEach
	void preparar() {
		reservas = mock(ReservaRepository.class);
		motos = mock(MotoRepository.class);
		disponibles = mock(MotoDisponibleRepository.class);
		clientes = mock(ClienteRepository.class);
		servicio = new ReservaService(reservas, motos, disponibles, clientes, Fixtures.RELOJ);
	}

	@Test
	void reservaDuraSieteDiasDesdeHoyEnBogota() {
		Moto moto = Fixtures.moto("GTK22F", Fixtures.sede(1, "Sede Chapinero"));
		when(disponibles.existsById("GTK22F")).thenReturn(true);
		when(motos.getReferenceById("GTK22F")).thenReturn(moto);
		when(clientes.getReferenceById(3)).thenReturn(Fixtures.cliente(3));
		AtomicReference<Reserva> guardada = new AtomicReference<>();
		when(reservas.saveAndFlush(any())).thenAnswer(i -> {
			Reserva r = i.getArgument(0);
			r.setIdReserva(50);
			guardada.set(r);
			return r;
		});
		when(reservas.findById(50)).thenAnswer(i -> Optional.of(guardada.get()));

		ReservaDTO creada = servicio.reservarParaCliente(3, " gtk22f ");

		assertThat(creada.placa()).isEqualTo("GTK22F");
		assertThat(creada.fechaVencimiento()).isEqualTo(LocalDate.of(2026, 11, 7));
		assertThat(creada.fechaReserva()).isEqualTo(LocalDateTime.of(2026, 10, 31, 10, 0));
		assertThat(creada.estado()).isEqualTo(EstadoReserva.ACTIVA);
	}

	@Test
	void noReservaUnaMotoQueNoEstaDisponible() {
		when(disponibles.existsById("JKL78D")).thenReturn(false);
		assertThatThrownBy(() -> servicio.reservarParaCliente(3, "JKL78D")).isInstanceOf(ConflictException.class);
		verify(reservas, never()).saveAndFlush(any());
	}

	@Test
	void soloCancelaReservasPropiasYActivas() {
		Reserva reserva = new Reserva();
		reserva.setIdReserva(8);
		reserva.setCliente(Fixtures.cliente(2));
		reserva.setMoto(Fixtures.moto("MNP12E", Fixtures.sede(1, "Sede Chapinero")));
		reserva.setEstado(EstadoReserva.ACTIVA);
		when(reservas.findById(8)).thenReturn(Optional.of(reserva));
		when(reservas.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));

		assertThatThrownBy(() -> servicio.cancelarDeCliente(1, 8)).isInstanceOf(ResourceNotFoundException.class);
		ReservaDTO cancelada = servicio.cancelarDeCliente(2, 8);
		assertThat(cancelada.estado()).isEqualTo(EstadoReserva.CANCELADA);
		assertThatThrownBy(() -> servicio.cancelarDeCliente(2, 8)).isInstanceOf(ConflictException.class);
	}
}

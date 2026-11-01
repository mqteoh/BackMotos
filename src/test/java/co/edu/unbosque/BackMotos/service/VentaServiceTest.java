package co.edu.unbosque.BackMotos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.BackMotos.dto.NuevaVentaAccesorioDTO;
import co.edu.unbosque.BackMotos.dto.NuevaVentaMotoDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.entity.Sucursal;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.AccesorioRepository;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReservaRepository;
import co.edu.unbosque.BackMotos.repository.VendedorRepository;
import co.edu.unbosque.BackMotos.repository.VentaAccesorioRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import jakarta.persistence.EntityManager;

class VentaServiceTest {

	private final Sucursal chapinero = Fixtures.sede(1, "Sede Chapinero");
	private final Sucursal suba = Fixtures.sede(2, "Sede Suba");
	private final UsuarioAutenticado laura = Fixtures.usuario(2, Rol.VENDEDOR, 1);
	private VentaMotoRepository ventasMoto;
	private VentaAccesorioRepository ventasAccesorio;
	private MotoRepository motos;
	private ClienteRepository clientes;
	private ReservaRepository reservas;
	private VentaService servicio;

	@BeforeEach
	void preparar() {
		ventasMoto = mock(VentaMotoRepository.class);
		ventasAccesorio = mock(VentaAccesorioRepository.class);
		motos = mock(MotoRepository.class);
		clientes = mock(ClienteRepository.class);
		reservas = mock(ReservaRepository.class);
		VendedorRepository vendedores = mock(VendedorRepository.class);
		when(vendedores.findById(2)).thenReturn(Optional.of(Fixtures.vendedor(2, chapinero)));
		when(clientes.findById(3)).thenReturn(Optional.of(Fixtures.cliente(3)));
		servicio = new VentaService(ventasMoto, ventasAccesorio, motos, clientes, vendedores, reservas,
				mock(AccesorioRepository.class), mock(EntityManager.class), Fixtures.RELOJ);
	}

	private NuevaVentaMotoDTO venta(String placa) {
		return new NuevaVentaMotoDTO(placa, 3, MetodoPago.EFECTIVO, TipoFactura.POS, new BigDecimal("15000000"), null);
	}

	@Test
	void noVendeMotosDeOtraSede() {
		when(motos.findById("QRS34A")).thenReturn(Optional.of(Fixtures.moto("QRS34A", suba)));
		assertThatThrownBy(() -> servicio.registrarVentaMoto(laura, venta("qrs34a")))
				.isInstanceOf(ConflictException.class).hasMessageContaining("Sede Suba");
		verify(ventasMoto, never()).saveAndFlush(any());
	}

	@Test
	void noVendeUnaMotoReservadaPorOtroCliente() {
		when(motos.findById("MNP12E")).thenReturn(Optional.of(Fixtures.moto("MNP12E", chapinero)));
		Reserva deJuan = new Reserva();
		deJuan.setCliente(Fixtures.cliente(2));
		deJuan.setEstado(EstadoReserva.ACTIVA);
		deJuan.setFechaVencimiento(LocalDate.of(2026, 11, 3));
		when(reservas.findByMotoPlacaAndEstado("MNP12E", EstadoReserva.ACTIVA)).thenReturn(Optional.of(deJuan));
		assertThatThrownBy(() -> servicio.registrarVentaMoto(laura, venta("MNP12E")))
				.isInstanceOf(ConflictException.class).hasMessageContaining("2026-11-03");
		verify(ventasMoto, never()).saveAndFlush(any());
	}

	@Test
	void unAccesorioNoPuedeRepetirseEnLaMismaVenta() {
		NuevaVentaAccesorioDTO datos = new NuevaVentaAccesorioDTO(3, MetodoPago.EFECTIVO, TipoFactura.POS,
				List.of(new NuevaVentaAccesorioDTO.Item(4, 1, null), new NuevaVentaAccesorioDTO.Item(4, 2, null)));
		assertThatThrownBy(() -> servicio.registrarVentaAccesorio(laura, datos)).isInstanceOf(BadRequestException.class);
		verify(ventasAccesorio, never()).saveAndFlush(any());
	}
}

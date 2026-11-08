package co.edu.unbosque.BackMotos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.BackMotos.dto.NuevaReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.UsoRepuestoDTO;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.Repuesto;
import co.edu.unbosque.BackMotos.entity.Sucursal;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.VentaMoto;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;
import co.edu.unbosque.BackMotos.repository.TecnicoRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

class TallerServiceTest {

	private final Sucursal chapinero = Fixtures.sede(1, "Sede Chapinero");
	private final Sucursal suba = Fixtures.sede(2, "Sede Suba");
	private final Tecnico jhon = Fixtures.tecnico(4, chapinero);
	private final Tecnico felipe = Fixtures.tecnico(5, chapinero);
	private final Tecnico diego = Fixtures.tecnico(7, suba);
	private final UsuarioAutenticado sesionJhon = Fixtures.usuario(4, Rol.TECNICO, 1);
	private ReparacionRepository reparaciones;
	private RepuestoRepository repuestos;
	private TecnicoRepository tecnicos;
	private VentaMotoRepository ventas;
	private TallerService servicio;
	private Moto vendida;

	@BeforeEach
	void preparar() {
		reparaciones = mock(ReparacionRepository.class);
		repuestos = mock(RepuestoRepository.class);
		tecnicos = mock(TecnicoRepository.class);
		ventas = mock(VentaMotoRepository.class);
		MotoRepository motos = mock(MotoRepository.class);
		servicio = new TallerService(reparaciones, motos, ventas, repuestos, tecnicos);
		vendida = Fixtures.moto("HBN45C", chapinero);
		VentaMoto venta = new VentaMoto();
		venta.setMoto(vendida);
		venta.setPlaca("HBN45C");
		venta.setCliente(Fixtures.cliente(8));
		venta.setFechaVenta(LocalDateTime.of(2026, 5, 30, 11, 50));
		when(motos.findById("HBN45C")).thenReturn(Optional.of(vendida));
		when(motos.findById("GTK22F")).thenReturn(Optional.of(Fixtures.moto("GTK22F", chapinero)));
		when(ventas.existsById("HBN45C")).thenReturn(true);
		when(ventas.findById("HBN45C")).thenReturn(Optional.of(venta));
		AtomicReference<Reparacion> guardada = new AtomicReference<>();
		when(reparaciones.saveAndFlush(any())).thenAnswer(i -> {
			Reparacion r = i.getArgument(0);
			r.setIdReparacion(20);
			guardada.set(r);
			return r;
		});
		when(reparaciones.findById(20)).thenAnswer(i -> Optional.ofNullable(guardada.get()));
		Repuesto aceite = new Repuesto();
		aceite.setIdRepuesto(1);
		aceite.setNombre("Aceite");
		aceite.setPrecio(new BigDecimal("38000"));
		when(repuestos.findAllById(anyIterable())).thenReturn(List.of(aceite));
	}

	private NuevaReparacionDTO datos(String placa, LocalDate fecha, List<Integer> idTecnicos,
			List<UsoRepuestoDTO> usos) {
		return new NuevaReparacionDTO(placa, fecha, "Cambio de aceite", new BigDecimal("50000"), idTecnicos, usos);
	}

	@Test
	void elTecnicoQueRegistraQuedaComoParticipante() {
		when(tecnicos.findAllById(anyIterable())).thenReturn(List.of(jhon, felipe));
		ReparacionDTO dto = servicio.registrar(sesionJhon,
				datos("hbn45c", LocalDate.of(2026, 10, 1), List.of(5), List.of(new UsoRepuestoDTO(1, 2, null))));
		assertThat(dto.tecnicos()).extracting(ReparacionDTO.TecnicoParticipante::idEmpleado).containsExactlyInAnyOrder(4, 5);
		assertThat(dto.costoRepuestos()).isEqualByComparingTo("76000");
		assertThat(dto.costoTotal()).isEqualByComparingTo("126000");
		assertThat(dto.idCliente()).isEqualTo(8);
	}

	@Test
	void soloSeReparanMotosVendidas() {
		assertThatThrownBy(() -> servicio.registrar(sesionJhon, datos("GTK22F", LocalDate.of(2026, 10, 1), null, null)))
				.isInstanceOf(ConflictException.class).hasMessageContaining("vendidas");
		verify(reparaciones, never()).saveAndFlush(any());
	}

	@Test
	void laReparacionNoPuedeSerAnteriorALaVenta() {
		when(tecnicos.findAllById(anyIterable())).thenReturn(List.of(jhon));
		assertThatThrownBy(() -> servicio.registrar(sesionJhon, datos("HBN45C", LocalDate.of(2026, 5, 1), null, null)))
				.isInstanceOf(ConflictException.class).hasMessageContaining("anterior a la venta");
	}

	@Test
	void losTecnicosDebenSerDeLaMismaSede() {
		when(tecnicos.findAllById(anyIterable())).thenReturn(List.of(jhon, diego));
		assertThatThrownBy(
				() -> servicio.registrar(sesionJhon, datos("HBN45C", LocalDate.of(2026, 10, 1), List.of(7), null)))
				.isInstanceOf(ConflictException.class).hasMessageContaining("misma sede");
	}

	@Test
	void elDuenoDebeIndicarAlMenosUnTecnico() {
		UsuarioAutenticado dueno = Fixtures.usuario(1, Rol.DUENO, null);
		assertThatThrownBy(() -> servicio.registrar(dueno, datos("HBN45C", LocalDate.of(2026, 10, 1), List.of(), null)))
				.isInstanceOf(BadRequestException.class).hasMessageContaining("al menos un técnico");
	}

	@Test
	void unRepuestoRepetidoSeRechaza() {
		when(tecnicos.findAllById(anyIterable())).thenReturn(List.of(jhon));
		List<UsoRepuestoDTO> repetidos = List.of(new UsoRepuestoDTO(1, 1, null), new UsoRepuestoDTO(1, 2, null));
		assertThatThrownBy(
				() -> servicio.registrar(sesionJhon, datos("HBN45C", LocalDate.of(2026, 10, 1), null, repetidos)))
				.isInstanceOf(BadRequestException.class);
	}

	@Test
	void soloUnParticipanteCorrigeLaReparacion() {
		Reparacion reparacion = new Reparacion();
		reparacion.setIdReparacion(9);
		reparacion.setMoto(vendida);
		reparacion.getTecnicos().add(felipe);
		when(reparaciones.findById(9)).thenReturn(Optional.of(reparacion));
		assertThatThrownBy(() -> servicio.actualizar(sesionJhon, 9, datos("HBN45C", LocalDate.of(2026, 10, 1), null, null)))
				.isInstanceOf(ConflictException.class).hasMessageContaining("participó");
		UsuarioAutenticado deOtraSede = Fixtures.usuario(7, Rol.TECNICO, 2);
		assertThatThrownBy(() -> servicio.detalle(deOtraSede, 9)).isInstanceOf(ConflictException.class)
				.hasMessageContaining("Sede Chapinero");
	}
}

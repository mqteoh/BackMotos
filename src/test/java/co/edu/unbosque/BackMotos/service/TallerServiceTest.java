package co.edu.unbosque.BackMotos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.BackMotos.dto.FinalizarReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.UsoRepuestoDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.Repuesto;
import co.edu.unbosque.BackMotos.entity.Sucursal;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;
import co.edu.unbosque.BackMotos.repository.TecnicoRepository;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

class TallerServiceTest {

	private final Sucursal chapinero = Fixtures.sede(1, "Sede Chapinero");
	private ReparacionRepository reparaciones;
	private RepuestoRepository repuestos;
	private TecnicoRepository tecnicos;
	private TallerService servicio;
	private Reparacion reparacion;
	private final UsuarioAutenticado jhon = Fixtures.usuario(4, Rol.TECNICO, 1);

	@BeforeEach
	void preparar() {
		reparaciones = mock(ReparacionRepository.class);
		repuestos = mock(RepuestoRepository.class);
		tecnicos = mock(TecnicoRepository.class);
		servicio = new TallerService(reparaciones, repuestos, tecnicos, Fixtures.RELOJ);
		reparacion = new Reparacion();
		reparacion.setIdReparacion(11);
		reparacion.setMoto(Fixtures.moto("HBN45C", chapinero));
		reparacion.setCliente(Fixtures.cliente(8));
		reparacion.setSucursal(chapinero);
		reparacion.setFechaAgendada(LocalDateTime.of(2026, 11, 2, 8, 0));
		reparacion.setDescripcion("Mantenimiento");
		when(reparaciones.findById(11)).thenReturn(Optional.of(reparacion));
		when(reparaciones.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
	}

	private void enProcesoCon(Tecnico tecnico) {
		reparacion.setEstado(EstadoReparacion.EN_PROCESO);
		reparacion.setTecnico(tecnico);
	}

	@Test
	void tecnicoDeOtraSedeNoPuedeTomarla() {
		UsuarioAutenticado diego = Fixtures.usuario(7, Rol.TECNICO, 2);
		assertThatThrownBy(() -> servicio.tomar(diego, 11)).isInstanceOf(ConflictException.class)
				.hasMessageContaining("Sede Chapinero");
	}

	@Test
	void tomarAsignaAlTecnicoYSoloSiEstaAgendada() {
		when(tecnicos.findById(4)).thenReturn(Optional.of(Fixtures.tecnico(4, chapinero)));
		ReparacionDTO tomada = servicio.tomar(jhon, 11);
		assertThat(tomada.estado()).isEqualTo(EstadoReparacion.EN_PROCESO);
		assertThat(tomada.idTecnico()).isEqualTo(4);
		assertThatThrownBy(() -> servicio.tomar(jhon, 11)).isInstanceOf(ConflictException.class);
	}

	@Test
	void registrarDosVecesElMismoRepuestoActualizaLaLinea() {
		enProcesoCon(Fixtures.tecnico(4, chapinero));
		Repuesto aceite = new Repuesto();
		aceite.setIdRepuesto(1);
		aceite.setNombre("Aceite");
		aceite.setPrecio(new BigDecimal("38000"));
		when(repuestos.findById(1)).thenReturn(Optional.of(aceite));
		servicio.registrarRepuesto(jhon, 11, new UsoRepuestoDTO(1, 1, null));
		ReparacionDTO resultado = servicio.registrarRepuesto(jhon, 11, new UsoRepuestoDTO(1, 3, null));
		assertThat(resultado.repuestos()).hasSize(1);
		assertThat(resultado.costoRepuestos()).isEqualByComparingTo("114000");
	}

	@Test
	void soloElTecnicoAsignadoModificaYSeExigeCostoParaCerrar() {
		enProcesoCon(Fixtures.tecnico(5, chapinero));
		assertThatThrownBy(() -> servicio.finalizar(jhon, 11, new FinalizarReparacionDTO(new BigDecimal("1"), null)))
				.isInstanceOf(ConflictException.class).hasMessageContaining("técnico asignado");
		enProcesoCon(Fixtures.tecnico(4, chapinero));
		assertThatThrownBy(() -> servicio.finalizar(jhon, 11, new FinalizarReparacionDTO(null, null)))
				.isInstanceOf(ConflictException.class).hasMessageContaining("costo");
		verify(reparaciones, never()).saveAndFlush(any());
		ReparacionDTO cerrada = servicio.finalizar(jhon, 11, new FinalizarReparacionDTO(new BigDecimal("80000"), null));
		assertThat(cerrada.estado()).isEqualTo(EstadoReparacion.FINALIZADA);
		assertThat(cerrada.fechaFinalizacion()).isEqualTo(LocalDateTime.of(2026, 10, 31, 10, 0));
	}
}

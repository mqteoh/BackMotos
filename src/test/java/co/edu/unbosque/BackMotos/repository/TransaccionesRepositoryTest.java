package co.edu.unbosque.BackMotos.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.entity.Suministro;
import co.edu.unbosque.BackMotos.entity.SuministroId;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import co.edu.unbosque.BackMotos.entity.VentaAccesorio;
import co.edu.unbosque.BackMotos.entity.VentaMoto;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TransaccionesRepositoryTest {

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private VentaMotoRepository ventaMotoRepository;

	@Autowired
	private VentaAccesorioRepository ventaAccesorioRepository;

	@Autowired
	private TotalVentaAccesorioRepository totalVentaAccesorioRepository;

	@Autowired
	private ReparacionRepository reparacionRepository;

	@Autowired
	private CostoReparacionRepository costoReparacionRepository;

	@Autowired
	private SuministroRepository suministroRepository;

	@Autowired
	private MotoRepository motoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private VendedorRepository vendedorRepository;

	@Autowired
	private AccesorioRepository accesorioRepository;

	@Autowired
	private RepuestoRepository repuestoRepository;

	@Autowired
	private ProveedorRepository proveedorRepository;

	@Autowired
	private SucursalRepository sucursalRepository;

	@Autowired
	private TestEntityManager em;

	@Test
	void ventaDeMotoDesdeReservaConcretada() {
		VentaMoto venta = ventaMotoRepository.findByMotoPlaca("HBN46C").orElseThrow();
		assertThat(venta.getReserva().getEstado()).isEqualTo(EstadoReserva.CONCRETADA);
		assertThat(venta.getCliente().getIdCliente()).isEqualTo(venta.getReserva().getCliente().getIdCliente());
		assertThat(venta.getVendedor().getNombres()).isEqualTo("Laura");
		assertThat(ventaMotoRepository.findByVendedorIdEmpleadoOrderByFechaVentaDesc(8)).hasSize(3);
	}

	@Test
	void totalDeVentaDeAccesoriosCoincideConLaVista() {
		VentaAccesorio venta = ventaAccesorioRepository.findById(1).orElseThrow();
		assertThat(venta.getDetalles()).hasSize(3);
		assertThat(venta.getValorTotal()).isEqualByComparingTo("577000");
		assertThat(totalVentaAccesorioRepository.findById(1).orElseThrow().getValorTotal()).isEqualByComparingTo("577000");
	}

	@Test
	void costoDeReparacionSumaManoDeObraYRepuestos() {
		Reparacion reparacion = reparacionRepository.findById(2).orElseThrow();
		assertThat(reparacion.getMoto().getPlaca()).isEqualTo("BCD47E");
		assertThat(reparacion.getTecnico().getNombres()).isEqualTo("Felipe");
		assertThat(reparacion.getRepuestos()).hasSize(3);
		assertThat(reparacion.getCostoTotal()).isEqualByComparingTo("488000");
		assertThat(costoReparacionRepository.findById(2).orElseThrow().getCostoTotal()).isEqualByComparingTo("488000");
	}

	@Test
	void reparacionAgendadaNoTieneTecnico() {
		Reparacion agendada = reparacionRepository.findByMotoPlacaOrderByFechaAgendada("HBN45C").get(0);
		assertThat(agendada.getEstado()).isEqualTo(EstadoReparacion.AGENDADA);
		assertThat(agendada.getTecnico()).isNull();
		assertThat(agendada.getCosto()).isNull();
	}

	@Test
	void consultasDeReservasYSuministros() {
		assertThat(reservaRepository.findByClienteIdClienteOrderByFechaReservaDesc(2)).hasSize(2);
		assertThat(reservaRepository.findByEstadoAndMotoSucursalIdSucursalOrderByFechaVencimiento(EstadoReserva.ACTIVA, 1))
				.extracting(r -> r.getMoto().getPlaca()).containsExactly("MNP12E");
		assertThat(suministroRepository.count()).isEqualTo(21);
		assertThat(suministroRepository.findByProveedorNitOrderByIdFechaSuministroDesc("900456789-1")).hasSize(8);
	}

	@Test
	void unaMotoNoAdmiteDosReservasActivas() {
		reservaRepository.saveAndFlush(nuevaReserva("GTK22F", 3));
		assertThatThrownBy(() -> reservaRepository.saveAndFlush(nuevaReserva("GTK22F", 4))).rootCause()
				.isInstanceOf(SQLException.class).hasMessageContaining("reserva activa")
				.extracting(e -> ((SQLException) e).getSQLState()).isEqualTo("45000");
	}

	@Test
	void venderDesdeUnaReservaLaConcreta() {
		Reserva reserva = reservaRepository.findByMotoPlacaAndEstado("LMN24P", EstadoReserva.ACTIVA).orElseThrow();
		VentaMoto venta = new VentaMoto();
		venta.setFechaVenta(LocalDateTime.of(2026, 10, 29, 10, 0));
		venta.setMetodoPago(MetodoPago.TRANSFERENCIA);
		venta.setTipoFactura(TipoFactura.ELECTRONICA);
		venta.setValorTotal(new BigDecimal("27600000"));
		venta.setMoto(reserva.getMoto());
		venta.setCliente(reserva.getCliente());
		venta.setVendedor(vendedorRepository.getReferenceById(6));
		venta.setReserva(reserva);
		ventaMotoRepository.saveAndFlush(venta);
		em.refresh(reserva);
		assertThat(reserva.getEstado()).isEqualTo(EstadoReserva.CONCRETADA);
		assertThat(ventaMotoRepository.existsByMotoPlaca("LMN24P")).isTrue();
	}

	@Test
	void guardaVentaDeAccesoriosConSusItemsEnCascada() {
		VentaAccesorio venta = new VentaAccesorio();
		venta.setFechaVenta(LocalDateTime.of(2026, 10, 29, 11, 30));
		venta.setMetodoPago(MetodoPago.EFECTIVO);
		venta.setTipoFactura(TipoFactura.POS);
		venta.setCliente(clienteRepository.getReferenceById(11));
		venta.setVendedor(vendedorRepository.getReferenceById(12));
		venta.agregarDetalle(accesorioRepository.getReferenceById(4), 1, new BigDecimal("189000"));
		venta.agregarDetalle(accesorioRepository.getReferenceById(8), 2, new BigDecimal("299000"));
		Integer id = ventaAccesorioRepository.saveAndFlush(venta).getIdVentaAccesorio();
		em.clear();
		assertThat(ventaAccesorioRepository.findById(id).orElseThrow().getDetalles()).hasSize(2);
		assertThat(totalVentaAccesorioRepository.findById(id).orElseThrow().getValorTotal()).isEqualByComparingTo("787000");
	}

	@Test
	void registraRepuestoUsadoYLaBaseExigeCostoParaFinalizar() {
		Reparacion reparacion = reparacionRepository.findById(10).orElseThrow();
		reparacion.agregarRepuesto(repuestoRepository.getReferenceById(1), 1, new BigDecimal("38000"));
		reparacionRepository.saveAndFlush(reparacion);
		assertThat(costoReparacionRepository.findById(10).orElseThrow().getRepuestos()).isEqualByComparingTo("38000");
		reparacion.setEstado(EstadoReparacion.FINALIZADA);
		reparacion.setFechaFinalizacion(LocalDateTime.of(2026, 10, 29, 17, 0));
		assertThatThrownBy(() -> reparacionRepository.saveAndFlush(reparacion)).rootCause()
				.hasMessageContaining("ck_reparacion_cierre");
	}

	@Test
	void guardaUnSuministroConClaveDeCuatroPartes() {
		LocalDateTime fecha = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		Suministro suministro = new Suministro(proveedorRepository.getReferenceById("830987654-2"),
				repuestoRepository.getReferenceById(2), sucursalRepository.getReferenceById(4), fecha, 10,
				new BigDecimal("12000"));
		suministroRepository.saveAndFlush(suministro);
		em.clear();
		assertThat(suministroRepository.findById(new SuministroId("830987654-2", 2, 4, fecha))).isPresent();
	}

	private Reserva nuevaReserva(String placa, Integer idCliente) {
		Reserva reserva = new Reserva();
		reserva.setFechaReserva(LocalDateTime.of(2026, 10, 29, 9, 0));
		reserva.setFechaVencimiento(LocalDate.of(2026, 11, 5));
		reserva.setMoto(motoRepository.getReferenceById(placa));
		reserva.setCliente(clienteRepository.getReferenceById(idCliente));
		return reserva;
	}
}

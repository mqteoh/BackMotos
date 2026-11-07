package co.edu.unbosque.BackMotos.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.entity.Cuenta;
import co.edu.unbosque.BackMotos.entity.Empleado;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.TipoEmpleado;
import co.edu.unbosque.BackMotos.entity.Vendedor;
import co.edu.unbosque.BackMotos.security.Rol;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PersonasRepositoryTest {

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Autowired
	private VendedorRepository vendedorRepository;

	@Autowired
	private TecnicoRepository tecnicoRepository;

	@Autowired
	private SucursalRepository sucursalRepository;

	@Autowired
	private CuentaRepository cuentaRepository;

	@Autowired
	private TestEntityManager em;

	@Test
	void clienteConCuentaYContactosMultivaluados() {
		Cliente cliente = clienteRepository.findByDocumento("1022345678").orElseThrow();
		assertThat(cliente.getNombreCompleto()).isEqualTo("Juan Sebastián López Medina");
		assertThat(cliente.getTelefonos()).containsExactlyInAnyOrder("3012345678", "6016543210");
		Cuenta cuenta = cuentaRepository.findByClienteIdCliente(cliente.getIdCliente()).orElseThrow();
		assertThat(cuenta.getRol()).isEqualTo(Rol.CLIENTE);
		assertThat(encoder.matches("Cliente2026*", cuenta.getPasswordHash())).isTrue();
		assertThat(cliente.getCorreos()).contains(cuenta.getCorreo());
		assertThat(cuentaRepository.findByCorreoIgnoreCase(cuenta.getCorreo().toUpperCase())).contains(cuenta);
	}

	@Test
	void clienteRegistradoEnSedeNoTieneCuenta() {
		Cliente cliente = clienteRepository.findByDocumento("1030567123").orElseThrow();
		assertThat(cuentaRepository.findByClienteIdCliente(cliente.getIdCliente())).isEmpty();
		assertThat(cuentaRepository.idsClientesConCuenta()).hasSize(10).doesNotContain(cliente.getIdCliente());
	}

	@Test
	void duenoEsEmpleadoSinSubclaseConCuentaDeDueno() {
		Empleado dueno = empleadoRepository.findById(1).orElseThrow();
		assertThat(dueno.getClass()).isEqualTo(Empleado.class);
		assertThat(dueno.getTipoEmpleado()).isEqualTo(TipoEmpleado.DUENO);
		assertThat(dueno.getSucursal().getNombre()).isEqualTo("Sede Chapinero");
		assertThat(dueno.getJefe()).isNull();
		Cuenta cuenta = cuentaRepository.findByEmpleadoIdEmpleado(1).orElseThrow();
		assertThat(cuenta.getRol()).isEqualTo(Rol.DUENO);
		assertThat(encoder.matches("Motos2026*", cuenta.getPasswordHash())).isTrue();
	}

	@Test
	void vendedorTraeDatosDeEmpleadoYDeSuSubclase() {
		Empleado empleado = empleadoRepository.findById(2).orElseThrow();
		assertThat(empleado).isInstanceOf(Vendedor.class);
		Vendedor vendedor = (Vendedor) empleado;
		assertThat(vendedor.getTipoEmpleado()).isEqualTo(TipoEmpleado.VENDEDOR);
		assertThat(vendedor.getComision()).isEqualByComparingTo("3.00");
		assertThat(vendedor.getSalario()).isEqualByComparingTo("3200000");
		assertThat(vendedor.getSucursal().getNombre()).isEqualTo("Sede Chapinero");
		assertThat(vendedor.getJefe().getIdEmpleado()).isEqualTo(1);
		assertThat(vendedor.getTelefonos()).hasSize(2);
	}

	@Test
	void tecnicoTraeSuEspecialidad() {
		Cuenta cuenta = cuentaRepository.findByCorreoIgnoreCase("jhon.castano@concesionario-motos.test").orElseThrow();
		Tecnico tecnico = tecnicoRepository.findById(cuenta.getEmpleado().getIdEmpleado()).orElseThrow();
		assertThat(tecnico.getEspecialidad()).isEqualTo("Motor");
	}

	@Test
	void conteosPorTipoDeEmpleado() {
		assertThat(empleadoRepository.count()).isEqualTo(12);
		assertThat(vendedorRepository.count()).isEqualTo(6);
		assertThat(tecnicoRepository.count()).isEqualTo(5);
		assertThat(cuentaRepository.deEmpleados()).hasSize(12);
		assertThat(vendedorRepository.findBySucursalIdSucursal(2)).hasSize(2);
		assertThat(empleadoRepository.findByJefeIdEmpleado(2)).hasSize(3);
	}

	@Test
	void guardaUnVendedorNuevoConSuCuenta() {
		Vendedor nuevo = new Vendedor();
		nuevo.setDocumento("1099999999");
		nuevo.setNombres("Prueba");
		nuevo.setApellidos("Vendedor Nuevo");
		nuevo.setFechaContratacion(LocalDate.of(2026, 10, 1));
		nuevo.setSalario(new BigDecimal("2000000"));
		nuevo.setSucursal(sucursalRepository.getReferenceById(1));
		nuevo.setJefe(empleadoRepository.getReferenceById(2));
		nuevo.setCertificadoVentas("Asesor comercial certificado");
		nuevo.setComision(new BigDecimal("2.00"));
		nuevo.getTelefonos().add("3009998877");
		Vendedor guardado = vendedorRepository.saveAndFlush(nuevo);
		Cuenta cuenta = new Cuenta();
		cuenta.setCorreo("prueba.vendedor@concesionario-motos.test");
		cuenta.setPasswordHash(encoder.encode("ClavePrueba1*"));
		cuenta.setRol(Rol.VENDEDOR);
		cuenta.setEmpleado(guardado);
		cuentaRepository.saveAndFlush(cuenta);
		em.clear();
		Empleado leido = empleadoRepository.findById(guardado.getIdEmpleado()).orElseThrow();
		assertThat(leido).isInstanceOf(Vendedor.class);
		assertThat(leido.getTelefonos()).containsExactly("3009998877");
		assertThat(cuentaRepository.findByEmpleadoIdEmpleado(leido.getIdEmpleado())).isPresent();
	}

	@Test
	void laBaseImpideQueUnEmpleadoSeaSuPropioJefe() {
		Empleado empleado = empleadoRepository.findById(3).orElseThrow();
		empleado.setJefe(empleado);
		assertThatThrownBy(() -> empleadoRepository.saveAndFlush(empleado)).rootCause()
				.isInstanceOf(SQLException.class).hasMessageContaining("ck_empleado_jefe")
				.extracting(e -> ((SQLException) e).getSQLState()).isEqualTo("23514");
	}

	@Test
	void laBaseRechazaUnaCuentaDeVendedorParaQuienNoEsVendedor() {
		Cuenta cuenta = cuentaRepository.findByEmpleadoIdEmpleado(4).orElseThrow();
		cuenta.setRol(Rol.VENDEDOR);
		assertThatThrownBy(() -> cuentaRepository.saveAndFlush(cuenta)).rootCause()
				.isInstanceOf(SQLException.class).hasMessageContaining("no está registrado como vendedor")
				.extracting(e -> ((SQLException) e).getSQLState()).isEqualTo("45000");
	}
}

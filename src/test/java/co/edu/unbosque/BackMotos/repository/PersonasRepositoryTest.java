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
import co.edu.unbosque.BackMotos.entity.Empleado;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.TipoEmpleado;
import co.edu.unbosque.BackMotos.entity.Vendedor;

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
	private TestEntityManager em;

	@Test
	void clienteConCuentaWebYContactosMultivaluados() {
		Cliente cliente = clienteRepository.findByDocumento("1022345678").orElseThrow();
		assertThat(cliente.getNombreCompleto()).isEqualTo("Juan Sebastián López Medina");
		assertThat(cliente.tieneCuentaWeb()).isTrue();
		assertThat(encoder.matches("Cliente2026*", cliente.getPasswordHash())).isTrue();
		assertThat(cliente.getTelefonos()).containsExactlyInAnyOrder("3012345678", "6016543210");
		assertThat(cliente.getCorreos()).contains(cliente.getCorreoAcceso());
		assertThat(clienteRepository.findByCorreoAccesoIgnoreCase(cliente.getCorreoAcceso().toUpperCase()))
				.contains(cliente);
	}

	@Test
	void clienteRegistradoEnSedeNoTieneCuenta() {
		Cliente cliente = clienteRepository.findByDocumento("1030567123").orElseThrow();
		assertThat(cliente.tieneCuentaWeb()).isFalse();
		assertThat(cliente.getPasswordHash()).isNull();
	}

	@Test
	void duenoEsEmpleadoSinSubclaseNiSede() {
		Empleado dueno = empleadoRepository.findById(1).orElseThrow();
		assertThat(dueno.getClass()).isEqualTo(Empleado.class);
		assertThat(dueno.getTipoEmpleado()).isEqualTo(TipoEmpleado.DUENO);
		assertThat(dueno.getSucursal()).isNull();
		assertThat(dueno.getJefe()).isNull();
		assertThat(encoder.matches("Motos2026*", dueno.getPasswordHash())).isTrue();
	}

	@Test
	void vendedorTraeDatosDeEmpleadoYDeSuSubclase() {
		Empleado empleado = empleadoRepository.findById(2).orElseThrow();
		assertThat(empleado).isInstanceOf(Vendedor.class);
		Vendedor vendedor = (Vendedor) empleado;
		assertThat(vendedor.getTipoEmpleado()).isEqualTo(TipoEmpleado.VENDEDOR);
		assertThat(vendedor.getComision()).isEqualByComparingTo("3.00");
		assertThat(vendedor.getSucursal().getNombre()).isEqualTo("Sede Chapinero");
		assertThat(vendedor.getJefe().getIdEmpleado()).isEqualTo(1);
		assertThat(vendedor.getTelefonos()).hasSize(2);
	}

	@Test
	void tecnicoTraeSuEspecialidad() {
		Empleado empleado = empleadoRepository.findByCorreoAccesoIgnoreCase("jhon.castano@concesionario-motos.test")
				.orElseThrow();
		assertThat(empleado).isInstanceOf(Tecnico.class);
		assertThat(((Tecnico) empleado).getEspecialidad()).isEqualTo("Motor");
	}

	@Test
	void conteosPorTipoDeEmpleado() {
		assertThat(empleadoRepository.count()).isEqualTo(12);
		assertThat(vendedorRepository.count()).isEqualTo(6);
		assertThat(tecnicoRepository.count()).isEqualTo(5);
		assertThat(vendedorRepository.findBySucursalIdSucursal(2)).hasSize(2);
		assertThat(empleadoRepository.findByJefeIdEmpleado(2)).hasSize(3);
	}

	@Test
	void guardaUnVendedorNuevoEnLasDosTablas() {
		Vendedor nuevo = new Vendedor();
		nuevo.setDocumento("1099999999");
		nuevo.setNombres("Prueba");
		nuevo.setApellidos("Vendedor Nuevo");
		nuevo.setFechaContratacion(LocalDate.of(2026, 10, 1));
		nuevo.setSucursal(sucursalRepository.getReferenceById(1));
		nuevo.setJefe(empleadoRepository.getReferenceById(2));
		nuevo.setCorreoAcceso("prueba.vendedor@concesionario-motos.test");
		nuevo.setPasswordHash(encoder.encode("ClavePrueba1*"));
		nuevo.setCertificadoVentas("Asesor comercial certificado");
		nuevo.setComision(new BigDecimal("2.00"));
		nuevo.getTelefonos().add("3009998877");
		Integer id = vendedorRepository.saveAndFlush(nuevo).getIdEmpleado();
		em.clear();
		Empleado leido = empleadoRepository.findById(id).orElseThrow();
		assertThat(leido).isInstanceOf(Vendedor.class);
		assertThat(leido.getTelefonos()).containsExactly("3009998877");
	}

	@Test
	void laBaseImpideQueUnEmpleadoSeaSuPropioJefe() {
		Empleado empleado = empleadoRepository.findById(3).orElseThrow();
		empleado.setJefe(empleado);
		assertThatThrownBy(() -> empleadoRepository.saveAndFlush(empleado)).rootCause()
				.isInstanceOf(SQLException.class).hasMessageContaining("propio jefe")
				.extracting(e -> ((SQLException) e).getSQLState()).isEqualTo("45000");
	}

	@Test
	void laBaseRechazaUnaCuentaWebSinContrasena() {
		Cliente cliente = new Cliente();
		cliente.setDocumento("1099999998");
		cliente.setNombres("Prueba");
		cliente.setApellidos("Sin Clave");
		cliente.setFechaNacimiento(LocalDate.of(2000, 1, 1));
		cliente.setCiudad("Bogotá");
		cliente.setBarrio("Centro");
		cliente.setCalle("Calle 1");
		cliente.setNumeroDireccion("1-1");
		cliente.setCorreoAcceso("sin.clave@correo.test");
		assertThatThrownBy(() -> clienteRepository.saveAndFlush(cliente)).rootCause()
				.hasMessageContaining("ck_cliente_cuenta");
	}
}

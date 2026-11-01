package co.edu.unbosque.BackMotos.controller;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Reserva;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.ReservaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private MotoRepository motoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	private String dueno;

	@BeforeEach
	void iniciarSesion() throws Exception {
		dueno = token("ricardo.salazar@concesionario-motos.test", "Motos2026*");
	}

	private String token(String correo, String clave) throws Exception {
		String cuerpo = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"" + correo + "\",\"contrasena\":\"" + clave + "\"}")).andReturn().getResponse()
				.getContentAsString();
		return JsonPath.read(cuerpo, "$.token");
	}

	private MockHttpServletRequestBuilder con(MockHttpServletRequestBuilder peticion) {
		return peticion.header(HttpHeaders.AUTHORIZATION, "Bearer " + dueno).contentType(MediaType.APPLICATION_JSON);
	}

	private String moto(String placa, String extra) {
		return "{\"placa\":\"" + placa + "\",\"idMarca\":1,\"nroModelo\":1,\"origen\":\"INVENTARIO\",\"anio\":2026,"
				+ "\"color\":\"Rojo\",\"kilometraje\":0,\"fechaIngreso\":\"2026-10-20\"" + extra + "}";
	}

	private String empleado(String documento, String tipo, String extra) {
		return "{\"documento\":\"" + documento + "\",\"nombres\":\"Ana\",\"apellidos\":\"Nueva Sede\","
				+ "\"fechaContratacion\":\"2026-10-01\",\"tipoEmpleado\":\"" + tipo + "\",\"idSucursal\":3,\"idJefe\":8,"
				+ "\"correoAcceso\":\"ana.nueva@concesionario-motos.test\",\"telefonos\":[\"3015550000\"],"
				+ "\"contrasena\":\"ClaveInicial1\"" + extra + "}";
	}

	@Test
	void crudDelCatalogoConReglasDeIntegridad() throws Exception {
		String cuerpo = mvc.perform(con(post("/api/admin/marcas")).content("{\"nombre\":\"Benelli\",\"paisOrigen\":\"Italia\"}"))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.read(cuerpo, "$.idMarca");
		mvc.perform(con(put("/api/admin/marcas/" + id)).content("{\"nombre\":\"Benelli\",\"paisOrigen\":\"China\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.paisOrigen").value("China"));
		mvc.perform(con(delete("/api/admin/marcas/" + id))).andExpect(status().isNoContent());
		mvc.perform(con(get("/api/admin/modelos/1/3"))).andExpect(jsonPath("$.nombre").value("MT-03"));
		mvc.perform(con(post("/api/admin/modelos")).content("{\"idMarca\":8,\"nombre\":\"Raider 125\",\"cilindraje\":125,"
				+ "\"tipoMoto\":\"CALLE\",\"potenciaHp\":11.2,\"capacidadTanque\":10.0}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.nroModelo").value(2));
		mvc.perform(con(delete("/api/admin/modelos/8/2"))).andExpect(status().isNoContent());
		mvc.perform(con(get("/api/admin/proveedores/900456789-1")))
				.andExpect(jsonPath("$.nombre").value("Distribuidora Andina de Repuestos S.A.S."));
		mvc.perform(con(get("/api/admin/sucursales"))).andExpect(jsonPath("$", hasSize(4)));
	}

	@Test
	void noSeEliminaUnaMarcaConModelos() throws Exception {
		mvc.perform(con(delete("/api/admin/marcas/1"))).andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("No se puede eliminar porque otros registros dependen de él."));
	}

	@Test
	void administraMotosRespetandoSuOrigen() throws Exception {
		mvc.perform(con(get("/api/admin/motos").param("idSucursal", "1"))).andExpect(jsonPath("$", hasSize(8)));
		mvc.perform(con(get("/api/admin/motos").param("origen", "EXTERNA"))).andExpect(jsonPath("$", hasSize(6)));
		mvc.perform(con(post("/api/admin/motos")).content(moto("ADM10A", ",\"idSucursal\":1")))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("Una moto de inventario necesita sede y precio."));
		mvc.perform(con(post("/api/admin/motos")).content(moto("adm10a", ",\"idSucursal\":1,\"precio\":11000000")))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.placa").value("ADM10A"))
				.andExpect(jsonPath("$.vendida").value(false));
		mvc.perform(get("/api/publico/motos/ADM10A")).andExpect(status().isOk());
		mvc.perform(con(put("/api/admin/motos/ADM10A")).content(moto("ADM10A", ",\"idSucursal\":2,\"precio\":10500000")))
				.andExpect(jsonPath("$.sucursal").value("Sede Suba"));
		mvc.perform(con(delete("/api/admin/motos/ADM10A"))).andExpect(status().isNoContent());
		mvc.perform(con(delete("/api/admin/motos/JKL78D"))).andExpect(status().isConflict());
	}

	@Test
	void registraEmpleadosConSuSubclaseYPuedenEntrar() throws Exception {
		mvc.perform(con(post("/api/admin/empleados")).content(empleado("1099111222", "VENDEDOR",
				",\"certificadoVentas\":\"Asesor comercial certificado\",\"comision\":2.5")))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.tipoEmpleado").value("VENDEDOR"))
				.andExpect(jsonPath("$.jefe").value("Santiago Pérez Arango")).andExpect(jsonPath("$.contrasena").doesNotExist());
		mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"ana.nueva@concesionario-motos.test\",\"contrasena\":\"ClaveInicial1\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.usuario.rol").value("VENDEDOR"))
				.andExpect(jsonPath("$.usuario.idSucursal").value(3));
		mvc.perform(con(post("/api/admin/empleados")).content(empleado("1099111223", "TECNICO", "")
				.replace("ana.nueva", "otro.tecnico"))).andExpect(status().isBadRequest());
		mvc.perform(con(get("/api/admin/empleados").param("tipo", "TECNICO"))).andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	void reglasAlEditarYEliminarEmpleados() throws Exception {
		String laura = "{\"documento\":\"52789456\",\"nombres\":\"Laura\",\"apellidos\":\"Gómez Torres\","
				+ "\"fechaContratacion\":\"2018-03-12\",\"tipoEmpleado\":\"TECNICO\",\"idSucursal\":1,\"idJefe\":1,"
				+ "\"correoAcceso\":\"laura.gomez@concesionario-motos.test\",\"certificadoTecnico\":\"x\",\"especialidad\":\"y\"}";
		mvc.perform(con(put("/api/admin/empleados/2")).content(laura)).andExpect(status().isConflict());
		String lauraVendedora = laura.replace("\"TECNICO\"", "\"VENDEDOR\"").replace(
				"\"certificadoTecnico\":\"x\",\"especialidad\":\"y\"", "\"certificadoVentas\":\"Asesor senior\",\"comision\":3.5");
		mvc.perform(con(put("/api/admin/empleados/2")).content(lauraVendedora)).andExpect(status().isOk())
				.andExpect(jsonPath("$.comision").value(3.5));
		mvc.perform(con(delete("/api/admin/empleados/2"))).andExpect(status().isConflict());
		mvc.perform(con(delete("/api/admin/empleados/1"))).andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("No puede eliminar su propia cuenta."));
	}

	@Test
	void clientesYSuministros() throws Exception {
		mvc.perform(con(get("/api/admin/clientes"))).andExpect(jsonPath("$", hasSize(15)));
		mvc.perform(con(get("/api/admin/suministros").param("idSucursal", "3"))).andExpect(jsonPath("$", hasSize(6)));
		String cuerpo = mvc.perform(con(post("/api/admin/suministros"))
				.content("{\"nit\":\"830987654-2\",\"idRepuesto\":7,\"idSucursal\":3,\"cantidad\":5,\"costoUnitario\":130000}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.repuesto").value("Batería 12V 7Ah")).andReturn()
				.getResponse().getContentAsString();
		String fecha = JsonPath.read(cuerpo, "$.fechaSuministro");
		mvc.perform(con(delete("/api/admin/suministros")).param("nit", "830987654-2").param("idRepuesto", "7")
				.param("idSucursal", "3").param("fecha", fecha)).andExpect(status().isNoContent());
		mvc.perform(con(delete("/api/admin/clientes/1"))).andExpect(status().isConflict());
	}

	@Test
	void operacionesDelDueno() throws Exception {
		mvc.perform(con(get("/api/admin/reservas").param("estado", "ACTIVA"))).andExpect(jsonPath("$", hasSize(3)));
		mvc.perform(con(delete("/api/admin/reservas/1"))).andExpect(status().isConflict());
		mvc.perform(con(delete("/api/admin/reservas/4"))).andExpect(status().isNoContent());
		mvc.perform(con(delete("/api/admin/ventas-moto/4"))).andExpect(status().isNoContent());
		mvc.perform(get("/api/publico/motos/HBN46C")).andExpect(status().isOk());
		mvc.perform(con(get("/api/admin/reservas").param("estado", "CANCELADA")))
				.andExpect(jsonPath("$[?(@.idReserva == 1)].estado").value("CANCELADA"));
		mvc.perform(con(delete("/api/admin/ventas-accesorio/1"))).andExpect(status().isNoContent());
		mvc.perform(con(get("/api/admin/reparaciones").param("estado", "FINALIZADA"))).andExpect(jsonPath("$", hasSize(8)));
		mvc.perform(con(delete("/api/admin/reparaciones/1"))).andExpect(status().isNoContent());
	}

	@Test
	void vencerReservasMarcaLasQueYaPasaron() throws Exception {
		Reserva vieja = new Reserva();
		vieja.setFechaReserva(LocalDateTime.now().minusDays(10).withNano(0));
		vieja.setFechaVencimiento(LocalDate.now().minusDays(3));
		vieja.setMoto(motoRepository.getReferenceById("GTK22F"));
		vieja.setCliente(clienteRepository.getReferenceById(3));
		Integer id = reservaRepository.saveAndFlush(vieja).getIdReserva();
		mvc.perform(con(post("/api/admin/reservas/vencer"))).andExpect(status().isOk())
				.andExpect(jsonPath("$.reservasVencidas", greaterThanOrEqualTo(1)));
		mvc.perform(con(get("/api/admin/reservas").param("estado", "VENCIDA")))
				.andExpect(jsonPath("$[?(@.idReserva == " + id + ")].estado").value(EstadoReserva.VENCIDA.name()));
	}

	@Test
	void reportesDeGestion() throws Exception {
		mvc.perform(con(get("/api/admin/reportes/resumen"))).andExpect(jsonPath("$.motosDisponibles").value(12))
				.andExpect(jsonPath("$.reservasActivas").value(3)).andExpect(jsonPath("$.reparacionesPendientes").value(4))
				.andExpect(jsonPath("$.clientes").value(15)).andExpect(jsonPath("$.empleados").value(12));
		mvc.perform(con(get("/api/admin/reportes/ventas-por-vendedor"))).andExpect(jsonPath("$", hasSize(6)))
				.andExpect(jsonPath("$[0].vendedor").value("Santiago Pérez Arango"))
				.andExpect(jsonPath("$[0].totalMotos").value(64600000.0));
		mvc.perform(con(get("/api/admin/reportes/ventas-por-mes").param("anio", "2026"))).andExpect(jsonPath("$", hasSize(12)))
				.andExpect(jsonPath("$[1].motosVendidas").value(2)).andExpect(jsonPath("$[1].totalMotos").value(22000000.0));
		mvc.perform(con(get("/api/admin/reportes/ventas-por-mes").param("anio", "1900"))).andExpect(status().isBadRequest());
		mvc.perform(con(get("/api/admin/reportes/costos-reparacion"))).andExpect(jsonPath("$", hasSize(4)));
		mvc.perform(con(get("/api/admin/reportes/repuestos-por-sucursal").param("idSucursal", "3")))
				.andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$[*].idSucursal", everyItem(is(3))));
		mvc.perform(con(get("/api/admin/reportes/disponibles-por-sede")))
				.andExpect(jsonPath("$[*].disponibles", everyItem(is(3))));
		String laura = token("laura.gomez@concesionario-motos.test", "Motos2026*");
		mvc.perform(get("/api/admin/reportes/resumen").header(HttpHeaders.AUTHORIZATION, "Bearer " + laura))
				.andExpect(status().isForbidden());
	}
}

package co.edu.unbosque.BackMotos.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClienteControllerTest {

	@Autowired
	private MockMvc mvc;

	private String maria;
	private String juan;

	@BeforeEach
	void iniciarSesion() throws Exception {
		maria = token("maria.ramirez@correo.test");
		juan = token("juan.lopez@correo.test");
	}

	private String token(String correo) throws Exception {
		String cuerpo = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"" + correo + "\",\"contrasena\":\"Cliente2026*\"}")).andReturn().getResponse()
				.getContentAsString();
		return JsonPath.read(cuerpo, "$.token");
	}

	private MockHttpServletRequestBuilder con(MockHttpServletRequestBuilder peticion, String token) {
		return peticion.header(HttpHeaders.AUTHORIZATION, "Bearer " + token).contentType(MediaType.APPLICATION_JSON);
	}

	private String agenda(String placa, String extra) {
		return agenda(placa, extra, LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.MINUTES));
	}

	private String agenda(String placa, String extra, LocalDateTime fecha) {
		return "{\"placa\":\"" + placa + "\",\"idSucursal\":1,\"fechaAgendada\":\"" + fecha
				+ "\",\"descripcion\":\"Revisión de frenos\",\"kilometrajeIngreso\":5200" + extra + "}";
	}

	@Test
	void perfilSeConsultaYSeActualiza() throws Exception {
		mvc.perform(con(get("/api/cliente/perfil"), maria)).andExpect(status().isOk())
				.andExpect(jsonPath("$.documento").value("1013654789")).andExpect(jsonPath("$.telefonos", hasSize(1)))
				.andExpect(jsonPath("$.edad").isNumber());
		String cambio = "{\"nombres\":\"María José\",\"apellidos\":\"Ramírez Castro\",\"fechaNacimiento\":\"1995-04-18\","
				+ "\"ciudad\":\"Bogotá\",\"barrio\":\"Teusaquillo\",\"calle\":\"Calle 45\",\"numeroDireccion\":\"19-30\","
				+ "\"telefonos\":[\"3001234567\",\"6012345678\"],\"correos\":[\"maria.ramirez@correo.test\"],"
				+ "\"documento\":\"999\"}";
		mvc.perform(con(put("/api/cliente/perfil"), maria).content(cambio)).andExpect(status().isOk())
				.andExpect(jsonPath("$.barrio").value("Teusaquillo")).andExpect(jsonPath("$.telefonos", hasSize(2)))
				.andExpect(jsonPath("$.documento").value("1013654789"));
		mvc.perform(con(put("/api/cliente/perfil"), maria).content(cambio.replace("6012345678", "12")))
				.andExpect(status().isBadRequest());
	}

	@Test
	void reservaUnaMotoDisponibleYOtroClienteYaNoPuede() throws Exception {
		mvc.perform(con(post("/api/cliente/reservas"), maria).content("{\"placa\":\"gtk22f\"}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.estado").value("ACTIVA"))
				.andExpect(jsonPath("$.placa").value("GTK22F"))
				.andExpect(jsonPath("$.fechaVencimiento").value(LocalDate.now().plusDays(7).toString()));
		mvc.perform(con(post("/api/cliente/reservas"), juan).content("{\"placa\":\"GTK22F\"}"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("La moto GTK22F no está disponible para reservar."));
		mvc.perform(get("/api/publico/motos/GTK22F")).andExpect(status().isNotFound());
		mvc.perform(con(get("/api/cliente/reservas"), maria)).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void noSeReservaUnaMotoVendida() throws Exception {
		mvc.perform(con(post("/api/cliente/reservas"), maria).content("{\"placa\":\"JKL78D\"}"))
				.andExpect(status().isConflict());
	}

	@Test
	void cancelarSoloReservasPropiasYActivas() throws Exception {
		mvc.perform(con(patch("/api/cliente/reservas/8/cancelar"), maria)).andExpect(status().isNotFound());
		mvc.perform(con(patch("/api/cliente/reservas/8/cancelar"), juan)).andExpect(status().isOk())
				.andExpect(jsonPath("$.estado").value("CANCELADA"));
		mvc.perform(con(patch("/api/cliente/reservas/8/cancelar"), juan)).andExpect(status().isConflict());
		mvc.perform(get("/api/publico/motos/MNP12E")).andExpect(status().isOk());
	}

	@Test
	void agendaServicioParaMotoCompradaSinDuplicarlo() throws Exception {
		mvc.perform(con(post("/api/cliente/reparaciones"), maria).content(agenda("HBN46C", "")))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.estado").value("AGENDADA"))
				.andExpect(jsonPath("$.tecnico").doesNotExist()).andExpect(jsonPath("$.sucursal").value("Sede Chapinero"));
		mvc.perform(con(post("/api/cliente/reparaciones"), maria).content(agenda("HBN46C", "")))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("La moto HBN46C ya tiene un servicio pendiente en el taller."));
	}

	@Test
	void agendaServicioParaUnaMotoExternaNueva() throws Exception {
		String externa = ",\"motoExterna\":{\"idMarca\":2,\"nroModelo\":2,\"anio\":2021,\"color\":\"Negro\"}";
		mvc.perform(con(post("/api/cliente/reparaciones"), maria).content(agenda("new12a", externa)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.placa").value("NEW12A"))
				.andExpect(jsonPath("$.modelo").value("XR 150L"));
		mvc.perform(con(get("/api/cliente/motos"), maria)).andExpect(jsonPath("$[*].placa", hasItem("NEW12A")));
	}

	@Test
	void reglasDelTallerParaPlacasNoValidas() throws Exception {
		mvc.perform(con(post("/api/cliente/reparaciones"), maria).content(agenda("ZZZ99Z", "")))
				.andExpect(status().isBadRequest());
		mvc.perform(con(post("/api/cliente/reparaciones"), maria).content(agenda("GTK22F", "")))
				.andExpect(status().isConflict());
		mvc.perform(con(post("/api/cliente/reparaciones"), maria)
				.content(agenda("HBN46C", "", LocalDateTime.of(2020, 1, 1, 9, 0)))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos[0].campo").value("fechaAgendada"));
	}

	@Test
	void cancelarUnServicioAgendado() throws Exception {
		String cuerpo = mvc.perform(con(post("/api/cliente/reparaciones"), maria).content(agenda("HBN46C", "")))
				.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.read(cuerpo, "$.idReparacion");
		mvc.perform(con(patch("/api/cliente/reparaciones/" + id + "/cancelar"), maria)).andExpect(status().isOk())
				.andExpect(jsonPath("$.estado").value("CANCELADA"));
		mvc.perform(con(patch("/api/cliente/reparaciones/" + id + "/cancelar"), juan)).andExpect(status().isNotFound());
	}

	@Test
	void consultaSusReparacionesMotosYCompras() throws Exception {
		mvc.perform(con(get("/api/cliente/reparaciones"), maria)).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].placa").value("STU28H")).andExpect(jsonPath("$[0].estado").value("EN_PROCESO"))
				.andExpect(jsonPath("$[0].tecnico").value("Felipe Ortiz Lozano"))
				.andExpect(jsonPath("$[0].costoRepuestos").value(72000.0));
		mvc.perform(con(get("/api/cliente/motos"), maria))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("HBN46C", "STU28H")));
		mvc.perform(con(get("/api/cliente/compras"), maria)).andExpect(jsonPath("$.motos", hasSize(1)))
				.andExpect(jsonPath("$.motos[0].placa").value("HBN46C"))
				.andExpect(jsonPath("$.accesorios[0].valorTotal").value(1048000.0))
				.andExpect(jsonPath("$.accesorios[0].items", hasSize(2)))
				.andExpect(jsonPath("$.totalGastado").value(12348000.0));
	}
}

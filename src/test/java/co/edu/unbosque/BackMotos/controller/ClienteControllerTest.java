package co.edu.unbosque.BackMotos.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
	void elClienteSoloConsultaLasReparaciones() throws Exception {
		String reparacion = "{\"placa\":\"HBN46C\",\"fecha\":\"2026-10-01\",\"descripcion\":\"Revisión\",\"costo\":1}";
		mvc.perform(con(post("/api/tecnico/reparaciones"), maria).content(reparacion)).andExpect(status().isForbidden());
		mvc.perform(con(get("/api/tecnico/historial/HBN46C"), maria)).andExpect(status().isForbidden());
	}

	@Test
	void consultaSusReparacionesMotosYCompras() throws Exception {
		mvc.perform(con(get("/api/cliente/reparaciones"), maria)).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].placa").value("HBN46C")).andExpect(jsonPath("$[0].fecha").value("2026-04-10"))
				.andExpect(jsonPath("$[0].tecnicos[*].nombre", containsInAnyOrder("Felipe Ortiz Lozano", "Jhon Castaño Ruiz")))
				.andExpect(jsonPath("$[0].costoRepuestos").value(338000.0)).andExpect(jsonPath("$[0].costoTotal").value(488000.0));
		mvc.perform(con(get("/api/cliente/reparaciones"), juan)).andExpect(jsonPath("$[*].placa", containsInAnyOrder("QRS36A")));
		mvc.perform(con(get("/api/cliente/motos"), maria)).andExpect(jsonPath("$[*].placa", containsInAnyOrder("HBN46C")));
		mvc.perform(con(get("/api/cliente/compras"), maria)).andExpect(jsonPath("$.motos", hasSize(1)))
				.andExpect(jsonPath("$.motos[0].placa").value("HBN46C"))
				.andExpect(jsonPath("$.accesorios[0].valorTotal").value(1048000.0))
				.andExpect(jsonPath("$.accesorios[0].items", hasSize(2)))
				.andExpect(jsonPath("$.totalGastado").value(12348000.0));
	}
}

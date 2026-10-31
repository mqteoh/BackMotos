package co.edu.unbosque.BackMotos.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class TecnicoControllerTest {

	@Autowired
	private MockMvc mvc;

	private String jhon;
	private String felipe;
	private String diego;
	private String dueno;

	@BeforeEach
	void iniciarSesion() throws Exception {
		jhon = token("jhon.castano");
		felipe = token("felipe.ortiz");
		diego = token("diego.moreno");
		dueno = token("ricardo.salazar");
	}

	private String token(String usuario) throws Exception {
		String cuerpo = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(
				"{\"correo\":\"" + usuario + "@concesionario-motos.test\",\"contrasena\":\"Motos2026*\"}"))
				.andReturn().getResponse().getContentAsString();
		return JsonPath.read(cuerpo, "$.token");
	}

	private MockHttpServletRequestBuilder con(MockHttpServletRequestBuilder peticion, String token) {
		return peticion.header(HttpHeaders.AUTHORIZATION, "Bearer " + token).contentType(MediaType.APPLICATION_JSON);
	}

	@Test
	void agendaDeLaSedeConFiltros() throws Exception {
		mvc.perform(con(get("/api/tecnico/agenda"), jhon)).andExpect(status().isOk())
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("STU28H", "HBN45C")));
		mvc.perform(con(get("/api/tecnico/agenda").param("estado", "FINALIZADA"), jhon))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("JKL78D", "BCD47E", "GTK21F")));
		mvc.perform(con(get("/api/tecnico/agenda").param("estado", "FINALIZADA").param("desde", "2026-01-01")
				.param("hasta", "2026-06-30"), jhon)).andExpect(jsonPath("$[*].placa", containsInAnyOrder("BCD47E")));
		mvc.perform(con(get("/api/tecnico/agenda"), dueno)).andExpect(jsonPath("$", hasSize(4)));
		mvc.perform(con(get("/api/tecnico/agenda").param("desde", "2026-12-01").param("hasta", "2026-01-01"), jhon))
				.andExpect(status().isBadRequest());
	}

	@Test
	void flujoCompletoDeUnServicio() throws Exception {
		mvc.perform(con(patch("/api/tecnico/reparaciones/11/tomar"), jhon)).andExpect(status().isOk())
				.andExpect(jsonPath("$.estado").value("EN_PROCESO")).andExpect(jsonPath("$.tecnico").value("Jhon Castaño Ruiz"));
		mvc.perform(con(post("/api/tecnico/reparaciones/11/repuestos"), jhon).content("{\"idRepuesto\":1,\"cantidad\":1}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.costoRepuestos").value(38000.0));
		mvc.perform(con(post("/api/tecnico/reparaciones/11/repuestos"), jhon).content("{\"idRepuesto\":1,\"cantidad\":2}"))
				.andExpect(jsonPath("$.repuestos", hasSize(1))).andExpect(jsonPath("$.costoRepuestos").value(76000.0));
		mvc.perform(con(post("/api/tecnico/reparaciones/11/repuestos"), jhon)
				.content("{\"idRepuesto\":2,\"cantidad\":1,\"precioUnitario\":20000}"))
				.andExpect(jsonPath("$.repuestos", hasSize(2))).andExpect(jsonPath("$.costoRepuestos").value(96000.0));
		mvc.perform(con(delete("/api/tecnico/reparaciones/11/repuestos/2"), jhon))
				.andExpect(jsonPath("$.repuestos", hasSize(1)));
		mvc.perform(con(patch("/api/tecnico/reparaciones/11/finalizar"), jhon).content("{}"))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("costo")));
		mvc.perform(con(patch("/api/tecnico/reparaciones/11/finalizar"), jhon)
				.content("{\"costo\":80000,\"descripcion\":\"Mantenimiento de los 1000 km realizado\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("FINALIZADA"))
				.andExpect(jsonPath("$.fechaFinalizacion").isNotEmpty()).andExpect(jsonPath("$.costoTotal").value(156000.0));
	}

	@Test
	void reglasDeAsignacion() throws Exception {
		mvc.perform(con(patch("/api/tecnico/reparaciones/11/tomar"), diego)).andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje", containsString("Sede Chapinero")));
		mvc.perform(con(patch("/api/tecnico/reparaciones/11/tomar"), dueno)).andExpect(status().isForbidden());
		mvc.perform(con(patch("/api/tecnico/reparaciones/11/tomar"), jhon)).andExpect(status().isOk());
		mvc.perform(con(patch("/api/tecnico/reparaciones/11/tomar"), felipe)).andExpect(status().isConflict());
		mvc.perform(con(post("/api/tecnico/reparaciones/11/repuestos"), felipe).content("{\"idRepuesto\":1,\"cantidad\":1}"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("Solo el técnico asignado puede modificar esta reparación."));
		mvc.perform(con(post("/api/tecnico/reparaciones/1/repuestos"), jhon).content("{\"idRepuesto\":1,\"cantidad\":1}"))
				.andExpect(status().isConflict());
	}

	@Test
	void historialYBuscadorDeRepuestos() throws Exception {
		mvc.perform(con(get("/api/tecnico/historial/gtk21f"), jhon)).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].costoTotal").value(303000.0)).andExpect(jsonPath("$[0].repuestos", hasSize(4)));
		mvc.perform(con(get("/api/tecnico/repuestos").param("categoria", "frenos"), jhon))
				.andExpect(jsonPath("$", hasSize(3)));
		mvc.perform(con(get("/api/tecnico/repuestos").param("texto", "pastillas"), jhon))
				.andExpect(jsonPath("$", hasSize(2)));
		mvc.perform(con(get("/api/tecnico/asignadas"), felipe))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("BCD47E", "STU28H")));
	}
}

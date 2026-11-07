package co.edu.unbosque.BackMotos.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

	private String reparacion(String placa, Object fecha, String tecnicos, String repuestos) {
		return "{\"placa\":\"" + placa + "\",\"fecha\":\"" + fecha + "\",\"descripcion\":\"Cambio de aceite y filtro\","
				+ "\"costo\":60000,\"idTecnicos\":[" + tecnicos + "],\"repuestos\":[" + repuestos + "]}";
	}

	@Test
	void reparacionesDeLaSedeConFiltros() throws Exception {
		mvc.perform(con(get("/api/tecnico/reparaciones"), jhon)).andExpect(status().isOk())
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("JKL78D", "HBN46C", "GTK21F", "HBN45C")));
		mvc.perform(con(get("/api/tecnico/reparaciones").param("idSucursal", "4"), jhon)).andExpect(jsonPath("$", hasSize(4)));
		mvc.perform(con(get("/api/tecnico/reparaciones").param("desde", "2026-01-01").param("hasta", "2026-06-30"), jhon))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("HBN46C")));
		mvc.perform(con(get("/api/tecnico/reparaciones"), dueno)).andExpect(jsonPath("$", hasSize(10)));
		mvc.perform(con(get("/api/tecnico/reparaciones").param("idSucursal", "4"), dueno))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("CDE34J", "CDE33J")));
		mvc.perform(con(get("/api/tecnico/reparaciones").param("desde", "2026-12-01").param("hasta", "2026-01-01"), jhon))
				.andExpect(status().isBadRequest());
		mvc.perform(con(get("/api/tecnico/mis-reparaciones"), felipe))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("HBN46C", "HBN45C")));
	}

	@Test
	void registraUnaReparacionHechaYLaCorrige() throws Exception {
		String cuerpo = mvc.perform(con(post("/api/tecnico/reparaciones"), jhon).content(reparacion("hbn45c", "2026-10-30", "5",
				"{\"idRepuesto\":1,\"cantidad\":1},{\"idRepuesto\":2,\"cantidad\":1,\"precioUnitario\":20000}")))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.placa").value("HBN45C"))
				.andExpect(jsonPath("$.tecnicos[*].nombre", containsInAnyOrder("Jhon Castaño Ruiz", "Felipe Ortiz Lozano")))
				.andExpect(jsonPath("$.sucursal").value("Sede Chapinero")).andExpect(jsonPath("$.idCliente").value(8))
				.andExpect(jsonPath("$.costoRepuestos").value(58000.0)).andExpect(jsonPath("$.costoTotal").value(118000.0))
				.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.read(cuerpo, "$.idReparacion");
		mvc.perform(con(put("/api/tecnico/reparaciones/" + id), felipe)
				.content(reparacion("HBN45C", "2026-10-30", "", "{\"idRepuesto\":2,\"cantidad\":2}")))
				.andExpect(status().isOk()).andExpect(jsonPath("$.tecnicos", hasSize(1)))
				.andExpect(jsonPath("$.repuestos", hasSize(1))).andExpect(jsonPath("$.costoTotal").value(104000.0));
		mvc.perform(con(get("/api/tecnico/reparaciones/" + id), diego)).andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje", containsString("Sede Chapinero")));
		mvc.perform(con(get("/api/tecnico/reparaciones/" + id), dueno)).andExpect(status().isOk());
		mvc.perform(con(get("/api/tecnico/historial/hbn45c"), jhon)).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void reglasDelRegistro() throws Exception {
		mvc.perform(con(post("/api/tecnico/reparaciones"), jhon).content(reparacion("GTK22F", "2026-10-30", "", "")))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("vendidas")));
		mvc.perform(con(post("/api/tecnico/reparaciones"), jhon).content(reparacion("ZZZ99Z", "2026-10-30", "", "")))
				.andExpect(status().isNotFound());
		mvc.perform(con(post("/api/tecnico/reparaciones"), jhon).content(reparacion("HBN45C", "2026-05-01", "", "")))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("anterior a la venta")));
		mvc.perform(con(post("/api/tecnico/reparaciones"), jhon).content(reparacion("HBN45C", "2026-10-30", "7", "")))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("misma sede")));
		mvc.perform(con(post("/api/tecnico/reparaciones"), jhon).content(reparacion("HBN45C", "2026-10-30", "",
				"{\"idRepuesto\":1,\"cantidad\":1},{\"idRepuesto\":1,\"cantidad\":2}"))).andExpect(status().isBadRequest());
		mvc.perform(con(post("/api/tecnico/reparaciones"), jhon)
				.content(reparacion("HBN45C", LocalDate.now().plusDays(5), "", ""))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos[0].campo").value("fecha"));
		mvc.perform(con(post("/api/tecnico/reparaciones"), dueno).content(reparacion("HBN45C", "2026-10-30", "", "")))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje", containsString("al menos un técnico")));
		mvc.perform(con(put("/api/tecnico/reparaciones/1"), felipe).content(reparacion("JKL78D", "2025-12-15", "", "")))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("participó")));
		mvc.perform(con(put("/api/tecnico/reparaciones/4"), jhon).content(reparacion("QRS36A", "2026-05-15", "", "")))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("Sede Suba")));
		String laura = token("laura.gomez");
		mvc.perform(con(get("/api/tecnico/reparaciones"), laura)).andExpect(status().isForbidden());
	}

	@Test
	void historialRepuestosYTecnicos() throws Exception {
		mvc.perform(con(get("/api/tecnico/historial/gtk21f"), jhon)).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].costoTotal").value(303000.0)).andExpect(jsonPath("$[0].repuestos", hasSize(4)))
				.andExpect(jsonPath("$[0].tecnicos[0].nombre").value("Jhon Castaño Ruiz"));
		mvc.perform(con(get("/api/tecnico/repuestos").param("categoria", "frenos"), jhon))
				.andExpect(jsonPath("$", hasSize(3)));
		mvc.perform(con(get("/api/tecnico/repuestos").param("texto", "pastillas"), jhon))
				.andExpect(jsonPath("$", hasSize(2)));
		mvc.perform(con(get("/api/tecnico/tecnicos"), jhon))
				.andExpect(jsonPath("$[*].nombre", containsInAnyOrder("Felipe Ortiz Lozano", "Jhon Castaño Ruiz")));
		mvc.perform(con(get("/api/tecnico/tecnicos"), dueno)).andExpect(jsonPath("$", hasSize(5)));
	}
}

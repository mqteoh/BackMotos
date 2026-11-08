package co.edu.unbosque.BackMotos.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class SeguridadTest {

	private static final String CLAVE_EMPLEADO = "Motos2026*";
	private static final String CLAVE_CLIENTE = "Cliente2026*";
	private static final String DUENO = "ricardo.salazar@concesionario-motos.test";
	private static final String VENDEDORA = "laura.gomez@concesionario-motos.test";
	private static final String TECNICO = "jhon.castano@concesionario-motos.test";
	private static final String CLIENTA = "maria.ramirez@correo.test";
	private static final String[] AREAS = { "cliente", "vendedor", "tecnico", "admin" };

	@Autowired
	private MockMvc mvc;

	private String token(String correo, String clave) throws Exception {
		String cuerpo = mvc
				.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
						.content("{\"correo\":\"" + correo + "\",\"contrasena\":\"" + clave + "\"}"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return JsonPath.read(cuerpo, "$.token");
	}

	@Test
	void loginDevuelveTokenYRolDeCadaTipoDeUsuario() throws Exception {
		Map<String, String[]> casos = Map.of(DUENO, new String[] { CLAVE_EMPLEADO, "DUENO" }, VENDEDORA,
				new String[] { CLAVE_EMPLEADO, "VENDEDOR" }, TECNICO, new String[] { CLAVE_EMPLEADO, "TECNICO" },
				CLIENTA, new String[] { CLAVE_CLIENTE, "CLIENTE" });
		for (Map.Entry<String, String[]> caso : casos.entrySet()) {
			mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(
					"{\"correo\":\"" + caso.getKey() + "\",\"contrasena\":\"" + caso.getValue()[0] + "\"}"))
					.andExpect(status().isOk()).andExpect(jsonPath("$.tipo").value("Bearer"))
					.andExpect(jsonPath("$.usuario.rol").value(caso.getValue()[1]))
					.andExpect(jsonPath("$.usuario.correo").value(caso.getKey()));
		}
	}

	@Test
	void credencialesIncorrectasResponden401SinRevelarCual() throws Exception {
		for (String cuerpo : new String[] { "{\"correo\":\"" + VENDEDORA + "\",\"contrasena\":\"otra\"}",
				"{\"correo\":\"nadie@correo.test\",\"contrasena\":\"Motos2026*\"}" }) {
			mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
					.andExpect(status().isUnauthorized())
					.andExpect(jsonPath("$.mensaje").value("Correo o contraseña incorrectos."));
		}
	}

	@Test
	void sinTokenSoloSeAccedeALoPublico() throws Exception {
		mvc.perform(get("/api/publico/prueba-rol")).andExpect(status().isOk());
		for (String area : AREAS) {
			mvc.perform(get("/api/" + area + "/prueba-rol")).andExpect(status().isUnauthorized())
					.andExpect(jsonPath("$.mensaje").value("Debe iniciar sesión para continuar."));
		}
	}

	@Test
	void cadaRolEntraSoloASusAreas() throws Exception {
		Map<String, String> tokens = Map.of("CLIENTE", token(CLIENTA, CLAVE_CLIENTE), "VENDEDOR",
				token(VENDEDORA, CLAVE_EMPLEADO), "TECNICO", token(TECNICO, CLAVE_EMPLEADO), "DUENO",
				token(DUENO, CLAVE_EMPLEADO));
		Map<String, String> permitidas = Map.of("CLIENTE", "cliente", "VENDEDOR", "vendedor", "TECNICO", "tecnico",
				"DUENO", "vendedor tecnico admin");
		for (Map.Entry<String, String> rol : tokens.entrySet()) {
			for (String area : AREAS) {
				boolean permitido = permitidas.get(rol.getKey()).contains(area);
				mvc.perform(get("/api/" + area + "/prueba-rol").header(HttpHeaders.AUTHORIZATION,
						"Bearer " + rol.getValue()))
						.andExpect(permitido ? status().isOk() : status().isForbidden());
			}
		}
	}

	@Test
	void elTokenIdentificaAlUsuarioYUnTokenAlteradoNoSirve() throws Exception {
		String token = token(VENDEDORA, CLAVE_EMPLEADO);
		mvc.perform(get("/auth/yo").header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Laura Gómez Torres")).andExpect(jsonPath("$.idSucursal").value(1));
		mvc.perform(get("/api/vendedor/prueba-rol").header(HttpHeaders.AUTHORIZATION, "Bearer " + token + "x"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void corsSoloPermiteElOrigenDelFront() throws Exception {
		mvc.perform(options("/auth/login").header(HttpHeaders.ORIGIN, "http://localhost:4200")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")).andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200"));
		mvc.perform(options("/auth/login").header(HttpHeaders.ORIGIN, "http://otro-sitio.test")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")).andExpect(status().isForbidden());
	}
}

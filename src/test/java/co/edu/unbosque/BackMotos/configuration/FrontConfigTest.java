package co.edu.unbosque.BackMotos.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FrontConfigTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void distingueRutasDelFrontDeLaApiYDeLosArchivos() {
		assertThat(FrontConfig.esRutaDelFront("admin/motos")).isTrue();
		assertThat(FrontConfig.esRutaDelFront("motos/ABC12D")).isTrue();
		assertThat(FrontConfig.esRutaDelFront("api/publico/nada")).isFalse();
		assertThat(FrontConfig.esRutaDelFront("auth/nada")).isFalse();
		assertThat(FrontConfig.esRutaDelFront("main-ABC123.js")).isFalse();
	}

	@Test
	void lasRutasDelFrontDevuelvenElIndiceSinIniciarSesion() throws Exception {
		mvc.perform(get("/cliente/reservas")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Front de prueba")));
		mvc.perform(get("/admin/motos/ABC12D")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Front de prueba")));
	}

	@Test
	void losArchivosQueNoExistenYLaApiSiguenRespondiendoConErrores() throws Exception {
		mvc.perform(get("/no-existe.js")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.estado").value(404));
		mvc.perform(get("/api/admin/marcas")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/publico/no-existe")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.estado").value(404));
	}

	@Test
	void laDocumentacionNoLaAtrapaElFront() throws Exception {
		mvc.perform(get("/docs")).andExpect(status().is3xxRedirection());
		mvc.perform(get("/v3/api-docs")).andExpect(status().isOk()).andExpect(jsonPath("$.openapi").exists());
	}
}

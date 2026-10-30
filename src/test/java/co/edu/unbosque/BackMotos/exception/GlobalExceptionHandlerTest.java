package co.edu.unbosque.BackMotos.exception;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void triggerDeLaBaseResponde409ConSuMensaje() throws Exception {
		mvc.perform(post("/prueba-errores/reserva-moto-vendida")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.estado").value(409)).andExpect(jsonPath("$.mensaje").value("La moto ya fue vendida"))
				.andExpect(jsonPath("$.ruta").value("/prueba-errores/reserva-moto-vendida"));
	}

	@Test
	void checkDeLaBaseResponde409ConMensajeLegible() throws Exception {
		mvc.perform(post("/prueba-errores/moto-externa-con-sede")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value(containsString("moto externa no lleva")));
	}

	@Test
	void validacionResponde400ConLosCampos() throws Exception {
		mvc.perform(post("/prueba-errores/marca").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos", hasSize(2)))
				.andExpect(jsonPath("$.campos[?(@.campo == 'nombre')].mensaje").value("El nombre es obligatorio."));
	}

	@Test
	void jsonMalFormadoResponde400() throws Exception {
		mvc.perform(post("/prueba-errores/marca").contentType(MediaType.APPLICATION_JSON).content("{nombre"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("La petición no tiene el formato esperado."));
	}

	@Test
	void duplicadoDetectadoPorElServicioResponde409() throws Exception {
		mvc.perform(post("/prueba-errores/marca").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\":\"yamaha\",\"paisOrigen\":\"Japón\"}")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("Ya existe una marca con el nombre yamaha."));
	}

	@Test
	void inexistenteResponde404() throws Exception {
		mvc.perform(get("/prueba-errores/marca/999")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.mensaje").value("No existe la marca con id 999."));
	}

	@Test
	void tipoDeParametroInvalidoResponde400() throws Exception {
		mvc.perform(get("/prueba-errores/marca/abc")).andExpect(status().isBadRequest());
	}

	@Test
	void borrarRegistroConDependientesResponde409() throws Exception {
		mvc.perform(delete("/prueba-errores/marca/1")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("No se puede eliminar porque otros registros dependen de él."));
	}
}

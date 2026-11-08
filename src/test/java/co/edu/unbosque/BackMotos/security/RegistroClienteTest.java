package co.edu.unbosque.BackMotos.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegistroClienteTest {

	@Autowired
	private MockMvc mvc;

	private String registro(String documento, String correo, String clave) {
		return "{\"documento\":\"" + documento + "\",\"nombres\":\"Ana\",\"apellidos\":\"Prueba Registro\","
				+ "\"fechaNacimiento\":\"2000-05-10\",\"ciudad\":\"Bogotá\",\"barrio\":\"Teusaquillo\","
				+ "\"calle\":\"Calle 45\",\"numeroDireccion\":\"20-10\",\"telefono\":\"3001112233\",\"correo\":\""
				+ correo + "\",\"contrasena\":\"" + clave + "\"}";
	}

	@Test
	void clienteNuevoSeRegistraYPuedeIniciarSesion() throws Exception {
		mvc.perform(post("/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content(registro("1099888777", "Ana.Prueba@Correo.test", "ClaveSegura1")))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.usuario.rol").value("CLIENTE"))
				.andExpect(jsonPath("$.usuario.correo").value("ana.prueba@correo.test"));
		mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"ana.prueba@correo.test\",\"contrasena\":\"ClaveSegura1\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.usuario.nombre").value("Ana Prueba Registro"));
	}

	@Test
	void clienteRegistradoEnSedeActivaSuCuentaSinDuplicarse() throws Exception {
		mvc.perform(post("/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content(registro("1030567123", "kevin.arias@correo.test", "ClaveSegura1")))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.usuario.id").value(11))
				.andExpect(jsonPath("$.usuario.nombre").value("Kevin Arias Pinzón"));
	}

	@Test
	void noPermiteCorreoDeOtraCuentaNiDocumentoConCuenta() throws Exception {
		mvc.perform(post("/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content(registro("1099888776", "laura.gomez@concesionario-motos.test", "ClaveSegura1")))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje").value("Ese correo ya está registrado."));
		mvc.perform(post("/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content(registro("1013654789", "otra.cuenta@correo.test", "ClaveSegura1")))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("Ya existe una cuenta para ese documento."));
	}

	@Test
	void contrasenaDebilSeRechaza() throws Exception {
		mvc.perform(post("/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content(registro("1099888775", "debil@correo.test", "corta"))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos[0].campo").value("contrasena"));
	}
}

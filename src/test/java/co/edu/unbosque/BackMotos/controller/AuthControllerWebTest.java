package co.edu.unbosque.BackMotos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.unbosque.BackMotos.dto.TokenDTO;
import co.edu.unbosque.BackMotos.dto.UsuarioDTO;
import co.edu.unbosque.BackMotos.security.JwtAuthenticationFilter;
import co.edu.unbosque.BackMotos.security.JwtUtil;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.SecurityConfig;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import co.edu.unbosque.BackMotos.service.AuthService;

@WebMvcTest(AuthController.class)
@Import({ SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtil.class })
class AuthControllerWebTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JwtUtil jwtUtil;

	@MockBean
	private AuthService authService;

	@Test
	void loginDevuelveElTokenDelServicio() throws Exception {
		when(authService.login(any())).thenReturn(new TokenDTO("abc", "Bearer", 1000,
				new UsuarioDTO(2, "Laura Gómez Torres", "laura@x.test", Rol.VENDEDOR, 1)));
		mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"laura@x.test\",\"contrasena\":\"Clave123\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("abc")).andExpect(jsonPath("$.usuario.rol").value("VENDEDOR"));
	}

	@Test
	void cuerpoIncompletoNoLlegaAlServicio() throws Exception {
		mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"correo\":\"\"}"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos.length()").value(2));
		verify(authService, never()).login(any());
	}

	@Test
	void credencialesInvalidasResponden401() throws Exception {
		when(authService.login(any())).thenThrow(new BadCredentialsException("x"));
		mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"a@x.test\",\"contrasena\":\"mal\"}")).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.mensaje").value("Correo o contraseña incorrectos."));
	}

	@Test
	void yoRequiereTokenYDevuelveSusDatos() throws Exception {
		mvc.perform(get("/auth/yo")).andExpect(status().isUnauthorized());
		String token = jwtUtil.generarToken(new UsuarioAutenticado(9, "cliente@x.test", null, "Cliente Nueve", Rol.CLIENTE, null));
		mvc.perform(get("/auth/yo").header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(9)).andExpect(jsonPath("$.rol").value("CLIENTE"));
	}
}

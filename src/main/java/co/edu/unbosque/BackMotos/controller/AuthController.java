package co.edu.unbosque.BackMotos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.LoginDTO;
import co.edu.unbosque.BackMotos.dto.RegistroClienteDTO;
import co.edu.unbosque.BackMotos.dto.TokenDTO;
import co.edu.unbosque.BackMotos.dto.UsuarioDTO;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import co.edu.unbosque.BackMotos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Inicio de sesión de clientes y empleados, y registro de clientes")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	@Operation(summary = "Inicia sesión y devuelve un token JWT")
	public TokenDTO login(@Valid @RequestBody LoginDTO datos) {
		return authService.login(datos);
	}

	@PostMapping("/registro")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Crea la cuenta web de un cliente; si ya estaba registrado en sede, le activa la cuenta")
	public TokenDTO registro(@Valid @RequestBody RegistroClienteDTO datos) {
		return authService.registrarCliente(datos);
	}

	@GetMapping("/yo")
	@Operation(summary = "Devuelve los datos del usuario del token")
	public UsuarioDTO yo(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return UsuarioDTO.de(usuario);
	}
}

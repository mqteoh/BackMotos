package co.edu.unbosque.BackMotos.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.UsuarioDTO;

@RestController
class PruebaRolesController {

	@GetMapping("/api/publico/prueba-rol")
	String publico() {
		return "publico";
	}

	@GetMapping({ "/api/cliente/prueba-rol", "/api/vendedor/prueba-rol", "/api/tecnico/prueba-rol",
			"/api/admin/prueba-rol" })
	UsuarioDTO protegido(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return UsuarioDTO.de(usuario);
	}
}

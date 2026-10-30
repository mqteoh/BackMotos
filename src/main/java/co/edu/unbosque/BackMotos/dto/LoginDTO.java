package co.edu.unbosque.BackMotos.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank(message = "El correo es obligatorio.") String correo,
		@NotBlank(message = "La contraseña es obligatoria.") String contrasena) {
}

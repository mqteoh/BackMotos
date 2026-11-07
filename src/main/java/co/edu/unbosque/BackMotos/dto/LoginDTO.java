package co.edu.unbosque.BackMotos.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Correo y contraseña para iniciar sesión.
 *
 * @param correo correo electrónico
 * @param contrasena contraseña en texto plano; solo se recibe, nunca se devuelve
 */
public record LoginDTO(@NotBlank(message = "El correo es obligatorio.") String correo,
		@NotBlank(message = "La contraseña es obligatoria.") String contrasena) {
}

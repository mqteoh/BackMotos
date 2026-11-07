package co.edu.unbosque.BackMotos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos para que un cliente cree su cuenta en la web.
 *
 * @param documento número de documento de identidad
 * @param nombres nombres de la persona
 * @param apellidos apellidos de la persona
 * @param fechaNacimiento fecha de nacimiento
 * @param ciudad ciudad
 * @param barrio barrio de la dirección
 * @param calle calle de la dirección
 * @param numeroDireccion número de la dirección
 * @param telefono número de teléfono
 * @param correo correo electrónico
 * @param contrasena contraseña en texto plano; solo se recibe, nunca se devuelve
 */
public record RegistroClienteDTO(
		@NotBlank(message = "El documento es obligatorio.") @Pattern(regexp = "^[0-9]{5,15}$", message = "El documento debe tener entre 5 y 15 dígitos.") String documento,
		@NotBlank(message = "Los nombres son obligatorios.") @Size(max = 60, message = "Los nombres admiten máximo 60 caracteres.") String nombres,
		@NotBlank(message = "Los apellidos son obligatorios.") @Size(max = 60, message = "Los apellidos admiten máximo 60 caracteres.") String apellidos,
		@NotNull(message = "La fecha de nacimiento es obligatoria.") @Past(message = "La fecha de nacimiento debe estar en el pasado.") LocalDate fechaNacimiento,
		@NotBlank(message = "La ciudad es obligatoria.") @Size(max = 60, message = "La ciudad admite máximo 60 caracteres.") String ciudad,
		@NotBlank(message = "El barrio es obligatorio.") @Size(max = 60, message = "El barrio admite máximo 60 caracteres.") String barrio,
		@NotBlank(message = "La calle es obligatoria.") @Size(max = 60, message = "La calle admite máximo 60 caracteres.") String calle,
		@NotBlank(message = "El número de la dirección es obligatorio.") @Size(max = 20, message = "El número de la dirección admite máximo 20 caracteres.") String numeroDireccion,
		@Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos.") String telefono,
		@NotBlank(message = "El correo es obligatorio.") @Email(message = "El correo no es válido.") @Size(max = 120, message = "El correo admite máximo 120 caracteres.") String correo,
		@NotBlank(message = "La contraseña es obligatoria.") @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,72}$", message = "La contraseña debe tener entre 8 y 72 caracteres, con al menos una letra y un número.") String contrasena) {
}

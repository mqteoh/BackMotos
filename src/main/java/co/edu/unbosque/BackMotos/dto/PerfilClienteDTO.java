package co.edu.unbosque.BackMotos.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PerfilClienteDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer idCliente,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String documento,
		@NotBlank(message = "Los nombres son obligatorios.") @Size(max = 60, message = "Los nombres admiten máximo 60 caracteres.") String nombres,
		@NotBlank(message = "Los apellidos son obligatorios.") @Size(max = 60, message = "Los apellidos admiten máximo 60 caracteres.") String apellidos,
		@NotNull(message = "La fecha de nacimiento es obligatoria.") @Past(message = "La fecha de nacimiento debe estar en el pasado.") LocalDate fechaNacimiento,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer edad,
		@NotBlank(message = "La ciudad es obligatoria.") @Size(max = 60, message = "La ciudad admite máximo 60 caracteres.") String ciudad,
		@NotBlank(message = "El barrio es obligatorio.") @Size(max = 60, message = "El barrio admite máximo 60 caracteres.") String barrio,
		@NotBlank(message = "La calle es obligatoria.") @Size(max = 60, message = "La calle admite máximo 60 caracteres.") String calle,
		@NotBlank(message = "El número de la dirección es obligatorio.") @Size(max = 20, message = "El número de la dirección admite máximo 20 caracteres.") String numeroDireccion,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String correoAcceso,
		@Size(max = 5, message = "Se admiten máximo 5 teléfonos.") List<@Pattern(regexp = "^[0-9]{7,15}$", message = "Cada teléfono debe tener entre 7 y 15 dígitos.") String> telefonos,
		@Size(max = 5, message = "Se admiten máximo 5 correos.") List<@Email(message = "Cada correo debe ser válido.") @Size(max = 120, message = "Cada correo admite máximo 120 caracteres.") String> correos) {
}

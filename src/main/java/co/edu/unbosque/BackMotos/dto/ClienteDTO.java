package co.edu.unbosque.BackMotos.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.BackMotos.entity.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer idCliente,
		@NotBlank(message = "El documento es obligatorio.") @Pattern(regexp = "^[0-9]{5,15}$", message = "El documento debe tener entre 5 y 15 dígitos.") String documento,
		@NotBlank(message = "Los nombres son obligatorios.") @Size(max = 60, message = "Los nombres admiten máximo 60 caracteres.") String nombres,
		@NotBlank(message = "Los apellidos son obligatorios.") @Size(max = 60, message = "Los apellidos admiten máximo 60 caracteres.") String apellidos,
		@NotNull(message = "La fecha de nacimiento es obligatoria.") @Past(message = "La fecha de nacimiento debe estar en el pasado.") LocalDate fechaNacimiento,
		@NotBlank(message = "La ciudad es obligatoria.") @Size(max = 60, message = "La ciudad admite máximo 60 caracteres.") String ciudad,
		@NotBlank(message = "El barrio es obligatorio.") @Size(max = 60, message = "El barrio admite máximo 60 caracteres.") String barrio,
		@NotBlank(message = "La calle es obligatoria.") @Size(max = 60, message = "La calle admite máximo 60 caracteres.") String calle,
		@NotBlank(message = "El número de la dirección es obligatorio.") @Size(max = 20, message = "El número de la dirección admite máximo 20 caracteres.") String numeroDireccion,
		@Size(max = 5, message = "Se admiten máximo 5 teléfonos.") List<@Pattern(regexp = "^[0-9]{7,15}$", message = "Cada teléfono debe tener entre 7 y 15 dígitos.") String> telefonos,
		@Size(max = 5, message = "Se admiten máximo 5 correos.") List<@Email(message = "Cada correo debe ser válido.") @Size(max = 120, message = "Cada correo admite máximo 120 caracteres.") String> correos,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) Boolean tieneCuentaWeb) {

	public static ClienteDTO de(Cliente c) {
		return new ClienteDTO(c.getIdCliente(), c.getDocumento(), c.getNombres(), c.getApellidos(),
				c.getFechaNacimiento(), c.getCiudad(), c.getBarrio(), c.getCalle(), c.getNumeroDireccion(),
				new ArrayList<>(c.getTelefonos()), new ArrayList<>(c.getCorreos()), c.tieneCuentaWeb());
	}
}

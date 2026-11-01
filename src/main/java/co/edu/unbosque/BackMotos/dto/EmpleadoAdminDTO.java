package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.edu.unbosque.BackMotos.entity.Empleado;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.TipoEmpleado;
import co.edu.unbosque.BackMotos.entity.Vendedor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpleadoAdminDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer idEmpleado,
		@NotBlank(message = "El documento es obligatorio.") @Pattern(regexp = "^[0-9]{5,15}$", message = "El documento debe tener entre 5 y 15 dígitos.") String documento,
		@NotBlank(message = "Los nombres son obligatorios.") @Size(max = 60, message = "Los nombres admiten máximo 60 caracteres.") String nombres,
		@NotBlank(message = "Los apellidos son obligatorios.") @Size(max = 60, message = "Los apellidos admiten máximo 60 caracteres.") String apellidos,
		@NotNull(message = "La fecha de contratación es obligatoria.") @PastOrPresent(message = "La fecha de contratación no puede ser futura.") LocalDate fechaContratacion,
		@NotNull(message = "El tipo de empleado es obligatorio.") TipoEmpleado tipoEmpleado,
		Integer idSucursal, Integer idJefe,
		@NotBlank(message = "El correo de acceso es obligatorio.") @Email(message = "El correo de acceso no es válido.") @Size(max = 120, message = "El correo admite máximo 120 caracteres.") String correoAcceso,
		@Size(max = 5, message = "Se admiten máximo 5 teléfonos.") List<@Pattern(regexp = "^[0-9]{7,15}$", message = "Cada teléfono debe tener entre 7 y 15 dígitos.") String> telefonos,
		@Size(max = 80, message = "El certificado admite máximo 80 caracteres.") String certificadoVentas,
		@DecimalMin(value = "0", message = "La comisión mínima es 0.") @DecimalMax(value = "100", message = "La comisión máxima es 100.") BigDecimal comision,
		@Size(max = 80, message = "El certificado admite máximo 80 caracteres.") String certificadoTecnico,
		@Size(max = 60, message = "La especialidad admite máximo 60 caracteres.") String especialidad,
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,72}$", message = "La contraseña debe tener entre 8 y 72 caracteres, con al menos una letra y un número.") String contrasena,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String sucursal,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String jefe) {

	public static EmpleadoAdminDTO de(Empleado e) {
		Vendedor v = e instanceof Vendedor vendedor ? vendedor : null;
		Tecnico t = e instanceof Tecnico tecnico ? tecnico : null;
		return new EmpleadoAdminDTO(e.getIdEmpleado(), e.getDocumento(), e.getNombres(), e.getApellidos(),
				e.getFechaContratacion(), e.getTipoEmpleado(),
				e.getSucursal() == null ? null : e.getSucursal().getIdSucursal(),
				e.getJefe() == null ? null : e.getJefe().getIdEmpleado(), e.getCorreoAcceso(),
				new ArrayList<>(e.getTelefonos()), v == null ? null : v.getCertificadoVentas(),
				v == null ? null : v.getComision(), t == null ? null : t.getCertificadoTecnico(),
				t == null ? null : t.getEspecialidad(), null, e.getSucursal() == null ? null : e.getSucursal().getNombre(),
				e.getJefe() == null ? null : e.getJefe().getNombreCompleto());
	}
}

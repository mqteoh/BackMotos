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

/**
 * Datos de un empleado y de su cuenta para el panel del dueño.
 * La contraseña solo se recibe; nunca se devuelve.
 *
 * @param idEmpleado identificador del empleado
 * @param documento número de documento de identidad
 * @param nombres nombres de la persona
 * @param apellidos apellidos de la persona
 * @param fechaContratacion fecha en que se contrató al empleado
 * @param tipoEmpleado cargo: DUENO, VENDEDOR o TECNICO
 * @param salario salario mensual
 * @param idSucursal identificador de la sede donde trabaja
 * @param idJefe identificador del jefe directo; null si no tiene
 * @param correoAcceso correo con el que inicia sesión en la aplicación
 * @param telefonos números de teléfono
 * @param certificadoVentas certificado de ventas del vendedor; solo para vendedores
 * @param comision porcentaje de comisión del vendedor; solo para vendedores
 * @param certificadoTecnico certificado del técnico; solo para técnicos
 * @param especialidad especialidad del técnico; solo para técnicos
 * @param contrasena contraseña en texto plano; solo se recibe, nunca se devuelve
 * @param sucursal nombre de la sede
 * @param jefe nombre del jefe directo; null si no tiene
 */
public record EmpleadoAdminDTO(@Schema(accessMode = Schema.AccessMode.READ_ONLY) Integer idEmpleado,
		@NotBlank(message = "El documento es obligatorio.") @Pattern(regexp = "^[0-9]{5,15}$", message = "El documento debe tener entre 5 y 15 dígitos.") String documento,
		@NotBlank(message = "Los nombres son obligatorios.") @Size(max = 60, message = "Los nombres admiten máximo 60 caracteres.") String nombres,
		@NotBlank(message = "Los apellidos son obligatorios.") @Size(max = 60, message = "Los apellidos admiten máximo 60 caracteres.") String apellidos,
		@NotNull(message = "La fecha de contratación es obligatoria.") @PastOrPresent(message = "La fecha de contratación no puede ser futura.") LocalDate fechaContratacion,
		@NotNull(message = "El tipo de empleado es obligatorio.") TipoEmpleado tipoEmpleado,
		@NotNull(message = "El salario es obligatorio.") @DecimalMin(value = "1", message = "El salario debe ser mayor que cero.") BigDecimal salario,
		@NotNull(message = "La sede es obligatoria.") Integer idSucursal, Integer idJefe,
		@NotBlank(message = "El correo de acceso es obligatorio.") @Email(message = "El correo de acceso no es válido.") @Size(max = 120, message = "El correo admite máximo 120 caracteres.") String correoAcceso,
		@Size(max = 5, message = "Se admiten máximo 5 teléfonos.") List<@Pattern(regexp = "^[0-9]{7,15}$", message = "Cada teléfono debe tener entre 7 y 15 dígitos.") String> telefonos,
		@Size(max = 80, message = "El certificado admite máximo 80 caracteres.") String certificadoVentas,
		@DecimalMin(value = "0", message = "La comisión mínima es 0.") @DecimalMax(value = "100", message = "La comisión máxima es 100.") BigDecimal comision,
		@Size(max = 80, message = "El certificado admite máximo 80 caracteres.") String certificadoTecnico,
		@Size(max = 60, message = "La especialidad admite máximo 60 caracteres.") String especialidad,
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,72}$", message = "La contraseña debe tener entre 8 y 72 caracteres, con al menos una letra y un número.") String contrasena,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String sucursal,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String jefe) {

	/**
	 * Arma el DTO con el correo de la cuenta del empleado.
	 *
	 * @param e empleado que se convierte
	 * @param correoAcceso correo con el que inicia sesión en la aplicación
	 * @return el DTO del empleado
	 */
	public static EmpleadoAdminDTO de(Empleado e, String correoAcceso) {
		Vendedor v = e instanceof Vendedor vendedor ? vendedor : null;
		Tecnico t = e instanceof Tecnico tecnico ? tecnico : null;
		return new EmpleadoAdminDTO(e.getIdEmpleado(), e.getDocumento(), e.getNombres(), e.getApellidos(),
				e.getFechaContratacion(), e.getTipoEmpleado(), e.getSalario(), e.getSucursal().getIdSucursal(),
				e.getJefe() == null ? null : e.getJefe().getIdEmpleado(), correoAcceso,
				new ArrayList<>(e.getTelefonos()), v == null ? null : v.getCertificadoVentas(),
				v == null ? null : v.getComision(), t == null ? null : t.getCertificadoTecnico(),
				t == null ? null : t.getEspecialidad(), null, e.getSucursal().getNombre(),
				e.getJefe() == null ? null : e.getJefe().getNombreCompleto());
	}
}

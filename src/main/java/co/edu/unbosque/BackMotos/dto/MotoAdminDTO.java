package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.EstadoMoto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Moto del inventario para el panel del dueño. El estado solo se lee.
 *
 * @param placa placa de la moto
 * @param idMarca identificador de la marca
 * @param nroModelo número del modelo dentro de la marca
 * @param idSucursal identificador de la sede
 * @param anio año del modelo de la moto
 * @param color color de la moto
 * @param kilometraje kilómetros recorridos
 * @param precio precio de lista
 * @param fechaIngreso fecha en que la moto entró al inventario
 * @param marca nombre de la marca
 * @param modelo nombre del modelo
 * @param sucursal nombre de la sede
 * @param estado DISPONIBLE, RESERVADA o VENDIDA
 */
public record MotoAdminDTO(
		@NotBlank(message = "La placa es obligatoria.") @Pattern(regexp = "^[A-Za-z]{3}[0-9]{2}[A-Za-z]$", message = "La placa debe tener el formato AAA00A.") String placa,
		@NotNull(message = "La marca es obligatoria.") Integer idMarca,
		@NotNull(message = "El modelo es obligatorio.") Integer nroModelo,
		@NotNull(message = "La sede es obligatoria.") Integer idSucursal,
		@NotNull(message = "El año es obligatorio.") @Min(value = 1980, message = "El año mínimo es 1980.") @Max(value = 2030, message = "El año máximo es 2030.") Integer anio,
		@NotBlank(message = "El color es obligatorio.") @Size(max = 30, message = "El color admite máximo 30 caracteres.") String color,
		@NotNull(message = "El kilometraje es obligatorio.") @PositiveOrZero(message = "El kilometraje no puede ser negativo.") Integer kilometraje,
		@NotNull(message = "El precio es obligatorio.") @Positive(message = "El precio debe ser mayor que cero.") @Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.") BigDecimal precio,
		@NotNull(message = "La fecha de ingreso es obligatoria.") LocalDate fechaIngreso,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String marca,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String modelo,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String sucursal,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) EstadoMoto estado) {

	/**
	 * Arma el DTO desde la entidad.
	 *
	 * @param m moto que se convierte
	 * @return el DTO de la moto
	 */
	public static MotoAdminDTO de(Moto m) {
		return new MotoAdminDTO(m.getPlaca(), m.getModelo().getId().getIdMarca(), m.getModelo().getId().getNroModelo(),
				m.getSucursal().getIdSucursal(), m.getAnio(),
				m.getColor(), m.getKilometraje(), m.getPrecio(), m.getFechaIngreso(), m.getModelo().getMarca().getNombre(),
				m.getModelo().getNombre(), m.getSucursal().getNombre(), m.getEstado());
	}
}

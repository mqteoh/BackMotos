package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;
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

public record MotoAdminDTO(
		@NotBlank(message = "La placa es obligatoria.") @Pattern(regexp = "^[A-Za-z]{3}[0-9]{2}[A-Za-z]$", message = "La placa debe tener el formato AAA00A.") String placa,
		@NotNull(message = "La marca es obligatoria.") Integer idMarca,
		@NotNull(message = "El modelo es obligatorio.") Integer nroModelo,
		Integer idSucursal,
		@NotNull(message = "El origen es obligatorio.") OrigenMoto origen,
		@NotNull(message = "El año es obligatorio.") @Min(value = 1980, message = "El año mínimo es 1980.") @Max(value = 2030, message = "El año máximo es 2030.") Integer anio,
		@NotBlank(message = "El color es obligatorio.") @Size(max = 30, message = "El color admite máximo 30 caracteres.") String color,
		@NotNull(message = "El kilometraje es obligatorio.") @PositiveOrZero(message = "El kilometraje no puede ser negativo.") Integer kilometraje,
		@Positive(message = "El precio debe ser mayor que cero.") @Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.") BigDecimal precio,
		@NotNull(message = "La fecha de ingreso es obligatoria.") LocalDate fechaIngreso,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String marca,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String modelo,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String sucursal,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) Boolean vendida) {

	public static MotoAdminDTO de(Moto m, boolean vendida) {
		return new MotoAdminDTO(m.getPlaca(), m.getModelo().getId().getIdMarca(), m.getModelo().getId().getNroModelo(),
				m.getSucursal() == null ? null : m.getSucursal().getIdSucursal(), m.getOrigen(), m.getAnio(),
				m.getColor(), m.getKilometraje(), m.getPrecio(), m.getFechaIngreso(), m.getModelo().getMarca().getNombre(),
				m.getModelo().getNombre(), m.getSucursal() == null ? null : m.getSucursal().getNombre(), vendida);
	}
}

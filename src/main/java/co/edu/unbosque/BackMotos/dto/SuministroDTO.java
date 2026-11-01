package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import co.edu.unbosque.BackMotos.entity.Suministro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record SuministroDTO(@NotBlank(message = "El proveedor es obligatorio.") String nit,
		@NotNull(message = "El repuesto es obligatorio.") Integer idRepuesto,
		@NotNull(message = "La sede es obligatoria.") Integer idSucursal,
		@PastOrPresent(message = "La fecha de la entrega no puede ser futura.") LocalDateTime fechaSuministro,
		@NotNull(message = "La cantidad es obligatoria.") @Min(value = 1, message = "La cantidad mínima es 1.") @Max(value = 9999, message = "La cantidad máxima es 9999.") Integer cantidad,
		@NotNull(message = "El costo unitario es obligatorio.") @Positive(message = "El costo unitario debe ser mayor que cero.") @Digits(integer = 10, fraction = 2, message = "El costo admite hasta 10 enteros y 2 decimales.") BigDecimal costoUnitario,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String proveedor,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String repuesto,
		@Schema(accessMode = Schema.AccessMode.READ_ONLY) String sucursal) {

	public static SuministroDTO de(Suministro s) {
		return new SuministroDTO(s.getProveedor().getNit(), s.getRepuesto().getIdRepuesto(),
				s.getSucursal().getIdSucursal(), s.getFechaSuministro(), s.getCantidad(), s.getCostoUnitario(),
				s.getProveedor().getNombre(), s.getRepuesto().getNombre(), s.getSucursal().getNombre());
	}
}

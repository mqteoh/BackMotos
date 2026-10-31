package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UsoRepuestoDTO(@NotNull(message = "El repuesto es obligatorio.") Integer idRepuesto,
		@NotNull(message = "La cantidad es obligatoria.") @Min(value = 1, message = "La cantidad mínima es 1.") @Max(value = 99, message = "La cantidad máxima es 99.") Integer cantidad,
		@Positive(message = "El precio unitario debe ser mayor que cero.") @Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.") BigDecimal precioUnitario) {
}

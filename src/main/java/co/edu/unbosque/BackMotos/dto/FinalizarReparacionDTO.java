package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record FinalizarReparacionDTO(
		@PositiveOrZero(message = "El costo no puede ser negativo.") @Digits(integer = 10, fraction = 2, message = "El costo admite hasta 10 enteros y 2 decimales.") BigDecimal costo,
		@Size(max = 255, message = "La descripción admite máximo 255 caracteres.") String descripcion) {
}

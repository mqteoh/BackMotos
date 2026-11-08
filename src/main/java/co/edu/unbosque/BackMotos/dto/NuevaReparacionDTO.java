package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Datos para registrar o corregir una reparación ya hecha.
 *
 * @param placa       moto reparada; debe haber sido vendida por el concesionario
 * @param fecha       día en que se hizo
 * @param descripcion trabajo realizado
 * @param costo       mano de obra, sin repuestos
 * @param idTecnicos  técnicos que participaron; el técnico que registra se agrega solo
 * @param repuestos   repuestos usados
 */
public record NuevaReparacionDTO(
		@NotBlank(message = "La placa es obligatoria.") String placa,
		@NotNull(message = "La fecha es obligatoria.") @PastOrPresent(message = "La fecha no puede ser futura.") LocalDate fecha,
		@NotBlank(message = "La descripción es obligatoria.") @Size(max = 255, message = "La descripción admite máximo 255 caracteres.") String descripcion,
		@NotNull(message = "El costo de la mano de obra es obligatorio.") @PositiveOrZero(message = "El costo no puede ser negativo.") @Digits(integer = 10, fraction = 2, message = "El costo admite hasta 10 enteros y 2 decimales.") BigDecimal costo,
		@Size(max = 5, message = "Se admiten máximo 5 técnicos.") List<Integer> idTecnicos,
		@Size(max = 20, message = "Se admiten máximo 20 repuestos.") List<@Valid UsoRepuestoDTO> repuestos) {
}

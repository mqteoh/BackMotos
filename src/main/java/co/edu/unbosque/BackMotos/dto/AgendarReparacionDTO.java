package co.edu.unbosque.BackMotos.dto;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AgendarReparacionDTO(
		@NotBlank(message = "La placa es obligatoria.") @Pattern(regexp = "^[A-Za-z]{3}[0-9]{2}[A-Za-z]$", message = "La placa debe tener el formato AAA00A.") String placa,
		@NotNull(message = "La sede es obligatoria.") Integer idSucursal,
		@NotNull(message = "La fecha es obligatoria.") @Future(message = "La fecha debe ser posterior al momento actual.") LocalDateTime fechaAgendada,
		@NotBlank(message = "Describa la falla o el servicio que necesita.") @Size(max = 255, message = "La descripción admite máximo 255 caracteres.") String descripcion,
		@PositiveOrZero(message = "El kilometraje no puede ser negativo.") Integer kilometrajeIngreso,
		@Valid MotoExterna motoExterna) {

	public record MotoExterna(@NotNull(message = "La marca de la moto es obligatoria.") Integer idMarca,
			@NotNull(message = "El modelo de la moto es obligatorio.") Integer nroModelo,
			@NotNull(message = "El año de la moto es obligatorio.") @Min(value = 1980, message = "El año mínimo es 1980.") @Max(value = 2030, message = "El año máximo es 2030.") Integer anio,
			@NotBlank(message = "El color es obligatorio.") @Size(max = 30, message = "El color admite máximo 30 caracteres.") String color) {
	}
}

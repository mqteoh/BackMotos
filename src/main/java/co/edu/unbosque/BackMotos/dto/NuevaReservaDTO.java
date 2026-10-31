package co.edu.unbosque.BackMotos.dto;

import jakarta.validation.constraints.NotBlank;

public record NuevaReservaDTO(@NotBlank(message = "La placa es obligatoria.") String placa) {
}

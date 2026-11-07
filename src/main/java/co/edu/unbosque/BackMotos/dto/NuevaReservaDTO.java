package co.edu.unbosque.BackMotos.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Placa de la moto que el cliente quiere reservar.
 *
 * @param placa placa de la moto
 */
public record NuevaReservaDTO(@NotBlank(message = "La placa es obligatoria.") String placa) {
}

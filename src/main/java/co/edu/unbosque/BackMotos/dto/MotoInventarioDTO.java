package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Moto sin vender de la sede del vendedor, con su reserva activa si la tiene.
 *
 * @param placa placa de la moto
 * @param marca nombre de la marca
 * @param modelo nombre del modelo
 * @param tipoMoto tipo de moto
 * @param anio año del modelo de la moto
 * @param color color de la moto
 * @param precio precio de lista
 * @param idSucursal identificador de la sede
 * @param sucursal nombre de la sede
 * @param reservada true si la moto tiene una reserva activa
 * @param idReserva identificador de la reserva
 * @param idClienteReserva identificador del cliente que tiene la reserva; null si no hay reserva
 * @param clienteReserva nombre del cliente que tiene la reserva; null si no hay reserva
 * @param vencimientoReserva fecha en que vence la reserva; null si no hay reserva
 */
public record MotoInventarioDTO(String placa, String marca, String modelo, String tipoMoto, Integer anio,
		String color, BigDecimal precio, Integer idSucursal, String sucursal, boolean reservada, Integer idReserva,
		Integer idClienteReserva, String clienteReserva, LocalDate vencimientoReserva) {
}

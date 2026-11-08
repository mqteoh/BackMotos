package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Reserva;

/**
 * Reserva con la moto y el cliente.
 *
 * @param idReserva identificador de la reserva
 * @param fechaReserva fecha en que se hizo la reserva
 * @param fechaVencimiento fecha en que vence la reserva
 * @param estado ACTIVA, VENCIDA, CANCELADA o CONCRETADA
 * @param placa placa de la moto
 * @param marca nombre de la marca
 * @param modelo nombre del modelo
 * @param precio precio de lista de la moto
 * @param idSucursal identificador de la sede
 * @param sucursal nombre de la sede
 * @param idCliente identificador del cliente
 * @param cliente nombre completo del cliente
 */
public record ReservaDTO(Integer idReserva, LocalDateTime fechaReserva, LocalDate fechaVencimiento,
		EstadoReserva estado, String placa, String marca, String modelo, BigDecimal precio, Integer idSucursal,
		String sucursal, Integer idCliente, String cliente) {

	/**
	 * Arma el DTO desde la entidad.
	 *
	 * @param reserva reserva que se convierte
	 * @return el DTO de la reserva
	 */
	public static ReservaDTO de(Reserva reserva) {
		Moto moto = reserva.getMoto();
		return new ReservaDTO(reserva.getIdReserva(), reserva.getFechaReserva(), reserva.getFechaVencimiento(),
				reserva.getEstado(), moto.getPlaca(), moto.getModelo().getMarca().getNombre(),
				moto.getModelo().getNombre(), moto.getPrecio(), moto.getSucursal().getIdSucursal(),
				moto.getSucursal().getNombre(), reserva.getCliente().getIdCliente(),
				reserva.getCliente().getNombreCompleto());
	}
}

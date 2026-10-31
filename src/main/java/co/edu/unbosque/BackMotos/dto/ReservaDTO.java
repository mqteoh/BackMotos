package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Reserva;

public record ReservaDTO(Integer idReserva, LocalDateTime fechaReserva, LocalDate fechaVencimiento,
		EstadoReserva estado, String placa, String marca, String modelo, BigDecimal precio, Integer idSucursal,
		String sucursal, Integer idCliente, String cliente) {

	public static ReservaDTO de(Reserva reserva) {
		Moto moto = reserva.getMoto();
		return new ReservaDTO(reserva.getIdReserva(), reserva.getFechaReserva(), reserva.getFechaVencimiento(),
				reserva.getEstado(), moto.getPlaca(), moto.getModelo().getMarca().getNombre(),
				moto.getModelo().getNombre(), moto.getPrecio(), moto.getSucursal().getIdSucursal(),
				moto.getSucursal().getNombre(), reserva.getCliente().getIdCliente(),
				reserva.getCliente().getNombreCompleto());
	}
}

package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MotoInventarioDTO(String placa, String marca, String modelo, String tipoMoto, Integer anio,
		String color, BigDecimal precio, Integer idSucursal, String sucursal, boolean reservada, Integer idReserva,
		Integer idClienteReserva, String clienteReserva, LocalDate vencimientoReserva) {
}

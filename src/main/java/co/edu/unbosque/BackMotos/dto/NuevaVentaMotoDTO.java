package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NuevaVentaMotoDTO(@NotBlank(message = "La placa es obligatoria.") String placa,
		@NotNull(message = "El cliente es obligatorio.") Integer idCliente,
		@NotNull(message = "El método de pago es obligatorio.") MetodoPago metodoPago,
		@NotNull(message = "El tipo de factura es obligatorio.") TipoFactura tipoFactura,
		@NotNull(message = "El valor total es obligatorio.") @Positive(message = "El valor total debe ser mayor que cero.") @Digits(integer = 10, fraction = 2, message = "El valor admite hasta 10 enteros y 2 decimales.") BigDecimal valorTotal,
		Integer idReserva) {
}

package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.util.List;

import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NuevaVentaAccesorioDTO(@NotNull(message = "El cliente es obligatorio.") Integer idCliente,
		@NotNull(message = "El método de pago es obligatorio.") MetodoPago metodoPago,
		@NotNull(message = "El tipo de factura es obligatorio.") TipoFactura tipoFactura,
		@NotEmpty(message = "La venta debe tener al menos un accesorio.") @Size(max = 30, message = "Una venta admite máximo 30 ítems.") List<@Valid Item> items) {

	public record Item(@NotNull(message = "El accesorio es obligatorio.") Integer idAccesorio,
			@NotNull(message = "La cantidad es obligatoria.") @Min(value = 1, message = "La cantidad mínima es 1.") @Max(value = 99, message = "La cantidad máxima es 99.") Integer cantidad,
			@Positive(message = "El precio unitario debe ser mayor que cero.") @Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.") BigDecimal precioUnitario) {
	}
}

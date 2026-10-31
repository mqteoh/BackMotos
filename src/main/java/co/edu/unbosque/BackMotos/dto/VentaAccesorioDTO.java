package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import co.edu.unbosque.BackMotos.entity.DetalleAccesorio;
import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import co.edu.unbosque.BackMotos.entity.VentaAccesorio;

public record VentaAccesorioDTO(Integer idVentaAccesorio, LocalDateTime fechaVenta, MetodoPago metodoPago,
		TipoFactura tipoFactura, Integer idCliente, String cliente, Integer idVendedor, String vendedor,
		BigDecimal valorTotal, List<Item> items) {

	public record Item(Integer idAccesorio, String nombre, String marca, Integer cantidad, BigDecimal precioUnitario,
			BigDecimal subtotal) {

		static Item de(DetalleAccesorio d) {
			return new Item(d.getAccesorio().getIdAccesorio(), d.getAccesorio().getNombre(),
					d.getAccesorio().getMarca(), d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());
		}
	}

	public static VentaAccesorioDTO de(VentaAccesorio v) {
		return new VentaAccesorioDTO(v.getIdVentaAccesorio(), v.getFechaVenta(), v.getMetodoPago(),
				v.getTipoFactura(), v.getCliente().getIdCliente(), v.getCliente().getNombreCompleto(),
				v.getVendedor().getIdEmpleado(), v.getVendedor().getNombreCompleto(), v.getValorTotal(),
				v.getDetalles().stream().map(Item::de).toList());
	}
}

package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import co.edu.unbosque.BackMotos.entity.DetalleAccesorio;
import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import co.edu.unbosque.BackMotos.entity.VentaAccesorio;

/**
 * Venta de accesorios con sus líneas.
 *
 * @param idVentaAccesorio identificador de la venta de accesorios
 * @param fechaVenta fecha y hora de la venta
 * @param metodoPago método de pago
 * @param tipoFactura tipo de factura
 * @param idCliente identificador del cliente
 * @param cliente nombre completo del cliente
 * @param idVendedor identificador del vendedor
 * @param vendedor nombre del vendedor
 * @param valorTotal valor total de la venta
 * @param items productos de la venta
 */
public record VentaAccesorioDTO(Integer idVentaAccesorio, LocalDateTime fechaVenta, MetodoPago metodoPago,
		TipoFactura tipoFactura, Integer idCliente, String cliente, Integer idVendedor, String vendedor,
		BigDecimal valorTotal, List<Item> items) {

	/**
	 * Línea de la venta.
	 *
	 * @param idAccesorio identificador del accesorio
	 * @param nombre nombre
	 * @param marca nombre de la marca
	 * @param cantidad unidades del accesorio
	 * @param precioUnitario precio por unidad
	 * @param subtotal cantidad por precio unitario
	 */
	public record Item(Integer idAccesorio, String nombre, String marca, Integer cantidad, BigDecimal precioUnitario,
			BigDecimal subtotal) {

		/**
		 * Arma la línea desde la entidad.
		 *
		 * @param d línea de la venta
		 * @return la línea de la venta
		 */
		static Item de(DetalleAccesorio d) {
			return new Item(d.getAccesorio().getIdAccesorio(), d.getAccesorio().getNombre(),
					d.getAccesorio().getMarca(), d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());
		}
	}

	/**
	 * Arma el DTO desde la entidad.
	 *
	 * @param v venta que se convierte
	 * @return el DTO de la venta
	 */
	public static VentaAccesorioDTO de(VentaAccesorio v) {
		return new VentaAccesorioDTO(v.getIdVentaAccesorio(), v.getFechaVenta(), v.getMetodoPago(),
				v.getTipoFactura(), v.getCliente().getIdCliente(), v.getCliente().getNombreCompleto(),
				v.getVendedor().getIdEmpleado(), v.getVendedor().getNombreCompleto(), v.getValorTotal(),
				v.getDetalles().stream().map(Item::de).toList());
	}
}

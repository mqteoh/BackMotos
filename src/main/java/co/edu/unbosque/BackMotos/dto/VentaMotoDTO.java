package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import co.edu.unbosque.BackMotos.entity.VentaMoto;

/**
 * Venta de una moto con cliente, vendedor y sede.
 *
 * @param fechaVenta fecha y hora de la venta
 * @param metodoPago método de pago
 * @param tipoFactura tipo de factura
 * @param valorTotal valor total de la venta
 * @param placa placa de la moto
 * @param marca nombre de la marca
 * @param modelo nombre del modelo
 * @param idCliente identificador del cliente
 * @param cliente nombre completo del cliente
 * @param idVendedor identificador del vendedor
 * @param vendedor nombre del vendedor
 * @param sucursal nombre de la sede
 */
public record VentaMotoDTO(LocalDateTime fechaVenta, MetodoPago metodoPago,
		TipoFactura tipoFactura, BigDecimal valorTotal, String placa, String marca, String modelo, Integer idCliente,
		String cliente, Integer idVendedor, String vendedor, String sucursal) {

	/**
	 * Arma el DTO desde la entidad.
	 *
	 * @param v venta que se convierte
	 * @return el DTO de la venta
	 */
	public static VentaMotoDTO de(VentaMoto v) {
		return new VentaMotoDTO(v.getFechaVenta(), v.getMetodoPago(), v.getTipoFactura(),
				v.getValorTotal(), v.getMoto().getPlaca(), v.getMoto().getModelo().getMarca().getNombre(),
				v.getMoto().getModelo().getNombre(), v.getCliente().getIdCliente(), v.getCliente().getNombreCompleto(),
				v.getVendedor().getIdEmpleado(), v.getVendedor().getNombreCompleto(),
				v.getMoto().getSucursal().getNombre());
	}
}

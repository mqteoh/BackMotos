package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import co.edu.unbosque.BackMotos.entity.MetodoPago;
import co.edu.unbosque.BackMotos.entity.TipoFactura;
import co.edu.unbosque.BackMotos.entity.VentaMoto;

public record VentaMotoDTO(Integer idVentaMoto, LocalDateTime fechaVenta, MetodoPago metodoPago,
		TipoFactura tipoFactura, BigDecimal valorTotal, String placa, String marca, String modelo, Integer idCliente,
		String cliente, Integer idVendedor, String vendedor, String sucursal, Integer idReserva) {

	public static VentaMotoDTO de(VentaMoto v) {
		return new VentaMotoDTO(v.getIdVentaMoto(), v.getFechaVenta(), v.getMetodoPago(), v.getTipoFactura(),
				v.getValorTotal(), v.getMoto().getPlaca(), v.getMoto().getModelo().getMarca().getNombre(),
				v.getMoto().getModelo().getNombre(), v.getCliente().getIdCliente(), v.getCliente().getNombreCompleto(),
				v.getVendedor().getIdEmpleado(), v.getVendedor().getNombreCompleto(),
				v.getMoto().getSucursal().getNombre(), v.getReserva() == null ? null : v.getReserva().getIdReserva());
	}
}

package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.RepuestoReparacion;

public record ReparacionDTO(Integer idReparacion, String placa, String marca, String modelo,
		LocalDateTime fechaAgendada, LocalDateTime fechaFinalizacion, EstadoReparacion estado, String descripcion,
		Integer kilometrajeIngreso, Integer idSucursal, String sucursal, Integer idCliente, String cliente,
		Integer idTecnico, String tecnico, BigDecimal costoManoObra, BigDecimal costoRepuestos, BigDecimal costoTotal,
		List<RepuestoUsado> repuestos) {

	public record RepuestoUsado(Integer idRepuesto, String nombre, String categoria, Integer cantidad,
			BigDecimal precioUnitario, BigDecimal subtotal) {

		static RepuestoUsado de(RepuestoReparacion uso) {
			return new RepuestoUsado(uso.getRepuesto().getIdRepuesto(), uso.getRepuesto().getNombre(),
					uso.getRepuesto().getCategoria(), uso.getCantidad(), uso.getPrecioUnitario(), uso.getSubtotal());
		}
	}

	public static ReparacionDTO de(Reparacion r) {
		Moto moto = r.getMoto();
		return new ReparacionDTO(r.getIdReparacion(), moto.getPlaca(), moto.getModelo().getMarca().getNombre(),
				moto.getModelo().getNombre(), r.getFechaAgendada(), r.getFechaFinalizacion(), r.getEstado(),
				r.getDescripcion(), r.getKilometrajeIngreso(), r.getSucursal().getIdSucursal(),
				r.getSucursal().getNombre(), r.getCliente().getIdCliente(), r.getCliente().getNombreCompleto(),
				r.getTecnico() == null ? null : r.getTecnico().getIdEmpleado(),
				r.getTecnico() == null ? null : r.getTecnico().getNombreCompleto(), r.getCosto(), r.getCostoRepuestos(),
				r.getCostoTotal(), r.getRepuestos().stream().map(RepuestoUsado::de).toList());
	}
}

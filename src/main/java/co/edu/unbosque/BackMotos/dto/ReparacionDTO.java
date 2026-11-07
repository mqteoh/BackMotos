package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.Reparacion;
import co.edu.unbosque.BackMotos.entity.RepuestoReparacion;
import co.edu.unbosque.BackMotos.entity.Sucursal;
import co.edu.unbosque.BackMotos.entity.Tecnico;
import co.edu.unbosque.BackMotos.entity.VentaMoto;

/**
 * Reparación lista para mostrar: moto, dueño de la moto, técnicos, repuestos y costos.
 *
 * @param idReparacion identificador de la reparación
 * @param placa placa de la moto
 * @param marca nombre de la marca
 * @param modelo nombre del modelo
 * @param fecha fecha de la reparación
 * @param descripcion trabajo que se hizo
 * @param idSucursal identificador de la sede
 * @param sucursal nombre de la sede de los técnicos
 * @param idCliente identificador del cliente dueño de la moto
 * @param cliente nombre del cliente dueño de la moto
 * @param tecnicos técnicos que participaron
 * @param costoManoObra costo de la mano de obra
 * @param costoRepuestos suma de los repuestos usados
 * @param costoTotal mano de obra más repuestos
 * @param repuestos repuestos usados
 */
public record ReparacionDTO(Integer idReparacion, String placa, String marca, String modelo, LocalDate fecha,
		String descripcion, Integer idSucursal, String sucursal, Integer idCliente, String cliente,
		List<TecnicoParticipante> tecnicos, BigDecimal costoManoObra, BigDecimal costoRepuestos, BigDecimal costoTotal,
		List<RepuestoUsado> repuestos) {

	/**
	 * Técnico que trabajó en la reparación, con su sede.
	 *
	 * @param idEmpleado identificador del empleado
	 * @param nombre nombre completo del técnico
	 * @param idSucursal sede del técnico
	 * @param sucursal nombre de la sede del técnico
	 */
	public record TecnicoParticipante(Integer idEmpleado, String nombre, Integer idSucursal, String sucursal) {

		/**
		 * Arma el participante desde la entidad.
		 *
		 * @param tecnico técnico que se convierte
		 * @return el técnico participante
		 */
		public static TecnicoParticipante de(Tecnico tecnico) {
			return new TecnicoParticipante(tecnico.getIdEmpleado(), tecnico.getNombreCompleto(),
					tecnico.getSucursal().getIdSucursal(), tecnico.getSucursal().getNombre());
		}
	}

	/**
	 * Repuesto usado con su cantidad, precio y subtotal.
	 *
	 * @param idRepuesto identificador del repuesto
	 * @param nombre nombre del repuesto
	 * @param categoria categoría del repuesto
	 * @param cantidad unidades usadas
	 * @param precioUnitario precio por unidad
	 * @param subtotal cantidad por precio unitario
	 */
	public record RepuestoUsado(Integer idRepuesto, String nombre, String categoria, Integer cantidad,
			BigDecimal precioUnitario, BigDecimal subtotal) {

		/**
		 * Arma el repuesto usado desde la entidad.
		 *
		 * @param uso repuesto usado en la reparación
		 * @return el repuesto usado
		 */
		static RepuestoUsado de(RepuestoReparacion uso) {
			return new RepuestoUsado(uso.getRepuesto().getIdRepuesto(), uso.getRepuesto().getNombre(),
					uso.getRepuesto().getCategoria(), uso.getCantidad(), uso.getPrecioUnitario(), uso.getSubtotal());
		}
	}

	/**
	 * Arma el DTO de una reparación.
	 *
	 * @param r     reparación con sus técnicos y repuestos
	 * @param venta venta de la moto, para saber quién es el cliente; puede ser null
	 * @return datos para la página
	 */
	public static ReparacionDTO de(Reparacion r, VentaMoto venta) {
		Moto moto = r.getMoto();
		Sucursal sede = r.getSucursal();
		return new ReparacionDTO(r.getIdReparacion(), moto.getPlaca(), moto.getModelo().getMarca().getNombre(),
				moto.getModelo().getNombre(), r.getFecha(), r.getDescripcion(),
				sede == null ? null : sede.getIdSucursal(), sede == null ? null : sede.getNombre(),
				venta == null ? null : venta.getCliente().getIdCliente(),
				venta == null ? null : venta.getCliente().getNombreCompleto(),
				r.getTecnicos().stream().map(TecnicoParticipante::de)
						.sorted(Comparator.comparing(TecnicoParticipante::nombre)).toList(),
				r.getCosto(), r.getCostoRepuestos(), r.getCostoTotal(),
				r.getRepuestos().stream().map(RepuestoUsado::de).toList());
	}
}

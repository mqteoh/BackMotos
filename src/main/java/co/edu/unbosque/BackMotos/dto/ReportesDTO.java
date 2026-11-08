package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

/** Respuestas de los reportes del dueño. */
public final class ReportesDTO {

	/** No se crean objetos de esta clase. */
	private ReportesDTO() {
	}

	/**
	 * Cifras del mes: motos disponibles, reservas, reparaciones y ventas.
	 *
	 * @param mes mes actual en formato año-mes
	 * @param motosDisponibles motos disponibles en todas las sedes
	 * @param reservasActivas reservas que siguen vigentes
	 * @param reparacionesMes reparaciones hechas en el mes
	 * @param motosVendidasMes motos vendidas en el mes
	 * @param ventasMotosMes valor vendido en motos en el mes
	 * @param ventasAccesoriosMes valor vendido en accesorios en el mes
	 * @param clientes número de clientes registrados
	 * @param empleados número de empleados
	 */
	public record Resumen(String mes, long motosDisponibles, long reservasActivas, long reparacionesMes,
			long motosVendidasMes, BigDecimal ventasMotosMes, BigDecimal ventasAccesoriosMes, long clientes,
			long empleados) {
	}

	/**
	 * Ventas de motos y accesorios de un vendedor.
	 *
	 * @param idEmpleado identificador del empleado
	 * @param vendedor nombre del vendedor
	 * @param sucursal nombre de la sede
	 * @param motosVendidas número de motos vendidas
	 * @param totalMotos valor vendido en motos
	 * @param totalAccesorios valor vendido en accesorios
	 * @param total motos más accesorios
	 */
	public record VentasVendedor(Integer idEmpleado, String vendedor, String sucursal, long motosVendidas,
			BigDecimal totalMotos, BigDecimal totalAccesorios, BigDecimal total) {
	}

	/**
	 * Ventas de un mes del año.
	 *
	 * @param mes número del mes, de 1 a 12
	 * @param motosVendidas número de motos vendidas
	 * @param totalMotos valor vendido en motos
	 * @param ventasAccesorios número de ventas de accesorios
	 * @param totalAccesorios valor vendido en accesorios
	 * @param total motos más accesorios
	 */
	public record VentasMes(int mes, long motosVendidas, BigDecimal totalMotos, long ventasAccesorios,
			BigDecimal totalAccesorios, BigDecimal total) {
	}

	/**
	 * Costos de reparación de una sede: mano de obra y repuestos.
	 *
	 * @param idSucursal identificador de la sede
	 * @param sucursal nombre de la sede
	 * @param reparaciones número de reparaciones
	 * @param manoObra suma de la mano de obra
	 * @param repuestos suma de los repuestos usados
	 * @param total mano de obra más repuestos
	 */
	public record CostosSede(Integer idSucursal, String sucursal, long reparaciones, BigDecimal manoObra,
			BigDecimal repuestos, BigDecimal total) {
	}

	/**
	 * Repuestos que recibió una sede y lo que costaron.
	 *
	 * @param idSucursal identificador de la sede
	 * @param sucursal nombre de la sede
	 * @param idRepuesto identificador del repuesto
	 * @param repuesto nombre del repuesto
	 * @param recibidas unidades recibidas por suministro
	 * @param usadas unidades usadas en reparaciones
	 */
	public record RepuestosSede(Integer idSucursal, String sucursal, Integer idRepuesto, String repuesto,
			long recibidas, long usadas) {
	}

	/**
	 * Motos disponibles en una sede.
	 *
	 * @param idSucursal identificador de la sede
	 * @param sucursal nombre de la sede
	 * @param ciudad ciudad
	 * @param disponibles motos disponibles en la sede
	 */
	public record DisponiblesSede(Integer idSucursal, String sucursal, String ciudad, long disponibles) {
	}
}

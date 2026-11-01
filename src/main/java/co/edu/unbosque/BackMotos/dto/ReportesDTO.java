package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

public final class ReportesDTO {

	private ReportesDTO() {
	}

	public record Resumen(String mes, long motosDisponibles, long reservasActivas, long reparacionesPendientes,
			long motosVendidasMes, BigDecimal ventasMotosMes, BigDecimal ventasAccesoriosMes, long clientes,
			long empleados) {
	}

	public record VentasVendedor(Integer idEmpleado, String vendedor, String sucursal, long motosVendidas,
			BigDecimal totalMotos, BigDecimal totalAccesorios, BigDecimal total) {
	}

	public record VentasMes(int mes, long motosVendidas, BigDecimal totalMotos, long ventasAccesorios,
			BigDecimal totalAccesorios, BigDecimal total) {
	}

	public record CostosSede(Integer idSucursal, String sucursal, long finalizadas, BigDecimal manoObra,
			BigDecimal repuestos, BigDecimal total) {
	}

	public record RepuestosSede(Integer idSucursal, String sucursal, Integer idRepuesto, String repuesto,
			long recibidas, long usadas) {
	}

	public record DisponiblesSede(Integer idSucursal, String sucursal, String ciudad, long disponibles) {
	}
}

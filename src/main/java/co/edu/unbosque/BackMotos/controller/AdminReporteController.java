package co.edu.unbosque.BackMotos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.ReportesDTO.CostosSede;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.DisponiblesSede;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.RepuestosSede;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.Resumen;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.VentasMes;
import co.edu.unbosque.BackMotos.dto.ReportesDTO.VentasVendedor;
import co.edu.unbosque.BackMotos.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/** Reportes de gestión del dueño. */
@RestController
@RequestMapping("/api/admin/reportes")
@Tag(name = "Dueño: reportes", description = "Consultas de gestión calculadas con las vistas de la base")
@SecurityRequirement(name = "bearer")
public class AdminReporteController {

	/** Servicio de los reportes. */
	private final ReporteService reporteService;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param reporteService servicio de los reportes
	 */
	public AdminReporteController(ReporteService reporteService) {
		this.reporteService = reporteService;
	}

	/**
	 * Cifras del mes: motos disponibles, reservas activas, reparaciones y ventas.
	 *
	 * @return cifras del mes: motos disponibles, reservas activas, reparaciones y ventas
	 */
	@GetMapping("/resumen")
	@Operation(summary = "Cifras del mes: motos disponibles, reservas activas, reparaciones y ventas")
	public Resumen resumen() {
		return reporteService.resumen();
	}

	/**
	 * Motos vendidas y total vendido por cada vendedor.
	 *
	 * @return motos vendidas y total vendido por cada vendedor
	 */
	@GetMapping("/ventas-por-vendedor")
	@Operation(summary = "Motos vendidas y total vendido por cada vendedor")
	public List<VentasVendedor> ventasPorVendedor() {
		return reporteService.ventasPorVendedor();
	}

	/**
	 * Ventas de motos y accesorios de cada mes del año.
	 *
	 * @param anio año; null para el año actual
	 * @return ventas de motos y accesorios de cada mes del año
	 */
	@GetMapping("/ventas-por-mes")
	@Operation(summary = "Ventas de motos y accesorios de cada mes del año")
	public List<VentasMes> ventasPorMes(@RequestParam(required = false) Integer anio) {
		return reporteService.ventasPorMes(anio);
	}

	/**
	 * Reparaciones y su costo por sede.
	 *
	 * @return reparaciones y su costo por sede
	 */
	@GetMapping("/costos-reparacion")
	@Operation(summary = "Reparaciones y su costo por sede")
	public List<CostosSede> costosReparacion() {
		return reporteService.costosReparacion();
	}

	/**
	 * Unidades de cada repuesto recibidas frente a las usadas, por sede.
	 *
	 * @param idSucursal identificador de la sede; null para no filtrar
	 * @return unidades de cada repuesto recibidas frente a las usadas, por sede
	 */
	@GetMapping("/repuestos-por-sucursal")
	@Operation(summary = "Unidades de cada repuesto recibidas frente a las usadas, por sede")
	public List<RepuestosSede> repuestosPorSucursal(@RequestParam(required = false) Integer idSucursal) {
		return reporteService.repuestosPorSucursal(idSucursal);
	}

	/**
	 * Motos disponibles en cada sede.
	 *
	 * @return motos disponibles en cada sede
	 */
	@GetMapping("/disponibles-por-sede")
	@Operation(summary = "Motos disponibles en cada sede")
	public List<DisponiblesSede> disponiblesPorSede() {
		return reporteService.disponiblesPorSede();
	}
}

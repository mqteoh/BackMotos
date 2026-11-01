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

@RestController
@RequestMapping("/api/admin/reportes")
@Tag(name = "Dueño: reportes", description = "Consultas de gestión calculadas con las vistas de la base")
@SecurityRequirement(name = "bearer")
public class AdminReporteController {

	private final ReporteService reporteService;

	public AdminReporteController(ReporteService reporteService) {
		this.reporteService = reporteService;
	}

	@GetMapping("/resumen")
	@Operation(summary = "Cifras actuales: motos disponibles, reservas activas, servicios pendientes y ventas del mes")
	public Resumen resumen() {
		return reporteService.resumen();
	}

	@GetMapping("/ventas-por-vendedor")
	@Operation(summary = "Motos vendidas y total vendido por cada vendedor")
	public List<VentasVendedor> ventasPorVendedor() {
		return reporteService.ventasPorVendedor();
	}

	@GetMapping("/ventas-por-mes")
	@Operation(summary = "Ventas de motos y accesorios de cada mes del año")
	public List<VentasMes> ventasPorMes(@RequestParam(required = false) Integer anio) {
		return reporteService.ventasPorMes(anio);
	}

	@GetMapping("/costos-reparacion")
	@Operation(summary = "Servicios finalizados y su costo por sede")
	public List<CostosSede> costosReparacion() {
		return reporteService.costosReparacion();
	}

	@GetMapping("/repuestos-por-sucursal")
	@Operation(summary = "Unidades de cada repuesto recibidas frente a las usadas, por sede")
	public List<RepuestosSede> repuestosPorSucursal(@RequestParam(required = false) Integer idSucursal) {
		return reporteService.repuestosPorSucursal(idSucursal);
	}

	@GetMapping("/disponibles-por-sede")
	@Operation(summary = "Motos disponibles en cada sede")
	public List<DisponiblesSede> disponiblesPorSede() {
		return reporteService.disponiblesPorSede();
	}
}

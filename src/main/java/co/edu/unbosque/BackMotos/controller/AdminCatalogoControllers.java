package co.edu.unbosque.BackMotos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.AccesorioDTO;
import co.edu.unbosque.BackMotos.dto.MarcaDTO;
import co.edu.unbosque.BackMotos.dto.ProveedorDTO;
import co.edu.unbosque.BackMotos.dto.RepuestoDTO;
import co.edu.unbosque.BackMotos.dto.SucursalDTO;
import co.edu.unbosque.BackMotos.service.AccesorioService;
import co.edu.unbosque.BackMotos.service.MarcaService;
import co.edu.unbosque.BackMotos.service.ProveedorService;
import co.edu.unbosque.BackMotos.service.RepuestoService;
import co.edu.unbosque.BackMotos.service.SucursalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

public final class AdminCatalogoControllers {

	private AdminCatalogoControllers() {
	}

	@RestController
	@RequestMapping("/api/admin/marcas")
	@Tag(name = "Dueño: marcas")
	@SecurityRequirement(name = "bearer")
	public static class Marcas extends CrudControllerBase<MarcaDTO, Integer> {
		public Marcas(MarcaService servicio) {
			super(servicio);
		}
	}

	@RestController
	@RequestMapping("/api/admin/sucursales")
	@Tag(name = "Dueño: sedes")
	@SecurityRequirement(name = "bearer")
	public static class Sucursales extends CrudControllerBase<SucursalDTO, Integer> {
		public Sucursales(SucursalService servicio) {
			super(servicio);
		}
	}

	@RestController
	@RequestMapping("/api/admin/accesorios")
	@Tag(name = "Dueño: accesorios")
	@SecurityRequirement(name = "bearer")
	public static class Accesorios extends CrudControllerBase<AccesorioDTO, Integer> {
		public Accesorios(AccesorioService servicio) {
			super(servicio);
		}
	}

	@RestController
	@RequestMapping("/api/admin/repuestos")
	@Tag(name = "Dueño: repuestos")
	@SecurityRequirement(name = "bearer")
	public static class Repuestos extends CrudControllerBase<RepuestoDTO, Integer> {
		public Repuestos(RepuestoService servicio) {
			super(servicio);
		}
	}

	@RestController
	@RequestMapping("/api/admin/proveedores")
	@Tag(name = "Dueño: proveedores")
	@SecurityRequirement(name = "bearer")
	public static class Proveedores extends CrudControllerBase<ProveedorDTO, String> {
		public Proveedores(ProveedorService servicio) {
			super(servicio);
		}
	}
}

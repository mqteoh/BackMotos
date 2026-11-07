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

/** CRUD del dueño para las tablas de catálogo. Cada clase interna atiende una tabla. */
public final class AdminCatalogoControllers {

	/** No se crean objetos de esta clase. */
	private AdminCatalogoControllers() {
	}

	/** Rutas /api/admin/marcas. */
	@RestController
	@RequestMapping("/api/admin/marcas")
	@Tag(name = "Dueño: marcas")
	@SecurityRequirement(name = "bearer")
	public static class Marcas extends CrudControllerBase<MarcaDTO, Integer> {
		/**
		 * Recibe las dependencias que inyecta Spring.
		 *
		 * @param servicio servicio de las marcas
		 */
		public Marcas(MarcaService servicio) {
			super(servicio);
		}
	}

	/** Rutas /api/admin/sucursales. */
	@RestController
	@RequestMapping("/api/admin/sucursales")
	@Tag(name = "Dueño: sedes")
	@SecurityRequirement(name = "bearer")
	public static class Sucursales extends CrudControllerBase<SucursalDTO, Integer> {
		/**
		 * Recibe las dependencias que inyecta Spring.
		 *
		 * @param servicio servicio de las sedes
		 */
		public Sucursales(SucursalService servicio) {
			super(servicio);
		}
	}

	/** Rutas /api/admin/accesorios. */
	@RestController
	@RequestMapping("/api/admin/accesorios")
	@Tag(name = "Dueño: accesorios")
	@SecurityRequirement(name = "bearer")
	public static class Accesorios extends CrudControllerBase<AccesorioDTO, Integer> {
		/**
		 * Recibe las dependencias que inyecta Spring.
		 *
		 * @param servicio servicio de los accesorios
		 */
		public Accesorios(AccesorioService servicio) {
			super(servicio);
		}
	}

	/** Rutas /api/admin/repuestos. */
	@RestController
	@RequestMapping("/api/admin/repuestos")
	@Tag(name = "Dueño: repuestos")
	@SecurityRequirement(name = "bearer")
	public static class Repuestos extends CrudControllerBase<RepuestoDTO, Integer> {
		/**
		 * Recibe las dependencias que inyecta Spring.
		 *
		 * @param servicio servicio de los repuestos
		 */
		public Repuestos(RepuestoService servicio) {
			super(servicio);
		}
	}

	/** Rutas /api/admin/proveedores; la llave es el NIT. */
	@RestController
	@RequestMapping("/api/admin/proveedores")
	@Tag(name = "Dueño: proveedores")
	@SecurityRequirement(name = "bearer")
	public static class Proveedores extends CrudControllerBase<ProveedorDTO, String> {
		/**
		 * Recibe las dependencias que inyecta Spring.
		 *
		 * @param servicio servicio que hace las operaciones
		 */
		public Proveedores(ProveedorService servicio) {
			super(servicio);
		}
	}
}

package co.edu.unbosque.BackMotos.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.AccesorioDTO;
import co.edu.unbosque.BackMotos.dto.FiltrosCatalogoDTO;
import co.edu.unbosque.BackMotos.dto.MarcaDTO;
import co.edu.unbosque.BackMotos.dto.ModeloDTO;
import co.edu.unbosque.BackMotos.dto.MotoCatalogoDTO;
import co.edu.unbosque.BackMotos.dto.MotoDetalleDTO;
import co.edu.unbosque.BackMotos.dto.OrdenCatalogo;
import co.edu.unbosque.BackMotos.dto.PaginaDTO;
import co.edu.unbosque.BackMotos.dto.SucursalDTO;
import co.edu.unbosque.BackMotos.service.AccesorioService;
import co.edu.unbosque.BackMotos.service.CatalogoService;
import co.edu.unbosque.BackMotos.service.CatalogoService.FiltroMotos;
import co.edu.unbosque.BackMotos.service.MarcaService;
import co.edu.unbosque.BackMotos.service.ModeloService;
import co.edu.unbosque.BackMotos.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/** Rutas públicas: catálogo, marcas, modelos, accesorios y sedes. */
@RestController
@RequestMapping("/api/publico")
@Tag(name = "Catálogo público", description = "Consultas sin sesión: motos disponibles, marcas, modelos, accesorios y sedes")
public class PublicoController {

	/** Servicio de el catálogo público. */
	private final CatalogoService catalogoService;
	/** Servicio de las marcas. */
	private final MarcaService marcaService;
	/** Servicio de los modelos. */
	private final ModeloService modeloService;
	/** Servicio de los accesorios. */
	private final AccesorioService accesorioService;
	/** Servicio de las sedes. */
	private final SucursalService sucursalService;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param catalogoService servicio del catálogo público
	 * @param marcaService servicio de las marcas
	 * @param modeloService servicio de los modelos
	 * @param accesorioService servicio de los accesorios
	 * @param sucursalService servicio de las sedes
	 */
	public PublicoController(CatalogoService catalogoService, MarcaService marcaService, ModeloService modeloService,
			AccesorioService accesorioService, SucursalService sucursalService) {
		this.catalogoService = catalogoService;
		this.marcaService = marcaService;
		this.modeloService = modeloService;
		this.accesorioService = accesorioService;
		this.sucursalService = sucursalService;
	}

	/**
	 * Motos disponibles (sin vender ni reservar) con filtros, orden y paginación.
	 *
	 * @param marca marca; null para no filtrar
	 * @param tipo tipo de moto; null para no filtrar
	 * @param idSucursal identificador de la sede; null para no filtrar
	 * @param precioMin precio mínimo; null para no filtrar
	 * @param precioMax precio máximo; null para no filtrar
	 * @param texto texto que se busca en la marca, el modelo o la placa; null para no filtrar
	 * @param pagina número de página, desde 0
	 * @param tamano cantidad de elementos por página
	 * @param orden orden del catálogo
	 * @return una página de motos disponibles
	 */
	@GetMapping("/motos")
	@Operation(summary = "Motos disponibles (sin vender ni reservar) con filtros, orden y paginación")
	public PaginaDTO<MotoCatalogoDTO> motos(@RequestParam(required = false) String marca,
			@RequestParam(required = false) String tipo, @RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) BigDecimal precioMin, @RequestParam(required = false) BigDecimal precioMax,
			@RequestParam(required = false) String texto, @RequestParam(defaultValue = "0") int pagina,
			@RequestParam(defaultValue = "12") int tamano, @RequestParam(defaultValue = "MARCA") OrdenCatalogo orden) {
		return catalogoService.buscarMotos(new FiltroMotos(marca, tipo, idSucursal, precioMin, precioMax, texto),
				pagina, tamano, orden);
	}

	/**
	 * Opciones para los filtros del catálogo con la cantidad de motos disponibles en cada una.
	 *
	 * @return opciones para los filtros del catálogo con la cantidad de motos disponibles en cada una
	 */
	@GetMapping("/motos/filtros")
	@Operation(summary = "Opciones para los filtros del catálogo con la cantidad de motos disponibles en cada una")
	public FiltrosCatalogoDTO filtros() {
		return catalogoService.filtros();
	}

	/**
	 * Ficha técnica de una moto disponible.
	 *
	 * @param placa placa de la moto
	 * @return ficha técnica de una moto disponible
	 */
	@GetMapping("/motos/{placa}")
	@Operation(summary = "Ficha técnica de una moto disponible")
	public MotoDetalleDTO moto(@PathVariable String placa) {
		return catalogoService.detalleMoto(placa);
	}

	/**
	 * Marcas de motos.
	 *
	 * @return marcas de motos
	 */
	@GetMapping("/marcas")
	@Operation(summary = "Marcas de motos")
	public List<MarcaDTO> marcas() {
		return marcaService.listar();
	}

	/**
	 * Modelos de una marca.
	 *
	 * @param idMarca identificador de la marca
	 * @return modelos de una marca
	 */
	@GetMapping("/marcas/{idMarca}/modelos")
	@Operation(summary = "Modelos de una marca")
	public List<ModeloDTO> modelos(@PathVariable Integer idMarca) {
		return modeloService.listarPorMarca(idMarca);
	}

	/**
	 * Accesorios a la venta en las sedes.
	 *
	 * @return accesorios a la venta en las sedes
	 */
	@GetMapping("/accesorios")
	@Operation(summary = "Accesorios a la venta en las sedes")
	public List<AccesorioDTO> accesorios() {
		return accesorioService.listar();
	}

	/**
	 * Sedes del concesionario.
	 *
	 * @return sedes del concesionario
	 */
	@GetMapping("/sucursales")
	@Operation(summary = "Sedes del concesionario")
	public List<SucursalDTO> sucursales() {
		return sucursalService.listar();
	}
}

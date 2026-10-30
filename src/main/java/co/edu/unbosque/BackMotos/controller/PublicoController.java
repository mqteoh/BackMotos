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

@RestController
@RequestMapping("/api/publico")
@Tag(name = "Catálogo público", description = "Consultas sin sesión: motos disponibles, marcas, modelos, accesorios y sedes")
public class PublicoController {

	private final CatalogoService catalogoService;
	private final MarcaService marcaService;
	private final ModeloService modeloService;
	private final AccesorioService accesorioService;
	private final SucursalService sucursalService;

	public PublicoController(CatalogoService catalogoService, MarcaService marcaService, ModeloService modeloService,
			AccesorioService accesorioService, SucursalService sucursalService) {
		this.catalogoService = catalogoService;
		this.marcaService = marcaService;
		this.modeloService = modeloService;
		this.accesorioService = accesorioService;
		this.sucursalService = sucursalService;
	}

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

	@GetMapping("/motos/filtros")
	@Operation(summary = "Opciones para los filtros del catálogo con la cantidad de motos disponibles en cada una")
	public FiltrosCatalogoDTO filtros() {
		return catalogoService.filtros();
	}

	@GetMapping("/motos/{placa}")
	@Operation(summary = "Ficha técnica de una moto disponible")
	public MotoDetalleDTO moto(@PathVariable String placa) {
		return catalogoService.detalleMoto(placa);
	}

	@GetMapping("/marcas")
	@Operation(summary = "Marcas de motos")
	public List<MarcaDTO> marcas() {
		return marcaService.listar();
	}

	@GetMapping("/marcas/{idMarca}/modelos")
	@Operation(summary = "Modelos de una marca")
	public List<ModeloDTO> modelos(@PathVariable Integer idMarca) {
		return modeloService.listarPorMarca(idMarca);
	}

	@GetMapping("/accesorios")
	@Operation(summary = "Accesorios a la venta en las sedes")
	public List<AccesorioDTO> accesorios() {
		return accesorioService.listar();
	}

	@GetMapping("/sucursales")
	@Operation(summary = "Sedes del concesionario")
	public List<SucursalDTO> sucursales() {
		return sucursalService.listar();
	}
}

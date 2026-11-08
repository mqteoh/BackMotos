package co.edu.unbosque.BackMotos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.NuevaReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO.TecnicoParticipante;
import co.edu.unbosque.BackMotos.dto.RepuestoDTO;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import co.edu.unbosque.BackMotos.service.RepuestoService;
import co.edu.unbosque.BackMotos.service.TallerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Panel del técnico: reparaciones de su sede, historial por placa y registro de reparaciones hechas.
 * El dueño también entra a estas rutas.
 */
@RestController
@RequestMapping("/api/tecnico")
@Tag(name = "Panel del técnico", description = "Reparaciones de la sede, historial por placa y registro de trabajos hechos")
@SecurityRequirement(name = "bearer")
public class TecnicoController {

	/** Servicio de el taller. */
	private final TallerService tallerService;
	/** Servicio de los repuestos. */
	private final RepuestoService repuestoService;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param tallerService servicio del taller
	 * @param repuestoService servicio de los repuestos
	 */
	public TecnicoController(TallerService tallerService, RepuestoService repuestoService) {
		this.tallerService = tallerService;
		this.repuestoService = repuestoService;
	}

	/**
	 * Reparaciones de la sede entre dos fechas opcionales.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param idSucursal identificador de la sede; null para no filtrar
	 * @param desde fecha inicial; null para no filtrar
	 * @param hasta fecha final; null para no filtrar
	 * @return reparaciones de la sede entre dos fechas opcionales
	 */
	@GetMapping("/reparaciones")
	@Operation(summary = "Reparaciones de la sede entre dos fechas opcionales")
	public List<ReparacionDTO> reparaciones(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		return tallerService.listar(usuario, idSucursal, desde, hasta);
	}

	/**
	 * Reparaciones en las que participó el técnico.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return reparaciones en las que participó el técnico
	 */
	@GetMapping("/mis-reparaciones")
	@Operation(summary = "Reparaciones en las que participó el técnico")
	public List<ReparacionDTO> mias(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return tallerService.mias(usuario);
	}

	/**
	 * Detalle de una reparación con técnicos, repuestos y costos.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param idReparacion identificador de la reparación
	 * @return detalle de una reparación con técnicos, repuestos y costos
	 */
	@GetMapping("/reparaciones/{idReparacion}")
	@Operation(summary = "Detalle de una reparación con técnicos, repuestos y costos")
	public ReparacionDTO detalle(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion) {
		return tallerService.detalle(usuario, idReparacion);
	}

	/**
	 * Registra una reparación ya hecha; el técnico que la registra queda como participante.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param datos datos que llegan en la petición
	 * @return la reparación registrada
	 */
	@PostMapping("/reparaciones")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra una reparación ya hecha; el técnico que la registra queda como participante")
	public ReparacionDTO registrar(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody NuevaReparacionDTO datos) {
		return tallerService.registrar(usuario, datos);
	}

	/**
	 * Corrige una reparación: datos, técnicos y repuestos.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param idReparacion identificador de la reparación
	 * @param datos datos que llegan en la petición
	 * @return la reparación con los cambios
	 */
	@PutMapping("/reparaciones/{idReparacion}")
	@Operation(summary = "Corrige una reparación: datos, técnicos y repuestos")
	public ReparacionDTO actualizar(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion, @Valid @RequestBody NuevaReparacionDTO datos) {
		return tallerService.actualizar(usuario, idReparacion, datos);
	}

	/**
	 * Historial de reparaciones de una moto.
	 *
	 * @param placa placa de la moto
	 * @return historial de reparaciones de una moto
	 */
	@GetMapping("/historial/{placa}")
	@Operation(summary = "Historial de reparaciones de una moto")
	public List<ReparacionDTO> historial(@PathVariable String placa) {
		return tallerService.historial(placa);
	}

	/**
	 * Técnicos que se pueden agregar a una reparación.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return técnicos que se pueden agregar a una reparación
	 */
	@GetMapping("/tecnicos")
	@Operation(summary = "Técnicos que se pueden agregar a una reparación")
	public List<TecnicoParticipante> tecnicos(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return tallerService.tecnicos(usuario);
	}

	/**
	 * Repuestos para registrar, con búsqueda por nombre o categoría.
	 *
	 * @param texto parte del nombre; null para no filtrar
	 * @param categoria categoría del repuesto; null para no filtrar
	 * @return repuestos para registrar, con búsqueda por nombre o categoría
	 */
	@GetMapping("/repuestos")
	@Operation(summary = "Repuestos para registrar, con búsqueda por nombre o categoría")
	public List<RepuestoDTO> repuestos(@RequestParam(required = false) String texto,
			@RequestParam(required = false) String categoria) {
		return repuestoService.buscar(texto, categoria);
	}
}

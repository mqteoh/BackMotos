package co.edu.unbosque.BackMotos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.FinalizarReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.RepuestoDTO;
import co.edu.unbosque.BackMotos.dto.UsoRepuestoDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import co.edu.unbosque.BackMotos.service.RepuestoService;
import co.edu.unbosque.BackMotos.service.TallerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tecnico")
@Tag(name = "Panel del técnico", description = "Agenda del taller de la sede, repuestos usados y cierre de servicios")
@SecurityRequirement(name = "bearer")
public class TecnicoController {

	private final TallerService tallerService;
	private final RepuestoService repuestoService;

	public TecnicoController(TallerService tallerService, RepuestoService repuestoService) {
		this.tallerService = tallerService;
		this.repuestoService = repuestoService;
	}

	@GetMapping("/agenda")
	@Operation(summary = "Servicios de la sede; por defecto los agendados y en proceso")
	public List<ReparacionDTO> agenda(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) List<EstadoReparacion> estado,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		return tallerService.agenda(usuario, idSucursal, estado, desde, hasta);
	}

	@GetMapping("/asignadas")
	@Operation(summary = "Servicios asignados al técnico")
	public List<ReparacionDTO> asignadas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return tallerService.asignadas(usuario);
	}

	@GetMapping("/reparaciones/{idReparacion}")
	@Operation(summary = "Detalle de un servicio con sus repuestos y costos")
	public ReparacionDTO detalle(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion) {
		return tallerService.detalle(usuario, idReparacion);
	}

	@GetMapping("/historial/{placa}")
	@Operation(summary = "Historial de servicios de una moto")
	public List<ReparacionDTO> historial(@PathVariable String placa) {
		return tallerService.historial(placa);
	}

	@GetMapping("/repuestos")
	@Operation(summary = "Repuestos disponibles para registrar, con búsqueda por nombre o categoría")
	public List<RepuestoDTO> repuestos(@RequestParam(required = false) String texto,
			@RequestParam(required = false) String categoria) {
		return repuestoService.buscar(texto, categoria);
	}

	@PatchMapping("/reparaciones/{idReparacion}/tomar")
	@PreAuthorize("hasRole('TECNICO')")
	@Operation(summary = "El técnico se asigna un servicio agendado y lo pasa a en proceso")
	public ReparacionDTO tomar(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion) {
		return tallerService.tomar(usuario, idReparacion);
	}

	@PostMapping("/reparaciones/{idReparacion}/repuestos")
	@PreAuthorize("hasRole('TECNICO')")
	@Operation(summary = "Registra un repuesto usado; si ya estaba, reemplaza su cantidad y precio")
	public ReparacionDTO registrarRepuesto(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion, @Valid @RequestBody UsoRepuestoDTO datos) {
		return tallerService.registrarRepuesto(usuario, idReparacion, datos);
	}

	@DeleteMapping("/reparaciones/{idReparacion}/repuestos/{idRepuesto}")
	@PreAuthorize("hasRole('TECNICO')")
	@Operation(summary = "Quita un repuesto registrado por error")
	public ReparacionDTO quitarRepuesto(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion, @PathVariable Integer idRepuesto) {
		return tallerService.quitarRepuesto(usuario, idReparacion, idRepuesto);
	}

	@PatchMapping("/reparaciones/{idReparacion}/finalizar")
	@PreAuthorize("hasRole('TECNICO')")
	@Operation(summary = "Cierra el servicio con el costo de mano de obra")
	public ReparacionDTO finalizar(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion, @Valid @RequestBody FinalizarReparacionDTO datos) {
		return tallerService.finalizar(usuario, idReparacion, datos);
	}
}

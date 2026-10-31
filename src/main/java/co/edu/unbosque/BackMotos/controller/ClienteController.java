package co.edu.unbosque.BackMotos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.AgendarReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ComprasClienteDTO;
import co.edu.unbosque.BackMotos.dto.MotoClienteDTO;
import co.edu.unbosque.BackMotos.dto.NuevaReservaDTO;
import co.edu.unbosque.BackMotos.dto.PerfilClienteDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReservaDTO;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import co.edu.unbosque.BackMotos.service.ClientePortalService;
import co.edu.unbosque.BackMotos.service.ReparacionService;
import co.edu.unbosque.BackMotos.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente")
@Tag(name = "Área del cliente", description = "Perfil, reservas, agenda del taller y compras del cliente que inició sesión")
@SecurityRequirement(name = "bearer")
public class ClienteController {

	private final ClientePortalService portalService;
	private final ReservaService reservaService;
	private final ReparacionService reparacionService;

	public ClienteController(ClientePortalService portalService, ReservaService reservaService,
			ReparacionService reparacionService) {
		this.portalService = portalService;
		this.reservaService = reservaService;
		this.reparacionService = reparacionService;
	}

	@GetMapping("/perfil")
	@Operation(summary = "Datos del cliente")
	public PerfilClienteDTO perfil(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return portalService.perfil(usuario.getId());
	}

	@PutMapping("/perfil")
	@Operation(summary = "Actualiza nombre, dirección, teléfonos y correos de contacto")
	public PerfilClienteDTO actualizarPerfil(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody PerfilClienteDTO datos) {
		return portalService.actualizarPerfil(usuario.getId(), datos);
	}

	@GetMapping("/motos")
	@Operation(summary = "Motos del cliente: las que compró y las externas que ha llevado al taller")
	public List<MotoClienteDTO> motos(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return portalService.misMotos(usuario.getId());
	}

	@GetMapping("/compras")
	@Operation(summary = "Compras de motos y de accesorios del cliente")
	public ComprasClienteDTO compras(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return portalService.compras(usuario.getId());
	}

	@GetMapping("/reservas")
	@Operation(summary = "Reservas del cliente")
	public List<ReservaDTO> reservas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return reservaService.deCliente(usuario.getId());
	}

	@PostMapping("/reservas")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Aparta una moto disponible por 7 días, sin pago")
	public ReservaDTO reservar(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody NuevaReservaDTO datos) {
		return reservaService.reservarParaCliente(usuario.getId(), datos.placa());
	}

	@PatchMapping("/reservas/{idReserva}/cancelar")
	@Operation(summary = "Cancela una reserva activa del cliente")
	public ReservaDTO cancelarReserva(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReserva) {
		return reservaService.cancelarDeCliente(usuario.getId(), idReserva);
	}

	@GetMapping("/reparaciones")
	@Operation(summary = "Servicios de taller del cliente con su estado y costo")
	public List<ReparacionDTO> reparaciones(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return reparacionService.deCliente(usuario.getId());
	}

	@PostMapping("/reparaciones")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Agenda un servicio de taller para una moto propia o una moto externa nueva")
	public ReparacionDTO agendar(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody AgendarReparacionDTO datos) {
		return reparacionService.agendar(usuario.getId(), datos);
	}

	@PatchMapping("/reparaciones/{idReparacion}/cancelar")
	@Operation(summary = "Cancela un servicio que todavía no ha empezado")
	public ReparacionDTO cancelarReparacion(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReparacion) {
		return reparacionService.cancelarDeCliente(usuario.getId(), idReparacion);
	}
}

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

/** Área del cliente con sesión iniciada. */
@RestController
@RequestMapping("/api/cliente")
@Tag(name = "Área del cliente", description = "Perfil, reservas, reparaciones y compras del cliente que inició sesión")
@SecurityRequirement(name = "bearer")
public class ClienteController {

	/** Servicio de el área del cliente. */
	private final ClientePortalService portalService;
	/** Servicio de las reservas. */
	private final ReservaService reservaService;
	/** Servicio de las reparaciones del cliente. */
	private final ReparacionService reparacionService;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param portalService servicio del área del cliente
	 * @param reservaService servicio de las reservas
	 * @param reparacionService servicio de las reparaciones del cliente
	 */
	public ClienteController(ClientePortalService portalService, ReservaService reservaService,
			ReparacionService reparacionService) {
		this.portalService = portalService;
		this.reservaService = reservaService;
		this.reparacionService = reparacionService;
	}

	/**
	 * Datos del cliente.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return datos del cliente
	 */
	@GetMapping("/perfil")
	@Operation(summary = "Datos del cliente")
	public PerfilClienteDTO perfil(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return portalService.perfil(usuario.getId());
	}

	/**
	 * Actualiza nombre, dirección, teléfonos y correos de contacto.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param datos datos que llegan en la petición
	 * @return el perfil con los cambios
	 */
	@PutMapping("/perfil")
	@Operation(summary = "Actualiza nombre, dirección, teléfonos y correos de contacto")
	public PerfilClienteDTO actualizarPerfil(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody PerfilClienteDTO datos) {
		return portalService.actualizarPerfil(usuario.getId(), datos);
	}

	/**
	 * Motos que compró el cliente.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return motos que compró el cliente
	 */
	@GetMapping("/motos")
	@Operation(summary = "Motos que compró el cliente")
	public List<MotoClienteDTO> motos(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return portalService.misMotos(usuario.getId());
	}

	/**
	 * Compras de motos y de accesorios del cliente.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return compras de motos y de accesorios del cliente
	 */
	@GetMapping("/compras")
	@Operation(summary = "Compras de motos y de accesorios del cliente")
	public ComprasClienteDTO compras(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return portalService.compras(usuario.getId());
	}

	/**
	 * Reservas del cliente.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return reservas del cliente
	 */
	@GetMapping("/reservas")
	@Operation(summary = "Reservas del cliente")
	public List<ReservaDTO> reservas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return reservaService.deCliente(usuario.getId());
	}

	/**
	 * Aparta una moto disponible por 7 días, sin pago.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param datos datos que llegan en la petición
	 * @return la reserva creada
	 */
	@PostMapping("/reservas")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Aparta una moto disponible por 7 días, sin pago")
	public ReservaDTO reservar(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody NuevaReservaDTO datos) {
		return reservaService.reservarParaCliente(usuario.getId(), datos.placa());
	}

	/**
	 * Cancela una reserva activa del cliente.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param idReserva identificador de la reserva
	 * @return la reserva cancelada
	 */
	@PatchMapping("/reservas/{idReserva}/cancelar")
	@Operation(summary = "Cancela una reserva activa del cliente")
	public ReservaDTO cancelarReserva(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@PathVariable Integer idReserva) {
		return reservaService.cancelarDeCliente(usuario.getId(), idReserva);
	}

	/**
	 * Reparaciones de las motos que compró el cliente, con su costo.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @return reparaciones de las motos que compró el cliente, con su costo
	 */
	@GetMapping("/reparaciones")
	@Operation(summary = "Reparaciones de las motos que compró el cliente, con su costo")
	public List<ReparacionDTO> reparaciones(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return reparacionService.deCliente(usuario.getId());
	}
}

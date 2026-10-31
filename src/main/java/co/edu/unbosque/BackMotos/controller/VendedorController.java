package co.edu.unbosque.BackMotos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import co.edu.unbosque.BackMotos.dto.ClienteDTO;
import co.edu.unbosque.BackMotos.dto.MotoInventarioDTO;
import co.edu.unbosque.BackMotos.dto.NuevaVentaAccesorioDTO;
import co.edu.unbosque.BackMotos.dto.NuevaVentaMotoDTO;
import co.edu.unbosque.BackMotos.dto.ReservaDTO;
import co.edu.unbosque.BackMotos.dto.VentaAccesorioDTO;
import co.edu.unbosque.BackMotos.dto.VentaMotoDTO;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import co.edu.unbosque.BackMotos.service.ClienteService;
import co.edu.unbosque.BackMotos.service.ReservaService;
import co.edu.unbosque.BackMotos.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendedor")
@Tag(name = "Panel del vendedor", description = "Clientes, reservas, inventario y ventas de la sede")
@SecurityRequirement(name = "bearer")
public class VendedorController {

	private final ClienteService clienteService;
	private final ReservaService reservaService;
	private final VentaService ventaService;

	public VendedorController(ClienteService clienteService, ReservaService reservaService, VentaService ventaService) {
		this.clienteService = clienteService;
		this.reservaService = reservaService;
		this.ventaService = ventaService;
	}

	@GetMapping("/clientes")
	@Operation(summary = "Busca clientes por documento exacto o por nombre o inicio del documento")
	public List<ClienteDTO> buscarClientes(@RequestParam(required = false) String documento,
			@RequestParam(required = false) String texto) {
		if (documento != null && !documento.isBlank()) {
			return List.of(clienteService.buscarPorDocumento(documento));
		}
		return clienteService.buscar(texto);
	}

	@GetMapping("/clientes/{idCliente}")
	@Operation(summary = "Datos de un cliente")
	public ClienteDTO cliente(@PathVariable Integer idCliente) {
		return clienteService.buscarPorId(idCliente);
	}

	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra un cliente en la sede (sin cuenta web)")
	public ClienteDTO crearCliente(@Valid @RequestBody ClienteDTO datos) {
		return clienteService.crear(datos);
	}

	@PutMapping("/clientes/{idCliente}")
	@Operation(summary = "Actualiza los datos de un cliente")
	public ClienteDTO actualizarCliente(@PathVariable Integer idCliente, @Valid @RequestBody ClienteDTO datos) {
		return clienteService.actualizar(idCliente, datos);
	}

	@GetMapping("/reservas")
	@Operation(summary = "Reservas activas de la sede, de la más próxima a vencer a la más lejana")
	public List<ReservaDTO> reservas(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@RequestParam(required = false) Integer idSucursal) {
		return reservaService.activasDeSede(usuario.getRol() == Rol.DUENO ? idSucursal : usuario.getIdSucursal());
	}

	@GetMapping("/inventario")
	@Operation(summary = "Motos de la sede sin vender, indicando cuáles están reservadas y por quién")
	public List<MotoInventarioDTO> inventario(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@RequestParam(required = false) Integer idSucursal) {
		return ventaService.inventario(usuario, idSucursal);
	}

	@PostMapping("/ventas-moto")
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('VENDEDOR')")
	@Operation(summary = "Registra la venta de una moto de la sede; si el cliente la tenía reservada, la reserva se concreta")
	public VentaMotoDTO venderMoto(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody NuevaVentaMotoDTO datos) {
		return ventaService.registrarVentaMoto(usuario, datos);
	}

	@GetMapping("/ventas-moto")
	@Operation(summary = "Ventas de motos del vendedor (el dueño ve todas)")
	public List<VentaMotoDTO> ventasMoto(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return ventaService.ventasMoto(usuario);
	}

	@PostMapping("/ventas-accesorio")
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('VENDEDOR')")
	@Operation(summary = "Registra una venta de accesorios con sus ítems; si el precio no se envía se usa el de lista")
	public VentaAccesorioDTO venderAccesorios(@AuthenticationPrincipal UsuarioAutenticado usuario,
			@Valid @RequestBody NuevaVentaAccesorioDTO datos) {
		return ventaService.registrarVentaAccesorio(usuario, datos);
	}

	@GetMapping("/ventas-accesorio")
	@Operation(summary = "Ventas de accesorios del vendedor (el dueño ve todas)")
	public List<VentaAccesorioDTO> ventasAccesorio(@AuthenticationPrincipal UsuarioAutenticado usuario) {
		return ventaService.ventasAccesorio(usuario);
	}
}

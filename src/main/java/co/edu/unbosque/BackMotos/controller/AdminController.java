package co.edu.unbosque.BackMotos.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import co.edu.unbosque.BackMotos.dto.EmpleadoAdminDTO;
import co.edu.unbosque.BackMotos.dto.MotoAdminDTO;
import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.dto.ReservaDTO;
import co.edu.unbosque.BackMotos.dto.SuministroDTO;
import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;
import co.edu.unbosque.BackMotos.entity.SuministroId;
import co.edu.unbosque.BackMotos.entity.TipoEmpleado;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;
import co.edu.unbosque.BackMotos.service.ClienteService;
import co.edu.unbosque.BackMotos.service.EmpleadoAdminService;
import co.edu.unbosque.BackMotos.service.MotoAdminService;
import co.edu.unbosque.BackMotos.service.OperacionesAdminService;
import co.edu.unbosque.BackMotos.service.SuministroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Dueño: inventario, personas y operaciones")
@SecurityRequirement(name = "bearer")
public class AdminController {

	private final MotoAdminService motoService;
	private final EmpleadoAdminService empleadoService;
	private final ClienteService clienteService;
	private final SuministroService suministroService;
	private final OperacionesAdminService operacionesService;

	public AdminController(MotoAdminService motoService, EmpleadoAdminService empleadoService,
			ClienteService clienteService, SuministroService suministroService,
			OperacionesAdminService operacionesService) {
		this.motoService = motoService;
		this.empleadoService = empleadoService;
		this.clienteService = clienteService;
		this.suministroService = suministroService;
		this.operacionesService = operacionesService;
	}

	@GetMapping("/motos")
	@Operation(summary = "Todas las motos, filtrables por sede y origen")
	public List<MotoAdminDTO> motos(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) OrigenMoto origen) {
		return motoService.listar(idSucursal, origen);
	}

	@GetMapping("/motos/{placa}")
	public MotoAdminDTO moto(@PathVariable String placa) {
		return motoService.buscar(placa);
	}

	@PostMapping("/motos")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra una moto de inventario o externa")
	public MotoAdminDTO crearMoto(@Valid @RequestBody MotoAdminDTO datos) {
		return motoService.crear(datos);
	}

	@PutMapping("/motos/{placa}")
	public MotoAdminDTO actualizarMoto(@PathVariable String placa, @Valid @RequestBody MotoAdminDTO datos) {
		return motoService.actualizar(placa, datos);
	}

	@DeleteMapping("/motos/{placa}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina una moto sin ventas, reservas ni reparaciones")
	public void eliminarMoto(@PathVariable String placa) {
		motoService.eliminar(placa);
	}

	@GetMapping("/empleados")
	@Operation(summary = "Empleados, filtrables por sede y tipo")
	public List<EmpleadoAdminDTO> empleados(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) TipoEmpleado tipo) {
		return empleadoService.listar(idSucursal, tipo);
	}

	@GetMapping("/empleados/{id}")
	public EmpleadoAdminDTO empleado(@PathVariable Integer id) {
		return empleadoService.buscar(id);
	}

	@PostMapping("/empleados")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra un empleado con su subclase y su contraseña inicial")
	public EmpleadoAdminDTO crearEmpleado(@Valid @RequestBody EmpleadoAdminDTO datos) {
		return empleadoService.crear(datos);
	}

	@PutMapping("/empleados/{id}")
	@Operation(summary = "Actualiza un empleado; la contraseña solo cambia si se envía")
	public EmpleadoAdminDTO actualizarEmpleado(@PathVariable Integer id, @Valid @RequestBody EmpleadoAdminDTO datos) {
		return empleadoService.actualizar(id, datos);
	}

	@DeleteMapping("/empleados/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarEmpleado(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Integer id) {
		empleadoService.eliminar(id, usuario.getId());
	}

	@GetMapping("/clientes")
	public List<ClienteDTO> clientes() {
		return clienteService.listar();
	}

	@GetMapping("/clientes/{id}")
	public ClienteDTO cliente(@PathVariable Integer id) {
		return clienteService.buscarPorId(id);
	}

	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteDTO crearCliente(@Valid @RequestBody ClienteDTO datos) {
		return clienteService.crear(datos);
	}

	@PutMapping("/clientes/{id}")
	public ClienteDTO actualizarCliente(@PathVariable Integer id, @Valid @RequestBody ClienteDTO datos) {
		return clienteService.actualizar(id, datos);
	}

	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina un cliente sin compras, reservas ni reparaciones")
	public void eliminarCliente(@PathVariable Integer id) {
		clienteService.eliminar(id);
	}

	@GetMapping("/suministros")
	@Operation(summary = "Entregas de repuestos, filtrables por sede o proveedor")
	public List<SuministroDTO> suministros(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) String nit) {
		return suministroService.listar(idSucursal, nit);
	}

	@PostMapping("/suministros")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra una entrega de repuestos; si no se envía la fecha se usa la actual")
	public SuministroDTO registrarSuministro(@Valid @RequestBody SuministroDTO datos) {
		return suministroService.registrar(datos);
	}

	@DeleteMapping("/suministros")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina una entrega identificada por proveedor, repuesto, sede y fecha")
	public void eliminarSuministro(@RequestParam String nit, @RequestParam Integer idRepuesto,
			@RequestParam Integer idSucursal,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
		suministroService.eliminar(new SuministroId(nit, idRepuesto, idSucursal, fecha));
	}

	@GetMapping("/reservas")
	public List<ReservaDTO> reservas(@RequestParam(required = false) EstadoReserva estado) {
		return operacionesService.reservas(estado);
	}

	@DeleteMapping("/reservas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarReserva(@PathVariable Integer id) {
		operacionesService.eliminarReserva(id);
	}

	@PostMapping("/reservas/vencer")
	@Operation(summary = "Marca como vencidas las reservas activas cuya fecha ya pasó (también corre cada noche)")
	public Map<String, Integer> vencerReservas() {
		return Map.of("reservasVencidas", operacionesService.vencerReservas());
	}

	@DeleteMapping("/ventas-moto/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Anula una venta de moto; si venía de una reserva, la reserva queda cancelada")
	public void eliminarVentaMoto(@PathVariable Integer id) {
		operacionesService.eliminarVentaMoto(id);
	}

	@DeleteMapping("/ventas-accesorio/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarVentaAccesorio(@PathVariable Integer id) {
		operacionesService.eliminarVentaAccesorio(id);
	}

	@GetMapping("/reparaciones")
	public List<ReparacionDTO> reparaciones(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) List<EstadoReparacion> estado) {
		return operacionesService.reparaciones(idSucursal, estado);
	}

	@DeleteMapping("/reparaciones/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarReparacion(@PathVariable Integer id) {
		operacionesService.eliminarReparacion(id);
	}
}

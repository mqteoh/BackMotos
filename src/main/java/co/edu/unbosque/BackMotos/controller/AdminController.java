package co.edu.unbosque.BackMotos.controller;

import java.time.LocalDateTime;
import java.time.LocalDate;
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
import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.EstadoMoto;
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

/** Panel del dueño: motos, empleados, clientes, suministros, reservas, ventas y reparaciones. */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Dueño: inventario, personas y operaciones")
@SecurityRequirement(name = "bearer")
public class AdminController {

	/** Servicio de las motos del inventario. */
	private final MotoAdminService motoService;
	/** Servicio de los empleados. */
	private final EmpleadoAdminService empleadoService;
	/** Servicio de los clientes. */
	private final ClienteService clienteService;
	/** Servicio de los suministros. */
	private final SuministroService suministroService;
	/** Servicio de las reservas, ventas y reparaciones del dueño. */
	private final OperacionesAdminService operacionesService;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param motoService servicio de las motos del inventario
	 * @param empleadoService servicio de los empleados
	 * @param clienteService servicio de los clientes
	 * @param suministroService servicio de los suministros
	 * @param operacionesService servicio de las reservas, ventas y reparaciones del dueño
	 */
	public AdminController(MotoAdminService motoService, EmpleadoAdminService empleadoService,
			ClienteService clienteService, SuministroService suministroService,
			OperacionesAdminService operacionesService) {
		this.motoService = motoService;
		this.empleadoService = empleadoService;
		this.clienteService = clienteService;
		this.suministroService = suministroService;
		this.operacionesService = operacionesService;
	}

	/**
	 * Todas las motos, filtrables por sede y estado.
	 *
	 * @param idSucursal identificador de la sede; null para no filtrar
	 * @param estado estado de la moto; null para todas
	 * @return todas las motos, filtrables por sede y estado
	 */
	@GetMapping("/motos")
	@Operation(summary = "Todas las motos, filtrables por sede y estado")
	public List<MotoAdminDTO> motos(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) EstadoMoto estado) {
		return motoService.listar(idSucursal, estado);
	}

	/**
	 * Datos de una moto por placa.
	 *
	 * @param placa placa de la moto
	 * @return los datos de la moto
	 */
	@GetMapping("/motos/{placa}")
	public MotoAdminDTO moto(@PathVariable String placa) {
		return motoService.buscar(placa);
	}

	/**
	 * Registra una moto en una sede; queda disponible.
	 *
	 * @param datos datos que llegan en la petición
	 * @return la moto registrada
	 */
	@PostMapping("/motos")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra una moto en una sede; queda disponible")
	public MotoAdminDTO crearMoto(@Valid @RequestBody MotoAdminDTO datos) {
		return motoService.crear(datos);
	}

	/**
	 * Actualiza una moto; la placa no cambia.
	 *
	 * @param placa placa de la moto
	 * @param datos datos que llegan en la petición
	 * @return la moto con los cambios
	 */
	@PutMapping("/motos/{placa}")
	public MotoAdminDTO actualizarMoto(@PathVariable String placa, @Valid @RequestBody MotoAdminDTO datos) {
		return motoService.actualizar(placa, datos);
	}

	/**
	 * Elimina una moto sin ventas, reservas ni reparaciones.
	 *
	 * @param placa placa de la moto
	 */
	@DeleteMapping("/motos/{placa}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina una moto sin ventas, reservas ni reparaciones")
	public void eliminarMoto(@PathVariable String placa) {
		motoService.eliminar(placa);
	}

	/**
	 * Empleados, filtrables por sede y tipo.
	 *
	 * @param idSucursal identificador de la sede; null para no filtrar
	 * @param tipo tipo de empleado; null para todos
	 * @return empleados, filtrables por sede y tipo
	 */
	@GetMapping("/empleados")
	@Operation(summary = "Empleados, filtrables por sede y tipo")
	public List<EmpleadoAdminDTO> empleados(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) TipoEmpleado tipo) {
		return empleadoService.listar(idSucursal, tipo);
	}

	/**
	 * Datos de un empleado con el correo de su cuenta.
	 *
	 * @param id identificador del empleado
	 * @return datos de un empleado con el correo de su cuenta
	 */
	@GetMapping("/empleados/{id}")
	public EmpleadoAdminDTO empleado(@PathVariable Integer id) {
		return empleadoService.buscar(id);
	}

	/**
	 * Registra un empleado con su subclase y su contraseña inicial.
	 *
	 * @param datos datos que llegan en la petición
	 * @return el empleado registrado
	 */
	@PostMapping("/empleados")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra un empleado con su subclase y su contraseña inicial")
	public EmpleadoAdminDTO crearEmpleado(@Valid @RequestBody EmpleadoAdminDTO datos) {
		return empleadoService.crear(datos);
	}

	/**
	 * Actualiza un empleado; la contraseña solo cambia si se envía.
	 *
	 * @param id identificador del empleado
	 * @param datos datos que llegan en la petición
	 * @return el empleado con los cambios
	 */
	@PutMapping("/empleados/{id}")
	@Operation(summary = "Actualiza un empleado; la contraseña solo cambia si se envía")
	public EmpleadoAdminDTO actualizarEmpleado(@PathVariable Integer id, @Valid @RequestBody EmpleadoAdminDTO datos) {
		return empleadoService.actualizar(id, datos);
	}

	/**
	 * Elimina un empleado y su cuenta; el dueño no puede eliminarse a sí mismo.
	 *
	 * @param usuario usuario con sesión iniciada
	 * @param id identificador del empleado
	 */
	@DeleteMapping("/empleados/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarEmpleado(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable Integer id) {
		empleadoService.eliminar(id, usuario.getId());
	}

	/**
	 * Todos los clientes.
	 *
	 * @return todos los clientes
	 */
	@GetMapping("/clientes")
	public List<ClienteDTO> clientes() {
		return clienteService.listar();
	}

	/**
	 * Datos de un cliente.
	 *
	 * @param id identificador del cliente
	 * @return datos de un cliente
	 */
	@GetMapping("/clientes/{id}")
	public ClienteDTO cliente(@PathVariable Integer id) {
		return clienteService.buscarPorId(id);
	}

	/**
	 * Registra un cliente sin cuenta web.
	 *
	 * @param datos datos que llegan en la petición
	 * @return el cliente registrado
	 */
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteDTO crearCliente(@Valid @RequestBody ClienteDTO datos) {
		return clienteService.crear(datos);
	}

	/**
	 * Actualiza los datos de un cliente.
	 *
	 * @param id identificador del cliente
	 * @param datos datos que llegan en la petición
	 * @return el cliente con los cambios
	 */
	@PutMapping("/clientes/{id}")
	public ClienteDTO actualizarCliente(@PathVariable Integer id, @Valid @RequestBody ClienteDTO datos) {
		return clienteService.actualizar(id, datos);
	}

	/**
	 * Elimina un cliente sin compras ni reservas.
	 *
	 * @param id identificador del cliente
	 */
	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina un cliente sin compras ni reservas")
	public void eliminarCliente(@PathVariable Integer id) {
		clienteService.eliminar(id);
	}

	/**
	 * Entregas de repuestos, filtrables por sede o proveedor.
	 *
	 * @param idSucursal identificador de la sede; null para no filtrar
	 * @param nit el NIT del proveedor; null para no filtrar
	 * @return entregas de repuestos, filtrables por sede o proveedor
	 */
	@GetMapping("/suministros")
	@Operation(summary = "Entregas de repuestos, filtrables por sede o proveedor")
	public List<SuministroDTO> suministros(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) String nit) {
		return suministroService.listar(idSucursal, nit);
	}

	/**
	 * Registra una entrega de repuestos; si no se envía la fecha se usa la actual.
	 *
	 * @param datos datos que llegan en la petición
	 * @return la entrega registrada
	 */
	@PostMapping("/suministros")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Registra una entrega de repuestos; si no se envía la fecha se usa la actual")
	public SuministroDTO registrarSuministro(@Valid @RequestBody SuministroDTO datos) {
		return suministroService.registrar(datos);
	}

	/**
	 * Elimina una entrega identificada por proveedor, repuesto, sede y fecha.
	 *
	 * @param nit el NIT del proveedor
	 * @param idRepuesto identificador del repuesto
	 * @param idSucursal identificador de la sede
	 * @param fecha fecha y hora de la entrega
	 */
	@DeleteMapping("/suministros")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina una entrega identificada por proveedor, repuesto, sede y fecha")
	public void eliminarSuministro(@RequestParam String nit, @RequestParam Integer idRepuesto,
			@RequestParam Integer idSucursal,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
		suministroService.eliminar(new SuministroId(nit, idRepuesto, idSucursal, fecha));
	}

	/**
	 * Reservas de un estado, o todas.
	 *
	 * @param estado estado de las reservas; null para todas
	 * @return reservas de un estado, o todas
	 */
	@GetMapping("/reservas")
	public List<ReservaDTO> reservas(@RequestParam(required = false) EstadoReserva estado) {
		return operacionesService.reservas(estado);
	}

	/**
	 * Elimina una reserva que no se haya concretado.
	 *
	 * @param id identificador de la reserva
	 */
	@DeleteMapping("/reservas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarReserva(@PathVariable Integer id) {
		operacionesService.eliminarReserva(id);
	}

	/**
	 * Marca como vencidas las reservas activas cuya fecha ya pasó (también corre cada noche).
	 *
	 * @return un mapa con la cantidad de reservas vencidas
	 */
	@PostMapping("/reservas/vencer")
	@Operation(summary = "Marca como vencidas las reservas activas cuya fecha ya pasó (también corre cada noche)")
	public Map<String, Integer> vencerReservas() {
		return Map.of("reservasVencidas", operacionesService.vencerReservas());
	}

	/**
	 * Anula la venta de una moto; la moto vuelve a estar disponible y su reserva queda cancelada.
	 *
	 * @param placa placa de la moto
	 */
	@DeleteMapping("/ventas-moto/{placa}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Anula la venta de una moto; la moto vuelve a estar disponible y su reserva queda cancelada")
	public void eliminarVentaMoto(@PathVariable String placa) {
		operacionesService.eliminarVentaMoto(placa);
	}

	/**
	 * Elimina una venta de accesorios.
	 *
	 * @param id identificador de la venta de accesorios
	 */
	@DeleteMapping("/ventas-accesorio/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarVentaAccesorio(@PathVariable Integer id) {
		operacionesService.eliminarVentaAccesorio(id);
	}

	/**
	 * Reparaciones de todas las sedes, filtrables por sede y fechas.
	 *
	 * @param idSucursal identificador de la sede; null para no filtrar
	 * @param desde fecha inicial; null para no filtrar
	 * @param hasta fecha final; null para no filtrar
	 * @return reparaciones de todas las sedes, filtrables por sede y fechas
	 */
	@GetMapping("/reparaciones")
	@Operation(summary = "Reparaciones de todas las sedes, filtrables por sede y fechas")
	public List<ReparacionDTO> reparaciones(@RequestParam(required = false) Integer idSucursal,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		return operacionesService.reparaciones(idSucursal, desde, hasta);
	}

	/**
	 * Elimina una reparación con sus técnicos y repuestos.
	 *
	 * @param id identificador de la reparación
	 */
	@DeleteMapping("/reparaciones/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarReparacion(@PathVariable Integer id) {
		operacionesService.eliminarReparacion(id);
	}
}

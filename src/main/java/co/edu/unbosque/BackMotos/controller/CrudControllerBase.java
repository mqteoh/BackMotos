package co.edu.unbosque.BackMotos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.edu.unbosque.BackMotos.service.CRUDoperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * Rutas CRUD comunes (listar, buscar, crear, actualizar y eliminar) sobre un servicio.
 *
 * @param <D>  DTO de la tabla
 * @param <ID> tipo de la llave
 */
public abstract class CrudControllerBase<D, ID> {

	/** Servicio que hace las operaciones. */
	private final CRUDoperation<D, ID> servicio;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param servicio servicio que hace las operaciones
	 */
	protected CrudControllerBase(CRUDoperation<D, ID> servicio) {
		this.servicio = servicio;
	}

	/**
	 * Lista todos los registros.
	 *
	 * @return todos los registros
	 */
	@GetMapping
	@Operation(summary = "Lista todos los registros")
	public List<D> listar() {
		return servicio.listar();
	}

	/**
	 * Consulta un registro.
	 *
	 * @param id llave del registro
	 * @return el registro encontrado
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Consulta un registro")
	public D buscar(@PathVariable ID id) {
		return servicio.buscarPorId(id);
	}

	/**
	 * Crea un registro.
	 *
	 * @param datos datos que llegan en la petición
	 * @return el registro creado
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Crea un registro")
	public D crear(@Valid @RequestBody D datos) {
		return servicio.crear(datos);
	}

	/**
	 * Reemplaza los datos de un registro.
	 *
	 * @param id llave del registro
	 * @param datos datos que llegan en la petición
	 * @return el registro con los cambios
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Reemplaza los datos de un registro")
	public D actualizar(@PathVariable ID id, @Valid @RequestBody D datos) {
		return servicio.actualizar(id, datos);
	}

	/**
	 * Elimina un registro si nada depende de él.
	 *
	 * @param id llave del registro
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina un registro si nada depende de él")
	public void eliminar(@PathVariable ID id) {
		servicio.eliminar(id);
	}
}

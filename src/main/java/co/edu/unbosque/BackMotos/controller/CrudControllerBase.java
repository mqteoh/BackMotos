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

public abstract class CrudControllerBase<D, ID> {

	private final CRUDoperation<D, ID> servicio;

	protected CrudControllerBase(CRUDoperation<D, ID> servicio) {
		this.servicio = servicio;
	}

	@GetMapping
	@Operation(summary = "Lista todos los registros")
	public List<D> listar() {
		return servicio.listar();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Consulta un registro")
	public D buscar(@PathVariable ID id) {
		return servicio.buscarPorId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Crea un registro")
	public D crear(@Valid @RequestBody D datos) {
		return servicio.crear(datos);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Reemplaza los datos de un registro")
	public D actualizar(@PathVariable ID id, @Valid @RequestBody D datos) {
		return servicio.actualizar(id, datos);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina un registro si nada depende de él")
	public void eliminar(@PathVariable ID id) {
		servicio.eliminar(id);
	}
}

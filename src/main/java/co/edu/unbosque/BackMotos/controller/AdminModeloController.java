package co.edu.unbosque.BackMotos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.BackMotos.dto.ModeloDTO;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.service.ModeloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/modelos")
@Tag(name = "Dueño: modelos", description = "El modelo se identifica por la marca y su número dentro de la marca")
@SecurityRequirement(name = "bearer")
public class AdminModeloController {

	private final ModeloService modeloService;

	public AdminModeloController(ModeloService modeloService) {
		this.modeloService = modeloService;
	}

	@GetMapping
	@Operation(summary = "Lista todos los modelos")
	public List<ModeloDTO> listar() {
		return modeloService.listar();
	}

	@GetMapping("/{idMarca}/{nroModelo}")
	@Operation(summary = "Consulta un modelo")
	public ModeloDTO buscar(@PathVariable Integer idMarca, @PathVariable Integer nroModelo) {
		return modeloService.buscarPorId(new ModeloId(idMarca, nroModelo));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Crea un modelo; el número se asigna dentro de la marca")
	public ModeloDTO crear(@Valid @RequestBody ModeloDTO datos) {
		return modeloService.crear(datos);
	}

	@PutMapping("/{idMarca}/{nroModelo}")
	@Operation(summary = "Actualiza un modelo (la marca no se puede cambiar)")
	public ModeloDTO actualizar(@PathVariable Integer idMarca, @PathVariable Integer nroModelo,
			@Valid @RequestBody ModeloDTO datos) {
		return modeloService.actualizar(new ModeloId(idMarca, nroModelo), datos);
	}

	@DeleteMapping("/{idMarca}/{nroModelo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Elimina un modelo sin motos asociadas")
	public void eliminar(@PathVariable Integer idMarca, @PathVariable Integer nroModelo) {
		modeloService.eliminar(new ModeloId(idMarca, nroModelo));
	}
}

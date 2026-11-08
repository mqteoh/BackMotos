package co.edu.unbosque.BackMotos.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.RepuestoDTO;
import co.edu.unbosque.BackMotos.entity.Repuesto;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;

/** CRUD de repuestos y búsqueda para el taller. */
@Service
public class RepuestoService extends ServicioCrudBase<Repuesto, RepuestoDTO, Integer> {

	/** Acceso a la base para los repuestos. */
	private final RepuestoRepository repuestoRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param repuestoRepository acceso a la base para los repuestos
	 * @param mapper convierte entidades en DTO y al revés
	 */
	public RepuestoService(RepuestoRepository repuestoRepository, ModelMapper mapper) {
		super(repuestoRepository, mapper, Repuesto.class, RepuestoDTO.class, "el repuesto", Sort.by("nombre"));
		this.repuestoRepository = repuestoRepository;
	}

	/**
	 * Busca repuestos por categoría o, si no hay categoría, por parte del nombre.
	 *
	 * @param texto parte del nombre; null si se busca por categoría
	 * @param categoria categoría del repuesto; null para buscar por nombre
	 * @return los repuestos encontrados
	 */
	@Transactional(readOnly = true)
	public List<RepuestoDTO> buscar(String texto, String categoria) {
		List<Repuesto> repuestos;
		if (categoria != null && !categoria.isBlank()) {
			repuestos = repuestoRepository.findByCategoriaIgnoreCaseOrderByNombre(categoria.trim());
		} else if (texto != null && !texto.isBlank()) {
			repuestos = repuestoRepository.findByNombreContainingIgnoreCaseOrderByNombre(texto.trim());
		} else {
			repuestos = repuestoRepository.findAll(Sort.by("nombre"));
		}
		return repuestos.stream().map(this::aDto).toList();
	}

	@Override
	protected void prepararNueva(Repuesto entidad) {
		entidad.setIdRepuesto(null);
	}

	@Override
	protected void asignarId(Repuesto entidad, Integer id) {
		entidad.setIdRepuesto(id);
	}
}

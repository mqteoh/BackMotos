package co.edu.unbosque.BackMotos.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.RepuestoDTO;
import co.edu.unbosque.BackMotos.entity.Repuesto;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;

@Service
public class RepuestoService extends ServicioCrudBase<Repuesto, RepuestoDTO, Integer> {

	private final RepuestoRepository repuestoRepository;

	public RepuestoService(RepuestoRepository repuestoRepository, ModelMapper mapper) {
		super(repuestoRepository, mapper, Repuesto.class, RepuestoDTO.class, "el repuesto", Sort.by("nombre"));
		this.repuestoRepository = repuestoRepository;
	}

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

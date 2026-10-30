package co.edu.unbosque.BackMotos.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.unbosque.BackMotos.dto.RepuestoDTO;
import co.edu.unbosque.BackMotos.entity.Repuesto;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;

@Service
public class RepuestoService extends ServicioCrudBase<Repuesto, RepuestoDTO, Integer> {

	public RepuestoService(RepuestoRepository repuestoRepository, ModelMapper mapper) {
		super(repuestoRepository, mapper, Repuesto.class, RepuestoDTO.class, "el repuesto", Sort.by("nombre"));
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

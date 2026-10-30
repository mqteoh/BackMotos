package co.edu.unbosque.BackMotos.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.unbosque.BackMotos.dto.AccesorioDTO;
import co.edu.unbosque.BackMotos.entity.Accesorio;
import co.edu.unbosque.BackMotos.repository.AccesorioRepository;

@Service
public class AccesorioService extends ServicioCrudBase<Accesorio, AccesorioDTO, Integer> {

	public AccesorioService(AccesorioRepository accesorioRepository, ModelMapper mapper) {
		super(accesorioRepository, mapper, Accesorio.class, AccesorioDTO.class, "el accesorio", Sort.by("nombre"));
	}

	@Override
	protected void prepararNueva(Accesorio entidad) {
		entidad.setIdAccesorio(null);
	}

	@Override
	protected void asignarId(Accesorio entidad, Integer id) {
		entidad.setIdAccesorio(id);
	}
}

package co.edu.unbosque.BackMotos.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.unbosque.BackMotos.dto.MarcaDTO;
import co.edu.unbosque.BackMotos.entity.Marca;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.MarcaRepository;

@Service
public class MarcaService extends ServicioCrudBase<Marca, MarcaDTO, Integer> {

	private final MarcaRepository marcaRepository;

	public MarcaService(MarcaRepository marcaRepository, ModelMapper mapper) {
		super(marcaRepository, mapper, Marca.class, MarcaDTO.class, "la marca", Sort.by("nombre"));
		this.marcaRepository = marcaRepository;
	}

	@Override
	protected void validarNuevo(MarcaDTO dto) {
		if (marcaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
			throw new ConflictException("Ya existe una marca con el nombre " + dto.getNombre() + ".");
		}
	}

	@Override
	protected void validarCambio(Marca actual, MarcaDTO dto) {
		if (!actual.getNombre().equalsIgnoreCase(dto.getNombre())) {
			validarNuevo(dto);
		}
	}

	@Override
	protected void prepararNueva(Marca entidad) {
		entidad.setIdMarca(null);
	}

	@Override
	protected void asignarId(Marca entidad, Integer id) {
		entidad.setIdMarca(id);
	}
}

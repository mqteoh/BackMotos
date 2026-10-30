package co.edu.unbosque.BackMotos.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.unbosque.BackMotos.dto.SucursalDTO;
import co.edu.unbosque.BackMotos.entity.Sucursal;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;

@Service
public class SucursalService extends ServicioCrudBase<Sucursal, SucursalDTO, Integer> {

	private final SucursalRepository sucursalRepository;

	public SucursalService(SucursalRepository sucursalRepository, ModelMapper mapper) {
		super(sucursalRepository, mapper, Sucursal.class, SucursalDTO.class, "la sede", Sort.by("nombre"));
		this.sucursalRepository = sucursalRepository;
	}

	@Override
	protected void validarNuevo(SucursalDTO dto) {
		if (sucursalRepository.existsByNombreIgnoreCase(dto.getNombre())) {
			throw new ConflictException("Ya existe una sede con el nombre " + dto.getNombre() + ".");
		}
	}

	@Override
	protected void validarCambio(Sucursal actual, SucursalDTO dto) {
		if (!actual.getNombre().equalsIgnoreCase(dto.getNombre())) {
			validarNuevo(dto);
		}
	}

	@Override
	protected void prepararNueva(Sucursal entidad) {
		entidad.setIdSucursal(null);
	}

	@Override
	protected void asignarId(Sucursal entidad, Integer id) {
		entidad.setIdSucursal(id);
	}
}

package co.edu.unbosque.BackMotos.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.unbosque.BackMotos.dto.ProveedorDTO;
import co.edu.unbosque.BackMotos.entity.Proveedor;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.ProveedorRepository;

@Service
public class ProveedorService extends ServicioCrudBase<Proveedor, ProveedorDTO, String> {

	private final ProveedorRepository proveedorRepository;

	public ProveedorService(ProveedorRepository proveedorRepository, ModelMapper mapper) {
		super(proveedorRepository, mapper, Proveedor.class, ProveedorDTO.class, "el proveedor", Sort.by("nombre"));
		this.proveedorRepository = proveedorRepository;
	}

	@Override
	protected void validarNuevo(ProveedorDTO dto) {
		if (proveedorRepository.existsById(dto.getNit())) {
			throw new ConflictException("Ya existe un proveedor con el NIT " + dto.getNit() + ".");
		}
		validarNombre(dto.getNombre());
	}

	@Override
	protected void validarCambio(Proveedor actual, ProveedorDTO dto) {
		if (!actual.getNombre().equalsIgnoreCase(dto.getNombre())) {
			validarNombre(dto.getNombre());
		}
	}

	@Override
	protected void prepararNueva(Proveedor entidad) {
	}

	@Override
	protected void asignarId(Proveedor entidad, String id) {
		entidad.setNit(id);
	}

	private void validarNombre(String nombre) {
		if (proveedorRepository.existsByNombreIgnoreCase(nombre)) {
			throw new ConflictException("Ya existe un proveedor con el nombre " + nombre + ".");
		}
	}
}

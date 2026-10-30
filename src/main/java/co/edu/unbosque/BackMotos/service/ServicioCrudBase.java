package co.edu.unbosque.BackMotos.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;

public abstract class ServicioCrudBase<E, D, ID> implements CRUDoperation<D, ID> {

	protected final JpaRepository<E, ID> repositorio;
	protected final ModelMapper mapper;
	private final Class<E> claseEntidad;
	private final Class<D> claseDto;
	private final String recurso;
	private final Sort orden;

	protected ServicioCrudBase(JpaRepository<E, ID> repositorio, ModelMapper mapper, Class<E> claseEntidad,
			Class<D> claseDto, String recurso, Sort orden) {
		this.repositorio = repositorio;
		this.mapper = mapper;
		this.claseEntidad = claseEntidad;
		this.claseDto = claseDto;
		this.recurso = recurso;
		this.orden = orden;
	}

	@Override
	@Transactional
	public D crear(D dto) {
		validarNuevo(dto);
		E entidad = mapper.map(dto, claseEntidad);
		prepararNueva(entidad);
		return aDto(repositorio.saveAndFlush(entidad));
	}

	@Override
	@Transactional(readOnly = true)
	public List<D> listar() {
		return repositorio.findAll(orden).stream().map(this::aDto).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public D buscarPorId(ID id) {
		return aDto(obtener(id));
	}

	@Override
	@Transactional
	public D actualizar(ID id, D dto) {
		E entidad = obtener(id);
		validarCambio(entidad, dto);
		mapper.map(dto, entidad);
		asignarId(entidad, id);
		return aDto(repositorio.saveAndFlush(entidad));
	}

	@Override
	@Transactional
	public void eliminar(ID id) {
		repositorio.delete(obtener(id));
		repositorio.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public long contar() {
		return repositorio.count();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existe(ID id) {
		return repositorio.existsById(id);
	}

	protected E obtener(ID id) {
		return repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe " + recurso + " con id " + id + "."));
	}

	protected D aDto(E entidad) {
		return mapper.map(entidad, claseDto);
	}

	protected void validarNuevo(D dto) {
	}

	protected void validarCambio(E actual, D dto) {
	}

	protected abstract void prepararNueva(E entidad);

	protected abstract void asignarId(E entidad, ID id);
}

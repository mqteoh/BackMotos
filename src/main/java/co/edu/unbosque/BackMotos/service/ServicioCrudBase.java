package co.edu.unbosque.BackMotos.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;

/**
 * CRUD común para las tablas de catálogo. Cada servicio indica cómo copiar los datos.
 *
 * @param <E>  entidad
 * @param <D>  DTO
 * @param <ID> tipo de la llave
 */
public abstract class ServicioCrudBase<E, D, ID> implements CRUDoperation<D, ID> {

	/** Repositorio de la entidad. */
	protected final JpaRepository<E, ID> repositorio;
	/** Convierte entidades en DTO y al revés. */
	protected final ModelMapper mapper;
	/** Clase de la entidad. */
	private final Class<E> claseEntidad;
	/** Clase del DTO. */
	private final Class<D> claseDto;
	/** Nombre del registro para los mensajes de error. */
	private final String recurso;
	/** Orden en que se listan los registros. */
	private final Sort orden;

	/**
	 * Recibe lo que necesita el CRUD de la entidad.
	 *
	 * @param repositorio repositorio de la entidad
	 * @param mapper convierte entidades en DTO y al revés
	 * @param claseEntidad clase de la entidad
	 * @param claseDto clase del DTO
	 * @param recurso nombre del registro para los mensajes de error
	 * @param orden orden en que se listan los registros
	 */
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

	/**
	 * Busca la entidad; si no existe lanza ResourceNotFoundException.
	 *
	 * @param id llave del registro
	 * @return la entidad
	 */
	protected E obtener(ID id) {
		return repositorio.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe " + recurso + " con id " + id + "."));
	}

	/**
	 * Convierte la entidad en DTO.
	 *
	 * @param entidad entidad que se convierte
	 * @return el DTO
	 */
	protected D aDto(E entidad) {
		return mapper.map(entidad, claseDto);
	}

	/**
	 * Revisa los datos antes de crear; las subclases la cambian si necesitan reglas propias.
	 *
	 * @param dto datos del registro nuevo
	 */
	protected void validarNuevo(D dto) {
	}

	/**
	 * Revisa los datos antes de actualizar; las subclases la cambian si necesitan reglas propias.
	 *
	 * @param actual registro guardado
	 * @param dto datos nuevos
	 */
	protected void validarCambio(E actual, D dto) {
	}

	/**
	 * Ajusta la entidad nueva antes de guardarla, por ejemplo quitándole el id.
	 *
	 * @param entidad entidad nueva
	 */
	protected abstract void prepararNueva(E entidad);

	/**
	 * Pone la llave en la entidad antes de actualizarla.
	 *
	 * @param entidad entidad con los datos nuevos
	 * @param id llave del registro
	 */
	protected abstract void asignarId(E entidad, ID id);
}

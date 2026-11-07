package co.edu.unbosque.BackMotos.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ModeloDTO;
import co.edu.unbosque.BackMotos.entity.Marca;
import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.MarcaRepository;
import co.edu.unbosque.BackMotos.repository.ModeloRepository;

/** CRUD de modelos. El número del modelo se asigna solo dentro de cada marca. */
@Service
public class ModeloService extends ServicioCrudBase<Modelo, ModeloDTO, ModeloId> {

	/** Acceso a la base para los modelos. */
	private final ModeloRepository modeloRepository;
	/** Acceso a la base para las marcas. */
	private final MarcaRepository marcaRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param modeloRepository acceso a la base para los modelos
	 * @param marcaRepository acceso a la base para las marcas
	 * @param mapper convierte entidades en DTO y al revés
	 */
	public ModeloService(ModeloRepository modeloRepository, MarcaRepository marcaRepository, ModelMapper mapper) {
		super(modeloRepository, mapper, Modelo.class, ModeloDTO.class, "el modelo",
				Sort.by("id.idMarca", "id.nroModelo"));
		this.modeloRepository = modeloRepository;
		this.marcaRepository = marcaRepository;
	}

	/** Crea el modelo con el siguiente número libre de la marca. */
	@Override
	@Transactional
	public ModeloDTO crear(ModeloDTO dto) {
		Marca marca = marcaRepository.findById(dto.getIdMarca())
				.orElseThrow(() -> new ResourceNotFoundException("No existe la marca con id " + dto.getIdMarca() + "."));
		validarNombre(dto.getIdMarca(), dto.getNombre());
		Modelo modelo = new Modelo();
		modelo.setId(new ModeloId(marca.getIdMarca(), modeloRepository.siguienteNroModelo(marca.getIdMarca())));
		modelo.setMarca(marca);
		copiarDatos(dto, modelo);
		return aDto(modeloRepository.saveAndFlush(modelo));
	}

	/** Cambia los datos del modelo; la marca y el número no cambian. */
	@Override
	@Transactional
	public ModeloDTO actualizar(ModeloId id, ModeloDTO dto) {
		Modelo modelo = obtener(id);
		if (dto.getIdMarca() != null && !dto.getIdMarca().equals(id.getIdMarca())) {
			throw new BadRequestException("La marca de un modelo no se puede cambiar.");
		}
		if (!modelo.getNombre().equalsIgnoreCase(dto.getNombre())) {
			validarNombre(id.getIdMarca(), dto.getNombre());
		}
		copiarDatos(dto, modelo);
		return aDto(modeloRepository.saveAndFlush(modelo));
	}

	/**
	 * Modelos de una marca.
	 *
	 * @param idMarca identificador de la marca
	 * @return modelos de una marca
	 */
	@Transactional(readOnly = true)
	public List<ModeloDTO> listarPorMarca(Integer idMarca) {
		if (!marcaRepository.existsById(idMarca)) {
			throw new ResourceNotFoundException("No existe la marca con id " + idMarca + ".");
		}
		return modeloRepository.findByIdIdMarcaOrderByIdNroModelo(idMarca).stream().map(this::aDto).toList();
	}

	@Override
	protected ModeloDTO aDto(Modelo modelo) {
		ModeloDTO dto = new ModeloDTO();
		dto.setIdMarca(modelo.getId().getIdMarca());
		dto.setNroModelo(modelo.getId().getNroModelo());
		dto.setNombreMarca(modelo.getMarca().getNombre());
		dto.setNombre(modelo.getNombre());
		dto.setCilindraje(modelo.getCilindraje());
		dto.setTipoMoto(modelo.getTipoMoto());
		dto.setPotenciaHp(modelo.getPotenciaHp());
		dto.setCapacidadTanque(modelo.getCapacidadTanque());
		return dto;
	}

	@Override
	protected void prepararNueva(Modelo entidad) {
	}

	@Override
	protected void asignarId(Modelo entidad, ModeloId id) {
		entidad.setId(id);
	}

	/**
	 * Copia los datos del DTO al modelo.
	 *
	 * @param dto datos nuevos
	 * @param modelo modelo que se modifica
	 */
	private void copiarDatos(ModeloDTO dto, Modelo modelo) {
		modelo.setNombre(dto.getNombre());
		modelo.setCilindraje(dto.getCilindraje());
		modelo.setTipoMoto(dto.getTipoMoto());
		modelo.setPotenciaHp(dto.getPotenciaHp());
		modelo.setCapacidadTanque(dto.getCapacidadTanque());
	}

	/**
	 * Lanza ConflictException si la marca ya tiene un modelo con ese nombre.
	 *
	 * @param idMarca identificador de la marca
	 * @param nombre nombre del modelo
	 */
	private void validarNombre(Integer idMarca, String nombre) {
		if (modeloRepository.existsByIdIdMarcaAndNombreIgnoreCase(idMarca, nombre)) {
			throw new ConflictException("La marca ya tiene un modelo llamado " + nombre + ".");
		}
	}
}

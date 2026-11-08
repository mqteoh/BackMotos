package co.edu.unbosque.BackMotos.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.MotoAdminDTO;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.entity.EstadoMoto;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ModeloRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;

/** Inventario de motos para el dueño. */
@Service
public class MotoAdminService {

	/** Acceso a la base para las motos. */
	private final MotoRepository motoRepository;
	/** Acceso a la base para los modelos. */
	private final ModeloRepository modeloRepository;
	/** Acceso a la base para las sedes. */
	private final SucursalRepository sucursalRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param motoRepository acceso a la base para las motos
	 * @param modeloRepository acceso a la base para los modelos
	 * @param sucursalRepository acceso a la base para las sedes
	 */
	public MotoAdminService(MotoRepository motoRepository, ModeloRepository modeloRepository,
			SucursalRepository sucursalRepository) {
		this.motoRepository = motoRepository;
		this.modeloRepository = modeloRepository;
		this.sucursalRepository = sucursalRepository;
	}

	/**
	 * Motos filtradas por sede y estado; los filtros en null no se aplican.
	 *
	 * @param idSucursal identificador de la sede
	 * @param estado estado de la moto; null para todas
	 * @return motos filtradas por sede y estado; los filtros en null no se aplican
	 */
	@Transactional(readOnly = true)
	public List<MotoAdminDTO> listar(Integer idSucursal, EstadoMoto estado) {
		return motoRepository.findAll().stream()
				.filter(m -> idSucursal == null || m.getSucursal().getIdSucursal().equals(idSucursal))
				.filter(m -> estado == null || m.getEstado() == estado).sorted(Comparator.comparing(Moto::getPlaca))
				.map(MotoAdminDTO::de).toList();
	}

	/**
	 * Busca una moto por placa.
	 *
	 * @param placa placa de la moto
	 * @return la moto encontrada
	 */
	@Transactional(readOnly = true)
	public MotoAdminDTO buscar(String placa) {
		return MotoAdminDTO.de(obtener(normalizar(placa)));
	}

	/**
	 * Registra una moto; queda DISPONIBLE.
	 *
	 * @param datos datos que llegan en la petición
	 * @return la moto registrada
	 */
	@Transactional
	public MotoAdminDTO crear(MotoAdminDTO datos) {
		String placa = normalizar(datos.placa());
		if (motoRepository.existsById(placa)) {
			throw new ConflictException("Ya existe una moto con la placa " + placa + ".");
		}
		Moto moto = new Moto();
		moto.setPlaca(placa);
		copiar(datos, moto);
		return MotoAdminDTO.de(motoRepository.saveAndFlush(moto));
	}

	/**
	 * Cambia los datos de una moto; la placa no cambia.
	 *
	 * @param placa placa de la moto
	 * @param datos datos que llegan en la petición
	 * @return la moto con los cambios
	 */
	@Transactional
	public MotoAdminDTO actualizar(String placa, MotoAdminDTO datos) {
		Moto moto = obtener(normalizar(placa));
		if (!moto.getPlaca().equals(normalizar(datos.placa()))) {
			throw new BadRequestException("La placa no se puede cambiar; registre la moto de nuevo.");
		}
		copiar(datos, moto);
		return MotoAdminDTO.de(motoRepository.saveAndFlush(moto));
	}

	/**
	 * Borra una moto; la base lo impide si tiene ventas, reservas o reparaciones.
	 *
	 * @param placa placa de la moto
	 */
	@Transactional
	public void eliminar(String placa) {
		motoRepository.delete(obtener(normalizar(placa)));
		motoRepository.flush();
	}

	/**
	 * Copia los datos del DTO a la moto, con su modelo y su sede.
	 *
	 * @param datos datos nuevos
	 * @param moto moto que se modifica
	 */
	private void copiar(MotoAdminDTO datos, Moto moto) {
		moto.setModelo(modeloRepository.findById(new ModeloId(datos.idMarca(), datos.nroModelo()))
				.orElseThrow(() -> new ResourceNotFoundException("No existe el modelo indicado.")));
		moto.setSucursal(sucursalRepository.findById(datos.idSucursal()).orElseThrow(
				() -> new ResourceNotFoundException("No existe la sede con id " + datos.idSucursal() + ".")));
		moto.setAnio(datos.anio());
		moto.setColor(datos.color().trim());
		moto.setKilometraje(datos.kilometraje());
		moto.setPrecio(datos.precio());
		moto.setFechaIngreso(datos.fechaIngreso());
	}

	/**
	 * Busca la moto; si no existe lanza ResourceNotFoundException.
	 *
	 * @param placa placa de la moto
	 * @return la moto
	 */
	private Moto obtener(String placa) {
		return motoRepository.findById(placa)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la moto con placa " + placa + "."));
	}

	/**
	 * Quita los espacios y pasa la placa a mayúsculas.
	 *
	 * @param placa placa como llega
	 * @return la placa normalizada
	 */
	private String normalizar(String placa) {
		return placa.trim().toUpperCase(Locale.ROOT);
	}
}

package co.edu.unbosque.BackMotos.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.SuministroDTO;
import co.edu.unbosque.BackMotos.entity.Suministro;
import co.edu.unbosque.BackMotos.entity.SuministroId;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ProveedorRepository;
import co.edu.unbosque.BackMotos.repository.RepuestoRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;
import co.edu.unbosque.BackMotos.repository.SuministroRepository;

/** Suministros de repuestos a las sedes. */
@Service
public class SuministroService {

	/** Acceso a la base para los suministros. */
	private final SuministroRepository suministroRepository;
	/** Acceso a la base para los proveedores. */
	private final ProveedorRepository proveedorRepository;
	/** Acceso a la base para los repuestos. */
	private final RepuestoRepository repuestoRepository;
	/** Acceso a la base para las sedes. */
	private final SucursalRepository sucursalRepository;
	/** Reloj para saber la fecha y hora actual. */
	private final Clock reloj;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param suministroRepository acceso a la base para los suministros
	 * @param proveedorRepository acceso a la base para los proveedores
	 * @param repuestoRepository acceso a la base para los repuestos
	 * @param sucursalRepository acceso a la base para las sedes
	 * @param reloj reloj para saber la fecha y hora actual
	 */
	public SuministroService(SuministroRepository suministroRepository, ProveedorRepository proveedorRepository,
			RepuestoRepository repuestoRepository, SucursalRepository sucursalRepository, Clock reloj) {
		this.suministroRepository = suministroRepository;
		this.proveedorRepository = proveedorRepository;
		this.repuestoRepository = repuestoRepository;
		this.sucursalRepository = sucursalRepository;
		this.reloj = reloj;
	}

	/**
	 * Suministros de una sede o de un proveedor, del más reciente al más antiguo.
	 *
	 * @param idSucursal identificador de la sede
	 * @param nit el NIT del proveedor
	 * @return suministros de una sede o de un proveedor, del más reciente al más antiguo
	 */
	@Transactional(readOnly = true)
	public List<SuministroDTO> listar(Integer idSucursal, String nit) {
		List<Suministro> suministros;
		if (idSucursal != null) {
			suministros = suministroRepository.findBySucursalIdSucursalOrderByIdFechaSuministroDesc(idSucursal);
		} else if (nit != null && !nit.isBlank()) {
			suministros = suministroRepository.findByProveedorNitOrderByIdFechaSuministroDesc(nit.trim());
		} else {
			suministros = suministroRepository.findAll(Sort.by(Sort.Direction.DESC, "id.fechaSuministro"));
		}
		return suministros.stream().filter(s -> nit == null || nit.isBlank() || s.getProveedor().getNit().equals(nit.trim()))
				.map(SuministroDTO::de).toList();
	}

	/**
	 * Registra una entrega; si no trae fecha usa la fecha y hora actuales.
	 *
	 * @param datos datos que llegan en la petición
	 * @return el suministro registrado
	 */
	@Transactional
	public SuministroDTO registrar(SuministroDTO datos) {
		LocalDateTime fecha = (datos.fechaSuministro() == null ? LocalDateTime.now(reloj) : datos.fechaSuministro())
				.truncatedTo(ChronoUnit.SECONDS);
		SuministroId id = new SuministroId(datos.nit().trim(), datos.idRepuesto(), datos.idSucursal(), fecha);
		if (suministroRepository.existsById(id)) {
			throw new ConflictException("Ya hay una entrega registrada con ese proveedor, repuesto, sede y hora.");
		}
		Suministro suministro = new Suministro(
				proveedorRepository.findById(datos.nit().trim()).orElseThrow(
						() -> new ResourceNotFoundException("No existe el proveedor con NIT " + datos.nit() + ".")),
				repuestoRepository.findById(datos.idRepuesto()).orElseThrow(
						() -> new ResourceNotFoundException("No existe el repuesto con id " + datos.idRepuesto() + ".")),
				sucursalRepository.findById(datos.idSucursal()).orElseThrow(
						() -> new ResourceNotFoundException("No existe la sede con id " + datos.idSucursal() + ".")),
				fecha, datos.cantidad(), datos.costoUnitario());
		return SuministroDTO.de(suministroRepository.saveAndFlush(suministro));
	}

	/**
	 * Borra un suministro por su llave completa.
	 *
	 * @param id llave del suministro: proveedor, repuesto, sede y fecha
	 */
	@Transactional
	public void eliminar(SuministroId id) {
		Suministro suministro = suministroRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No existe esa entrega de repuestos."));
		suministroRepository.delete(suministro);
		suministroRepository.flush();
	}
}

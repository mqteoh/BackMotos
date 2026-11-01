package co.edu.unbosque.BackMotos.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.MotoAdminDTO;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.ModeloId;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ModeloRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;

@Service
public class MotoAdminService {

	private final MotoRepository motoRepository;
	private final ModeloRepository modeloRepository;
	private final SucursalRepository sucursalRepository;
	private final VentaMotoRepository ventaMotoRepository;

	public MotoAdminService(MotoRepository motoRepository, ModeloRepository modeloRepository,
			SucursalRepository sucursalRepository, VentaMotoRepository ventaMotoRepository) {
		this.motoRepository = motoRepository;
		this.modeloRepository = modeloRepository;
		this.sucursalRepository = sucursalRepository;
		this.ventaMotoRepository = ventaMotoRepository;
	}

	@Transactional(readOnly = true)
	public List<MotoAdminDTO> listar(Integer idSucursal, OrigenMoto origen) {
		Set<String> vendidas = ventaMotoRepository.findAll().stream().map(v -> v.getMoto().getPlaca())
				.collect(Collectors.toSet());
		return motoRepository.findAll().stream()
				.filter(m -> idSucursal == null
						|| (m.getSucursal() != null && m.getSucursal().getIdSucursal().equals(idSucursal)))
				.filter(m -> origen == null || m.getOrigen() == origen)
				.sorted(Comparator.comparing(Moto::getPlaca))
				.map(m -> MotoAdminDTO.de(m, vendidas.contains(m.getPlaca()))).toList();
	}

	@Transactional(readOnly = true)
	public MotoAdminDTO buscar(String placa) {
		Moto moto = obtener(normalizar(placa));
		return MotoAdminDTO.de(moto, ventaMotoRepository.existsByMotoPlaca(moto.getPlaca()));
	}

	@Transactional
	public MotoAdminDTO crear(MotoAdminDTO datos) {
		String placa = normalizar(datos.placa());
		if (motoRepository.existsById(placa)) {
			throw new ConflictException("Ya existe una moto con la placa " + placa + ".");
		}
		Moto moto = new Moto();
		moto.setPlaca(placa);
		copiar(datos, moto);
		return MotoAdminDTO.de(motoRepository.saveAndFlush(moto), false);
	}

	@Transactional
	public MotoAdminDTO actualizar(String placa, MotoAdminDTO datos) {
		Moto moto = obtener(normalizar(placa));
		if (!moto.getPlaca().equals(normalizar(datos.placa()))) {
			throw new BadRequestException("La placa no se puede cambiar; registre la moto de nuevo.");
		}
		copiar(datos, moto);
		return MotoAdminDTO.de(motoRepository.saveAndFlush(moto), ventaMotoRepository.existsByMotoPlaca(moto.getPlaca()));
	}

	@Transactional
	public void eliminar(String placa) {
		motoRepository.delete(obtener(normalizar(placa)));
		motoRepository.flush();
	}

	private void copiar(MotoAdminDTO datos, Moto moto) {
		if (datos.origen() == OrigenMoto.INVENTARIO && (datos.idSucursal() == null || datos.precio() == null)) {
			throw new BadRequestException("Una moto de inventario necesita sede y precio.");
		}
		if (datos.origen() == OrigenMoto.EXTERNA && (datos.idSucursal() != null || datos.precio() != null)) {
			throw new BadRequestException("Una moto externa no lleva sede ni precio.");
		}
		moto.setModelo(modeloRepository.findById(new ModeloId(datos.idMarca(), datos.nroModelo()))
				.orElseThrow(() -> new ResourceNotFoundException("No existe el modelo indicado.")));
		moto.setSucursal(datos.idSucursal() == null ? null
				: sucursalRepository.findById(datos.idSucursal()).orElseThrow(
						() -> new ResourceNotFoundException("No existe la sede con id " + datos.idSucursal() + ".")));
		moto.setOrigen(datos.origen());
		moto.setAnio(datos.anio());
		moto.setColor(datos.color().trim());
		moto.setKilometraje(datos.kilometraje());
		moto.setPrecio(datos.precio());
		moto.setFechaIngreso(datos.fechaIngreso());
	}

	private Moto obtener(String placa) {
		return motoRepository.findById(placa)
				.orElseThrow(() -> new ResourceNotFoundException("No existe la moto con placa " + placa + "."));
	}

	private String normalizar(String placa) {
		return placa.trim().toUpperCase(Locale.ROOT);
	}
}

package co.edu.unbosque.BackMotos.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.FiltrosCatalogoDTO;
import co.edu.unbosque.BackMotos.dto.FiltrosCatalogoDTO.Opcion;
import co.edu.unbosque.BackMotos.dto.FiltrosCatalogoDTO.OpcionSede;
import co.edu.unbosque.BackMotos.dto.MotoCatalogoDTO;
import co.edu.unbosque.BackMotos.dto.MotoDetalleDTO;
import co.edu.unbosque.BackMotos.dto.OrdenCatalogo;
import co.edu.unbosque.BackMotos.dto.PaginaDTO;
import co.edu.unbosque.BackMotos.dto.SucursalDTO;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.MotoDisponible;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.MotoDisponibleRepository;
import co.edu.unbosque.BackMotos.repository.MotoRepository;
import co.edu.unbosque.BackMotos.repository.SucursalRepository;

@Service
@Transactional(readOnly = true)
public class CatalogoService {

	public static final int TAMANO_MAXIMO = 48;
	private static final Set<String> TIPOS = Set.of("CALLE", "DEPORTIVA", "NAKED", "SCOOTER", "ENDURO", "TOURING",
			"DOBLE_PROPOSITO");

	public record FiltroMotos(String marca, String tipo, Integer idSucursal, BigDecimal precioMin,
			BigDecimal precioMax, String texto) {
	}

	private final MotoDisponibleRepository motoDisponibleRepository;
	private final MotoRepository motoRepository;
	private final SucursalRepository sucursalRepository;
	private final ModelMapper mapper;

	public CatalogoService(MotoDisponibleRepository motoDisponibleRepository, MotoRepository motoRepository,
			SucursalRepository sucursalRepository, ModelMapper mapper) {
		this.motoDisponibleRepository = motoDisponibleRepository;
		this.motoRepository = motoRepository;
		this.sucursalRepository = sucursalRepository;
		this.mapper = mapper;
	}

	public PaginaDTO<MotoCatalogoDTO> buscarMotos(FiltroMotos filtro, int pagina, int tamano, OrdenCatalogo orden) {
		validar(filtro, pagina, tamano);
		PageRequest solicitud = PageRequest.of(pagina, tamano, orden.getSort());
		return PaginaDTO.de(motoDisponibleRepository.findAll(especificacion(filtro), solicitud), MotoCatalogoDTO::de);
	}

	public MotoDetalleDTO detalleMoto(String placa) {
		String normalizada = placa.trim().toUpperCase(Locale.ROOT);
		if (!motoDisponibleRepository.existsById(normalizada)) {
			throw new ResourceNotFoundException("La moto " + normalizada + " no existe o ya no está disponible.");
		}
		Moto moto = motoRepository.findById(normalizada).orElseThrow();
		return MotoDetalleDTO.de(moto, mapper.map(moto.getSucursal(), SucursalDTO.class));
	}

	public FiltrosCatalogoDTO filtros() {
		List<MotoDisponible> disponibles = motoDisponibleRepository.findAll();
		Map<Integer, Long> porSede = disponibles.stream()
				.collect(Collectors.groupingBy(MotoDisponible::getIdSucursal, Collectors.counting()));
		List<OpcionSede> sedes = sucursalRepository.findAll().stream()
				.map(s -> new OpcionSede(s.getIdSucursal(), s.getNombre(), s.getCiudad(),
						porSede.getOrDefault(s.getIdSucursal(), 0L)))
				.sorted(Comparator.comparing(OpcionSede::nombre)).toList();
		BigDecimal minimo = disponibles.stream().map(MotoDisponible::getPrecio).min(BigDecimal::compareTo)
				.orElse(null);
		BigDecimal maximo = disponibles.stream().map(MotoDisponible::getPrecio).max(BigDecimal::compareTo)
				.orElse(null);
		return new FiltrosCatalogoDTO(contar(disponibles, MotoDisponible::getMarca),
				contar(disponibles, MotoDisponible::getTipoMoto), sedes, minimo, maximo, disponibles.size());
	}

	private List<Opcion> contar(List<MotoDisponible> motos, Function<MotoDisponible, String> campo) {
		Map<String, Long> conteo = motos.stream()
				.collect(Collectors.groupingBy(campo, TreeMap::new, Collectors.counting()));
		List<Opcion> opciones = new ArrayList<>();
		conteo.forEach((valor, cantidad) -> opciones.add(new Opcion(valor, cantidad)));
		return opciones;
	}

	private void validar(FiltroMotos filtro, int pagina, int tamano) {
		if (pagina < 0) {
			throw new BadRequestException("La página no puede ser negativa.");
		}
		if (tamano < 1 || tamano > TAMANO_MAXIMO) {
			throw new BadRequestException("El tamaño de página debe estar entre 1 y " + TAMANO_MAXIMO + ".");
		}
		if (filtro.tipo() != null && !filtro.tipo().isBlank()
				&& !TIPOS.contains(filtro.tipo().trim().toUpperCase(Locale.ROOT))) {
			throw new BadRequestException("El tipo de moto " + filtro.tipo() + " no existe.");
		}
		if (filtro.precioMin() != null && filtro.precioMax() != null
				&& filtro.precioMin().compareTo(filtro.precioMax()) > 0) {
			throw new BadRequestException("El precio mínimo no puede ser mayor que el máximo.");
		}
	}

	private Specification<MotoDisponible> especificacion(FiltroMotos f) {
		List<Specification<MotoDisponible>> condiciones = new ArrayList<>();
		if (f.marca() != null && !f.marca().isBlank()) {
			String marca = f.marca().trim().toLowerCase(Locale.ROOT);
			condiciones.add((r, q, cb) -> cb.equal(cb.lower(r.get("marca")), marca));
		}
		if (f.tipo() != null && !f.tipo().isBlank()) {
			String tipo = f.tipo().trim().toUpperCase(Locale.ROOT);
			condiciones.add((r, q, cb) -> cb.equal(r.get("tipoMoto"), tipo));
		}
		if (f.idSucursal() != null) {
			condiciones.add((r, q, cb) -> cb.equal(r.get("idSucursal"), f.idSucursal()));
		}
		if (f.precioMin() != null) {
			condiciones.add((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("precio"), f.precioMin()));
		}
		if (f.precioMax() != null) {
			condiciones.add((r, q, cb) -> cb.lessThanOrEqualTo(r.get("precio"), f.precioMax()));
		}
		if (f.texto() != null && !f.texto().isBlank()) {
			String patron = "%" + f.texto().trim().toLowerCase(Locale.ROOT) + "%";
			condiciones.add((r, q, cb) -> cb.or(cb.like(cb.lower(r.get("marca")), patron),
					cb.like(cb.lower(r.get("modelo")), patron), cb.like(cb.lower(r.get("placa")), patron)));
		}
		return Specification.allOf(condiciones);
	}
}

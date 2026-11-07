package co.edu.unbosque.BackMotos.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ReparacionDTO;
import co.edu.unbosque.BackMotos.entity.VentaMoto;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;

/**
 * Reparaciones vistas por el cliente: las de las motos que compró.
 */
@Service
public class ReparacionService {

	/** Acceso a la base para las reparaciones. */
	private final ReparacionRepository reparacionRepository;
	/** Acceso a la base para las ventas de motos. */
	private final VentaMotoRepository ventaMotoRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param reparacionRepository acceso a la base para las reparaciones
	 * @param ventaMotoRepository acceso a la base para las ventas de motos
	 */
	public ReparacionService(ReparacionRepository reparacionRepository, VentaMotoRepository ventaMotoRepository) {
		this.reparacionRepository = reparacionRepository;
		this.ventaMotoRepository = ventaMotoRepository;
	}

	/**
	 * Reparaciones de las motos de un cliente.
	 *
	 * @param idCliente cliente con sesión iniciada
	 * @return reparaciones de la más reciente a la más antigua
	 */
	@Transactional(readOnly = true)
	public List<ReparacionDTO> deCliente(Integer idCliente) {
		Map<String, VentaMoto> ventas = ventaMotoRepository.findByClienteIdClienteOrderByFechaVentaDesc(idCliente)
				.stream().collect(Collectors.toMap(VentaMoto::getPlaca, Function.identity()));
		return reparacionRepository.deCliente(idCliente).stream()
				.map(r -> ReparacionDTO.de(r, ventas.get(r.getMoto().getPlaca()))).toList();
	}
}

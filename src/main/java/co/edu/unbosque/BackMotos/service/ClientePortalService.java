package co.edu.unbosque.BackMotos.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ComprasClienteDTO;
import co.edu.unbosque.BackMotos.dto.MotoClienteDTO;
import co.edu.unbosque.BackMotos.dto.PerfilClienteDTO;
import co.edu.unbosque.BackMotos.dto.VentaAccesorioDTO;
import co.edu.unbosque.BackMotos.dto.VentaMotoDTO;
import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.ReparacionRepository;
import co.edu.unbosque.BackMotos.repository.VentaAccesorioRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;

@Service
@Transactional(readOnly = true)
public class ClientePortalService {

	private final ClienteRepository clienteRepository;
	private final VentaMotoRepository ventaMotoRepository;
	private final VentaAccesorioRepository ventaAccesorioRepository;
	private final ReparacionRepository reparacionRepository;

	public ClientePortalService(ClienteRepository clienteRepository, VentaMotoRepository ventaMotoRepository,
			VentaAccesorioRepository ventaAccesorioRepository, ReparacionRepository reparacionRepository) {
		this.clienteRepository = clienteRepository;
		this.ventaMotoRepository = ventaMotoRepository;
		this.ventaAccesorioRepository = ventaAccesorioRepository;
		this.reparacionRepository = reparacionRepository;
	}

	public PerfilClienteDTO perfil(Integer idCliente) {
		return aPerfil(obtener(idCliente));
	}

	@Transactional
	public PerfilClienteDTO actualizarPerfil(Integer idCliente, PerfilClienteDTO datos) {
		Cliente cliente = obtener(idCliente);
		cliente.setNombres(datos.nombres().trim());
		cliente.setApellidos(datos.apellidos().trim());
		cliente.setFechaNacimiento(datos.fechaNacimiento());
		cliente.setCiudad(datos.ciudad().trim());
		cliente.setBarrio(datos.barrio().trim());
		cliente.setCalle(datos.calle().trim());
		cliente.setNumeroDireccion(datos.numeroDireccion().trim());
		if (datos.telefonos() != null) {
			cliente.getTelefonos().clear();
			cliente.getTelefonos().addAll(datos.telefonos());
		}
		if (datos.correos() != null) {
			cliente.getCorreos().clear();
			datos.correos().stream().map(c -> c.trim().toLowerCase()).forEach(cliente.getCorreos()::add);
		}
		return aPerfil(clienteRepository.saveAndFlush(cliente));
	}

	public List<MotoClienteDTO> misMotos(Integer idCliente) {
		Map<String, Moto> motos = new LinkedHashMap<>();
		ventaMotoRepository.findByClienteIdClienteOrderByFechaVentaDesc(idCliente)
				.forEach(v -> motos.putIfAbsent(v.getMoto().getPlaca(), v.getMoto()));
		reparacionRepository.findByClienteIdClienteOrderByFechaAgendadaDesc(idCliente).stream()
				.map(r -> r.getMoto()).filter(m -> m.getOrigen() == OrigenMoto.EXTERNA)
				.forEach(m -> motos.putIfAbsent(m.getPlaca(), m));
		return motos.values().stream().map(MotoClienteDTO::de).sorted(Comparator.comparing(MotoClienteDTO::placa))
				.toList();
	}

	public ComprasClienteDTO compras(Integer idCliente) {
		List<VentaMotoDTO> motos = ventaMotoRepository.findByClienteIdClienteOrderByFechaVentaDesc(idCliente).stream()
				.map(VentaMotoDTO::de).toList();
		List<VentaAccesorioDTO> accesorios = ventaAccesorioRepository
				.findByClienteIdClienteOrderByFechaVentaDesc(idCliente).stream().map(VentaAccesorioDTO::de).toList();
		BigDecimal total = motos.stream().map(VentaMotoDTO::valorTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
				.add(accesorios.stream().map(VentaAccesorioDTO::valorTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
		return new ComprasClienteDTO(motos, accesorios, total);
	}

	private Cliente obtener(Integer idCliente) {
		return clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con id " + idCliente + "."));
	}

	private PerfilClienteDTO aPerfil(Cliente c) {
		return new PerfilClienteDTO(c.getIdCliente(), c.getDocumento(), c.getNombres(), c.getApellidos(),
				c.getFechaNacimiento(), c.getEdad(), c.getCiudad(), c.getBarrio(), c.getCalle(),
				c.getNumeroDireccion(), c.getCorreoAcceso(), new ArrayList<>(new LinkedHashSet<>(c.getTelefonos())),
				new ArrayList<>(c.getCorreos()));
	}
}

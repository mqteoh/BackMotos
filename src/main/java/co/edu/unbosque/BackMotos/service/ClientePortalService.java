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
import co.edu.unbosque.BackMotos.entity.Cuenta;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.CuentaRepository;
import co.edu.unbosque.BackMotos.repository.VentaAccesorioRepository;
import co.edu.unbosque.BackMotos.repository.VentaMotoRepository;

/** Portal del cliente: perfil, motos compradas y compras. */
@Service
@Transactional(readOnly = true)
public class ClientePortalService {

	/** Acceso a la base para los clientes. */
	private final ClienteRepository clienteRepository;
	/** Acceso a la base para las ventas de motos. */
	private final VentaMotoRepository ventaMotoRepository;
	/** Acceso a la base para las ventas de accesorios. */
	private final VentaAccesorioRepository ventaAccesorioRepository;
	/** Acceso a la base para las cuentas. */
	private final CuentaRepository cuentaRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param clienteRepository acceso a la base para los clientes
	 * @param ventaMotoRepository acceso a la base para las ventas de motos
	 * @param ventaAccesorioRepository acceso a la base para las ventas de accesorios
	 * @param cuentaRepository acceso a la base para las cuentas
	 */
	public ClientePortalService(ClienteRepository clienteRepository, VentaMotoRepository ventaMotoRepository,
			VentaAccesorioRepository ventaAccesorioRepository, CuentaRepository cuentaRepository) {
		this.clienteRepository = clienteRepository;
		this.ventaMotoRepository = ventaMotoRepository;
		this.ventaAccesorioRepository = ventaAccesorioRepository;
		this.cuentaRepository = cuentaRepository;
	}

	/**
	 * Perfil del cliente con el correo de su cuenta.
	 *
	 * @param idCliente identificador del cliente
	 * @return perfil del cliente con el correo de su cuenta
	 */
	public PerfilClienteDTO perfil(Integer idCliente) {
		return aPerfil(obtener(idCliente));
	}

	/**
	 * Cambia los datos de contacto y la dirección; el documento no cambia.
	 *
	 * @param idCliente identificador del cliente
	 * @param datos datos que llegan en la petición
	 * @return el perfil con los cambios
	 */
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

	/**
	 * Motos que el cliente compró.
	 *
	 * @param idCliente identificador del cliente
	 * @return motos que el cliente compró
	 */
	public List<MotoClienteDTO> misMotos(Integer idCliente) {
		Map<String, Moto> motos = new LinkedHashMap<>();
		ventaMotoRepository.findByClienteIdClienteOrderByFechaVentaDesc(idCliente)
				.forEach(v -> motos.putIfAbsent(v.getMoto().getPlaca(), v.getMoto()));
		return motos.values().stream().map(MotoClienteDTO::de).sorted(Comparator.comparing(MotoClienteDTO::placa))
				.toList();
	}

	/**
	 * Motos y accesorios comprados, con el total gastado.
	 *
	 * @param idCliente identificador del cliente
	 * @return motos y accesorios comprados, con el total gastado
	 */
	public ComprasClienteDTO compras(Integer idCliente) {
		List<VentaMotoDTO> motos = ventaMotoRepository.findByClienteIdClienteOrderByFechaVentaDesc(idCliente).stream()
				.map(VentaMotoDTO::de).toList();
		List<VentaAccesorioDTO> accesorios = ventaAccesorioRepository
				.findByClienteIdClienteOrderByFechaVentaDesc(idCliente).stream().map(VentaAccesorioDTO::de).toList();
		BigDecimal total = motos.stream().map(VentaMotoDTO::valorTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
				.add(accesorios.stream().map(VentaAccesorioDTO::valorTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
		return new ComprasClienteDTO(motos, accesorios, total);
	}

	/**
	 * Busca el cliente; si no existe lanza ResourceNotFoundException.
	 *
	 * @param idCliente identificador del cliente
	 * @return el cliente
	 */
	private Cliente obtener(Integer idCliente) {
		return clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con id " + idCliente + "."));
	}

	/**
	 * Arma el perfil con el correo de la cuenta.
	 *
	 * @param c cliente
	 * @return el perfil del cliente
	 */
	private PerfilClienteDTO aPerfil(Cliente c) {
		return new PerfilClienteDTO(c.getIdCliente(), c.getDocumento(), c.getNombres(), c.getApellidos(),
				c.getFechaNacimiento(), c.getEdad(), c.getCiudad(), c.getBarrio(), c.getCalle(),
				c.getNumeroDireccion(),
				cuentaRepository.findByClienteIdCliente(c.getIdCliente()).map(Cuenta::getCorreo).orElse(null), new ArrayList<>(new LinkedHashSet<>(c.getTelefonos())),
				new ArrayList<>(c.getCorreos()));
	}
}

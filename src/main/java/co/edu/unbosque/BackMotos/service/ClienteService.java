package co.edu.unbosque.BackMotos.service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ClienteDTO;
import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.CuentaRepository;

/** Clientes para el vendedor y el dueño. */
@Service
public class ClienteService {

	/** Máximo de clientes que devuelve una búsqueda. */
	private static final int MAXIMO_RESULTADOS = 20;

	/** Acceso a la base para los clientes. */
	private final ClienteRepository clienteRepository;
	/** Acceso a la base para las cuentas. */
	private final CuentaRepository cuentaRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param clienteRepository acceso a la base para los clientes
	 * @param cuentaRepository acceso a la base para las cuentas
	 */
	public ClienteService(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
		this.clienteRepository = clienteRepository;
		this.cuentaRepository = cuentaRepository;
	}

	/**
	 * Busca un cliente por documento; si no existe responde 404.
	 *
	 * @param documento número de documento de identidad
	 * @return el cliente encontrado
	 */
	@Transactional(readOnly = true)
	public ClienteDTO buscarPorDocumento(String documento) {
		return clienteRepository.findByDocumento(documento.trim()).map(this::aDTO).orElseThrow(
				() -> new ResourceNotFoundException("No hay un cliente registrado con el documento " + documento + "."));
	}

	/**
	 * Busca por nombre, sin importar tildes, o por el inicio del documento; trae pocos resultados.
	 *
	 * @param texto nombre o inicio del documento
	 * @return los clientes encontrados
	 */
	@Transactional(readOnly = true)
	public List<ClienteDTO> buscar(String texto) {
		if (texto == null || texto.trim().length() < 2) {
			throw new BadRequestException("Escriba al menos 2 caracteres para buscar.");
		}
		Set<Integer> conCuenta = cuentaRepository.idsClientesConCuenta();
		return clienteRepository.buscar(texto.trim(), PageRequest.of(0, MAXIMO_RESULTADOS)).stream()
				.map(c -> ClienteDTO.de(c, conCuenta.contains(c.getIdCliente()))).toList();
	}

	/**
	 * Todos los clientes ordenados por apellido.
	 *
	 * @return todos los clientes ordenados por apellido
	 */
	@Transactional(readOnly = true)
	public List<ClienteDTO> listar() {
		Set<Integer> conCuenta = cuentaRepository.idsClientesConCuenta();
		return clienteRepository.findAll(Sort.by("apellidos", "nombres")).stream()
				.map(c -> ClienteDTO.de(c, conCuenta.contains(c.getIdCliente()))).toList();
	}

	/**
	 * Borra un cliente; la base lo impide si tiene compras o reservas.
	 *
	 * @param idCliente identificador del cliente
	 */
	@Transactional
	public void eliminar(Integer idCliente) {
		clienteRepository.delete(obtener(idCliente));
		clienteRepository.flush();
	}

	/**
	 * Busca un cliente por id.
	 *
	 * @param idCliente identificador del cliente
	 * @return el cliente encontrado
	 */
	@Transactional(readOnly = true)
	public ClienteDTO buscarPorId(Integer idCliente) {
		return aDTO(obtener(idCliente));
	}

	/**
	 * Registra un cliente en la sede, sin cuenta web.
	 *
	 * @param datos datos que llegan en la petición
	 * @return el cliente registrado
	 */
	@Transactional
	public ClienteDTO crear(ClienteDTO datos) {
		if (clienteRepository.existsByDocumento(datos.documento())) {
			throw new ConflictException("Ya existe un cliente con el documento " + datos.documento() + ".");
		}
		Cliente cliente = new Cliente();
		cliente.setDocumento(datos.documento());
		copiar(datos, cliente);
		return aDTO(clienteRepository.saveAndFlush(cliente));
	}

	/**
	 * Cambia los datos de un cliente.
	 *
	 * @param idCliente identificador del cliente
	 * @param datos datos que llegan en la petición
	 * @return el cliente con los cambios
	 */
	@Transactional
	public ClienteDTO actualizar(Integer idCliente, ClienteDTO datos) {
		Cliente cliente = obtener(idCliente);
		if (!cliente.getDocumento().equals(datos.documento())) {
			if (clienteRepository.existsByDocumento(datos.documento())) {
				throw new ConflictException("Ya existe un cliente con el documento " + datos.documento() + ".");
			}
			cliente.setDocumento(datos.documento());
		}
		copiar(datos, cliente);
		return aDTO(clienteRepository.saveAndFlush(cliente));
	}

	/**
	 * Arma el DTO e indica si el cliente tiene cuenta web.
	 *
	 * @param cliente cliente que se convierte
	 * @return el DTO del cliente
	 */
	private ClienteDTO aDTO(Cliente cliente) {
		return ClienteDTO.de(cliente, cuentaRepository.findByClienteIdCliente(cliente.getIdCliente()).isPresent());
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
	 * Copia los datos del DTO al cliente; el documento no cambia.
	 *
	 * @param datos datos nuevos
	 * @param cliente cliente que se modifica
	 */
	private void copiar(ClienteDTO datos, Cliente cliente) {
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
			datos.correos().stream().map(c -> c.trim().toLowerCase(Locale.ROOT)).forEach(cliente.getCorreos()::add);
		}
	}
}

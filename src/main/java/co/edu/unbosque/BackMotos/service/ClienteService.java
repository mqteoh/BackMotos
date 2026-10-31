package co.edu.unbosque.BackMotos.service;

import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.ClienteDTO;
import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.exception.BadRequestException;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.exception.ResourceNotFoundException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;

@Service
public class ClienteService {

	private static final int MAXIMO_RESULTADOS = 20;

	private final ClienteRepository clienteRepository;

	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Transactional(readOnly = true)
	public ClienteDTO buscarPorDocumento(String documento) {
		return clienteRepository.findByDocumento(documento.trim()).map(ClienteDTO::de).orElseThrow(
				() -> new ResourceNotFoundException("No hay un cliente registrado con el documento " + documento + "."));
	}

	@Transactional(readOnly = true)
	public List<ClienteDTO> buscar(String texto) {
		if (texto == null || texto.trim().length() < 2) {
			throw new BadRequestException("Escriba al menos 2 caracteres para buscar.");
		}
		return clienteRepository.buscar(texto.trim(), PageRequest.of(0, MAXIMO_RESULTADOS)).stream().map(ClienteDTO::de)
				.toList();
	}

	@Transactional(readOnly = true)
	public ClienteDTO buscarPorId(Integer idCliente) {
		return ClienteDTO.de(obtener(idCliente));
	}

	@Transactional
	public ClienteDTO crear(ClienteDTO datos) {
		if (clienteRepository.existsByDocumento(datos.documento())) {
			throw new ConflictException("Ya existe un cliente con el documento " + datos.documento() + ".");
		}
		Cliente cliente = new Cliente();
		cliente.setDocumento(datos.documento());
		copiar(datos, cliente);
		return ClienteDTO.de(clienteRepository.saveAndFlush(cliente));
	}

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
		return ClienteDTO.de(clienteRepository.saveAndFlush(cliente));
	}

	private Cliente obtener(Integer idCliente) {
		return clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con id " + idCliente + "."));
	}

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

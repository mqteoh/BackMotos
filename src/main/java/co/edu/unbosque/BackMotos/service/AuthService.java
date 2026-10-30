package co.edu.unbosque.BackMotos.service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.dto.LoginDTO;
import co.edu.unbosque.BackMotos.dto.RegistroClienteDTO;
import co.edu.unbosque.BackMotos.dto.TokenDTO;
import co.edu.unbosque.BackMotos.dto.UsuarioDTO;
import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.EmpleadoRepository;
import co.edu.unbosque.BackMotos.security.JwtUtil;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;
	private final ClienteRepository clienteRepository;
	private final EmpleadoRepository empleadoRepository;

	public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
			ClienteRepository clienteRepository, EmpleadoRepository empleadoRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
		this.clienteRepository = clienteRepository;
		this.empleadoRepository = empleadoRepository;
	}

	public TokenDTO login(LoginDTO datos) {
		Authentication autenticacion = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(datos.correo().trim(), datos.contrasena()));
		return emitirToken((UsuarioAutenticado) autenticacion.getPrincipal());
	}

	@Transactional
	public TokenDTO registrarCliente(RegistroClienteDTO datos) {
		String correo = datos.correo().trim().toLowerCase(Locale.ROOT);
		if (empleadoRepository.existsByCorreoAccesoIgnoreCase(correo)
				|| clienteRepository.existsByCorreoAccesoIgnoreCase(correo)) {
			throw new ConflictException("Ese correo ya está registrado.");
		}
		Cliente cliente = clienteRepository.findByDocumento(datos.documento()).orElseGet(() -> nuevoCliente(datos));
		if (cliente.tieneCuentaWeb()) {
			throw new ConflictException("Ya existe una cuenta para ese documento.");
		}
		cliente.setCorreoAcceso(correo);
		cliente.setPasswordHash(passwordEncoder.encode(datos.contrasena()));
		cliente.getCorreos().add(correo);
		if (datos.telefono() != null && !datos.telefono().isBlank()) {
			cliente.getTelefonos().add(datos.telefono());
		}
		return emitirToken(UsuarioAutenticado.deCliente(clienteRepository.saveAndFlush(cliente)));
	}

	private Cliente nuevoCliente(RegistroClienteDTO datos) {
		Cliente cliente = new Cliente();
		cliente.setDocumento(datos.documento());
		cliente.setNombres(datos.nombres().trim());
		cliente.setApellidos(datos.apellidos().trim());
		cliente.setFechaNacimiento(datos.fechaNacimiento());
		cliente.setCiudad(datos.ciudad().trim());
		cliente.setBarrio(datos.barrio().trim());
		cliente.setCalle(datos.calle().trim());
		cliente.setNumeroDireccion(datos.numeroDireccion().trim());
		return cliente;
	}

	private TokenDTO emitirToken(UsuarioAutenticado usuario) {
		return new TokenDTO(jwtUtil.generarToken(usuario), "Bearer", jwtUtil.getExpiracionMs(), UsuarioDTO.de(usuario));
	}
}

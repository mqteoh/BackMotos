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
import co.edu.unbosque.BackMotos.entity.Cuenta;
import co.edu.unbosque.BackMotos.exception.ConflictException;
import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.CuentaRepository;
import co.edu.unbosque.BackMotos.security.JwtUtil;
import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

/**
 * Inicio de sesión y registro de clientes desde la página.
 */
@Service
public class AuthService {

	/** Revisa el correo y la contraseña al iniciar sesión. */
	private final AuthenticationManager authenticationManager;
	/** Crea y valida los tokens JWT. */
	private final JwtUtil jwtUtil;
	/** Cifra y compara contraseñas. */
	private final PasswordEncoder passwordEncoder;
	/** Acceso a la base para los clientes. */
	private final ClienteRepository clienteRepository;
	/** Acceso a la base para las cuentas. */
	private final CuentaRepository cuentaRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param authenticationManager revisa el correo y la contraseña al iniciar sesión
	 * @param jwtUtil crea y valida los tokens JWT
	 * @param passwordEncoder cifra y compara contraseñas
	 * @param clienteRepository acceso a la base para los clientes
	 * @param cuentaRepository acceso a la base para las cuentas
	 */
	public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
			ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
		this.clienteRepository = clienteRepository;
		this.cuentaRepository = cuentaRepository;
	}

	/**
	 * Revisa correo y contraseña y entrega un token JWT.
	 *
	 * @param datos correo y contraseña
	 * @return token y datos del usuario
	 */
	public TokenDTO login(LoginDTO datos) {
		Authentication autenticacion = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(datos.correo().trim(), datos.contrasena()));
		return emitirToken((UsuarioAutenticado) autenticacion.getPrincipal());
	}

	/**
	 * Crea la cuenta web de un cliente. Si el documento ya existe como cliente, le crea la cuenta a ese cliente.
	 *
	 * @param datos datos personales, correo y contraseña
	 * @return token para entrar de una vez
	 */
	@Transactional
	public TokenDTO registrarCliente(RegistroClienteDTO datos) {
		String correo = datos.correo().trim().toLowerCase(Locale.ROOT);
		if (cuentaRepository.existsByCorreoIgnoreCase(correo)) {
			throw new ConflictException("Ese correo ya está registrado.");
		}
		Cliente cliente = clienteRepository.findByDocumento(datos.documento()).orElseGet(() -> nuevoCliente(datos));
		if (cliente.getIdCliente() != null && cuentaRepository.findByClienteIdCliente(cliente.getIdCliente()).isPresent()) {
			throw new ConflictException("Ya existe una cuenta para ese documento.");
		}
		cliente.getCorreos().add(correo);
		if (datos.telefono() != null && !datos.telefono().isBlank()) {
			cliente.getTelefonos().add(datos.telefono());
		}
		Cuenta cuenta = new Cuenta();
		cuenta.setCorreo(correo);
		cuenta.setPasswordHash(passwordEncoder.encode(datos.contrasena()));
		cuenta.setRol(Rol.CLIENTE);
		cuenta.setCliente(clienteRepository.saveAndFlush(cliente));
		return emitirToken(UsuarioAutenticado.deCuenta(cuentaRepository.saveAndFlush(cuenta)));
	}

	/**
	 * Crea el cliente con los datos del registro, sin espacios sobrantes.
	 *
	 * @param datos datos del registro
	 * @return el cliente nuevo, todavía sin guardar
	 */
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

	/**
	 * Genera el token para el usuario.
	 *
	 * @param usuario usuario que inició sesión
	 * @return el token y los datos del usuario
	 */
	private TokenDTO emitirToken(UsuarioAutenticado usuario) {
		return new TokenDTO(jwtUtil.generarToken(usuario), "Bearer", jwtUtil.getExpiracionMs(), UsuarioDTO.de(usuario));
	}
}

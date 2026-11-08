package co.edu.unbosque.BackMotos.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.repository.CuentaRepository;

/**
 * Busca la cuenta por correo cuando alguien inicia sesión.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/** Acceso a la base para las cuentas. */
	private final CuentaRepository cuentaRepository;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param cuentaRepository acceso a la base para las cuentas
	 */
	public UserDetailsServiceImpl(CuentaRepository cuentaRepository) {
		this.cuentaRepository = cuentaRepository;
	}

	/** Busca la cuenta por correo para iniciar sesión. */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String correo) {
		String normalizado = correo == null ? "" : correo.trim();
		return cuentaRepository.findByCorreoIgnoreCase(normalizado).map(UsuarioAutenticado::deCuenta)
				.orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta con ese correo."));
	}
}

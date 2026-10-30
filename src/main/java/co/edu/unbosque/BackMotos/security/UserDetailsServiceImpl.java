package co.edu.unbosque.BackMotos.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BackMotos.repository.ClienteRepository;
import co.edu.unbosque.BackMotos.repository.EmpleadoRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final EmpleadoRepository empleadoRepository;
	private final ClienteRepository clienteRepository;

	public UserDetailsServiceImpl(EmpleadoRepository empleadoRepository, ClienteRepository clienteRepository) {
		this.empleadoRepository = empleadoRepository;
		this.clienteRepository = clienteRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String correo) {
		String normalizado = correo == null ? "" : correo.trim();
		return empleadoRepository.findByCorreoAccesoIgnoreCase(normalizado).map(UsuarioAutenticado::deEmpleado)
				.or(() -> clienteRepository.findByCorreoAccesoIgnoreCase(normalizado)
						.filter(c -> c.getPasswordHash() != null).map(UsuarioAutenticado::deCliente))
				.orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta con ese correo."));
	}
}

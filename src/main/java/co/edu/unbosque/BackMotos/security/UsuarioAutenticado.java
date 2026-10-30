package co.edu.unbosque.BackMotos.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.entity.Empleado;

public class UsuarioAutenticado implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final Integer id;
	private final String correo;
	private final String passwordHash;
	private final String nombre;
	private final Rol rol;
	private final Integer idSucursal;

	public UsuarioAutenticado(Integer id, String correo, String passwordHash, String nombre, Rol rol,
			Integer idSucursal) {
		this.id = id;
		this.correo = correo;
		this.passwordHash = passwordHash;
		this.nombre = nombre;
		this.rol = rol;
		this.idSucursal = idSucursal;
	}

	public static UsuarioAutenticado deEmpleado(Empleado empleado) {
		Integer sucursal = empleado.getSucursal() == null ? null : empleado.getSucursal().getIdSucursal();
		return new UsuarioAutenticado(empleado.getIdEmpleado(), empleado.getCorreoAcceso(),
				empleado.getPasswordHash(), empleado.getNombreCompleto(),
				Rol.valueOf(empleado.getTipoEmpleado().name()), sucursal);
	}

	public static UsuarioAutenticado deCliente(Cliente cliente) {
		return new UsuarioAutenticado(cliente.getIdCliente(), cliente.getCorreoAcceso(), cliente.getPasswordHash(),
				cliente.getNombreCompleto(), Rol.CLIENTE, null);
	}

	public Integer getId() {
		return id;
	}

	public String getCorreo() {
		return correo;
	}

	public String getNombre() {
		return nombre;
	}

	public Rol getRol() {
		return rol;
	}

	public Integer getIdSucursal() {
		return idSucursal;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
	}

	@Override
	public String getPassword() {
		return passwordHash;
	}

	@Override
	public String getUsername() {
		return correo;
	}
}

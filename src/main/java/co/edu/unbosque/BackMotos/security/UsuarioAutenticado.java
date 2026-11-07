package co.edu.unbosque.BackMotos.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import co.edu.unbosque.BackMotos.entity.Cliente;
import co.edu.unbosque.BackMotos.entity.Cuenta;
import co.edu.unbosque.BackMotos.entity.Empleado;

/**
 * Usuario con sesión iniciada. Guarda lo que el back necesita en cada petición: id, rol y sede.
 */
public class UsuarioAutenticado implements UserDetails {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/** Identificador del cliente o del empleado. */
	private final Integer id;
	/** Correo con el que inició sesión. */
	private final String correo;
	/** Contraseña cifrada. */
	private final String passwordHash;
	/** Nombre completo del usuario. */
	private final String nombre;
	/** Rol del usuario. */
	private final Rol rol;
	/** Sede del empleado; null si es un cliente. */
	private final Integer idSucursal;

	/**
	 * Crea el usuario con los datos de su cuenta.
	 *
	 * @param id identificador del cliente o del empleado
	 * @param correo correo con el que inició sesión
	 * @param passwordHash contraseña cifrada
	 * @param nombre nombre completo del usuario
	 * @param rol rol del usuario
	 * @param idSucursal sede del empleado; null si es un cliente
	 */
	public UsuarioAutenticado(Integer id, String correo, String passwordHash, String nombre, Rol rol,
			Integer idSucursal) {
		this.id = id;
		this.correo = correo;
		this.passwordHash = passwordHash;
		this.nombre = nombre;
		this.rol = rol;
		this.idSucursal = idSucursal;
	}

	/**
	 * Arma el usuario de la sesión a partir de su cuenta.
	 * El id es el del cliente o el del empleado; el dueño no queda atado a una sede.
	 *
	 * @param cuenta cuenta con su cliente o empleado cargado
	 * @return usuario listo para Spring Security
	 */
	public static UsuarioAutenticado deCuenta(Cuenta cuenta) {
		if (cuenta.getRol() == Rol.CLIENTE) {
			Cliente cliente = cuenta.getCliente();
			return new UsuarioAutenticado(cliente.getIdCliente(), cuenta.getCorreo(), cuenta.getPasswordHash(),
					cliente.getNombreCompleto(), Rol.CLIENTE, null);
		}
		Empleado empleado = cuenta.getEmpleado();
		Integer sucursal = cuenta.getRol() == Rol.DUENO ? null : empleado.getSucursal().getIdSucursal();
		return new UsuarioAutenticado(empleado.getIdEmpleado(), cuenta.getCorreo(), cuenta.getPasswordHash(),
				empleado.getNombreCompleto(), cuenta.getRol(), sucursal);
	}

	/**
	 * Devuelve el identificador del cliente o del empleado.
	 *
	 * @return el identificador del cliente o del empleado
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Devuelve el correo con el que inició sesión.
	 *
	 * @return el correo con el que inició sesión
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * Devuelve el nombre completo del usuario.
	 *
	 * @return el nombre completo del usuario
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve el rol del usuario.
	 *
	 * @return el rol del usuario
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * Devuelve la sede del empleado; null si es un cliente.
	 *
	 * @return la sede del empleado; null si es un cliente
	 */
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

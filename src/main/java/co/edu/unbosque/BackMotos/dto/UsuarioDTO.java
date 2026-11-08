package co.edu.unbosque.BackMotos.dto;

import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

/**
 * Usuario con sesión iniciada: nombre, correo, rol y sede.
 *
 * @param id identificador del cliente o del empleado
 * @param nombre nombre completo
 * @param correo correo electrónico
 * @param rol rol con el que entra a la aplicación
 * @param idSucursal identificador de la sede
 */
public record UsuarioDTO(Integer id, String nombre, String correo, Rol rol, Integer idSucursal) {

	/**
	 * Arma el DTO desde el usuario del token.
	 *
	 * @param usuario usuario del token
	 * @return los datos del usuario
	 */
	public static UsuarioDTO de(UsuarioAutenticado usuario) {
		return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol(),
				usuario.getIdSucursal());
	}
}

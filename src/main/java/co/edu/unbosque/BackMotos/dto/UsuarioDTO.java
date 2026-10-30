package co.edu.unbosque.BackMotos.dto;

import co.edu.unbosque.BackMotos.security.Rol;
import co.edu.unbosque.BackMotos.security.UsuarioAutenticado;

public record UsuarioDTO(Integer id, String nombre, String correo, Rol rol, Integer idSucursal) {

	public static UsuarioDTO de(UsuarioAutenticado usuario) {
		return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol(),
				usuario.getIdSucursal());
	}
}

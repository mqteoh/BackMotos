package co.edu.unbosque.BackMotos.dto;

/**
 * Respuesta del inicio de sesión: token, vencimiento y datos del usuario.
 *
 * @param token token JWT para las siguientes peticiones
 * @param tipo tipo de token, siempre Bearer
 * @param expiraEnMs milisegundos que dura el token
 * @param usuario datos del usuario que inició sesión
 */
public record TokenDTO(String token, String tipo, long expiraEnMs, UsuarioDTO usuario) {
}

package co.edu.unbosque.BackMotos.dto;

public record TokenDTO(String token, String tipo, long expiraEnMs, UsuarioDTO usuario) {
}

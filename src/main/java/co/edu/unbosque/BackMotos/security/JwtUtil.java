package co.edu.unbosque.BackMotos.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/** Crea y valida los tokens JWT. */
@Component
public class JwtUtil {

	/** Largo mínimo del secreto JWT, en caracteres. */
	private static final int LONGITUD_MINIMA_SECRETO = 32;

	/** Llave con la que se firman los tokens. */
	private final Key llave;
	/** Milisegundos que dura un token. */
	private final long expiracionMs;

	/**
	 * Prepara la llave para firmar los tokens; el secreto debe tener al menos 32 caracteres.
	 *
	 * @param secreto secreto con el que se firman los tokens
	 * @param expiracionMs milisegundos que dura un token
	 */
	public JwtUtil(@Value("${jwt.secret}") String secreto, @Value("${jwt.expiration}") long expiracionMs) {
		byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
		if (bytes.length < LONGITUD_MINIMA_SECRETO) {
			throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres.");
		}
		this.llave = Keys.hmacShaKeyFor(bytes);
		this.expiracionMs = expiracionMs;
	}

	/**
	 * Crea un token con el id, el rol y la sede del usuario.
	 *
	 * @param usuario usuario que inició sesión
	 * @return el token firmado
	 */
	public String generarToken(UsuarioAutenticado usuario) {
		Date ahora = new Date();
		return Jwts.builder().setSubject(usuario.getCorreo()).claim("id", usuario.getId())
				.claim("rol", usuario.getRol().name()).claim("nombre", usuario.getNombre())
				.claim("sucursal", usuario.getIdSucursal()).setIssuedAt(ahora)
				.setExpiration(new Date(ahora.getTime() + expiracionMs)).signWith(llave, SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Lee un token; queda vacío si es falso o ya venció.
	 *
	 * @param token token que llega en la petición
	 * @return el usuario del token, o vacío si no es válido
	 */
	public Optional<UsuarioAutenticado> validar(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(llave).build().parseClaimsJws(token).getBody();
			return Optional.of(new UsuarioAutenticado(claims.get("id", Integer.class), claims.getSubject(), null,
					claims.get("nombre", String.class), Rol.valueOf(claims.get("rol", String.class)),
					claims.get("sucursal", Integer.class)));
		} catch (JwtException | IllegalArgumentException e) {
			return Optional.empty();
		}
	}

	/**
	 * Devuelve los milisegundos que dura un token.
	 *
	 * @return los milisegundos que dura un token
	 */
	public long getExpiracionMs() {
		return expiracionMs;
	}
}

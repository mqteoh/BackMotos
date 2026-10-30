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

@Component
public class JwtUtil {

	private static final int LONGITUD_MINIMA_SECRETO = 32;

	private final Key llave;
	private final long expiracionMs;

	public JwtUtil(@Value("${jwt.secret}") String secreto, @Value("${jwt.expiration}") long expiracionMs) {
		byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
		if (bytes.length < LONGITUD_MINIMA_SECRETO) {
			throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres.");
		}
		this.llave = Keys.hmacShaKeyFor(bytes);
		this.expiracionMs = expiracionMs;
	}

	public String generarToken(UsuarioAutenticado usuario) {
		Date ahora = new Date();
		return Jwts.builder().setSubject(usuario.getCorreo()).claim("id", usuario.getId())
				.claim("rol", usuario.getRol().name()).claim("nombre", usuario.getNombre())
				.claim("sucursal", usuario.getIdSucursal()).setIssuedAt(ahora)
				.setExpiration(new Date(ahora.getTime() + expiracionMs)).signWith(llave, SignatureAlgorithm.HS256)
				.compact();
	}

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

	public long getExpiracionMs() {
		return expiracionMs;
	}
}

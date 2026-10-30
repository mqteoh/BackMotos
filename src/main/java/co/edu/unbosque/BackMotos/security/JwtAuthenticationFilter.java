package co.edu.unbosque.BackMotos.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String PREFIJO = "Bearer ";

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String cabecera = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (cabecera != null && cabecera.startsWith(PREFIJO)
				&& SecurityContextHolder.getContext().getAuthentication() == null) {
			jwtUtil.validar(cabecera.substring(PREFIJO.length())).ifPresent(usuario -> {
				UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(usuario,
						null, usuario.getAuthorities());
				autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(autenticacion);
			});
		}
		chain.doFilter(request, response);
	}
}

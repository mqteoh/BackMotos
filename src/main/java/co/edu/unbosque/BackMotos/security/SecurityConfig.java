package co.edu.unbosque.BackMotos.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unbosque.BackMotos.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private static final String[] RUTAS_DOCUMENTACION = { "/", "/index.html", "/swagger-ui.html", "/swagger-ui/**",
			"/v3/api-docs/**" };

	private final JwtAuthenticationFilter jwtFilter;
	private final ObjectMapper objectMapper;
	private final List<String> origenesPermitidos;

	public SecurityConfig(JwtAuthenticationFilter jwtFilter, ObjectMapper objectMapper,
			@Value("${app.cors.origenes:http://localhost:4200}") String origenes) {
		this.jwtFilter = jwtFilter;
		this.objectMapper = objectMapper;
		this.origenesPermitidos = Arrays.stream(origenes.split(",")).map(String::trim).filter(s -> !s.isEmpty())
				.toList();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).cors(c -> c.configurationSource(corsConfigurationSource()))
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(RUTAS_DOCUMENTACION).permitAll()
						.requestMatchers("/auth/login", "/auth/registro").permitAll()
						.requestMatchers("/api/publico/**").permitAll()
						.requestMatchers("/api/cliente/**").hasRole(Rol.CLIENTE.name())
						.requestMatchers("/api/vendedor/**").hasAnyRole(Rol.VENDEDOR.name(), Rol.DUENO.name())
						.requestMatchers("/api/tecnico/**").hasAnyRole(Rol.TECNICO.name(), Rol.DUENO.name())
						.requestMatchers("/api/admin/**").hasRole(Rol.DUENO.name()).anyRequest().authenticated())
				.exceptionHandling(e -> e
						.authenticationEntryPoint((req, res, ex) -> escribirError(req, res, 401,
								"Debe iniciar sesión para continuar."))
						.accessDeniedHandler((req, res, ex) -> escribirError(req, res, 403,
								"No tiene permiso para realizar esta acción.")))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> registroFiltroJwt(JwtAuthenticationFilter filtro) {
		FilterRegistrationBean<JwtAuthenticationFilter> registro = new FilterRegistrationBean<>(filtro);
		registro.setEnabled(false);
		return registro;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(origenesPermitidos);
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuracion) throws Exception {
		return configuracion.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private void escribirError(HttpServletRequest req, HttpServletResponse res, int estado, String mensaje)
			throws IOException {
		res.setStatus(estado);
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
		res.setCharacterEncoding("UTF-8");
		objectMapper.writeValue(res.getOutputStream(), ErrorDTO.de(estado, mensaje, req.getRequestURI()));
	}
}

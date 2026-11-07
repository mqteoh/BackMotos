package co.edu.unbosque.BackMotos.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

/** Documentación de la API en Swagger, separada por rol. */
@Configuration
public class OpenApiConfig {

	/** Constructor que usa Spring para crear la configuración. */
	public OpenApiConfig() {
	}

	/**
	 * Datos generales de la API y el esquema de token JWT.
	 *
	 * @return datos generales de la API y el esquema de token JWT
	 */
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.info(new Info().title("API Concesionario de Motos").version("1.0").description(
						"Catálogo, reservas, ventas, taller y administración del concesionario. Inicie sesión en "
								+ "/auth/login, copie el token y péguelo en el botón Authorize."))
				.components(new Components().addSecuritySchemes("bearer",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}

	/**
	 * Grupo con todas las rutas.
	 *
	 * @return grupo con todas las rutas
	 */
	@Bean
	public GroupedOpenApi todo() {
		return GroupedOpenApi.builder().group("1-todo").displayName("Todo").pathsToMatch("/**").build();
	}

	/**
	 * Grupo con las rutas públicas y de inicio de sesión.
	 *
	 * @return grupo con las rutas públicas y de inicio de sesión
	 */
	@Bean
	public GroupedOpenApi publico() {
		return GroupedOpenApi.builder().group("2-publico").displayName("Público y autenticación")
				.pathsToMatch("/api/publico/**", "/auth/**").build();
	}

	/**
	 * Grupo con las rutas del cliente.
	 *
	 * @return grupo con las rutas del cliente
	 */
	@Bean
	public GroupedOpenApi cliente() {
		return GroupedOpenApi.builder().group("3-cliente").displayName("Cliente").pathsToMatch("/api/cliente/**")
				.build();
	}

	/**
	 * Grupo con las rutas del vendedor.
	 *
	 * @return grupo con las rutas del vendedor
	 */
	@Bean
	public GroupedOpenApi vendedor() {
		return GroupedOpenApi.builder().group("4-vendedor").displayName("Vendedor").pathsToMatch("/api/vendedor/**")
				.build();
	}

	/**
	 * Grupo con las rutas del técnico.
	 *
	 * @return grupo con las rutas del técnico
	 */
	@Bean
	public GroupedOpenApi tecnico() {
		return GroupedOpenApi.builder().group("5-tecnico").displayName("Técnico").pathsToMatch("/api/tecnico/**")
				.build();
	}

	/**
	 * Grupo con las rutas del dueño.
	 *
	 * @return grupo con las rutas del dueño
	 */
	@Bean
	public GroupedOpenApi dueno() {
		return GroupedOpenApi.builder().group("6-dueno").displayName("Dueño").pathsToMatch("/api/admin/**").build();
	}
}

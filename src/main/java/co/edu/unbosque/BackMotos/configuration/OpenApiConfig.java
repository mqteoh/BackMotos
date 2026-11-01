package co.edu.unbosque.BackMotos.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.info(new Info().title("API Concesionario de Motos").version("1.0").description(
						"Catálogo, reservas, ventas, taller y administración del concesionario. Inicie sesión en "
								+ "/auth/login, copie el token y péguelo en el botón Authorize."))
				.components(new Components().addSecuritySchemes("bearer",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}

	@Bean
	public GroupedOpenApi todo() {
		return GroupedOpenApi.builder().group("1-todo").displayName("Todo").pathsToMatch("/**").build();
	}

	@Bean
	public GroupedOpenApi publico() {
		return GroupedOpenApi.builder().group("2-publico").displayName("Público y autenticación")
				.pathsToMatch("/api/publico/**", "/auth/**").build();
	}

	@Bean
	public GroupedOpenApi cliente() {
		return GroupedOpenApi.builder().group("3-cliente").displayName("Cliente").pathsToMatch("/api/cliente/**")
				.build();
	}

	@Bean
	public GroupedOpenApi vendedor() {
		return GroupedOpenApi.builder().group("4-vendedor").displayName("Vendedor").pathsToMatch("/api/vendedor/**")
				.build();
	}

	@Bean
	public GroupedOpenApi tecnico() {
		return GroupedOpenApi.builder().group("5-tecnico").displayName("Técnico").pathsToMatch("/api/tecnico/**")
				.build();
	}

	@Bean
	public GroupedOpenApi dueno() {
		return GroupedOpenApi.builder().group("6-dueno").displayName("Dueño").pathsToMatch("/api/admin/**").build();
	}
}

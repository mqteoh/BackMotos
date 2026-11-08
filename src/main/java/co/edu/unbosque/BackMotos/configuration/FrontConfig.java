package co.edu.unbosque.BackMotos.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/** Sirve el front de Angular desde el mismo back. */
@Configuration
public class FrontConfig implements WebMvcConfigurer {

	/** Página de entrada del front. */
	private static final String INDICE = "index.html";

	/** Constructor que usa Spring para crear la configuración. */
	public FrontConfig() {
	}

	/** Entrega los archivos estáticos y, para las rutas del front, el index.html. */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").resourceChain(true)
				.addResolver(new PathResourceResolver() {
					@Override
					protected Resource getResource(String ruta, Resource ubicacion) throws IOException {
						Resource recurso = ubicacion.createRelative(ruta);
						if (recurso.exists() && recurso.isReadable()) {
							return recurso;
						}
						if (!esRutaDelFront(ruta)) {
							return null;
						}
						Resource indice = ubicacion.createRelative(INDICE);
						return indice.exists() && indice.isReadable() ? indice : null;
					}
				});
	}

	/**
	 * Dice si una ruta es del front y no de la API ni un archivo.
	 *
	 * @param ruta ruta pedida, sin la barra inicial
	 * @return true si se responde con la página del front
	 */
	static boolean esRutaDelFront(String ruta) {
		String ultimo = ruta.substring(ruta.lastIndexOf('/') + 1);
		return !ruta.startsWith("api/") && !ruta.startsWith("auth/") && !ultimo.contains(".");
	}
}

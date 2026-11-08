package co.edu.unbosque.BackMotos;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/** Permite desplegar el .war en un servidor de aplicaciones externo. */
public class ServletInitializer extends SpringBootServletInitializer {

	/** Constructor que usa el servidor al desplegar el WAR. */
	public ServletInitializer() {
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BackMotosApplication.class);
	}

}

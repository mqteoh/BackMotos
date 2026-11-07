package co.edu.unbosque.BackMotos.configuration;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Reloj de la aplicación en la hora de Bogotá. */
@Configuration
public class RelojConfig {

	/** Zona horaria de Colombia. */
	public static final ZoneId ZONA = ZoneId.of("America/Bogota");

	/** Constructor que usa Spring para crear la configuración. */
	public RelojConfig() {
	}

	/**
	 * Reloj que usan los servicios; en las pruebas se puede reemplazar.
	 *
	 * @return el reloj del sistema
	 */
	@Bean
	public Clock reloj() {
		return Clock.system(ZONA);
	}
}

package co.edu.unbosque.BackMotos;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.edu.unbosque.BackMotos.configuration.RelojConfig;

/** Punto de entrada del back del concesionario de motos. */
@SpringBootApplication
public class BackMotosApplication {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone(RelojConfig.ZONA));
	}

	/** Constructor que usa Spring al arrancar. */
	public BackMotosApplication() {
	}

	/**
	 * Arranca la aplicación Spring Boot.
	 *
	 * @param args argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackMotosApplication.class, args);
	}

}

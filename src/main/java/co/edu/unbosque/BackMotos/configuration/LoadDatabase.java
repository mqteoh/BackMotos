package co.edu.unbosque.BackMotos.configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/** Revisa la base de datos al arrancar. */
@Configuration
@ConditionalOnProperty(name = "app.verificar-bd", havingValue = "true", matchIfMissing = true)
public class LoadDatabase {

	/** Registro de mensajes de la clase. */
	private static final Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);
	/** Tablas que se cuentan al arrancar. */
	private static final List<String> TABLAS = List.of("marca", "modelo", "sucursal", "moto", "cliente", "empleado",
			"accesorio", "repuesto", "proveedor", "venta_moto", "venta_accesorio", "reparacion", "suministros", "cuenta",
			"reserva");

	/** Constructor que usa Spring para crear la configuración. */
	public LoadDatabase() {
	}

	/**
	 * Muestra en el log el esquema en uso y cuántos registros tiene cada tabla.
	 * Avisa si no hay cuentas para iniciar sesión.
	 *
	 * @param jdbc ejecuta SQL directo en la base
	 * @return la tarea que corre al arrancar
	 */
	@Bean
	public CommandLineRunner verificarBaseDeDatos(JdbcTemplate jdbc) {
		return args -> {
			String version = jdbc.queryForObject("SELECT VERSION()", String.class);
			String esquema = jdbc.queryForObject("SELECT current_schema()", String.class);
			Map<String, Integer> conteos = new LinkedHashMap<>();
			for (String tabla : TABLAS) {
				conteos.put(tabla, jdbc.queryForObject("SELECT COUNT(*) FROM " + tabla, Integer.class));
			}
			LOG.info("Conectado a {} en el esquema {}. Registros: {}", version, esquema, conteos);
			if (conteos.get("cuenta") == 0) {
				LOG.warn("No hay cuentas registradas: ejecute 02_insercion.sql o cree la cuenta del dueño para poder administrar el sistema.");
			}
		};
	}
}

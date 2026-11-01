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

@Configuration
@ConditionalOnProperty(name = "app.verificar-bd", havingValue = "true", matchIfMissing = true)
public class LoadDatabase {

	private static final Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);
	private static final List<String> TABLAS = List.of("marca", "modelo", "sucursal", "moto", "cliente", "empleado",
			"accesorio", "repuesto", "proveedor", "reserva", "venta_moto", "venta_accesorio", "reparacion", "suministro");

	@Bean
	public CommandLineRunner verificarBaseDeDatos(JdbcTemplate jdbc) {
		return args -> {
			String version = jdbc.queryForObject("SELECT VERSION()", String.class);
			String esquema = jdbc.queryForObject("SELECT DATABASE()", String.class);
			Map<String, Integer> conteos = new LinkedHashMap<>();
			for (String tabla : TABLAS) {
				conteos.put(tabla, jdbc.queryForObject("SELECT COUNT(*) FROM " + tabla, Integer.class));
			}
			LOG.info("Conectado a MySQL {} en el esquema {}. Registros: {}", version, esquema, conteos);
			if (conteos.get("empleado") == 0) {
				LOG.warn("No hay empleados registrados: ejecute 02_insercion.sql o cree un dueño para poder administrar el sistema.");
			}
		};
	}
}

package co.edu.unbosque.BackMotos.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import co.edu.unbosque.BackMotos.service.OperacionesAdminService;

/** Tareas que corren solas. */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "app.tareas.habilitadas", havingValue = "true", matchIfMissing = true)
public class TareasProgramadas {

	/** Registro de mensajes de la clase. */
	private static final Logger LOG = LoggerFactory.getLogger(TareasProgramadas.class);

	/** Servicio de las reservas, ventas y reparaciones del dueño. */
	private final OperacionesAdminService operacionesService;

	/**
	 * Recibe las dependencias que inyecta Spring.
	 *
	 * @param operacionesService servicio de las reservas, ventas y reparaciones del dueño
	 */
	public TareasProgramadas(OperacionesAdminService operacionesService) {
		this.operacionesService = operacionesService;
	}

	/** Todos los días a las 12:05 a. m. marca como vencidas las reservas que ya pasaron. */
	@Scheduled(cron = "0 5 0 * * *", zone = "America/Bogota")
	public void vencerReservas() {
		LOG.info("Reservas vencidas esta noche: {}", operacionesService.vencerReservas());
	}
}

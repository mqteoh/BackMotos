package co.edu.unbosque.BackMotos.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import co.edu.unbosque.BackMotos.service.OperacionesAdminService;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "app.tareas.habilitadas", havingValue = "true", matchIfMissing = true)
public class TareasProgramadas {

	private static final Logger LOG = LoggerFactory.getLogger(TareasProgramadas.class);

	private final OperacionesAdminService operacionesService;

	public TareasProgramadas(OperacionesAdminService operacionesService) {
		this.operacionesService = operacionesService;
	}

	@Scheduled(cron = "0 5 0 * * *", zone = "America/Bogota")
	public void vencerReservas() {
		LOG.info("Reservas vencidas esta noche: {}", operacionesService.vencerReservas());
	}
}

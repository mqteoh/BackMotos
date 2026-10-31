package co.edu.unbosque.BackMotos.configuration;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RelojConfig {

	public static final ZoneId ZONA = ZoneId.of("America/Bogota");

	@Bean
	public Clock reloj() {
		return Clock.system(ZONA);
	}
}

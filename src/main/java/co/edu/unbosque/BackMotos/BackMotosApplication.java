package co.edu.unbosque.BackMotos;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.edu.unbosque.BackMotos.configuration.RelojConfig;

@SpringBootApplication
public class BackMotosApplication {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone(RelojConfig.ZONA));
	}

	public static void main(String[] args) {
		SpringApplication.run(BackMotosApplication.class, args);
	}

}

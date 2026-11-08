package co.edu.unbosque.BackMotos.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configura el ModelMapper que copia datos entre entidades y DTO. */
@Configuration
public class ModelMapperConfig {

	/** Constructor que usa Spring para crear la configuración. */
	public ModelMapperConfig() {
	}

	/**
	 * ModelMapper que exige nombres de campos iguales.
	 *
	 * @return el ModelMapper configurado
	 */
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}
}

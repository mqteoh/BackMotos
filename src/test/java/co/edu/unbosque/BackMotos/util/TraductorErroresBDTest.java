package co.edu.unbosque.BackMotos.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import co.edu.unbosque.BackMotos.util.TraductorErroresBD.ErrorBD;

class TraductorErroresBDTest {

	private ErrorBD traducir(SQLException causa) {
		return TraductorErroresBD.traducir(new RuntimeException("envoltura", causa)).orElseThrow();
	}

	@Test
	void reglaDeTriggerConservaSuMensaje() {
		ErrorBD error = traducir(new SQLException("La moto ya fue vendida", "45000", 1644));
		assertThat(error.estado()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(error.mensaje()).isEqualTo("La moto ya fue vendida");
	}

	@Test
	void checkConocidoSeTraduceAMensajeLegible() {
		ErrorBD error = traducir(new SQLException("Check constraint 'ck_reparacion_cierre' is violated.", "HY000", 3819));
		assertThat(error.estado()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(error.mensaje()).contains("finalizar una reparación");
	}

	@Test
	void checkDesconocidoUsaMensajeGeneral() {
		ErrorBD error = traducir(new SQLException("Check constraint 'ck_nueva' is violated.", "HY000", 3819));
		assertThat(error.mensaje()).isEqualTo("La operación no cumple una regla de la base de datos.");
	}

	@Test
	void duplicadoIdentificaLaRestriccionUnique() {
		ErrorBD error = traducir(
				new SQLException("Duplicate entry 'Yamaha' for key 'marca.uq_marca_nombre'", "23000", 1062));
		assertThat(error.estado()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(error.mensaje()).isEqualTo("Ya existe una marca con ese nombre.");
	}

	@Test
	void llavesForaneasDistinguenBorradoDeReferenciaInexistente() {
		assertThat(traducir(new SQLException("Cannot delete or update a parent row", "23000", 1451)).estado())
				.isEqualTo(HttpStatus.CONFLICT);
		assertThat(traducir(new SQLException("Cannot add or update a child row", "23000", 1452)).estado())
				.isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void errorSinCausaSqlNoSeTraduce() {
		assertThat(TraductorErroresBD.traducir(new IllegalStateException("otro"))).isEmpty();
		assertThat(TraductorErroresBD.traducir(new SQLException("Communications link failure", "08S01", 0))).isEmpty();
	}
}

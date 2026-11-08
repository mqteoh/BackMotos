package co.edu.unbosque.BackMotos.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PublicoControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void listadoSinSesionDevuelveSoloLasDoceDisponibles() throws Exception {
		mvc.perform(get("/api/publico/motos").param("tamano", "48")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElementos").value(12)).andExpect(jsonPath("$.contenido", hasSize(12)))
				.andExpect(jsonPath("$.contenido[*].placa", not(hasItem("JKL78D"))))
				.andExpect(jsonPath("$.contenido[*].placa", not(hasItem("LMN24P"))))
				.andExpect(jsonPath("$.contenido[*].placa", not(hasItem("BCD47E"))));
	}

	@Test
	void filtrosReducenElResultadoDeFormaCoherente() throws Exception {
		mvc.perform(get("/api/publico/motos").param("marca", "yamaha")).andExpect(jsonPath("$.totalElementos").value(3))
				.andExpect(jsonPath("$.contenido[*].marca", everyItem(is("Yamaha"))));
		mvc.perform(get("/api/publico/motos").param("tipo", "naked")).andExpect(jsonPath("$.totalElementos").value(4));
		mvc.perform(get("/api/publico/motos").param("tipo", "NAKED").param("precioMax", "11000000"))
				.andExpect(jsonPath("$.contenido[*].placa", containsInAnyOrder("FGH68K", "QRS34A")))
				.andExpect(jsonPath("$.contenido[*].precio", everyItem(lessThanOrEqualTo(11000000.0))));
		mvc.perform(get("/api/publico/motos").param("idSucursal", "1"))
				.andExpect(jsonPath("$.contenido[*].placa", containsInAnyOrder("JKL79D", "GTK22F", "MNP13E")));
		mvc.perform(get("/api/publico/motos").param("texto", "xr")).andExpect(jsonPath("$.totalElementos").value(2));
	}

	@Test
	void ordenYPaginacion() throws Exception {
		mvc.perform(get("/api/publico/motos").param("orden", "PRECIO_ASC").param("tamano", "5"))
				.andExpect(jsonPath("$.contenido", hasSize(5))).andExpect(jsonPath("$.totalPaginas").value(3))
				.andExpect(jsonPath("$.contenido[0].placa").value("CDE35J"));
		mvc.perform(get("/api/publico/motos").param("orden", "PRECIO_DESC"))
				.andExpect(jsonPath("$.contenido[0].placa").value("MNP13E"));
		mvc.perform(get("/api/publico/motos").param("tamano", "5").param("pagina", "2"))
				.andExpect(jsonPath("$.contenido", hasSize(2))).andExpect(jsonPath("$.pagina").value(2));
	}

	@Test
	void parametrosInvalidosResponden400() throws Exception {
		mvc.perform(get("/api/publico/motos").param("precioMin", "20000000").param("precioMax", "1000"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("El precio mínimo no puede ser mayor que el máximo."));
		mvc.perform(get("/api/publico/motos").param("tipo", "VOLADORA")).andExpect(status().isBadRequest());
		mvc.perform(get("/api/publico/motos").param("tamano", "500")).andExpect(status().isBadRequest());
		mvc.perform(get("/api/publico/motos").param("orden", "ALFABETO")).andExpect(status().isBadRequest());
	}

	@Test
	void fichaDeMotoDisponibleYMotosQueNoSeMuestran() throws Exception {
		mvc.perform(get("/api/publico/motos/gtk22f")).andExpect(status().isOk())
				.andExpect(jsonPath("$.placa").value("GTK22F")).andExpect(jsonPath("$.modelo").value("NMAX 155"))
				.andExpect(jsonPath("$.potenciaHp").value(15.1))
				.andExpect(jsonPath("$.sucursal.nombre").value("Sede Chapinero"));
		for (String placa : new String[] { "JKL78D", "LMN24P", "BCD47E", "NOEXISTE" }) {
			mvc.perform(get("/api/publico/motos/" + placa)).andExpect(status().isNotFound());
		}
	}

	@Test
	void opcionesDeFiltroConConteos() throws Exception {
		mvc.perform(get("/api/publico/motos/filtros")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalDisponibles").value(12)).andExpect(jsonPath("$.sedes", hasSize(4)))
				.andExpect(jsonPath("$.marcas[?(@.valor == 'Yamaha')].disponibles").value(3))
				.andExpect(jsonPath("$.tipos[?(@.valor == 'NAKED')].disponibles").value(4))
				.andExpect(jsonPath("$.precioMinimo").value(5600000.0))
				.andExpect(jsonPath("$.precioMaximo").value(32500000.0));
	}

	@Test
	void catalogosBasicosSonPublicos() throws Exception {
		mvc.perform(get("/api/publico/marcas")).andExpect(jsonPath("$", hasSize(8)));
		mvc.perform(get("/api/publico/marcas/1/modelos")).andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].nombreMarca").value("Yamaha"));
		mvc.perform(get("/api/publico/marcas/999/modelos")).andExpect(status().isNotFound());
		mvc.perform(get("/api/publico/accesorios")).andExpect(jsonPath("$", hasSize(10)));
		mvc.perform(get("/api/publico/sucursales")).andExpect(jsonPath("$", hasSize(4)));
	}
}

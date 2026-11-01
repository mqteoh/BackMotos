package co.edu.unbosque.BackMotos.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.unbosque.BackMotos.dto.MotoCatalogoDTO;
import co.edu.unbosque.BackMotos.dto.OrdenCatalogo;
import co.edu.unbosque.BackMotos.dto.PaginaDTO;
import co.edu.unbosque.BackMotos.security.JwtAuthenticationFilter;
import co.edu.unbosque.BackMotos.security.JwtUtil;
import co.edu.unbosque.BackMotos.security.SecurityConfig;
import co.edu.unbosque.BackMotos.service.AccesorioService;
import co.edu.unbosque.BackMotos.service.CatalogoService;
import co.edu.unbosque.BackMotos.service.CatalogoService.FiltroMotos;
import co.edu.unbosque.BackMotos.service.MarcaService;
import co.edu.unbosque.BackMotos.service.ModeloService;
import co.edu.unbosque.BackMotos.service.SucursalService;

@WebMvcTest(PublicoController.class)
@Import({ SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtil.class })
class PublicoControllerWebTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CatalogoService catalogoService;

	@MockBean
	private MarcaService marcaService;

	@MockBean
	private ModeloService modeloService;

	@MockBean
	private AccesorioService accesorioService;

	@MockBean
	private SucursalService sucursalService;

	@Test
	void pasaLosFiltrosAlServicioYDevuelveLaPagina() throws Exception {
		MotoCatalogoDTO moto = new MotoCatalogoDTO("GTK22F", "Yamaha", "NMAX 155", "SCOOTER", 155, 2026, "Gris",
				new BigDecimal("15900000"), 1, "Sede Chapinero", "Bogotá");
		when(catalogoService.buscarMotos(any(), anyInt(), anyInt(), any()))
				.thenReturn(new PaginaDTO<>(List.of(moto), 1, 6, 7, 2));
		mvc.perform(get("/api/publico/motos").param("marca", "Yamaha").param("precioMax", "20000000")
				.param("pagina", "1").param("tamano", "6").param("orden", "PRECIO_DESC")).andExpect(status().isOk())
				.andExpect(jsonPath("$.contenido[0].placa").value("GTK22F")).andExpect(jsonPath("$.totalElementos").value(7));
		ArgumentCaptor<FiltroMotos> filtro = ArgumentCaptor.forClass(FiltroMotos.class);
		verify(catalogoService).buscarMotos(filtro.capture(), eq(1), eq(6), eq(OrdenCatalogo.PRECIO_DESC));
		assertThat(filtro.getValue().marca()).isEqualTo("Yamaha");
		assertThat(filtro.getValue().precioMax()).isEqualByComparingTo("20000000");
	}

	@Test
	void parametroConTipoInvalidoNoLlegaAlServicio() throws Exception {
		mvc.perform(get("/api/publico/motos").param("precioMin", "barato")).andExpect(status().isBadRequest());
		mvc.perform(get("/api/publico/motos").param("orden", "ALFABETICO")).andExpect(status().isBadRequest());
		verify(catalogoService, never()).buscarMotos(any(), anyInt(), anyInt(), any());
	}

	@Test
	void elCatalogoNoExigeSesion() throws Exception {
		when(marcaService.listar()).thenReturn(List.of());
		mvc.perform(get("/api/publico/marcas")).andExpect(status().isOk());
	}
}

package co.edu.unbosque.BackMotos.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VendedorControllerTest {

	@Autowired
	private MockMvc mvc;

	private String laura;
	private String dueno;

	@BeforeEach
	void iniciarSesion() throws Exception {
		laura = token("laura.gomez@concesionario-motos.test", "Motos2026*");
		dueno = token("ricardo.salazar@concesionario-motos.test", "Motos2026*");
	}

	private String token(String correo, String clave) throws Exception {
		String cuerpo = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"" + correo + "\",\"contrasena\":\"" + clave + "\"}")).andReturn().getResponse()
				.getContentAsString();
		return JsonPath.read(cuerpo, "$.token");
	}

	private MockHttpServletRequestBuilder con(MockHttpServletRequestBuilder peticion, String token) {
		return peticion.header(HttpHeaders.AUTHORIZATION, "Bearer " + token).contentType(MediaType.APPLICATION_JSON);
	}

	private String venta(String placa, int idCliente) {
		return "{\"placa\":\"" + placa + "\",\"idCliente\":" + idCliente
				+ ",\"metodoPago\":\"TRANSFERENCIA\",\"tipoFactura\":\"ELECTRONICA\",\"valorTotal\":15500000}";
	}

	private String clienteNuevo(String documento) {
		return "{\"documento\":\"" + documento + "\",\"nombres\":\"Pedro\",\"apellidos\":\"Sede Prueba\","
				+ "\"fechaNacimiento\":\"1990-02-02\",\"ciudad\":\"Bogotá\",\"barrio\":\"Chapinero\",\"calle\":\"Calle 60\","
				+ "\"numeroDireccion\":\"9-10\",\"telefonos\":[\"3005556677\"],\"correos\":[]}";
	}

	@Test
	void buscaYRegistraClientesEnLaSede() throws Exception {
		mvc.perform(con(get("/api/vendedor/clientes").param("documento", "1013654789"), laura))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].nombres").value("María José"));
		mvc.perform(con(get("/api/vendedor/clientes").param("documento", "000"), laura)).andExpect(status().isNotFound());
		mvc.perform(con(get("/api/vendedor/clientes").param("texto", "lopez"), laura))
				.andExpect(jsonPath("$[*].documento", hasItem("1022345678")));
		String cuerpo = mvc.perform(con(post("/api/vendedor/clientes"), laura).content(clienteNuevo("1099777666")))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.tieneCuentaWeb").value(false)).andReturn()
				.getResponse().getContentAsString();
		Integer id = JsonPath.read(cuerpo, "$.idCliente");
		mvc.perform(con(post("/api/vendedor/clientes"), laura).content(clienteNuevo("1099777666")))
				.andExpect(status().isConflict());
		mvc.perform(con(put("/api/vendedor/clientes/" + id), laura)
				.content(clienteNuevo("1099777666").replace("Chapinero", "Rosales"))).andExpect(status().isOk())
				.andExpect(jsonPath("$.barrio").value("Rosales"));
	}

	@Test
	void reservasEInventarioSeLimitanALaSedeDelVendedor() throws Exception {
		mvc.perform(con(get("/api/vendedor/reservas").param("idSucursal", "4"), laura))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("MNP12E")));
		mvc.perform(con(get("/api/vendedor/reservas"), dueno)).andExpect(jsonPath("$", hasSize(3)));
		mvc.perform(con(get("/api/vendedor/reservas").param("idSucursal", "4"), dueno))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("FGH69K")));
		mvc.perform(con(get("/api/vendedor/inventario"), laura))
				.andExpect(jsonPath("$[*].placa", containsInAnyOrder("GTK22F", "JKL79D", "MNP12E", "MNP13E")))
				.andExpect(jsonPath("$[?(@.placa == 'MNP12E')].clienteReserva").value("Juan Sebastián López Medina"));
	}

	@Test
	void ventaDirectaSacaLaMotoDelCatalogo() throws Exception {
		mvc.perform(con(post("/api/vendedor/ventas-moto"), laura).content(venta("gtk22f", 3)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.placa").value("GTK22F"))
				.andExpect(jsonPath("$.vendedor").value("Laura Gómez Torres")).andExpect(jsonPath("$.idReserva").doesNotExist());
		mvc.perform(get("/api/publico/motos/GTK22F")).andExpect(status().isNotFound());
		mvc.perform(con(post("/api/vendedor/ventas-moto"), laura).content(venta("GTK22F", 3)))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje").value("La moto GTK22F ya fue vendida."));
	}

	@Test
	void ventaAlClienteQueReservoConcretaSuReserva() throws Exception {
		mvc.perform(con(post("/api/vendedor/ventas-moto"), laura).content(venta("MNP12E", 3)))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("reservada por otro cliente")));
		mvc.perform(con(post("/api/vendedor/ventas-moto"), laura).content(venta("MNP12E", 2)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.idReserva").value(8));
		mvc.perform(con(get("/api/vendedor/reservas"), laura)).andExpect(jsonPath("$", hasSize(0)));
		String juan = token("juan.lopez@correo.test", "Cliente2026*");
		mvc.perform(con(get("/api/cliente/reservas"), juan))
				.andExpect(jsonPath("$[?(@.idReserva == 8)].estado").value("CONCRETADA"));
	}

	@Test
	void noSeVendeMotoDeOtraSedeYElDuenoNoRegistraVentas() throws Exception {
		mvc.perform(con(post("/api/vendedor/ventas-moto"), laura).content(venta("QRS34A", 3)))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.mensaje", containsString("Sede Suba")));
		mvc.perform(con(post("/api/vendedor/ventas-moto"), dueno).content(venta("GTK22F", 3)))
				.andExpect(status().isForbidden());
		mvc.perform(con(post("/api/vendedor/ventas-moto"), laura).content(venta("GTK22F", 3).replace("15500000", "-1")))
				.andExpect(status().isBadRequest());
	}

	@Test
	void ventaDeAccesoriosEnUnaSolaTransaccion() throws Exception {
		mvc.perform(con(post("/api/vendedor/ventas-accesorio"), laura).content(
				"{\"idCliente\":11,\"metodoPago\":\"EFECTIVO\",\"tipoFactura\":\"POS\",\"items\":[{\"idAccesorio\":4,\"cantidad\":1},"
						+ "{\"idAccesorio\":8,\"cantidad\":2,\"precioUnitario\":280000}]}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.valorTotal").value(749000.0))
				.andExpect(jsonPath("$.items[0].precioUnitario").value(189000.0));
		mvc.perform(con(post("/api/vendedor/ventas-accesorio"), laura).content(
				"{\"idCliente\":11,\"metodoPago\":\"EFECTIVO\",\"tipoFactura\":\"POS\",\"items\":[{\"idAccesorio\":4,\"cantidad\":1},"
						+ "{\"idAccesorio\":999,\"cantidad\":1}]}"))
				.andExpect(status().isNotFound());
		mvc.perform(con(post("/api/vendedor/ventas-accesorio"), laura).content(
				"{\"idCliente\":11,\"metodoPago\":\"EFECTIVO\",\"tipoFactura\":\"POS\",\"items\":[{\"idAccesorio\":4,\"cantidad\":1},"
						+ "{\"idAccesorio\":4,\"cantidad\":1}]}"))
				.andExpect(status().isBadRequest());
		mvc.perform(con(post("/api/vendedor/ventas-accesorio"), laura)
				.content("{\"idCliente\":11,\"metodoPago\":\"EFECTIVO\",\"tipoFactura\":\"POS\",\"items\":[]}"))
				.andExpect(status().isBadRequest());
		mvc.perform(con(get("/api/vendedor/ventas-accesorio"), laura)).andExpect(jsonPath("$", hasSize(4)));
	}

	@Test
	void cadaVendedorVeSusVentasYElDuenoTodas() throws Exception {
		mvc.perform(con(get("/api/vendedor/ventas-moto"), laura)).andExpect(jsonPath("$", hasSize(3)));
		mvc.perform(con(get("/api/vendedor/ventas-moto"), dueno)).andExpect(jsonPath("$", hasSize(13)));
		String maria = token("maria.ramirez@correo.test", "Cliente2026*");
		mvc.perform(con(get("/api/vendedor/inventario"), maria)).andExpect(status().isForbidden());
	}
}

package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.util.List;

public record FiltrosCatalogoDTO(List<Opcion> marcas, List<Opcion> tipos, List<OpcionSede> sedes,
		BigDecimal precioMinimo, BigDecimal precioMaximo, long totalDisponibles) {

	public record Opcion(String valor, long disponibles) {
	}

	public record OpcionSede(Integer idSucursal, String nombre, String ciudad, long disponibles) {
	}
}

package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Opciones para los filtros del catálogo, con cuántas motos hay en cada una.
 *
 * @param marcas marcas con motos disponibles
 * @param tipos tipos de moto con motos disponibles
 * @param sedes sedes con motos disponibles
 * @param precioMinimo precio de la moto disponible más barata
 * @param precioMaximo precio de la moto disponible más cara
 * @param totalDisponibles número total de motos disponibles
 */
public record FiltrosCatalogoDTO(List<Opcion> marcas, List<Opcion> tipos, List<OpcionSede> sedes,
		BigDecimal precioMinimo, BigDecimal precioMaximo, long totalDisponibles) {

	/**
	 * Valor del filtro y motos disponibles con ese valor.
	 *
	 * @param valor valor de la opción, por ejemplo la marca o el tipo
	 * @param disponibles motos disponibles con esta opción
	 */
	public record Opcion(String valor, long disponibles) {
	}

	/**
	 * Sede del filtro y motos disponibles en ella.
	 *
	 * @param idSucursal identificador de la sede
	 * @param nombre nombre de la sede
	 * @param ciudad ciudad
	 * @param disponibles motos disponibles con esta opción
	 */
	public record OpcionSede(Integer idSucursal, String nombre, String ciudad, long disponibles) {
	}
}

package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import co.edu.unbosque.BackMotos.entity.MotoDisponible;

/**
 * Moto disponible tal como se muestra en el catálogo público.
 *
 * @param placa placa de la moto
 * @param marca nombre de la marca
 * @param modelo nombre del modelo
 * @param tipoMoto tipo de moto
 * @param cilindraje cilindraje del modelo en centímetros cúbicos
 * @param anio año del modelo de la moto
 * @param color color de la moto
 * @param precio precio de lista
 * @param idSucursal identificador de la sede
 * @param sucursal nombre de la sede
 * @param ciudad ciudad
 */
public record MotoCatalogoDTO(String placa, String marca, String modelo, String tipoMoto, Integer cilindraje,
		Integer anio, String color, BigDecimal precio, Integer idSucursal, String sucursal, String ciudad) {

	/**
	 * Arma el DTO desde la vista de motos disponibles.
	 *
	 * @param moto moto de la vista de disponibles
	 * @return la moto para el catálogo
	 */
	public static MotoCatalogoDTO de(MotoDisponible moto) {
		return new MotoCatalogoDTO(moto.getPlaca(), moto.getMarca(), moto.getModelo(), moto.getTipoMoto(),
				moto.getCilindraje(), moto.getAnio(), moto.getColor(), moto.getPrecio(), moto.getIdSucursal(),
				moto.getSucursal(), moto.getCiudad());
	}
}

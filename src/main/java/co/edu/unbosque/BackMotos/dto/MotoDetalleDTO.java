package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.Moto;

/**
 * Ficha completa de una moto del catálogo.
 *
 * @param placa placa de la moto
 * @param idMarca identificador de la marca
 * @param marca nombre de la marca
 * @param paisOrigenMarca país de origen de la marca
 * @param nroModelo número del modelo dentro de la marca
 * @param modelo nombre del modelo
 * @param tipoMoto tipo de moto
 * @param cilindraje cilindraje del modelo en centímetros cúbicos
 * @param potenciaHp potencia en caballos de fuerza
 * @param capacidadTanque capacidad del tanque en litros
 * @param anio año del modelo de la moto
 * @param color color de la moto
 * @param kilometraje kilómetros recorridos
 * @param precio precio de lista
 * @param sucursal nombre de la sede
 */
public record MotoDetalleDTO(String placa, Integer idMarca, String marca, String paisOrigenMarca, Integer nroModelo,
		String modelo, String tipoMoto, Integer cilindraje, BigDecimal potenciaHp, BigDecimal capacidadTanque,
		Integer anio, String color, Integer kilometraje, BigDecimal precio, SucursalDTO sucursal) {

	/**
	 * Arma la ficha con la moto y su sede.
	 *
	 * @param moto moto que se convierte
	 * @param sucursal sede de la moto
	 * @return la ficha de la moto
	 */
	public static MotoDetalleDTO de(Moto moto, SucursalDTO sucursal) {
		Modelo modelo = moto.getModelo();
		return new MotoDetalleDTO(moto.getPlaca(), modelo.getId().getIdMarca(), modelo.getMarca().getNombre(),
				modelo.getMarca().getPaisOrigen(), modelo.getId().getNroModelo(), modelo.getNombre(),
				modelo.getTipoMoto(), modelo.getCilindraje(), modelo.getPotenciaHp(), modelo.getCapacidadTanque(),
				moto.getAnio(), moto.getColor(), moto.getKilometraje(), moto.getPrecio(), sucursal);
	}
}

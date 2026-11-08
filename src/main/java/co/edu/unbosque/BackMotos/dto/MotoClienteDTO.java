package co.edu.unbosque.BackMotos.dto;

import co.edu.unbosque.BackMotos.entity.Moto;

/**
 * Moto comprada por el cliente.
 *
 * @param placa placa de la moto
 * @param marca nombre de la marca
 * @param modelo nombre del modelo
 * @param anio año del modelo de la moto
 * @param color color de la moto
 */
public record MotoClienteDTO(String placa, String marca, String modelo, Integer anio, String color) {

	/**
	 * Arma el DTO desde la entidad.
	 *
	 * @param moto moto que se convierte
	 * @return el DTO de la moto
	 */
	public static MotoClienteDTO de(Moto moto) {
		return new MotoClienteDTO(moto.getPlaca(), moto.getModelo().getMarca().getNombre(),
				moto.getModelo().getNombre(), moto.getAnio(), moto.getColor());
	}
}

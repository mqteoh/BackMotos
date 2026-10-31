package co.edu.unbosque.BackMotos.dto;

import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;

public record MotoClienteDTO(String placa, String marca, String modelo, Integer anio, String color,
		OrigenMoto origen) {

	public static MotoClienteDTO de(Moto moto) {
		return new MotoClienteDTO(moto.getPlaca(), moto.getModelo().getMarca().getNombre(),
				moto.getModelo().getNombre(), moto.getAnio(), moto.getColor(), moto.getOrigen());
	}
}

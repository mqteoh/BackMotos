package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import co.edu.unbosque.BackMotos.entity.MotoDisponible;

public record MotoCatalogoDTO(String placa, String marca, String modelo, String tipoMoto, Integer cilindraje,
		Integer anio, String color, BigDecimal precio, Integer idSucursal, String sucursal, String ciudad) {

	public static MotoCatalogoDTO de(MotoDisponible moto) {
		return new MotoCatalogoDTO(moto.getPlaca(), moto.getMarca(), moto.getModelo(), moto.getTipoMoto(),
				moto.getCilindraje(), moto.getAnio(), moto.getColor(), moto.getPrecio(), moto.getIdSucursal(),
				moto.getSucursal(), moto.getCiudad());
	}
}

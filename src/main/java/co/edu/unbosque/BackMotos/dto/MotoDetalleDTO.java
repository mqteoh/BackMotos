package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.Moto;

public record MotoDetalleDTO(String placa, Integer idMarca, String marca, String paisOrigenMarca, Integer nroModelo,
		String modelo, String tipoMoto, Integer cilindraje, BigDecimal potenciaHp, BigDecimal capacidadTanque,
		Integer anio, String color, Integer kilometraje, BigDecimal precio, SucursalDTO sucursal) {

	public static MotoDetalleDTO de(Moto moto, SucursalDTO sucursal) {
		Modelo modelo = moto.getModelo();
		return new MotoDetalleDTO(moto.getPlaca(), modelo.getId().getIdMarca(), modelo.getMarca().getNombre(),
				modelo.getMarca().getPaisOrigen(), modelo.getId().getNroModelo(), modelo.getNombre(),
				modelo.getTipoMoto(), modelo.getCilindraje(), modelo.getPotenciaHp(), modelo.getCapacidadTanque(),
				moto.getAnio(), moto.getColor(), moto.getKilometraje(), moto.getPrecio(), sucursal);
	}
}

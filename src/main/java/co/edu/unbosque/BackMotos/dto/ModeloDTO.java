package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ModeloDTO {

	@NotNull(message = "La marca es obligatoria.")
	private Integer idMarca;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer nroModelo;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String nombreMarca;

	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	@NotNull(message = "El cilindraje es obligatorio.")
	@Min(value = 50, message = "El cilindraje mínimo es 50 cc.")
	@Max(value = 2500, message = "El cilindraje máximo es 2500 cc.")
	private Integer cilindraje;

	@NotBlank(message = "El tipo de moto es obligatorio.")
	@Pattern(regexp = "CALLE|DEPORTIVA|NAKED|SCOOTER|ENDURO|TOURING|DOBLE_PROPOSITO", message = "El tipo de moto debe ser CALLE, DEPORTIVA, NAKED, SCOOTER, ENDURO, TOURING o DOBLE_PROPOSITO.")
	private String tipoMoto;

	@NotNull(message = "La potencia es obligatoria.")
	@DecimalMin(value = "0.1", message = "La potencia debe ser mayor que cero.")
	@Digits(integer = 4, fraction = 1, message = "La potencia admite hasta 4 enteros y 1 decimal.")
	private BigDecimal potenciaHp;

	@NotNull(message = "La capacidad del tanque es obligatoria.")
	@DecimalMin(value = "0.1", message = "La capacidad del tanque debe ser mayor que cero.")
	@Digits(integer = 3, fraction = 1, message = "La capacidad del tanque admite hasta 3 enteros y 1 decimal.")
	private BigDecimal capacidadTanque;

	public ModeloDTO() {
	}

	public Integer getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(Integer idMarca) {
		this.idMarca = idMarca;
	}

	public Integer getNroModelo() {
		return nroModelo;
	}

	public void setNroModelo(Integer nroModelo) {
		this.nroModelo = nroModelo;
	}

	public String getNombreMarca() {
		return nombreMarca;
	}

	public void setNombreMarca(String nombreMarca) {
		this.nombreMarca = nombreMarca;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public void setCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
	}

	public String getTipoMoto() {
		return tipoMoto;
	}

	public void setTipoMoto(String tipoMoto) {
		this.tipoMoto = tipoMoto;
	}

	public BigDecimal getPotenciaHp() {
		return potenciaHp;
	}

	public void setPotenciaHp(BigDecimal potenciaHp) {
		this.potenciaHp = potenciaHp;
	}

	public BigDecimal getCapacidadTanque() {
		return capacidadTanque;
	}

	public void setCapacidadTanque(BigDecimal capacidadTanque) {
		this.capacidadTanque = capacidadTanque;
	}
}

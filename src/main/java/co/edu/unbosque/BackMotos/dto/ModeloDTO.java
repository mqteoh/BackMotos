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

/** Datos de un modelo con su marca. */
public class ModeloDTO {

	/** Identificador de la marca. */
	@NotNull(message = "La marca es obligatoria.")
	private Integer idMarca;

	/** Número del modelo dentro de la marca. */
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer nroModelo;

	/** Nombre de la marca. */
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String nombreMarca;

	/** Nombre del modelo. */
	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	/** Cilindraje del modelo en centímetros cúbicos. */
	@NotNull(message = "El cilindraje es obligatorio.")
	@Min(value = 50, message = "El cilindraje mínimo es 50 cc.")
	@Max(value = 2500, message = "El cilindraje máximo es 2500 cc.")
	private Integer cilindraje;

	/** Tipo de moto. */
	@NotBlank(message = "El tipo de moto es obligatorio.")
	@Pattern(regexp = "CALLE|DEPORTIVA|NAKED|SCOOTER|ENDURO|TOURING|DOBLE_PROPOSITO", message = "El tipo de moto debe ser CALLE, DEPORTIVA, NAKED, SCOOTER, ENDURO, TOURING o DOBLE_PROPOSITO.")
	private String tipoMoto;

	/** Potencia en caballos de fuerza. */
	@NotNull(message = "La potencia es obligatoria.")
	@DecimalMin(value = "0.1", message = "La potencia debe ser mayor que cero.")
	@Digits(integer = 4, fraction = 1, message = "La potencia admite hasta 4 enteros y 1 decimal.")
	private BigDecimal potenciaHp;

	/** Capacidad del tanque en litros. */
	@NotNull(message = "La capacidad del tanque es obligatoria.")
	@DecimalMin(value = "0.1", message = "La capacidad del tanque debe ser mayor que cero.")
	@Digits(integer = 3, fraction = 1, message = "La capacidad del tanque admite hasta 3 enteros y 1 decimal.")
	private BigDecimal capacidadTanque;

	/** Crea un DTO vacío; los datos se llenan con los setters. */
	public ModeloDTO() {
	}

	/**
	 * Devuelve el identificador de la marca.
	 *
	 * @return el identificador de la marca
	 */
	public Integer getIdMarca() {
		return idMarca;
	}

	/**
	 * Cambia el identificador de la marca.
	 *
	 * @param idMarca el identificador de la marca
	 */
	public void setIdMarca(Integer idMarca) {
		this.idMarca = idMarca;
	}

	/**
	 * Devuelve el número del modelo dentro de la marca.
	 *
	 * @return el número del modelo dentro de la marca
	 */
	public Integer getNroModelo() {
		return nroModelo;
	}

	/**
	 * Cambia el número del modelo dentro de la marca.
	 *
	 * @param nroModelo el número del modelo dentro de la marca
	 */
	public void setNroModelo(Integer nroModelo) {
		this.nroModelo = nroModelo;
	}

	/**
	 * Devuelve el nombre de la marca.
	 *
	 * @return el nombre de la marca
	 */
	public String getNombreMarca() {
		return nombreMarca;
	}

	/**
	 * Cambia el nombre de la marca.
	 *
	 * @param nombreMarca el nombre de la marca
	 */
	public void setNombreMarca(String nombreMarca) {
		this.nombreMarca = nombreMarca;
	}

	/**
	 * Devuelve el nombre del modelo.
	 *
	 * @return el nombre del modelo
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre del modelo.
	 *
	 * @param nombre el nombre del modelo
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el cilindraje del modelo en centímetros cúbicos.
	 *
	 * @return el cilindraje del modelo en centímetros cúbicos
	 */
	public Integer getCilindraje() {
		return cilindraje;
	}

	/**
	 * Cambia el cilindraje del modelo en centímetros cúbicos.
	 *
	 * @param cilindraje el cilindraje del modelo en centímetros cúbicos
	 */
	public void setCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
	}

	/**
	 * Devuelve el tipo de moto.
	 *
	 * @return el tipo de moto
	 */
	public String getTipoMoto() {
		return tipoMoto;
	}

	/**
	 * Cambia el tipo de moto.
	 *
	 * @param tipoMoto el tipo de moto
	 */
	public void setTipoMoto(String tipoMoto) {
		this.tipoMoto = tipoMoto;
	}

	/**
	 * Devuelve la potencia en caballos de fuerza.
	 *
	 * @return la potencia en caballos de fuerza
	 */
	public BigDecimal getPotenciaHp() {
		return potenciaHp;
	}

	/**
	 * Cambia la potencia en caballos de fuerza.
	 *
	 * @param potenciaHp la potencia en caballos de fuerza
	 */
	public void setPotenciaHp(BigDecimal potenciaHp) {
		this.potenciaHp = potenciaHp;
	}

	/**
	 * Devuelve la capacidad del tanque en litros.
	 *
	 * @return la capacidad del tanque en litros
	 */
	public BigDecimal getCapacidadTanque() {
		return capacidadTanque;
	}

	/**
	 * Cambia la capacidad del tanque en litros.
	 *
	 * @param capacidadTanque la capacidad del tanque en litros
	 */
	public void setCapacidadTanque(BigDecimal capacidadTanque) {
		this.capacidadTanque = capacidadTanque;
	}
}

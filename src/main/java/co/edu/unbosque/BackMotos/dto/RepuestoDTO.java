package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos de un repuesto. */
public class RepuestoDTO {

	/** Identificador del repuesto. */
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idRepuesto;

	/** Nombre del repuesto. */
	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	/** Categoría del repuesto. */
	@NotBlank(message = "La categoría es obligatoria.")
	@Size(max = 40, message = "La categoría admite máximo 40 caracteres.")
	private String categoria;

	/** Precio de lista. */
	@NotNull(message = "El precio es obligatorio.")
	@Positive(message = "El precio debe ser mayor que cero.")
	@Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.")
	private BigDecimal precio;

	/** Crea un DTO vacío; los datos se llenan con los setters. */
	public RepuestoDTO() {
	}

	/**
	 * Devuelve el identificador del repuesto.
	 *
	 * @return el identificador del repuesto
	 */
	public Integer getIdRepuesto() {
		return idRepuesto;
	}

	/**
	 * Cambia el identificador del repuesto.
	 *
	 * @param idRepuesto el identificador del repuesto
	 */
	public void setIdRepuesto(Integer idRepuesto) {
		this.idRepuesto = idRepuesto;
	}

	/**
	 * Devuelve el nombre del repuesto.
	 *
	 * @return el nombre del repuesto
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre del repuesto.
	 *
	 * @param nombre el nombre del repuesto
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la categoría del repuesto.
	 *
	 * @return la categoría del repuesto
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * Cambia la categoría del repuesto.
	 *
	 * @param categoria la categoría del repuesto
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * Devuelve el precio de lista.
	 *
	 * @return el precio de lista
	 */
	public BigDecimal getPrecio() {
		return precio;
	}

	/**
	 * Cambia el precio de lista.
	 *
	 * @param precio el precio de lista
	 */
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
}

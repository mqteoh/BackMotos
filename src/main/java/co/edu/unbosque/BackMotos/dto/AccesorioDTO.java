package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos de un accesorio para el catálogo y el panel del dueño. */
public class AccesorioDTO {

	/** Identificador del accesorio. */
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idAccesorio;

	/** Nombre del accesorio. */
	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	/** Marca del accesorio. */
	@NotBlank(message = "La marca es obligatoria.")
	@Size(max = 60, message = "La marca admite máximo 60 caracteres.")
	private String marca;

	/** Descripción del accesorio. */
	@Size(max = 255, message = "La descripción admite máximo 255 caracteres.")
	private String descripcion;

	/** Precio de lista. */
	@NotNull(message = "El precio es obligatorio.")
	@Positive(message = "El precio debe ser mayor que cero.")
	@Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.")
	private BigDecimal precio;

	/** Crea un DTO vacío; los datos se llenan con los setters. */
	public AccesorioDTO() {
	}

	/**
	 * Devuelve el identificador del accesorio.
	 *
	 * @return el identificador del accesorio
	 */
	public Integer getIdAccesorio() {
		return idAccesorio;
	}

	/**
	 * Cambia el identificador del accesorio.
	 *
	 * @param idAccesorio el identificador del accesorio
	 */
	public void setIdAccesorio(Integer idAccesorio) {
		this.idAccesorio = idAccesorio;
	}

	/**
	 * Devuelve el nombre del accesorio.
	 *
	 * @return el nombre del accesorio
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre del accesorio.
	 *
	 * @param nombre el nombre del accesorio
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la marca del accesorio.
	 *
	 * @return la marca del accesorio
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * Cambia la marca del accesorio.
	 *
	 * @param marca la marca del accesorio
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * Devuelve la descripción del accesorio.
	 *
	 * @return la descripción del accesorio
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Cambia la descripción del accesorio.
	 *
	 * @param descripcion la descripción del accesorio
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

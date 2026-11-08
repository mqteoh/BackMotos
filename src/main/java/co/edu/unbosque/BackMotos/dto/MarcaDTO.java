package co.edu.unbosque.BackMotos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos de una marca. */
public class MarcaDTO {

	/** Identificador de la marca. */
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idMarca;

	/** Nombre de la marca. */
	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 60, message = "El nombre admite máximo 60 caracteres.")
	private String nombre;

	/** País de origen de la marca. */
	@NotBlank(message = "El país de origen es obligatorio.")
	@Size(max = 60, message = "El país de origen admite máximo 60 caracteres.")
	private String paisOrigen;

	/** Crea un DTO vacío; los datos se llenan con los setters. */
	public MarcaDTO() {
	}

	/**
	 * Crea la marca con sus datos.
	 *
	 * @param nombre nombre de la marca
	 * @param paisOrigen país de origen de la marca
	 */
	public MarcaDTO(String nombre, String paisOrigen) {
		this.nombre = nombre;
		this.paisOrigen = paisOrigen;
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
	 * Devuelve el nombre de la marca.
	 *
	 * @return el nombre de la marca
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre de la marca.
	 *
	 * @param nombre el nombre de la marca
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el país de origen de la marca.
	 *
	 * @return el país de origen de la marca
	 */
	public String getPaisOrigen() {
		return paisOrigen;
	}

	/**
	 * Cambia el país de origen de la marca.
	 *
	 * @param paisOrigen el país de origen de la marca
	 */
	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}
}

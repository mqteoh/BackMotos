package co.edu.unbosque.BackMotos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos de una sede. */
public class SucursalDTO {

	/** Identificador de la sede. */
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idSucursal;

	/** Nombre de la sede. */
	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	/** Ciudad de la sede. */
	@NotBlank(message = "La ciudad es obligatoria.")
	@Size(max = 60, message = "La ciudad admite máximo 60 caracteres.")
	private String ciudad;

	/** Barrio de la dirección. */
	@NotBlank(message = "El barrio es obligatorio.")
	@Size(max = 60, message = "El barrio admite máximo 60 caracteres.")
	private String barrio;

	/** Calle de la dirección. */
	@NotBlank(message = "La calle es obligatoria.")
	@Size(max = 60, message = "La calle admite máximo 60 caracteres.")
	private String calle;

	/** Crea un DTO vacío; los datos se llenan con los setters. */
	public SucursalDTO() {
	}

	/**
	 * Devuelve el identificador de la sede.
	 *
	 * @return el identificador de la sede
	 */
	public Integer getIdSucursal() {
		return idSucursal;
	}

	/**
	 * Cambia el identificador de la sede.
	 *
	 * @param idSucursal el identificador de la sede
	 */
	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	/**
	 * Devuelve el nombre de la sede.
	 *
	 * @return el nombre de la sede
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre de la sede.
	 *
	 * @param nombre el nombre de la sede
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la ciudad de la sede.
	 *
	 * @return la ciudad de la sede
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * Cambia la ciudad de la sede.
	 *
	 * @param ciudad la ciudad de la sede
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * Devuelve el barrio de la dirección.
	 *
	 * @return el barrio de la dirección
	 */
	public String getBarrio() {
		return barrio;
	}

	/**
	 * Cambia el barrio de la dirección.
	 *
	 * @param barrio el barrio de la dirección
	 */
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	/**
	 * Devuelve la calle de la dirección.
	 *
	 * @return la calle de la dirección
	 */
	public String getCalle() {
		return calle;
	}

	/**
	 * Cambia la calle de la dirección.
	 *
	 * @param calle la calle de la dirección
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}
}

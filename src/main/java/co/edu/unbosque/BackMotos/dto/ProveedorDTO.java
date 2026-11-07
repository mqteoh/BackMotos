package co.edu.unbosque.BackMotos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos de un proveedor. */
public class ProveedorDTO {

	/** NIT del proveedor. */
	@NotBlank(message = "El NIT es obligatorio.")
	@Pattern(regexp = "^[0-9]{6,12}-[0-9]$", message = "El NIT debe tener el formato 000000000-0.")
	private String nit;

	/** Nombre del proveedor. */
	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 100, message = "El nombre admite máximo 100 caracteres.")
	private String nombre;

	/** Teléfono del proveedor. */
	@NotBlank(message = "El teléfono es obligatorio.")
	@Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos.")
	private String telefono;

	/** Correo del proveedor. */
	@NotBlank(message = "El correo es obligatorio.")
	@Email(message = "El correo no es válido.")
	@Size(max = 120, message = "El correo admite máximo 120 caracteres.")
	private String correo;

	/** Crea un DTO vacío; los datos se llenan con los setters. */
	public ProveedorDTO() {
	}

	/**
	 * Devuelve el NIT del proveedor.
	 *
	 * @return el NIT del proveedor
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * Cambia el NIT del proveedor.
	 *
	 * @param nit el NIT del proveedor
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * Devuelve el nombre del proveedor.
	 *
	 * @return el nombre del proveedor
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre del proveedor.
	 *
	 * @param nombre el nombre del proveedor
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el teléfono del proveedor.
	 *
	 * @return el teléfono del proveedor
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Cambia el teléfono del proveedor.
	 *
	 * @param telefono el teléfono del proveedor
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Devuelve el correo del proveedor.
	 *
	 * @return el correo del proveedor
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * Cambia el correo del proveedor.
	 *
	 * @param correo el correo del proveedor
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}
}

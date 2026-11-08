package co.edu.unbosque.BackMotos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/** Datos que comparten clientes y empleados: documento, nombres y apellidos. */
@MappedSuperclass
public abstract class Persona {

	/** Número de documento de identidad. */
	@Column(name = "documento", nullable = false, length = 15, unique = true)
	private String documento;

	/** Nombres de la persona. */
	@Column(name = "nombres", nullable = false, length = 60)
	private String nombres;

	/** Apellidos de la persona. */
	@Column(name = "apellidos", nullable = false, length = 60)
	private String apellidos;

	/** Constructor que usan las subclases y JPA. */
	protected Persona() {
	}

	/**
	 * Devuelve el número de documento de identidad.
	 *
	 * @return el número de documento de identidad
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * Cambia el número de documento de identidad.
	 *
	 * @param documento el número de documento de identidad
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * Devuelve los nombres de la persona.
	 *
	 * @return los nombres de la persona
	 */
	public String getNombres() {
		return nombres;
	}

	/**
	 * Cambia los nombres de la persona.
	 *
	 * @param nombres los nombres de la persona
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	/**
	 * Devuelve los apellidos de la persona.
	 *
	 * @return los apellidos de la persona
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * Cambia los apellidos de la persona.
	 *
	 * @param apellidos los apellidos de la persona
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * Devuelve los nombres y apellidos.
	 *
	 * @return los nombres y apellidos
	 */
	public String getNombreCompleto() {
		return nombres + " " + apellidos;
	}
}

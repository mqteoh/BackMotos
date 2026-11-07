package co.edu.unbosque.BackMotos.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Proveedor de repuestos (tabla proveedor). */
@Entity
@Table(name = "proveedor")
public class Proveedor {

	/** NIT del proveedor. */
	@Id
	@Column(name = "nit", length = 15)
	private String nit;

	/** Nombre del proveedor. */
	@Column(name = "nombre", nullable = false, length = 100, unique = true)
	private String nombre;

	/** Teléfono del proveedor. */
	@Column(name = "telefono", nullable = false, length = 15)
	private String telefono;

	/** Correo del proveedor. */
	@Column(name = "correo", nullable = false, length = 120)
	private String correo;

	/** Constructor vacío que necesita JPA. */
	public Proveedor() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Proveedor otro)) {
			return false;
		}
		return nit != null && nit.equals(otro.nit);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nit);
	}
}

package co.edu.unbosque.BackMotos.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Sede del concesionario (tabla sucursal). */
@Entity
@Table(name = "sucursal")
public class Sucursal {

	/** Identificador de la sede. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sucursal")
	private Integer idSucursal;

	/** Nombre de la sede. */
	@Column(name = "nombre", nullable = false, length = 80, unique = true)
	private String nombre;

	/** Ciudad de la sede. */
	@Column(name = "ciudad", nullable = false, length = 60)
	private String ciudad;

	/** Barrio de la dirección. */
	@Column(name = "barrio", nullable = false, length = 60)
	private String barrio;

	/** Calle de la dirección. */
	@Column(name = "calle", nullable = false, length = 60)
	private String calle;

	/** Constructor vacío que necesita JPA. */
	public Sucursal() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Sucursal otra)) {
			return false;
		}
		return idSucursal != null && idSucursal.equals(otra.idSucursal);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idSucursal);
	}
}

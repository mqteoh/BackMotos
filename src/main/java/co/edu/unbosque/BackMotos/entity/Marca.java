package co.edu.unbosque.BackMotos.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Marca de motos (tabla marca). */
@Entity
@Table(name = "marca")
public class Marca {

	/** Identificador de la marca. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_marca")
	private Integer idMarca;

	/** Nombre de la marca. */
	@Column(name = "nombre", nullable = false, length = 60, unique = true)
	private String nombre;

	/** País de origen de la marca. */
	@Column(name = "pais_origen", nullable = false, length = 60)
	private String paisOrigen;

	/** Constructor vacío que necesita JPA. */
	public Marca() {
	}

	/**
	 * Crea la marca con sus datos.
	 *
	 * @param nombre nombre de la marca
	 * @param paisOrigen país de origen de la marca
	 */
	public Marca(String nombre, String paisOrigen) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Marca otra)) {
			return false;
		}
		return idMarca != null && idMarca.equals(otra.idMarca);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idMarca);
	}
}

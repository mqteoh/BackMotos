package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Llave del modelo: marca y número del modelo dentro de la marca. */
@Embeddable
public class ModeloId implements Serializable {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/** Identificador de la marca. */
	@Column(name = "id_marca")
	private Integer idMarca;

	/** Número del modelo dentro de la marca. */
	@Column(name = "id_modelo")
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer nroModelo;

	/** Constructor vacío que necesita JPA. */
	public ModeloId() {
	}

	/**
	 * Crea la llave del modelo con sus datos.
	 *
	 * @param idMarca identificador de la marca
	 * @param nroModelo número del modelo dentro de la marca
	 */
	public ModeloId(Integer idMarca, Integer nroModelo) {
		this.idMarca = idMarca;
		this.nroModelo = nroModelo;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ModeloId otro)) {
			return false;
		}
		return Objects.equals(idMarca, otro.idMarca) && Objects.equals(nroModelo, otro.nroModelo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idMarca, nroModelo);
	}
}

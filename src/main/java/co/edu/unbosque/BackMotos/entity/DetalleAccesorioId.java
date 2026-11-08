package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Llave del detalle: venta y accesorio. */
@Embeddable
public class DetalleAccesorioId implements Serializable {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/** Identificador de la venta de accesorios. */
	@Column(name = "id_venta")
	private Integer idVentaAccesorio;

	/** Identificador del accesorio. */
	@Column(name = "id_accesorio")
	private Integer idAccesorio;

	/** Constructor vacío que necesita JPA. */
	public DetalleAccesorioId() {
	}

	/**
	 * Crea la llave de la línea con sus datos.
	 *
	 * @param idVentaAccesorio identificador de la venta de accesorios
	 * @param idAccesorio identificador del accesorio
	 */
	public DetalleAccesorioId(Integer idVentaAccesorio, Integer idAccesorio) {
		this.idVentaAccesorio = idVentaAccesorio;
		this.idAccesorio = idAccesorio;
	}

	/**
	 * Devuelve el identificador de la venta de accesorios.
	 *
	 * @return el identificador de la venta de accesorios
	 */
	public Integer getIdVentaAccesorio() {
		return idVentaAccesorio;
	}

	/**
	 * Cambia el identificador de la venta de accesorios.
	 *
	 * @param idVentaAccesorio el identificador de la venta de accesorios
	 */
	public void setIdVentaAccesorio(Integer idVentaAccesorio) {
		this.idVentaAccesorio = idVentaAccesorio;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DetalleAccesorioId otro)) {
			return false;
		}
		return Objects.equals(idVentaAccesorio, otro.idVentaAccesorio) && Objects.equals(idAccesorio, otro.idAccesorio);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idVentaAccesorio, idAccesorio);
	}
}

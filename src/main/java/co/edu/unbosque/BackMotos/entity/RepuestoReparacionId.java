package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Llave del repuesto usado: reparación y repuesto. */
@Embeddable
public class RepuestoReparacionId implements Serializable {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/** Identificador de la reparación. */
	@Column(name = "id_reparacion")
	private Integer idReparacion;

	/** Identificador del repuesto. */
	@Column(name = "id_repuestos")
	private Integer idRepuesto;

	/** Constructor vacío que necesita JPA. */
	public RepuestoReparacionId() {
	}

	/**
	 * Crea la llave de la línea con sus datos.
	 *
	 * @param idReparacion identificador de la reparación
	 * @param idRepuesto identificador del repuesto
	 */
	public RepuestoReparacionId(Integer idReparacion, Integer idRepuesto) {
		this.idReparacion = idReparacion;
		this.idRepuesto = idRepuesto;
	}

	/**
	 * Devuelve el identificador de la reparación.
	 *
	 * @return el identificador de la reparación
	 */
	public Integer getIdReparacion() {
		return idReparacion;
	}

	/**
	 * Cambia el identificador de la reparación.
	 *
	 * @param idReparacion el identificador de la reparación
	 */
	public void setIdReparacion(Integer idReparacion) {
		this.idReparacion = idReparacion;
	}

	/**
	 * Devuelve el identificador del repuesto.
	 *
	 * @return el identificador del repuesto
	 */
	public Integer getIdRepuesto() {
		return idRepuesto;
	}

	/**
	 * Cambia el identificador del repuesto.
	 *
	 * @param idRepuesto el identificador del repuesto
	 */
	public void setIdRepuesto(Integer idRepuesto) {
		this.idRepuesto = idRepuesto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RepuestoReparacionId otro)) {
			return false;
		}
		return Objects.equals(idReparacion, otro.idReparacion) && Objects.equals(idRepuesto, otro.idRepuesto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idReparacion, idRepuesto);
	}
}

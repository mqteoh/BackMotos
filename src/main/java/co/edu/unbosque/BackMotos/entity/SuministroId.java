package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Llave del suministro: proveedor, repuesto, sede y fecha. */
@Embeddable
public class SuministroId implements Serializable {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/** NIT del proveedor. */
	@Column(name = "nit", length = 15)
	private String nit;

	/** Identificador del repuesto. */
	@Column(name = "id_repuestos")
	private Integer idRepuesto;

	/** Identificador de la sede. */
	@Column(name = "id_sucursal")
	private Integer idSucursal;

	/** Fecha en que llegó el suministro. */
	@Column(name = "fecha_suministro")
	private LocalDateTime fechaSuministro;

	/** Constructor vacío que necesita JPA. */
	public SuministroId() {
	}

	/**
	 * Crea la llave del suministro con sus datos.
	 *
	 * @param nit el NIT del proveedor
	 * @param idRepuesto identificador del repuesto
	 * @param idSucursal identificador de la sede
	 * @param fechaSuministro fecha en que llegó el suministro
	 */
	public SuministroId(String nit, Integer idRepuesto, Integer idSucursal, LocalDateTime fechaSuministro) {
		this.nit = nit;
		this.idRepuesto = idRepuesto;
		this.idSucursal = idSucursal;
		this.fechaSuministro = fechaSuministro;
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
	 * Devuelve la fecha en que llegó el suministro.
	 *
	 * @return la fecha en que llegó el suministro
	 */
	public LocalDateTime getFechaSuministro() {
		return fechaSuministro;
	}

	/**
	 * Cambia la fecha en que llegó el suministro.
	 *
	 * @param fechaSuministro la fecha en que llegó el suministro
	 */
	public void setFechaSuministro(LocalDateTime fechaSuministro) {
		this.fechaSuministro = fechaSuministro;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SuministroId otro)) {
			return false;
		}
		return Objects.equals(nit, otro.nit) && Objects.equals(idRepuesto, otro.idRepuesto)
				&& Objects.equals(idSucursal, otro.idSucursal) && Objects.equals(fechaSuministro, otro.fechaSuministro);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nit, idRepuesto, idSucursal, fechaSuministro);
	}
}

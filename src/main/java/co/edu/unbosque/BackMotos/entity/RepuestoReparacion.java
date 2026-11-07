package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/** Repuesto usado en una reparación, con cantidad y precio (tabla reparacion_repuestos). */
@Entity
@Table(name = "reparacion_repuestos")
public class RepuestoReparacion {

	/** Llave de la línea: reparación y repuesto. */
	@EmbeddedId
	private RepuestoReparacionId id = new RepuestoReparacionId();

	/** Reparación donde se usó el repuesto. */
	@MapsId("idReparacion")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_reparacion")
	private Reparacion reparacion;

	/** Repuesto usado. */
	@MapsId("idRepuesto")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_repuestos")
	private Repuesto repuesto;

	/** Unidades usadas. */
	@Column(name = "cantidad", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer cantidad;

	/** Precio por unidad. */
	@Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
	private BigDecimal precioUnitario;

	/** Constructor vacío que necesita JPA. */
	public RepuestoReparacion() {
	}

	/**
	 * Crea la línea de la reparación con sus datos.
	 *
	 * @param reparacion reparación donde se usó el repuesto
	 * @param repuesto repuesto usado
	 * @param cantidad unidades usadas
	 * @param precioUnitario precio por unidad
	 */
	public RepuestoReparacion(Reparacion reparacion, Repuesto repuesto, Integer cantidad, BigDecimal precioUnitario) {
		this.reparacion = reparacion;
		this.repuesto = repuesto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}

	/**
	 * Devuelve la cantidad por precio unitario.
	 *
	 * @return la cantidad por precio unitario
	 */
	public BigDecimal getSubtotal() {
		return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
	}

	/**
	 * Devuelve la llave de la línea: reparación y repuesto.
	 *
	 * @return la llave de la línea: reparación y repuesto
	 */
	public RepuestoReparacionId getId() {
		return id;
	}

	/**
	 * Cambia la llave de la línea: reparación y repuesto.
	 *
	 * @param id la llave de la línea: reparación y repuesto
	 */
	public void setId(RepuestoReparacionId id) {
		this.id = id;
	}

	/**
	 * Devuelve la reparación donde se usó el repuesto.
	 *
	 * @return la reparación donde se usó el repuesto
	 */
	public Reparacion getReparacion() {
		return reparacion;
	}

	/**
	 * Cambia la reparación donde se usó el repuesto.
	 *
	 * @param reparacion la reparación donde se usó el repuesto
	 */
	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}

	/**
	 * Devuelve el repuesto usado.
	 *
	 * @return el repuesto usado
	 */
	public Repuesto getRepuesto() {
		return repuesto;
	}

	/**
	 * Cambia el repuesto usado.
	 *
	 * @param repuesto el repuesto usado
	 */
	public void setRepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
	}

	/**
	 * Devuelve las unidades usadas.
	 *
	 * @return las unidades usadas
	 */
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * Cambia las unidades usadas.
	 *
	 * @param cantidad las unidades usadas
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * Devuelve el precio por unidad.
	 *
	 * @return el precio por unidad
	 */
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	/**
	 * Cambia el precio por unidad.
	 *
	 * @param precioUnitario el precio por unidad
	 */
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RepuestoReparacion otro)) {
			return false;
		}
		return id.getIdReparacion() != null && id.equals(otro.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}

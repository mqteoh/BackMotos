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

/** Línea de una venta de accesorios: qué accesorio, cuántos y a qué precio (tabla detalle_accesorio). */
@Entity
@Table(name = "detalle_accesorio")
public class DetalleAccesorio {

	/** Llave de la línea: venta y accesorio. */
	@EmbeddedId
	private DetalleAccesorioId id = new DetalleAccesorioId();

	/** Venta a la que pertenece la línea. */
	@MapsId("idVentaAccesorio")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_venta")
	private VentaAccesorio venta;

	/** Accesorio vendido. */
	@MapsId("idAccesorio")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_accesorio")
	private Accesorio accesorio;

	/** Unidades vendidas. */
	@Column(name = "cantidad", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer cantidad;

	/** Precio por unidad. */
	@Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
	private BigDecimal precioUnitario;

	/** Constructor vacío que necesita JPA. */
	public DetalleAccesorio() {
	}

	/**
	 * Crea la línea de la venta con sus datos.
	 *
	 * @param venta venta a la que pertenece la línea
	 * @param accesorio accesorio vendido
	 * @param cantidad unidades vendidas
	 * @param precioUnitario precio por unidad
	 */
	public DetalleAccesorio(VentaAccesorio venta, Accesorio accesorio, Integer cantidad, BigDecimal precioUnitario) {
		this.venta = venta;
		this.accesorio = accesorio;
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
	 * Devuelve la llave de la línea: venta y accesorio.
	 *
	 * @return la llave de la línea: venta y accesorio
	 */
	public DetalleAccesorioId getId() {
		return id;
	}

	/**
	 * Cambia la llave de la línea: venta y accesorio.
	 *
	 * @param id la llave de la línea: venta y accesorio
	 */
	public void setId(DetalleAccesorioId id) {
		this.id = id;
	}

	/**
	 * Devuelve la venta a la que pertenece la línea.
	 *
	 * @return la venta a la que pertenece la línea
	 */
	public VentaAccesorio getVenta() {
		return venta;
	}

	/**
	 * Cambia la venta a la que pertenece la línea.
	 *
	 * @param venta la venta a la que pertenece la línea
	 */
	public void setVenta(VentaAccesorio venta) {
		this.venta = venta;
	}

	/**
	 * Devuelve el accesorio vendido.
	 *
	 * @return el accesorio vendido
	 */
	public Accesorio getAccesorio() {
		return accesorio;
	}

	/**
	 * Cambia el accesorio vendido.
	 *
	 * @param accesorio el accesorio vendido
	 */
	public void setAccesorio(Accesorio accesorio) {
		this.accesorio = accesorio;
	}

	/**
	 * Devuelve las unidades vendidas.
	 *
	 * @return las unidades vendidas
	 */
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * Cambia las unidades vendidas.
	 *
	 * @param cantidad las unidades vendidas
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
		if (!(o instanceof DetalleAccesorio otro)) {
			return false;
		}
		return id.getIdVentaAccesorio() != null && id.equals(otro.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}

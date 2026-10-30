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

@Entity
@Table(name = "detalle_accesorio")
public class DetalleAccesorio {

	@EmbeddedId
	private DetalleAccesorioId id = new DetalleAccesorioId();

	@MapsId("idVentaAccesorio")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_venta_accesorio")
	private VentaAccesorio venta;

	@MapsId("idAccesorio")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_accesorio")
	private Accesorio accesorio;

	@Column(name = "cantidad", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer cantidad;

	@Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
	private BigDecimal precioUnitario;

	public DetalleAccesorio() {
	}

	public DetalleAccesorio(VentaAccesorio venta, Accesorio accesorio, Integer cantidad, BigDecimal precioUnitario) {
		this.venta = venta;
		this.accesorio = accesorio;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getSubtotal() {
		return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
	}

	public DetalleAccesorioId getId() {
		return id;
	}

	public void setId(DetalleAccesorioId id) {
		this.id = id;
	}

	public VentaAccesorio getVenta() {
		return venta;
	}

	public void setVenta(VentaAccesorio venta) {
		this.venta = venta;
	}

	public Accesorio getAccesorio() {
		return accesorio;
	}

	public void setAccesorio(Accesorio accesorio) {
		this.accesorio = accesorio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

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

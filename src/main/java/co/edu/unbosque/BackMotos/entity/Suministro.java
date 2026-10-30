package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "suministro")
public class Suministro {

	@EmbeddedId
	private SuministroId id = new SuministroId();

	@MapsId("nit")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nit")
	private Proveedor proveedor;

	@MapsId("idRepuesto")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_repuesto")
	private Repuesto repuesto;

	@MapsId("idSucursal")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_sucursal")
	private Sucursal sucursal;

	@Column(name = "cantidad", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer cantidad;

	@Column(name = "costo_unitario", nullable = false, precision = 12, scale = 2)
	private BigDecimal costoUnitario;

	public Suministro() {
	}

	public Suministro(Proveedor proveedor, Repuesto repuesto, Sucursal sucursal, LocalDateTime fechaSuministro,
			Integer cantidad, BigDecimal costoUnitario) {
		this.proveedor = proveedor;
		this.repuesto = repuesto;
		this.sucursal = sucursal;
		this.id.setFechaSuministro(fechaSuministro);
		this.cantidad = cantidad;
		this.costoUnitario = costoUnitario;
	}

	public LocalDateTime getFechaSuministro() {
		return id.getFechaSuministro();
	}

	public SuministroId getId() {
		return id;
	}

	public void setId(SuministroId id) {
		this.id = id;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Repuesto getRepuesto() {
		return repuesto;
	}

	public void setRepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getCostoUnitario() {
		return costoUnitario;
	}

	public void setCostoUnitario(BigDecimal costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Suministro otro)) {
			return false;
		}
		return id.getNit() != null && id.equals(otro.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}

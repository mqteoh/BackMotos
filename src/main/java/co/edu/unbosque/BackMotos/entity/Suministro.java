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

/** Entrega de repuestos de un proveedor a una sede (tabla suministros). */
@Entity
@Table(name = "suministros")
public class Suministro {

	/** Llave del suministro: proveedor, repuesto, sede y fecha. */
	@EmbeddedId
	private SuministroId id = new SuministroId();

	/** Proveedor que hizo la entrega. */
	@MapsId("nit")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nit")
	private Proveedor proveedor;

	/** Repuesto entregado. */
	@MapsId("idRepuesto")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_repuestos")
	private Repuesto repuesto;

	/** Sede que recibió la entrega. */
	@MapsId("idSucursal")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_sucursal")
	private Sucursal sucursal;

	/** Unidades entregadas. */
	@Column(name = "cantidad", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer cantidad;

	/** Costo por unidad. */
	@Column(name = "costo_unitario", nullable = false, precision = 12, scale = 2)
	private BigDecimal costoUnitario;

	/** Constructor vacío que necesita JPA. */
	public Suministro() {
	}

	/**
	 * Crea el suministro con sus datos.
	 *
	 * @param proveedor proveedor que hizo la entrega
	 * @param repuesto repuesto entregado
	 * @param sucursal sede que recibió la entrega
	 * @param fechaSuministro fecha en que llegó el suministro
	 * @param cantidad unidades entregadas
	 * @param costoUnitario costo por unidad
	 */
	public Suministro(Proveedor proveedor, Repuesto repuesto, Sucursal sucursal, LocalDateTime fechaSuministro,
			Integer cantidad, BigDecimal costoUnitario) {
		this.proveedor = proveedor;
		this.repuesto = repuesto;
		this.sucursal = sucursal;
		this.id.setFechaSuministro(fechaSuministro);
		this.cantidad = cantidad;
		this.costoUnitario = costoUnitario;
	}

	/**
	 * Devuelve la fecha en que llegó el suministro.
	 *
	 * @return la fecha en que llegó el suministro
	 */
	public LocalDateTime getFechaSuministro() {
		return id.getFechaSuministro();
	}

	/**
	 * Devuelve la llave del suministro: proveedor, repuesto, sede y fecha.
	 *
	 * @return la llave del suministro: proveedor, repuesto, sede y fecha
	 */
	public SuministroId getId() {
		return id;
	}

	/**
	 * Cambia la llave del suministro: proveedor, repuesto, sede y fecha.
	 *
	 * @param id la llave del suministro: proveedor, repuesto, sede y fecha
	 */
	public void setId(SuministroId id) {
		this.id = id;
	}

	/**
	 * Devuelve el proveedor que hizo la entrega.
	 *
	 * @return el proveedor que hizo la entrega
	 */
	public Proveedor getProveedor() {
		return proveedor;
	}

	/**
	 * Cambia el proveedor que hizo la entrega.
	 *
	 * @param proveedor el proveedor que hizo la entrega
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	/**
	 * Devuelve el repuesto entregado.
	 *
	 * @return el repuesto entregado
	 */
	public Repuesto getRepuesto() {
		return repuesto;
	}

	/**
	 * Cambia el repuesto entregado.
	 *
	 * @param repuesto el repuesto entregado
	 */
	public void setRepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
	}

	/**
	 * Devuelve la sede que recibió la entrega.
	 *
	 * @return la sede que recibió la entrega
	 */
	public Sucursal getSucursal() {
		return sucursal;
	}

	/**
	 * Cambia la sede que recibió la entrega.
	 *
	 * @param sucursal la sede que recibió la entrega
	 */
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	/**
	 * Devuelve las unidades entregadas.
	 *
	 * @return las unidades entregadas
	 */
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * Cambia las unidades entregadas.
	 *
	 * @param cantidad las unidades entregadas
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * Devuelve el costo por unidad.
	 *
	 * @return el costo por unidad
	 */
	public BigDecimal getCostoUnitario() {
		return costoUnitario;
	}

	/**
	 * Cambia el costo por unidad.
	 *
	 * @param costoUnitario el costo por unidad
	 */
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

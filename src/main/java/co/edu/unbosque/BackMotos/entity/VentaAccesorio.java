package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Venta de accesorios con sus líneas (tabla venta_accesorio).
 * El valor total lo calcula un trigger.
 */
@Entity
@Table(name = "venta_accesorio")
public class VentaAccesorio {

	/** Identificador de la venta de accesorios. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_venta")
	private Integer idVentaAccesorio;

	/** Fecha y hora de la venta. */
	@Column(name = "fecha_venta", nullable = false)
	private LocalDateTime fechaVenta;

	/** Método de pago. */
	@Enumerated(EnumType.STRING)
	@Column(name = "metodo_pago", nullable = false, length = 20)
	private MetodoPago metodoPago;

	/** Tipo de factura. */
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_factura", nullable = false, length = 12)
	private TipoFactura tipoFactura;

	/** Cliente que compró. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	/** Vendedor que registró la venta. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_vendedor")
	private Vendedor vendedor;

	/** Líneas de la venta. */
	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleAccesorio> detalles = new ArrayList<>();

	/** Constructor vacío que necesita JPA. */
	public VentaAccesorio() {
	}

	/**
	 * Agrega una línea a la venta.
	 *
	 * @param accesorio      accesorio vendido
	 * @param cantidad       unidades
	 * @param precioUnitario precio de cada unidad
	 * @return la línea agregada
	 */
	public DetalleAccesorio agregarDetalle(Accesorio accesorio, Integer cantidad, BigDecimal precioUnitario) {
		DetalleAccesorio detalle = new DetalleAccesorio(this, accesorio, cantidad, precioUnitario);
		detalles.add(detalle);
		return detalle;
	}

	/**
	 * Devuelve el valor total de la venta.
	 *
	 * @return el valor total de la venta
	 */
	public BigDecimal getValorTotal() {
		return detalles.stream().map(DetalleAccesorio::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
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
	 * Devuelve la fecha y hora de la venta.
	 *
	 * @return la fecha y hora de la venta
	 */
	public LocalDateTime getFechaVenta() {
		return fechaVenta;
	}

	/**
	 * Cambia la fecha y hora de la venta.
	 *
	 * @param fechaVenta la fecha y hora de la venta
	 */
	public void setFechaVenta(LocalDateTime fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	/**
	 * Devuelve el método de pago.
	 *
	 * @return el método de pago
	 */
	public MetodoPago getMetodoPago() {
		return metodoPago;
	}

	/**
	 * Cambia el método de pago.
	 *
	 * @param metodoPago el método de pago
	 */
	public void setMetodoPago(MetodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}

	/**
	 * Devuelve el tipo de factura.
	 *
	 * @return el tipo de factura
	 */
	public TipoFactura getTipoFactura() {
		return tipoFactura;
	}

	/**
	 * Cambia el tipo de factura.
	 *
	 * @param tipoFactura el tipo de factura
	 */
	public void setTipoFactura(TipoFactura tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	/**
	 * Devuelve el cliente que compró.
	 *
	 * @return el cliente que compró
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Cambia el cliente que compró.
	 *
	 * @param cliente el cliente que compró
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Devuelve el vendedor que registró la venta.
	 *
	 * @return el vendedor que registró la venta
	 */
	public Vendedor getVendedor() {
		return vendedor;
	}

	/**
	 * Cambia el vendedor que registró la venta.
	 *
	 * @param vendedor el vendedor que registró la venta
	 */
	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	/**
	 * Devuelve las líneas de la venta.
	 *
	 * @return las líneas de la venta
	 */
	public List<DetalleAccesorio> getDetalles() {
		return detalles;
	}

	/**
	 * Cambia las líneas de la venta.
	 *
	 * @param detalles las líneas de la venta
	 */
	public void setDetalles(List<DetalleAccesorio> detalles) {
		this.detalles = detalles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof VentaAccesorio otra)) {
			return false;
		}
		return idVentaAccesorio != null && idVentaAccesorio.equals(otra.idVentaAccesorio);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idVentaAccesorio);
	}
}

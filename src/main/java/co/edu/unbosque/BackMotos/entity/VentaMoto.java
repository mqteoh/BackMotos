package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Venta de una moto. Su llave es la placa, porque cada moto se vende una sola vez.
 * Al guardarla, la base marca la moto como VENDIDA y concreta la reserva activa del mismo cliente.
 */
@Entity
@Table(name = "venta_moto")
public class VentaMoto {

	/** Placa de la moto. */
	@Id
	@Column(name = "placa", length = 6)
	private String placa;

	/** Moto vendida. */
	@MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "placa")
	private Moto moto;

	/** Cliente que compró la moto. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	/** Vendedor que registró la venta. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_vendedor")
	private Vendedor vendedor;

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

	/** Valor acordado con el cliente; puede ser distinto del precio de lista. */
	@Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
	private BigDecimal valorTotal;

	/** Constructor vacío que necesita JPA. */
	public VentaMoto() {
	}

	/**
	 * Devuelve la placa de la moto.
	 *
	 * @return la placa de la moto
	 */
	public String getPlaca() {
		return placa;
	}

	/**
	 * Cambia la placa de la moto.
	 *
	 * @param placa la placa de la moto
	 */
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	/**
	 * Devuelve la moto vendida.
	 *
	 * @return la moto vendida
	 */
	public Moto getMoto() {
		return moto;
	}

	/**
	 * Cambia la moto vendida.
	 *
	 * @param moto la moto vendida
	 */
	public void setMoto(Moto moto) {
		this.moto = moto;
	}

	/**
	 * Devuelve el cliente que compró la moto.
	 *
	 * @return el cliente que compró la moto
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Cambia el cliente que compró la moto.
	 *
	 * @param cliente el cliente que compró la moto
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Devuelve el vendedor que hizo la venta.
	 *
	 * @return el vendedor que hizo la venta
	 */
	public Vendedor getVendedor() {
		return vendedor;
	}

	/**
	 * Cambia el vendedor que hizo la venta.
	 *
	 * @param vendedor el vendedor que hizo la venta
	 */
	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
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
	 * Devuelve el valor total de la venta.
	 *
	 * @return el valor total de la venta
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * Cambia el valor total de la venta.
	 *
	 * @param valorTotal el valor total de la venta
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof VentaMoto otra)) {
			return false;
		}
		return placa != null && placa.equals(otra.placa);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(placa);
	}
}

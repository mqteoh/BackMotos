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

@Entity
@Table(name = "venta_accesorio")
public class VentaAccesorio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_venta_accesorio")
	private Integer idVentaAccesorio;

	@Column(name = "fecha_venta", nullable = false)
	private LocalDateTime fechaVenta;

	@Enumerated(EnumType.STRING)
	@Column(name = "metodo_pago", nullable = false, length = 20)
	private MetodoPago metodoPago;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_factura", nullable = false, length = 12)
	private TipoFactura tipoFactura;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_vendedor")
	private Vendedor vendedor;

	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleAccesorio> detalles = new ArrayList<>();

	public VentaAccesorio() {
	}

	public DetalleAccesorio agregarDetalle(Accesorio accesorio, Integer cantidad, BigDecimal precioUnitario) {
		DetalleAccesorio detalle = new DetalleAccesorio(this, accesorio, cantidad, precioUnitario);
		detalles.add(detalle);
		return detalle;
	}

	public BigDecimal getValorTotal() {
		return detalles.stream().map(DetalleAccesorio::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public Integer getIdVentaAccesorio() {
		return idVentaAccesorio;
	}

	public void setIdVentaAccesorio(Integer idVentaAccesorio) {
		this.idVentaAccesorio = idVentaAccesorio;
	}

	public LocalDateTime getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(LocalDateTime fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public MetodoPago getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(MetodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}

	public TipoFactura getTipoFactura() {
		return tipoFactura;
	}

	public void setTipoFactura(TipoFactura tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public List<DetalleAccesorio> getDetalles() {
		return detalles;
	}

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

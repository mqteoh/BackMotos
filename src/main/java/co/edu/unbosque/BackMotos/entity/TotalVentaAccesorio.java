package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Immutable
@Subselect("SELECT * FROM v_total_venta_accesorio")
@Synchronize({ "venta_accesorio", "detalle_accesorio" })
public class TotalVentaAccesorio {

	@Id
	@Column(name = "id_venta_accesorio")
	private Integer idVentaAccesorio;

	@Column(name = "fecha_venta")
	private LocalDateTime fechaVenta;

	@Column(name = "id_cliente")
	private Integer idCliente;

	@Column(name = "id_vendedor")
	private Integer idVendedor;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	protected TotalVentaAccesorio() {
	}

	public Integer getIdVentaAccesorio() {
		return idVentaAccesorio;
	}

	public LocalDateTime getFechaVenta() {
		return fechaVenta;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public Integer getIdVendedor() {
		return idVendedor;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}
}

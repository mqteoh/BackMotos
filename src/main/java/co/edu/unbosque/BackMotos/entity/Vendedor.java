package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "vendedor")
@PrimaryKeyJoinColumn(name = "id_empleado")
@DiscriminatorValue("VENDEDOR")
public class Vendedor extends Empleado {

	@Column(name = "certificado_ventas", nullable = false, length = 80)
	private String certificadoVentas;

	@Column(name = "comision", nullable = false, precision = 5, scale = 2)
	private BigDecimal comision;

	public Vendedor() {
	}

	@Override
	@Transient
	public TipoEmpleado getTipoEmpleado() {
		return TipoEmpleado.VENDEDOR;
	}

	public String getCertificadoVentas() {
		return certificadoVentas;
	}

	public void setCertificadoVentas(String certificadoVentas) {
		this.certificadoVentas = certificadoVentas;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}
}

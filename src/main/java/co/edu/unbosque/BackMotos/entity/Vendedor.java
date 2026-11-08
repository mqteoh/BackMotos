package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/** Empleado que vende motos y accesorios (tabla vendedor). */
@Entity
@Table(name = "vendedor")
@PrimaryKeyJoinColumn(name = "id_empleado")
public class Vendedor extends Empleado {

	/** Certificado de ventas del vendedor; solo para vendedores. */
	@Column(name = "certificado_ventas", nullable = false, length = 80)
	private String certificadoVentas;

	/** Porcentaje de comisión del vendedor; solo para vendedores. */
	@Column(name = "comision", nullable = false, precision = 5, scale = 2)
	private BigDecimal comision;

	/** Constructor vacío que necesita JPA. */
	public Vendedor() {
	}

	@Override
	@Transient
	public TipoEmpleado getTipoEmpleado() {
		return TipoEmpleado.VENDEDOR;
	}

	/**
	 * Devuelve el certificado de ventas del vendedor; solo para vendedores.
	 *
	 * @return el certificado de ventas del vendedor; solo para vendedores
	 */
	public String getCertificadoVentas() {
		return certificadoVentas;
	}

	/**
	 * Cambia el certificado de ventas del vendedor; solo para vendedores.
	 *
	 * @param certificadoVentas el certificado de ventas del vendedor; solo para vendedores
	 */
	public void setCertificadoVentas(String certificadoVentas) {
		this.certificadoVentas = certificadoVentas;
	}

	/**
	 * Devuelve el porcentaje de comisión del vendedor; solo para vendedores.
	 *
	 * @return el porcentaje de comisión del vendedor; solo para vendedores
	 */
	public BigDecimal getComision() {
		return comision;
	}

	/**
	 * Cambia el porcentaje de comisión del vendedor; solo para vendedores.
	 *
	 * @param comision el porcentaje de comisión del vendedor; solo para vendedores
	 */
	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}
}

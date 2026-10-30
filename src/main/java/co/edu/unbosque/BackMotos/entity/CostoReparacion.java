package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
@Immutable
@Subselect("SELECT * FROM v_costo_reparacion")
@Synchronize({ "reparacion", "repuesto_reparacion" })
public class CostoReparacion {

	@Id
	@Column(name = "id_reparacion")
	private Integer idReparacion;

	@Column(name = "placa")
	private String placa;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	private EstadoReparacion estado;

	@Column(name = "mano_obra")
	private BigDecimal manoObra;

	@Column(name = "repuestos")
	private BigDecimal repuestos;

	@Column(name = "costo_total")
	private BigDecimal costoTotal;

	protected CostoReparacion() {
	}

	public Integer getIdReparacion() {
		return idReparacion;
	}

	public String getPlaca() {
		return placa;
	}

	public EstadoReparacion getEstado() {
		return estado;
	}

	public BigDecimal getManoObra() {
		return manoObra;
	}

	public BigDecimal getRepuestos() {
		return repuestos;
	}

	public BigDecimal getCostoTotal() {
		return costoTotal;
	}
}

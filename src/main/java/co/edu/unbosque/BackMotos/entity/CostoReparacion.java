package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Costo de cada reparación, leído de la vista v_costo_reparacion.
 * Suma la mano de obra y los repuestos usados. Es solo de lectura.
 */
@Entity
@Immutable
@Subselect("SELECT * FROM v_costo_reparacion")
@Synchronize({ "reparacion", "reparacion_repuestos" })
public class CostoReparacion {

	/** Identificador de la reparación. */
	@Id
	@Column(name = "id_reparacion")
	private Integer idReparacion;

	/** Placa de la moto. */
	@Column(name = "placa")
	private String placa;

	/** Fecha de la reparación. */
	@Column(name = "fecha")
	private LocalDate fecha;

	/** Costo de la mano de obra. */
	@Column(name = "mano_obra")
	private BigDecimal manoObra;

	/** Suma de los repuestos usados. */
	@Column(name = "repuestos")
	private BigDecimal repuestos;

	/** Mano de obra más repuestos. */
	@Column(name = "costo_total")
	private BigDecimal costoTotal;

	/** Constructor vacío que necesita JPA. */
	protected CostoReparacion() {
	}

	/**
	 * Devuelve el identificador de la reparación.
	 *
	 * @return el identificador de la reparación
	 */
	public Integer getIdReparacion() {
		return idReparacion;
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
	 * Devuelve la fecha de la reparación.
	 *
	 * @return la fecha de la reparación
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * Devuelve el costo de la mano de obra.
	 *
	 * @return el costo de la mano de obra
	 */
	public BigDecimal getManoObra() {
		return manoObra;
	}

	/**
	 * Devuelve la suma de los repuestos usados.
	 *
	 * @return la suma de los repuestos usados
	 */
	public BigDecimal getRepuestos() {
		return repuestos;
	}

	/**
	 * Devuelve la mano de obra más repuestos.
	 *
	 * @return la mano de obra más repuestos
	 */
	public BigDecimal getCostoTotal() {
		return costoTotal;
	}
}

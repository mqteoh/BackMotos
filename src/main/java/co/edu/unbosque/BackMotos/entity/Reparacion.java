package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Reparación hecha en el taller a una moto vendida por el concesionario.
 * La registra el técnico cuando el trabajo ya está hecho; puede tener varios técnicos y varios repuestos.
 */
@Entity
@Table(name = "reparacion")
public class Reparacion {

	/** Identificador de la reparación. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reparacion")
	private Integer idReparacion;

	/** Moto reparada. La base exige que haya sido vendida antes de esta fecha. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "placa")
	private Moto moto;

	/** Día en que se hizo la reparación. */
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	/** Trabajo que se hizo. */
	@Column(name = "descripcion", nullable = false, length = 255)
	private String descripcion;

	/** Costo de la mano de obra, sin contar repuestos. */
	@Column(name = "costo", nullable = false, precision = 12, scale = 2)
	private BigDecimal costo;

	/** Técnicos que trabajaron en la reparación (tabla tecnicos_reparacion). Todos son de la misma sede. */
	@ManyToMany
	@JoinTable(name = "tecnicos_reparacion", joinColumns = @JoinColumn(name = "id_reparacion"),
			inverseJoinColumns = @JoinColumn(name = "id_empleado"))
	private Set<Tecnico> tecnicos = new LinkedHashSet<>();

	/** Repuestos usados, con cantidad y precio (tabla reparacion_repuestos). */
	@OneToMany(mappedBy = "reparacion", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RepuestoReparacion> repuestos = new ArrayList<>();

	/** Constructor vacío que necesita JPA. */
	public Reparacion() {
	}

	/**
	 * Agrega un repuesto usado en la reparación.
	 *
	 * @param repuesto       repuesto usado
	 * @param cantidad       unidades usadas
	 * @param precioUnitario precio cobrado por unidad
	 * @return la línea creada
	 */
	public RepuestoReparacion agregarRepuesto(Repuesto repuesto, Integer cantidad, BigDecimal precioUnitario) {
		RepuestoReparacion uso = new RepuestoReparacion(this, repuesto, cantidad, precioUnitario);
		repuestos.add(uso);
		return uso;
	}

	/**
	 * Calcula la suma de los repuestos usados.
	 *
	 * @return suma de los repuestos usados
	 */
	public BigDecimal getCostoRepuestos() {
		return repuestos.stream().map(RepuestoReparacion::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * Calcula el costo total: mano de obra más repuestos.
	 *
	 * @return mano de obra más repuestos
	 */
	public BigDecimal getCostoTotal() {
		return (costo == null ? BigDecimal.ZERO : costo).add(getCostoRepuestos());
	}

	/**
	 * Devuelve la sede de la reparación, que es la de sus técnicos.
	 *
	 * @return sede de los técnicos que hicieron la reparación, o null si no tiene técnicos
	 */
	public Sucursal getSucursal() {
		return tecnicos.stream().findFirst().map(Tecnico::getSucursal).orElse(null);
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
	 * Cambia el identificador de la reparación.
	 *
	 * @param idReparacion el identificador de la reparación
	 */
	public void setIdReparacion(Integer idReparacion) {
		this.idReparacion = idReparacion;
	}

	/**
	 * Devuelve la moto que se reparó.
	 *
	 * @return la moto que se reparó
	 */
	public Moto getMoto() {
		return moto;
	}

	/**
	 * Cambia la moto que se reparó.
	 *
	 * @param moto la moto que se reparó
	 */
	public void setMoto(Moto moto) {
		this.moto = moto;
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
	 * Cambia la fecha de la reparación.
	 *
	 * @param fecha la fecha de la reparación
	 */
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	/**
	 * Devuelve el trabajo que se hizo.
	 *
	 * @return el trabajo que se hizo
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Cambia el trabajo que se hizo.
	 *
	 * @param descripcion el trabajo que se hizo
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve el costo de la mano de obra.
	 *
	 * @return el costo de la mano de obra
	 */
	public BigDecimal getCosto() {
		return costo;
	}

	/**
	 * Cambia el costo de la mano de obra.
	 *
	 * @param costo el costo de la mano de obra
	 */
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	/**
	 * Devuelve los técnicos que participaron.
	 *
	 * @return los técnicos que participaron
	 */
	public Set<Tecnico> getTecnicos() {
		return tecnicos;
	}

	/**
	 * Cambia los técnicos que participaron.
	 *
	 * @param tecnicos los técnicos que participaron
	 */
	public void setTecnicos(Set<Tecnico> tecnicos) {
		this.tecnicos = tecnicos;
	}

	/**
	 * Devuelve los repuestos usados.
	 *
	 * @return los repuestos usados
	 */
	public List<RepuestoReparacion> getRepuestos() {
		return repuestos;
	}

	/**
	 * Cambia los repuestos usados.
	 *
	 * @param repuestos los repuestos usados
	 */
	public void setRepuestos(List<RepuestoReparacion> repuestos) {
		this.repuestos = repuestos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Reparacion otra)) {
			return false;
		}
		return idReparacion != null && idReparacion.equals(otra.idReparacion);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idReparacion);
	}
}

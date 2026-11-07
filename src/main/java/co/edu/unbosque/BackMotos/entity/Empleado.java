package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Empleado de una sede, con salario y jefe (tabla empleado).
 * Vendedor y Tecnico son sus subclases; el dueño es un empleado sin subclase.
 */
@Entity
@Table(name = "empleado")
@Inheritance(strategy = InheritanceType.JOINED)
public class Empleado extends Persona {

	/** Identificador del empleado. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_empleado")
	private Integer idEmpleado;

	/** Fecha en que se contrató al empleado. */
	@Column(name = "fecha_contratacion", nullable = false)
	private LocalDate fechaContratacion;

	/** Salario mensual. */
	@Column(name = "salario", nullable = false, precision = 12, scale = 2)
	private BigDecimal salario;

	/** Sede donde trabaja el empleado. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_sucursal")
	private Sucursal sucursal;

	/** Jefe directo; null si no tiene. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_jefe")
	private Empleado jefe;

	/** Números de teléfono. */
	@ElementCollection
	@CollectionTable(name = "telefono_empleado", joinColumns = @JoinColumn(name = "id_empleado"))
	@Column(name = "numero", length = 15)
	private Set<String> telefonos = new LinkedHashSet<>();

	/** Constructor vacío que necesita JPA. */
	public Empleado() {
	}

	/**
	 * Devuelve el cargo: DUENO, VENDEDOR o TECNICO.
	 *
	 * @return el cargo: DUENO, VENDEDOR o TECNICO
	 */
	@Transient
	public TipoEmpleado getTipoEmpleado() {
		return TipoEmpleado.DUENO;
	}

	/**
	 * Devuelve el identificador del empleado.
	 *
	 * @return el identificador del empleado
	 */
	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	/**
	 * Cambia el identificador del empleado.
	 *
	 * @param idEmpleado el identificador del empleado
	 */
	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	/**
	 * Devuelve la fecha en que se contrató al empleado.
	 *
	 * @return la fecha en que se contrató al empleado
	 */
	public LocalDate getFechaContratacion() {
		return fechaContratacion;
	}

	/**
	 * Cambia la fecha en que se contrató al empleado.
	 *
	 * @param fechaContratacion la fecha en que se contrató al empleado
	 */
	public void setFechaContratacion(LocalDate fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}

	/**
	 * Devuelve el salario mensual.
	 *
	 * @return el salario mensual
	 */
	public BigDecimal getSalario() {
		return salario;
	}

	/**
	 * Cambia el salario mensual.
	 *
	 * @param salario el salario mensual
	 */
	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	/**
	 * Devuelve la sede donde trabaja el empleado.
	 *
	 * @return la sede donde trabaja el empleado
	 */
	public Sucursal getSucursal() {
		return sucursal;
	}

	/**
	 * Cambia la sede donde trabaja el empleado.
	 *
	 * @param sucursal la sede donde trabaja el empleado
	 */
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	/**
	 * Devuelve el jefe directo; null si no tiene.
	 *
	 * @return el jefe directo; null si no tiene
	 */
	public Empleado getJefe() {
		return jefe;
	}

	/**
	 * Cambia el jefe directo; null si no tiene.
	 *
	 * @param jefe el jefe directo; null si no tiene
	 */
	public void setJefe(Empleado jefe) {
		this.jefe = jefe;
	}

	/**
	 * Devuelve los números de teléfono.
	 *
	 * @return los números de teléfono
	 */
	public Set<String> getTelefonos() {
		return telefonos;
	}

	/**
	 * Cambia los números de teléfono.
	 *
	 * @param telefonos los números de teléfono
	 */
	public void setTelefonos(Set<String> telefonos) {
		this.telefonos = telefonos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Empleado otro)) {
			return false;
		}
		return idEmpleado != null && idEmpleado.equals(otro.idEmpleado);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idEmpleado);
	}
}

package co.edu.unbosque.BackMotos.entity;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
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

@Entity
@Table(name = "empleado")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_empleado", discriminatorType = DiscriminatorType.STRING, length = 10)
@DiscriminatorValue("DUENO")
public class Empleado extends Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_empleado")
	private Integer idEmpleado;

	@Column(name = "fecha_contratacion", nullable = false)
	private LocalDate fechaContratacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal")
	private Sucursal sucursal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_jefe")
	private Empleado jefe;

	@Column(name = "correo_acceso", nullable = false, length = 120, unique = true)
	private String correoAcceso;

	@Column(name = "password_hash", nullable = false, length = 60)
	@JdbcTypeCode(SqlTypes.CHAR)
	private String passwordHash;

	@ElementCollection
	@CollectionTable(name = "telefono_empleado", joinColumns = @JoinColumn(name = "id_empleado"))
	@Column(name = "numero", length = 15)
	private Set<String> telefonos = new LinkedHashSet<>();

	public Empleado() {
	}

	@Transient
	public TipoEmpleado getTipoEmpleado() {
		return TipoEmpleado.DUENO;
	}

	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public LocalDate getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(LocalDate fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Empleado getJefe() {
		return jefe;
	}

	public void setJefe(Empleado jefe) {
		this.jefe = jefe;
	}

	public String getCorreoAcceso() {
		return correoAcceso;
	}

	public void setCorreoAcceso(String correoAcceso) {
		this.correoAcceso = correoAcceso;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Set<String> getTelefonos() {
		return telefonos;
	}

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

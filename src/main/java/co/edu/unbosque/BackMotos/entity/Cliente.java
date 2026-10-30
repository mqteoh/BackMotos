package co.edu.unbosque.BackMotos.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Integer idCliente;

	@Column(name = "fecha_nacimiento", nullable = false)
	private LocalDate fechaNacimiento;

	@Column(name = "ciudad", nullable = false, length = 60)
	private String ciudad;

	@Column(name = "barrio", nullable = false, length = 60)
	private String barrio;

	@Column(name = "calle", nullable = false, length = 60)
	private String calle;

	@Column(name = "numero_direccion", nullable = false, length = 20)
	private String numeroDireccion;

	@Column(name = "correo_acceso", length = 120, unique = true)
	private String correoAcceso;

	@Column(name = "password_hash", length = 60)
	@JdbcTypeCode(SqlTypes.CHAR)
	private String passwordHash;

	@ElementCollection
	@CollectionTable(name = "telefono_cliente", joinColumns = @JoinColumn(name = "id_cliente"))
	@Column(name = "numero", length = 15)
	private Set<String> telefonos = new LinkedHashSet<>();

	@ElementCollection
	@CollectionTable(name = "correo_cliente", joinColumns = @JoinColumn(name = "id_cliente"))
	@Column(name = "direccion_correo", length = 120)
	private Set<String> correos = new LinkedHashSet<>();

	public Cliente() {
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public int getEdad() {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumeroDireccion() {
		return numeroDireccion;
	}

	public void setNumeroDireccion(String numeroDireccion) {
		this.numeroDireccion = numeroDireccion;
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

	public boolean tieneCuentaWeb() {
		return correoAcceso != null;
	}

	public Set<String> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(Set<String> telefonos) {
		this.telefonos = telefonos;
	}

	public Set<String> getCorreos() {
		return correos;
	}

	public void setCorreos(Set<String> correos) {
		this.correos = correos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Cliente otro)) {
			return false;
		}
		return idCliente != null && idCliente.equals(otro.idCliente);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idCliente);
	}
}

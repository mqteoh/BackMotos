package co.edu.unbosque.BackMotos.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/** Cliente del concesionario, con sus teléfonos y correos (tabla cliente). */
@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

	/** Identificador del cliente. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Integer idCliente;

	/** Fecha de nacimiento. */
	@Column(name = "fecha_nacimiento", nullable = false)
	private LocalDate fechaNacimiento;

	/** Ciudad. */
	@Column(name = "ciudad", nullable = false, length = 60)
	private String ciudad;

	/** Barrio de la dirección. */
	@Column(name = "barrio", nullable = false, length = 60)
	private String barrio;

	/** Calle de la dirección. */
	@Column(name = "calle", nullable = false, length = 60)
	private String calle;

	/** Número de la dirección. */
	@Column(name = "numero_direccion", nullable = false, length = 20)
	private String numeroDireccion;

	/** Números de teléfono. */
	@ElementCollection
	@CollectionTable(name = "telefono_cliente", joinColumns = @JoinColumn(name = "id_cliente"))
	@Column(name = "numero", length = 15)
	private Set<String> telefonos = new LinkedHashSet<>();

	/** Correos de contacto. */
	@ElementCollection
	@CollectionTable(name = "correo_cliente", joinColumns = @JoinColumn(name = "id_cliente"))
	@Column(name = "direccion_correo", length = 120)
	private Set<String> correos = new LinkedHashSet<>();

	/** Constructor vacío que necesita JPA. */
	public Cliente() {
	}

	/**
	 * Devuelve el identificador del cliente.
	 *
	 * @return el identificador del cliente
	 */
	public Integer getIdCliente() {
		return idCliente;
	}

	/**
	 * Cambia el identificador del cliente.
	 *
	 * @param idCliente el identificador del cliente
	 */
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * Devuelve la fecha de nacimiento.
	 *
	 * @return la fecha de nacimiento
	 */
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * Cambia la fecha de nacimiento.
	 *
	 * @param fechaNacimiento la fecha de nacimiento
	 */
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * Devuelve la edad en años, calculada con la fecha de nacimiento.
	 *
	 * @return la edad en años, calculada con la fecha de nacimiento
	 */
	public int getEdad() {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	/**
	 * Devuelve la ciudad donde vive el cliente.
	 *
	 * @return la ciudad donde vive el cliente
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * Cambia la ciudad donde vive el cliente.
	 *
	 * @param ciudad la ciudad donde vive el cliente
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * Devuelve el barrio de la dirección.
	 *
	 * @return el barrio de la dirección
	 */
	public String getBarrio() {
		return barrio;
	}

	/**
	 * Cambia el barrio de la dirección.
	 *
	 * @param barrio el barrio de la dirección
	 */
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	/**
	 * Devuelve la calle de la dirección.
	 *
	 * @return la calle de la dirección
	 */
	public String getCalle() {
		return calle;
	}

	/**
	 * Cambia la calle de la dirección.
	 *
	 * @param calle la calle de la dirección
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}

	/**
	 * Devuelve el número de la dirección.
	 *
	 * @return el número de la dirección
	 */
	public String getNumeroDireccion() {
		return numeroDireccion;
	}

	/**
	 * Cambia el número de la dirección.
	 *
	 * @param numeroDireccion el número de la dirección
	 */
	public void setNumeroDireccion(String numeroDireccion) {
		this.numeroDireccion = numeroDireccion;
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

	/**
	 * Devuelve los correos de contacto.
	 *
	 * @return los correos de contacto
	 */
	public Set<String> getCorreos() {
		return correos;
	}

	/**
	 * Cambia los correos de contacto.
	 *
	 * @param correos los correos de contacto
	 */
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

package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Accesorio que se vende en el concesionario (tabla accesorio). */
@Entity
@Table(name = "accesorio")
public class Accesorio {

	/** Identificador del accesorio. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_accesorio")
	private Integer idAccesorio;

	/** Nombre del accesorio. */
	@Column(name = "nombre", nullable = false, length = 80)
	private String nombre;

	/** Marca del accesorio. */
	@Column(name = "marca", nullable = false, length = 60)
	private String marca;

	/** Descripción del accesorio. */
	@Column(name = "descripcion", length = 255)
	private String descripcion;

	/** Precio de lista. */
	@Column(name = "precio", nullable = false, precision = 12, scale = 2)
	private BigDecimal precio;

	/** Constructor vacío que necesita JPA. */
	public Accesorio() {
	}

	/**
	 * Devuelve el identificador del accesorio.
	 *
	 * @return el identificador del accesorio
	 */
	public Integer getIdAccesorio() {
		return idAccesorio;
	}

	/**
	 * Cambia el identificador del accesorio.
	 *
	 * @param idAccesorio el identificador del accesorio
	 */
	public void setIdAccesorio(Integer idAccesorio) {
		this.idAccesorio = idAccesorio;
	}

	/**
	 * Devuelve el nombre del accesorio.
	 *
	 * @return el nombre del accesorio
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre del accesorio.
	 *
	 * @param nombre el nombre del accesorio
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la marca del accesorio.
	 *
	 * @return la marca del accesorio
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * Cambia la marca del accesorio.
	 *
	 * @param marca la marca del accesorio
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * Devuelve la descripción del accesorio.
	 *
	 * @return la descripción del accesorio
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Cambia la descripción del accesorio.
	 *
	 * @param descripcion la descripción del accesorio
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve el precio de lista.
	 *
	 * @return el precio de lista
	 */
	public BigDecimal getPrecio() {
		return precio;
	}

	/**
	 * Cambia el precio de lista.
	 *
	 * @param precio el precio de lista
	 */
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Accesorio otro)) {
			return false;
		}
		return idAccesorio != null && idAccesorio.equals(otro.idAccesorio);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idAccesorio);
	}
}

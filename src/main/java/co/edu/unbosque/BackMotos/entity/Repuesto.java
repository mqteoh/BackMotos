package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Repuesto que usa el taller (tabla repuesto). */
@Entity
@Table(name = "repuesto")
public class Repuesto {

	/** Identificador del repuesto. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_repuestos")
	private Integer idRepuesto;

	/** Nombre del repuesto. */
	@Column(name = "nombre", nullable = false, length = 80)
	private String nombre;

	/** Categoría del repuesto. */
	@Column(name = "categoria", nullable = false, length = 40)
	private String categoria;

	/** Precio de lista. */
	@Column(name = "precio", nullable = false, precision = 12, scale = 2)
	private BigDecimal precio;

	/** Constructor vacío que necesita JPA. */
	public Repuesto() {
	}

	/**
	 * Devuelve el identificador del repuesto.
	 *
	 * @return el identificador del repuesto
	 */
	public Integer getIdRepuesto() {
		return idRepuesto;
	}

	/**
	 * Cambia el identificador del repuesto.
	 *
	 * @param idRepuesto el identificador del repuesto
	 */
	public void setIdRepuesto(Integer idRepuesto) {
		this.idRepuesto = idRepuesto;
	}

	/**
	 * Devuelve el nombre del repuesto.
	 *
	 * @return el nombre del repuesto
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre del repuesto.
	 *
	 * @param nombre el nombre del repuesto
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve la categoría del repuesto.
	 *
	 * @return la categoría del repuesto
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * Cambia la categoría del repuesto.
	 *
	 * @param categoria la categoría del repuesto
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
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
		if (!(o instanceof Repuesto otro)) {
			return false;
		}
		return idRepuesto != null && idRepuesto.equals(otro.idRepuesto);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idRepuesto);
	}
}

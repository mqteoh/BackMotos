package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/** Modelo de una marca, entidad débil de marca (tabla modelo). */
@Entity
@Table(name = "modelo")
public class Modelo {

	/** Llave del modelo: marca y número. */
	@EmbeddedId
	private ModeloId id;

	/** Marca del modelo. */
	@MapsId("idMarca")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_marca")
	private Marca marca;

	/** Nombre del modelo. */
	@Column(name = "nombre", nullable = false, length = 80)
	private String nombre;

	/** Cilindraje del modelo en centímetros cúbicos. */
	@Column(name = "cilindraje", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer cilindraje;

	/** Tipo de moto. */
	@Column(name = "tipo_moto", nullable = false, length = 30)
	private String tipoMoto;

	/** Potencia en caballos de fuerza. */
	@Column(name = "potencia", nullable = false, precision = 5, scale = 1)
	private BigDecimal potenciaHp;

	/** Capacidad del tanque en litros. */
	@Column(name = "capacidad_tanque", nullable = false, precision = 4, scale = 1)
	private BigDecimal capacidadTanque;

	/** Constructor vacío que necesita JPA. */
	public Modelo() {
	}

	/**
	 * Devuelve la llave del modelo: marca y número.
	 *
	 * @return la llave del modelo: marca y número
	 */
	public ModeloId getId() {
		return id;
	}

	/**
	 * Cambia la llave del modelo: marca y número.
	 *
	 * @param id la llave del modelo: marca y número
	 */
	public void setId(ModeloId id) {
		this.id = id;
	}

	/**
	 * Devuelve la marca del modelo.
	 *
	 * @return la marca del modelo
	 */
	public Marca getMarca() {
		return marca;
	}

	/**
	 * Cambia la marca del modelo.
	 *
	 * @param marca la marca del modelo
	 */
	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	/**
	 * Devuelve el nombre del modelo.
	 *
	 * @return el nombre del modelo
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Cambia el nombre del modelo.
	 *
	 * @param nombre el nombre del modelo
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Devuelve el cilindraje del modelo en centímetros cúbicos.
	 *
	 * @return el cilindraje del modelo en centímetros cúbicos
	 */
	public Integer getCilindraje() {
		return cilindraje;
	}

	/**
	 * Cambia el cilindraje del modelo en centímetros cúbicos.
	 *
	 * @param cilindraje el cilindraje del modelo en centímetros cúbicos
	 */
	public void setCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
	}

	/**
	 * Devuelve el tipo de moto.
	 *
	 * @return el tipo de moto
	 */
	public String getTipoMoto() {
		return tipoMoto;
	}

	/**
	 * Cambia el tipo de moto.
	 *
	 * @param tipoMoto el tipo de moto
	 */
	public void setTipoMoto(String tipoMoto) {
		this.tipoMoto = tipoMoto;
	}

	/**
	 * Devuelve la potencia en caballos de fuerza.
	 *
	 * @return la potencia en caballos de fuerza
	 */
	public BigDecimal getPotenciaHp() {
		return potenciaHp;
	}

	/**
	 * Cambia la potencia en caballos de fuerza.
	 *
	 * @param potenciaHp la potencia en caballos de fuerza
	 */
	public void setPotenciaHp(BigDecimal potenciaHp) {
		this.potenciaHp = potenciaHp;
	}

	/**
	 * Devuelve la capacidad del tanque en litros.
	 *
	 * @return la capacidad del tanque en litros
	 */
	public BigDecimal getCapacidadTanque() {
		return capacidadTanque;
	}

	/**
	 * Cambia la capacidad del tanque en litros.
	 *
	 * @param capacidadTanque la capacidad del tanque en litros
	 */
	public void setCapacidadTanque(BigDecimal capacidadTanque) {
		this.capacidadTanque = capacidadTanque;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Modelo otro)) {
			return false;
		}
		return id != null && id.equals(otro.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}

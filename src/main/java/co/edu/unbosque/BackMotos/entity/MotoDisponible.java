package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/** Moto que se puede reservar o vender, leída de la vista v_moto_disponible. */
@Entity
@Immutable
@Subselect("SELECT * FROM v_moto_disponible")
@Synchronize({ "moto", "modelo", "marca", "sucursal", "venta_moto", "reserva" })
public class MotoDisponible {

	/** Placa de la moto. */
	@Id
	@Column(name = "placa")
	private String placa;

	/** Nombre de la marca. */
	@Column(name = "marca")
	private String marca;

	/** Nombre del modelo. */
	@Column(name = "modelo")
	private String modelo;

	/** Tipo de moto. */
	@Column(name = "tipo_moto")
	private String tipoMoto;

	/** Cilindraje del modelo en centímetros cúbicos. */
	@Column(name = "cilindraje")
	private Integer cilindraje;

	/** Año del modelo de la moto. */
	@Column(name = "anio")
	private Integer anio;

	/** Color de la moto. */
	@Column(name = "color")
	private String color;

	/** Precio de lista. */
	@Column(name = "precio")
	private BigDecimal precio;

	/** Identificador de la sede. */
	@Column(name = "id_sucursal")
	private Integer idSucursal;

	/** Nombre de la sede. */
	@Column(name = "sucursal")
	private String sucursal;

	/** Ciudad. */
	@Column(name = "ciudad")
	private String ciudad;

	/** Constructor vacío que necesita JPA. */
	protected MotoDisponible() {
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
	 * Devuelve el nombre de la marca.
	 *
	 * @return el nombre de la marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * Devuelve el nombre del modelo.
	 *
	 * @return el nombre del modelo
	 */
	public String getModelo() {
		return modelo;
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
	 * Devuelve el cilindraje del modelo en centímetros cúbicos.
	 *
	 * @return el cilindraje del modelo en centímetros cúbicos
	 */
	public Integer getCilindraje() {
		return cilindraje;
	}

	/**
	 * Devuelve el año del modelo de la moto.
	 *
	 * @return el año del modelo de la moto
	 */
	public Integer getAnio() {
		return anio;
	}

	/**
	 * Devuelve el color de la moto.
	 *
	 * @return el color de la moto
	 */
	public String getColor() {
		return color;
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
	 * Devuelve el identificador de la sede.
	 *
	 * @return el identificador de la sede
	 */
	public Integer getIdSucursal() {
		return idSucursal;
	}

	/**
	 * Devuelve el nombre de la sede.
	 *
	 * @return el nombre de la sede
	 */
	public String getSucursal() {
		return sucursal;
	}

	/**
	 * Devuelve la ciudad de la sede.
	 *
	 * @return la ciudad de la sede
	 */
	public String getCiudad() {
		return ciudad;
	}
}

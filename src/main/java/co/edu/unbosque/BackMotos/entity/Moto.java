package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Moto del inventario de una sede (tabla moto).
 * El estado lo cambian los triggers de reservas y ventas.
 */
@Entity
@Table(name = "moto")
public class Moto {

	/** Placa de la moto. */
	@Id
	@Column(name = "placa", length = 6)
	private String placa;

	/** Modelo de la moto. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "id_marca", referencedColumnName = "id_marca"),
			@JoinColumn(name = "id_modelo", referencedColumnName = "id_modelo") })
	private Modelo modelo;

	/** Sede donde está la moto. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_sucursal")
	private Sucursal sucursal;


	/** Año del modelo de la moto. */
	@Column(name = "anio", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer anio;

	/** Color de la moto. */
	@Column(name = "color", nullable = false, length = 30)
	private String color;

	/** Kilómetros recorridos. */
	@Column(name = "kilometraje", nullable = false)
	private Integer kilometraje;

	/** Precio de lista. */
	@Column(name = "precio", nullable = false, precision = 12, scale = 2)
	private BigDecimal precio;

	/** Estado de la moto: DISPONIBLE, RESERVADA o VENDIDA. */
	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 10, updatable = false)
	private EstadoMoto estado = EstadoMoto.DISPONIBLE;

	/** Fecha en que la moto entró al inventario. */
	@Column(name = "fecha_ingreso", nullable = false)
	private LocalDate fechaIngreso;

	/** Constructor vacío que necesita JPA. */
	public Moto() {
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
	 * Cambia la placa de la moto.
	 *
	 * @param placa la placa de la moto
	 */
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	/**
	 * Devuelve el modelo de la moto.
	 *
	 * @return el modelo de la moto
	 */
	public Modelo getModelo() {
		return modelo;
	}

	/**
	 * Cambia el modelo de la moto.
	 *
	 * @param modelo el modelo de la moto
	 */
	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	/**
	 * Devuelve la sede donde está la moto.
	 *
	 * @return la sede donde está la moto
	 */
	public Sucursal getSucursal() {
		return sucursal;
	}

	/**
	 * Cambia la sede donde está la moto.
	 *
	 * @param sucursal la sede donde está la moto
	 */
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
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
	 * Cambia el año del modelo de la moto.
	 *
	 * @param anio el año del modelo de la moto
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
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
	 * Cambia el color de la moto.
	 *
	 * @param color el color de la moto
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Devuelve los kilómetros recorridos.
	 *
	 * @return los kilómetros recorridos
	 */
	public Integer getKilometraje() {
		return kilometraje;
	}

	/**
	 * Cambia los kilómetros recorridos.
	 *
	 * @param kilometraje los kilómetros recorridos
	 */
	public void setKilometraje(Integer kilometraje) {
		this.kilometraje = kilometraje;
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

	/**
	 * Devuelve el estado de la moto: DISPONIBLE, RESERVADA o VENDIDA.
	 *
	 * @return el estado de la moto: DISPONIBLE, RESERVADA o VENDIDA
	 */
	public EstadoMoto getEstado() {
		return estado;
	}

	/**
	 * Cambia el estado de la moto: DISPONIBLE, RESERVADA o VENDIDA.
	 *
	 * @param estado el estado de la moto: DISPONIBLE, RESERVADA o VENDIDA
	 */
	public void setEstado(EstadoMoto estado) {
		this.estado = estado;
	}

	/**
	 * Devuelve la fecha en que la moto entró al inventario.
	 *
	 * @return la fecha en que la moto entró al inventario
	 */
	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * Cambia la fecha en que la moto entró al inventario.
	 *
	 * @param fechaIngreso la fecha en que la moto entró al inventario
	 */
	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Moto otra)) {
			return false;
		}
		return placa != null && placa.equals(otra.placa);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(placa);
	}
}

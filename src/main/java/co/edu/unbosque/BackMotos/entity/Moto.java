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

@Entity
@Table(name = "moto")
public class Moto {

	@Id
	@Column(name = "placa", length = 6)
	private String placa;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "id_marca", referencedColumnName = "id_marca"),
			@JoinColumn(name = "nro_modelo", referencedColumnName = "nro_modelo") })
	private Modelo modelo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal")
	private Sucursal sucursal;

	@Enumerated(EnumType.STRING)
	@Column(name = "origen", nullable = false, length = 10)
	private OrigenMoto origen;

	@Column(name = "anio", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer anio;

	@Column(name = "color", nullable = false, length = 30)
	private String color;

	@Column(name = "kilometraje", nullable = false)
	private Integer kilometraje;

	@Column(name = "precio", precision = 12, scale = 2)
	private BigDecimal precio;

	@Column(name = "fecha_ingreso", nullable = false)
	private LocalDate fechaIngreso;

	public Moto() {
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public OrigenMoto getOrigen() {
		return origen;
	}

	public void setOrigen(OrigenMoto origen) {
		this.origen = origen;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getKilometraje() {
		return kilometraje;
	}

	public void setKilometraje(Integer kilometraje) {
		this.kilometraje = kilometraje;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

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

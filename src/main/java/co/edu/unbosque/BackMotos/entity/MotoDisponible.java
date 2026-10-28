package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Immutable
@Subselect("SELECT * FROM v_moto_disponible")
@Synchronize({ "moto", "modelo", "marca", "sucursal", "venta_moto", "reserva" })
public class MotoDisponible {

	@Id
	@Column(name = "placa")
	private String placa;

	@Column(name = "marca")
	private String marca;

	@Column(name = "modelo")
	private String modelo;

	@Column(name = "tipo_moto")
	private String tipoMoto;

	@Column(name = "cilindraje")
	private Integer cilindraje;

	@Column(name = "anio")
	private Integer anio;

	@Column(name = "color")
	private String color;

	@Column(name = "precio")
	private BigDecimal precio;

	@Column(name = "id_sucursal")
	private Integer idSucursal;

	@Column(name = "sucursal")
	private String sucursal;

	@Column(name = "ciudad")
	private String ciudad;

	protected MotoDisponible() {
	}

	public String getPlaca() {
		return placa;
	}

	public String getMarca() {
		return marca;
	}

	public String getModelo() {
		return modelo;
	}

	public String getTipoMoto() {
		return tipoMoto;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public Integer getAnio() {
		return anio;
	}

	public String getColor() {
		return color;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public Integer getIdSucursal() {
		return idSucursal;
	}

	public String getSucursal() {
		return sucursal;
	}

	public String getCiudad() {
		return ciudad;
	}
}

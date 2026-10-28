package co.edu.unbosque.BackMotos.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sucursal")
public class Sucursal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sucursal")
	private Integer idSucursal;

	@Column(name = "nombre", nullable = false, length = 80, unique = true)
	private String nombre;

	@Column(name = "ciudad", nullable = false, length = 60)
	private String ciudad;

	@Column(name = "barrio", nullable = false, length = 60)
	private String barrio;

	@Column(name = "calle", nullable = false, length = 60)
	private String calle;

	@Column(name = "numero_direccion", nullable = false, length = 20)
	private String numeroDireccion;

	public Sucursal() {
	}

	public Integer getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Sucursal otra)) {
			return false;
		}
		return idSucursal != null && idSucursal.equals(otra.idSucursal);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(idSucursal);
	}
}

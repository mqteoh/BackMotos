package co.edu.unbosque.BackMotos.entity;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accesorio")
public class Accesorio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_accesorio")
	private Integer idAccesorio;

	@Column(name = "nombre", nullable = false, length = 80)
	private String nombre;

	@Column(name = "marca", nullable = false, length = 60)
	private String marca;

	@Column(name = "descripcion", length = 255)
	private String descripcion;

	@Column(name = "precio", nullable = false, precision = 12, scale = 2)
	private BigDecimal precio;

	public Accesorio() {
	}

	public Integer getIdAccesorio() {
		return idAccesorio;
	}

	public void setIdAccesorio(Integer idAccesorio) {
		this.idAccesorio = idAccesorio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

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

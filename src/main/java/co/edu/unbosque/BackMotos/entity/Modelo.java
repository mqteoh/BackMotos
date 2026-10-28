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

@Entity
@Table(name = "modelo")
public class Modelo {

	@EmbeddedId
	private ModeloId id;

	@MapsId("idMarca")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_marca")
	private Marca marca;

	@Column(name = "nombre", nullable = false, length = 80)
	private String nombre;

	@Column(name = "cilindraje", nullable = false)
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer cilindraje;

	@Column(name = "tipo_moto", nullable = false, length = 30)
	private String tipoMoto;

	@Column(name = "potencia_hp", nullable = false, precision = 5, scale = 1)
	private BigDecimal potenciaHp;

	@Column(name = "capacidad_tanque", nullable = false, precision = 4, scale = 1)
	private BigDecimal capacidadTanque;

	public Modelo() {
	}

	public ModeloId getId() {
		return id;
	}

	public void setId(ModeloId id) {
		this.id = id;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public void setCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
	}

	public String getTipoMoto() {
		return tipoMoto;
	}

	public void setTipoMoto(String tipoMoto) {
		this.tipoMoto = tipoMoto;
	}

	public BigDecimal getPotenciaHp() {
		return potenciaHp;
	}

	public void setPotenciaHp(BigDecimal potenciaHp) {
		this.potenciaHp = potenciaHp;
	}

	public BigDecimal getCapacidadTanque() {
		return capacidadTanque;
	}

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

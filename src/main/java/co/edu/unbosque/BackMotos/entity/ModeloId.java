package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ModeloId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id_marca")
	private Integer idMarca;

	@Column(name = "nro_modelo")
	@JdbcTypeCode(SqlTypes.SMALLINT)
	private Integer nroModelo;

	public ModeloId() {
	}

	public ModeloId(Integer idMarca, Integer nroModelo) {
		this.idMarca = idMarca;
		this.nroModelo = nroModelo;
	}

	public Integer getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(Integer idMarca) {
		this.idMarca = idMarca;
	}

	public Integer getNroModelo() {
		return nroModelo;
	}

	public void setNroModelo(Integer nroModelo) {
		this.nroModelo = nroModelo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ModeloId otro)) {
			return false;
		}
		return Objects.equals(idMarca, otro.idMarca) && Objects.equals(nroModelo, otro.nroModelo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idMarca, nroModelo);
	}
}

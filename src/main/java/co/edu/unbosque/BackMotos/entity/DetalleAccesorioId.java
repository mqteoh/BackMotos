package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DetalleAccesorioId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id_venta_accesorio")
	private Integer idVentaAccesorio;

	@Column(name = "id_accesorio")
	private Integer idAccesorio;

	public DetalleAccesorioId() {
	}

	public DetalleAccesorioId(Integer idVentaAccesorio, Integer idAccesorio) {
		this.idVentaAccesorio = idVentaAccesorio;
		this.idAccesorio = idAccesorio;
	}

	public Integer getIdVentaAccesorio() {
		return idVentaAccesorio;
	}

	public void setIdVentaAccesorio(Integer idVentaAccesorio) {
		this.idVentaAccesorio = idVentaAccesorio;
	}

	public Integer getIdAccesorio() {
		return idAccesorio;
	}

	public void setIdAccesorio(Integer idAccesorio) {
		this.idAccesorio = idAccesorio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DetalleAccesorioId otro)) {
			return false;
		}
		return Objects.equals(idVentaAccesorio, otro.idVentaAccesorio) && Objects.equals(idAccesorio, otro.idAccesorio);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idVentaAccesorio, idAccesorio);
	}
}

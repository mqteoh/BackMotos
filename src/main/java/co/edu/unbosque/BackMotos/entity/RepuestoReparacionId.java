package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RepuestoReparacionId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id_reparacion")
	private Integer idReparacion;

	@Column(name = "id_repuesto")
	private Integer idRepuesto;

	public RepuestoReparacionId() {
	}

	public RepuestoReparacionId(Integer idReparacion, Integer idRepuesto) {
		this.idReparacion = idReparacion;
		this.idRepuesto = idRepuesto;
	}

	public Integer getIdReparacion() {
		return idReparacion;
	}

	public void setIdReparacion(Integer idReparacion) {
		this.idReparacion = idReparacion;
	}

	public Integer getIdRepuesto() {
		return idRepuesto;
	}

	public void setIdRepuesto(Integer idRepuesto) {
		this.idRepuesto = idRepuesto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RepuestoReparacionId otro)) {
			return false;
		}
		return Objects.equals(idReparacion, otro.idReparacion) && Objects.equals(idRepuesto, otro.idRepuesto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idReparacion, idRepuesto);
	}
}

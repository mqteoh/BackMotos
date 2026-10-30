package co.edu.unbosque.BackMotos.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class SuministroId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "nit", length = 15)
	private String nit;

	@Column(name = "id_repuesto")
	private Integer idRepuesto;

	@Column(name = "id_sucursal")
	private Integer idSucursal;

	@Column(name = "fecha_suministro")
	private LocalDateTime fechaSuministro;

	public SuministroId() {
	}

	public SuministroId(String nit, Integer idRepuesto, Integer idSucursal, LocalDateTime fechaSuministro) {
		this.nit = nit;
		this.idRepuesto = idRepuesto;
		this.idSucursal = idSucursal;
		this.fechaSuministro = fechaSuministro;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public Integer getIdRepuesto() {
		return idRepuesto;
	}

	public void setIdRepuesto(Integer idRepuesto) {
		this.idRepuesto = idRepuesto;
	}

	public Integer getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	public LocalDateTime getFechaSuministro() {
		return fechaSuministro;
	}

	public void setFechaSuministro(LocalDateTime fechaSuministro) {
		this.fechaSuministro = fechaSuministro;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SuministroId otro)) {
			return false;
		}
		return Objects.equals(nit, otro.nit) && Objects.equals(idRepuesto, otro.idRepuesto)
				&& Objects.equals(idSucursal, otro.idSucursal) && Objects.equals(fechaSuministro, otro.fechaSuministro);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nit, idRepuesto, idSucursal, fechaSuministro);
	}
}

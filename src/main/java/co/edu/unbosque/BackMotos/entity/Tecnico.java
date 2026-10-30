package co.edu.unbosque.BackMotos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tecnico")
@PrimaryKeyJoinColumn(name = "id_empleado")
@DiscriminatorValue("TECNICO")
public class Tecnico extends Empleado {

	@Column(name = "certificado_tecnico", nullable = false, length = 80)
	private String certificadoTecnico;

	@Column(name = "especialidad", nullable = false, length = 60)
	private String especialidad;

	public Tecnico() {
	}

	@Override
	@Transient
	public TipoEmpleado getTipoEmpleado() {
		return TipoEmpleado.TECNICO;
	}

	public String getCertificadoTecnico() {
		return certificadoTecnico;
	}

	public void setCertificadoTecnico(String certificadoTecnico) {
		this.certificadoTecnico = certificadoTecnico;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
}

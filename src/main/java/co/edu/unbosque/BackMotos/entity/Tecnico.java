package co.edu.unbosque.BackMotos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/** Empleado que trabaja en el taller (tabla tecnico). */
@Entity
@Table(name = "tecnico")
@PrimaryKeyJoinColumn(name = "id_empleado")
public class Tecnico extends Empleado {

	/** Certificado del técnico; solo para técnicos. */
	@Column(name = "certificado_tecnico", nullable = false, length = 80)
	private String certificadoTecnico;

	/** Especialidad del técnico; solo para técnicos. */
	@Column(name = "especialidad", nullable = false, length = 60)
	private String especialidad;

	/** Constructor vacío que necesita JPA. */
	public Tecnico() {
	}

	@Override
	@Transient
	public TipoEmpleado getTipoEmpleado() {
		return TipoEmpleado.TECNICO;
	}

	/**
	 * Devuelve el certificado del técnico; solo para técnicos.
	 *
	 * @return el certificado del técnico; solo para técnicos
	 */
	public String getCertificadoTecnico() {
		return certificadoTecnico;
	}

	/**
	 * Cambia el certificado del técnico; solo para técnicos.
	 *
	 * @param certificadoTecnico el certificado del técnico; solo para técnicos
	 */
	public void setCertificadoTecnico(String certificadoTecnico) {
		this.certificadoTecnico = certificadoTecnico;
	}

	/**
	 * Devuelve la especialidad del técnico; solo para técnicos.
	 *
	 * @return la especialidad del técnico; solo para técnicos
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * Cambia la especialidad del técnico; solo para técnicos.
	 *
	 * @param especialidad la especialidad del técnico; solo para técnicos
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
}

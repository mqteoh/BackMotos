package co.edu.unbosque.BackMotos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Persona {

	@Column(name = "documento", nullable = false, length = 15, unique = true)
	private String documento;

	@Column(name = "nombres", nullable = false, length = 60)
	private String nombres;

	@Column(name = "apellidos", nullable = false, length = 60)
	private String apellidos;

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombreCompleto() {
		return nombres + " " + apellidos;
	}
}

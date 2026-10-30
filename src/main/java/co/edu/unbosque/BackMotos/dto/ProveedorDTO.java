package co.edu.unbosque.BackMotos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProveedorDTO {

	@NotBlank(message = "El NIT es obligatorio.")
	@Pattern(regexp = "^[0-9]{6,12}-[0-9]$", message = "El NIT debe tener el formato 000000000-0.")
	private String nit;

	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 100, message = "El nombre admite máximo 100 caracteres.")
	private String nombre;

	@NotBlank(message = "El teléfono es obligatorio.")
	@Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos.")
	private String telefono;

	@NotBlank(message = "El correo es obligatorio.")
	@Email(message = "El correo no es válido.")
	@Size(max = 120, message = "El correo admite máximo 120 caracteres.")
	private String correo;

	public ProveedorDTO() {
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
}

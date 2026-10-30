package co.edu.unbosque.BackMotos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MarcaDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idMarca;

	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 60, message = "El nombre admite máximo 60 caracteres.")
	private String nombre;

	@NotBlank(message = "El país de origen es obligatorio.")
	@Size(max = 60, message = "El país de origen admite máximo 60 caracteres.")
	private String paisOrigen;

	public MarcaDTO() {
	}

	public MarcaDTO(String nombre, String paisOrigen) {
		this.nombre = nombre;
		this.paisOrigen = paisOrigen;
	}

	public Integer getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(Integer idMarca) {
		this.idMarca = idMarca;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPaisOrigen() {
		return paisOrigen;
	}

	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}
}

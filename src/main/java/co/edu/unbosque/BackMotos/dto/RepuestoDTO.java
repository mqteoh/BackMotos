package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RepuestoDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idRepuesto;

	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	@NotBlank(message = "La categoría es obligatoria.")
	@Size(max = 40, message = "La categoría admite máximo 40 caracteres.")
	private String categoria;

	@NotNull(message = "El precio es obligatorio.")
	@Positive(message = "El precio debe ser mayor que cero.")
	@Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.")
	private BigDecimal precio;

	public RepuestoDTO() {
	}

	public Integer getIdRepuesto() {
		return idRepuesto;
	}

	public void setIdRepuesto(Integer idRepuesto) {
		this.idRepuesto = idRepuesto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
}

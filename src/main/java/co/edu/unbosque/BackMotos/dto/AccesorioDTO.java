package co.edu.unbosque.BackMotos.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AccesorioDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idAccesorio;

	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	@NotBlank(message = "La marca es obligatoria.")
	@Size(max = 60, message = "La marca admite máximo 60 caracteres.")
	private String marca;

	@Size(max = 255, message = "La descripción admite máximo 255 caracteres.")
	private String descripcion;

	@NotNull(message = "El precio es obligatorio.")
	@Positive(message = "El precio debe ser mayor que cero.")
	@Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales.")
	private BigDecimal precio;

	public AccesorioDTO() {
	}

	public Integer getIdAccesorio() {
		return idAccesorio;
	}

	public void setIdAccesorio(Integer idAccesorio) {
		this.idAccesorio = idAccesorio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
}

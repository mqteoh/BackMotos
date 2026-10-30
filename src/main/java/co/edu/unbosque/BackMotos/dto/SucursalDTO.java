package co.edu.unbosque.BackMotos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SucursalDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer idSucursal;

	@NotBlank(message = "El nombre es obligatorio.")
	@Size(max = 80, message = "El nombre admite máximo 80 caracteres.")
	private String nombre;

	@NotBlank(message = "La ciudad es obligatoria.")
	@Size(max = 60, message = "La ciudad admite máximo 60 caracteres.")
	private String ciudad;

	@NotBlank(message = "El barrio es obligatorio.")
	@Size(max = 60, message = "El barrio admite máximo 60 caracteres.")
	private String barrio;

	@NotBlank(message = "La calle es obligatoria.")
	@Size(max = 60, message = "La calle admite máximo 60 caracteres.")
	private String calle;

	@NotBlank(message = "El número de la dirección es obligatorio.")
	@Size(max = 20, message = "El número de la dirección admite máximo 20 caracteres.")
	private String numeroDireccion;

	public SucursalDTO() {
	}

	public Integer getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumeroDireccion() {
		return numeroDireccion;
	}

	public void setNumeroDireccion(String numeroDireccion) {
		this.numeroDireccion = numeroDireccion;
	}
}

package co.edu.unbosque.BackMotos.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Formato único de los errores de la API.
 *
 * @param estado código de estado HTTP
 * @param mensaje mensaje para el usuario
 * @param campos errores de cada campo del formulario; vacío si el error no es de un campo
 * @param ruta ruta de la petición que falló
 * @param fecha fecha y hora del error
 */
public record ErrorDTO(int estado, String mensaje, List<ErrorCampo> campos, String ruta, LocalDateTime fecha) {

	/**
	 * Campo con un valor no válido y su mensaje.
	 *
	 * @param campo nombre del campo que tiene el error
	 * @param mensaje qué está mal en el campo
	 */
	public record ErrorCampo(String campo, String mensaje) {
	}

	/**
	 * Error sin detalle de campos.
	 *
	 * @param estado código de estado HTTP
	 * @param mensaje mensaje para el usuario
	 * @param ruta ruta de la petición que falló
	 * @return el error listo para responder
	 */
	public static ErrorDTO de(int estado, String mensaje, String ruta) {
		return new ErrorDTO(estado, mensaje, List.of(), ruta, LocalDateTime.now());
	}

	/**
	 * Error de validación con la lista de campos.
	 *
	 * @param estado código de estado HTTP
	 * @param mensaje mensaje para el usuario
	 * @param campos errores de cada campo del formulario; vacío si el error no es de un campo
	 * @param ruta ruta de la petición que falló
	 * @return el error listo para responder
	 */
	public static ErrorDTO deCampos(int estado, String mensaje, List<ErrorCampo> campos, String ruta) {
		return new ErrorDTO(estado, mensaje, campos, ruta, LocalDateTime.now());
	}
}

package co.edu.unbosque.BackMotos.exception;

/** Datos de la petición que no son válidos; responde 400. */
public class BadRequestException extends RuntimeException {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/**
	 * Crea la excepción con el mensaje para el usuario.
	 *
	 * @param mensaje mensaje para el usuario
	 */
	public BadRequestException(String mensaje) {
		super(mensaje);
	}
}

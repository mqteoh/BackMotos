package co.edu.unbosque.BackMotos.exception;

/** El registro pedido no existe; responde 404. */
public class ResourceNotFoundException extends RuntimeException {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/**
	 * Crea la excepción con el mensaje para el usuario.
	 *
	 * @param mensaje mensaje para el usuario
	 */
	public ResourceNotFoundException(String mensaje) {
		super(mensaje);
	}
}

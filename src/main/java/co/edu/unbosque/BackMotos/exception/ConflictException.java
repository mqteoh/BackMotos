package co.edu.unbosque.BackMotos.exception;

/** La operación choca con una regla del negocio; responde 409. */
public class ConflictException extends RuntimeException {

	/** Versión de la clase para serializarla. */
	private static final long serialVersionUID = 1L;

	/**
	 * Crea la excepción con el mensaje para el usuario.
	 *
	 * @param mensaje mensaje para el usuario
	 */
	public ConflictException(String mensaje) {
		super(mensaje);
	}
}

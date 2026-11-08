package co.edu.unbosque.BackMotos.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import co.edu.unbosque.BackMotos.dto.ErrorDTO;
import co.edu.unbosque.BackMotos.dto.ErrorDTO.ErrorCampo;
import co.edu.unbosque.BackMotos.util.TraductorErroresBD;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/** Convierte los errores en respuestas JSON con el mismo formato (ErrorDTO). */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/** Registro de mensajes de la clase. */
	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/** Constructor que usa Spring para crear el manejador. */
	public GlobalExceptionHandler() {
	}

	/**
	 * Campos que no cumplen las validaciones del DTO; lista cada campo con su mensaje.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDTO> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
		List<ErrorCampo> campos = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> new ErrorCampo(e.getField(), e.getDefaultMessage())).toList();
		return responder(ErrorDTO.deCampos(400, "Hay datos inválidos en la petición.", campos, req.getRequestURI()));
	}

	/**
	 * Parámetros de la URL que no cumplen las validaciones.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDTO> restriccion(ConstraintViolationException ex, HttpServletRequest req) {
		List<ErrorCampo> campos = ex.getConstraintViolations().stream()
				.map(v -> new ErrorCampo(v.getPropertyPath().toString(), v.getMessage())).toList();
		return responder(ErrorDTO.deCampos(400, "Hay datos inválidos en la petición.", campos, req.getRequestURI()));
	}

	/**
	 * JSON mal escrito o parámetro con un tipo que no corresponde.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
			MissingServletRequestParameterException.class })
	public ResponseEntity<ErrorDTO> peticionMalFormada(Exception ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(400, "La petición no tiene el formato esperado.", req.getRequestURI()));
	}

	/**
	 * Datos rechazados por un servicio.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorDTO> badRequest(BadRequestException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(400, ex.getMessage(), req.getRequestURI()));
	}

	/**
	 * Registro o ruta que no existe.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler({ ResourceNotFoundException.class, NoResourceFoundException.class })
	public ResponseEntity<ErrorDTO> noEncontrado(Exception ex, HttpServletRequest req) {
		String mensaje = ex instanceof ResourceNotFoundException ? ex.getMessage() : "El recurso solicitado no existe.";
		return responder(ErrorDTO.de(404, mensaje, req.getRequestURI()));
	}

	/**
	 * Método HTTP que la ruta no acepta.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorDTO> metodoNoPermitido(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest req) {
		return responder(ErrorDTO.de(405, "El método " + ex.getMethod() + " no está permitido en esta ruta.",
				req.getRequestURI()));
	}

	/**
	 * Correo o contraseña incorrectos.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorDTO> credencialesInvalidas(BadCredentialsException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(401, "Correo o contraseña incorrectos.", req.getRequestURI()));
	}

	/**
	 * Petición sin sesión iniciada.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorDTO> noAutenticado(AuthenticationException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(401, "Debe iniciar sesión para continuar.", req.getRequestURI()));
	}

	/**
	 * Usuario sin permiso para la ruta.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDTO> sinPermiso(AccessDeniedException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(403, "No tiene permiso para realizar esta acción.", req.getRequestURI()));
	}

	/**
	 * Regla del negocio que impide la operación.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorDTO> conflicto(ConflictException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(409, ex.getMessage(), req.getRequestURI()));
	}

	/**
	 * Error de la base de datos; si es una regla conocida, devuelve un mensaje claro.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorDTO> baseDeDatos(DataAccessException ex, HttpServletRequest req) {
		return TraductorErroresBD.traducir(ex)
				.map(e -> responder(ErrorDTO.de(e.estado().value(), e.mensaje(), req.getRequestURI())))
				.orElseGet(() -> inesperado(ex, req));
	}

	/**
	 * Cualquier otro error; responde 500 sin mostrar detalles internos.
	 *
	 * @param ex excepción que se lanzó
	 * @param req petición que causó el error
	 * @return la respuesta con el error y su código HTTP
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDTO> inesperado(Exception ex, HttpServletRequest req) {
		LOG.error("Error no controlado en {}", req.getRequestURI(), ex);
		return responder(ErrorDTO.de(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Ocurrió un error inesperado. Intente de nuevo más tarde.", req.getRequestURI()));
	}

	/**
	 * Arma la respuesta con el código del error.
	 *
	 * @param error error que se responde
	 * @return la respuesta con el error y su código HTTP
	 */
	private ResponseEntity<ErrorDTO> responder(ErrorDTO error) {
		return ResponseEntity.status(error.estado()).body(error);
	}
}

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

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDTO> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
		List<ErrorCampo> campos = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> new ErrorCampo(e.getField(), e.getDefaultMessage())).toList();
		return responder(ErrorDTO.deCampos(400, "Hay datos inválidos en la petición.", campos, req.getRequestURI()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDTO> restriccion(ConstraintViolationException ex, HttpServletRequest req) {
		List<ErrorCampo> campos = ex.getConstraintViolations().stream()
				.map(v -> new ErrorCampo(v.getPropertyPath().toString(), v.getMessage())).toList();
		return responder(ErrorDTO.deCampos(400, "Hay datos inválidos en la petición.", campos, req.getRequestURI()));
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
			MissingServletRequestParameterException.class })
	public ResponseEntity<ErrorDTO> peticionMalFormada(Exception ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(400, "La petición no tiene el formato esperado.", req.getRequestURI()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorDTO> badRequest(BadRequestException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(400, ex.getMessage(), req.getRequestURI()));
	}

	@ExceptionHandler({ ResourceNotFoundException.class, NoResourceFoundException.class })
	public ResponseEntity<ErrorDTO> noEncontrado(Exception ex, HttpServletRequest req) {
		String mensaje = ex instanceof ResourceNotFoundException ? ex.getMessage() : "El recurso solicitado no existe.";
		return responder(ErrorDTO.de(404, mensaje, req.getRequestURI()));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorDTO> metodoNoPermitido(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest req) {
		return responder(ErrorDTO.de(405, "El método " + ex.getMethod() + " no está permitido en esta ruta.",
				req.getRequestURI()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorDTO> credencialesInvalidas(BadCredentialsException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(401, "Correo o contraseña incorrectos.", req.getRequestURI()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorDTO> noAutenticado(AuthenticationException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(401, "Debe iniciar sesión para continuar.", req.getRequestURI()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDTO> sinPermiso(AccessDeniedException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(403, "No tiene permiso para realizar esta acción.", req.getRequestURI()));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorDTO> conflicto(ConflictException ex, HttpServletRequest req) {
		return responder(ErrorDTO.de(409, ex.getMessage(), req.getRequestURI()));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorDTO> baseDeDatos(DataAccessException ex, HttpServletRequest req) {
		return TraductorErroresBD.traducir(ex)
				.map(e -> responder(ErrorDTO.de(e.estado().value(), e.mensaje(), req.getRequestURI())))
				.orElseGet(() -> inesperado(ex, req));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDTO> inesperado(Exception ex, HttpServletRequest req) {
		LOG.error("Error no controlado en {}", req.getRequestURI(), ex);
		return responder(ErrorDTO.de(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Ocurrió un error inesperado. Intente de nuevo más tarde.", req.getRequestURI()));
	}

	private ResponseEntity<ErrorDTO> responder(ErrorDTO error) {
		return ResponseEntity.status(error.estado()).body(error);
	}
}

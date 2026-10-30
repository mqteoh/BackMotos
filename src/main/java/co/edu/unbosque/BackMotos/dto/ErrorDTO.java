package co.edu.unbosque.BackMotos.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorDTO(int estado, String mensaje, List<ErrorCampo> campos, String ruta, LocalDateTime fecha) {

	public record ErrorCampo(String campo, String mensaje) {
	}

	public static ErrorDTO de(int estado, String mensaje, String ruta) {
		return new ErrorDTO(estado, mensaje, List.of(), ruta, LocalDateTime.now());
	}

	public static ErrorDTO deCampos(int estado, String mensaje, List<ErrorCampo> campos, String ruta) {
		return new ErrorDTO(estado, mensaje, campos, ruta, LocalDateTime.now());
	}
}

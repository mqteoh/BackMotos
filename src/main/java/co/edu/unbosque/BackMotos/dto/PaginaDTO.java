package co.edu.unbosque.BackMotos.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public record PaginaDTO<T>(List<T> contenido, int pagina, int tamano, long totalElementos, int totalPaginas) {

	public static <E, T> PaginaDTO<T> de(Page<E> pagina, Function<E, T> conversion) {
		return new PaginaDTO<>(pagina.getContent().stream().map(conversion).toList(), pagina.getNumber(),
				pagina.getSize(), pagina.getTotalElements(), pagina.getTotalPages());
	}
}

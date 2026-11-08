package co.edu.unbosque.BackMotos.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

/**
 * Una página de resultados con el total de elementos y de páginas.
 *
 * @param <T> tipo de los elementos
 * @param contenido elementos de la página
 * @param pagina número de página, desde 0
 * @param tamano elementos por página
 * @param totalElementos total de elementos
 * @param totalPaginas total de páginas
 */
public record PaginaDTO<T>(List<T> contenido, int pagina, int tamano, long totalElementos, int totalPaginas) {

	/**
	 * Convierte una página de Spring usando la función indicada.
	 *
	 * @param <E> tipo de los elementos de la página de Spring
	 * @param <T> tipo de los elementos convertidos
	 * @param pagina página que devuelve Spring Data
	 * @param conversion función que convierte cada elemento
	 * @return la página con los elementos convertidos
	 */
	public static <E, T> PaginaDTO<T> de(Page<E> pagina, Function<E, T> conversion) {
		return new PaginaDTO<>(pagina.getContent().stream().map(conversion).toList(), pagina.getNumber(),
				pagina.getSize(), pagina.getTotalElements(), pagina.getTotalPages());
	}
}

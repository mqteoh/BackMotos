package co.edu.unbosque.BackMotos.dto;

import org.springframework.data.domain.Sort;

/** Formas de ordenar el catálogo. */
public enum OrdenCatalogo {

	/** Por marca, modelo y placa. */
	MARCA(Sort.by("marca", "modelo", "placa")),

	/** Del precio más bajo al más alto. */
	PRECIO_ASC(Sort.by(Sort.Order.asc("precio"), Sort.Order.asc("placa"))),

	/** Del precio más alto al más bajo. */
	PRECIO_DESC(Sort.by(Sort.Order.desc("precio"), Sort.Order.asc("placa"))),

	/** Del cilindraje más alto al más bajo. */
	CILINDRAJE(Sort.by(Sort.Order.desc("cilindraje"), Sort.Order.asc("placa")));

	/** Orden de Spring que aplica esta opción. */
	private final Sort sort;

	/**
	 * Asocia la opción con su orden.
	 *
	 * @param sort orden de Spring que aplica esta opción
	 */
	OrdenCatalogo(Sort sort) {
		this.sort = sort;
	}

	/**
	 * Devuelve el orden de Spring que aplica esta opción.
	 *
	 * @return el orden de Spring que aplica esta opción
	 */
	public Sort getSort() {
		return sort;
	}
}

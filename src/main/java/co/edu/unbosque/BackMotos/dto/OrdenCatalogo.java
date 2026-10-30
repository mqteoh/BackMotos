package co.edu.unbosque.BackMotos.dto;

import org.springframework.data.domain.Sort;

public enum OrdenCatalogo {

	MARCA(Sort.by("marca", "modelo", "placa")),
	PRECIO_ASC(Sort.by(Sort.Order.asc("precio"), Sort.Order.asc("placa"))),
	PRECIO_DESC(Sort.by(Sort.Order.desc("precio"), Sort.Order.asc("placa"))),
	CILINDRAJE(Sort.by(Sort.Order.desc("cilindraje"), Sort.Order.asc("placa")));

	private final Sort sort;

	OrdenCatalogo(Sort sort) {
		this.sort = sort;
	}

	public Sort getSort() {
		return sort;
	}
}

package co.edu.unbosque.BackMotos.service;

import java.util.List;

/**
 * Operaciones básicas de un CRUD: crear, listar, buscar, actualizar y eliminar.
 *
 * @param <D>  DTO que se recibe y se devuelve
 * @param <ID> tipo de la llave
 */
public interface CRUDoperation<D, ID> {

	/**
	 * Guarda un registro nuevo.
	 *
	 * @param dto datos del registro
	 * @return el registro guardado
	 */
	D crear(D dto);

	/**
	 * Todos los registros.
	 *
	 * @return todos los registros
	 */
	List<D> listar();

	/**
	 * Busca un registro; si no existe lanza ResourceNotFoundException.
	 *
	 * @param id llave del registro
	 * @return el registro encontrado
	 */
	D buscarPorId(ID id);

	/**
	 * Cambia los datos de un registro que ya existe.
	 *
	 * @param id llave del registro
	 * @param dto datos del registro
	 * @return el registro con los cambios
	 */
	D actualizar(ID id, D dto);

	/**
	 * Borra un registro; la base lo impide si otros dependen de él.
	 *
	 * @param id llave del registro
	 */
	void eliminar(ID id);

	/**
	 * Cuántos registros hay.
	 *
	 * @return la cantidad de registros
	 */
	long contar();

	/**
	 * Dice si existe un registro con esa llave.
	 *
	 * @param id llave del registro
	 * @return true si existe
	 */
	boolean existe(ID id);
}

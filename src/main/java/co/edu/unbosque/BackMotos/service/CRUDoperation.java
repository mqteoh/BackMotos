package co.edu.unbosque.BackMotos.service;

import java.util.List;

public interface CRUDoperation<D, ID> {

	D crear(D dto);

	List<D> listar();

	D buscarPorId(ID id);

	D actualizar(ID id, D dto);

	void eliminar(ID id);

	long contar();

	boolean existe(ID id);
}

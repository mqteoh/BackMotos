package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Repuesto;

/** Acceso a la tabla repuesto. */
public interface RepuestoRepository extends JpaRepository<Repuesto, Integer> {

	/**
	 * Repuestos de una categoría, ordenados por nombre.
	 *
	 * @param categoria categoría del repuesto
	 * @return los repuestos de la categoría
	 */
	List<Repuesto> findByCategoriaIgnoreCaseOrderByNombre(String categoria);

	/**
	 * Repuestos cuyo nombre contiene el texto, ordenados por nombre.
	 *
	 * @param texto parte del nombre
	 * @return los repuestos encontrados
	 */
	List<Repuesto> findByNombreContainingIgnoreCaseOrderByNombre(String texto);
}

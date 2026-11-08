package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Accesorio;

/** Acceso a la tabla accesorio. */
public interface AccesorioRepository extends JpaRepository<Accesorio, Integer> {

	/**
	 * Accesorios de una marca, sin importar mayúsculas, ordenados por nombre.
	 *
	 * @param marca marca del accesorio
	 * @return los accesorios de la marca
	 */
	List<Accesorio> findByMarcaIgnoreCaseOrderByNombre(String marca);

	/**
	 * Accesorios cuyo nombre contiene el texto, ordenados por nombre.
	 *
	 * @param texto parte del nombre
	 * @return los accesorios encontrados
	 */
	List<Accesorio> findByNombreContainingIgnoreCaseOrderByNombre(String texto);
}

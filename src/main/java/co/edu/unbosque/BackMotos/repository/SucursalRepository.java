package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Sucursal;

/** Acceso a la tabla sucursal. */
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

	/**
	 * Sedes de una ciudad, ordenadas por nombre.
	 *
	 * @param ciudad ciudad
	 * @return las sedes de la ciudad
	 */
	List<Sucursal> findByCiudadIgnoreCaseOrderByNombre(String ciudad);

	/**
	 * Dice si ya existe una sede con ese nombre.
	 *
	 * @param nombre nombre de la sede
	 * @return true si el nombre ya está registrado
	 */
	boolean existsByNombreIgnoreCase(String nombre);
}

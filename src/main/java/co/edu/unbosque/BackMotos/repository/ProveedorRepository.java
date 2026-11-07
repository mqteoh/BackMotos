package co.edu.unbosque.BackMotos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Proveedor;

/** Acceso a la tabla proveedor. */
public interface ProveedorRepository extends JpaRepository<Proveedor, String> {

	/**
	 * Dice si ya existe un proveedor con ese nombre.
	 *
	 * @param nombre nombre del proveedor
	 * @return true si el nombre ya está registrado
	 */
	boolean existsByNombreIgnoreCase(String nombre);
}

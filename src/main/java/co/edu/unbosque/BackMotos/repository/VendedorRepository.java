package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Vendedor;

/** Acceso a los vendedores (tablas empleado y vendedor). */
public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {

	/**
	 * Vendedores de una sede.
	 *
	 * @param idSucursal identificador de la sede
	 * @return los vendedores de la sede
	 */
	List<Vendedor> findBySucursalIdSucursal(Integer idSucursal);
}

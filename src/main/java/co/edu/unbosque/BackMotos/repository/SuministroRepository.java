package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Suministro;
import co.edu.unbosque.BackMotos.entity.SuministroId;

/** Acceso a la tabla suministros. */
public interface SuministroRepository extends JpaRepository<Suministro, SuministroId> {

	/**
	 * Suministros de una sede, del más reciente al más antiguo.
	 *
	 * @param idSucursal identificador de la sede
	 * @return los suministros de la sede
	 */
	List<Suministro> findBySucursalIdSucursalOrderByIdFechaSuministroDesc(Integer idSucursal);

	/**
	 * Suministros de un proveedor, del más reciente al más antiguo.
	 *
	 * @param nit el NIT del proveedor
	 * @return los suministros del proveedor
	 */
	List<Suministro> findByProveedorNitOrderByIdFechaSuministroDesc(String nit);
}

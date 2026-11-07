package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Moto;

/**
 * Consultas de las motos del inventario.
 */
public interface MotoRepository extends JpaRepository<Moto, String> {

	/**
	 * Motos de una sede.
	 *
	 * @param idSucursal identificador de la sede
	 * @return las motos de la sede
	 */
	List<Moto> findBySucursalIdSucursal(Integer idSucursal);

	/**
	 * Motos que todavía no se han vendido (disponibles o reservadas), de una sede o de todas.
	 *
	 * @param idSucursal identificador de la sede
	 * @return las motos sin vender
	 */
	@Query("SELECT m FROM Moto m WHERE m.estado <> co.edu.unbosque.BackMotos.entity.EstadoMoto.VENDIDA"
			+ " AND (:idSucursal IS NULL OR m.sucursal.idSucursal = :idSucursal) ORDER BY m.sucursal.nombre, m.placa")
	List<Moto> sinVender(Integer idSucursal);
}

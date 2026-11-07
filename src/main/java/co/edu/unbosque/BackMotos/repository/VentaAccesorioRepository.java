package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.VentaAccesorio;

/** Acceso a las ventas de accesorios con sus líneas. */
public interface VentaAccesorioRepository extends JpaRepository<VentaAccesorio, Integer> {

	/**
	 * Compras de accesorios de un cliente, de la más reciente a la más antigua.
	 *
	 * @param idCliente identificador del cliente
	 * @return las ventas del cliente
	 */
	List<VentaAccesorio> findByClienteIdClienteOrderByFechaVentaDesc(Integer idCliente);

	/**
	 * Ventas de accesorios de un vendedor, de la más reciente a la más antigua.
	 *
	 * @param idVendedor identificador del vendedor
	 * @return las ventas del vendedor
	 */
	List<VentaAccesorio> findByVendedorIdEmpleadoOrderByFechaVentaDesc(Integer idVendedor);
}

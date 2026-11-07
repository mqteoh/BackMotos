package co.edu.unbosque.BackMotos.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.VentaMoto;

/** Acceso a la tabla venta_moto; la llave es la placa. */
public interface VentaMotoRepository extends JpaRepository<VentaMoto, String> {

	/**
	 * Venta de una moto.
	 *
	 * @param placa placa de la moto
	 * @return la venta, o vacío si la moto no se ha vendido
	 */
	Optional<VentaMoto> findByMotoPlaca(String placa);

	/**
	 * Dice si una moto ya se vendió.
	 *
	 * @param placa placa de la moto
	 * @return true si ya se vendió
	 */
	boolean existsByMotoPlaca(String placa);

	/**
	 * Motos que compró un cliente, de la compra más reciente a la más antigua.
	 *
	 * @param idCliente identificador del cliente
	 * @return las ventas del cliente
	 */
	List<VentaMoto> findByClienteIdClienteOrderByFechaVentaDesc(Integer idCliente);

	/**
	 * Ventas de motos de un vendedor, de la más reciente a la más antigua.
	 *
	 * @param idVendedor identificador del vendedor
	 * @return las ventas del vendedor
	 */
	List<VentaMoto> findByVendedorIdEmpleadoOrderByFechaVentaDesc(Integer idVendedor);

	/**
	 * Ventas de motos entre dos fechas, en orden de fecha.
	 *
	 * @param desde fecha y hora inicial
	 * @param hasta fecha y hora final
	 * @return las ventas del periodo
	 */
	List<VentaMoto> findByFechaVentaBetweenOrderByFechaVenta(LocalDateTime desde, LocalDateTime hasta);
}

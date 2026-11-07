package co.edu.unbosque.BackMotos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Reserva;

/** Acceso a la tabla reserva. */
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

	/**
	 * Reservas de un cliente, de la más reciente a la más antigua.
	 *
	 * @param idCliente identificador del cliente
	 * @return las reservas del cliente
	 */
	List<Reserva> findByClienteIdClienteOrderByFechaReservaDesc(Integer idCliente);

	/**
	 * Reserva de una moto en un estado.
	 *
	 * @param placa placa de la moto
	 * @param estado estado de la reserva
	 * @return la reserva, o vacío si no hay
	 */
	Optional<Reserva> findByMotoPlacaAndEstado(String placa, EstadoReserva estado);

	/**
	 * Reservas de una sede en un estado, de la más próxima a vencer a la más lejana.
	 *
	 * @param estado estado de la reserva
	 * @param idSucursal identificador de la sede
	 * @return las reservas encontradas
	 */
	List<Reserva> findByEstadoAndMotoSucursalIdSucursalOrderByFechaVencimiento(EstadoReserva estado, Integer idSucursal);

	/**
	 * Dice si una moto tiene una reserva en ese estado.
	 *
	 * @param placa placa de la moto
	 * @param estado estado de la reserva
	 * @return true si la tiene
	 */
	boolean existsByMotoPlacaAndEstado(String placa, EstadoReserva estado);

	/**
	 * Reservas en un estado, de la más próxima a vencer a la más lejana.
	 *
	 * @param estado estado de la reserva
	 * @return las reservas encontradas
	 */
	List<Reserva> findByEstadoOrderByFechaVencimiento(EstadoReserva estado);
}

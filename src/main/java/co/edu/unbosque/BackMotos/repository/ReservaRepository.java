package co.edu.unbosque.BackMotos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.EstadoReserva;
import co.edu.unbosque.BackMotos.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

	List<Reserva> findByClienteIdClienteOrderByFechaReservaDesc(Integer idCliente);

	Optional<Reserva> findByMotoPlacaAndEstado(String placa, EstadoReserva estado);

	List<Reserva> findByEstadoAndMotoSucursalIdSucursalOrderByFechaVencimiento(EstadoReserva estado, Integer idSucursal);

	boolean existsByMotoPlacaAndEstado(String placa, EstadoReserva estado);
}

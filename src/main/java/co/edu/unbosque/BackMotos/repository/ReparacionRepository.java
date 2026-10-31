package co.edu.unbosque.BackMotos.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.Reparacion;

public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {

	List<Reparacion> findByClienteIdClienteOrderByFechaAgendadaDesc(Integer idCliente);

	List<Reparacion> findByMotoPlacaOrderByFechaAgendada(String placa);

	List<Reparacion> findBySucursalIdSucursalAndEstadoOrderByFechaAgendada(Integer idSucursal, EstadoReparacion estado);

	List<Reparacion> findByTecnicoIdEmpleadoOrderByFechaAgendadaDesc(Integer idTecnico);

	boolean existsByMotoPlacaAndEstadoIn(String placa, Collection<EstadoReparacion> estados);

	@Query("SELECT r FROM Reparacion r WHERE (:idSucursal IS NULL OR r.sucursal.idSucursal = :idSucursal)"
			+ " AND r.estado IN :estados AND (:desde IS NULL OR r.fechaAgendada >= :desde)"
			+ " AND (:hasta IS NULL OR r.fechaAgendada < :hasta) ORDER BY r.fechaAgendada")
	List<Reparacion> agenda(Integer idSucursal, Collection<EstadoReparacion> estados, LocalDateTime desde,
			LocalDateTime hasta);
}

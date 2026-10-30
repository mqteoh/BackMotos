package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.EstadoReparacion;
import co.edu.unbosque.BackMotos.entity.Reparacion;

public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {

	List<Reparacion> findByClienteIdClienteOrderByFechaAgendadaDesc(Integer idCliente);

	List<Reparacion> findByMotoPlacaOrderByFechaAgendada(String placa);

	List<Reparacion> findBySucursalIdSucursalAndEstadoOrderByFechaAgendada(Integer idSucursal, EstadoReparacion estado);

	List<Reparacion> findByTecnicoIdEmpleadoOrderByFechaAgendadaDesc(Integer idTecnico);
}

package co.edu.unbosque.BackMotos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Reparacion;

/**
 * Consultas de las reparaciones del taller.
 */
public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {

	/**
	 * Historial de una moto, de la más reciente a la más antigua.
	 *
	 * @param placa placa de la moto
	 * @return historial de una moto, de la más reciente a la más antigua
	 */
	List<Reparacion> findByMotoPlacaOrderByFechaDescIdReparacionDesc(String placa);

	/**
	 * Reparaciones de las motos que compró un cliente.
	 *
	 * @param idCliente identificador del cliente
	 * @return las reparaciones del cliente
	 */
	@Query("SELECT r FROM Reparacion r WHERE r.moto.placa IN"
			+ " (SELECT v.placa FROM VentaMoto v WHERE v.cliente.idCliente = :idCliente)"
			+ " ORDER BY r.fecha DESC, r.idReparacion DESC")
	List<Reparacion> deCliente(Integer idCliente);

	/**
	 * Reparaciones en las que participó un técnico.
	 *
	 * @param idTecnico identificador del técnico
	 * @return las reparaciones del técnico
	 */
	@Query("SELECT r FROM Reparacion r JOIN r.tecnicos t WHERE t.idEmpleado = :idTecnico"
			+ " ORDER BY r.fecha DESC, r.idReparacion DESC")
	List<Reparacion> deTecnico(Integer idTecnico);

	/**
	 * Reparaciones de una sede entre dos fechas. Los filtros vacíos no se aplican.
	 * La sede de una reparación es la de sus técnicos.
	 *
	 * @param idSucursal identificador de la sede
	 * @param desde fecha inicial; null para no filtrar
	 * @param hasta fecha final; null para no filtrar
	 * @return las reparaciones encontradas
	 */
	@Query("SELECT DISTINCT r FROM Reparacion r JOIN r.tecnicos t"
			+ " WHERE (:idSucursal IS NULL OR t.sucursal.idSucursal = :idSucursal)"
			+ " AND (CAST(:desde AS LocalDate) IS NULL OR r.fecha >= :desde)"
			+ " AND (CAST(:hasta AS LocalDate) IS NULL OR r.fecha <= :hasta)"
			+ " ORDER BY r.fecha DESC, r.idReparacion DESC")
	List<Reparacion> buscar(Integer idSucursal, LocalDate desde, LocalDate hasta);
}

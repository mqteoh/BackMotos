package co.edu.unbosque.BackMotos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Empleado;

/** Acceso a la tabla empleado y sus subclases. */
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

	/**
	 * Busca un empleado por documento.
	 *
	 * @param documento número de documento de identidad
	 * @return el empleado, o vacío si no existe
	 */
	Optional<Empleado> findByDocumento(String documento);

	/**
	 * Empleados de una sede.
	 *
	 * @param idSucursal identificador de la sede
	 * @return los empleados de la sede
	 */
	List<Empleado> findBySucursalIdSucursal(Integer idSucursal);

	/**
	 * Empleados a cargo de un jefe.
	 *
	 * @param idJefe identificador del jefe directo; null si no tiene
	 * @return los empleados a cargo
	 */
	List<Empleado> findByJefeIdEmpleado(Integer idJefe);

	/**
	 * Dice si ya hay un empleado con ese documento.
	 *
	 * @param documento número de documento de identidad
	 * @return true si el documento ya está registrado
	 */
	boolean existsByDocumento(String documento);

}

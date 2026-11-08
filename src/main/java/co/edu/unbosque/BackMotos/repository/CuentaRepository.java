package co.edu.unbosque.BackMotos.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Cuenta;

/**
 * Consultas de las cuentas de acceso a la página.
 */
public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {

	/**
	 * Busca una cuenta por correo, sin importar mayúsculas.
	 *
	 * @param correo correo electrónico
	 * @return la cuenta, o vacío si no existe
	 */
	Optional<Cuenta> findByCorreoIgnoreCase(String correo);

	/**
	 * Dice si el correo ya tiene una cuenta.
	 *
	 * @param correo correo electrónico
	 * @return true si el correo ya está registrado
	 */
	boolean existsByCorreoIgnoreCase(String correo);

	/**
	 * Cuenta web de un cliente.
	 *
	 * @param idCliente identificador del cliente
	 * @return la cuenta, o vacío si el cliente no tiene
	 */
	Optional<Cuenta> findByClienteIdCliente(Integer idCliente);

	/**
	 * Cuenta de un empleado.
	 *
	 * @param idEmpleado identificador del empleado
	 * @return la cuenta, o vacío si el empleado no tiene
	 */
	Optional<Cuenta> findByEmpleadoIdEmpleado(Integer idEmpleado);

	/**
	 * Ids de los clientes que tienen cuenta web.
	 *
	 * @return ids de los clientes que tienen cuenta web
	 */
	@Query("SELECT c.cliente.idCliente FROM Cuenta c WHERE c.cliente IS NOT NULL")
	Set<Integer> idsClientesConCuenta();

	/**
	 * Cuentas de empleados, para mostrar su correo de acceso.
	 *
	 * @return cuentas de empleados, para mostrar su correo de acceso
	 */
	@Query("SELECT c FROM Cuenta c WHERE c.empleado IS NOT NULL")
	List<Cuenta> deEmpleados();
}

package co.edu.unbosque.BackMotos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

	Optional<Empleado> findByCorreoAccesoIgnoreCase(String correoAcceso);

	Optional<Empleado> findByDocumento(String documento);

	List<Empleado> findBySucursalIdSucursal(Integer idSucursal);

	List<Empleado> findByJefeIdEmpleado(Integer idJefe);

	boolean existsByDocumento(String documento);

	boolean existsByCorreoAccesoIgnoreCase(String correoAcceso);
}

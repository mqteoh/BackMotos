package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

	List<Sucursal> findByCiudadIgnoreCaseOrderByNombre(String ciudad);

	boolean existsByNombreIgnoreCase(String nombre);
}

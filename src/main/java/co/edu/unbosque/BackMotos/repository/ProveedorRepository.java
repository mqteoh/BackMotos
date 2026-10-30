package co.edu.unbosque.BackMotos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, String> {

	boolean existsByNombreIgnoreCase(String nombre);
}

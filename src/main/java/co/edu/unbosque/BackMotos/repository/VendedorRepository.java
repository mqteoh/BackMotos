package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {

	List<Vendedor> findBySucursalIdSucursal(Integer idSucursal);
}

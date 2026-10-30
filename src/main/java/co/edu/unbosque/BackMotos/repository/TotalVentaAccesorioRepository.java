package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.TotalVentaAccesorio;

public interface TotalVentaAccesorioRepository extends JpaRepository<TotalVentaAccesorio, Integer> {

	List<TotalVentaAccesorio> findByIdVendedor(Integer idVendedor);
}

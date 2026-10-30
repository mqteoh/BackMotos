package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.VentaAccesorio;

public interface VentaAccesorioRepository extends JpaRepository<VentaAccesorio, Integer> {

	List<VentaAccesorio> findByClienteIdClienteOrderByFechaVentaDesc(Integer idCliente);

	List<VentaAccesorio> findByVendedorIdEmpleadoOrderByFechaVentaDesc(Integer idVendedor);
}

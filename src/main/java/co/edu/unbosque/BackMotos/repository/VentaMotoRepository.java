package co.edu.unbosque.BackMotos.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.VentaMoto;

public interface VentaMotoRepository extends JpaRepository<VentaMoto, Integer> {

	Optional<VentaMoto> findByMotoPlaca(String placa);

	boolean existsByMotoPlaca(String placa);

	List<VentaMoto> findByClienteIdClienteOrderByFechaVentaDesc(Integer idCliente);

	List<VentaMoto> findByVendedorIdEmpleadoOrderByFechaVentaDesc(Integer idVendedor);

	List<VentaMoto> findByFechaVentaBetweenOrderByFechaVenta(LocalDateTime desde, LocalDateTime hasta);
}

package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.CostoReparacion;

public interface CostoReparacionRepository extends JpaRepository<CostoReparacion, Integer> {

	List<CostoReparacion> findByPlacaOrderByIdReparacion(String placa);
}

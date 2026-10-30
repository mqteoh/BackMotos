package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Repuesto;

public interface RepuestoRepository extends JpaRepository<Repuesto, Integer> {

	List<Repuesto> findByCategoriaIgnoreCaseOrderByNombre(String categoria);

	List<Repuesto> findByNombreContainingIgnoreCaseOrderByNombre(String texto);
}

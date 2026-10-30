package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Accesorio;

public interface AccesorioRepository extends JpaRepository<Accesorio, Integer> {

	List<Accesorio> findByMarcaIgnoreCaseOrderByNombre(String marca);

	List<Accesorio> findByNombreContainingIgnoreCaseOrderByNombre(String texto);
}

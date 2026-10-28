package co.edu.unbosque.BackMotos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {

	Optional<Marca> findByNombreIgnoreCase(String nombre);

	boolean existsByNombreIgnoreCase(String nombre);
}

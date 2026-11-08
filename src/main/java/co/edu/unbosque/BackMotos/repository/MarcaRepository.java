package co.edu.unbosque.BackMotos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Marca;

/** Acceso a la tabla marca. */
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

	/**
	 * Busca una marca por nombre, sin importar mayúsculas.
	 *
	 * @param nombre nombre de la marca
	 * @return la marca, o vacío si no existe
	 */
	Optional<Marca> findByNombreIgnoreCase(String nombre);

	/**
	 * Dice si ya existe una marca con ese nombre.
	 *
	 * @param nombre nombre de la marca
	 * @return true si el nombre ya está registrado
	 */
	boolean existsByNombreIgnoreCase(String nombre);
}

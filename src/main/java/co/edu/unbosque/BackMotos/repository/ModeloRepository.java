package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.ModeloId;

/** Acceso a la tabla modelo. */
public interface ModeloRepository extends JpaRepository<Modelo, ModeloId> {

	/**
	 * Modelos de una marca, en orden de número.
	 *
	 * @param idMarca identificador de la marca
	 * @return los modelos de la marca
	 */
	List<Modelo> findByIdIdMarcaOrderByIdNroModelo(Integer idMarca);

	/**
	 * Dice si la marca ya tiene un modelo con ese nombre.
	 *
	 * @param idMarca identificador de la marca
	 * @param nombre nombre del modelo
	 * @return true si el nombre ya está en uso
	 */
	boolean existsByIdIdMarcaAndNombreIgnoreCase(Integer idMarca, String nombre);

	/**
	 * Número que le toca al próximo modelo de la marca.
	 *
	 * @param idMarca identificador de la marca
	 * @return número que le toca al próximo modelo de la marca
	 */
	@Query("SELECT COALESCE(MAX(m.id.nroModelo), 0) + 1 FROM Modelo m WHERE m.id.idMarca = :idMarca")
	Integer siguienteNroModelo(Integer idMarca);
}

package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Modelo;
import co.edu.unbosque.BackMotos.entity.ModeloId;

public interface ModeloRepository extends JpaRepository<Modelo, ModeloId> {

	List<Modelo> findByIdIdMarcaOrderByIdNroModelo(Integer idMarca);

	boolean existsByIdIdMarcaAndNombreIgnoreCase(Integer idMarca, String nombre);

	@Query("SELECT COALESCE(MAX(m.id.nroModelo), 0) + 1 FROM Modelo m WHERE m.id.idMarca = :idMarca")
	Integer siguienteNroModelo(Integer idMarca);
}

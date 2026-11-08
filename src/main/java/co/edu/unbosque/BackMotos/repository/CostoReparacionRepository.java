package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.CostoReparacion;

/** Lectura de la vista v_costo_reparacion. */
public interface CostoReparacionRepository extends JpaRepository<CostoReparacion, Integer> {

	/**
	 * Costos de las reparaciones de una moto.
	 *
	 * @param placa placa de la moto
	 * @return los costos de cada reparación
	 */
	List<CostoReparacion> findByPlacaOrderByIdReparacion(String placa);
}

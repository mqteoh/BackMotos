package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Tecnico;

/** Acceso a los técnicos (tablas empleado y tecnico). */
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

	/**
	 * Técnicos de una sede.
	 *
	 * @param idSucursal identificador de la sede
	 * @return los técnicos de la sede
	 */
	List<Tecnico> findBySucursalIdSucursal(Integer idSucursal);
}

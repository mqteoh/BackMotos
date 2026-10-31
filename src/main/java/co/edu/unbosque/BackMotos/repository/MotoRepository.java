package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;

public interface MotoRepository extends JpaRepository<Moto, String> {

	List<Moto> findByOrigen(OrigenMoto origen);

	List<Moto> findBySucursalIdSucursal(Integer idSucursal);

	@Query("SELECT m FROM Moto m WHERE m.origen = :origen AND (:idSucursal IS NULL OR m.sucursal.idSucursal = :idSucursal)"
			+ " AND NOT EXISTS (SELECT v FROM VentaMoto v WHERE v.moto = m) ORDER BY m.sucursal.nombre, m.placa")
	List<Moto> sinVender(OrigenMoto origen, Integer idSucursal);
}

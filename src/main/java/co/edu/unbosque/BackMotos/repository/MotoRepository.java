package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Moto;
import co.edu.unbosque.BackMotos.entity.OrigenMoto;

public interface MotoRepository extends JpaRepository<Moto, String> {

	List<Moto> findByOrigen(OrigenMoto origen);

	List<Moto> findBySucursalIdSucursal(Integer idSucursal);
}

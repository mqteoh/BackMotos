package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

	List<Tecnico> findBySucursalIdSucursal(Integer idSucursal);
}

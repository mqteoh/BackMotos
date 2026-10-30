package co.edu.unbosque.BackMotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Suministro;
import co.edu.unbosque.BackMotos.entity.SuministroId;

public interface SuministroRepository extends JpaRepository<Suministro, SuministroId> {

	List<Suministro> findBySucursalIdSucursalOrderByIdFechaSuministroDesc(Integer idSucursal);

	List<Suministro> findByProveedorNitOrderByIdFechaSuministroDesc(String nit);
}

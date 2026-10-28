package co.edu.unbosque.BackMotos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import co.edu.unbosque.BackMotos.entity.MotoDisponible;

public interface MotoDisponibleRepository
		extends JpaRepository<MotoDisponible, String>, JpaSpecificationExecutor<MotoDisponible> {
}

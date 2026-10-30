package co.edu.unbosque.BackMotos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.BackMotos.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	Optional<Cliente> findByDocumento(String documento);

	Optional<Cliente> findByCorreoAccesoIgnoreCase(String correoAcceso);

	boolean existsByDocumento(String documento);

	boolean existsByCorreoAccesoIgnoreCase(String correoAcceso);
}

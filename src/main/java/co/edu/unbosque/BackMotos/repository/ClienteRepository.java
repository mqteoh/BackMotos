package co.edu.unbosque.BackMotos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	Optional<Cliente> findByDocumento(String documento);

	Optional<Cliente> findByCorreoAccesoIgnoreCase(String correoAcceso);

	boolean existsByDocumento(String documento);

	boolean existsByCorreoAccesoIgnoreCase(String correoAcceso);

	@Query("SELECT c FROM Cliente c WHERE LOWER(CONCAT(c.nombres, ' ', c.apellidos)) LIKE LOWER(CONCAT('%', :texto, '%'))"
			+ " OR c.documento LIKE CONCAT(:texto, '%') ORDER BY c.apellidos, c.nombres")
	List<Cliente> buscar(String texto, Pageable pagina);
}

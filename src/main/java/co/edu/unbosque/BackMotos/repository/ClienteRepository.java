package co.edu.unbosque.BackMotos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unbosque.BackMotos.entity.Cliente;

/** Acceso a la tabla cliente con sus teléfonos y correos. */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	/**
	 * Busca un cliente por documento.
	 *
	 * @param documento número de documento de identidad
	 * @return el cliente, o vacío si no existe
	 */
	Optional<Cliente> findByDocumento(String documento);

	/**
	 * Dice si ya hay un cliente con ese documento.
	 *
	 * @param documento número de documento de identidad
	 * @return true si el documento ya está registrado
	 */
	boolean existsByDocumento(String documento);

	/**
	 * Busca clientes por nombre completo o por el inicio del documento, sin importar mayúsculas ni tildes.
	 *
	 * @param texto  parte del nombre o del documento
	 * @param pagina cuántos resultados traer
	 * @return clientes ordenados por apellido
	 */
	@Query("SELECT c FROM Cliente c WHERE FUNCTION('translate' AS String, LOWER(CONCAT(c.nombres, ' ', c.apellidos)), 'áéíóúüñ', 'aeiouun')"
			+ " LIKE CONCAT('%', FUNCTION('translate' AS String, LOWER(:texto), 'áéíóúüñ', 'aeiouun'), '%')"
			+ " OR c.documento LIKE CONCAT(:texto, '%') ORDER BY c.apellidos, c.nombres")
	List<Cliente> buscar(String texto, Pageable pagina);
}

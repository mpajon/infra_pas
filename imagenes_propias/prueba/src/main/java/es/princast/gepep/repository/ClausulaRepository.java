package es.princast.gepep.repository;

import es.princast.gepep.domain.Clausula;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Clausula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClausulaRepository extends JpaRepository<Clausula, Long> {
	
		List<Clausula> findAllByTexto (String texto);

}

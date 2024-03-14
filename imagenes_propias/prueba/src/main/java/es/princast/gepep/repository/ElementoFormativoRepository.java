package es.princast.gepep.repository;


import es.princast.gepep.domain.ElementoFormativo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElementoFormativoRepository extends JpaRepository<ElementoFormativo, Long> {
		
    Optional<ElementoFormativo> findByCodigo(String codigo);
    List<ElementoFormativo> findAllByCodigo(String codigo);

}

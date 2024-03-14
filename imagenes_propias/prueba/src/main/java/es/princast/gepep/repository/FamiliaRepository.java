package es.princast.gepep.repository;


import es.princast.gepep.domain.Familia;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Familia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Long> {
		
    
    Iterable<Familia> findByNombre(String nombre);
 

}

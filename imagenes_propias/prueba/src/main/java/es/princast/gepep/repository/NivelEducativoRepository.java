package es.princast.gepep.repository;


import es.princast.gepep.domain.NivelEducativo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Familia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NivelEducativoRepository extends JpaRepository<NivelEducativo, Integer> {
		
    
    Iterable<NivelEducativo> findByNivel(String nivel);
 

}

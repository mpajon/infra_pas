package es.princast.gepep.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.NormaReguladora;
import es.princast.gepep.domain.TipoPractica;


/**
 * Spring Data JPA repository for the NormaReguladora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NormaReguladoraRepository extends JpaRepository<NormaReguladora, Long> {

}

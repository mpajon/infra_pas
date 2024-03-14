package es.princast.gepep.repository;

import es.princast.gepep.domain.TipoCentro;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TipoCentro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoCentroRepository extends JpaRepository<TipoCentro, Long> {

}

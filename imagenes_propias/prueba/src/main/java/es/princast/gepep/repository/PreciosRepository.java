package es.princast.gepep.repository;

 
import es.princast.gepep.domain.Precios;
 
import es.princast.gepep.domain.TipoPractica;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Precios entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreciosRepository extends JpaRepository<Precios, Long> {
	Optional<Precios> findPreciosByTipoPractica(TipoPractica tipoPractica);

}

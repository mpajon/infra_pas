package es.princast.gepep.repository;

import es.princast.gepep.domain.TipoGasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the TipoCentro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoGastoRepository extends JpaRepository<TipoGasto, Long> {

}

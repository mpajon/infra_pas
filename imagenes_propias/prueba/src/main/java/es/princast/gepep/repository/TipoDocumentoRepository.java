package es.princast.gepep.repository;

import es.princast.gepep.domain.TipoDocumento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TipoDocumento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {

}

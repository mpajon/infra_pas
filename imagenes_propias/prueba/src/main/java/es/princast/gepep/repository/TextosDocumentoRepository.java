package es.princast.gepep.repository;


import es.princast.gepep.domain.Documento;
import es.princast.gepep.domain.TextosDocumento;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TextosDocumento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TextosDocumentoRepository extends JpaRepository<TextosDocumento, Long> {

	 Iterable<TextosDocumento> findAllByDocumento(Documento documento);
	 TextosDocumento findByDocumento(Documento documento);
}

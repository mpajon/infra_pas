package es.princast.gepep.repository;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.domain.OfertasCentro;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OfertaFormativa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfertasCentroRepository extends JpaRepository<OfertasCentro, String> {

	List<OfertasCentro> findAllByCentro(Centro centro);
	List<OfertasCentro> findAllByOferta (OfertaFormativa oferta);
	Optional<OfertasCentro> findOneByOfertaAndCentro(OfertaFormativa oferta, Centro centro);
	
 	}

package es.princast.gepep.repository;


import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.OfertaFormativa;
 

import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the OfertaFormativa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfertaFormativaRepository extends JpaRepository<OfertaFormativa, String> {
		 
	@Query(" SELECT distinct oferta_formativa FROM OfertaFormativa oferta_formativa  WHERE oferta_formativa.idOfertaFormativa in ('4628','S100528','S4686','S4822')")	 
	List<OfertaFormativa> getOfertasFormativasBylistaIds(@Param("listaIds") String listaIds);	 
	List<OfertaFormativa> findAllByCicloAndAnioFinIsNull(Ciclo ciclo);
	
	//List<OfertaFormativa> findAllByCicloAndVigente(Ciclo ciclo);
	List<OfertaFormativa> findAllByCicloAndCodigoAndAnioFinIsNull(Ciclo ciclo, String codigo);
	List<OfertaFormativa> findAllByCiclo(Optional<Ciclo> ciclo);
	 
}
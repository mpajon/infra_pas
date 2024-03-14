package es.princast.gepep.repository;

import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.Documento;
import es.princast.gepep.domain.TipoDocumento;
import es.princast.gepep.domain.TipoPractica;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Documento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
	
    @Query("select distinct documento from Documento documento left join fetch documento.clausulas")
    List<Documento> findAllWithEagerRelationships();

    @Query("select documento from Documento documento left join fetch documento.clausulas where documento.id =:id")
    Documento findOneWithEagerRelationships(@Param("id") Long id); 
    
    Iterable<Documento> findAllByTipoPractica(TipoPractica tipopractica);   
    Iterable<Documento> findAllByTipoDocumento(TipoDocumento  tipoDocumento);
    List<Documento> findAllByNombreAndTipoPractica(String nombre, TipoPractica tipoPractica);

}

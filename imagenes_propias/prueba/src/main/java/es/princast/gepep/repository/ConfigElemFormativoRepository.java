package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.ConfigElemFormativo;
import es.princast.gepep.domain.ElementoFormativo;
import es.princast.gepep.domain.TipoPractica;


/**
 * Spring Data JPA repository for the Actividad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigElemFormativoRepository extends JpaRepository<ConfigElemFormativo, Long> {
	
	@Query(value = "SELECT c "
	    		+ " from ConfigElemFormativo c join fetch c.elementoFormativo ")
	List<ConfigElemFormativo> findAll(Sort sort);
	Iterable<ConfigElemFormativo> findAllByElementoFormativoOrderByOrdenAsc(ElementoFormativo elemento);
	/*Iterable<ConfigElemFormativo> findAllByTipoPracticaOrderByOrdenAsc(TipoPractica practica);	
    Optional<ConfigElemFormativo> findByDenominacion(String denominacion);*/    
    Iterable<ConfigElemFormativo> findByElementoFormativo(ElementoFormativo elemento);    
    List<ConfigElemFormativo> findAllByDenominacionAndElementoFormativo(String denominacion, ElementoFormativo elemento);
    /*List<ConfigElemFormativo> findAllByTipoPracticaAndElementoFormativo(TipoPractica practica, ElementoFormativo elemento);
    List<ConfigElemFormativo> findAllByDenominacionAndTipoPracticaAndElementoFormativo(String denominacion,TipoPractica practica, ElementoFormativo elemento);*/
}

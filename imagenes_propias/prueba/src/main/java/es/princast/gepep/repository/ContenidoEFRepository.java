package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.ConfigElemFormativo;
import es.princast.gepep.domain.ContenidoEF;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.domain.Ciclo;


/**
 * Spring Data JPA repository for the Actividad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContenidoEFRepository extends JpaRepository<ContenidoEF, Long> {
	
	@Query(value = "SELECT c "
	    		+ " from ContenidoEF c join fetch c.ciclo  join fetch c.distribucion")
	List<ContenidoEF> findAll(Sort sort);
	List<ContenidoEF> findAllByCiclo(Ciclo ciclo);
	 List<ContenidoEF> findAllByDistribucion(Distribucion distribucion);
    Iterable<ContenidoEF> findAllByConfigElemFormativo(ConfigElemFormativo config);   
    List<ContenidoEF> findAllByCicloAndConfigElemFormativo(Ciclo ciclo,ConfigElemFormativo config);
    List<ContenidoEF> findAllByDistribucionAndConfigElemFormativo(Distribucion distribucion,ConfigElemFormativo config);
    
    
}


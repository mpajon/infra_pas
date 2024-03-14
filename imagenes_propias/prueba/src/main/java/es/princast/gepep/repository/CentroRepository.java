package es.princast.gepep.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.TipoPractica;


/**
 * Spring Data JPA repository for the Centro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CentroRepository extends JpaRepository<Centro, String> {
	
		List<Centro> findAllByFechaBajaIsNull();
 		List<Centro> findAllByCodigo(String codigo);
 		
 	    @Query(value = "SELECT  centro "
 	    		+ " from Centro centro join fetch centro.municipio "
 	    		+ "  where centro.fechaBaja IS NULL order by centro.nombre ASC")
	    List<Centro> findAllActivosOrderByNombreAsc();
		
 	    Centro findOneByCodigo(String codigo);

 
}

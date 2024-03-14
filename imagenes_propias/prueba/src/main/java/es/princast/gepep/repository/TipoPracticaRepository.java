package es.princast.gepep.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.domain.Visor;


/**
 * Spring Data JPA repository for the TipoPractica entity.
 */
@SuppressWarnings("unused")
@Repository



public interface TipoPracticaRepository extends JpaRepository<TipoPractica, Long> {
	
	
	    @Query("select distinct tipo_practica "
	    		+ " from TipoPractica tipo_practica "
	    		+ " left join fetch tipo_practica.ensenanzas "
	    		+ " left join fetch tipo_practica.normasReguladoras "
	    		+ " left join fetch tipo_practica.configuracionesEF "
	    		+ " order by tipo_practica.nombre asc ")
	    List<TipoPractica> findAllWithEagerRelationships();

	   @Query("select tipo_practica "
	   		+ " from TipoPractica tipo_practica "
	   		+ " left join fetch tipo_practica.ensenanzas "
	   		+ " left join fetch tipo_practica.normasReguladoras "
	   		+ " left join fetch tipo_practica.configuracionesEF "
	   		+ " where tipo_practica.id =:id ")
	    TipoPractica findOneWithEagerRelationships(@Param("id") Long id); 
	 
		Iterable<TipoPractica> findAllByEnsenanzas(Ensenanza ensenanza);
		List <TipoPractica> findAllByNombre(String nombre);
		
	    @Query(value = "SELECT  tipoPractica "
	    		+ " from TipoPractica tipoPractica "
	    		+ " where tipoPractica.fechaFinVigencia IS NULL OR tipoPractica.fechaFinVigencia> :fecha ORDER BY tipoPractica.nombre ASC")
	    List<TipoPractica> findAllByFechaFinVigenciaOrderByNombre(@Param ("fecha") LocalDate fecha);
	    
	    @Query(value = "SELECT  tipoPractica from TipoPractica tipoPractica where tipoPractica.nombre= :nombre AND (tipoPractica.fechaFinVigencia IS NULL OR tipoPractica.fechaFinVigencia> :fecha)  ORDER BY tipoPractica.nombre ASC")
	    List<TipoPractica> findAllByNombreAndFechaFinVigencia(@Param ("nombre")String nombre, @Param ("fecha") LocalDate fecha);
	    
		List <TipoPractica> findAllByfechaFinVigenciaIsNull();
		
		Iterable<TipoPractica>findAllByVisor(Visor visor);

}

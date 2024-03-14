package es.princast.gepep.repository;

import es.princast.gepep.domain.TipoPractica;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

 
import es.princast.gepep.domain.Actividad;
import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.Ensenanza;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Ensenanza entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnsenanzaRepository extends JpaRepository<Ensenanza, String> {

		
	  	Optional<Ensenanza> findByDescripcion(String descripcion);
	    Iterable<Ensenanza> findByNombre(String nombre);	   
	    Optional<Ensenanza> findByIdEnsenanza (String idEnsenanza);
	    List<Ensenanza> findAllByNombreAndFechaFinVigenciaIsNull(String nombre);
	    
	    
	    @Query(value = "SELECT  ensenanza from Ensenanza ensenanza where ensenanza.fechaFinVigencia IS NULL OR ensenanza.fechaFinVigencia> :fecha ORDER BY ensenanza.nombre ASC")
	    List<Ensenanza> findAllByFechaFinVigenciaOrderByNombre(@Param ("fecha") LocalDate fecha);
	    
	    @Query(value = "SELECT  ensenanza from Ensenanza ensenanza where ensenanza.nombre= :nombre AND (ensenanza.fechaFinVigencia IS NULL OR ensenanza.fechaFinVigencia> :fecha) ORDER BY ensenanza.nombre ASC")
	    List<Ensenanza> findAllByNombreAndFechaFinVigenciaOrderByNombre(@Param ("nombre") String nombre, @Param ("fecha") LocalDate fecha);
}

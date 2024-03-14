package es.princast.gepep.repository;

import es.princast.gepep.domain.CursoAcademico;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Sector entity.
 */
@SuppressWarnings("unused")
public interface CursoAcademicoRepository extends JpaRepository<CursoAcademico, Integer> {
		
       
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE  es.princast.gepep.domain.CursoAcademico curso  SET curso.actual=false WHERE curso.idAnio != :#{#cursoAcademico.idAnio}")    
    Integer updateCursoAcademico(@Param ("cursoAcademico") CursoAcademico curso);
    
    
    Optional<CursoAcademico> findByActual(Boolean actual);

}

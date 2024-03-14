package es.princast.gepep.repository;

 
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.Unidad;


/**
 * Spring Data JPA repository for the Unidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnidadRepository extends JpaRepository<Unidad, String> {
	 
	List<Unidad> findAllByOfertaCentro(Optional<OfertasCentro> ofertaCentro);

	List<Unidad> findAllByOfertaCentroAndAnio(Optional<OfertasCentro> ofertaCentro, Integer anio);

	List<Unidad> findAllByOfertaCentroAndAnioAndNombre(Optional<OfertasCentro> ofertaCentro, Integer anio, String nombre);

	List<Unidad> findAllByTutor(Profesor tutor);
	
	List<Unidad> findAllByTutorAndAnio(Optional<Profesor> tutor, Integer anio);
	
	List<Unidad> findAllByTutorIsNotNull();

	List<Unidad> findAllByTutorOrTutorAdicional(Sort sorteable, Optional<Profesor> tutor, Optional<Profesor> tutorAdicional);
	
	@Query("SELECT unidad FROM Unidad as unidad where unidad.anio = :anio and (unidad.tutor = :tutor or unidad.tutorAdicional = :tutorAdicional)")
	List<Unidad> findAllByTutorOrTutorAdicionalAndAnio(@Param("tutor") Profesor tutor, @Param("tutorAdicional") Profesor tutorAdicional, @Param("anio") Integer anio);

	Page<Unidad> findAll(Specification<Unidad> specification, Pageable pageable);
}

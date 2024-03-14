package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.CriterioEvaluacion;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.Usuario;


/**
 * Spring Data JPA repository for the Alumno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, String>, JpaSpecificationExecutor<Alumno> {

	
	Optional<Alumno> getOneByNif(String nif);
	List<Alumno> findAllByNif(String nif);
	
	@Query(" select alumno from Alumno alumno "
			+ " where alumno.idAlumno in ("
								+ " select matricula.alumno.idAlumno "
								+ " from Matricula matricula "
								+ " inner join OfertasCentro ofercen on matricula.ofertaCentro.idOfertaCentro = ofercen.idOfertaCentro"
							 	+ " where matricula.anio= :anio and ofercen.centro.idCentro = :idCentro "
			+ " ) order by alumno.apellido1, alumno.apellido2, alumno.nombre asc ") 
	Iterable<Alumno> getAlumnosByCentro(@Param("idCentro") Integer idCentro,  @Param("anio")Integer anio);
;}

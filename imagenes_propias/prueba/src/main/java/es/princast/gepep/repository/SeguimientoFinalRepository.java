package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.GastoAlumno;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.SeguimientoFinal;


/**
 * Spring Data JPA repository for the SeguimientoFinal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeguimientoFinalRepository extends JpaRepository<SeguimientoFinal, Long> {


	Optional <SeguimientoFinal> getOneByMatricula(Matricula matricula);
	Optional <SeguimientoFinal> findOneByMatricula(Matricula matricula);


	@Query("SELECT seg" + 
			"   FROM Matricula m " +
			"	inner join OfertasCentro oc on oc.idOfertaCentro = m.ofertaCentro.idOfertaCentro " +
			"   inner join Alumno a on a.idAlumno = m.alumno.idAlumno " +
			"   inner join SeguimientoFinal seg on seg.matricula.idMatricula = m.idMatricula " +
			"   WHERE " +
			"   (m.unidad.tutor.idProfesor = :tutor or m.unidad.tutorAdicional.idProfesor = :tutor or :tutor = 'undefined') "+
			"   and (oc.centro.idCentro = :idCentro or :idCentro = 'undefined') " +
			"   and m.anio = :curso " +
			"	order by seg.matricula.unidad.ofertaCentro.centro.nombre, seg.matricula.unidad.ofertaCentro.oferta.nombre, seg.matricula.unidad.nombre" 
		) 
	List<SeguimientoFinal> getSeguimientoFinalByCursoAndCentroAndTutor(@Param("idCentro")String idCentro, @Param("curso") Integer curso, @Param("tutor")String tutor);
	

}

package es.princast.gepep.repository;

 
import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.AnexoContrato;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.TipoPractica;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the OfertaFormativa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, String> {

	Iterable <Matricula> findAllByOfertaCentro(OfertasCentro ofertaCentro);
	List<Matricula> findAllByAlumnoOrderByAnioAscCursoAsc(Alumno alumno);
	List<Matricula> findAllByAlumnoAndAnioOrderByCursoAsc(Alumno alumno, Integer anio);
	
	
    @Query("select matricula from Matricula matricula "
				+ " join fetch matricula.alumno "
				+ " join fetch matricula.ofertaCentro "
				+ " inner join Centro cen on cen.idCentro = matricula.ofertaCentro.centro.idCentro "
				+ " where matricula.alumno.idAlumno =:idAlumno and matricula.anio= :anio and cen.idCentro=:idCentro"				
				+ " order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ") 
	List<Matricula> getMatriculasByAlumnoAndAnioAndCentro(@Param("idAlumno") String idAlumno, @Param("anio")Integer anio,@Param("idCentro") String idCentro);
	
	@Query("select matricula from Matricula matricula "
			+ " inner join Unidad unidad on matricula.unidad.idUnidad = unidad.idUnidad "
			+ "	inner join Profesor profesor on profesor.idProfesor = unidad.tutor.idProfesor"
		 	+ " where profesor.idProfesor = :#{#profe.idProfesor} ") 
	List<Matricula> findAllByTutor(@Param ("profe") Profesor profe);
	
	@Query("select matricula from Matricula matricula "
			+ " join fetch matricula.alumno " 
		 	+ " where matricula.ofertaCentro.idOfertaCentro =:idOferCen and matricula.anio= :anio "
			+ " order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ") 
	Iterable<Matricula> getMatriculasByOfertaCentroAndAnio(@Param("idOferCen") String idOferCen,  @Param("anio")Integer anio);
	
	
	@Query("select matricula from Matricula matricula "
			+ " join fetch matricula.alumno " 
			+ " inner join Unidad unidad on matricula.unidad.idUnidad = unidad.idUnidad "
		 	+ " where matricula.ofertaCentro.idOfertaCentro =:idOferCen and unidad.nombre= :idUnidad and matricula.anio= :anio "
			+ " order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ") 
	Iterable<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnio(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio);
	

	@Query("select matricula from Matricula matricula "
			+ " join fetch matricula.alumno " 
			+ " inner join Unidad unidad on matricula.unidad.idUnidad = unidad.idUnidad "
		 	+ " where matricula.ofertaCentro.idOfertaCentro =:idOferCen and unidad.nombre= :idUnidad and matricula.anio= :anio and (unidad.tutor.idProfesor= :tutor or unidad.tutorAdicional.idProfesor= :tutor)"
			+ " order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ") 
	Iterable<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndTutor(@Param("idOferCen") String idOferCen,  @Param("idUnidad") String idUnidad, @Param("anio")Integer anio, @Param("tutor") String tutor);
	
		
	 @Query("select matricula from Matricula matricula "
			 + " join fetch matricula.alumno " 
	 		+ " where matricula.idMatricula in ("
			 		+ " select distinct mat.idMatricula from Matricula mat  "
			 		+ " inner join Unidad unidad on mat.unidad.idUnidad = unidad.idUnidad "
			 		+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "
			 		+ " where  mat.ofertaCentro.idOfertaCentro =:idOferCen and unidad.nombre= :idUnidad and mat.anio= :anio "
			+ " ) order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ")
	 Iterable<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioDistribuidas(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio); 
	 
	 
	 
	 @Query("select matricula from Matricula matricula "
			 + " join fetch matricula.alumno " 
		 		+ " where matricula.idMatricula in ("
						+ " select distinct mat.idMatricula from Matricula mat  "
						+ " inner join Unidad unidad on mat.unidad.idUnidad = unidad.idUnidad "
				 		+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "	 		
				 		+ " inner join DistribucionPeriodo distribucionPeriodo on distribucion.idDistribucion = distribucionPeriodo.distribucion.idDistribucion  "
				 		+ " inner join PeriodoPractica periodoPractica on periodoPractica.idPeriodo = distribucionPeriodo.periodo.idPeriodo  "
				 		+ " where  mat.ofertaCentro.idOfertaCentro =:idOferCen and unidad.nombre= :idUnidad and mat.anio= :anio and periodoPractica.idPeriodo = :idPeriodoPractica "	 	
	 		+ " ) order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ")
	 Iterable<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoDistribuidas(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio, @Param("idPeriodoPractica") Long idPeriodoPractica); 
	 
	 
	 @Query("select matricula from Matricula matricula "
			 + " join fetch matricula.alumno " 
		 		+ " where matricula.idMatricula in ("
						+ " select distinct mat.idMatricula "
				 		+ " from Matricula mat  "
				 		+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "
				 		+ " where  mat.ofertaCentro.idOfertaCentro =:idOferCen and mat.anio= :anio and mat.unidad.tutor.idProfesor= :tutor "
	 		+ " ) order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ")
	 Iterable<Matricula> getMatriculasByOfertaCentroAndAnioAndTutorDistribuidas(@Param("idOferCen") String idOferCen,  @Param("anio")Integer anio, @Param("tutor") String tutor);
	 
	 @Query("select matricula from Matricula matricula "
			 + " join fetch matricula.alumno " 
		 		+ " where matricula.idMatricula in ("
						+ " select distinct mat.idMatricula "
				 		+ " from Matricula mat  "
				 		+ " inner join Unidad unidad on mat.unidad.idUnidad = unidad.idUnidad "
				 		+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "
				 		+ " where  mat.ofertaCentro.idOfertaCentro =:idOferCen  and unidad.nombre= :idUnidad and mat.anio= :anio and (unidad.tutor.idProfesor= :tutor or unidad.tutorAdicional.idProfesor= :tutor)"
			+ " ) order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ")
	 Iterable<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndTutorDistribuidas(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio, @Param("tutor") String tutor); 
	 
	 @Query("select matricula from Matricula matricula "
			 + " join fetch matricula.alumno " 
		 		+ " where matricula.idMatricula in ("
						+ " select distinct mat.idMatricula "
				 		+ "	from Matricula mat  "
				 		+ " inner join Unidad unidad on mat.unidad.idUnidad = unidad.idUnidad "
				 		+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "
				 		+ " inner join DistribucionPeriodo distribucionPeriodo on distribucion.idDistribucion = distribucionPeriodo.distribucion.idDistribucion  "
				 		+ " inner join PeriodoPractica periodoPractica on periodoPractica.idPeriodo = distribucionPeriodo.periodo.idPeriodo  "
				 		+ " where mat.ofertaCentro.idOfertaCentro =:idOferCen  and unidad.nombre= :idUnidad  and mat.anio= :anio and (unidad.tutor.idProfesor= :tutor or unidad.tutorAdicional.idProfesor= :tutor) and periodoPractica.idPeriodo = :idPeriodoPractica"
			+ " ) order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc ")
	 Iterable<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorDistribuidas(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio,   @Param("idPeriodoPractica") Long idPeriodoPractica, @Param("tutor") String tutor); 
	 
	 @Query("select matricula from Matricula matricula "
			 + " join fetch matricula.alumno " 
			 	+ " inner join Unidad unidad on matricula.unidad.idUnidad = unidad.idUnidad "
			  	+ "where matricula.ofertaCentro.idOfertaCentro = :idOferCen and matricula.anio= :anio and unidad.nombre= :idUnidad and (unidad.tutor.idProfesor= :tutor or unidad.tutorAdicional.idProfesor= :tutor)"
			  	+ "and matricula.idMatricula not in ("
						  	+ " select distinct mat.idMatricula from Matricula mat  "
						  	+ " inner join Unidad uni on mat.unidad.idUnidad = uni.idUnidad "
					 		+ " inner join  Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "
					 		+ " inner join  DistribucionPeriodo distribucionPeriodo on distribucion.idDistribucion = distribucionPeriodo.distribucion.idDistribucion  "
					 		+ " inner join  PeriodoPractica periodoPractica on periodoPractica.idPeriodo = distribucionPeriodo.periodo.idPeriodo  "
					 		+ " where  mat.ofertaCentro.idOfertaCentro =:idOferCen  and uni.nombre= :idUnidad  and mat.anio= :anio and (uni.tutor.idProfesor= :tutor or uni.tutorAdicional.idProfesor= :tutor) and periodoPractica.idPeriodo = :idPeriodoPractica "
	 			+ ") order by matricula.alumno.apellido1, matricula.alumno.apellido2, matricula.alumno.nombre asc " )
		 Iterable<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorNoDistribuidas(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio,   @Param("idPeriodoPractica") Long idPeriodoPractica, @Param("tutor") String tutor); 

	 
	 
	 
		
	 
	 
	 
}

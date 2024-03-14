package es.princast.gepep.repository;

import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.PeriodoPractica;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the DistribucionPeriodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistribucionPeriodoRepository extends JpaRepository<DistribucionPeriodo, Long> {

	Iterable<DistribucionPeriodo> findAllByDistribucion(Distribucion distribucion);
	
	Iterable<DistribucionPeriodo> findAllByPeriodo(PeriodoPractica periodo);
	
	Iterable<DistribucionPeriodo> findAllByDistribucionAndPeriodo(Distribucion distribucion,PeriodoPractica periodo);
	
	    @Query("SELECT sum(dp.horas) from DistribucionPeriodo dp " +
	            " INNER JOIN Distribucion dist  on dp.distribucion.idDistribucion = dist.idDistribucion " +
	            " INNER JOIN Matricula mat  on dist.matricula.idMatricula = mat.idMatricula " + 
	            " INNER JOIN PeriodoPractica pp  on dp.periodo.idPeriodo = pp.idPeriodo " +
	            " INNER JOIN TipoPractica tp  on pp.tipoPractica.idTipoPractica = tp.idTipoPractica " +
	    		" WHERE mat.idMatricula = :idMatricula" +
	            " AND dp.distribucion.idDistribucion = :idDistribucion " + 
	    		" AND tp.idTipoPractica = :idTipoPractica")
	    Integer getHorasDistribuidasByMatriculaDistribucionAndTipoPractica(@Param("idMatricula") String idMatricula,@Param("idDistribucion") Long idDistribucion,@Param("idTipoPractica") Long idTipoPractica);
		
	    @Query(" select distribucionPeriodo "
	    		+ " from Matricula mat  "
				+ " inner join Unidad unidad on mat.unidad.idUnidad = unidad.idUnidad "
		 		+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "	 		
		 		+ " inner join DistribucionPeriodo distribucionPeriodo on distribucion.idDistribucion = distribucionPeriodo.distribucion.idDistribucion  "
		 		+ " inner join PeriodoPractica periodoPractica on periodoPractica.idPeriodo = distribucionPeriodo.periodo.idPeriodo  "
		 		+ " where  mat.ofertaCentro.idOfertaCentro =:idOferCen and unidad.nombre= :idUnidad and mat.anio= :anio and periodoPractica.idPeriodo = :idPeriodoPractica "
		 		+ " order by distribucionPeriodo.distribucion.matricula.alumno.apellido1, distribucionPeriodo.distribucion.matricula.alumno.apellido2, distribucionPeriodo.distribucion.matricula.alumno.nombre asc "
		 		)
	    List<DistribucionPeriodo> getDistribucionesPeriodoByOfertaCentroAndUnidadAndAnioAndPeriodo(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio, @Param("idPeriodoPractica") Long idPeriodoPractica);

	@Query(" select distribucionPeriodo "
			+ " from Matricula mat  "
			+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "
			+ " inner join DistribucionPeriodo distribucionPeriodo on distribucion.idDistribucion = distribucionPeriodo.distribucion.idDistribucion  "
			+ " inner join PeriodoPractica periodoPractica on periodoPractica.idPeriodo = distribucionPeriodo.periodo.idPeriodo  "
			+ " where  mat.ofertaCentro.idOfertaCentro =:idOferCen and mat.anio= :anio and periodoPractica.idPeriodo = :idPeriodoPractica "
			+ " order by distribucionPeriodo.distribucion.matricula.alumno.apellido1, distribucionPeriodo.distribucion.matricula.alumno.apellido2, distribucionPeriodo.distribucion.matricula.alumno.nombre asc "
	)
	List<DistribucionPeriodo> getDistribucionesPeriodoByOfertaCentroAndAnioAndPeriodo(@Param("idOferCen") String idOferCen, @Param("anio")Integer anio, @Param("idPeriodoPractica") Long idPeriodoPractica);

	@Query(" select distribucionPeriodo "
	    		+ " from Matricula mat  "
				 		+ " inner join Unidad unidad on mat.unidad.idUnidad = unidad.idUnidad "
				 		+ " inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "
				 		+ " inner join DistribucionPeriodo distribucionPeriodo on distribucion.idDistribucion = distribucionPeriodo.distribucion.idDistribucion  "
				 		+ " inner join PeriodoPractica periodoPractica on periodoPractica.idPeriodo = distribucionPeriodo.periodo.idPeriodo  "
				 		+ " where mat.ofertaCentro.idOfertaCentro =:idOferCen  and unidad.nombre= :idUnidad  and mat.anio= :anio and (unidad.tutor.idProfesor= :tutor or unidad.tutorAdicional.idProfesor= :tutor) and periodoPractica.idPeriodo = :idPeriodoPractica"
			)
	 List<DistribucionPeriodo> getDistribucionesPeriodoByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutor(@Param("idOferCen") String idOferCen, @Param("idUnidad") String idUnidad, @Param("anio")Integer anio,   @Param("idPeriodoPractica") Long idPeriodoPractica, @Param("tutor") String tutor); 
	 
	 
}

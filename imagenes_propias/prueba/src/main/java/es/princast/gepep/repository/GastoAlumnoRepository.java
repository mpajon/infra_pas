package es.princast.gepep.repository;

import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.domain.GastoAlumno;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.PeriodoLiquidacion;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;


/**
 * Spring Data JPA repository for the GastoAlumno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GastoAlumnoRepository extends JpaRepository<GastoAlumno, Integer> {

	
	Iterable <GastoAlumno> findAllByPeriodoLiquidacion (PeriodoLiquidacion periodoLiquidacion);
	Iterable <GastoAlumno> findAllByMatricula(Matricula matricula);
	Iterable <GastoAlumno> findAllByMatriculaAndPeriodoLiquidacion (Matricula matricula,PeriodoLiquidacion periodoLiquidacion);

//CRQ561434
	
//	@Query("SELECT sum(ga.idGastoAlumno) as idGastoAlumno, sum(ga.numBillete) as numBillete,sum(ga.numDieta) as numDieta, sum(ga.numKm) as numKm , sum(ga.numPension) as numPension, sum(ga.otrosGastos) as otrosGastos, sum(ga.precioBillete) as precioBillete ," +
//	"   ga.precioDieta as precioDieta , ga.precioKm as precioKm ,ga.precioPension as precioPension, " + 
//	"	ga.periodoLiquidacion.idPeriodoLiquidacion as idPeriodoLiquidacion,  ga.matricula.idMatricula as idMatricula, ga.alumno as alumno,sum(ga.distribucionPeriodo.idDistribucionPeriodo) as idDistribucionPeriodo " +			
//	"   FROM Matricula m inner join OfertasCentro oc on oc.idOfertaCentro = m.ofertaCentro.idOfertaCentro " +
//	"   inner join OfertaFormativa off on off.idOfertaFormativa = oc.oferta.idOfertaFormativa " +
//	"   inner join Distribucion d on d.matricula.idMatricula = m.idMatricula " +
//	"   inner join Alumno a on a.idAlumno = m.alumno.idAlumno " +
//	"   inner join GastoAlumno ga on ga.matricula.idMatricula = m.idMatricula " +
//	"   WHERE off.ciclo.idCiclo  = :idCiclo " +
//	"	and m.turno = :regimen and m.unidad.nombre = :grupo "+
//	"   and oc.centro.idCentro = :idCentro " +
//	"   and m.anio = :curso " +
//	"   and d.fechaBaja is null " +
//	"   and ga.periodoLiquidacion.idPeriodoLiquidacion = :periodo " +
//	"   and d.idDistribucion in (select dp.distribucion.idDistribucion " +
//	"                               from DistribucionPeriodo dp" +
//	"								 where dp.periodo.idPeriodo IN ( select pp.idPeriodo from PeriodoPractica pp " +
//	"                                                        where pp.cursoAcademico.descripcion = :anioAcademico)) " +
//	" group by ga.matricula.idMatricula, ga.alumno,ga.precioDieta,ga.precioKm,ga.precioPension,ga.periodoLiquidacion.idPeriodoLiquidacion ")
	
	@Query("SELECT DISTINCT ga " + 
			"   FROM Matricula m inner join OfertasCentro oc on oc.idOfertaCentro = m.ofertaCentro.idOfertaCentro " +
			"   inner join OfertaFormativa off on off.idOfertaFormativa = oc.oferta.idOfertaFormativa " +
			"   inner join Distribucion d on d.matricula.idMatricula = m.idMatricula " +
			"   inner join Alumno a on a.idAlumno = m.alumno.idAlumno " +
			"   inner join GastoAlumno ga on ga.matricula.idMatricula = m.idMatricula " +
			"   WHERE off.ciclo.idCiclo  = :idCiclo " +
			"	and m.turno = :regimen and m.unidad.nombre = :grupo "+
			"   and oc.centro.idCentro = :idCentro " +
			"   and m.anio = :curso " +
			"   and d.fechaBaja is null " +
			"   and ga.periodoLiquidacion.idPeriodoLiquidacion = :periodo " +
			"   and d.idDistribucion in (select dp.distribucion.idDistribucion " +
			"                               from DistribucionPeriodo dp" +
			"								 where dp.periodo.idPeriodo IN ( select pp.idPeriodo from PeriodoPractica pp " +
			"                                                        where pp.cursoAcademico.descripcion = :anioAcademico))")
	List<GastoAlumno> getGastoAlumnoCurso(@Param("idCentro")String idCentro,@Param("idCiclo")String idCiclo,
									   @Param("periodo") Integer periodo,@Param("curso") Integer curso,
									   @Param("regimen")String regimen, @Param("grupo")String grupo,
										@Param("anioAcademico") String anioAcademico);
	
	//CRQ000000561434
	
//	@Query( "SELECT  0 as idGastoAlumno, sum(ga.numBillete) as numBillete, sum(ga.numDieta) as numDieta, sum(ga.numKm) as numKm, sum(ga.numPension) as numPension, " +
//			" sum(ga.otrosGastos) as otrosGastos, sum(ga.precioBillete) as precioBillete,ga.precioKm ,ga.precioKm ,ga.precioDieta, ga.precioPension,"+
//			" ga.idPeriodoLiquidacion ,ga.idMatricula ,ga.alumno ,sum(ga.idDistribucionPeriodo) as idDistribucionPeriodo, ga.createdBy, null as createdDate, ga.lastModifiedBy, null as lastModifiedDate "+
//			" FROM ( " +
//			" 	SELECT DISTINCT ga.idGastoAlumno,  ga.numBillete,ga.numDieta,ga.numKm,ga.numPension,ga.otrosGastos,ga.precioBillete,ga.precioKm,ga.precioDieta, ga.precioPension," + 
//			"	ga.periodoLiquidacion.idPeriodoLiquidacion as idPeriodoLiquidacion,ga.matricula.idMatricula as idMatricula,ga.alumno,ga.distribucionPeriodo.idDistribucionPeriodo as idDistribucionPeriodo, ga.createdBy, null as createdDate, ga.lastModifiedBy, null as lastModifiedDate" + 
//			"   FROM Matricula m inner join OfertasCentro oc on oc.idOfertaCentro = m.ofertaCentro.idOfertaCentro " +
//			"   inner join OfertaFormativa off on off.idOfertaFormativa = oc.oferta.idOfertaFormativa " +
//			"   inner join Distribucion d on d.matricula.idMatricula = m.idMatricula " +
//			"   inner join Alumno a on a.idAlumno = m.alumno.idAlumno " +
//			"   inner join GastoAlumno ga on ga.matricula.idMatricula = m.idMatricula " +
//			"   WHERE off.ciclo.idCiclo  = :idCiclo " +
//			"	and m.turno = :regimen and m.unidad.nombre = :grupo "+
//			"   and oc.centro.idCentro = :idCentro " +
//			"   and m.anio = :curso " +
//			"   and d.fechaBaja is null " +
//			"   and ga.periodoLiquidacion.idPeriodoLiquidacion = :periodo " +
//			"   and d.idDistribucion in (select dp.distribucion.idDistribucion " +
//			"                               from DistribucionPeriodo dp" +
//			"								 where dp.periodo.idPeriodo IN ( select pp.idPeriodo from PeriodoPractica pp " +
//			"                                                        where pp.cursoAcademico.descripcion = :anioAcademico))" +
//			" ) ga group by ga.idPeriodoLiquidacion,ga.idMatricula,ga.idAlumno,ga.precioKm,ga.precioPension,ga.precioDieta, ga.createdBy, ga.lastModifiedBy")
//	List<GastoAlumno> getGastoAlumnoCursoNuevo(@Param("idCentro")String idCentro,@Param("idCiclo")String idCiclo,
//									   @Param("periodo") Integer periodo,@Param("curso") Integer curso,
//									   @Param("regimen")String regimen, @Param("grupo")String grupo,
//										@Param("anioAcademico") String anioAcademico);
//	
	 
	
	
	
	// Consulta usada para la exportacion de gastos //
	@Query("SELECT ga " + 
			"   FROM Matricula m " +
			"	inner join OfertasCentro oc on oc.idOfertaCentro = m.ofertaCentro.idOfertaCentro " +
			"   inner join Alumno a on a.idAlumno = m.alumno.idAlumno " +
			"   inner join GastoAlumno ga on ga.matricula.idMatricula = m.idMatricula " +
			"   inner join Distribucion d on d.matricula.idMatricula = m.idMatricula and ga.distribucionPeriodo.distribucion.idDistribucion = d.idDistribucion " +
			"   inner join DistribucionPeriodo dp on dp.distribucion.idDistribucion = d.idDistribucion " + 
			"	inner join PeriodoLiquidacion pl on pl.idPeriodoLiquidacion = ga.periodoLiquidacion.idPeriodoLiquidacion " +
			"   WHERE " +
			"   (m.unidad.tutor.idProfesor = :tutor or m.unidad.tutorAdicional.idProfesor = :tutor or :tutor = 'undefined') "+
			"   and (oc.centro.idCentro = :idCentro or :idCentro = 'undefined') " +
			"   and m.anio = :curso " +
			"   and d.fechaBaja is null " +
			"	order by ga.distribucionPeriodo.periodo.tipoPractica.nombre, ga.matricula.unidad.ofertaCentro.centro.nombre " 
			) 
	List<GastoAlumno> getGastosAlumnosByCursoAndCentroAndTutor(@Param("idCentro")String idCentro, @Param("curso") Integer curso, @Param("tutor")String tutor);
	
	List<GastoAlumno> findByDistribucionPeriodo(DistribucionPeriodo distribucionPeriodo);
	
	List<GastoAlumno> findByDistribucionPeriodoAndMatricula(DistribucionPeriodo distribucionPeriodo, Matricula matricula);

	@Query("SELECT g FROM GastoAlumno g " +
			"	inner join DistribucionPeriodo dp on g.distribucionPeriodo.idDistribucionPeriodo= dp.idDistribucionPeriodo " +
			"   inner join PeriodoPractica p on dp.periodo.idPeriodo = p.idPeriodo  " +
			"   WHERE p.idPeriodo  = :periodo "
	)
	List<GastoAlumno> getGastosPeriodo(@Param("periodo") Long periodo);


	@Modifying(clearAutomatically=true, flushAutomatically = true)
	@Transactional
	@Query("UPDATE GastoAlumno ga SET  ga.flValidado = :validado, ga.fValidacion= :fValidacion "+
			"                    WHERE ga.idGastoAlumno = :idGastoAlumno ")
	Integer updateValidaGasto(@Param("idGastoAlumno") Integer idGastoAlumno, @Param("validado") boolean validado, @Param("fValidacion") LocalDate fValidacion);

}

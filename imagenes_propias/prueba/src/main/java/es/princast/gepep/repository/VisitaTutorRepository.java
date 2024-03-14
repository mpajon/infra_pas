package es.princast.gepep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.AnexoContrato;
import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.domain.VisitaTutor;


/**
 * Spring Data JPA repository for the VisitaTutor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitaTutorRepository extends JpaRepository<VisitaTutor, Long> {

	 List<VisitaTutor> findAllByCiclo (Ciclo ciclo);
	 List<VisitaTutor> findAllByProfesor(Profesor profesor);
	 
	 List<VisitaTutor> findAllByProfesorAndMesAndAnioAndTipoPractica(Profesor profesor,Integer mes, Integer Anio, TipoPractica tipoPractica);
	 //CRQ151434
	 List<VisitaTutor> findAllByProfesorAndMesAndAnioAndTipoPracticaOrderByDiaAscMesAsc(Profesor profesor,Integer mes, Integer Anio, TipoPractica tipoPractica);
	 
	 List<VisitaTutor> findAllByProfesorAndAutorizadaAndMesAndAnio(Profesor profesor, Boolean autorizada,Integer mes, Integer Anio);
	 List<VisitaTutor> findAllByProfesorAndAutorizadaAndMesAndAnioAndTipoPractica(Profesor profesor, Boolean autorizada,Integer mes, Integer Anio, TipoPractica tipoPractica);
	 List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndAutorizadaAndRealizadaAndMesBetween(String idProfesor, Integer anio,Boolean autorizada, Boolean realizada, Integer mes1, Integer mes2);
	 List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndAutorizadaAndRealizadaAndMesBetweenOrderByMesAscDiaAsc (String idProfesor, Integer anio,Boolean autorizada, Boolean realizada, Integer mes1, Integer mes2);
	 
	 List<VisitaTutor> findAllByProfesorAndAnioAndMesOrderByIdVisitaTutor (Profesor profesor, Integer anio, Integer mes);
	 //CRQ151434
	 //List<VisitaTutor> findAllByProfesorAndAnioAndMesAndTipoPracticaOrderByIdVisitaTutor (Profesor profesor, Integer anio, Integer mes, TipoPractica tipoPractica);
	 List<VisitaTutor> findAllByProfesorAndAnioAndMesAndTipoPracticaOrderByDiaAscMesAsc (Profesor profesor, Integer anio, Integer mes, TipoPractica tipoPractica);
	 //List<VisitaTutor> findAllByProfesorAndAnioAndMesAndCicloOrderByIdVisitaTutor (Profesor profesor, Integer anio, Integer mes, Ciclo ciclo);
	 List<VisitaTutor> findAllByProfesorAndAnioAndMesAndCicloOrderByDiaAscMesAsc (Profesor profesor, Integer anio, Integer mes, Ciclo ciclo);
	 
	 //CRQ151434
	 //List<VisitaTutor> findAllByProfesorAndAnioAndMesAndCicloAndTipoPracticaOrderByIdVisitaTutor (Profesor profesor, Integer anio, Integer mes, Ciclo ciclo, TipoPractica tipoPractica);
	 List<VisitaTutor> findAllByProfesorAndAnioAndMesAndCicloAndTipoPracticaOrderByDiaAscMesAsc (Profesor profesor, Integer anio, Integer mes, Ciclo ciclo, TipoPractica tipoPractica);
	 List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndMes(String idProfesor, Integer anio, Integer mes);
	 List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndMesAndTipoPracticaOrderByDiaAscMesAsc (String idProfesor, Integer anio, Integer mes, TipoPractica tipoPractica);
 	 List<VisitaTutor> findAllByAnioAndMes(Integer anio,Integer mes);
 	 //List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndRealizadaAndCicloIdCicloAndMesBetween(String idProfesor, Integer anio,Boolean realizada, String idCiclo,Integer mes1, Integer mes2);
 	 List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndRealizadaAndCicloIdCicloAndMesBetweenOrderByDiaAscMesAsc(String idProfesor, Integer anio,Boolean realizada, String idCiclo,Integer mes1, Integer mes2);
 	//List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndAutorizadaAndRealizadaAndCicloIdCicloAndTipoPracticaIdTipoPracticaAndMesBetween(String idProfesor, Integer anio,Boolean autorizada, Boolean realizada, String idCiclo,Long idTipoPractica,Integer mes1, Integer mes2); 	 
 	List<VisitaTutor> findAllByProfesorIdProfesorAndAnioAndAutorizadaAndRealizadaAndCicloIdCicloAndTipoPracticaIdTipoPracticaAndMesBetweenOrderByDiaAscMesAsc(String idProfesor, Integer anio,Boolean autorizada, Boolean realizada, String idCiclo,Long idTipoPractica,Integer mes1, Integer mes2);

 	 
 	 @Query("select distinct visita from VisitaTutor visita " +
 	 		" where visita.profesor.idProfesor = :#{#filtroVisita.profesor.idProfesor} " +
 	 		" and visita.anio = :#{#filtroVisita.anio} " +
 	 		" and visita.mes = :#{#filtroVisita.mes} " +
 	 		" and visita.dia = :#{#filtroVisita.dia} " +
 	 		" and visita.tipoPractica.idTipoPractica = :#{#filtroVisita.tipoPractica.idTipoPractica} "
 	 		)
 	 List<VisitaTutor> findByProfesorDiaCicloTipoPractica(@Param("filtroVisita") VisitaTutor filtroVisita); 
 	 
 	 
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
     @Query("UPDATE VisitaTutor vt SET  vt.autorizada = :autoriza "+
             "                    WHERE vt.profesor.idProfesor = :idProfesor " +
             "                      AND vt.anio = :anio " +
             "                      AND vt.mes = :mes" +
             "                      AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)" +
             " 						AND vt.bloqueada = false")
     Integer updateAutorizaVisitaByTutorAndMesAndAnioAndTipoPractica(@Param("idProfesor") String idProfesor,
                               @Param("anio") int anio,
                               @Param("mes") int mes , @Param ("autoriza") boolean autoriza, @Param("idTipoPractica") Long idTipoPractica);
 	 
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
 	   @Query("UPDATE VisitaTutor vt SET  vt.autorizada= :autoriza, vt.realizada= :autoriza "+
 	             "                    WHERE vt.profesor.idProfesor = :idProfesor " +
 	             "                      AND vt.anio = :anio " +
 	             "                      AND vt.mes = :mes" +
 	            "                      AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)"+
 				" 					  AND vt.bloqueada = false")
 	  Integer updateNoAutorizaVisitaByTutorAndMesAndAnioAndTipoPractica(@Param("idProfesor") String idProfesor,
              @Param("anio") int anio,
              @Param("mes") int mes, @Param("autoriza") boolean autoriza, @Param("idTipoPractica") Long idTipoPractica);
 	
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
 	   @Query("UPDATE VisitaTutor vt SET  vt.bloqueada = :bloqueada "+
            "                    WHERE vt.profesor.idProfesor = :idProfesor " +
            "                      AND vt.anio = :anio " +
            "                      AND vt.mes = :mes " +
            "                      AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)")
 	   Integer updateBloqueaVisitaByTutorAndMesAndAnioAndTipoPractica(@Param("idProfesor") String idProfesor,
                              @Param("anio") int anio,
                              @Param("mes") int mes, @Param ("bloqueada") boolean bloqueada, @Param("idTipoPractica") Long idTipoPractica);
 	 	 
 	   
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
 	   @Query("UPDATE VisitaTutor vt SET  vt.realizada = (case when (horaEntrada!=null and horaSalida!=null)  then :realizada else vt.realizada end ) "+
 	            "                    WHERE vt.profesor.idProfesor = :idProfesor " +
 	            "                      AND vt.anio = :anio " +
 	            "                      AND vt.mes = :mes" +
 	            "                      AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)"+
 			   	" 					  AND vt.autorizada = true"+
 				"					   AND (date('' || vt.anio ||'-'|| vt.mes || '-' || vt.dia||'') < current_timestamp )")
 	 	Integer updateRealizaVisitaByTutorAndMesAndAnioAndTipoPractica(@Param("idProfesor") String idProfesor,
 	                              @Param("anio") int anio,
 	                              @Param("mes") int mes , @Param ("realizada") boolean realizada, @Param("idTipoPractica") Long idTipoPractica);
 	 
 	 
 	 
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
     @Query("UPDATE VisitaTutor vt SET  vt.autorizada = :autoriza "+
             "                    WHERE vt.anio = :anio " +
             "                      AND vt.mes = :mes" +
             "                      AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)"+
             "					AND vt.bloqueada = false")
     Integer updateAutorizaVisitaByMesAndAnioAndTipoPractica(@Param("anio") int anio,
                               @Param("mes") int mes , @Param ("autoriza") boolean autoriza, @Param("idTipoPractica") Long idTipoPractica);
 	 
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
 	   @Query("UPDATE VisitaTutor vt SET  vt.autorizada= :autoriza, vt.realizada= :autoriza "+
 	             "                    WHERE vt.anio = :anio " +
 	             "                    AND vt.mes = :mes" +
 	             "                    AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)" + 
 	             " 					  AND vt.bloqueada = false")
 	  Integer updateNoAutorizaVisitaByMesAndAnioAndTipoPractica( @Param("anio") int anio,
              @Param("mes") int mes, @Param("autoriza") boolean autoriza, @Param("idTipoPractica") Long idTipoPractica);
 	
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
 	   @Query("UPDATE VisitaTutor vt SET  vt.bloqueada = :bloqueada "+
            "                    WHERE vt.anio = :anio " +
            "                      AND vt.mes = :mes" +
            "                      AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)")
 	   Integer updateBloqueaVisitaByMesAndAnioAndTipoPractica( @Param("anio") int anio,
                              @Param("mes") int mes , @Param ("bloqueada") boolean bloqueada, @Param("idTipoPractica") Long idTipoPractica);
 	   
 	 @Modifying(clearAutomatically=true, flushAutomatically = true)
     @Transactional
 	   @Query("UPDATE VisitaTutor vt SET  vt.realizada = (case when (horaEntrada!=null and horaSalida!=null)  then :realizada else vt.realizada end ) "+
 	            "                    WHERE  vt.anio = :anio " +
 	            "                      AND vt.mes = :mes" +
 	            "                      AND ( vt.tipoPractica.idTipoPractica = :idTipoPractica or :idTipoPractica is null)" +
 				" 					   AND vt.autorizada = true"+
 			   	"					   AND (date('' || vt.anio ||'-'|| vt.mes || '-' || vt.dia||'') < current_timestamp )")
 	 	Integer updateRealizaVisitaMesAndAnioAndTipoPractica(@Param("anio") int anio,
 	                              @Param("mes") int mes , @Param ("realizada") boolean realizada, @Param("idTipoPractica") Long idTipoPractica);
	 
	 
  
	 
 	 
}	 	


package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.VisitaAgrupada;
 

public interface VisitaAgrupadaRepository extends JpaRepository<VisitaAgrupada, Long>, JpaSpecificationExecutor {
 
    @Query(nativeQuery = true)
	 Iterable<VisitaAgrupada> findListadoByAnioAndMes( @Param("anio") Integer anio, @Param("mes") Integer mes);
    
    @Query(nativeQuery = true)
	 Iterable<VisitaAgrupada> findListadoByCentroAndAnioAndMes(@Param("centro") String centro, @Param("anio") Integer anio, @Param("mes") Integer mes);
     
     
    @Query(nativeQuery = true)
	 Iterable<VisitaAgrupada> findListadoByAnioAndMesAndTutor( @Param("anio") Integer anio, @Param("mes") Integer mes, @Param("tutor") String tutor);
 
    @Query(nativeQuery = true)
  	 Iterable<VisitaAgrupada> findListadoByCentroAndAnioAndMesAndTutor(@Param("centro") String centro, @Param("anio") Integer anio, @Param("mes") Integer mes,  @Param("tutor") String tutor);
    
    
    @Query(nativeQuery = true)
	 Iterable<VisitaAgrupada> findListadoByCentroAndAnioAndMesAndTipoPractica(@Param("centro") String centro, @Param("anio") Integer anio, @Param("mes") Integer mes, @Param("idTipoPractica") Integer idTipoPractica, @Param("cursoAca") Integer cursoAca);
    
   
    @Query(nativeQuery = true)
  	 Iterable<VisitaAgrupada> findListadoByCentroAndAnioAndMesAndTipoPracticaAndTutor(@Param("centro") String centro, @Param("anio") Integer anio, @Param("mes") Integer mes, @Param("idTipoPractica") Integer idTipoPractica,  @Param("tutor") String tutor);
    
    
}

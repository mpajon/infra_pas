package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.OfertaMatric;

public interface OfertaMatricRepository extends JpaRepository<OfertaMatric, Long>, JpaSpecificationExecutor {
 
    @Query(nativeQuery = true)
	 Iterable<OfertaMatric> findListadoOferMatric(@Param("centro")String centro, @Param("anio") Integer anio);
    
    @Query(nativeQuery = true)
	 Iterable<OfertaMatric> findListadoOferMatricTutor(@Param("centro")String centro, @Param("anio") Integer anio, @Param("tutor") String tutor);
   
    
    @Query(nativeQuery = true)
	 Iterable<OfertaMatric> findByTipoPractica(@Param("centro")String centro, @Param("anio") Integer anio , @Param("idTipoPractica") Integer idTipoPractica);
   
    @Query(nativeQuery = true)
 	 Iterable<OfertaMatric> findByTipoPracticaAndTutor(@Param("centro")String centro, @Param("anio") Integer anio , @Param("idTipoPractica") Integer idTipoPractica,  @Param("tutor") String tutor);
    
  
    @Query(nativeQuery = true)
	 Iterable<OfertaMatric> findByTipoPracticaAndPeriodo(@Param("centro")String centro, @Param("anio") Integer anio , @Param("idTipoPractica") Integer idTipoPractica, @Param("idPeriodoPractica") Integer idPeriodoPractica);
 
    @Query(nativeQuery = true)
  	 Iterable<OfertaMatric> findByTipoPracticaAndPeriodoAndUnidad(@Param("centro")String centro, @Param("anio") Integer anio , @Param("idTipoPractica") Integer idTipoPractica, @Param("idPeriodoPractica") Integer idPeriodoPractica,@Param("unidad")String unidad,@Param("ofercen")String ofercen,@Param("curso") Integer curso, @Param("turno")String turno);
   
    
     @Query(nativeQuery = true)
	 Iterable<OfertaMatric> findByTipoPracticaAndPeriodoAndTutor(@Param("centro")String centro, @Param("anio") Integer anio , @Param("idTipoPractica") Integer idTipoPractica, @Param("idPeriodoPractica") Integer idPeriodoPractica, @Param("tutor") String tutor);
  
     @Query(nativeQuery = true)
   	 Iterable<OfertaMatric> findListadoParaMatriculacion(@Param("centro")String centro, @Param("anio") Integer anio);
     
}

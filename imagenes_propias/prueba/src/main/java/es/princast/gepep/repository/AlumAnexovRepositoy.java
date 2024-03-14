package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.AlumAnexov;


public interface AlumAnexovRepositoy extends JpaRepository<AlumAnexov, Long>, JpaSpecificationExecutor {
 
    @Query(nativeQuery = true)
	 Iterable<AlumAnexov> findAlumByAnexo(@Param("idCentro") String idCentro,@Param("curso") Integer curso, @Param("idOferta") String idOferta, @Param("idPeriodoPractica") Integer idPeriodoPractica );

    @Query(nativeQuery = true)
	 Iterable<AlumAnexov> findAlumByAnexoByTutor(@Param("idCentro") String idCentro,@Param("curso") Integer curso, @Param("idOferta") String idOferta, @Param("idPeriodoPractica") Integer idPeriodoPractica, @Param("tutor") String tutor );

    
    
    @Query(nativeQuery = true)
 	 Iterable<AlumAnexov> findAlumByUnidadAnexo(@Param("idCentro") String idCentro,@Param("curso") Integer curso, @Param("idOferta") String idOferta, @Param("idPeriodoPractica") Integer idPeriodoPractica , @Param("idUnidad") String idUnidad);
    
    @Query(nativeQuery = true)
	 Iterable<AlumAnexov> findAlumByUnidadAnexoByTutor(@Param("idCentro") String idCentro,@Param("curso") Integer curso, @Param("idOferta") String idOferta, @Param("idPeriodoPractica") Integer idPeriodoPractica, @Param("idUnidad") String idUnidad, @Param("tutor") String tutor );
}

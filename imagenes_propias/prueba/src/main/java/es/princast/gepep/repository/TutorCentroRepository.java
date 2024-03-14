package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 
import es.princast.gepep.domain.TutorCentro;

public interface TutorCentroRepository extends JpaRepository<TutorCentro, Long>, JpaSpecificationExecutor {
 
	 @Query(nativeQuery = true)
	 Iterable<TutorCentro> findTutorByUsuario(@Param("nif")String  nif);
	 
	 @Query(nativeQuery = true)
	 Iterable<TutorCentro> findTutorByUsuarioAndAnio(@Param("nif")String  nif,@Param("anio") Integer anio);
	 
	  @Query(nativeQuery = true)
	 Iterable<TutorCentro> findTutorByUsuarioAndAnioAndCentro(@Param("nif") String  nif,@Param("centro") String centro, @Param("anio") Integer anio);
	 
	 @Query(nativeQuery = true)
	 Iterable<TutorCentro> findTutorByUsuarioAndAnioAndCentroAndTipoPractica(@Param("nif") String  nif,@Param("centro") String centro, @Param("anio") Integer anio, @Param("idTipoPractica") Integer idTipoPractica);
	 
    @Query(nativeQuery = true)
	 Iterable<TutorCentro> findListadoTutoresCentroAnio(@Param("centro")String centro, @Param("anio") Integer anio);
    
    @Query(nativeQuery = true)
	 Iterable<TutorCentro> findListadoTutoresCentroAnioAndTipoPractica(@Param("centro")String centro, @Param("anio") Integer anio, @Param("idTipoPractica") Long idTipoPractica);
    
    
    @Query(nativeQuery = true)
 	 Iterable<TutorCentro> findListadoTutoresCentroAnioAndTipoPracticaDeportiva(@Param("centro")String centro, @Param("anio") Integer anio, @Param("idTipoPractica") Long idTipoPractica);
    
    
    @Query(nativeQuery = true)
    Iterable<TutorCentro> findListadoTutoresCentroAnioOfertaCentro(@Param("centro")String centro, @Param("anio") Integer anio,@Param("ofercen") String ofercen);

    @Query(nativeQuery = true)
	Iterable<TutorCentro> findListadoTutoresCentroAnioLite(@Param("centro")String centro, @Param("anio") Integer anio);
    
  
}

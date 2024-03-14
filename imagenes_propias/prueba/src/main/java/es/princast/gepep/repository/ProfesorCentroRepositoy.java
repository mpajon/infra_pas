package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.ProfesorCentro;


public interface ProfesorCentroRepositoy extends JpaRepository<ProfesorCentro, Long>, JpaSpecificationExecutor {
 
    @Query(nativeQuery = true)
	 Iterable<ProfesorCentro> getListaProfesorByCentroByTipoPractica(@Param("curso") Integer curso, @Param("idCentro") String idCentro, @Param("idTipoPractica") Integer idTipoPractica);
    
    @Query(nativeQuery = true)
	 Iterable<ProfesorCentro> getListaProfesorByCentroByTipoPracticaByTutor(@Param("curso") Integer curso, @Param("idCentro") String idCentro, @Param("idTipoPractica") Integer idTipoPractica, @Param("tutor") String tutor);

}

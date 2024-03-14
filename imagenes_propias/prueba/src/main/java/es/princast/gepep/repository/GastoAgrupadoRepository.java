package es.princast.gepep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.GastoAgrupado;

public interface GastoAgrupadoRepository extends JpaRepository<GastoAgrupado, Integer>, JpaSpecificationExecutor {
 
    @Query(nativeQuery = true)	 
    List<GastoAgrupado> findGastoAlumnoCursoNuevo(@Param("idCentro")String idCentro,@Param("idCiclo")String idCiclo, @Param("periodo") Integer periodo,@Param("curso") Integer curso, @Param("regimen")String regimen, @Param("grupo")String grupo,@Param("anioAcademico") Integer anioAcademico);
    List<GastoAgrupado> findGastoAlumnoCursoNuevoTipo2(@Param("idCentro")String idCentro,@Param("idCiclo")String idCiclo, @Param("periodo") Integer periodo,@Param("curso") Integer curso, @Param("regimen")String regimen, @Param("grupo")String grupo,@Param("anioAcademico") Integer anioAcademico);
    
}

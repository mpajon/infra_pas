package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.ControlSeguimiento;

public interface ControlSeguimientoRepository extends JpaRepository<ControlSeguimiento, Long>, JpaSpecificationExecutor<ControlSeguimiento> {
 
    @Query(nativeQuery = true)
	 Iterable<ControlSeguimiento> getResumenControlSeguimiento(@Param("idCentro")String idCentro, @Param("anio") Integer anio, @Param("tutor")String tutor);
    
    @Query(nativeQuery = true)
	 Iterable<ControlSeguimiento> getContadoresControlSeguimiento(@Param("idCentro")String idCentro, @Param("anio") Integer anio, @Param("tutor")String tutor);

}

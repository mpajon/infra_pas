package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.AlumnoExportSauce;

public interface AlumnoExportSauceRepository extends JpaRepository<AlumnoExportSauce, Long>, JpaSpecificationExecutor<AlumnoExportSauce> {
 
    @Query(nativeQuery = true)
	 Iterable<AlumnoExportSauce> getAlumnosExportSauce(@Param("anio") Integer anio);
    
  
}

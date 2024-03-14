package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.AlumDisAnexoiii;


public interface AlumDisAnexoiiiRepositoy extends JpaRepository<AlumDisAnexoiii, Long>, JpaSpecificationExecutor {
 
    @Query(nativeQuery = true)
	 Iterable<AlumDisAnexoiii> findAlumDisByAnexo(@Param("idAnexo") Integer idAnexo);

}

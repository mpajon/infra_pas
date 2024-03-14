package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.OfertaEducativa;
import es.princast.gepep.domain.OfertaMatric;

public interface OfertaEducativaRepository extends JpaRepository<OfertaEducativa, Long>, JpaSpecificationExecutor {
 
    @Query(nativeQuery = true)
	 Iterable<OfertaEducativa> findByCentroAndAnio(@Param("centro")String centro,@Param("anio")Integer anio);
}

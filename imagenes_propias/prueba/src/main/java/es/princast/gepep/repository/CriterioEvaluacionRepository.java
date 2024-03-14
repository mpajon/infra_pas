package es.princast.gepep.repository;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.CriterioEvaluacion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CriterioEvaluacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriterioEvaluacionRepository extends JpaRepository<CriterioEvaluacion, Long> {

	List<CriterioEvaluacion> findAllByCiclo(Ciclo ciclo, Pageable pageable);
	
	 Optional <CriterioEvaluacion> findCriterioByCiclo(Ciclo ciclo);
}

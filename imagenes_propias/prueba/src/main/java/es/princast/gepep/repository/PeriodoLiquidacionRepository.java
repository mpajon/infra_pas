package es.princast.gepep.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.PeriodoLiquidacion;
import es.princast.gepep.domain.TipoPractica;


/**
 * Spring Data JPA repository for the PeriodoPractica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodoLiquidacionRepository extends JpaRepository<PeriodoLiquidacion, Integer> {
	
		Iterable<PeriodoLiquidacion> findAllByTipoPractica(TipoPractica tipopractica);
}

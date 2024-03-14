package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.CriterioEvaluacion;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.domain.ResultadoAprendizaje;;


/**
 * Spring Data JPA repository for the CapacidadTerminal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultadoAprendizajeRepository extends JpaRepository<ResultadoAprendizaje, Long> {

	 List <ResultadoAprendizaje> findAllByCiclo(Ciclo ciclo, Pageable pageable);
	 
	Optional <ResultadoAprendizaje> findResultadoByCiclo(Ciclo ciclo);
}

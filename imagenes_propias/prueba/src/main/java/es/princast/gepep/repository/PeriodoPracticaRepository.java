package es.princast.gepep.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.HorasPeriodo;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.domain.TipoPractica;


/**
 * Spring Data JPA repository for the PeriodoPractica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodoPracticaRepository extends JpaRepository<PeriodoPractica, Long>, JpaSpecificationExecutor {
	
		Iterable<PeriodoPractica> findAllByTipoPractica(TipoPractica tipopractica);
		List <PeriodoPractica> findAllByTipoPracticaAndNombre (TipoPractica tipopractica, String nombre);
		Iterable<PeriodoPractica> findAllByHorario(HorasPeriodo horario);
		Iterable<PeriodoPractica> findAllByCursoAcademico(CursoAcademico cursoAcademico);
		Iterable<PeriodoPractica> findAllByTipoPracticaAndCursoAcademico(TipoPractica tipopractica, CursoAcademico cursoAcademico);
}

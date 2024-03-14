package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.BloqueoGastos;
import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.PeriodoLiquidacion;
import es.princast.gepep.domain.Unidad;



/**
 * Spring Data JPA repository for the BloqueoGastos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BloqueoGastosRepository extends JpaRepository<BloqueoGastos, Long> {

	Iterable<BloqueoGastos> findAllByCursoAcademico(CursoAcademico curso);
    Iterable<BloqueoGastos> findAllByPeriodo(PeriodoLiquidacion periodo);
    Optional<BloqueoGastos> findOneByPeriodoAndCursoAcademico(PeriodoLiquidacion periodo, CursoAcademico curso);
    Iterable<BloqueoGastos> findAllByPeriodoAndCursoAcademico(PeriodoLiquidacion periodo, CursoAcademico curso);
 
    
}

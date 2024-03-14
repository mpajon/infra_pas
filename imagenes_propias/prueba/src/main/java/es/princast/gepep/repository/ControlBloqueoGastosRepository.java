package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.princast.gepep.domain.ControlBloqueoGastos;

public interface ControlBloqueoGastosRepository
		extends JpaRepository<ControlBloqueoGastos, Long>, JpaSpecificationExecutor<ControlBloqueoGastos> {

	@Query(nativeQuery = true)
	Iterable<ControlBloqueoGastos> getPeriodosResumen();

	@Query(nativeQuery = true)
	Iterable<ControlBloqueoGastos> getPeriodosResumenByAnio(@Param("anio") Integer anio);

	@Query(nativeQuery = true)
	Iterable<ControlBloqueoGastos> getPeriodosResumenByAnioAndTipoPractica(@Param("idTipoPrac") Integer idTipoPrac,
			@Param("anio") Integer anio);

	@Query(nativeQuery = true)
	Iterable<ControlBloqueoGastos> getPeriodosResumenByAnioAndTipoPracticaAndPeriodoLiquidacion(
			@Param("idPerLiq") Integer idPerLiq, @Param("idTipoPrac") Integer idTipoPrac, @Param("anio") Integer anio);

}



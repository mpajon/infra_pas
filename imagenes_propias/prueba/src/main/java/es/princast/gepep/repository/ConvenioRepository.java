package es.princast.gepep.repository;

import es.princast.gepep.domain.Area;
import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.Convenio;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.TipoPractica;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Convenio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConvenioRepository extends JpaRepository<Convenio, Long> , JpaSpecificationExecutor<Convenio>{

	List<Convenio> findAllByCentroAndFechaBajaIsNullAndValidadoIsTrue(Centro centro);
	List<Convenio> findAllByCentroAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(Centro centro);
	List<Convenio> findAllByCentroAndTipoPracticaAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(Centro centro,TipoPractica tipoPractica);

	List<Convenio> findAllByCentroAndTipoPracticaAndFechaBajaIsNullAndAntiguoIsFalseAndValidadoIsTrueOrderByNumeroAsc(Centro centro,TipoPractica tipoPractica);

	@Query("select convenio from Convenio convenio"
			+ " inner join TipoPractica tp on convenio.tipoPractica.idTipoPractica = tp.idTipoPractica "
			+ " inner join Centro cen on convenio.centro.idCentro = cen.idCentro "
		 	+ " where convenio.tipoPractica.idTipoPractica = :idTipoPractica and convenio.centro.idCentro =  :idCentro "
			+ " and convenio.fechaBaja is null and (convenio.fechaFin is null or convenio.fechaFin > current_date) and convenio.antiguo IS false and convenio.validado IS true "
			+ " order by convenio.centro.codigo asc, convenio.codigo desc ") 
	List<Convenio> findConveniosToDistribucion(@Param("idTipoPractica")Long idTipoPractica, @Param("idCentro") String idCentro);
	
 	
	List<Convenio> findAllByFechaBajaIsNull ();	
	List<Convenio> findAllByCodigoAndCentroAndFechaBajaIsNullAndAntiguoIsFalse(String codigo,Centro centro);
	
	List<Convenio> findAllByCodigoAndTipoPracticaAndCentroAndFechaBajaIsNullAndAntiguoIsFalse(String codigo,TipoPractica tipoPractica,Centro centro);
	
	Iterable<Convenio> findAllByTipoPractica(TipoPractica tipoPractica);
	List<Convenio> findAllByArea(Area area);
	List<Convenio> findAllByCentroAndTipoPracticaAndCodigoStartsWithAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(Centro centro,TipoPractica tipoPractica,String codigo);
	//List<Convenio> findAllByCentroAndTipoPracticaAndCodigoStartsWithAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(@Param("centro") Centro centro,@Param("tipoPractica") TipoPractica tipoPractica, @Param("codigo") String codigo);

}

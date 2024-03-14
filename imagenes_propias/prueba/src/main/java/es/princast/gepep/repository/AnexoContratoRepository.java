package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.AnexoContrato;
import es.princast.gepep.domain.Convenio;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.domain.TipoPractica;


/**
 * Spring Data JPA repository for the AnexoContrato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnexoContratoRepository extends JpaRepository<AnexoContrato, Long> {
	 
	
	 Iterable<AnexoContrato> findAllByConvenioOrderByCodAnexoDesc (Convenio convenio);
	 Iterable<AnexoContrato> findAllByConvenioOrderByCodAnexoAsc (Convenio convenio);
	 Iterable<AnexoContrato> findAllByOfertaCentro (OfertasCentro ofertaCentro);
	 Iterable<AnexoContrato> findAllByOfertaCentroAndPeriodo (OfertasCentro ofertaCentro,PeriodoPractica periodo);
	
	 
	 @Query(" select distinct anexo "
	 		+ " from AnexoContrato anexo "
	 		+ " left join fetch anexo.convenio "
	 		+ " where "
	 		+ " anexo.ofertaCentro.idOfertaCentro= :idOfertaCentro "
	 		+ " and anexo.unidad.nombre= :idUnidad "
	 		+ " and anexo.unidad.anio= :idAnio "
	 		+ " and anexo.convenio.tipoPractica.idTipoPractica = :idTipoPractica "
	 		+ " and (anexo.convenio.fechaFin is null or anexo.convenio.fechaFin > current_date ) ")
	 Iterable<AnexoContrato> findOneWithEagerRelationships(@Param("idOfertaCentro") String idOfertaCentro,@Param("idUnidad") String idUnidad, @Param("idAnio") Integer idAnio, @Param("idTipoPractica") Long idTipoPractica); 
	
	 
	 @Query(" select distinct anexo "
		 		+ " from AnexoContrato anexo "
		 		+ " left join fetch anexo.convenio "
		 		+ " where "
		 		+ " anexo.ofertaCentro.idOfertaCentro= :idOfertaCentro "
		 		+ " and anexo.unidad.nombre= :idUnidad "
		 		+ " and anexo.unidad.anio= :idAnio "
		 		+ " and anexo.periodo.idPeriodo= :idPeriodo "
		 		+ " and anexo.convenio.tipoPractica.idTipoPractica = :idTipoPractica "
		 		+ " and (anexo.convenio.fechaFin is null or anexo.convenio.fechaFin > current_date ) ")
		 Iterable<AnexoContrato> findOneWithEagerRelationships(@Param("idOfertaCentro") String idOfertaCentro,@Param("idUnidad") String idUnidad, @Param("idAnio") Integer idAnio, @Param("idTipoPractica") Long idTipoPractica,@Param("idPeriodo") Long idPeriodo ); 
		 
	 
	 @Query(" select distinct anexo "
		 		+ " from AnexoContrato anexo "
		 		+ " left join fetch anexo.convenio "
		 		+ " where "
		 		+ " anexo.ofertaCentro.idOfertaCentro= :idOfertaCentro "
		 		+ " and anexo.unidad.nombre= :idUnidad "
		 		+ " and anexo.unidad.anio= :idAnio "
		 		+ " and anexo.periodo.idPeriodo= :idPeriodo "
		 		+ " and anexo.convenio.tipoPractica.idTipoPractica = :idTipoPractica "
		 		+ " and (anexo.convenio.fechaFin is null or anexo.convenio.fechaFin > current_date ) "
		 		+ " and anexo.idAnexo not in "
		 		+ " 		(select distribucion.anexoContrato.idAnexo from Matricula mat "
		 		+ "			inner join Unidad unidad on mat.unidad.idUnidad = unidad.idUnidad "
		 		+ " 		inner join Distribucion distribucion on distribucion.matricula.idMatricula = mat.idMatricula  "	 		
				+ " 		inner join DistribucionPeriodo distribucionPeriodo on distribucion.idDistribucion = distribucionPeriodo.distribucion.idDistribucion  "
				+ " 		inner join PeriodoPractica periodoPractica on periodoPractica.idPeriodo = distribucionPeriodo.periodo.idPeriodo  and mat.alumno.idAlumno = :idAlumno )") 
	 Iterable<AnexoContrato> findOneWithEagerRelationshipsNoDistributed(@Param("idOfertaCentro") String idOfertaCentro,@Param("idUnidad") String idUnidad, @Param("idAnio") Integer idAnio, @Param("idTipoPractica") Long idTipoPractica,@Param("idPeriodo") Long idPeriodo,@Param("idAlumno") String idAlumno ); 
	
	 
	 @Query("select distinct anexo from AnexoContrato anexo left join fetch anexo.convenio where anexo.ofertaCentro.idOfertaCentro = :idOfertaCentro and (anexo.convenio.fechaFin is null or anexo.convenio.fechaFin > current_date ) ")
	 Iterable<AnexoContrato> findOneWithEagerRelationships(@Param("idOfertaCentro") String idOfertaCentro); 
	 
	 @Query("select distinct anexo from AnexoContrato anexo left join fetch anexo.convenio left join fetch anexo.periodo where anexo.ofertaCentro.idOfertaCentro = :idOfertaCentro and anexo.periodo.idPeriodo = :idPeriodo  and (anexo.convenio.fechaFin is null or anexo.convenio.fechaFin > current_date ) ")
	 Iterable<AnexoContrato> findOneWithEagerRelationships(@Param("idOfertaCentro") String idOfertaCentro,@Param("idPeriodo") Long idPeriodo); 
	 
	 @Query("select distinct anexo from AnexoContrato anexo left join fetch anexo.convenio where anexo.idAnexo= :idAnexo")
	 Optional<AnexoContrato> findOneAnxWithEagerRelationships(@Param("idAnexo") Long idAnexo); 
	
	 
	 
	  List<AnexoContrato> findAllByOfertaCentroAndConvenio(OfertasCentro ofertaCentro, Convenio convenio);
	  
	  List<AnexoContrato> findAllByOfertaCentroAndConvenioAndPeriodo(OfertasCentro ofertaCentro, Convenio convenio,PeriodoPractica periodo);
 

}

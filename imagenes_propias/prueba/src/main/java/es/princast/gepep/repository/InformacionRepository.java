package es.princast.gepep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.Informacion;


/**
 * Spring Data JPA repository for the Informacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformacionRepository extends JpaRepository<Informacion, Long> {

	@Query(value = "select i from Informacion i join fetch i.tipoPractica where i.fechaBaja is null order by i.tipoPractica.nombre, i.nombre")
	List<Informacion> findAllByFechaBajaIsNull();
	
	@Query(value = "select i from Informacion i join fetch i.tipoPractica where i.nombre = :nombre")
	List<Informacion> findAllByNombre(@Param ("nombre") String nombre);
	
	@Query(value = "select i from Informacion i join fetch i.tipoPractica where i.fechaBaja is null and i.nombre = :nombre")
	List<Informacion> findAllByNombreAndFechaBajaIsNull(@Param ("nombre") String nombre);
	
	@Query(value = "select i from Informacion i join fetch i.tipoPractica where  i.nombre = :nombre and i.tipoPractica.idTipoPractica = :idTipoPractica")
	List<Informacion> findAllByNombreAndIdTipoPractica(@Param ("nombre") String nombre, @Param ("idTipoPractica") Long idTipoPractica);
	
	@Modifying(clearAutomatically = true)
	 @Query(value = "UPDATE  es.princast.gepep.domain.Informacion inf  SET inf.fechaBaja= :#{#informacion.fechaBaja}  WHERE inf.idInformacion = :#{#informacion.idInformacion}")    
	 Integer updateBaja(@Param ("informacion") Informacion informacion);
}

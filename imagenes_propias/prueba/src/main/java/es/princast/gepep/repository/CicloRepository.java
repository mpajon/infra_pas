package es.princast.gepep.repository;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.TipoPractica;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Ciclo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CicloRepository extends JpaRepository<Ciclo, String> {

	List<Ciclo> findAllByFechaBajaIsNullOrderByNombreAsc();
	List<Ciclo> findAllByFechaBajaIsNull();
	List<Ciclo> findAllByCodigo(String codigo);
	List<Ciclo> findAllByCodigoAndDeSauce(String codigo,Boolean deSauce);
	
}

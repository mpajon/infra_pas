package es.princast.gepep.repository;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.Realizacion;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Realizacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RealizacionRepository extends JpaRepository<Realizacion, Long> {
	
	List<Realizacion>  findAllByCiclo(Ciclo ciclo, Pageable pageable);
	Optional<Realizacion> findRealizacionByCiclo(Ciclo ciclo);

}

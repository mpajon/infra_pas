package es.princast.gepep.repository;

import es.princast.gepep.domain.Visor;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Visor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisorRepository extends JpaRepository<Visor, Long> {

	List<Visor> findAllByNombreAndApellidos(String nombre,String apellidos);
}

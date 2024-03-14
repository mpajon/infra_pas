package es.princast.gepep.repository;


import es.princast.gepep.domain.Sector;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
		
    Optional<Sector> findByCodigo(String codigo);
    Iterable<Sector> findByNombre(String nombre);
    List<Sector> findAllByCodigoAndNombre(String codigo, String nombre);
    List<Sector> findAllByCodigo(String codigo);
    List<Sector> findAllByNombre(String nombre);

}

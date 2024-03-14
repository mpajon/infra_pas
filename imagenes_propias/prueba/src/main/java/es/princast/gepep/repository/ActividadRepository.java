package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.Actividad;
import es.princast.gepep.domain.Sector;


/**
 * Spring Data JPA repository for the Actividad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
	
	@Query(value = "SELECT a "
	    		+ " from Actividad a join fetch a.sector ")
	List<Actividad> findAll(Sort sort);
	Iterable<Actividad> findAllBySector(Sector sector);
    Optional<Actividad> findByCodigo(String codigo);
    Iterable<Actividad> findByNombre(String nombre);
    Iterable<Actividad> findBySector(Sector sector);
    List<Actividad> findAllByCodigoAndNombre(String codigo, String nombre);
    List<Actividad> findAllByCodigoAndSector(String codigo, Sector sector);
}

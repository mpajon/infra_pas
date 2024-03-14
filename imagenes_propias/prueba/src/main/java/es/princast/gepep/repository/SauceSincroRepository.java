package es.princast.gepep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.SauceSincro;


/**
 * Spring Data JPA repository for the Visor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SauceSincroRepository extends JpaRepository<SauceSincro, String> {

}

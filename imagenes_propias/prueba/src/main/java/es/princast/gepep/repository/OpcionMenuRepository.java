package es.princast.gepep.repository;

import es.princast.gepep.domain.OpcionMenu;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OpcionMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, Long> {

}

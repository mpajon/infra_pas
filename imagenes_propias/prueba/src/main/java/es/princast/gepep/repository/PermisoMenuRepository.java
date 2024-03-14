package es.princast.gepep.repository;

import es.princast.gepep.domain.PermisoMenu;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PermisoMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermisoMenuRepository extends JpaRepository<PermisoMenu, Long> {

}

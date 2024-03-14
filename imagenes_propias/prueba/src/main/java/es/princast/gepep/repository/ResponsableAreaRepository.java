package es.princast.gepep.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.Area;
import es.princast.gepep.domain.ResponsableArea;


/**
 * Spring Data JPA repository for the ResponsableArea entity.
 */

@Repository
public interface ResponsableAreaRepository extends JpaRepository<ResponsableArea, Long> {
	Iterable<ResponsableArea> findByArea (Area area);
}

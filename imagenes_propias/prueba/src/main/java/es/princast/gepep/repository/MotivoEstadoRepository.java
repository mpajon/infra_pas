package es.princast.gepep.repository;

import es.princast.gepep.domain.MotivoEstado;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MotivoEstado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotivoEstadoRepository extends JpaRepository<MotivoEstado, Long> {

}

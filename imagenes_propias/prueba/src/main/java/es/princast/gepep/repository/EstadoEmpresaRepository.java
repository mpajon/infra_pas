package es.princast.gepep.repository;

import es.princast.gepep.domain.EstadoEmpresa;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EstadoEmpresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadoEmpresaRepository extends JpaRepository<EstadoEmpresa, Long> {

}

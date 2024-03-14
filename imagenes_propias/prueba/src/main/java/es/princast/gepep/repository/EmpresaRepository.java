package es.princast.gepep.repository;

import es.princast.gepep.domain.Empresa;
import es.princast.gepep.domain.Ensenanza;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Empresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {

	   Optional<Empresa> findByCif (String cif);
	   Optional<Empresa> findByCifAndFechaBajaIsNull(String cif);
	   List<Empresa> findAllByFechaBajaIsNull();
	   List<Empresa> findAllByCifAndFechaBajaIsNull(String cif);
}

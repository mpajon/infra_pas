package es.princast.gepep.repository;

import es.princast.gepep.domain.Tutor;
import es.princast.gepep.domain.Usuario;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tutor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
	
	List<Tutor> findAllByUsuario(Usuario usuario);
}

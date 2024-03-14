package es.princast.gepep.repository;

import es.princast.gepep.domain.Perfil;
import es.princast.gepep.domain.Usuario;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Perfil entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

	//Iterable<Perfil> findAllByUsuario(Optional<Usuario> usuario);
}

package es.princast.gepep.repository;


import es.princast.gepep.domain.Area;
import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Perfil;
import es.princast.gepep.domain.Usuario;
import es.princast.mntpa.back.web.rest.util.ResponseUtil;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codahale.metrics.annotation.Timed;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;


/**
 * Spring Data JPA repository for the Usuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> , JpaSpecificationExecutor<Usuario>{
	
	
	//Optional<Usuario> findOneByActivationKey(String activationKey);
	
	Optional<Usuario> findOneByLoginIgnoreCase(String login);
	Optional<Usuario> findOneByLoginIgnoreCaseAndPwd(String login,String password);
	List<Usuario> findAllByCentroAndFechaBajaIsNull(Centro centro);	
	List<Usuario> findAllByLoginIgnoreCase(String login);
	Optional<Usuario> findOneByDni(String dni);
	List<Usuario> findAllByPerfilAndFechaBajaIsNullAndActivoIsTrue(Perfil perfil);

	//Page<Usuario> findAllOrderByNombreAsc(@Nullable Specification<Usuario> spec, Pageable pageable);
	
	//@Query(value = "SELECT u FROM Usuario u order by lower(nombre) asc")
	//Page<Usuario> findAll1(@Nullable Specification<Usuario> spec, Pageable pageable);
	

/*    List<Usuario> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<Usuario> findOneByResetKey(String resetKey);

    Optional<Usuario> findOneByEmailIgnoreCase(String email);*/

//    Optional<Usuario> findOneByLogin(String login);
//
//    @EntityGraph(attributePaths = "authorities")
//    Optional<Usuario> findOneWithPerfilById(Long id);
//
//    @EntityGraph(attributePaths = "authorities")
//    ResponseEntity<Usuario> findOneWithPerfilByLogin(String login);
//
//    //@EntityGraph(attributePaths = "authorities")
//    //Optional<Usuario> findOneWithAuthoritiesByEmail(String email);
//
//    Page<Usuario> findAllByLoginNot(Pageable pageable, String login);
    
    
    
}

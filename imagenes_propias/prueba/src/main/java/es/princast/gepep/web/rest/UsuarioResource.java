package es.princast.gepep.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.Usuario;
import es.princast.gepep.service.ProfesorService;
import es.princast.gepep.service.UsuarioService;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.gepep.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Usuario.
 */
@RestController
@RequestMapping("/api")
public class UsuarioResource {

    private static final Long ROLE_CENTRO = Long.valueOf(3);

	private final Logger log = LoggerFactory.getLogger(UsuarioResource.class);

    private static final String ENTITY_NAME = "usuario";

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ProfesorService profesorService;
    /**
     * POST  /usuarios : Create a new usuario.
     *
     * @param usuario the usuario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new usuario, or with status 400 (Bad Request) if the usuario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/usuarios")
    @Timed
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) throws URISyntaxException {
        log.debug("REST request to save Usuario : {}", usuario);
        if (usuario.getPerfil().getDescripcion().equals("ROLE_CENTRO") && profesorService.getProfesorByNif(usuario.getDni()) != null){
        	log.debug("Cumple requisitos");
        }
        
        Profesor profesor = null;

		if (usuario.getPerfil().getIdPerfil().equals(ROLE_CENTRO)) {
			profesor = profesorService.getProfesorByNif(usuario.getDni());
			profesor.setUsuario(usuario);
		}

		
			ResponseEntity<Usuario> nuevoUsuario = usuarioService.createUsuario(usuario);
			if (profesor != null) {
				profesorService.updateProfesor(profesor);
			}
        	
        return nuevoUsuario;
    }
    
   

    /**
     * PUT  /usuarios : Updates an existing usuario.
     *
     * @param usuario the usuario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated usuario,
     * or with status 400 (Bad Request) if the usuario is not valid,
     * or with status 500 (Internal Server Error) if the usuario couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/usuarios")
    @Timed
	public ResponseEntity<String> updateUsuario(@Valid @RequestBody Usuario usuario) throws URISyntaxException {
		log.debug("REST request to update Usuario : {}", usuario);
		Profesor profesor = null;

		if (usuario.getPerfil().getDescripcion().equals("ROLE_CENTRO")) {
			profesor = profesorService.getProfesorByNif(usuario.getDni());
			profesor.setUsuario(usuario);
		}

		try {
			usuarioService.updateUsuario(usuario);
			if (profesor != null) {
				profesorService.updateProfesor(profesor);
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}
    
    @GetMapping("/usuarios/deactive")
    @Timed
    public  ResponseEntity<String> desactivarUsuarios() throws URISyntaxException {
        log.debug("REST request to deactive users");
        try {
        	usuarioService.desactivarUsuarios();
        }
       	catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
   
  
    /**
     * POST  /usuarios/paged : get all the usuarios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of usuarios in body
     */
    @PostMapping("/usuarios/paged")
    @Timed
    public ResponseEntity<List<Usuario>> getAllUsuariosPaged(@RequestBody Usuario partialMatch, Pageable pageable, boolean unpaged) {
        log.debug("REST request to get all Usuarios");      
       
        Page<Usuario> page = usuarioService.getAllUsuariosByCriteria(partialMatch,  PaginationUtil.generatePageableSortIgnoreCase(pageable,unpaged));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usuarios/paged");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
           
    }

	
    
    /**
     * GET  /usuarios : get all the usuarios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of usuarios in body
     */
    @GetMapping("/usuarios")
    @Timed
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        log.debug("REST request to get all Usuarios");      
        Page<Usuario> page = usuarioService.getAllUsuarios(Pageable.unpaged());
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
           
    }
    

    /**
     * GET  /usuarios/:id : get the "id" usuario.
     *
     * @param id the id of the usuario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usuario, or with status 404 (Not Found)
     */
    @GetMapping("/usuarios/{id}")
    @Timed
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        log.debug("REST request to get Usuario : {}", id);
   	 
  	  try {
              return ResponseEntity.ok(usuarioService.getUsuario(id));
          } catch (IllegalArgumentException e) {
              return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
          }   
  	
     
    }

    /**
     * DELETE  /usuarios/:id : delete the "id" usuario.
     *
     * @param id the id of the usuario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/usuarios/{id}")
    @Timed
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {
        log.debug("REST request to delete Usuario : {}", id);
        
        try {
        	Usuario usuario = usuarioService.getUsuario(id);
        	Profesor profesor = profesorService.getProfesorByUsuario(usuario);
    		if (profesor != null) {
    			profesor.setUsuario(null);
    			profesorService.updateProfesor(profesor);
    		}
        	usuarioService.deleteUsuario(id);
        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        } catch (URISyntaxException e) {
        	return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
		}
         
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST /users/:login : obtain the "login" user.
     *
     * @param user the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @PostMapping("/users/getUser")
    @Timed
    public ResponseEntity<Object> getUserPost(@RequestBody Usuario user) {
        log.debug("REST request to get User By Login and Pwd: {}", user.getLogin());      
        try {
       	 	return ResponseEntity.ok(usuarioService.getUsuarioByLogin(user));
        } catch (IllegalArgumentException e) {
        	if (e.getMessage().equals("101")) {
        		usuarioService.desactivarUsuario(user);
        		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("El usuario no está activo en Gepep" , e.getMessage())).build();
        	}
        	if (e.getMessage().equals("102")) {
        		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("El usuario no tiene centro asociado. Por favor,contacte con el Administrador" , e.getMessage())).build();
        	}
        	if (e.getMessage().equals("103")) {
        		usuarioService.desactivarUsuario(user);
        		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("El usuario no tiene tutoría en el curso académico actual. Bloqueado acceso" , e.getMessage())).build();
        	}
        	if (e.getMessage().equals("104")) {
        		usuarioService.desactivarUsuario(user);
        		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("No existe un tutor con el nif del usuario en la aplicación" , e.getMessage())).build();
        	}
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("El usuario " + user.getLogin() +" no existe en Gepep" , e.getMessage())).build();
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error interno recuperando usuario " + user.getLogin() , e.getMessage())).build();
        }
    }
}
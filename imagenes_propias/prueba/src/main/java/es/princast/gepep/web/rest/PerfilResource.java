package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.princast.gepep.domain.Perfil;
import es.princast.gepep.service.PerfilService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Perfil.
 */
@RestController
@RequestMapping("/api")
public class PerfilResource {

    private final Logger log = LoggerFactory.getLogger(PerfilResource.class);

    private static final String ENTITY_NAME = "perfil";
    @Autowired
    private  PerfilService perfilService;
    

     
    /**
     * POST  /perfils : Create a new perfil.
     *
     * @param perfil the perfil to create
     * @return the ResponseEntity with status 201 (Created) and with body the new perfil, or with status 400 (Bad Request) if the perfil has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/perfiles")
    @Timed
    public ResponseEntity<Object> createPerfil(@Valid @RequestBody Perfil perfil) throws URISyntaxException {
        log.debug("REST request to save Perfil : {}", perfil);
        try {
		    return ResponseEntity.ok(perfilService.createPerfil(perfil));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	    
    }

    /**
     * PUT  /perfiles : Updates an existing perfil.
     *
     * @param perfil the perfil to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated perfil,
     * or with status 400 (Bad Request) if the perfil is not valid,
     * or with status 500 (Internal Server Error) if the perfil couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/perfiles")
    @Timed
    public ResponseEntity<String> updatePerfil(@Valid @RequestBody Perfil perfil) throws URISyntaxException {
        log.debug("REST request to update Perfil : {}", perfil);
        try {
        	perfilService.updatePerfil(perfil);
        	 
        } catch (IllegalArgumentException e) {
      	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
	
	 return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /perfiles : get all the perfils.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of perfils in body
     */
    @GetMapping("/perfiles")
    @Timed
    public ResponseEntity<List<Perfil>> getAllPerfiles() {
        log.debug("REST request to get all Perfiles");
        Iterable<Perfil> ListaPerfiles = perfilService.getAllPerfiles();
		return ResponseEntity.ok((List<Perfil>) ListaPerfiles);
        }

    /**
     * GET  /perfiles/:id : get the "id" perfil.
     *
     * @param id the id of the perfil to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the perfil, or with status 404 (Not Found)
     */
    @GetMapping("/perfiles/{id}")
    @Timed
    public ResponseEntity<Perfil> getPerfil(@PathVariable Long id) {
        log.debug("REST request to get Perfil : {}", id);
        try {
            return ResponseEntity.ok(perfilService.getPerfil(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }  
    }

    /**
     * DELETE  /perfiles/:id : delete the "id" perfil.
     *
     * @param id the id of the perfil to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/perfiles/{id}")
    @Timed
    public ResponseEntity<String> deletePerfil(@PathVariable Long id) {
        log.debug("REST request to delete Perfil : {}", id);
        try {
        	perfilService.deletePerfil(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }   
   
}

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

import es.princast.gepep.domain.NivelEducativo;
import es.princast.gepep.service.NivelEducativoService;

/**
 * REST controller for managing NivelEducativo.
 */
@RestController
@RequestMapping("/api")
public class NivelEducativoResource {

    private final Logger log = LoggerFactory.getLogger(NivelEducativoResource.class);


    @Autowired
    private NivelEducativoService nivelEducativoService;
    
    /**
     * POST  /nivelEducativos : Create a new nivelEducativo.
     *
     * @param nivelEducativo the nivelEducativo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nivelEducativo, or with status 400 (Bad Request) if the nivelEducativo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/nivel-educativo")
    @Timed
    public ResponseEntity<Object> createNivelEducativo(@Valid @RequestBody NivelEducativo nivelEducativo) throws URISyntaxException {
    	   log.debug("REST request to create NivelEducativo");   
    	   try {
   		    return ResponseEntity.ok(nivelEducativoService.createNivelEducativo(nivelEducativo));
    	   }
   	   catch (IllegalArgumentException e) {
   		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
   	   }
    }
    /**
     * PUT  /nivelEducativos : Updates an existing nivelEducativo.
     *
     * @param nivelEducativo the nivelEducativo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nivelEducativo,
     * or with status 400 (Bad Request) if the nivelEducativo is not valid,
     * or with status 500 (Internal Server Error) if the nivelEducativo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/nivel-educativo")
    @Timed
    public ResponseEntity<String> updateNivelEducativo(@Valid @RequestBody NivelEducativo nivelEducativoModificado) throws URISyntaxException {
      try {
       nivelEducativoService.updateNivelEducativo(nivelEducativoModificado);
      }
     catch (IllegalArgumentException e) {
  	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        
    
    }
    

    /**
     * GET  /nivelEducativos : get all the nivelEducativos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of nivelEducativos in body
     */
    @GetMapping("/nivel-educativo")
    @Timed
    public ResponseEntity<List<NivelEducativo>> getAllNivelEducativos() {
        log.debug("REST request to get all Niveles");
        Iterable<NivelEducativo> listaNivelEducativos = nivelEducativoService.getAllNivelEducativos();
        return ResponseEntity.ok((List<NivelEducativo>)listaNivelEducativos);
        }

    
   

    /**
     * GET  /nivelEducativos/:id : get the "id" nivelEducativo.
     *
     * @param id the id of the nivelEducativo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nivelEducativo, or with status 404 (Not Found)
     */
    @GetMapping("/nivel-educativo/{idNivel}")
    @Timed
    public ResponseEntity<NivelEducativo> getNivelEducativo (@PathVariable Integer idNivel) {
        log.debug("REST request to get Nivel Educativo : {}", idNivel);
        try {
        	 return ResponseEntity.ok(nivelEducativoService.getNivelEducativo(idNivel));        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }    
    }

    /**
     * DELETE  /nivelEducativos/:id : delete the "id" nivelEducativo.
     *
     * @param id the id of the nivelEducativo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/nivel-educativo/{idNivel}")
    @Timed
    public ResponseEntity<String> deleteNivelEducativo(@PathVariable Integer idNivel) {
        log.debug("REST request to delete Nivel Educativo : {}", idNivel);
        try {
        	nivelEducativoService.deleteNivelEducativo(idNivel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }
    
    
   

}

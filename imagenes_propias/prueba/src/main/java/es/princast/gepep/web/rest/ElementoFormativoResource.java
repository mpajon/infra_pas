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

import es.princast.gepep.domain.ElementoFormativo;
import es.princast.gepep.service.ElementoFormativoService;


/**
 * REST controller for managing ElementoFormativo.
 */
@RestController
@RequestMapping("/api")
public class ElementoFormativoResource {

    private final Logger log = LoggerFactory.getLogger(ElementoFormativoResource.class);


    @Autowired
    private ElementoFormativoService elementoFormativoService;
    
	 
    /**
     * POST  /elementoFormativoes : Create a new elementoFormativo.
     *
     * @param elementoFormativo the elementoFormativo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new elementoFormativo, or with status 400 (Bad Request) if the elementoFormativo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ef")
    @Timed
    public ResponseEntity<Object> createElementoFormativo(@Valid @RequestBody ElementoFormativo elementoFormativo) throws URISyntaxException {
    	   log.debug("REST request to create ElementoFormativo");    
    	   try {
    		    return ResponseEntity.ok(elementoFormativoService.createElementoFormativo(elementoFormativo));
    	   }
    	   catch (IllegalArgumentException e) {
    		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
           }
        
    }
    

    
    /**
     * PUT  /elementoFormativoes : Updates an existing elementoFormativo.
     *
     * @param elementoFormativo the elementoFormativo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated elementoFormativo,
     * or with status 400 (Bad Request) if the elementoFormativo is not valid,
     * or with status 500 (Internal Server Error) if the elementoFormativo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ef")
    @Timed
    public ResponseEntity<String> updateElementoFormativo(@Valid @RequestBody ElementoFormativo elementoFormativoModificado) throws URISyntaxException {
      try {
       elementoFormativoService.updateElementoFormativo(elementoFormativoModificado);
      }
     catch (IllegalArgumentException e) {
  	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        
    
    }

    /**
     * GET  /elementoFormativoes : get all the elementoFormativos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of elementoFormativos in body
     */
    @GetMapping("/ef")
    @Timed
    public ResponseEntity<List<ElementoFormativo>> getAllElementoFormativos() {
        log.debug("REST request to get all ElementoFormativoes");
        Iterable<ElementoFormativo> listaElementoFormativos = elementoFormativoService.getAllElementoFormativos();
        return ResponseEntity.ok((List<ElementoFormativo>)listaElementoFormativos);
        }


    /**
     * GET  /elementoFormativoes/:id : get the "id" elementoFormativo.
     *
     * @param id the id of the elementoFormativo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the elementoFormativo, or with status 404 (Not Found)
     */
    @GetMapping("/ef/{idElementoFormativo}")
    @Timed
    public ResponseEntity<ElementoFormativo> getElementoFormativo (@PathVariable Long idElementoFormativo) {
        log.debug("REST request to get ElementoFormativo : {}", idElementoFormativo);
        try {
        	 return ResponseEntity.ok(elementoFormativoService.getElementoFormativo(idElementoFormativo));        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }    
    }

    /**
     * DELETE  /elementoFormativoes/:id : delete the "id" elementoFormativo.
     *
     * @param id the id of the elementoFormativo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ef/{idElementoFormativo}")
    @Timed
    public ResponseEntity<String> deleteElementoFormativo(@PathVariable Long idElementoFormativo) {
        log.debug("REST request to delete ElementoFormativo : {}", idElementoFormativo);
        try {
            elementoFormativoService.deleteElementoFormativo(idElementoFormativo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }
    
    
   

}

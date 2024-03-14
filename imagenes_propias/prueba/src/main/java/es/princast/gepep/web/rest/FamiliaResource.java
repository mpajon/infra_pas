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

import es.princast.gepep.domain.Familia;
import es.princast.gepep.service.FamiliaService;

/**
 * REST controller for managing Familia.
 */
@RestController
@RequestMapping("/api")
public class FamiliaResource {

    private final Logger log = LoggerFactory.getLogger(FamiliaResource.class);


    @Autowired
    private FamiliaService familiaService;
    
    /**
     * POST  /familias : Create a new familia.
     *
     * @param familia the familia to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familia, or with status 400 (Bad Request) if the familia has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/familias")
    @Timed
    public ResponseEntity<Object> createFamilia(@Valid @RequestBody Familia familia) throws URISyntaxException {
    	   log.debug("REST request to create Familia");   
    	   try {
   		    return ResponseEntity.ok(familiaService.createFamilia(familia));
    	   }
   	   catch (IllegalArgumentException e) {
   		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
   	   }
    }
    /**
     * PUT  /familias : Updates an existing familia.
     *
     * @param familia the familia to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familia,
     * or with status 400 (Bad Request) if the familia is not valid,
     * or with status 500 (Internal Server Error) if the familia couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/familias")
    @Timed
    public ResponseEntity<String> updateFamilia(@Valid @RequestBody Familia familiaModificado) throws URISyntaxException {
      try {
       familiaService.updateFamilia(familiaModificado);
      }
     catch (IllegalArgumentException e) {
  	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        
    
    }
    

    /**
     * GET  /familias : get all the familias.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of familias in body
     */
    @GetMapping("/familias")
    @Timed
    public ResponseEntity<List<Familia>> getAllFamilias() {
        log.debug("REST request to get all Families");
        Iterable<Familia> listaFamilias = familiaService.getAllFamilias();
        return ResponseEntity.ok((List<Familia>)listaFamilias);
        }

    
   

    /**
     * GET  /familias/:id : get the "id" familia.
     *
     * @param id the id of the familia to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familia, or with status 404 (Not Found)
     */
    @GetMapping("/familias/{idFamilia}")
    @Timed
    public ResponseEntity<Familia> getFamilia (@PathVariable Long idFamilia) {
        log.debug("REST request to get Familia : {}", idFamilia);
        try {
        	 return ResponseEntity.ok(familiaService.getFamilia(idFamilia));        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }    
    }

    /**
     * DELETE  /familias/:id : delete the "id" familia.
     *
     * @param id the id of the familia to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/familias/{idFamilia}")
    @Timed
    public ResponseEntity<String> deleteFamilia(@PathVariable Long idFamilia) {
        log.debug("REST request to delete Familia : {}", idFamilia);
        try {
            familiaService.deleteFamilia(idFamilia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }
    
    
   

}

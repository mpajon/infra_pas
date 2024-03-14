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

import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.service.EnsenanzaService;
import es.princast.gepep.web.rest.util.HeaderUtil;
 

/**
 * REST controller for managing Ensenanza.
 */
@RestController
@RequestMapping("/api")
public class EnsenanzaResource {

    private final Logger log = LoggerFactory.getLogger(EnsenanzaResource.class);

    private static final String ENTITY_NAME = "ensenanza";

    
    @Autowired
    private EnsenanzaService ensenanzaService;
   
    /**
     * POST  /ensenanzas : Create a new ensenanza.
     *
     * @param ensenanza the ensenanza to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ensenanza, or with status 400 (Bad Request) if the ensenanza has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ensenanzas")
    @Timed
    public ResponseEntity<Object> createEnsenanza(@Valid @RequestBody Ensenanza ensenanza) throws URISyntaxException {
        log.debug("REST request to save Ensenanza : {}", ensenanza);
        try {
		    return ResponseEntity.ok(ensenanzaService.createEnsenanza(ensenanza));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } 
    }
	
    
    /**
     * PUT  /ensenanzas : Updates an existing ensenanza.
     *
     * @param ensenanza the ensenanza to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ensenanza,
     * or with status 400 (Bad Request) if the ensenanza is not valid,
     * or with status 500 (Internal Server Error) if the ensenanza couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ensenanzas")
    @Timed
    public ResponseEntity<String> updateEnsenanza(@Valid @RequestBody Ensenanza ensenanza) throws URISyntaxException {
    	 try {
             ensenanzaService.updateEnsenanza(ensenanza);
         } catch (IllegalArgumentException e) {
       	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
         }
         return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /ensenanzas : get all the ensenanzas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ensenanzas in body
     */
    @GetMapping("/ensenanzas")
    @Timed
    public ResponseEntity<List<Ensenanza>> getAllEnsenanzas() {
        log.debug("REST request to get all Ensenanzas");
        Iterable<Ensenanza> listaEnsenanzas = ensenanzaService.getAllEnsenanzas();
        return ResponseEntity.ok((List<Ensenanza>)listaEnsenanzas);
                
        }
    
    /**
     * GET  /ensenanzas : get all the ensenanzas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ensenanzas in body
     */
    @GetMapping("/ensenanzas/activas")
    @Timed
    public ResponseEntity<List<Ensenanza>> getAllEnsenanzasActivas() {
        log.debug("REST request to get all Ensenanzas");
        Iterable<Ensenanza> listaEnsenanzas = ensenanzaService.getAllEnsenanzasActivas();
        return ResponseEntity.ok((List<Ensenanza>)listaEnsenanzas);
                
        }
    
    
    

    /**
     * GET  /ensenanzas/:id : get the "id" ensenanza.
     *
     * @param id the id of the ensenanza to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ensenanza, or with status 404 (Not Found)
     */
    @GetMapping("/ensenanzas/{idEnsenanza}")
    @Timed
    public ResponseEntity<Ensenanza> getEnsenanza(@PathVariable String idEnsenanza) {
        log.debug("REST request to get Ensenanza : {}", idEnsenanza);
        try {
            return ResponseEntity.ok(ensenanzaService.getEnsenanza(idEnsenanza));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }
    
   
    /**
     * DELETE  /ensenanzas/:id : delete the "id" ensenanza.
     *
     * @param id the id of the ensenanza to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ensenanzas/{idEnsenanza}")
    @Timed
    public ResponseEntity<String> deleteEnsenanza(@PathVariable String idEnsenanza) {
        log.debug("REST request to delete Ensenanza : {}", idEnsenanza);
        try {
        	ensenanzaService.deleteEnsenanza(idEnsenanza);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idEnsenanza.toString())).build();
    }

 

}

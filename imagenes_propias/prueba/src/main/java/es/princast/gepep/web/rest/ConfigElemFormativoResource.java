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

import es.princast.gepep.domain.ConfigElemFormativo;
import es.princast.gepep.service.ConfigElemFormativoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ConfigElemFormativo.
 */
@RestController
@RequestMapping("/api")
public class ConfigElemFormativoResource {

    private final Logger log = LoggerFactory.getLogger(ConfigElemFormativoResource.class);

    private static final String ENTITY_NAME = "configElemFormativo";
    
    @Autowired
    private ConfigElemFormativoService configElemFormativoService;

    

    /**
     * POST  /configElemFormativoes : Create a new configElemFormativo.
     *
     * @param configElemFormativo the configElemFormativo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configElemFormativo, or with status 400 (Bad Request) if the configElemFormativo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/configEF")
    @Timed
    public ResponseEntity<Object> createConfigElemFormativo(@Valid @RequestBody ConfigElemFormativo configElemFormativo) throws URISyntaxException {
    	log.debug("REST request to save ConfigElemFormativo : {}", configElemFormativo);
    	
    	try {
		    return ResponseEntity.ok(configElemFormativoService.createConfigElemFormativo(configElemFormativo));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
    }

    /**
     * PUT  /configElemFormativos : Updates an existing configElemFormativo.
     *
     * @param configElemFormativo the configElemFormativo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configElemFormativo,
     * or with status 400 (Bad Request) if the configElemFormativo is not valid,
     * or with status 500 (Internal Server Error) if the configElemFormativo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/configEF")
    @Timed
    public ResponseEntity<String> updateConfigElemFormativo(@Valid @RequestBody ConfigElemFormativo configElemFormativo) throws URISyntaxException {
    	log.debug("REST request to update ConfigElemFormativo : {}", configElemFormativo);
    	  try {
              configElemFormativoService.updateConfigElemFormativo(configElemFormativo);
          } catch (IllegalArgumentException e) {
        	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
          }
          return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /configElemFormativoes : get all the configElemFormativoes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of configElemFormativoes in body
     */
    @GetMapping("/configEF")
    @Timed
    public ResponseEntity<List<ConfigElemFormativo>> getAllConfigElemFormativos() {    	
        log.debug("REST request to get all ConfigElemFormativoes");
        Iterable<ConfigElemFormativo> listaConfigElemFormativoes = configElemFormativoService.getAllConfigElemFormativos();
        return ResponseEntity.ok((List<ConfigElemFormativo>)listaConfigElemFormativoes);
        }

    
    /**
     * GET  /configElemFormativoes/:id : get the "id" configElemFormativo.
     *
     * @param id the id of the configElemFormativo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configElemFormativo, or with status 404 (Not Found)
     */
    @GetMapping("/configEF/{idConfigElemFormativo}")
    @Timed
    public ResponseEntity<ConfigElemFormativo> getConfigElemFormativo(@PathVariable Long idConfigElemFormativo) {
        log.debug("REST request to get ConfigElemFormativo : {}", idConfigElemFormativo);     
       
        try {
            return ResponseEntity.ok(configElemFormativoService.getConfigElemFormativo(idConfigElemFormativo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    /**
     * DELETE  /configElemFormativoes/:id : delete the "id" configElemFormativo.
     *
     * @param id the id of the configElemFormativo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/configEF/{idConfigElemFormativo}")
    @Timed
    public ResponseEntity<String> deleteConfigElemFormativo(@PathVariable Long idConfigElemFormativo) {
        log.debug("REST request to delete ConfigElemFormativo : {}", idConfigElemFormativo);
      try {
            configElemFormativoService.deleteConfigElemFormativo(idConfigElemFormativo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
      return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idConfigElemFormativo.toString())).build();
    }
 
    
    /**
     * GET  /configEF/:idConfigElemFormativo/configElemFormativoes-sector : obtiene configuraciones por id de EF
     *
     * @param id the id of the sector of activities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body, or with status 404 (Not Found)
     */
/*   @GetMapping("/configEF/{idTipoPractica}/{idConfigElemFormativo}")
    @Timed
	public ResponseEntity<List<ConfigElemFormativo>> getConfigElemFormativoByTipoPracticaAndEF(@PathVariable (value="idTipoPractica") Long idTipoPractica,@PathVariable (value="idElementoFormativo") Long idElementoFormativo) {
		log.debug("REST request to getConfigElemFormativoByTipoPracticaAndEF");
		List<ConfigElemFormativo> listaConfigElemFormativoes = configElemFormativoService.getConfigElemFormativoByTipoPracticaAndElementoFormativo(idTipoPractica, idElementoFormativo);
		return ResponseEntity.ok((List<ConfigElemFormativo>) listaConfigElemFormativoes);
	}*/
    
    /**
     * GET  /configEF/:idConfigElemFormativo/configElemFormativoes-sector : obtiene configuraciones por id de EF
     *
     * @param id the id of the sector of activities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body, or with status 404 (Not Found)
     */
    /*@GetMapping("/configEF/{idTipoPractica}/tp")
    @Timed
	public ResponseEntity<List<ConfigElemFormativo>> getConfigElemFormativoByTipoPractica(@PathVariable (value="idTipoPractica") Long idTipoPractica) {
		log.debug("REST request to getConfigElemFormativoByTipoPractica");
		Iterable<ConfigElemFormativo> listaConfigElemFormativoes = configElemFormativoService.getConfigElemFormativoByTipoPractica(idTipoPractica);
		return ResponseEntity.ok((List<ConfigElemFormativo>) listaConfigElemFormativoes);
	}*/
    
    
    /**
     * GET  /configEF/:idConfigElemFormativo/configElemFormativoes-sector : obtiene configuraciones por id de EF
     *
     * @param id the id of the sector of activities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body, or with status 404 (Not Found)
     */
    @GetMapping("/configEF/{idConfigElemFormativo}/ef")
    @Timed
	public ResponseEntity<List<ConfigElemFormativo>> getConfigElemFormativoByElementoFormativo(@PathVariable (value="idElementoFormativo") Long idElementoFormativo) {
		log.debug("REST request to getConfigElemFormativoByElementoFormativo");
		Iterable<ConfigElemFormativo> listaConfigElemFormativoes = configElemFormativoService.getConfigElemFormativoByElementoFormativo(idElementoFormativo);
		return ResponseEntity.ok((List<ConfigElemFormativo>) listaConfigElemFormativoes);
	}
    
}

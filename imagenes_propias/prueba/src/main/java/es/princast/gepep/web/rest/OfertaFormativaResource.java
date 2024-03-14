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

import es.princast.gepep.domain.OfertaEducativa;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.service.OfertaFormativaService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing OfertaFormativa.
 */
@RestController
@RequestMapping("/api")
public class OfertaFormativaResource {

    private final Logger log = LoggerFactory.getLogger(OfertaFormativaResource.class);

    private static final String ENTITY_NAME = "ofertaFormativa";

    @Autowired
    private OfertaFormativaService ofertaFormativaService;

   

    /**
     * POST  /oferta-formativas : Create a new ofertaFormativa.
     *
     * @param ofertaFormativa the ofertaFormativa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ofertaFormativa, or with status 400 (Bad Request) if the ofertaFormativa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ofertas-formativas")
    @Timed
    public ResponseEntity<Object> createOfertaFormativa(@Valid @RequestBody OfertaFormativa ofertaFormativa) throws URISyntaxException {
        log.debug("REST request to save OfertaFormativa : {}", ofertaFormativa);
        try {
		    return ResponseEntity.ok(ofertaFormativaService.createOfertaFormativa(ofertaFormativa));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } 
    }

    
    @PostMapping("/ofertas-formativas/noSauce")
    @Timed
    public ResponseEntity<OfertaFormativa> createOfertaFormativaNoSauce(@Valid @RequestBody OfertaFormativa ofertaFormativa) throws URISyntaxException {
        log.debug("REST request to save OfertaFormativa : {}", ofertaFormativa);
       return ofertaFormativaService.createOfertaFormativaNoSauce(ofertaFormativa);
    }
    
    /**
     * PUT  /oferta-formativas : Updates an existing ofertaFormativa.
     *
     * @param ofertaFormativa the ofertaFormativa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ofertaFormativa,
     * or with status 400 (Bad Request) if the ofertaFormativa is not valid,
     * or with status 500 (Internal Server Error) if the ofertaFormativa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ofertas-formativas")
    @Timed
    public ResponseEntity<String> updateOfertaFormativa(@Valid @RequestBody OfertaFormativa ofertaFormativa) throws URISyntaxException {
        log.debug("REST request to update OfertaFormativa : {}", ofertaFormativa);
        
        try {
        		ofertaFormativaService.updateOfertaFormativa(ofertaFormativa);
               } catch (IllegalArgumentException e) {
             	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
           }
       	
       	 return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
       

    /**
     * GET  /ofertas-formativas : get all the ofertaFormativas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ofertaFormativas in body
     */
    @GetMapping("/ofertas-formativas")
    @Timed
    public ResponseEntity<List<OfertaFormativa>> getAllOfertaFormativas() {
        log.debug("REST request to get all OfertaFormativas");
        Iterable<OfertaFormativa> listaOfertasFormativa= ofertaFormativaService.getAllOfertaFormativas();
        return ResponseEntity.ok((List<OfertaFormativa>)listaOfertasFormativa);
        
   
        
        }
   
    
    @GetMapping("/ofertas-formativas/ciclo/{id}")
    @Timed
    public ResponseEntity<List<OfertaFormativa>> getAllOfertaFormativas(@PathVariable String id) {
        log.debug("REST request to get all OfertaFormativas");
        Iterable<OfertaFormativa> listaOfertasFormativa= ofertaFormativaService.getAllOfertaFormativasByCiclo(id);
        return ResponseEntity.ok((List<OfertaFormativa>)listaOfertasFormativa);           
        }

    /**
     * GET  /ofertas-formativas/:id : get the "id" ofertaFormativa.
     *
     * @param id the id of the ofertaFormativa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ofertaFormativa, or with status 404 (Not Found)
     */
    @GetMapping("/ofertas-formativas/{id}")
    @Timed
    public ResponseEntity<OfertaFormativa> getOfertaFormativa(@PathVariable String id) {
        log.debug("REST request to get OfertaFormativa : {}", id);
  	  try {
          return ResponseEntity.ok(ofertaFormativaService.getOfertaFormativa(id));
      } catch (IllegalArgumentException e) {
          return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
      }      
    }
    
    @GetMapping("/ofertas-formativas/list/{listaIds}")
    @Timed
    public  ResponseEntity<List<OfertaFormativa>> getOfertasFormativas(@PathVariable String listaIds) {
        log.debug("REST request to get OfertaFormativa : {}");
        
        List<OfertaFormativa> listaOfertasFormativa= ofertaFormativaService.getOfertasByLista(listaIds);
        return ResponseEntity.ok(listaOfertasFormativa);       
    }
    
    @GetMapping("/ofertas-formativas/{centro}/{anio}")
    @Timed
    public ResponseEntity<List<OfertaEducativa>>getOfertasByCentroAndAnio(@PathVariable String centro,@PathVariable Integer anio) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");
        
        Iterable<OfertaEducativa> listaOfertas= ofertaFormativaService.getOfertasByCentroAndAnio(centro,anio);
        return ResponseEntity.ok((List<OfertaEducativa>)listaOfertas);
        } 
    
    /**
     * DELETE  /ofertas-formativas/:id : delete the "id" ofertaFormativa.
     *
     * @param id the id of the ofertaFormativa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ofertas-formativas/{id}")
    @Timed
    public ResponseEntity<String> deleteOfertaFormativa(@PathVariable String id) {
        log.debug("REST request to delete OfertaFormativa : {}", id);
        try {
        	ofertaFormativaService.deleteOfertaFormativa(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }       
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    

    
}

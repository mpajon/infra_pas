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

import es.princast.gepep.domain.PeriodoLiquidacion;
import es.princast.gepep.service.PeriodoLiquidacionService;
import es.princast.gepep.web.rest.util.HeaderUtil;


/**
 * REST controller for managing PeriodoPractica.
 */
@RestController
@RequestMapping("/api")
public class PeriodoLiquidacionResource {

    private final Logger log = LoggerFactory.getLogger(PeriodoLiquidacionResource.class);

    private static final String ENTITY_NAME = "periodoLiquidacion";

    @Autowired
    private PeriodoLiquidacionService periodoLiquidacionService;
    
    
  
    /**
     * POST  /periodo-practicas : Create a new periodoPractica.
     *
     * @param periodoPractica the periodoPractica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periodoPractica, or with status 400 (Bad Request) if the periodoPractica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
                                                                  
    
    @PostMapping("/periodos-liquidacion/create")
    @Timed
    public ResponseEntity<Object> createPeriodoLiquidacion(@Valid @RequestBody PeriodoLiquidacion periodo) throws URISyntaxException {
        log.debug("SERVICE request to save periodoPractica : {}", periodo);     
        try {
		    return ResponseEntity.ok(periodoLiquidacionService.createPeriodoLiquidacion(periodo));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }  
    }
    
       
    @PutMapping("/periodos-liquidacion")
    @Timed
    public ResponseEntity<String> updatePeriodoLiquidacion(@Valid @RequestBody  PeriodoLiquidacion periodo) throws URISyntaxException {
     
    	periodoLiquidacionService.updatePeriodoLiquidacion(periodo);         
         return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
            
       
    /**
     * GET  /periodos-practicas/:id : get the "id" periodoPractica.
     *
     * @param id the id of the periodoPractica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodoPractica, or with status 404 (Not Found)
     */
    @GetMapping("/periodos-liquidacion/{idPeriodoLiquidacion}")
    @Timed
    public ResponseEntity<PeriodoLiquidacion> getPeriodoLiquidacion(@PathVariable Integer idPeriodoLiquidacion) {
        log.debug("REST request to get PeriodoPractica : {}", idPeriodoLiquidacion);
        try {
            return ResponseEntity.ok(this.periodoLiquidacionService.getperiodoLiquidacion(idPeriodoLiquidacion));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }
    
    @GetMapping("/periodos-liquidacion/tipo-practica/{idTipoPractica}")
    @Timed
    public ResponseEntity<List<PeriodoLiquidacion>>getAllPeriodosLiquidacion(@PathVariable (value="idTipoPractica") Long idTipoPractica) {
        log.debug("REST request to get PeriodoPractica : {}", idTipoPractica);
        Iterable<PeriodoLiquidacion> listaPeriodosLiquidacion = periodoLiquidacionService.getAllPeriodosLiquidacionByTipoPractica(idTipoPractica);
        return ResponseEntity.ok((List<PeriodoLiquidacion>)listaPeriodosLiquidacion);
    }
    
    @GetMapping("/periodos-liquidacion/tipo-practica/{idTipoPractica}/{idPeriodoPractica}/{idAnio}")
    @Timed
    public ResponseEntity<Object>getPeriodoLiquidacionByPeriodoPractica(@PathVariable (value="idTipoPractica") Long idTipoPractica, @PathVariable (value="idPeriodoPractica") Long idPeriodoPractica, @PathVariable (value="idAnio") Integer idAnio) {
        log.debug("REST request to get getPeriodoLiquidacionByPeriodoPractica : {}", idTipoPractica, idPeriodoPractica);
        PeriodoLiquidacion periodoLiquidacion =null;
        try {
        	periodoLiquidacion = periodoLiquidacionService.getPeriodoLiquidacionByPeriodoPractica(idTipoPractica, idPeriodoPractica, idAnio);
        }catch (IllegalArgumentException e) {
 		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }  
        return ResponseEntity.ok(periodoLiquidacion);
    }
    
    @GetMapping("/periodos-liquidacion")
    @Timed
    public ResponseEntity<List<PeriodoLiquidacion>> getAllPeriodosLiquidacion() {
        log.debug("REST request to get all Ensenanzas");
        Iterable<PeriodoLiquidacion> listaPeriodosLiquidacion = periodoLiquidacionService.getAllperiodoLiquidacion();
        return ResponseEntity.ok((List<PeriodoLiquidacion>)listaPeriodosLiquidacion);
                
        }
        

    /**
     * DELETE  /periodos-practicas/:id : delete the "id" periodoPractica.
     *
     * @param id the id of the periodoPractica to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periodos-liquidacion/{idPeriodoLiquidacion}")
    @Timed
    public ResponseEntity<String> deletePeriodoLiquidacion(@PathVariable Integer idPeriodoLiquidacion) {
        log.debug("REST request to delete PeriodoPractica : {}", idPeriodoLiquidacion);
        try {
        	periodoLiquidacionService.deletePeriodoLiquidacion(idPeriodoLiquidacion);
        } catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idPeriodoLiquidacion.toString())).build();
        
                
        
    }
    
 
    
}

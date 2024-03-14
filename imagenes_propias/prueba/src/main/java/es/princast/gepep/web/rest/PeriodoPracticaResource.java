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

import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.service.CursoAcademicoService;
import es.princast.gepep.service.PeriodoPracticaService;
import es.princast.gepep.web.rest.util.HeaderUtil;


/**
 * REST controller for managing PeriodoPractica.
 */
@RestController
@RequestMapping("/api")
public class PeriodoPracticaResource {

    private final Logger log = LoggerFactory.getLogger(PeriodoPracticaResource.class);

    private static final String ENTITY_NAME = "periodoPractica";

    @Autowired
    private PeriodoPracticaService periodoPracticaService;
        
    @Autowired
    private  CursoAcademicoService cursoAcademicoService;
    
  
    /**
     * POST  /periodo-practicas : Create a new periodoPractica.
     *
     * @param periodoPractica the periodoPractica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periodoPractica, or with status 400 (Bad Request) if the periodoPractica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
                                                                  
    
    @PostMapping("/periodos-practica/create")
    @Timed
    public ResponseEntity<Object> createPeriodoPractica(@Valid @RequestBody PeriodoPractica periodoPractica) throws URISyntaxException {
        log.debug("SERVICE request to save periodoPractica : {}", periodoPractica);     
        try {
		    return ResponseEntity.ok(this.periodoPracticaService.createperiodoPractica(periodoPractica));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }    
    }

       
    @PutMapping("/periodos-practica")
    @Timed
    public ResponseEntity<String> updatePeriodoPractica(@Valid @RequestBody  PeriodoPractica periodoPractica) throws URISyntaxException {
     
    	 try {
 		   periodoPracticaService.updateperiodoPractica(periodoPractica); 	
 		   return ResponseEntity.status(HttpStatus.ACCEPTED).build();
 	   }
 	   catch (IllegalArgumentException e) {
 		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }  
    	 
    	 
    	 
    //	periodoPracticaService.updateperiodoPractica(periodoPractica);         
    //    
    }
            
       
    /**
     * GET  /periodos-practicas/:id : get the "id" periodoPractica.
     *
     * @param id the id of the periodoPractica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodoPractica, or with status 404 (Not Found)
     */
    @GetMapping("/periodos-practica/{idPeriodo}")
    @Timed
    public ResponseEntity<PeriodoPractica> getPeriodoPractica(@PathVariable Long idPeriodo) {
        log.debug("REST request to get PeriodoPractica : {}", idPeriodo);
        try {
            return ResponseEntity.ok(this.periodoPracticaService.getperiodoPractica(idPeriodo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }
        

    /**
     * DELETE  /periodos-practicas/:id : delete the "id" periodoPractica.
     *
     * @param id the id of the periodoPractica to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periodos-practica/{idPeriodo}")
    @Timed
    public ResponseEntity<String> deletePeriodoPractica(@PathVariable Long idPeriodo) {
        log.debug("REST request to delete PeriodoPractica : {}", idPeriodo);
        try {
        periodoPracticaService.deleteperiodoPractica(idPeriodo);
        } catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idPeriodo.toString())).build();
        
                
        
    }
    
    /**
     * GET  /periodos-practicas : get all the periodoPracticas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of periodoPracticas in body
     

    @GetMapping("/periodos-practica")
    @Timed
    public ResponseEntity<List<PeriodoPractica>> getAllPeriodoPracticas() {
        log.debug("REST request to get all Ensenanzas");        
        Iterable<PeriodoPractica> listaPeriodos = this.periodoPracticaService.getAllperiodoPracticas();
        return ResponseEntity.ok((List<PeriodoPractica>)listaPeriodos);
                
        }
    */
    /**
     * GET  /periodos-practicas : get all the periodoPracticas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of periodoPracticas in body
     */

    @PostMapping("/periodos-practica")
    @Timed
    public ResponseEntity<List<PeriodoPractica>> getAllPeriodoPracticasBuscador( @RequestBody PeriodoPractica periodoPractica) {
        return ResponseEntity.ok(periodoPracticaService.getAllPeriodosByCriteria(periodoPractica));
        }

    /**
     * POST  /periodos-practicas : Create a new periodoPractica.
     *
     * @param periodoPractica the periodoPractica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periodoPractica, or with status 400 (Bad Request) if the periodoPractica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipos-practica/{idTipoPractica}/periodos-practicas")
    @Timed
    public  ResponseEntity<PeriodoPractica> createPeriodoPractica(@PathVariable (value="idTipoPractica") Long idTipoPractica, @Valid @RequestBody PeriodoPractica periodoPractica) throws URISyntaxException {
        log.debug("REST request to save PeriodoPractica : {}", periodoPractica);
        return periodoPracticaService.createPeriodoPracticaInTipoPractica(idTipoPractica,periodoPractica);
    }

    
    @PutMapping("/tipos-practica/{idTipoPractica}/periodos-practicas/{idPeriodo}")
    @Timed
    public ResponseEntity<PeriodoPractica> updatePeriodoPractica(@PathVariable (value="idTipoPractica") Long idTipoPractica, @PathVariable (value = "idPeriodo") Long idPeriodoPractica, @Valid @RequestBody PeriodoPractica periodoPractica) throws URISyntaxException {
        log.debug("REST request to update PeriodoPractica : {}", periodoPractica);      
        return this.periodoPracticaService.updatePeriodoPracticaInTipoPractica(idTipoPractica, idPeriodoPractica,periodoPractica);
    }
        
    
    /**
     * GET  /periodos-practicas/:id : get the "id" periodoPractica.
     * @param id the id of the periodoPractica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodoPractica, or with status 404 (Not Found)
     */
    @GetMapping("/tipos-practica/{idTipoPractica}/periodos-practica")
    @Timed
    public Iterable<PeriodoPractica> getAllPeriodos(@PathVariable (value="idTipoPractica") Long idTipoPractica) {
        log.debug("REST request to get PeriodoPractica : {}", idTipoPractica);
        return  periodoPracticaService.getAllPeriodosPracticaByTipoPractica(idTipoPractica);
    }

    @GetMapping("/tipos-practica/{idTipoPractica}/periodos-practica/{curso}")
    @Timed
    public Iterable<PeriodoPractica> getAllPeriodosByCursoAcademico(@PathVariable (value="idTipoPractica") Long idTipoPractica,@PathVariable (value="curso") Integer curso) {
        log.debug("REST request to get PeriodoPractica : {}", idTipoPractica);
        
        CursoAcademico cursoAca = this.cursoAcademicoService.getCursoAcademico(curso);
        
        return  periodoPracticaService.getAllPeriodosPracticaByTipoPracticaAndCursoAcademico(idTipoPractica, cursoAca);
    }
    
    /**
     * DELETE  /periodos-practicas/:id : delete the "id" periodoPractica.
     *
     * @param id the id of the periodoPractica to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipos-practica/{idTipoPractica}/periodos-practica/{idPeriodo}")
    @Timed
    public ResponseEntity<Void> deleteperiodoPracticaByTipoPractica(@PathVariable (value="idTipoPractica") Long idTipoPractica,  @PathVariable (value = "idPeriodo") Long idPeriodo) {
        log.debug("REST request to delete PeriodoPractica : {}", idTipoPractica);
        
        periodoPracticaService.deleteperiodoPracticaByTipoPractica(idTipoPractica, idPeriodo);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idPeriodo.toString())).build();
    }
    
}

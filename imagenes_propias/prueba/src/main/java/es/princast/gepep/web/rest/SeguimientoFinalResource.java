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

import es.princast.gepep.domain.ControlSeguimiento;
import es.princast.gepep.domain.SeguimientoFinal;
import es.princast.gepep.service.SeguimientoFinalService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing SeguimientoFinal.
 */
@RestController
@RequestMapping("/api")
public class SeguimientoFinalResource {

    private final Logger log = LoggerFactory.getLogger(SeguimientoFinalResource.class);

    private static final String ENTITY_NAME = "seguimientoFinal";

    @Autowired
    private SeguimientoFinalService seguimientoFinalService;

    /**
     * POST  /seguimientos-finales : Create a new seguimientoFinal.
     *
     * @param seguimientoFinal the seguimientoFinal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new seguimientoFinal, or with status 400 (Bad Request) if the seguimientoFinal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/seguimiento-final")
    @Timed
    public ResponseEntity<Object> createSeguimientoFinal(@Valid @RequestBody SeguimientoFinal seguimientoFinal) throws URISyntaxException {
        log.debug("REST request to save SeguimientoFinal : {}", seguimientoFinal);
        
        try {
		    return ResponseEntity.ok(seguimientoFinalService.createSeguimientoFinal(seguimientoFinal));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
    }

    /**
     * PUT  /seguimientos-finales : Updates an existing seguimientoFinal.
     *
     * @param seguimientoFinal the seguimientoFinal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated seguimientoFinal,
     * or with status 400 (Bad Request) if the seguimientoFinal is not valid,
     * or with status 500 (Internal Server Error) if the seguimientoFinal couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/seguimiento-final")
    @Timed
    public ResponseEntity<String> updateSeguimientoFinal(@Valid @RequestBody SeguimientoFinal seguimientoFinal) throws URISyntaxException {
        log.debug("REST request to update SeguimientoFinal : {}", seguimientoFinal);
       try {
    	   seguimientoFinalService.updateSeguimientoFinal(seguimientoFinal);
        	 
        } catch (IllegalArgumentException e) {
      	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
	
	 return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /seguimientos-finales : get all the seguimientoFinals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of seguimientoFinals in body
     */
    @GetMapping("/seguimiento-final")
    @Timed
    public ResponseEntity<List<SeguimientoFinal>> getAllSeguimientoFinales() {
        log.debug("REST request to get all SeguimientoFinales");
        Iterable<SeguimientoFinal> listaSeguimientos = seguimientoFinalService.getAllSeguimientoFinales();
		return ResponseEntity.ok((List<SeguimientoFinal>) listaSeguimientos);
        
        }

    /**
     * GET  /seguimientos-finales/:id : get the "id" seguimientoFinal.
     *
     * @param id the id of the seguimientoFinal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the seguimientoFinal, or with status 404 (Not Found)
     */
    @GetMapping("/seguimiento-final/{id}")
    @Timed
    public ResponseEntity<SeguimientoFinal> getSeguimientoFinal(@PathVariable Long id) {
        log.debug("REST request to get SeguimientoFinal : {}", id);
        try {
            return ResponseEntity.ok(seguimientoFinalService.getSeguimientoFinal(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }  
    }

    @GetMapping("/seguimiento-final/matricula/{idMatricula}")
    @Timed
    public ResponseEntity<SeguimientoFinal> getSeguimientoFinalByMatricula(@PathVariable String idMatricula) {
        log.debug("REST request to get SeguimientoFinal : {}", idMatricula);
        try {
            return ResponseEntity.ok(seguimientoFinalService.getSeguimientoFinalByMatricula(idMatricula));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }  
    }
    /**
     * DELETE  /seguimientos-finales/:id : delete the "id" seguimientoFinal.
     *
     * @param id the id of the seguimientoFinal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/seguimiento-final/{id}")
    @Timed
    public ResponseEntity<String> deleteSeguimientoFinal(@PathVariable Long id) {
        log.debug("REST request to delete SeguimientoFinal : {}", id);
        try {
        	seguimientoFinalService.deleteSeguimientoFinal(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    
    /**
     * GET getSeguimientoFinalByCursoAndCentroAndTutor/{anio}/{idCentro}
     * 
     */
	@GetMapping("/seguimiento-final/getSeguimientoFinalByCursoAndCentroAndTutor/{anio}/{idCentro}/{tutor}")
	@Timed
	public ResponseEntity<List<SeguimientoFinal>> getSeguimientoFinalByCursoAndCentroAndTutor(@PathVariable Integer anio, @PathVariable String idCentro, @PathVariable String tutor){
		log.debug("REST request getContadoresControlSeguimiento");
		List<SeguimientoFinal> listaSeguimientoFinal = seguimientoFinalService.getSeguimientoFinalByCursoAndCentroAndTutor(idCentro, anio, tutor);
		return ResponseEntity.ok((List<SeguimientoFinal>) listaSeguimientoFinal);
	}
    
    
    /**
     * GET getResumenControlSeguimiento/{anio}/{idCentro}
     * 
     */
	@GetMapping("/seguimiento-final/getResumenControlSeguimiento/{anio}/{idCentro}/{tutor}")
	@Timed
	public ResponseEntity<List<ControlSeguimiento>> getResumenControlSeguimiento(@PathVariable Integer anio, @PathVariable String idCentro, @PathVariable String tutor){
		log.debug("REST request getResumenControlSeguimiento");
		List<ControlSeguimiento> resumenSeguimiento = seguimientoFinalService.getResumenControlSeguimiento(idCentro, anio, tutor);
		return ResponseEntity.ok((List<ControlSeguimiento>) resumenSeguimiento);
	}
	
	  /**
     * GET getContadoresControlSeguimiento/{anio}/{idCentro}
     * 
     */
	@GetMapping("/seguimiento-final/getContadoresControlSeguimiento/{anio}/{idCentro}/{tutor}")
	@Timed
	public ResponseEntity<List<ControlSeguimiento>> getContadoresControlSeguimiento(@PathVariable Integer anio, @PathVariable String idCentro, @PathVariable String tutor){
		log.debug("REST request getContadoresControlSeguimiento");
		List<ControlSeguimiento> resumenSeguimiento = seguimientoFinalService.getContadoresControlSeguimiento(idCentro, anio, tutor);
		return ResponseEntity.ok((List<ControlSeguimiento>) resumenSeguimiento);
	}
    
}

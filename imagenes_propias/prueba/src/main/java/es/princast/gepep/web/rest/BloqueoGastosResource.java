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

import es.princast.gepep.domain.BloqueoGastos;
import es.princast.gepep.domain.ControlBloqueoGastos;
import es.princast.gepep.service.BloqueoGastosService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Actividad.
 */
@RestController
@RequestMapping("/api")
public class BloqueoGastosResource {

    private final Logger log = LoggerFactory.getLogger(BloqueoGastosResource.class);

    private static final String ENTITY_NAME = "bloqueo_gastos";
    
    @Autowired
    private BloqueoGastosService bloqueoGastosService;

    

    /**
     * POST  /bloqueos : Create a new bloqueo.
     *
     * @param bloqueo the bloqueo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bloqueo, or with status 400 (Bad Request) if the bloqueo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bloqueos")
    @Timed
    public ResponseEntity<Object> createBloqueoGastos(@Valid @RequestBody BloqueoGastos bloqueoGastos) throws URISyntaxException {
    	log.debug("REST request to save Actividad : {}", bloqueoGastos);
    	
    	try {
		    return ResponseEntity.ok(bloqueoGastosService.createBloqueoGastos(bloqueoGastos));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }  
    }

    /**
     * PUT  /bloqueos : Updates an existing bloqueo.
     *
     * @param bloqueo the bloqueo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bloqueo,
     * or with status 400 (Bad Request) if the bloqueo is not valid,
     * or with status 500 (Internal Server Error) if the bloqueo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bloqueos")
    @Timed
    public ResponseEntity<String> updateBloqueoGastos(@Valid @RequestBody BloqueoGastos bloqueoGastos) throws URISyntaxException {
    	log.debug("REST request to update bloqueoGastos : {}", bloqueoGastos);
    	  try {
    		  bloqueoGastosService.updateBloqueoGastos(bloqueoGastos);
          } catch (IllegalArgumentException e) {
        	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
          }
          return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    
    /**
     * 
     * @param bloqueoGastos
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/bloqueos/bloquear")
    @Timed
    public ResponseEntity<String> bloquearGastos(@Valid @RequestBody ControlBloqueoGastos bloqueoGastos) throws URISyntaxException {
    	log.debug("REST request to update bloqueoGastos : {}", bloqueoGastos);
    	  try {
    		  bloqueoGastosService.bloquearGastos(bloqueoGastos);
          } catch (Exception e) {
        	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Error al bloquear gastos");
          }
          return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    /**
     * GET  /bloqueos : get all the bloqueos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bloqueos in body
     */
    @GetMapping("/bloqueos")
    @Timed
    public ResponseEntity<List<BloqueoGastos>> getAllBloqueoGastos() {    	
        log.debug("REST request to get all BloqueoGastos");
        Iterable<BloqueoGastos> listaBloqueos = bloqueoGastosService.getAllBloqueoGastos();
        return ResponseEntity.ok((List<BloqueoGastos>)listaBloqueos);
        }

    
    /**
     * GET  /bloqueos/:id : get the "id" bloqueo.
     *
     * @param id the id of the bloqueo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bloqueo, or with status 404 (Not Found)
     */
    @GetMapping("/bloqueos/{idbloqueo}")
    @Timed
    public ResponseEntity<BloqueoGastos> getBloqueoGastos(@PathVariable Long idBloqueo) {
        log.debug("REST request to get Actividad : {}", idBloqueo);     
       
        try {
            return ResponseEntity.ok(bloqueoGastosService.getBloqueoGastos(idBloqueo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    /**
     * DELETE  /bloqueos/:id : delete the "id" bloqueo.
     *
     * @param id the id of the bloqueo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bloqueos/{idBloqueo}")
    @Timed
    public ResponseEntity<String> bloqueoGastos(@PathVariable Long idBloqueo) {
        log.debug("REST request to delete Bloqueo : {}", idBloqueo);
      try {
    	  bloqueoGastosService.deleteBloqueoGastos(idBloqueo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
      return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idBloqueo.toString())).build();
    }


    
    @GetMapping("/bloqueos/periodo/{idPeriodo}/")
    @Timed
	public ResponseEntity<List<BloqueoGastos>> getBloqueosGastosByPeriodo(@PathVariable (value="idPeriodo") Integer idPeriodo) {
		log.debug("REST request to getbloqueos by Periodo");

		Iterable<BloqueoGastos> listaBloqueos = bloqueoGastosService.getBloqueoGastosByPeriodoLiquidacion(idPeriodo);
		return ResponseEntity.ok((List<BloqueoGastos>) listaBloqueos);
	}
    
    
    
    /**
     * GET getContadoresControlSeguimiento
     * 
     */
	@GetMapping("/bloqueos/resumen")
	@Timed
	public ResponseEntity<List<ControlBloqueoGastos>> getContadoresControlSeguimiento(){
		log.debug("REST request getPeriodosResumen");
		
		List<ControlBloqueoGastos> bloqueos = bloqueoGastosService.getPeriodosResumen();
		return ResponseEntity.ok((List<ControlBloqueoGastos>) bloqueos);
	}
	
	
	/**
     * GET getContadoresControlSeguimiento
     * 
     */
	@GetMapping("/bloqueos/resumen/{anio}")
	@Timed
	public ResponseEntity<List<ControlBloqueoGastos>> getContadoresControlSeguimiento(@PathVariable (value="anio") Integer anio){
		log.debug("REST request getPeriodosResumen");
		
		List<ControlBloqueoGastos> bloqueos = bloqueoGastosService.getPeriodosResumenByAnio(anio);
		return ResponseEntity.ok((List<ControlBloqueoGastos>) bloqueos);
	}
	
	/**
     * GET getContadoresControlSeguimiento
     * 
     */
	@GetMapping("/bloqueos/resumen/{idTipoPrac}/{anio}")
	@Timed
	public ResponseEntity<List<ControlBloqueoGastos>> getContadoresControlSeguimiento(@PathVariable (value="idTipoPrac") Integer idTipoPrac, @PathVariable (value="anio") Integer anio){
		log.debug("REST request getPeriodosResumen");
		
		List<ControlBloqueoGastos> bloqueos = bloqueoGastosService.getPeriodosResumenByAnioAndTipoPractica(idTipoPrac, anio);
		return ResponseEntity.ok((List<ControlBloqueoGastos>) bloqueos);
	}
	
	/**
     * GET getContadoresControlSeguimiento
     * 
     */
	@GetMapping("/bloqueos/resumen/{idPerLiq}/{idTipoPrac}/{anio}")
	@Timed
	public ResponseEntity<List<ControlBloqueoGastos>> getContadoresControlSeguimiento(@PathVariable (value="idPerLiq") Integer idPerLiq, @PathVariable (value="idTipoPrac") Integer idTipoPrac, @PathVariable (value="anio") Integer anio){
		log.debug("REST request getPeriodosResumen");
		
		List<ControlBloqueoGastos> bloqueos = bloqueoGastosService.getPeriodosResumenByAnioAndTipoPracticaAndPeriodoLiquidacion(idPerLiq, idTipoPrac, anio);
		return ResponseEntity.ok((List<ControlBloqueoGastos>) bloqueos);
	}
}

package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

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

import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.service.DistribucionPeriodoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing DistribucionPeriodo.
 */
@RestController
@RequestMapping("/api")
public class DistribucionPeriodoResource {

	private final Logger log = LoggerFactory.getLogger(DistribucionPeriodoResource.class);

	private static final String ENTITY_NAME = "DistribucionPeriodo";

	@Autowired
	private DistribucionPeriodoService distribucionPeriodoService;

	/**
	 * POST /istribucion-periodo : Create a new DistribucionPeriodo.
	 *
	 * @param distribucionPeriodo
	 *            the DistribucionPeriodo to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         DistribucionPeriodo, or with status 400 (Bad Request) if the
	 *         DistribucionPeriodo has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/distribucion-periodos")
	@Timed
	public ResponseEntity<Object> createDistribucionPeriodo(
			@RequestBody DistribucionPeriodo distribucionPeriodo) throws URISyntaxException {
		log.debug("REST request to save DistribucionPeriodo : {}", distribucionPeriodo);
		try {
		    return ResponseEntity.ok(distribucionPeriodoService.createDistribucionPeriodo(distribucionPeriodo));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }
	}

	/**
	 * PUT /istribucion-periodo : Updates an existing DistribucionPeriodo.
	 *
	 * @param distribucionPeriodo
	 *            the DistribucionPeriodo to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         DistribucionPeriodo, or with status 400 (Bad Request) if the
	 *         DistribucionPeriodo is not valid, or with status 500 (Internal
	 *         Server Error) if the DistribucionPeriodo couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/distribucion-periodos")
	@Timed
	public ResponseEntity<String> updateDistribucionPeriodo(@RequestBody DistribucionPeriodo distribucionPeriodo)
			throws URISyntaxException {
		log.debug("REST request to update DistribucionPeriodo : {}", distribucionPeriodo);
		try {
			distribucionPeriodoService.updateDistribucionPeriodo(distribucionPeriodo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /istribucion-periodo : get all the DistribucionPeriodos.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         DistribucionPeriodos in body
	 */
	@GetMapping("/distribucion-periodos")
	@Timed
	public ResponseEntity<List<DistribucionPeriodo>> getAllDistribucionPeriodos() {
		log.debug("REST request to get all DistribucionPeriodos");
		Iterable<DistribucionPeriodo> listaParticipantes = distribucionPeriodoService.getAllDistribucionPeriodos();
		return ResponseEntity.ok((List<DistribucionPeriodo>) listaParticipantes);
	}
	
	@GetMapping("/distribucion-periodos/horas/{idMatricula}/{idDistribucion}/{idTipoPractica}")
	@Timed
	public Integer getHorasEnDistribucion(@PathVariable (value="idMatricula") String idMatricula,@PathVariable (value="idDistribucion") Long idDistribucion,@PathVariable (value="idTipoPractica") Long idTipoPractica) {
		log.debug("REST request to get all DistribucionPeriodos");
		return  distribucionPeriodoService.getHorasEnDistribucion(idMatricula, idDistribucion, idTipoPractica);		
	}
	 

	/**
	 * GET /oferta-alumnos/:id : get the "id" DistribucionPeriodo.
	 *
	 * @param id
	 *            the id of the DistribucionPeriodo to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         DistribucionPeriodo, or with status 404 (Not Found)
	 */
	@GetMapping("/distribucion-periodos/{id}")
	@Timed
	public ResponseEntity<DistribucionPeriodo> getDistribucionPeriodo(@PathVariable Long id) {
		log.debug("REST request to get DistribucionPeriodo : {}", id);
		try {
			return ResponseEntity.ok(distribucionPeriodoService.getDistribucionPeriodo(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}
	
    @GetMapping("/distribucion-periodos/distribucion/{idDistribucion}")
    @Timed
    public Iterable<DistribucionPeriodo> getAllDistribucionesByDistribucion(@PathVariable (value="idDistribucion") Long id)  {
        log.debug("REST request to get all DistribucionPeriodo By Distribucion");       
        return distribucionPeriodoService.getAllByDistribucion(id);
     }
       
    
    @GetMapping("/distribucion-periodos/distribucion/{idDistribucion}/{idPeriodoPractica}")
    @Timed
    public Iterable<DistribucionPeriodo> getAllDistribucionesByDistribucionAndPeriodo(@PathVariable (value="idDistribucion") Long id,@PathVariable (value="idPeriodoPractica") Long idPeriodoPractica)  {
        log.debug("REST request to get all DistribucionPeriodo By Distribucion");       
        return distribucionPeriodoService.getAllByDistribucionAndPeriodo(id, idPeriodoPractica);
     }
   
	
	/**
	 * DELETE /oferta-alumnos/:id : delete the "id" DistribucionPeriodo.
	 *
	 * @param id
	 *            the id of the DistribucionPeriodo to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/distribucion-periodos/{id}")
	@Timed
	public ResponseEntity<String> deleteDistribucionPeriodo(@PathVariable Long id) {
		log.debug("REST request to delete DistribucionPeriodo : {}", id);
		try {
			distribucionPeriodoService.deleteDistribucionPeriodo(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}

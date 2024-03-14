package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import es.princast.gepep.domain.CriterioEvaluacion;
import es.princast.gepep.service.CriterioEvaluacionService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing CriterioEvaluacion.
 */
@RestController
@RequestMapping("/api")
public class CriterioEvaluacionResource {

	private final Logger log = LoggerFactory.getLogger(CriterioEvaluacionResource.class);

	private static final String ENTITY_NAME = "criterioEvaluacion";

	@Autowired
	private CriterioEvaluacionService criterioService;

	/**
	 * POST /criterio-evaluacion : Create a new criterioEvaluacion.
	 *
	 * @param criterioEvaluacion
	 *            the criterioEvaluacion to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         criterioEvaluacion, or with status 400 (Bad Request) if the
	 *         criterioEvaluacion has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/criterios")
	@Timed
	public ResponseEntity<Object> createCriterioEvaluacion(
			@Valid @RequestBody CriterioEvaluacion criterioEvaluacion) throws URISyntaxException {
		log.debug("REST request to save CriterioEvaluacion : {}", criterioEvaluacion);
		
		try {
		    return ResponseEntity.ok(criterioService.createCriterioEvaluacion(criterioEvaluacion));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } 
	}

	/**
	 * PUT /criterio-evaluacion : Updates an existing criterioEvaluacion.
	 *
	 * @param criterioEvaluacion
	 *            the criterioEvaluacion to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         criterioEvaluacion, or with status 400 (Bad Request) if the
	 *         criterioEvaluacion is not valid, or with status 500 (Internal Server
	 *         Error) if the criterioEvaluacion couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/criterios")
	@Timed
	public ResponseEntity<String> updateCriterioEvaluacion(@Valid @RequestBody CriterioEvaluacion criterioEvaluacion)
			throws URISyntaxException {
		log.debug("REST request to update CriterioEvaluacion : {}", criterioEvaluacion);
		try {
			criterioService.updateCriterioEvaluacion(criterioEvaluacion);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	/**
	 * GET /criterio-evaluacion : get all the criterioEvaluacions.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         criterioEvaluacions in body
	 */
	@GetMapping("/criterios")
	@Timed
	public ResponseEntity<List<CriterioEvaluacion>> getAllCriteriosEvaluacion() {
		log.debug("REST request to get all CriterioEvaluacions");
		Iterable<CriterioEvaluacion> listaCriterios= criterioService.getAllCriterios();
        return ResponseEntity.ok((List<CriterioEvaluacion>)listaCriterios);       
	 
	}

	/**
	 * GET /resultados-aprendizaje : get all the resultados by ciclo.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         capacidadTerminals in body
	 */
 

	@GetMapping("/criterios/{ca_ciclo}/ciclo")
	@Timed
	public List<CriterioEvaluacion> getAllCriteriosByCiclo(@PathVariable(value = "ca_ciclo") String id, Pageable pageable) {
		log.debug("REST request to get all CriteriosEvaluacion by Ciclo");
		return criterioService.getAllCriteriosByCiclo(id, pageable);
	}
	
	
	
	@GetMapping("/criterios/ciclo/{ca_ciclo}")
	@Timed
	public CriterioEvaluacion getCriteriosByCiclo(@PathVariable(value = "ca_ciclo") String id,
			Pageable pageable) {
		log.debug("REST request to get all Resultados Aprendizaje by Ciclo");
		return criterioService.getCriteriosByCiclo(id);
	}
	
	/**
	 * GET /criterio-evaluacion/:id : get the "id" criterioEvaluacion.
	 *
	 * @param id
	 *            the id of the criterioEvaluacion to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         criterioEvaluacion, or with status 404 (Not Found)
	 */
	@GetMapping("/criterios/{id}")
	@Timed
	public ResponseEntity<CriterioEvaluacion> getCriterioEvaluacion(@PathVariable Long id) {
		log.debug("REST request to get CriterioEvaluacion : {}", id);
		try {
			return ResponseEntity.ok(criterioService.getCriterioEvaluacion(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /criterio-evaluacion/:id : delete the "id" criterioEvaluacion.
	 *
	 * @param id
	 *            the id of the criterioEvaluacion to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/criterios/{id}")
	@Timed
	public ResponseEntity<String> deleteCriterioEvaluacion(@PathVariable Long id) {
		log.debug("REST request to delete CriterioEvaluacion : {}", id);
		try {
			criterioService.deleteCriterioEvaluacion(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

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

import es.princast.gepep.domain.Realizacion;
import es.princast.gepep.service.RealizacionService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Realizacion.
 */
@RestController
@RequestMapping("/api")
public class RealizacionResource {

	private final Logger log = LoggerFactory.getLogger(RealizacionResource.class);

	private static final String ENTITY_NAME = "realizacion";

	@Autowired
	private RealizacionService realizacionService;



	/**
	 * POST /realizaciones : Create a new realizacion.
	 *
	 * @param realizacion
	 *            the realizacion to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         realizacion, or with status 400 (Bad Request) if the realizacion has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/realizaciones")
	@Timed
	public ResponseEntity<Object> createRealizacion(@Valid @RequestBody Realizacion realizacion)
			throws URISyntaxException {
		log.debug("REST request to save Realizacion : {}", realizacion);
		try {
		    return ResponseEntity.ok(realizacionService.createRealizacion(realizacion));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	}

	/**
	 * PUT /realizaciones : Updates an existing realizacion.
	 *
	 * @param realizacion
	 *            the realizacion to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         realizacion, or with status 400 (Bad Request) if the realizacion is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         realizacion couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/realizaciones")
	@Timed
	public ResponseEntity<String> updateRealizacion(@Valid @RequestBody Realizacion realizacion)
			throws URISyntaxException {
		log.debug("REST request to update Realizacion : {}", realizacion);

		try {
			realizacionService.updateRealizacion(realizacion);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	/**
	 * GET /realizaciones : get all the realizacions.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of realizacions
	 *         in body
	 */
	@GetMapping("/realizaciones")
	@Timed
	public ResponseEntity<List<Realizacion>> getAllRealizaciones() {
		log.debug("REST request to get all Realizaciones");
		Iterable<Realizacion> listaRealizaciones = realizacionService.getAllRealizaciones();
		return ResponseEntity.ok((List<Realizacion>) listaRealizaciones);
	}

	/**
	 * GET /realizaciones : get all the realizaciones by ciclo
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         capacidadTerminals in body
	 */
	@GetMapping("/realizaciones/{ca_ciclo}/ciclo")
	@Timed
	public List<Realizacion> getAllRealizacionesByCiclo(@PathVariable(value = "ca_ciclo") String id, Pageable pageable) {
		log.debug("REST request to get all CriteriosEvaluacion by Ciclo");
		return realizacionService.getAllRealizacionesByCiclo(id, pageable);
	}
	
	@GetMapping("/realizaciones/ciclo/{ca_ciclo}")
	@Timed
	public  Realizacion getRealizacionByCiclo(@PathVariable(value = "ca_ciclo") String id) {
		log.debug("REST request to get all CriteriosEvaluacion by Ciclo");
		return realizacionService.getRealizacionByCiclo(id);
	}
	
	/**
	 * GET /realizacions/:id : get the "id" realizacion.
	 *
	 * @param id
	 *            the id of the realizacion to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         realizacion, or with status 404 (Not Found)
	 */
	@GetMapping("/realizaciones/{id}")
	@Timed
	public ResponseEntity<Realizacion> getRealizacion(@PathVariable Long id) {
		log.debug("REST request to get Realizacion : {}", id);
		try {
			return ResponseEntity.ok(realizacionService.getRealizacion(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

	/**
	 * DELETE /realizaciones/:id : delete the "id" realizacion.
	 *
	 * @param id
	 *            the id of the realizacion to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/realizaciones/{id}")
	@Timed
	public ResponseEntity<String> deleteRealizacion(@PathVariable Long id) {
		log.debug("REST request to delete Realizacion : {}", id);
		  try {
	        	realizacionService.deleteRealizacion(id);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
	                    .body(e.getMessage());
	            }
		  return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
		       
	}

}

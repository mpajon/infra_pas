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

import es.princast.gepep.domain.Clausula;
import es.princast.gepep.service.ClausulaService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Clausula.
 */
@RestController
@RequestMapping("/api")
public class ClausulaResource {

	private final Logger log = LoggerFactory.getLogger(ClausulaResource.class);

	private static final String ENTITY_NAME = "clausula";

	@Autowired
	private ClausulaService clausulaService;

	/**
	 * POST /clausulas : Create a new clausula.
	 *
	 * @param clausula
	 *            the clausula to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         clausula, or with status 400 (Bad Request) if the clausula has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/clausulas")
	@Timed
	public ResponseEntity<Object> createClausula(@Valid @RequestBody Clausula clausula) throws URISyntaxException {
		log.debug("REST request to save Clausula : {}", clausula);
		
		try {
		    return ResponseEntity.ok(clausulaService.createClausula(clausula));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	 
	}

	/**
	 * PUT /clausulas : Updates an existing clausula.
	 *
	 * @param clausula
	 *            the clausula to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         clausula, or with status 400 (Bad Request) if the clausula is not
	 *         valid, or with status 500 (Internal Server Error) if the clausula
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/clausulas")
	@Timed
	public ResponseEntity<String> updateClausula(@Valid @RequestBody Clausula clausula) throws URISyntaxException {
		log.debug("REST request to update Clausula : {}", clausula);
		
		clausulaService.updateClausula(clausula);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /clausulas : get all the clausulas.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of clausulas in
	 *         body
	 */
	@GetMapping("/clausulas")
	@Timed
	public ResponseEntity<List<Clausula>> getAllClausulas() {
		log.debug("REST request to get all Clausulas");
		Iterable<Clausula> listaClausulas = clausulaService.getAllClausulas();
		return ResponseEntity.ok((List<Clausula>) listaClausulas);

	}

	/**
	 * GET /clausulas/:id : get the "id" clausula.
	 *
	 * @param id
	 *            the id of the clausula to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the clausula,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/clausulas/{id}")
	@Timed
	public ResponseEntity<Clausula> getClausula(@PathVariable Long id) {
		log.debug("REST request to get Clausula : {}", id);

		try {
			return ResponseEntity.ok(clausulaService.getClausula(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /clausulas/:id : delete the "id" clausula.
	 *
	 * @param id
	 *            the id of the clausula to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/clausulas/{id}")
	@Timed
	public ResponseEntity<String> deleteClausula(@PathVariable Long id) {
		log.debug("REST request to delete Clausula : {}", id);
		try {
			clausulaService.deleteClausula(id);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

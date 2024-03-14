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

import es.princast.gepep.domain.Visor;
import es.princast.gepep.service.VisorService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Visor.
 */
@RestController
@RequestMapping("/api")
public class VisorResource {

	private final Logger log = LoggerFactory.getLogger(VisorResource.class);

	private static final String ENTITY_NAME = "visor";

	@Autowired
	private VisorService visorService;

	/**
	 * POST /visors : Create a new visor.
	 *
	 * @param visor
	 *            the visor to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         visor, or with status 400 (Bad Request) if the visor has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/visores")
	@Timed

	public ResponseEntity<Object> createVisor(@Valid @RequestBody Visor visor) throws URISyntaxException {
		log.debug("REST request to save Visor : {}", visor);
		try {
		    return ResponseEntity.ok(visorService.createVisor(visor));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }      
	}

	/**
	 * PUT /visors : Updates an existing visor.
	 *
	 * @param visor
	 *            the visor to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         visor, or with status 400 (Bad Request) if the visor is not valid, or
	 *         with status 500 (Internal Server Error) if the visor couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/visores")
	@Timed
	public ResponseEntity<String> updateVisor(@Valid @RequestBody Visor visor) throws URISyntaxException {
		visorService.updateVisor(visor);		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /visors : get all the visors.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of visors in
	 *         body
	 */
	@GetMapping("/visores")
	@Timed
	public ResponseEntity<List<Visor>> getAllVisores() {
		log.debug("REST request to get a page of Visores");
		Iterable<Visor> listaVisores = visorService.getAllVisores();
		return ResponseEntity.ok((List<Visor>) listaVisores);
	 
	}

	/**
	 * GET /visors/:id : get the "id" visor.
	 *
	 * @param id
	 *            the id of the visor to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the visor, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/visores/{id}")
	@Timed
	public ResponseEntity<Visor> getVisor(@PathVariable Long id) {
		log.debug("REST request to get Visor : {}", id);
		try {
			return ResponseEntity.ok(visorService.getVisor(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

	/**
	 * DELETE /visors/:id : delete the "id" visor.
	 *
	 * @param id
	 *            the id of the visor to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/visores/{id}")
	@Timed
	public ResponseEntity<String> deleteVisor(@PathVariable Long id) {
		log.debug("REST request to delete Visor : {}", id);
		try {
			visorService.deleteVisor(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}

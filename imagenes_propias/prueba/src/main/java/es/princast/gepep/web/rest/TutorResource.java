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

import es.princast.gepep.domain.Tutor;
import es.princast.gepep.service.TutorService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Tutor.
 */
@RestController
@RequestMapping("/api")
public class TutorResource {

	private final Logger log = LoggerFactory.getLogger(TutorResource.class);

	private static final String ENTITY_NAME = "tutor";

	@Autowired
	private TutorService tutorService;

	/**
	 * POST /tutores : Create a new tutor.
	 *
	 * @param tutor
	 *            the tutor to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         tutor, or with status 400 (Bad Request) if the tutor has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/tutores")
	@Timed
	public ResponseEntity<Object> createTutor(@RequestBody Tutor tutor) throws URISyntaxException {
		log.debug("REST request to save Tutor : {}", tutor);
		try {
		    return ResponseEntity.ok(tutorService.createTutor(tutor));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } 
		

	}

	/**
	 * PUT /tutores : Updates an existing tutor.
	 *
	 * @param tutor
	 *            the tutor to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         tutor, or with status 400 (Bad Request) if the tutor is not valid, or
	 *         with status 500 (Internal Server Error) if the tutor couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/tutores")
	@Timed
	public ResponseEntity<String> updateTutor(@RequestBody Tutor tutor) throws URISyntaxException {
		log.debug("REST request to update Tutor : {}", tutor);
		try {
			tutorService.updateTutor(tutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /tutores : get all the tutors.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tutors in
	 *         body
	 */
	@GetMapping("/tutores")
	@Timed
	public ResponseEntity<List<Tutor>> getAllTutores() {
		log.debug("REST request to get all Tutores");
		Iterable<Tutor> listaTutores = tutorService.getAllTutores();
		return ResponseEntity.ok((List<Tutor>) listaTutores);
	}

	/**
	 * GET /tutores/:id : get the "id" tutor.
	 *
	 * @param id
	 *            the id of the tutor to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tutor, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/tutores/{id}")
	@Timed
	public ResponseEntity<Tutor> getTutor(@PathVariable Long id) {
		log.debug("REST request to get Tutor : {}", id);
		try {
			return ResponseEntity.ok(tutorService.getTutor(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /tutores/:id : delete the "id" tutor.
	 *
	 * @param id
	 *            the id of the tutor to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tutores/{id}")
	@Timed
	public ResponseEntity<String> deleteTutor(@PathVariable Long id) {
		log.debug("REST request to delete Tutor : {}", id);
		try {
			tutorService.deleteTutor(id);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}

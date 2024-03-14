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

import es.princast.gepep.domain.ResponsableArea;
import es.princast.gepep.service.ResponsableAreaService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ResponsableArea.
 */
@RestController
@RequestMapping("/api")
public class ResponsableAreaResource {

	private final Logger log = LoggerFactory.getLogger(ResponsableAreaResource.class);

	private static final String ENTITY_NAME = "responsableArea";

	@Autowired
	private ResponsableAreaService responsableAreaService;

	/**
	 * POST /responsable-areas : Create a new responsableArea.
	 *
	 * @param responsableArea
	 *            the responsableArea to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         responsableArea, or with status 400 (Bad Request) if the
	 *         responsableArea has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/responsable-area")
	@Timed
	public ResponseEntity<Object> createResponsableArea(@Valid @RequestBody ResponsableArea responsableArea)
			throws URISyntaxException {
		log.debug("REST request to save ResponsableArea : {}", responsableArea);
		try {
		    return ResponseEntity.ok(responsableAreaService.createResponsableArea(responsableArea));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }  
	}

	/**
	 * PUT /responsable-areas : Updates an existing responsableArea.
	 *
	 * @param responsableArea
	 *            the responsableArea to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         responsableArea, or with status 400 (Bad Request) if the
	 *         responsableArea is not valid, or with status 500 (Internal Server
	 *         Error) if the responsableArea couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/responsable-area")
	@Timed
	public ResponseEntity<String> updateResponsableArea(@Valid @RequestBody ResponsableArea responsableArea)
			throws URISyntaxException {
		log.debug("REST request to update ResponsableArea : {}", responsableArea);

		try {
			responsableAreaService.updateResponsableArea(responsableArea);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /responsable-areas : get all the responsableAreas.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         responsableAreas in body
	 */
	@GetMapping("/responsable-area")
	@Timed
	public ResponseEntity<List<ResponsableArea>> getAllResponsableAreas() {
		log.debug("REST request to get all ResponsableAreas");
		Iterable<ResponsableArea> listaResponsables = responsableAreaService.getAllResponsableAreaes();
		return ResponseEntity.ok((List<ResponsableArea>) listaResponsables);
	}

	/**
	 * GET /responsable-areas/:id : get the "id" responsableArea.
	 *
	 * @param id
	 *            the id of the responsableArea to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         responsableArea, or with status 404 (Not Found)
	 */
	@GetMapping("/responsable-area/{id}")
	@Timed
	public ResponseEntity<ResponsableArea> getResponsableArea(@PathVariable Long id) {
		log.debug("REST request to get ResponsableArea : {}", id);

		try {
			return ResponseEntity.ok(responsableAreaService.getResponsableArea(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /responsable-areas/:id : delete the "id" responsableArea.
	 *
	 * @param id
	 *            the id of the responsableArea to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/responsable-area/{id}")
	@Timed
	public ResponseEntity<String> deleteResponsableArea(@PathVariable Long id) {
		log.debug("REST request to delete ResponsableArea : {}", id);
		try {
			responsableAreaService.deleteResponsableArea(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
	
    /**
     * GET  /areas - responsableArea/:id : get the "id" area.
     *
     * @param id the id of the area to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the area, or with status 404 (Not Found)
//     */
    @GetMapping("/area/{idArea}/reponsable-area")
    @Timed

    
	public ResponseEntity<List<ResponsableArea>> getResponsablByArea(@PathVariable (value="idArea") Long idArea) {
		log.debug("REST request to get all ResponsableAreas");
		//Iterable<ResponsableArea> listaResponsables = responsableAreaService.getAllResponsableAreaes();
		Iterable<ResponsableArea> listaResponsables = responsableAreaService.getResponsablByArea(idArea);
		return ResponseEntity.ok((List<ResponsableArea>) listaResponsables);
	}
}

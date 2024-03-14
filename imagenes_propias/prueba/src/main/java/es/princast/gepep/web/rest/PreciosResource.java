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

import es.princast.gepep.domain.Precios;
import es.princast.gepep.service.PreciosService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Precios.
 */
@RestController
@RequestMapping("/api")
public class PreciosResource {

	private final Logger log = LoggerFactory.getLogger(PreciosResource.class);

	private static final String ENTITY_NAME = "precios";

	@Autowired
	private PreciosService preciosService;

	/**
	 * POST /precios : Create a new precios.
	 *
	 * @param precios
	 *            the precios to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         precios, or with status 400 (Bad Request) if the precios has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/precios")
	@Timed
	public ResponseEntity<Object> createPrecios(@RequestBody Precios precios) throws URISyntaxException {
		log.debug("REST request to save Precios : {}", precios);
		try {
		    return ResponseEntity.ok(preciosService.createPrecios(precios));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } 
	}

	/**
	 * PUT /precios : Updates an existing precios.
	 *
	 * @param precios
	 *            the precios to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         precios, or with status 400 (Bad Request) if the precios is not
	 *         valid, or with status 500 (Internal Server Error) if the precios
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/precios")
	@Timed
	public ResponseEntity<String> updatePrecios(@RequestBody Precios precios) throws URISyntaxException {
		log.debug("REST request to update Precios : {}", precios);
		try {
			preciosService.updatePrecios(precios);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /precios : get all the precios.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of precios in
	 *         body
	 */
	@GetMapping("/precios")
	@Timed
	public ResponseEntity<List<Precios>> getAllPrecios() {
		log.debug("REST request to get all Precios");
		Iterable<Precios> listaPrecios = preciosService.getAllPrecios();
		return ResponseEntity.ok((List<Precios>) listaPrecios);
	}

	/**
	 * GET /precios/:id : get the "id" precios.
	 *
	 * @param id
	 *            the id of the precios to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the precios, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/precios/{id}")
	@Timed
	public ResponseEntity<Precios> getPrecios(@PathVariable Long id) {
		log.debug("REST request to get Precios : {}", id);
		try {
			return ResponseEntity.ok(preciosService.getPrecios(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}
	
	
	@GetMapping("/precios/practica/{idTipoPractica}")
	@Timed
	public Precios getAllPreciosByTipoPractica(@PathVariable(value = "idTipoPractica") Long idTipoPractica) {
		log.debug("REST request to get all Precios By TipoPractica");
		return preciosService.getPreciosByTipoPractica(idTipoPractica);
	}
	

	/**
	 * DELETE /precios/:id : delete the "id" precios.
	 *
	 * @param id
	 *            the id of the precios to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/precios/{id}")
	@Timed
	public ResponseEntity<String> deletePrecios(@PathVariable Long id) {
		log.debug("REST request to delete Precios : {}", id);
		try {
			preciosService.deletePrecios(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

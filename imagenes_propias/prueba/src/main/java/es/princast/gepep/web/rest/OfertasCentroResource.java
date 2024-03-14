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

import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.service.OfertasCentroService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing OfertasCentro.
 */
@RestController
@RequestMapping("/api")
public class OfertasCentroResource {

	private final Logger log = LoggerFactory.getLogger(OfertasCentroResource.class);

	private static final String ENTITY_NAME = "ofertasCentro";

	@Autowired
	private OfertasCentroService ofertasCentroService;

	/**
	 * POST /practica-ofertas : Create a new ofertasCentro.
	 *
	 * @param ofertasCentro
	 *            the ofertasCentro to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         ofertasCentro, or with status 400 (Bad Request) if the
	 *         ofertasCentro has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/ofertas-centro")
	@Timed
	public ResponseEntity<Object> createOfertasCentro(@RequestBody OfertasCentro ofertasCentro)
			throws URISyntaxException {
		log.debug("REST request to save OfertasCentro : {}", ofertasCentro);
		try {
		    return ResponseEntity.ok(ofertasCentroService.createOfertasCentro(ofertasCentro));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } 
	}
	
 
	/**
	 * PUT /practica-ofertas : Updates an existing ofertasCentro.
	 *
	 * @param ofertasCentro
	 *            the ofertasCentro to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         ofertasCentro, or with status 400 (Bad Request) if the
	 *         ofertasCentro is not valid, or with status 500 (Internal Server
	 *         Error) if the ofertasCentro couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/ofertas-centro")
	@Timed
	public ResponseEntity<String> updateOfertasCentro(@RequestBody OfertasCentro ofertasCentro)
			throws URISyntaxException {
		log.debug("REST request to update OfertasCentro : {}", ofertasCentro);
		try {
			ofertasCentroService.updateOfertasCentro(ofertasCentro);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	/**
	 * GET /practica-ofertas : get all the ofertasCentros.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         ofertasCentros in body
	 */
	@GetMapping("/ofertas-centro")
	@Timed
	public ResponseEntity<List<OfertasCentro>> getAllOfertasCentros() {
		log.debug("REST request to get all OfertasCentros");
		Iterable<OfertasCentro> listaOfertasCentros = ofertasCentroService.getAllOfertasCentros();
		return ResponseEntity.ok((List<OfertasCentro>) listaOfertasCentros);
	}

	/**
	 * GET /practica-ofertas/:id : get the "id" ofertasCentro.
	 *
	 * @param id
	 *            the id of the ofertasCentro to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         ofertasCentro, or with status 404 (Not Found)
	 */
	@GetMapping("/ofertas-centro/{id}")
	@Timed
	public ResponseEntity<OfertasCentro> getOfertasCentro(@PathVariable String id) {
		log.debug("REST request to get OfertasCentro : {}", id);
		try {
			return ResponseEntity.ok(ofertasCentroService.getOfertasCentro(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

	/**
	 * DELETE /practica-ofertas/:id : delete the "id" ofertasCentro.
	 *
	 * @param id
	 *            the id of the ofertasCentro to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/ofertas-centro/{id}")
	@Timed
	public ResponseEntity<String> deleteOfertasCentro(@PathVariable String id) {
		log.debug("REST request to delete OfertasCentro : {}", id);
		try {
			ofertasCentroService.deleteOfertasCentro(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

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

import es.princast.gepep.domain.TipoCentro;
import es.princast.gepep.service.TipoCentroService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing TipoCentro.
 */
@RestController
@RequestMapping("/api")
public class TipoCentroResource {

	private final Logger log = LoggerFactory.getLogger(TipoCentroResource.class);

	private static final String ENTITY_NAME = "tipoCentro";

	@Autowired
	private TipoCentroService tipoCentroService;

	/**
	 * POST /tipos-centro : Create a new tipoCentro.
	 *
	 * @param tipoCentro
	 *            the tipoCentro to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         tipoCentro, or with status 400 (Bad Request) if the tipoCentro has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/tipos-centro")
	@Timed
	public ResponseEntity<TipoCentro> createTipoCentro(@Valid @RequestBody TipoCentro tipoCentro)
			throws URISyntaxException {
		log.debug("REST request to save TipoCentro : {}", tipoCentro);
		return tipoCentroService.createTipoCentro(tipoCentro);
	}

	/**
	 * PUT /tipos-centro : Updates an existing tipoCentro.
	 *
	 * @param tipoCentro
	 *            the tipoCentro to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         tipoCentro, or with status 400 (Bad Request) if the tipoCentro is not
	 *         valid, or with status 500 (Internal Server Error) if the tipoCentro
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/tipos-centro")
	@Timed
	public ResponseEntity<String> updateTipoCentro(@Valid @RequestBody TipoCentro tipoCentro)
			throws URISyntaxException {
		log.debug("REST request to update TipoCentro : {}", tipoCentro);
		try {
			tipoCentroService.updateTipoCentro(tipoCentro);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /tipos-centro : get all the tipoCentros.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tipoCentros
	 *         in body
	 */
	@GetMapping("/tipos-centro")
	@Timed
	public ResponseEntity<List<TipoCentro>> getAllTipoCentros() {
		log.debug("REST request to get all TipoCentros");
		Iterable<TipoCentro> listaTiposCentro = tipoCentroService.getAllTiposCentros();
		return ResponseEntity.ok((List<TipoCentro>) listaTiposCentro);
	}

	/**
	 * GET /tipos-centro/:id : get the "id" tipoCentro.
	 *
	 * @param id
	 *            the id of the tipoCentro to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tipoCentro,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/tipos-centro/{id}")
	@Timed
	public ResponseEntity<TipoCentro> getTipoCentro(@PathVariable Long id) {
		log.debug("REST request to get TipoCentro : {}", id);
		try {
			return ResponseEntity.ok(tipoCentroService.getTipoCentro(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /tipos-centro/:id : delete the "id" tipoCentro.
	 *
	 * @param id
	 *            the id of the tipoCentro to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tipos-centro/{id}")
	@Timed
	public ResponseEntity<String> deleteTipoCentro(@PathVariable Long id) {
		log.debug("REST request to delete TipoCentro : {}", id);
		try {
			tipoCentroService.deleteTipoCentro(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

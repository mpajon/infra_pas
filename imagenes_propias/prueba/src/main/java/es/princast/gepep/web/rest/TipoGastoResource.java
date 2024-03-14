package es.princast.gepep.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.princast.gepep.domain.TipoGasto;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.service.TipoGastoService;
import es.princast.gepep.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing TipoGasto.
 */
@RestController
@RequestMapping("/api")
public class TipoGastoResource {

	private final Logger log = LoggerFactory.getLogger(TipoGastoResource.class);

	private static final String ENTITY_NAME = "tipoGasto";

	@Autowired
	private TipoGastoService tipoGastoService;

	/**
	 * POST /tipo-gasto : Create a new tipoGasto.
	 *
	 * @param tipoGasto
	 *            the tipoGasto to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         tipoGasto, or with status 400 (Bad Request) if the tipoGasto has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/tipo-gasto")
	@Timed
	public ResponseEntity<TipoGasto> createTipoGasto(@Valid @RequestBody TipoGasto tipoGasto)
			throws URISyntaxException {
		log.debug("REST request to save TipoGasto : {}", tipoGasto);
		return tipoGastoService.createTipoGasto(tipoGasto);
	}

	/**
	 * PUT /tipo-gasto : Updates an existing tipoGasto.
	 *
	 * @param tipoGasto
	 *            the tipoGasto to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         tipoGasto, or with status 400 (Bad Request) if the tipoGasto is not
	 *         valid, or with status 500 (Internal Server Error) if the tipoGasto
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/tipo-gasto")
	@Timed
	public ResponseEntity<String> updateTipoGasto(@Valid @RequestBody TipoGasto tipoGasto)
			throws URISyntaxException {
		log.debug("REST request to update TipoGasto : {}", tipoGasto);
		try {
			tipoGastoService.updateTipoGasto(tipoGasto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /tipo-gasto : get all the tipoGastos.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tipoGastos
	 *         in body
	 */
	@GetMapping("/tipo-gasto")
	@Timed
	public ResponseEntity<List<TipoGasto>> getAllTipoGastos() {
		log.debug("REST request to get all TipoGastos");
		Iterable<TipoGasto> listaTiposGasto = tipoGastoService.getAllTipoGasto();
		return ResponseEntity.ok((List<TipoGasto>) listaTiposGasto);
	}

	/**
	 * GET /tipo-gasto/:id : get the "id" tipoGasto.
	 *
	 * @param id
	 *            the id of the tipoGasto to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tipoGasto,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/tipo-gasto/{id}")
	@Timed
	public ResponseEntity<TipoGasto> getTipoGasto(@PathVariable Long id) {
		log.debug("REST request to get TipoGasto : {}", id);
		try {
			return ResponseEntity.ok(tipoGastoService.getTipoGasto(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /tipo-gasto/:id : delete the "id" tipoGasto.
	 *
	 * @param id
	 *            the id of the tipoGasto to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tipo-gasto/{id}")
	@Timed
	public ResponseEntity<String> deleteTipoGasto(@PathVariable Long id) {
		log.debug("REST request to delete TipoGasto : {}", id);
		try {
			tipoGastoService.deleteTipoGasto(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

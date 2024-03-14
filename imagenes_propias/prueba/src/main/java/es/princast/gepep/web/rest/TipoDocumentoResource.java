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

import es.princast.gepep.domain.TipoDocumento;
import es.princast.gepep.service.TipoDocumentoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing TipoDocumento.
 */
@RestController
@RequestMapping("/api")
public class TipoDocumentoResource {

	private final Logger log = LoggerFactory.getLogger(TipoDocumentoResource.class);

	private static final String ENTITY_NAME = "tipoDocumento";

	@Autowired
	private TipoDocumentoService tipoDocumentoService;


	/**
	 * POST /tipos-documento : Create a new tipoDocumento.
	 *
	 * @param tipoDocumento
	 *            the tipoDocumento to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         tipoDocumento, or with status 400 (Bad Request) if the tipoDocumento has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/tipos-documento")
	@Timed
	public ResponseEntity<Object> createTipoDocumento(@Valid @RequestBody TipoDocumento tipoDocumento)
			throws URISyntaxException {
		log.debug("REST request to save TipoDocumento : {}", tipoDocumento);
		
		try {
		    return ResponseEntity.ok(tipoDocumentoService.createTipoDocumento(tipoDocumento));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } 
	}

	/**
	 * PUT /tipos-documento : Updates an existing tipoDocumento.
	 *
	 * @param tipoDocumento
	 *            the tipoDocumento to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         tipoDocumento, or with status 400 (Bad Request) if the tipoDocumento is not
	 *         valid, or with status 500 (Internal Server Error) if the tipoDocumento
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/tipos-documento")
	@Timed
	public ResponseEntity<String> updateTipoDocumento(@Valid @RequestBody TipoDocumento tipoDocumento)
			throws URISyntaxException {
		log.debug("REST request to update TipoDocumento : {}", tipoDocumento);
		try {
			tipoDocumentoService.updateTipoDocumento(tipoDocumento);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /tipos-documento : get all the tipoDocumentos.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tipoDocumentos
	 *         in body
	 */
	@GetMapping("/tipos-documento")
	@Timed
	public ResponseEntity<List<TipoDocumento>> getAllTipoDocumentos() {
		log.debug("REST request to get all TipoDocumentos");
		Iterable<TipoDocumento> listaTiposCentro = tipoDocumentoService.getAllTiposDocumento();
		return ResponseEntity.ok((List<TipoDocumento>) listaTiposCentro);
	}

	/**
	 * GET /tipos-documento/:id : get the "id" tipoDocumento.
	 *
	 * @param id
	 *            the id of the tipoDocumento to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tipoDocumento,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/tipos-documento/{id}")
	@Timed
	public ResponseEntity<TipoDocumento> getTipoDocumento(@PathVariable Long id) {
		log.debug("REST request to get TipoDocumento : {}", id);
		try {
			return ResponseEntity.ok(tipoDocumentoService.getTipoDocumento(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /tipos-documento/:id : delete the "id" tipoDocumento.
	 *
	 * @param id
	 *            the id of the tipoDocumento to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/tipos-documento/{id}")
	@Timed
	public ResponseEntity<String> deleteTipoDocumento(@PathVariable Long id) {
		log.debug("REST request to delete TipoDocumento : {}", id);
		try {
			tipoDocumentoService.deleteTipoDocumento(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
	
	

}

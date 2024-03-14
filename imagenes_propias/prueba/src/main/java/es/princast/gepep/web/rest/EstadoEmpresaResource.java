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

import es.princast.gepep.domain.EstadoEmpresa;
import es.princast.gepep.service.EstadoEmpresaService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing EstadoEmpresa.
 */
@RestController
@RequestMapping("/api")
public class EstadoEmpresaResource {

	private final Logger log = LoggerFactory.getLogger(EstadoEmpresaResource.class);

	private static final String ENTITY_NAME = "estadoEmpresa";

	@Autowired
	private EstadoEmpresaService estadoEmpresaService;

	/**
	 * POST /estado-empresas : Create a new estadoEmpresa.
	 *
	 * @param estadoEmpresa
	 *            the estadoEmpresa to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         estadoEmpresa, or with status 400 (Bad Request) if the estadoEmpresa
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/estado-empresa")
	@Timed
	public ResponseEntity<Object> createEstadoEmpresa(@Valid @RequestBody EstadoEmpresa estadoEmpresa)
			throws URISyntaxException {
		log.debug("REST request to save EstadoEmpresa : {}", estadoEmpresa);
		
		try {
		    return ResponseEntity.ok(estadoEmpresaService.createEstadoEmpresa(estadoEmpresa));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }  
	
	}

	/**
	 * PUT /estado-empresas : Updates an existing estadoEmpresa.
	 *
	 * @param estadoEmpresa
	 *            the estadoEmpresa to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         estadoEmpresa, or with status 400 (Bad Request) if the estadoEmpresa
	 *         is not valid, or with status 500 (Internal Server Error) if the
	 *         estadoEmpresa couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/estado-empresa")
	@Timed
	public ResponseEntity<String> updateEstadoEmpresa(@Valid @RequestBody EstadoEmpresa estadoEmpresa)
			throws URISyntaxException {
		log.debug("REST request to update EstadoEmpresa : {}", estadoEmpresa);
		try {
			estadoEmpresaService.updateEstadoEmpresa(estadoEmpresa);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /estado-empresas : get all the estadoEmpresas.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         estadoEmpresas in body
	 */
	@GetMapping("/estado-empresa")
	@Timed
	public ResponseEntity<List<EstadoEmpresa>> getAllEstadoEmpresas() {
		log.debug("REST request to get all EstadoEmpresas");
		Iterable<EstadoEmpresa> listaEstados = estadoEmpresaService.getAllEstadoEmpresas();
		return ResponseEntity.ok((List<EstadoEmpresa>) listaEstados);
	}

	/**
	 * GET /estado-empresas/:id : get the "id" estadoEmpresa.
	 *
	 * @param id
	 *            the id of the estadoEmpresa to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         estadoEmpresa, or with status 404 (Not Found)
	 */
	@GetMapping("/estado-empresa/{id}")
	@Timed
	public ResponseEntity<EstadoEmpresa> getEstadoEmpresa(@PathVariable Long id) {
		log.debug("REST request to get EstadoEmpresa : {}", id);
		try {
			return ResponseEntity.ok(estadoEmpresaService.getEstadoEmpresa(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /estado-empresas/:id : delete the "id" estadoEmpresa.
	 *
	 * @param id
	 *            the id of the estadoEmpresa to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/estado-empresa/{id}")
	@Timed
	public ResponseEntity<String> deleteEstadoEmpresa(@PathVariable Long id) {
		log.debug("REST request to delete EstadoEmpresa : {}", id);
		try {
			estadoEmpresaService.deleteEstadoEmpresa(id);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

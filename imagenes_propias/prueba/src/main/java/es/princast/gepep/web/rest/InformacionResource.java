package es.princast.gepep.web.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
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

import es.princast.gepep.domain.Informacion;
import es.princast.gepep.service.InformacionService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Informacion.
 */
@RestController
@RequestMapping("/api")
public class InformacionResource {

	private final Logger log = LoggerFactory.getLogger(InformacionResource.class);

	private static final String ENTITY_NAME = "informacion";

	@Autowired
	private InformacionService informacionService;

	@Autowired
	private MessageSource messageSource;
	
	
	/**
	 * POST /informacion : Create a new informacion.
	 *
	 * @param informacion
	 *            the informacion to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         informacion, or with status 400 (Bad Request) if the informacion has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/informacion")
	@Timed
	public ResponseEntity<Object> createInformacion(@Valid @RequestBody Informacion informacion)
			throws URISyntaxException {
		log.debug("REST request to save Informacion : {}", informacion);
		
		try {
			 return ResponseEntity.ok(informacionService.createInformacion(informacion));
		} catch(DataIntegrityViolationException de) {
			 return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(messageSource.getMessage("error.informacion.existe",
	                   null, LocaleContextHolder.getLocale()));
		} catch (IOException e) {
			throw new IllegalArgumentException(messageSource.getMessage("error.informacion.documento.update",
	                   null, LocaleContextHolder.getLocale()));	
		}
	}

	/**
	 * PUT /informacion : Updates an existing informacion.
	 *
	 * @param informacion
	 *            the informacion to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         informacion, or with status 400 (Bad Request) if the informacion is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         informacion couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/informacion")
	@Timed
	public ResponseEntity<String> updateInformacion(@Valid @RequestBody Informacion informacion)
			throws URISyntaxException {
		log.debug("REST request to update Informacion : {}", informacion);
		try {
			informacionService.updateInformacion(informacion);

		} catch(DataIntegrityViolationException de) {
			throw new IllegalArgumentException(messageSource.getMessage("error.informacion.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
		} catch (IOException e) {
			throw new IllegalArgumentException(messageSource.getMessage("error.informacion.documento.update",
	                   null, LocaleContextHolder.getLocale()));	
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /informacion : get all the informacions.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of informacions
	 *         in body
	 */
	@GetMapping("/informacion")
	@Timed
	public ResponseEntity<List<Informacion>> getAllInformaciones() {
		log.debug("REST request to get all Informaciones");
		Iterable<Informacion> listInformaciones = informacionService.getAllInformaciones();
		return ResponseEntity.ok((List<Informacion>) listInformaciones);
	}

	/**
	 * GET /informacion/:id : get the "id" informacion.
	 *
	 * @param id
	 *            the id of the informacion to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         informacion, or with status 404 (Not Found)
	 */
	@GetMapping("/informacion/{id}")
	@Timed
	public ResponseEntity<Informacion> getInformacion(@PathVariable Long id) {
		log.debug("REST request to get Informacion : {}", id);
		try {
			return ResponseEntity.ok(informacionService.getInformacion(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

	/**
	 * DELETE /informacion/:id : delete the "id" informacion.
	 *
	 * @param id
	 *            the id of the informacion to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/informacion/{id}")
	@Timed
	public ResponseEntity<String> deleteInformacion(@PathVariable Long id) {
		log.debug("REST request to delete Informacion : {}", id);
		try {
        	informacionService.deleteInformacion(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

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

import es.princast.gepep.domain.HorasPeriodo;
import es.princast.gepep.service.HorasPeriodoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing HorasPeriodo.
 */
@RestController
@RequestMapping("/api")
public class HorasPeriodoResource {

	private final Logger log = LoggerFactory.getLogger(HorasPeriodoResource.class);

	private static final String ENTITY_NAME = "horasPeriodo";

	@Autowired
	private HorasPeriodoService horasPeriodoService;
	
	@Autowired
	private MessageSource messageSource;
	


	/**
	 * POST /horas-periodos : Create a new horasPeriodo.
	 *
	 * @param horasPeriodo
	 *            the horasPeriodo to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         horasPeriodo, or with status 400 (Bad Request) if the horasPeriodo
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/horas-periodo")
	@Timed
	public ResponseEntity<Object> createHorasPeriodo(@RequestBody HorasPeriodo horasPeriodo)
			throws URISyntaxException {
		log.debug("REST request to save HorasPeriodo : {}", horasPeriodo);
		try {
			return ResponseEntity.ok(horasPeriodoService.createHorasPeriodo(horasPeriodo));
		} catch (DataIntegrityViolationException de) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(messageSource.getMessage("error.horario.existe", null, LocaleContextHolder.getLocale()));			
		}
	}


	/**
	 * PUT /horas-periodos : Updates an existing horasPeriodo.
	 *
	 * @param horasPeriodo
	 *            the horasPeriodo to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         horasPeriodo, or with status 400 (Bad Request) if the horasPeriodo is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         horasPeriodo couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/horas-periodo")
	@Timed
	public ResponseEntity<String> updateHorasPeriodo(@RequestBody HorasPeriodo horasPeriodo) throws URISyntaxException {
		log.debug("REST request to update HorasPeriodo : {}", horasPeriodo);
		try {
			horasPeriodoService.updateHorasPeriodo(horasPeriodo);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (DataIntegrityViolationException de) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.horario.existe", null, LocaleContextHolder.getLocale()));
		}
	}

	/**
	 * GET /horas-periodos : get all the horasPeriodos.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of horasPeriodos
	 *         in body
	 */
	@GetMapping("/horas-periodo")
	@Timed
	public ResponseEntity<List<HorasPeriodo>> getAllHorasPeriodos() {
		log.debug("REST request to get all HorasPeriodos");
		Iterable<HorasPeriodo> listaHorasPeriodo = horasPeriodoService.getAllHoras();
		return ResponseEntity.ok((List<HorasPeriodo>) listaHorasPeriodo);

	}

	/**
	 * GET /horas-periodos/:id : get the "id" horasPeriodo.
	 *
	 * @param id
	 *            the id of the horasPeriodo to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         horasPeriodo, or with status 404 (Not Found)
	 */
	@GetMapping("/horas-periodo/{idHorasPeriodo}")
	@Timed
	public ResponseEntity<HorasPeriodo> getHorasPeriodo(@PathVariable Long idHorasPeriodo) {
		log.debug("REST request to get HorasPeriodo : {}", idHorasPeriodo);

		try {
			return ResponseEntity.ok(horasPeriodoService.getHorasPeriodo(idHorasPeriodo));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

	/**
	 * DELETE /horas-periodos/:id : delete the "id" horasPeriodo.
	 *
	 * @param id
	 *            the id of the horasPeriodo to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/horas-periodo/{idHorasPeriodo}")
	@Timed
	public ResponseEntity<String> deleteHorasPeriodo(@PathVariable Long idHorasPeriodo) {
		log.debug("REST request to delete HorasPeriodo : {}", idHorasPeriodo);
		try {
			horasPeriodoService.deleteHorasPeriodo(idHorasPeriodo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idHorasPeriodo.toString())).build();
	}

	 
}

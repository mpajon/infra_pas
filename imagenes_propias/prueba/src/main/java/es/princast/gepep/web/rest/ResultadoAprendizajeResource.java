package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import es.princast.gepep.domain.ResultadoAprendizaje;
import es.princast.gepep.service.ResultadoAprendizajeService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing CapacidadTerminal.
 */
@RestController
@RequestMapping("/api")
public class ResultadoAprendizajeResource {

	private final Logger log = LoggerFactory.getLogger(ResultadoAprendizajeResource.class);

	private static final String ENTITY_NAME = "resultadoAprendizaje";

	@Autowired
	private ResultadoAprendizajeService resultadoService;


	/**
	 * POST /resultados-aprendizaje : Create a new resultado aprendizaje.
	 *
	 * @param capacidadTerminal
	 *            the capacidadTerminal to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         capacidadTerminal, or with status 400 (Bad Request) if the
	 *         capacidadTerminal has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/resultados-aprendizaje")
	@Timed
	public ResponseEntity<Object> createResultadoAprendizaje(
			@Valid @RequestBody ResultadoAprendizaje resultadoAprendizaje) throws URISyntaxException {
		log.debug("REST request to save Resultado Aprendizaje : {}", resultadoAprendizaje);
		try {
		    return ResponseEntity.ok(resultadoService.createResultadoAprendizaje(resultadoAprendizaje));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }
		
	}

	/**
	 * PUT /resultados-aprendizaje : Updates an existing capacidadTerminal.
	 *
	 * @param capacidadTerminal
	 *            the capacidadTerminal to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         capacidadTerminal, or with status 400 (Bad Request) if the
	 *         capacidadTerminal is not valid, or with status 500 (Internal Server
	 *         Error) if the capacidadTerminal couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/resultados-aprendizaje")
	@Timed
	public ResponseEntity<String> updateResultadoAprendizaje(
			@Valid @RequestBody ResultadoAprendizaje resultadoAprendizaje) throws URISyntaxException {
		log.debug("REST request to update Resultado Aprendizaje : {}", resultadoAprendizaje);
		try {
			resultadoService.updateResultadoAprendizaje(resultadoAprendizaje);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /resultados-aprendizaje : get all the capacidadTerminals.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         capacidadTerminals in body
	 */
	@GetMapping("/resultados-aprendizaje")
	@Timed
	public  ResponseEntity<List<ResultadoAprendizaje>> getAllResultadosAprendizaje() {
		log.debug("REST request to get all ResultadosAprendizaje");
		Iterable<ResultadoAprendizaje> listaResultados = resultadoService.getAllResultados();
		return ResponseEntity.ok((List<ResultadoAprendizaje>) listaResultados);
		
	}
	
	/**
	 * GET /resultados-aprendizaje : get all the resultados by ciclo.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         capacidadTerminals in body
	 */
	@GetMapping("/resultados-aprendizaje/ciclo/{ca_ciclo}")
	@Timed
	public ResultadoAprendizaje getResultadosByCiclo(@PathVariable(value = "ca_ciclo") String id,
			Pageable pageable) {
		log.debug("REST request to get all Resultados Aprendizaje by Ciclo");
		return resultadoService.getResultadosByCiclo(id);
	}

	/**
	 * GET /resultados-aprendizaje/:id : get the "id" capacidadTerminal.
	 *
	 * @param id
	 *            the id of the capacidadTerminal to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         capacidadTerminal, or with status 404 (Not Found)
	 */
	@GetMapping("/resultados-aprendizaje/{id}")
	@Timed
	public ResponseEntity<ResultadoAprendizaje> getResultadoAprendizaje(@PathVariable Long id) {
		log.debug("REST request to get Resultado Aprendizaje : {}", id);
		try {
			return ResponseEntity.ok(resultadoService.getResultadoAprendizaje(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

	/**
	 * DELETE /resultados-aprendizaje/:id : delete the "id" capacidadTerminal.
	 *
	 * @param id
	 *            the id of the capacidadTerminal to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/resultados-aprendizaje/{id}")
	@Timed
	public ResponseEntity<String> deleteResultadoAprendizaje(@PathVariable Long id) {
		log.debug("REST request to delete CapacidadTerminal : {}", id);
		try {
			resultadoService.deleteResultadoAprendizaje(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}

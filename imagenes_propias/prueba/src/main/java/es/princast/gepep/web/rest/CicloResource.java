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

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.service.CicloService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Ciclo.
 */
@RestController
@RequestMapping("/api")
public class CicloResource {

	private final Logger log = LoggerFactory.getLogger(CicloResource.class);

	private static final String ENTITY_NAME = "ciclo";

	@Autowired
	private CicloService cicloService;

	/**
	 * POST /ciclos : Create a new ciclo.
	 *
	 * @param ciclo
	 *            the ciclo to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         ciclo, or with status 400 (Bad Request) if the ciclo has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/ciclos")
	@Timed
	public ResponseEntity<Object> createCiclo(@Valid @RequestBody Ciclo ciclo) throws URISyntaxException {
		log.debug("REST request to save Ciclo : {}", ciclo);
		try {
		    return ResponseEntity.ok(cicloService.createCiclo(ciclo));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }  
	}

 
	/**
	 * PUT /ciclos : Updates an existing ciclo.
	 *
	 * @param ciclo
	 *            the ciclo to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         ciclo, or with status 400 (Bad Request) if the ciclo is not valid, or
	 *         with status 500 (Internal Server Error) if the ciclo couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/ciclos")
	@Timed
	public ResponseEntity<String> updateCiclo(@Valid @RequestBody Ciclo ciclo) throws URISyntaxException {
		log.debug("REST request to update Ciclo : {}", ciclo);
		try {
			
			if(ciclo.getDeSauce()) {
				cicloService.updateCicloSauce(ciclo);
			}else {
				cicloService.updateCiclo(ciclo);
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}


	/**
	 * GET /ciclos : get all the ciclos.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of ciclos in
	 *         body
	 */
	@GetMapping("/ciclos")
	@Timed
	public ResponseEntity<List<Ciclo>> getAllCiclos() {
		log.debug("REST request to get all Ciclos");
		Iterable<Ciclo> listaCiclos = cicloService.getAllCiclos();
        return ResponseEntity.ok((List<Ciclo>)listaCiclos);
	}
	@GetMapping("/ciclos/vigentes")
	@Timed
	public ResponseEntity<List<Ciclo>> getAllCiclosVigentes() {
		log.debug("REST request to get all Ciclos");
		Iterable<Ciclo> listaCiclos = cicloService.findAllByFechaBajaIsNull();
        return ResponseEntity.ok((List<Ciclo>)listaCiclos);
	}
	
	
	/**
	 * GET /ciclos/:id : get the "id" ciclo.
	 *
	 * @param id
	 *            the id of the ciclo to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the ciclo, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/ciclos/{id}")
	@Timed
	public ResponseEntity<Ciclo> getCiclo(@PathVariable String id) {
		log.debug("REST request to get Ciclo : {}", id);

		try {
			return ResponseEntity.ok(cicloService.getCicloAndContenidosEF(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
		
	}
	

	/**
	 * DELETE /ciclos/:id : delete the "id" ciclo.
	 *
	 * @param id
	 *            the id of the ciclo to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/ciclos/{id}")
	@Timed
	public ResponseEntity<String> deleteCiclo(@PathVariable String id) {
		log.debug("REST request to delete Ciclo : {}", id);
		try {
			cicloService.deleteCiclo(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();

	}

}

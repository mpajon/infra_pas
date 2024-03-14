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

import es.princast.gepep.domain.Centro;
import es.princast.gepep.service.CentroService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Centro.
 */
@RestController
@RequestMapping("/api")
public class CentroResource {

	private final Logger log = LoggerFactory.getLogger(CentroResource.class);

	private static final String ENTITY_NAME = "centro";

	@Autowired
	private CentroService centroService;

	/**
	 * POST /centros : Create a new centro.
	 *
	 * @param centro
	 *            the centro to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         centro, or with status 400 (Bad Request) if the centro has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/centros")
	@Timed
	public ResponseEntity<Object> createCentro(@Valid @RequestBody Centro centro) throws URISyntaxException {
		log.debug("REST request to save Centro : {}", centro);
		try {
		    return ResponseEntity.ok(centroService.createCentro(centro));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	  
	}

	/**
	 * PUT /centros : Updates an existing centro.
	 *
	 * @param centro
	 *            the centro to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         centro, or with status 400 (Bad Request) if the centro is not valid,
	 *         or with status 500 (Internal Server Error) if the centro couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/centros")
	@Timed
	public ResponseEntity<String> updateCentro(@Valid @RequestBody Centro centro) throws URISyntaxException {
		log.debug("REST request to update Centro : {}", centro);
		try {
			centroService.updateCentro(centro);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /centros : get all the centros.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of centros in
	 *         body
	 */
	@GetMapping("/centros")
	@Timed
	public ResponseEntity<List<Centro>> getAllCentros() {
		log.debug("REST request to get all Centros");
		Iterable<Centro> listaCentros = centroService.getAllCentros();
        return ResponseEntity.ok((List<Centro>)listaCentros);       
		
	}
	
	 /**
     * GET  /centros : get all thecentros sin cargar sus relaciones. Usado en la carga de los combos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of centros in body
     */
    @GetMapping("/centros/getCentroComboList/{anio}")
    @Timed
    public  ResponseEntity<List<Centro>> getTipoPracticaComboList(@PathVariable Integer anio) {
        log.debug("REST request to get all getCentroComboList");
        Iterable <Centro> listaCentros= centroService.getCentroComboList(anio);
        return ResponseEntity.ok((List<Centro>)listaCentros);
        }
	
	@GetMapping("/centros/activos")
	@Timed
	public ResponseEntity<List<Centro>> getAllCentrosActivos() {
		log.debug("REST request to get all Centros");
		Iterable<Centro> listaCentros = centroService.getAllCentrosActivos();
        return ResponseEntity.ok((List<Centro>)listaCentros);       
		
	}

	/**
	 * GET /centros/:id : get the "id" centro.
	 *
	 * @param id
	 *            the id of the centro to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the centro, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/centros/{id}")
	@Timed
	public ResponseEntity<Centro> getCentro(@PathVariable String id) {
		log.debug("REST request to get Centro : {}", id);

		try {
			return ResponseEntity.ok(centroService.getCentro(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /centros/:id : delete the "id" centro.
	 *
	 * @param id
	 *            the id of the centro to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/centros/{id}")
	@Timed
	public ResponseEntity<String> deleteCentro(@PathVariable String id) {
		log.debug("REST request to delete Centro : {}", id);

	        
		try {
			centroService.deleteCentro(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

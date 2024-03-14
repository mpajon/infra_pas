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

import es.princast.gepep.domain.Municipio;
import es.princast.gepep.service.MunicipioService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Municipio.
 */
@RestController
@RequestMapping("/api")
public class MunicipioResource {

	private final Logger log = LoggerFactory.getLogger(MunicipioResource.class);

	private static final String ENTITY_NAME = "municipio";

	@Autowired
	private MunicipioService municipioService;

	/**
	 * POST /municipios : Create a new municipio.
	 *
	 * @param municipio
	 *            the municipio to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         municipio, or with status 400 (Bad Request) if the municipio has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/municipios")
	@Timed
	public ResponseEntity<Object> createMunicipio(@Valid @RequestBody Municipio municipio)
			throws URISyntaxException {
		log.debug("REST request to save Municipio : {}", municipio);
		try {
		    return ResponseEntity.ok(municipioService.createMunicipio(municipio));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	}

	/**
	 * PUT /municipios : Updates an existing municipio.
	 *
	 * @param municipio
	 *            the municipio to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         municipio, or with status 400 (Bad Request) if the municipio is not
	 *         valid, or with status 500 (Internal Server Error) if the municipio
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/municipios")
	@Timed
	public ResponseEntity<String> updateMunicipio(@Valid @RequestBody Municipio municipio) throws URISyntaxException {
		log.debug("REST request to update Municipio : {}", municipio);
		try {
			municipioService.updateMunicipio(municipio);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /municipios : get all the municipios.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of municipios in
	 *         body
	 */
	@GetMapping("/municipios")
	@Timed
	public ResponseEntity<List<Municipio>> getAllMunicipios() {
		log.debug("REST request to get all Municipios");
		Iterable<Municipio> listaMunicipios = municipioService.getAllMunicipios();
		return ResponseEntity.ok((List<Municipio>) listaMunicipios);
	}

	/**
	 * GET /municipios/:id : get the "id" municipio.
	 *
	 * @param id
	 *            the id of the municipio to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the municipio,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/municipios/{id}")
	@Timed
	public ResponseEntity<Municipio> getMunicipio(@PathVariable Long id) {
		log.debug("REST request to get Municipio : {}", id);
		try {
            return ResponseEntity.ok(municipioService.getMunicipio(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }   
	}

	/**
	 * DELETE /municipios/:id : delete the "id" municipio.
	 *
	 * @param id
	 *            the id of the municipio to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/municipios/{id}")
	@Timed
	public ResponseEntity<String> deleteMunicipio(@PathVariable Long id) {
		log.debug("REST request to delete Municipio : {}", id);
		  try {
			  municipioService.deleteMunicipio(id);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
	                    .body(e.getMessage());
	        }

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();

	}
}

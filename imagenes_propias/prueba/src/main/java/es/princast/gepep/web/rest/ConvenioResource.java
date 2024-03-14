package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import es.princast.gepep.config.ConvenioProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
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

import es.princast.gepep.domain.Convenio;
import es.princast.gepep.service.ConvenioService;
import es.princast.gepep.web.rest.filter.ConvenioFilter;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.gepep.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Convenio.
 */
@RestController
@RequestMapping("/api")
public class ConvenioResource {

	private final Logger log = LoggerFactory.getLogger(ConvenioResource.class);

	private static final String ENTITY_NAME = "convenio";

	@Autowired
	private ConvenioService convenioService;

	/**
	 * POST /convenios : Create a new convenio.
	 *
	 * @param convenio
	 *            the convenio to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         convenio, or with status 400 (Bad Request) if the convenio has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/convenios")
	@Timed
	public ResponseEntity<Object> createConvenio(@Valid @RequestBody Convenio convenio) throws URISyntaxException {
		log.debug("REST request to save Convenio : {}", convenio);
		
		try {
		    return ResponseEntity.ok(convenioService.createConvenio(convenio));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }  
	}

	/**
	 * PUT /convenios : Updates an existing convenio.
	 *
	 * @param convenio
	 *            the convenio to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         convenio, or with status 400 (Bad Request) if the convenio is not
	 *         valid, or with status 500 (Internal Server Error) if the convenio
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/convenios")
	@Timed
	public ResponseEntity<String> updateConvenio(@Valid @RequestBody Convenio convenio) throws URISyntaxException {
		log.debug("REST request to update Convenio : {}", convenio);
		try {
			convenioService.updateConvenio(convenio);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /convenios : get all the convenios.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of convenios in
	 *         body
	 */
	//@GetMapping("/convenios")
//	@PostMapping("/convenios/paged")
//	@Timed
//	public ResponseEntity<List<Convenio>> getAllConvenios(@RequestBody Convenio partialMatch,Pageable pageable, boolean unpaged) {		
//		log.debug("REST request to get all Convenios");
//				   
//		Page<Convenio> page = convenioService.getAllConveniosByCriteria(partialMatch, PaginationUtil.generatePageableSortIgnoreCase(pageable, unpaged));
//		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/convenios/paged");
//		
//		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//
//		  
//	}
	
	@GetMapping("/convenios")
	@Timed
	public ResponseEntity<List<Convenio>> getAllConvenios(ConvenioFilter partialMatch, Pageable pageable, boolean unpaged) {		
		log.debug("REST request to get all Convenios");

		Page<Convenio> page = convenioService.getAllConveniosByCriteriaFilter(partialMatch, PaginationUtil.generatePageableSortIgnoreCase(pageable, unpaged));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/convenios");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}


	@PostMapping("/convenios/paged")
	@Timed
	public ResponseEntity<List<Convenio>> getAllConveniosFilter(@RequestBody ConvenioFilter partialMatch, Pageable pageable, boolean unpaged) {		
		log.debug("REST request to get all Convenios");

		Page<Convenio> page = convenioService.getAllConveniosByCriteriaFilter(partialMatch, PaginationUtil.generatePageableSortIgnoreCase(pageable, unpaged));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/convenios");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
            
	
	@GetMapping("/convenios/activos")
	@Timed
	public ResponseEntity<List<Convenio>> getAllConveniosByFechaBajaIsNull() {		
		log.debug("REST request to get all Convenios");
		Iterable<Convenio> listaConvenios = convenioService.getAllConveniosByFechaBajaIsNull();
        return ResponseEntity.ok((List<Convenio>)listaConvenios);      

	}
	
 
    @GetMapping("/convenios-page")
    @Timed
    public ResponseEntity<List<Convenio>> getAllConveniosPaginados(@PageableDefault(size = 99) Pageable pageable) {
        log.debug("REST request to get a page of Convenios");
        Page<Convenio> page = convenioService.getAllConveniosPaginados(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/convenios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
	/**
	 * GET /convenios/:id : get the "id" convenio.
	 *
	 * @param id
	 *            the id of the convenio to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the convenio,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/convenios/{id}")
	@Timed
	public ResponseEntity<Convenio> getConvenio(@PathVariable Long id) {
		log.debug("REST request to get Convenio : {}", id);
		try {
			return ResponseEntity.ok(convenioService.getConvenio(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}
	
	@GetMapping("/convenios/centro/{ca_centro}")
	@Timed
	public List<Convenio> getAllConveniosByCentro(@PathVariable(value = "ca_centro") String id) {
		log.debug("REST request to get all Convenios by Centro");
		return convenioService.getAllConveniosByCentro(id);

	}

	@GetMapping("/convenios/centro/{ca_centro}/practica/{cn_tipo_practica}")
	@Timed
	public List<Convenio> getAllConveniosByCentroAndTipoPractica(@PathVariable(value = "ca_centro") String id,@PathVariable(value = "cn_tipo_practica") Long idTipoPractica) {
		log.debug("REST request to get all Convenios by Centro and TipoPractica");
		return convenioService.getAllConveniosByCentroAndTipoPractica(id,idTipoPractica);

	}

	
	@GetMapping("/convenios/area/{cn_area}")
	@Timed
	public List<Convenio> getAllConveniosByArea(@PathVariable(value = "cn_area") Long id) {
		log.debug("REST request to get all Convenios by Centro");
		return convenioService.getAllConveniosByArea(id);

	}

	
	@GetMapping("/convenios/validados/centro/{ca_centro}/{idTipoPractica}")
	@Timed
	public List<Convenio> getAllConveniosByCentroValidado(@PathVariable(value = "ca_centro") String id, @PathVariable(value = "idTipoPractica") Long idTipoPractica) {
		log.debug("REST request to get all Convenios by Centro");
		return convenioService.getAllConveniosByCentroAndPracticaValidado(id,idTipoPractica);

	}

    
	/**
	 * DELETE /convenios/:id : delete the "id" convenio.
	 *
	 * @param id
	 *            the id of the convenio to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/convenios/{id}")
	@Timed
	public ResponseEntity<String> deleteConvenio(@PathVariable Long id) {
		log.debug("REST request to delete Convenio : {}", id);

		try {
			convenioService.deleteConvenio(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

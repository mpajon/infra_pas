package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

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

import es.princast.gepep.domain.TextosDocumento;
import es.princast.gepep.service.TextosDocumentoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing TextosDocumento.
 */
@RestController
@RequestMapping("/api")
public class TextosDocumentoResource {

	private final Logger log = LoggerFactory.getLogger(TextosDocumentoResource.class);

	private static final String ENTITY_NAME = "textosDocumentacion";
    
	@Autowired
	private TextosDocumentoService textosDocumentoService;


	/**
	 * POST /textos-documentacion : Create a new textosDocumentacion.
	 *
	 * @param textosDocumento
	 *            the textosDocumentacion to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         textosDocumentacion, or with status 400 (Bad Request) if the
	 *         textosDocumentacion has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/textos-documentacion")
	@Timed
	public ResponseEntity<Object> createTextosDocumentacion(@RequestBody TextosDocumento textosDocumento)
			throws URISyntaxException {
		log.debug("REST request to save TextosDocumento : {}", textosDocumento);
		try {
		    return ResponseEntity.ok(textosDocumentoService.createTextosDocumento(textosDocumento));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }
	}

	/**
	 * PUT /textos-documentacion : Updates an existing textosDocumentacion.
	 *
	 * @param textosDocumento
	 *            the textosDocumentacion to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         textosDocumentacion, or with status 400 (Bad Request) if the
	 *         textosDocumentacion is not valid, or with status 500 (Internal Server
	 *         Error) if the textosDocumentacion couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/textos-documentacion")
	@Timed
	public ResponseEntity<String> updateTextosDocumentacion(@RequestBody TextosDocumento textosDocumento) throws URISyntaxException {
		
		textosDocumentoService.updateTextosDocumento(textosDocumento);	

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	/**
	 * GET /textos-documentacion : get all the textosDocumentacions.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         textosDocumentacions in body
	 */
	@GetMapping("/textos-documentacion")
	@Timed
	public ResponseEntity<List<TextosDocumento>> getAllTextosDocumentacion() {
		log.debug("REST request to get all TextosDocumento");
		Iterable<TextosDocumento> listaTextos = textosDocumentoService.getAllTextosDocumento();
		return ResponseEntity.ok((List<TextosDocumento>) listaTextos);

	}

	/**
	 * GET /textos-documentacions/:id : get the "id" textosDocumentacion.
	 *
	 * @param id
	 *            the id of the textosDocumentacion to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         textosDocumentacion, or with status 404 (Not Found)
	 */
	@GetMapping("/textos-documentacion/{id}")
	@Timed
	public ResponseEntity<TextosDocumento> getTextosDocumentacion(@PathVariable Long id) {
		log.debug("REST request to get TextosDocumento : {}", id);
		try {
			return ResponseEntity.ok(textosDocumentoService.getTextosDocumento(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /textos-documentacion/:id : delete the "id" textosDocumentacion.
	 *
	 * @param id
	 *            the id of the textosDocumentacion to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/textos-documentacion/{id}")
	@Timed
	public ResponseEntity<String> deleteTextosDocumentacion(@PathVariable Long id) {
		log.debug("REST request to delete TextosDocumento : {}", id);
		try {
			textosDocumentoService.deleteTextosDocumento(id);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
	
	 /**
     * GET  /periodos-practicas/:id : get the "id" periodoPractica.
     * @param id the id of the periodoPractica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodoPractica, or with status 404 (Not Found)
     */
    @GetMapping("/documentos/{idDocumento}/textos-documentacion")
    @Timed
    public Iterable<TextosDocumento> getAllTextos(@PathVariable (value="idDocumento") Long idDocumento) {
        log.debug("REST request to get TextosDocumento : {}", idDocumento);
        return  textosDocumentoService.getAllTextosDocumentoByDocumento(idDocumento); 
    }
   
    
}

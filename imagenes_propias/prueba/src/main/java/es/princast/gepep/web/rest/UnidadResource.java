package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import es.princast.gepep.domain.Unidad;
import es.princast.gepep.service.UnidadService;
import es.princast.gepep.web.rest.filter.UnidadFilter;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.gepep.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Unidad.
 */
@RestController
@RequestMapping("/api")
public class UnidadResource {

	private final Logger log = LoggerFactory.getLogger(UnidadResource.class);

    private static final String ENTITY_NAME = "unidad";

    @Autowired
    private UnidadService unidadService;
    
    /**
     * POST  /unidades : Create a new unidad.
     *
     * @param unidad the unidad to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unidad, or with status 400 (Bad Request) if the unidad has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/unidades")
    @Timed
    public ResponseEntity<Object> createUnidad(@Valid @RequestBody Unidad unidad) throws URISyntaxException {
        log.debug("REST request to save Unidad : {}", unidad);
        
        try {
		    return ResponseEntity.ok(unidadService.createUnidad(unidad));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }
         
    }
    
    
    /**
     * PUT  /unidades : Updates an existing unidad.
     *
     * @param unidad the unidad to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unidad,
     * or with status 400 (Bad Request) if the unidad is not valid,
     * or with status 500 (Internal Server Error) if the unidad couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/unidades")
    @Timed
	public ResponseEntity<String> updateUnidad(@Valid @RequestBody Unidad unidad) throws URISyntaxException {
		log.debug("REST request to update Unidad : {}", unidad);
		try {
			unidadService.updateUnidad(unidad);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

     
    
	@GetMapping("/unidades/ofertaCentro/{anio}/{idOfertaCentro}")
	@Timed
	public ResponseEntity<List<Unidad>> getAllUnidadesByOfertaCentro(@PathVariable(value = "anio") Integer anio,
			@PathVariable(value = "idOfertaCentro") String idOfertaCentro) {
		log.debug("REST request to get all Unidades by OfertaCentro");
		Iterable<Unidad> listaUnidades = unidadService.getAllUnidadesByOfertaCentro(idOfertaCentro, anio);
		return ResponseEntity.ok((List<Unidad>) listaUnidades);

	}
	
	 
		@GetMapping("/unidades/ofertaCentro/{anio}/{idOfertaCentro}/{nombre}")
		@Timed
		public ResponseEntity<List<Unidad>> getAllUnidadesByOfertaCentroAndNombre(@PathVariable(value = "anio") Integer anio,
				@PathVariable(value = "idOfertaCentro") String idOfertaCentro, @PathVariable(value = "nombre") String nombre) {
			log.debug("REST request to get all Unidades by OfertaCentro");
			Iterable<Unidad> listaUnidades = unidadService.getAllUnidadesByOfertaCentroAndNombre(idOfertaCentro, anio,nombre);
			return ResponseEntity.ok((List<Unidad>) listaUnidades);

		}
	
	@GetMapping("/unidades/tutor/{anio}/{idTutor}")
	@Timed
	public ResponseEntity<List<Unidad>> getAllUnidadesByTutorAndAnio(@PathVariable(value = "anio") Integer anio, @PathVariable(value = "idTutor") String idTutor) {
		log.debug("REST request to get all Unidades by tutor");
		Iterable<Unidad> listaUnidades = unidadService.getAllUnidadesByTutorAndAnio(idTutor, anio);
		return ResponseEntity.ok((List<Unidad>) listaUnidades);

	}
	
//	@GetMapping("/unidades")
//	@Timed
//	public ResponseEntity<List<Unidad>> getAllUnidades() {
//		log.debug("REST request to get all Unidades");
//		Iterable<Unidad> listaUnidades = unidadService.getAllUnidades();
//		return ResponseEntity.ok((List<Unidad>) listaUnidades);
//
//	}
	
//	/**
//     * get  /unidades/paged : get all the alumnos.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of alumnos in body
//     */   
//	@GetMapping("/unidades/paged")
//    @Timed
//    public ResponseEntity<List<Unidad>> getAllUnidadesPaged(@RequestBody UnidadFilter partialMatch, Pageable pageable, boolean unpaged) {
//        log.debug("REST request to get all Unidades");
//        
//        Page<Unidad> page = unidadService.getAllUnidadesByCriteria(partialMatch, PaginationUtil.generatePageableSortIgnoreCase(pageable,unpaged));
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unidades/paged");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//           
//    }
	
	/**
	 * GET /convenios : get all the convenios.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of convenios in
	 *         body
	 */
	@GetMapping("/unidades")
	@Timed
	public ResponseEntity<List<Unidad>> getAllUnidadesPaged(UnidadFilter partialMatch, Pageable pageable, boolean unpaged) {		
		log.debug("REST request to get all Unidades");

		try {
		Page<Unidad> page = unidadService.getAllUnidadesByCriteria(partialMatch, PaginationUtil.generatePageableSortIgnoreCase(pageable, unpaged));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unidades");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		
		}catch (Exception ex) {
			Iterable<Unidad> listaUnidades = new ArrayList<Unidad>();
			return ResponseEntity.ok((List<Unidad>) listaUnidades);			
		}
		
	}

	
	@GetMapping("/unidades/tutor/{idTutor}")
	@Timed
	public ResponseEntity<List<Unidad>> getAllUnidadesByTutor(@PathVariable(value = "idTutor") String idTutor) {
		log.debug("REST request to get all Unidades by tutor");
		Iterable<Unidad> listaUnidades = unidadService.getAllUnidadesByTutor(idTutor);
		return ResponseEntity.ok((List<Unidad>) listaUnidades);

	}

    /**
     * GET  /unidades/:id : get the "id" unidad.
     *
     * @param id the id of the unidad to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unidad, or with status 404 (Not Found)
     */
    @GetMapping("/unidades/{id}")
    @Timed
    public ResponseEntity<Unidad> getUnidad(@PathVariable String id) {
        log.debug("REST request to get Unidad : {}", id);
   	 
  	  try {
              return ResponseEntity.ok(unidadService.getUnidad(id));
          } catch (IllegalArgumentException e) {
              return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
          }   
  	
     
    }

    /**
     * DELETE  /unidades/:id : delete the "id" unidad.
     *
     * @param id the id of the unidad to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/unidades/{id}")
    @Timed
    public ResponseEntity<String> deleteUnidad(@PathVariable String id) {
        log.debug("REST request to delete Unidad : {}", id);
        
        try {        	
        	unidadService.deleteUnidad(id);        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
 
}

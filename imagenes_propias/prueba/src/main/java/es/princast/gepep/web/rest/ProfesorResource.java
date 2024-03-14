package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
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

import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.ProfesorCentro;
import es.princast.gepep.domain.TutorCentro;
import es.princast.gepep.domain.Unidad;
import es.princast.gepep.service.ProfesorService;
import es.princast.gepep.service.UnidadService;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.gepep.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Profesor.
 */
@RestController
@RequestMapping("/api")
public class ProfesorResource {

	private final Logger log = LoggerFactory.getLogger(ProfesorResource.class);

	private static final String ENTITY_NAME = "profesor";

	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
    private UnidadService unidadService;
    


	/**
	 * POST /profesores : Create a new profesor.
	 *
	 * @param profesor
	 *            the profesor to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         profesor, or with status 400 (Bad Request) if the profesor has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/profesores")
	@Timed
	public ResponseEntity<Object> createProfesor(@Valid @RequestBody Profesor profesor) throws URISyntaxException {
		log.debug("REST request to save Profesor : {}", profesor);
		try {
		    return ResponseEntity.ok(profesorService.createProfesor(profesor));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }
	}

	/**
	 * PUT /profesores : Updates an existing profesor.
	 *
	 * @param profesor
	 *            the profesor to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         profesor, or with status 400 (Bad Request) if the profesor is not
	 *         valid, or with status 500 (Internal Server Error) if the profesor
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/profesores")
	@Timed
	public ResponseEntity<String> updateProfesor(@Valid @RequestBody Profesor profesor) throws URISyntaxException {
		log.debug("REST request to update Profesor : {}", profesor);
		try {
			profesorService.updateProfesor(profesor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	
	/**
	 * GET /profesores/paged : get all the profesors paged
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of profesors in
	 *         body
	 */
	@PostMapping("/profesores/paged")
	@Timed
	public ResponseEntity<List<Profesor>> getAllProfesoresPaged(@RequestBody Profesor partialMatch, Pageable pageable, boolean unpaged) {
		log.debug("REST request to get all Profesores");
		
		 Page<Profesor> page = profesorService.getAllProfesoresByCriteria(partialMatch, PaginationUtil.generatePageableSortIgnoreCase(pageable,unpaged));
	     HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profesores/paged");
	     return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}
	
	
	/**
	 * GET /profesores : get all the profesors.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of profesors in
	 *         body
	 */
	@GetMapping("/profesores")
	@Timed
	public ResponseEntity<List<Profesor>> getAllProfesores() {
		log.debug("REST request to get all Profesores");
		 Page<Profesor> page = profesorService.getAllProfesores(Pageable.unpaged());
	     return new ResponseEntity<>(page.getContent(), HttpStatus.OK);

	}

	
	@GetMapping("/profesores/{centro}/{anio}")
	@Timed
	public ResponseEntity<List<TutorCentro>> getTutoresByCentroAndAnio(@PathVariable String centro,@PathVariable Integer anio ){
		log.debug("REST request to get all Tutores centro");
		Iterable<TutorCentro> listaTutores = profesorService.getTutoresByCentroAndAnio(centro, anio);
		return ResponseEntity.ok((List<TutorCentro>) listaTutores);

	}
	
	@GetMapping("/profesores/{centro}/{anio}/{idTipoPractica}")
	@Timed
	public ResponseEntity<List<TutorCentro>> getTutoresByCentroAndAnioAndTipoPractica(@PathVariable String centro,@PathVariable Integer anio, @PathVariable Long idTipoPractica ){
		log.debug("REST request to get all Tutores centro");
		Iterable<TutorCentro> listaTutores = profesorService.getTutoresByCentroAndAnioAndTipoPractica(centro, anio, idTipoPractica);
		return ResponseEntity.ok((List<TutorCentro>) listaTutores);

	}
	
	/**
	 * Sólo devuelve datos del profeosor (id, nombre y apellidos)
	 * @param centro id del centro
	 * @param anio Año
	 * @return datos del profesor
	 */
	@GetMapping("/profesores/lite/{centro}/{anio}")
	@Timed
	public ResponseEntity<List<TutorCentro>> getProfesoresCentroAndAnioLite(@PathVariable String centro,@PathVariable Integer anio ){
		log.debug("REST request to get all Profesores");
		Iterable<TutorCentro> listaProfesores = profesorService.getProfesoresByCentroAndAnioLite(centro, anio);
		return ResponseEntity.ok((List<TutorCentro>) listaProfesores);

	}
	
	/**
	 * Sólo devuelve datos del profesor
	 * @param centro id del centro
	 * @return datos del profesor
	 */
	@GetMapping("/profesores/centro/{centro}")
	@Timed
	public ResponseEntity<List<Profesor>> getProfesoresCentro(@PathVariable String centro){
		log.debug("REST request to get all Profesores");
		Iterable<Profesor> listaProfesores = profesorService.getProfesoresByCentro(centro);
		return ResponseEntity.ok((List<Profesor>) listaProfesores);

	}
	
	/**
	 * Sólo devuelve datos del profesor
	 * @param centro id del centro
	 * @return datos del profesor
	 */
	@GetMapping("/profesores/centro/{centro}/{anio}")
	@Timed
	public ResponseEntity<List<Profesor>> getProfesoresCentroAndAnio(@PathVariable String centro, @PathVariable Integer anio){
		log.debug("REST request to get all Profesores por centro y año");
		Iterable<Profesor> listaProfesores = profesorService.getProfesoresByCentroAndAnio(centro, anio);
		return ResponseEntity.ok((List<Profesor>) listaProfesores);

	}
	
	@GetMapping("/profesores/usuario/{nif}")
	@Timed
	public ResponseEntity<List<TutorCentro>> getTutorByUsuario(@PathVariable String nif){
		log.debug("REST request to get all Profesores");
		Iterable<TutorCentro> listaProfesores = profesorService.findTutorByUsuario(nif);
		return ResponseEntity.ok((List<TutorCentro>) listaProfesores);

	}
	
	@GetMapping("/profesores/usuario/anio/{nif}/{anio}")
	@Timed
	public ResponseEntity<List<TutorCentro>> getTutorByUsuarioAndAnio(@PathVariable String nif,@PathVariable Integer anio){
		log.debug("REST request to get all Profesores");
		Iterable<TutorCentro> listaProfesores = profesorService.findTutorByUsuarioAndAnio(nif,anio);
		return ResponseEntity.ok((List<TutorCentro>) listaProfesores);

	}
	
	@GetMapping("/profesores/usuario/{nif}/{anio}/{centro}")
	@Timed
	public ResponseEntity<List<TutorCentro>> getTutorByUsuarioAndAnioAndCentro(@PathVariable String nif, @PathVariable String centro,@PathVariable Integer anio){
		log.debug("REST request to get all Profesores");
		Iterable<TutorCentro> listaProfesores = profesorService.findTutorByUsuarioAndAnioAndCentro(nif, centro, anio);
		return ResponseEntity.ok((List<TutorCentro>) listaProfesores);

	}
	
	@GetMapping("/profesores/usuario/{nif}/{anio}/{centro}/{idTipoPractica}")
	@Timed
	public ResponseEntity<List<TutorCentro>> getTutorByUsuarioAndAnioAndCentroAndTipoPractica(@PathVariable String nif, @PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idTipoPractica){
		log.debug("REST request to get all Profesores");
		Iterable<TutorCentro> listaProfesores = profesorService.findTutorByUsuarioAndAnioAndCentroAndTipoPractica(nif, centro, anio, idTipoPractica);
		return ResponseEntity.ok((List<TutorCentro>) listaProfesores);

	}
	
	
	/**
	 * GET /profesores/:id : get the "id" profesor.
	 *
	 * @param id
	 *            the id of the profesor to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the profesor,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/profesores/{id}")
	@Timed
	public ResponseEntity<Profesor> getProfesor(@PathVariable String id) {
		log.debug("REST request to get Profesor : {}", id);

		try {
			return ResponseEntity.ok(profesorService.getProfesor(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}
	
	

	/**
	 * DELETE /profesores/:id : delete the "id" profesor.
	 *
	 * @param id
	 *            the id of the profesor to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/profesores/{id}")
	@Timed
	public ResponseEntity<String> deleteProfesor(@PathVariable String id) {
		log.debug("REST request to delete Profesor : {}", id);
		try {
        	profesorService.deleteProfesor(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
	
    @GetMapping("/profesores/profesorCentro/{idCentro}/{curso}/{idTipoPractica}")
    public ResponseEntity<List<ProfesorCentro>> getListaProfesorByCentroByTipoPractica (@PathVariable Integer curso,@PathVariable String idCentro, @PathVariable Integer idTipoPractica){

        Iterable<ProfesorCentro> listado= profesorService.getListaProfesorByCentroByTipoPractica( curso, idCentro, idTipoPractica);
        return ResponseEntity.ok((List<ProfesorCentro>)listado);
    }
    
    @GetMapping("/profesores/profesorCentro/{idCentro}/{curso}/{idTipoPractica}/{tutor}")
    public ResponseEntity<List<ProfesorCentro>> getListaProfesorByCentroByTipoPracticaByTutor (@PathVariable Integer curso,@PathVariable String idCentro, @PathVariable Integer idTipoPractica, @PathVariable String tutor){

        Iterable<ProfesorCentro> listado= profesorService.getListaProfesorByCentroByTipoPracticaByTutor( curso, idCentro, idTipoPractica, tutor);
        return ResponseEntity.ok((List<ProfesorCentro>)listado);
    }
    
    
    /**
     * Obtiene las unidades con tutor
     * @param anio
     * @param idTutor
     * @return
     */
    @GetMapping("/profesores/tutorias")
	@Timed
	public ResponseEntity<List<Unidad>> findAllProfesoradoTutorias() {
		log.debug("REST request to get all Unidades con tutor");
		Iterable<Unidad> listaUnidades = unidadService.findAllUnidadesConTutor();
		return ResponseEntity.ok((List<Unidad>) listaUnidades);

	}
}

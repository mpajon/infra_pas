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

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.AlumnoExportSauce;
import es.princast.gepep.service.AlumnoService;
import es.princast.gepep.web.rest.filter.AlumnoFilter;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.gepep.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Alumno.
 */
@RestController
@RequestMapping("/api")
public class AlumnoResource {

    private final Logger log = LoggerFactory.getLogger(AlumnoResource.class);

    private static final String ENTITY_NAME = "alumno";

    
    @Autowired
    private AlumnoService alumnoService;

    
    /**
     * POST  /alumnos : Create a new alumno.
     *
     * @param alumno the alumno to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alumno, or with status 400 (Bad Request) if the alumno has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alumnos")
    @Timed
    public ResponseEntity<Alumno> createAlumno(@Valid @RequestBody Alumno alumno) throws URISyntaxException {
        log.debug("REST request to save Alumno : {}", alumno);
        return  alumnoService.createAlumno(alumno);
         
    }

    /**
     * PUT  /alumnos : Updates an existing alumno.
     *
     * @param alumno the alumno to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alumno,
     * or with status 400 (Bad Request) if the alumno is not valid,
     * or with status 500 (Internal Server Error) if the alumno couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alumnos")
    @Timed
    public ResponseEntity<String> updateAlumno(@Valid @RequestBody Alumno alumno) throws URISyntaxException {
        log.debug("REST request to update Alumno : {}", alumno);
        try {
            alumnoService.updateAlumno(alumno);
        } catch (IllegalArgumentException e) {
      	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * POST  /alumnos/paged : get all the alumnos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of alumnos in body
     */   
    @PostMapping("/alumnos/paged")
    @Timed
    public ResponseEntity<List<Alumno>> getAllAlumnosPaged(@RequestBody AlumnoFilter partialMatch, Pageable pageable, boolean unpaged) {
        log.debug("REST request to get all Alumnos");     
        
        Page<Alumno> page = alumnoService.getAllAlumnosByCriteria(partialMatch, PaginationUtil.generatePageableSortIgnoreCase(pageable,unpaged));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alumnos/paged");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
           
    }
    
    /**
     * GET  /alumnos : get all the alumnos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of alumnos in body
     */   
    @GetMapping("/alumnos")
    @Timed
    public ResponseEntity<List<Alumno>> getAllAlumnos() {
        log.debug("REST request to get all Alumnos");     
  
        Page<Alumno> page = alumnoService.getAllAlumnos(Pageable.unpaged());
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
           
    }
    
       
    /**
     * GET  /alumnos/:id : get the "id" alumno.
     *
     * @param id the id of the alumno to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alumno, or with status 404 (Not Found)
     */
    @GetMapping("/alumnos/{id}")
    @Timed
    public ResponseEntity<Alumno> getAlumno(@PathVariable String id) {
        log.debug("REST request to get Alumno : {}", id);       
        try {
        return ResponseEntity.ok(alumnoService.getAlumno(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }        
    }
    
    @GetMapping("/alumnos/{nif}/nif")
    @Timed
    public ResponseEntity<Alumno> getAlumnoByNIF(@PathVariable String nif) {
        log.debug("REST request to get Alumno : {}", nif);       
        try {
        return ResponseEntity.ok(alumnoService.getAlumnoByNif(nif));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }        
    }

    /**
     * DELETE  /alumnos/:id : delete the "id" alumno.
     *
     * @param id the id of the alumno to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alumnos/{id}")
    @Timed
    public ResponseEntity<String> deleteAlumno(@PathVariable String id) {
        log.debug("REST request to delete Alumno : {}", id);
        
        try {
        	alumnoService.deleteAlumno(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
       // return ResponseEntity.accepted().build();
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

   
    
    
    /**
	 * Obtiene un listado con datos de centros, alumnos y distribuiciones para el export de alumnos sauce
	 * @param anio
	 * @return
	 */
	@GetMapping("/alumnos/getAlumnosExportSauce/{anio}")
	@Timed
	public ResponseEntity<List<AlumnoExportSauce>> getAlumnosExportSauce(@PathVariable Integer anio) {
		log.debug("REST request to getAlumnosExportSauce");
		Iterable<AlumnoExportSauce> lista = alumnoService.getAlumnosExportSauce(anio);
        return ResponseEntity.ok((List<AlumnoExportSauce>)lista);       
		
	}
    

}

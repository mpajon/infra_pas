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

import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.service.CursoAcademicoService;

/**
 * REST controller for managing CursoAcademico.
 */
@RestController
@RequestMapping("/api")
public class CursoAcademicoResource {

    private final Logger log = LoggerFactory.getLogger(CursoAcademicoResource.class);


    @Autowired
    private CursoAcademicoService cursoService;
    /**
     * POST  /cursos : Create a nuevo curso academico
     *
     * @param curso the curso to create
     * @return the ResponseEntity with status 201 (Created) and with body the new curso, or with status 400 (Bad Request) if the curso has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cursos")
    @Timed
    public ResponseEntity<Object> createCursoAcademico(@Valid @RequestBody CursoAcademico curso) throws URISyntaxException {
    	   log.debug("REST request to create CursoAcademico");    	  
    	   try {
   		    return ResponseEntity.ok(cursoService.createCursoAcademico(curso));
   	   }
   	   catch (IllegalArgumentException e) {
   		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
          } 
    }

    /**
     * PUT  /cursos : Updates an existing curso.
     *
     * @param curso the curso to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cursos,
     * or with status 400 (Bad Request) if the curso is not valid,
     * or with status 500 (Internal Server Error) if the curso couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cursos")
    @Timed
    public ResponseEntity<String> updateCursoAcademico(@Valid @RequestBody CursoAcademico cursoModificado) throws URISyntaxException {
    	  log.debug("REST request to update CursoAcademico");    	
     cursoService.updateCursoAcademico(cursoModificado);
     return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    
    }

    
    /**
     * GET  /cursos : get all the cursos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cursos in body
     */
    @GetMapping("/cursos")
    @Timed
    public ResponseEntity<List<CursoAcademico>> getAllCursos() {
        log.debug("REST request to get all Cursos");
        Iterable<CursoAcademico> listaCursos= cursoService.getAllCursosAcademicos();
        return ResponseEntity.ok((List<CursoAcademico>)listaCursos);
        }


    /**
     * GET  /cursos/:id : get the "id" curso.
     *
     * @param id the id of the curso to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the curso, or with status 404 (Not Found)
     */
    @GetMapping("/cursos/{idAnio}")
    @Timed
    public ResponseEntity<CursoAcademico> getCursoAcademico(@PathVariable Integer idAnio) {
        log.debug("REST request to get Curso Academico : {}", idAnio);
        try {
        	 return ResponseEntity.ok(cursoService.getCursoAcademico(idAnio));        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }    
    }
    
    /**
     * GET  /cursos/actual
     *     * 
     * @return the ResponseEntity with status 200 (OK) and with body the curso, or with status 404 (Not Found)
     */
    @GetMapping("/cursos/actual")
    @Timed
    public ResponseEntity<CursoAcademico> getActual () {
        log.debug("REST request to get Curso Academico Actual");
        try {
        	 return ResponseEntity.ok(cursoService.getCursoActual());        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }    
    }
    

    

    /**
     * DELETE  /cursos/:id : delete the "id" curso.
     *
     * @param id the id of the curso to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cursos/{idAnio}")
    @Timed
    public ResponseEntity<String> deleteCursoAcademico(@PathVariable Integer idAnio) {
        log.debug("REST request to delete Curso Academico : {}", idAnio);
        try {
        	cursoService.deleteCursoAcademico(idAnio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

}

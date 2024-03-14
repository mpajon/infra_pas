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

import es.princast.gepep.domain.AlumAnexov;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.OfertaMatric;
import es.princast.gepep.service.MatriculaService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Matricula.
 */
@RestController
@RequestMapping("/api")
public class MatriculaResource {

    private final Logger log = LoggerFactory.getLogger(MatriculaResource.class);

    private static final String ENTITY_NAME = "matricula";

    
    @Autowired
    private MatriculaService matriculaService;

    /**
     * POST  /matriculas : Create a new matricula.
     *
     * @param matricula the matricula to create
     * @return the ResponseEntity with status 201 (Created) and with body the new matricula, or with status 400 (Bad Request) if the matricula has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/matriculas")
    @Timed
    public ResponseEntity<Object> createMatricula(@Valid @RequestBody Matricula matricula) throws URISyntaxException {
        log.debug("REST request to save Matricula : {}", matricula);
        try {
		    return ResponseEntity.ok( matriculaService.createMatricula(matricula));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	   
    }

    /**
     * PUT  /matriculas : Updates an existing matricula.
     *
     * @param matricula the matricula to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated matricula,
     * or with status 400 (Bad Request) if the matricula is not valid,
     * or with status 500 (Internal Server Error) if the matricula couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/matriculas")
    @Timed
    public ResponseEntity<String> updateMatricula(@Valid @RequestBody Matricula matricula) throws URISyntaxException {
        log.debug("REST request to update Matricula : {}", matricula);
        try {
            matriculaService.updateMatricula(matricula);
        } catch (IllegalArgumentException e) {
      	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /matriculas : get all the matriculas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of matriculas in body
     */
    @GetMapping("/matriculas")
    @Timed
    public ResponseEntity<List<Matricula>> getAllMatriculas() {
        log.debug("REST request to get all Matriculas");
        Iterable<Matricula> listaMatriculas = matriculaService.getAllMatriculas();
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        }  
    
    /**
     * GET  /matriculas/{idAlumno} : get all the matriculas de un alumno
     *
     * @return the ResponseEntity with status 200 (OK) and the list of matriculas in body
     */
    @GetMapping("/matriculas/alumno/{idAlumno}")
    @Timed
    public ResponseEntity<List<Matricula>> findMatriculasByAlumno(@PathVariable String idAlumno) {
        log.debug("REST request to get findMatriculasByAlumno");
        Iterable<Matricula> listaMatriculas = matriculaService.findByAlumno(idAlumno);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        }  
    
    
    @GetMapping("/matriculas/alumno/{idAlumno}/anio/{anio}")
    @Timed
    public ResponseEntity<List<Matricula>> findMatriculasByAlumnoAndAnio(@PathVariable String idAlumno, @PathVariable Integer anio) {
        log.debug("REST request to get findMatriculasByAlumnoAndAnio");
        Iterable<Matricula> listaMatriculas = matriculaService.findByAlumnoAndAnio(idAlumno, anio);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        }  

    @GetMapping("/matriculas/alumno/{idAlumno}/anio/{anio}/centro/{idCentro}")
    @Timed
    public ResponseEntity<List<Matricula>> findMatriculasByAlumnoAndAnio(@PathVariable String idAlumno, @PathVariable Integer anio,@PathVariable String idCentro) {
        log.debug("REST request to get findMatriculasByAlumnoAndAnioAndCentro");
        Iterable<Matricula> listaMatriculas = matriculaService.findByAlumnoAndAnioAndCentro(idAlumno, anio,idCentro);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        }  
    
    
    @GetMapping("/matriculas/ofercen/{idOfertaCentro}")
    @Timed
    public ResponseEntity<List<Matricula>> getAllMatriculasByOfertaCentro(@PathVariable String idOfertaCentro) {
        log.debug("REST request to get all Matriculas by OfertaCentro");
        
        Iterable<Matricula> listaMatriculas = matriculaService.getAllByOfertasCentro(idOfertaCentro);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        }     
 

    @GetMapping("/matriculas/{idOferCen}/{anio}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndAnio(@PathVariable String idOferCen,@PathVariable Integer anio) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");
        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndAnio(idOferCen,anio);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    @GetMapping("/matriculas/unidad/{idOferCen}/{idUnidad}/{anio}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndUnidadAndAnio(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");
        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndUnidadAndAnio(idOferCen,idUnidad,anio);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    
    @GetMapping("/matriculas/tutor/unidad/{idOferCen}/{idUnidad}/{anio}/{tutor}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndUnidadAndAnioAndTutor(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio,@PathVariable String tutor) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");
        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndUnidadAndAnioAndTutor(idOferCen,idUnidad,anio,tutor);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    
    @GetMapping("/matriculas/distribuidas/unidad/{idOferCen}/{idUnidad}/{anio}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndAnioDistribuidas(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndUnidadAndAnioDistribuidas(idOferCen,idUnidad,anio);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    
    
    @GetMapping("/matriculas/noDistribuidas/unidad/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndAnioAndPeriodoNoDistribuidas(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio, @PathVariable Long idPeriodoPractica) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoNoDistribuidas(idOferCen,idUnidad,anio,idPeriodoPractica);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    
    @GetMapping("/matriculas/distribuidas/seguimientos/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasSeguimientosByOfertaCentroAndAnioAndPeriodoDistribuidas(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio, @PathVariable Long idPeriodoPractica) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasSeguimientosByOfertaCentroAndUnidadAndAnioAndPeriodoDistribuidas(idOferCen,idUnidad,anio, idPeriodoPractica);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    @GetMapping("/matriculas/distribuidas/tutor/{idOferCen}/{anio}/{tutor}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndAnioAndTutorDistribuidas(@PathVariable String idOferCen,@PathVariable Integer anio,@PathVariable String tutor) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndAnioAndTutorDistribuidas(idOferCen,anio,tutor);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    
    
    @GetMapping("/matriculas/distribuidas/tutor/unidad/{idOferCen}/{idUnidad}/{anio}/{tutor}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndUnidadAndAnioAndTutorDistribuidas(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio,@PathVariable String tutor) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndUnidadAndAnioAndTutorDistribuidas(idOferCen,idUnidad,anio,tutor);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    
    @GetMapping("/matriculas/distribuidas/tutor/seguimientos/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}/{tutor}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasSeguimientosByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorDistribuidas(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio,@PathVariable Long idPeriodoPractica, @PathVariable String tutor) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasSeguimientosByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorDistribuidas(idOferCen,idUnidad,anio,idPeriodoPractica,tutor);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    
    
    @GetMapping("/matriculas/noDistribuidas/tutor/unidad/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}/{tutor}")
    @Timed
    public ResponseEntity<List<Matricula>>getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorNoDistribuidas(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio,@PathVariable Long idPeriodoPractica, @PathVariable String tutor) {
        log.debug("REST request to get all Listado Matriculas by ofercen and anio");        
        Iterable<Matricula> listaMatriculas= matriculaService.getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorNoDistribuidas(idOferCen,idUnidad,anio,idPeriodoPractica,tutor);
        return ResponseEntity.ok((List<Matricula>)listaMatriculas);
        } 
    

    
    /**
     * GET  /matriculas/:id : get the "id" matricula.
     *
     * @param id the id of the matricula to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the matricula, or with status 404 (Not Found)
     */
    @GetMapping("/matriculas/{id}")
    @Timed
    public ResponseEntity<Matricula> getMatricula(@PathVariable String id) {
        log.debug("REST request to get Matricula : {}", id);        
        try {
            return ResponseEntity.ok(matriculaService.getMatricula(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }        
    }

    /**
     * DELETE  /matriculas/:id : delete the "id" matricula.
     *
     * @param id the id of the matricula to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/matriculas/{id}")
    @Timed
    public ResponseEntity<String> deleteMatricula(@PathVariable String id) {
        log.debug("REST request to delete Matricula : {}", id);
        
        try {
        	matriculaService.deleteMatricula(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
       // return ResponseEntity.accepted().build();
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    
    @GetMapping("/matriculas/ofertaMatric/{centro}/{anio}")
    public ResponseEntity<List<OfertaMatric>> getListadoOferMatric(@PathVariable String centro,@PathVariable Integer anio){

        Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatric(centro, anio);
        return ResponseEntity.ok((List<OfertaMatric>)listado);
    }
    
    
    @GetMapping("/matriculas/matricular/{centro}/{anio}")
    public ResponseEntity<List<OfertaMatric>> getListadoOfertaParaMatriculacion(@PathVariable String centro,@PathVariable Integer anio){

        Iterable<OfertaMatric> listado= matriculaService.getListOfertaParaMatriculacion(centro, anio);
        return ResponseEntity.ok((List<OfertaMatric>)listado);
    }
    
    
    @GetMapping("/matriculas/ofertaMatric/tutor/{centro}/{anio}/{tutor}")
    public ResponseEntity<List<OfertaMatric>> getListadoOferMatricTutor(@PathVariable String centro,@PathVariable Integer anio, @PathVariable String tutor){

        Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricTutor(centro, anio,tutor);
        return ResponseEntity.ok((List<OfertaMatric>)listado);
    }
    
    @GetMapping("/matriculas/ofertaMatric/{centro}/{anio}/{idPractica}")
    public ResponseEntity<List<OfertaMatric>> getListadoOferMatricPractica(@PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idPractica ){

        Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricByTipoPractica(centro, anio, idPractica);
        return ResponseEntity.ok((List<OfertaMatric>)listado);
    }
    
    @GetMapping("/matriculas/ofertaMatric/tutor/{centro}/{anio}/{idPractica}/{tutor}")
    public ResponseEntity<List<OfertaMatric>> getListadoOferMatricPracticaAndTutor(@PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idPractica, @PathVariable String tutor){

        Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricByTipoPracticaAndTutor(centro, anio, idPractica,tutor);
        return ResponseEntity.ok((List<OfertaMatric>)listado);
    }

   
    @GetMapping("/matriculas/alumAnexov/{idCentro}/{curso}/{idOferta}/{idPeriodoPractica}")
    public ResponseEntity<List<AlumAnexov>> getListaAlumByAnexo (@PathVariable String idCentro, @PathVariable Integer curso, @PathVariable String idOferta,@PathVariable Integer idPeriodoPractica){
        Iterable<AlumAnexov> listado= matriculaService.getListaAlumByAnexo(idCentro, curso, idOferta,idPeriodoPractica);
        return ResponseEntity.ok((List<AlumAnexov>)listado);
    }
    
    @GetMapping("/matriculas/alumAnexov/tutor/{idCentro}/{curso}/{idOferta}/{idPeriodoPractica}/{tutor}")
    public ResponseEntity<List<AlumAnexov>> getListaAlumByAnexoByTutor (@PathVariable String idCentro, @PathVariable Integer curso, @PathVariable String idOferta,@PathVariable Integer idPeriodoPractica, @PathVariable String tutor){
        Iterable<AlumAnexov> listado= matriculaService.getListaAlumByAnexoByTutor(idCentro, curso, idOferta, idPeriodoPractica, tutor);
        return ResponseEntity.ok((List<AlumAnexov>)listado);
    }
    
    
    @GetMapping("/matriculas/alumUnidadAnexov/{idCentro}/{curso}/{idOferta}/{idPeriodoPractica}/{idUnidad}")
    public ResponseEntity<List<AlumAnexov>> getListaAlumByUnidadAnexo (@PathVariable String idCentro, @PathVariable Integer curso, @PathVariable String idOferta,@PathVariable Integer idPeriodoPractica, @PathVariable String idUnidad){
        Iterable<AlumAnexov> listado= matriculaService.getListaAlumByUnidadAnexo(idCentro, curso, idOferta,idPeriodoPractica, idUnidad);
        return ResponseEntity.ok((List<AlumAnexov>)listado);
    }
    
    @GetMapping("/matriculas/alumUnidadAnexov/tutor/{idCentro}/{curso}/{idOferta}/{idPeriodoPractica}/{idUnidad}/{tutor}")
    public ResponseEntity<List<AlumAnexov>> getListaAlumByUnidadAnexoByTutor (@PathVariable String idCentro, @PathVariable Integer curso, @PathVariable String idOferta,@PathVariable Integer idPeriodoPractica, @PathVariable String idUnidad, @PathVariable String tutor){
        Iterable<AlumAnexov> listado= matriculaService.getListaAlumByUnidadAnexoByTutor(idCentro, curso, idOferta, idPeriodoPractica, idUnidad, tutor);
        return ResponseEntity.ok((List<AlumAnexov>)listado);
    }
    
    
    @GetMapping("/matriculas/ofertaMatric/{centro}/{anio}/{idPractica}/{idPeriodoPractica}")
  public ResponseEntity<List<OfertaMatric>> getListadoOferMatricPracticaAndPeriodo(@PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idPractica , @PathVariable Integer idPeriodoPractica){
    	// Esta consulta obtiene listado ofertaMatric cuando exista convenio de tipoPractica/centro, enviamos el flag sinConvenio a false// 
      Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricByTipoPracticaAndPeriodo(centro, anio, idPractica, idPeriodoPractica, false);
      return ResponseEntity.ok((List<OfertaMatric>)listado);
  }
  
    //ruth. cambiar para que no haga la subselect por matricula.
  @GetMapping("/matriculas/ofertaMatric/tutor/{centro}/{anio}/{idPractica}/{idPeriodoPractica}/{tutor}")
  public ResponseEntity<List<OfertaMatric>> getListadoOferMatricPracticaAndPeriodoAndTutor(@PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idPractica, @PathVariable Integer idPeriodoPractica, @PathVariable String tutor){
	  // Esta consulta obtiene listado ofertaMatric cuando exista convenio de tipoPractica/centro, enviamos el flag sinConvenio a false// 
      Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricByTipoPracticaAndPeriodoAndTutor(centro, anio, idPractica, idPeriodoPractica,false,tutor);
      return ResponseEntity.ok((List<OfertaMatric>)listado);
  }
  
  
  @GetMapping("/matriculas/ofertaMatric/seguimientoFinal/{centro}/{anio}/{idPractica}/{idPeriodoPractica}")
  public ResponseEntity<List<OfertaMatric>> getListadoOferMatricSeguimientoFinalPracticaAndPeriodo(@PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idPractica , @PathVariable Integer idPeriodoPractica){
	  // El listado de ofertaMatric para seguimientoFinal no debe mirar si existe un convenio, por eso enviamos el flag sinConvenio a true en la consulta //
      Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricByTipoPracticaAndPeriodo(centro, anio, idPractica, idPeriodoPractica, true);
      return ResponseEntity.ok((List<OfertaMatric>)listado);
  }
  
    
//ruth. cambiar para que no haga la subselect por matricula.
  @GetMapping("/matriculas/ofertaMatric/seguimientoFinal/tutor/{centro}/{anio}/{idPractica}/{idPeriodoPractica}/{tutor}")
  public ResponseEntity<List<OfertaMatric>> getListadoOferMatricSeguimientoFinalPracticaAndPeriodoAndTutor(@PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idPractica, @PathVariable Integer idPeriodoPractica, @PathVariable String tutor){
	  // El listado de ofertaMatric para seguimientoFinal no debe mirar si existe un convenio, por eso enviamos el flag sinConvenio a true en la consulta //
      Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricByTipoPracticaAndPeriodoAndTutor(centro, anio, idPractica, idPeriodoPractica,true,tutor);
      return ResponseEntity.ok((List<OfertaMatric>)listado);
  }
  
  
  @GetMapping("/matriculas/ofertaMatric/seguimientoFinal/anterior/{centro}/{anio}/{idPractica}/{idPeriodoPractica}/{idUnidad}/{idOfercen}/{curso}/{turno}")
  public ResponseEntity<List<OfertaMatric>> getListadoOferMatricSeguimientoFinalPracticaAndPeriodoAndUnidad(@PathVariable String centro,@PathVariable Integer anio,@PathVariable Integer idPractica , @PathVariable Integer idPeriodoPractica,@PathVariable String idUnidad,@PathVariable String idOfercen,@PathVariable Integer curso,@PathVariable String turno){
	  Iterable<OfertaMatric> listado= matriculaService.getListaOfertaMatricByTipoPracticaAndPeriodoAndUnidad(centro, anio, idPractica, idPeriodoPractica,idUnidad,idOfercen,curso,turno);
      return ResponseEntity.ok((List<OfertaMatric>)listado);
  }
  
  
   

}

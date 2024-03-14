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

import es.princast.gepep.domain.Actividad;
import es.princast.gepep.service.ActividadService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Actividad.
 */
@RestController
@RequestMapping("/api")
public class ActividadResource {

    private final Logger log = LoggerFactory.getLogger(ActividadResource.class);

    private static final String ENTITY_NAME = "actividad";
    
    @Autowired
    private ActividadService actividadService;

    

    /**
     * POST  /actividades : Create a new actividad.
     *
     * @param actividad the actividad to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actividad, or with status 400 (Bad Request) if the actividad has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/actividades")
    @Timed
    public ResponseEntity<Object> createActividad(@Valid @RequestBody Actividad actividad) throws URISyntaxException {
    	log.debug("REST request to save Actividad : {}", actividad);
    	
    	try {
		    return ResponseEntity.ok(actividadService.createActividad(actividad));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
    }

    /**
     * PUT  /actividads : Updates an existing actividad.
     *
     * @param actividad the actividad to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actividad,
     * or with status 400 (Bad Request) if the actividad is not valid,
     * or with status 500 (Internal Server Error) if the actividad couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/actividades")
    @Timed
    public ResponseEntity<String> updateActividad(@Valid @RequestBody Actividad actividad) throws URISyntaxException {
    	log.debug("REST request to update Actividad : {}", actividad);
    	  try {
              actividadService.updateActividad(actividad);
          } catch (IllegalArgumentException e) {
        	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
          }
          return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /actividades : get all the actividades.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actividades in body
     */
    @GetMapping("/actividades")
    @Timed
    public ResponseEntity<List<Actividad>> getAllActividades() {    	
        log.debug("REST request to get all Actividades");
        Iterable<Actividad> listaActividades = actividadService.getAllActividades();
        return ResponseEntity.ok((List<Actividad>)listaActividades);
        }

    
    /**
     * GET  /actividades/:id : get the "id" actividad.
     *
     * @param id the id of the actividad to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actividad, or with status 404 (Not Found)
     */
    @GetMapping("/actividades/{idActividad}")
    @Timed
    public ResponseEntity<Actividad> getActividad(@PathVariable Long idActividad) {
        log.debug("REST request to get Actividad : {}", idActividad);     
       
        try {
            return ResponseEntity.ok(actividadService.getActividad(idActividad));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    /**
     * DELETE  /actividades/:id : delete the "id" actividad.
     *
     * @param id the id of the actividad to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/actividades/{idActividad}")
    @Timed
    public ResponseEntity<String> deleteActividad(@PathVariable Long idActividad) {
        log.debug("REST request to delete Actividad : {}", idActividad);
      try {
            actividadService.deleteActividad(idActividad);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
      return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idActividad.toString())).build();
    }

//    @PostMapping("/actividades/matching")
//    public ResponseEntity<List<Actividad>> getAllMatchingActividades(@RequestBody final Actividad actividad) {
//        return ResponseEntity.ok(actividadService.getAllActividadesByCriteria(actividad));
//    }
    
    
    /**
     * GET  /actividades/:idSector/actividades-sector : obtiene actividades por "id" sector.
     *
     * @param id the id of the sector of activities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body, or with status 404 (Not Found)
     */
    @GetMapping("/actividades/{idSector}/actividades-sector")
    @Timed
	public ResponseEntity<List<Actividad>> getActividadBySector(@PathVariable (value="idSector") Long idSector) {
		log.debug("REST request to getActividadBySector");

		Iterable<Actividad> listaActividades = actividadService.getActividadesBySector(idSector);
		return ResponseEntity.ok((List<Actividad>) listaActividades);
	}
    
}

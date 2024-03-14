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

import es.princast.gepep.domain.MotivoEstado;
import es.princast.gepep.service.MotivoEstadoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing MotivoEstado.
 */
@RestController
@RequestMapping("/api")
public class MotivoEstadoResource {

    private final Logger log = LoggerFactory.getLogger(MotivoEstadoResource.class);

    private static final String ENTITY_NAME = "motivoEstado";

    @Autowired
    private MotivoEstadoService motivoEstadoService;

    
    /**
     * POST  /motivo-estados : Create a new motivoEstado.
     *
     * @param motivoEstado the motivoEstado to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motivoEstado, or with status 400 (Bad Request) if the motivoEstado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motivo-estado")
    @Timed
    public ResponseEntity<Object> createMotivoEstado(@Valid @RequestBody MotivoEstado motivoEstado) throws URISyntaxException {
        log.debug("REST request to save MotivoEstado : {}", motivoEstado);
        try {
		    return ResponseEntity.ok(motivoEstadoService.createMotivoEstado(motivoEstado));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
    }

    /**
     * PUT  /motivo-estados : Updates an existing motivoEstado.
     *
     * @param motivoEstado the motivoEstado to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motivoEstado,
     * or with status 400 (Bad Request) if the motivoEstado is not valid,
     * or with status 500 (Internal Server Error) if the motivoEstado couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motivo-estado")
    @Timed
    public ResponseEntity<String> updateMotivoEstado(@Valid @RequestBody MotivoEstado motivoEstado) throws URISyntaxException {
        log.debug("REST request to update MotivoEstado : {}", motivoEstado);
        try {
        	motivoEstadoService.updateMotivoEstado(motivoEstado);
        } catch (IllegalArgumentException e) {
      	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
	
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /motivo-estados : get all the motivoEstados.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motivoEstados in body
     */
    @GetMapping("/motivo-estado")
    @Timed
    public ResponseEntity<List<MotivoEstado>> getAllMotivoEstados() {
        log.debug("REST request to get all MotivoEstados");
    	Iterable<MotivoEstado> listaMotivos = motivoEstadoService.getAllMotivoEstados();
		return ResponseEntity.ok((List<MotivoEstado>) listaMotivos);
        
        }

    /**
     * GET  /motivo-estados/:id : get the "id" motivoEstado.
     *
     * @param id the id of the motivoEstado to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motivoEstado, or with status 404 (Not Found)
     */
    @GetMapping("/motivo-estado/{id}")
    @Timed
    public ResponseEntity<MotivoEstado> getMotivoEstado(@PathVariable Long id) {
        log.debug("REST request to get MotivoEstado : {}", id);
        try {
            return ResponseEntity.ok(motivoEstadoService.getMotivoEstado(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
     
    }

    /**
     * DELETE  /motivo-estados/:id : delete the "id" motivoEstado.
     *
     * @param id the id of the motivoEstado to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motivo-estado/{id}")
    @Timed
    public ResponseEntity<String> deleteMotivoEstado(@PathVariable Long id) {
        log.debug("REST request to delete MotivoEstado : {}", id);
        try {
        	motivoEstadoService.deleteMotivoEstado(id);
        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
      
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

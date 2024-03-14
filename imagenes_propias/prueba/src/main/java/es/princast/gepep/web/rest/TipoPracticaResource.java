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

import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.service.TipoPracticaService;

/**
 * REST controller for managing TipoPractica.
 */
@RestController
@RequestMapping("/api")
public class TipoPracticaResource {

    private final Logger log = LoggerFactory.getLogger(TipoPracticaResource.class);


    @Autowired
    private TipoPracticaService tipoPracticaService;
  
    
    /**
     * POST  /tipos-practica : Create a new tipoPractica.
     *
     * @param tipoPractica the tipoPractica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoPractica, or with status 400 (Bad Request) if the tipoPractica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipos-practica")
    @Timed
    public ResponseEntity<Object> createTipoPractica(@Valid @RequestBody TipoPractica tipoPractica) throws URISyntaxException {
        log.debug("REST request to save TipoPractica : {}", tipoPractica);
        try {
		    return ResponseEntity.ok(tipoPracticaService.createTipoPractica(tipoPractica));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }    
        
    }

    /**
     * PUT  /tipos-practica : Updates an existing tipoPractica.
     *
     * @param tipoPractica the tipoPractica to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoPractica,
     * or with status 400 (Bad Request) if the tipoPractica is not valid,
     * or with status 500 (Internal Server Error) if the tipoPractica couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipos-practica")
    @Timed
    public ResponseEntity<String> updateTipoPractica(@Valid @RequestBody TipoPractica tipoPractica) throws URISyntaxException {
        log.debug("REST request to update TipoPractica : {}", tipoPractica);      
        tipoPracticaService.updateTipoPractica(tipoPractica);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /tipos-practica : get all the tipoPracticas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipoPracticas in body
     */
    @GetMapping("/tipos-practica")
    @Timed
    public  ResponseEntity<List<TipoPractica>> getAllTipoPracticas() {
        log.debug("REST request to get all TipoPracticas");
        Iterable <TipoPractica> listaTiposPractica = tipoPracticaService.getAllTiposPracticas();
        return ResponseEntity.ok((List<TipoPractica>)listaTiposPractica);
        }

    
    /**
     * GET  /tipos-practica : get all the tipoPracticas sin cargar sus relaciones. Usado en la carga de los combos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipoPracticas in body
     */
    @GetMapping("/tipos-practica/getTipoPracticaComboList/{anio}")
    @Timed
    public  ResponseEntity<List<TipoPractica>> getTipoPracticaComboList(@PathVariable Integer anio) {
        log.debug("REST request to get all getTipoPracticaComboList");
        Iterable <TipoPractica> listaTiposPractica = tipoPracticaService.getTipoPracticaComboList(anio);
        return ResponseEntity.ok((List<TipoPractica>)listaTiposPractica);
        }

   
    /**
     * GET  /tipos-practica/:id : get the "id" tipoPractica.
     *
     * @param id the id of the tipoPractica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoPractica, or with status 404 (Not Found)
     */
    @GetMapping("/tipos-practica/{idTipoPractica}")
    @Timed
    public ResponseEntity<TipoPractica> getTipoPractica(@PathVariable Long idTipoPractica) {
        log.debug("REST request to get TipoPractica : {}", idTipoPractica);
        try {
            return ResponseEntity.ok(tipoPracticaService.getTipoPractica(idTipoPractica));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }       
    }
    
   
    /**
     * DELETE  /tipos-practica/:id : delete the "id" tipoPractica.
     *
     * @param id the id of the tipoPractica to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipos-practica/{idTipoPractica}")
    @Timed
    public ResponseEntity<String> deleteTipoPractica(@PathVariable Long idTipoPractica) {
        log.debug("REST request to delete TipoPractica : {}", idTipoPractica);
        try {
            tipoPracticaService.deleteTipoPractica(idTipoPractica);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.accepted().build();
        
    }
    
    /**
     * GET  /visores - ensenanzas/:id : get the "id" visor.
     *
     * @param id the id of the Visor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the Visor, or with status 404 (Not Found)
     */
    @GetMapping("/visores/{idVisor}/tipopracticas")
    @Timed
    public Iterable <TipoPractica> getAllTipoPracticasByVisor(@PathVariable (value="idVisor") Long idVisor) {
        log.debug("REST request to get all TipoPracticas by Visor");
        return tipoPracticaService.getAllTipoPracticaByVisor(idVisor);
        }
    

}

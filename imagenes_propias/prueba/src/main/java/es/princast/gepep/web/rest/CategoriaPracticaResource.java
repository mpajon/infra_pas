package es.princast.gepep.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.princast.gepep.domain.CategoriaPractica;
import es.princast.gepep.service.CategoriaPracticaService;


/**
 * REST controller for managing Categoria.
 */
@RestController
@RequestMapping("/api")
public class CategoriaPracticaResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaPracticaResource.class);



    @Autowired
    private CategoriaPracticaService categoriaService;
    
        

    /**
     * GET  /categorias : get all the categorias.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categorias in body
     */
    @GetMapping("/categorias")
    @Timed
    public ResponseEntity<List<CategoriaPractica>> getAllCategorias() {
        log.debug("REST request to get all Families");
        Iterable<CategoriaPractica> listaCategorias = categoriaService.getAllCategorias();
        return ResponseEntity.ok((List<CategoriaPractica>)listaCategorias);
        }
   
   

    /**
     * GET  /categorias/:id : get the "id" categoria.
     *
     * @param id the id of the categoria to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categoria, or with status 404 (Not Found)
     */
    @GetMapping("/categorias/{idCategoria}")
    @Timed
    public ResponseEntity<CategoriaPractica> getCategoria (@PathVariable Long idCategoria) {
        log.debug("REST request to get Categoria : {}", idCategoria);
        try {
        	 return ResponseEntity.ok(categoriaService.getCategoria(idCategoria));        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }    
    }

   
    
   

}

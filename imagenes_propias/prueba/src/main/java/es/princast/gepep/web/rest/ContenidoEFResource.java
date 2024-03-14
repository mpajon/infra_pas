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

import es.princast.gepep.domain.ContenidoEF;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.repository.DistribucionRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.service.ConfigElemFormativoService;
import es.princast.gepep.service.ContenidoEFService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ContenidoEF.
 */
@RestController
@RequestMapping("/api")
public class ContenidoEFResource {

    private final Logger log = LoggerFactory.getLogger(ContenidoEFResource.class);

    private static final String ENTITY_NAME = "contenidoEF";
    
    @Autowired
    private ConfigElemFormativoService configElemFormativoService;

    @Autowired
    private ContenidoEFService contenidoEFService;
    
    @Autowired
    private TipoPracticaRepository tipoPracticaRepository;

    @Autowired
    private DistribucionRepository distribucionRepository;

    @Autowired
    private CicloRepository cicloRepository;

    /**
     * POST  /contenidos : Create a new contenido.
     *
     * @param contenidoEF the contenido to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contenido, or with status 400 (Bad Request) if the contenido has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contenidoEF")
    @Timed
    public ResponseEntity<Object> createContenidoEF(@Valid @RequestBody ContenidoEF contenidoEF) throws URISyntaxException {
    	log.debug("REST request to save ContenidoEF : {}", contenidoEF);
    	
    	try {
		    return ResponseEntity.ok(contenidoEFService.createContenidoEF(contenidoEF));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
    }

    /**
     * PUT  /contenidos : Updates an existing contenido.
     *
     * @param contenidoEF the contenido to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contenido,
     * or with status 400 (Bad Request) if the contenido is not valid,
     * or with status 500 (Internal Server Error) if the contenido couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contenidoEF")
    @Timed
    public ResponseEntity<String> updateContenidoEF(@Valid @RequestBody ContenidoEF contenidoEF) throws URISyntaxException {
    	log.debug("REST request to update ContenidoEF : {}", contenidoEF);
    	  try {
    	      if(contenidoEF.getIdContenidoEF() != null) {
    		  contenidoEFService.updateContenidoEF(contenidoEF);
    	      }else {
    		  createContenidoEF(contenidoEF);
    	      }
          } catch (IllegalArgumentException e) {
        	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
          }
          return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * GET  /contenidos : get all the contenidos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contenidos in body
     */
    @GetMapping("/contenidoEF")
    @Timed
    public ResponseEntity<List<ContenidoEF>> getAllContenidoEFs() {    	
        log.debug("REST request to get all ContenidoEFs");
        Iterable<ContenidoEF> listaContenidoEFs = contenidoEFService.getAllContenidoEF();
        return ResponseEntity.ok((List<ContenidoEF>)listaContenidoEFs);
        }

    
    /**
     * GET  /contenidos/:id : get the "id" contenido.
     *
     * @param id the id of the contenido to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contenido, or with status 404 (Not Found)
     */
    @GetMapping("/contenidoEF/{idContenidoEF}")
    @Timed
    public ResponseEntity<ContenidoEF> getContenidoEF(@PathVariable Long idContenidoEF) {
        log.debug("REST request to get ContenidoEF : {}", idContenidoEF);     
       
        try {
            return ResponseEntity.ok(contenidoEFService.getContenidoEF(idContenidoEF));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    /**
     * DELETE  /contenidos/:id : delete the "id" contenido.
     *
     * @param id the id of the contenido to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contenidoEF/{idContenidoEF}")
    @Timed
    public ResponseEntity<String> deleteContenidoEF(@PathVariable Long idContenidoEF) {
        log.debug("REST request to delete ContenidoEF : {}", idContenidoEF);
      try {
            contenidoEFService.deleteContenidoEF(idContenidoEF);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
      return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, idContenidoEF.toString())).build();
    }
 
    
    /**
     * GET  /contenidoEF/ obtiene contenidos EF para tipoPractica y ciclo
     *
     */

    @GetMapping("/contenidoEF/getContenidoEFByTipoPracticaCiclo/{idCiclo}/{idTipoPractica}")
    @Timed
	public ResponseEntity<List<ContenidoEF>> getContenidoEFByTipoPracticaCiclo(@PathVariable (value="idCiclo") String idCiclo, @PathVariable (value="idTipoPractica") Long idTipoPractica) {
		log.debug("REST request to getContenidoEFByTipoPracticaAndEF");
		Iterable<ContenidoEF> listaContenidoEFs = contenidoEFService.getContenidosEFByTipoPracticaCicloDist(idTipoPractica, idCiclo, null);
		
		return ResponseEntity.ok((List<ContenidoEF>) listaContenidoEFs);
	}

    /**
     * GET  /contenidoEF/ obtiene contenidos EF para tipoPractica y ciclo
     *
     */

    @GetMapping("/contenidoEF/getContenidoEFByTipoPracticaDistribucion/{idDistribucion}/{idTipoPractica}")
    @Timed
	public ResponseEntity<List<ContenidoEF>> getContenidoEFByTipoPracticaDistribucion(@PathVariable (value="idDistribucion") Long idDistribucion, @PathVariable (value="idTipoPractica") Long idTipoPractica) {
		log.debug("REST request to getContenidoEFByTipoPracticaAndEF");
		Iterable<ContenidoEF> listaContenidoEFs = contenidoEFService.getContenidosEFByTipoPracticaCicloDist(idTipoPractica, null, idDistribucion);
		
		return ResponseEntity.ok((List<ContenidoEF>) listaContenidoEFs);
	}
    
    /**
     * GET  /configEF/:idContenidoEF/contenidos-sector : obtiene configuraciones por id de EF
     *
     * @param id the id of the sector of activities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body, or with status 404 (Not Found)
     */
    @GetMapping("/contenidoEF/config/{idConfigElemFormativo}")
    @Timed
	public ResponseEntity<List<ContenidoEF>> getContenidoEFByTipoPractica(@PathVariable (value="idConfigElemFormativo") Long idConfigElemFormativo) {
		log.debug("REST request to getContenidoEFByTipoPractica");
		Iterable<ContenidoEF> listaContenidoEFs = contenidoEFService.getContenidoEFByConfig(idConfigElemFormativo);
		return ResponseEntity.ok((List<ContenidoEF>) listaContenidoEFs);
	}
    
    
    /**
     * GET  /configEF/:idContenidoEF/contenidos-sector : obtiene configuraciones por id de EF
     *
     * @param id the id of the sector of activities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body, or with status 404 (Not Found)
     */
    @GetMapping("/contenidoEF/{idCiclo}/{idConfigElemFormativo}")
    @Timed
	public ResponseEntity<List<ContenidoEF>> getContenidoEFByElementoFormativo(@PathVariable (value="idCiclo") String idCiclo,@PathVariable (value="idConfigElemFormativo") Long idConfigElemFormativo) {
		log.debug("REST request to getContenidoEFByElementoFormativo");
		Iterable<ContenidoEF> listaContenidoEF = contenidoEFService.getContenidoEFByCicloAndConfig(idCiclo,idConfigElemFormativo);
	
		return ResponseEntity.ok((List<ContenidoEF>) listaContenidoEF);
	}
    
    @GetMapping("/contenidoEF/{idTipoPractica}/{idDistribucion}/{idCiclo}")
    @Timed
	public ResponseEntity<List<ContenidoEF>> getContenidosEFByTipoPracticaCicloDist(@PathVariable (value="idTipoPractica") Long idTipoPractica,@PathVariable (value="idCiclo") String idCiclo,@PathVariable (value="idDistribucion") Long idDistribucion) {
		log.debug("REST request to getContenidoEFByElementoFormativo");		
		Iterable<ContenidoEF> listaContenidoEF = contenidoEFService.getContenidosEFByTipoPracticaCicloDist(tipoPracticaRepository.getOne(idTipoPractica), cicloRepository.getOne(idCiclo), distribucionRepository.getOne(idDistribucion));	
		return ResponseEntity.ok((List<ContenidoEF>) listaContenidoEF);		
		 
	} 
    
    @GetMapping("/contenidoEF/distribucion/{idDistribucion}")
    @Timed
	public ResponseEntity<List<ContenidoEF>> getContenidosEFByDistribucion(@PathVariable (value="idDistribucion") Long idDistribucion) {
		log.debug("REST request to getContenidoEFByElementoFormativo");		
		Iterable<ContenidoEF> listaContenidoEF = contenidoEFService.getContenidoEFByDistribucion(idDistribucion);	
		return ResponseEntity.ok((List<ContenidoEF>) listaContenidoEF);
		
		 
	}
    
 
}

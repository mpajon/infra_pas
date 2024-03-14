package es.princast.gepep.web.rest;


import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import es.princast.gepep.domain.AnexoContrato;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.domain.GastoAlumno;
import es.princast.gepep.repository.AnexoContratoRepository;
import es.princast.gepep.repository.ConvenioRepository;
import es.princast.gepep.service.AnexoContratoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing AnexoContrato.
 */
@RestController
@RequestMapping("/api")
public class AnexoContratoResource {

    private final Logger log = LoggerFactory.getLogger(AnexoContratoResource.class);

    private static final String ENTITY_NAME = "anexoContrato";

    private final AnexoContratoRepository anexoContratoRepository;
    
    @Autowired
    private ConvenioRepository convenioRepository;
  

	@Autowired
	private AnexoContratoService anexoContratoService;
	

    public AnexoContratoResource(AnexoContratoRepository anexoContratoRepository) {
        this.anexoContratoRepository = anexoContratoRepository;
    }

    /**
     * POST  /anexo-contrato : Create a new anexoContrato.
     *
     * @param anexoContrato the anexoContrato to create
     * @return the ResponseEntity with status 201 (Created) and with body the new anexoContrato, or with status 400 (Bad Request) if the anexoContrato has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/anexo-contrato")
    @Timed
    public ResponseEntity<Object> createAnexoContrato(@Valid @RequestBody AnexoContrato anexoContrato) throws URISyntaxException {
    	
    	log.debug("REST request to save AnexoContrato : {}", anexoContrato);
    	
    	try {
		    return ResponseEntity.ok(anexoContratoService.createAnexoContrato(anexoContrato));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
    	
    }

    /**
     * PUT  /anexo-contrato : Updates an existing anexoContrato.
     *
     * @param anexoContrato the anexoContrato to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated anexoContrato,
     * or with status 400 (Bad Request) if the anexoContrato is not valid,
     * or with status 500 (Internal Server Error) if the anexoContrato couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/anexo-contrato")
    @Timed
    public ResponseEntity<String> updateAnexoContrato(@Valid @RequestBody AnexoContrato anexoContrato) throws URISyntaxException {
        log.debug("REST request to update AnexoContrato : {}", anexoContrato);
         
		try {
			anexoContratoService.updateAnexoContrato(anexoContrato);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        
        /*if (anexoContrato.getIdAnexo() == null) {
            return createAnexoContrato(anexoContrato);
        }
        AnexoContrato result = anexoContratoRepository.save(anexoContrato);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, anexoContrato.getIdAnexo().toString()))
            .body(result);*/
    }

    /**
     * GET  /anexo-contrato : get all the anexoContratoes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    @GetMapping("/anexo-contrato")
    @Timed
    public ResponseEntity<List<AnexoContrato>> getAllAnexosContrato() {
        log.debug("REST request to GET ALL AnexoContratos------------------------");
       // return anexoContratoRepository.findAll();
		Iterable<AnexoContrato> listaAnexos = anexoContratoService.getAllAnexoContratos();
        return ResponseEntity.ok((List<AnexoContrato>)listaAnexos);
        
        }


    /**
     * GET  /anexo-contrato : get all the  Anexos by Centro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    /*@GetMapping("/distribuciones/{cn_distribucion}/anexo-contrato")
    @Timed
    public Page<AnexoContrato> getAllAnexosByDistribucion(@PathVariable (value="cn_distribucion") Long id,Pageable pageable)  {
        log.debug("REST request to get all AnexoContratos By Distribucion");
        return anexoContratoRepository.findAllByDistribucion(distribucionRepository.getOne(id),pageable);
        }*/
    
    /**
     * GET  /anexo-contrato : get all the  Anexos by oferta
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
  /*  @GetMapping("/oferta-formativa/{ca_oferta_formativa}/anexo-contrato")
    @Timed
    public Page<AnexoContrato> getAllAnexosByOferta(@PathVariable (value="ca_oferta_formativa") Long id,Pageable pageable)  {
        log.debug("REST request to get all AnexoContratos By Centro");
        return anexoContratoRepository.findAllByOferta(ofertaformativaRepository.getOne(id),pageable);
        }
    */
    
    /**
     * GET  /anexo-contrato : get all the  Anexos by Convenio
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    @GetMapping("/convenios/{cn_convenio}/anexo-contrato")
    @Timed
    public Iterable<AnexoContrato> getAllAnexosByConvenio(@PathVariable (value="cn_convenio") Long id,Pageable pageable)  {
        log.debug("REST request to get all AnexoContratos By Convenio************");
       // return anexoContratoRepository.findAllByConvenio(convenioRepository.getOne(id),pageable);
        return anexoContratoRepository.findAllByConvenioOrderByCodAnexoAsc(convenioRepository.getOne(id));
        }
    
    
    
    /**
     * GET  /anexo-contrato : get all the  Anexos by OfertasCentro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    @GetMapping("/anexo-contrato/ofercen/{id}")
    @Timed
    public Iterable<AnexoContrato> getAllAnexosByOfertaCentro(@PathVariable (value="id") String id)  {
        log.debug("REST request to get all AnexoContratos By Convenio");
       
       // return anexoContratoRepository.findAllByOfertaCentro(ofertaCentroRepository.getOne(id));
        return anexoContratoRepository.findOneWithEagerRelationships(id);
        }
    
    /**
     * 
     * @param id
     * @param idPeriodo
     * @return
     */
    @GetMapping("/anexo-contrato/ofercen/{id}/periodo/{idPeriodo}")
    @Timed
    public Iterable<AnexoContrato> getAllAnexosByOfertaCentroAndPeriodo(@PathVariable (value="id") String id,@PathVariable (value="idPeriodo") Long idPeriodo)  {
        log.debug("REST request to get all AnexoContratos By Convenio");
       
       // return anexoContratoRepository.findAllByOfertaCentro(ofertaCentroRepository.getOne(id));
        return anexoContratoRepository.findOneWithEagerRelationships(id,idPeriodo);
        }
    
    
    /**
     * GET  /anexo-contrato : get all the  Anexos by OfertasCentro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    @GetMapping("/anexo-contrato/ofercen/{id}/{idUnidad}/{idAnio}/{idTipoPractica}")
    @Timed
    public Iterable<AnexoContrato> getAllAnexosByOfertaCentroAndUnidadAndAnio(@PathVariable (value="id") String id,@PathVariable (value="idUnidad") String idUnidad,@PathVariable (value="idAnio") Integer idAnio, @PathVariable (value="idTipoPractica") Long idTipoPractica)  {
        log.debug("REST request to get all AnexoContratos By OferCen and Unidad");

        return anexoContratoRepository.findOneWithEagerRelationships(id,idUnidad,idAnio,idTipoPractica);
        }
    
    /**
     * GET  /anexo-contrato : get all the  Anexos by OfertasCentro and Periodo
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    @GetMapping("/anexo-contrato/ofercen/{id}/{idUnidad}/{idAnio}/{idTipoPractica}/{idPeriodo}")
    @Timed
    public Iterable<AnexoContrato> getAllAnexosByOfertaCentroAndUnidadAndAnioAndPeriodo(@PathVariable (value="id") String id,@PathVariable (value="idUnidad") String idUnidad,@PathVariable (value="idAnio") Integer idAnio, @PathVariable (value="idTipoPractica") Long idTipoPractica,@PathVariable (value="idPeriodo") Long idPeriodo)  {
        log.debug("REST request to get all AnexoContratos By OferCen and Unidad");

        return anexoContratoRepository.findOneWithEagerRelationships(id,idUnidad,idAnio,idTipoPractica,idPeriodo);
        }
    
    /**
     * GET  /anexo-contrato : Obtiene los anexos de ofertacentro, unidad, anio , practica y periodo para un alumno en los que no ha sido distribuido para esos filtros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    @GetMapping("/anexo-contrato/ofercen/noDistribuido/{id}/{idUnidad}/{idAnio}/{idTipoPractica}/{idPeriodo}/{idAlumno}")
    @Timed
    public Iterable<AnexoContrato> getAllAnexosNoDistribuidosByOfertaCentroAndUnidadAndAnioAndPeriodo(@PathVariable (value="id") String id,@PathVariable (value="idUnidad") String idUnidad,@PathVariable (value="idAnio") Integer idAnio, @PathVariable (value="idTipoPractica") Long idTipoPractica,@PathVariable (value="idPeriodo") Long idPeriodo, @PathVariable (value="idAlumno") String idAlumno)  {
        log.debug("REST request to get all AnexoContratos By OferCen and Unidad");
            return anexoContratoRepository.findOneWithEagerRelationshipsNoDistributed(id,idUnidad,idAnio,idTipoPractica,idPeriodo,idAlumno);
        }
    
    /**
     * GET  /anexo-contrato/:id : get the "id" anexoContrato.
     *
     * @param id the id of the anexoContrato to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the anexoContrato, or with status 404 (Not Found)
     */
    @GetMapping("/anexo-contrato/{id}")
    @Timed
    public ResponseEntity<AnexoContrato> getAnexoContrato(@PathVariable Long id) {
        log.debug("REST request to get AnexoContrato : {}", id);   
        try {
			return ResponseEntity.ok(anexoContratoService.getAnexoContrato(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
    } 

    /**
     * DELETE  /anexo-contrato/:id : delete the "id" anexoContrato.
     *
     * @param id the id of the anexoContrato to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/anexo-contrato/{id}")
    @Timed
    public ResponseEntity<String> deleteAnexoContrato(@PathVariable Long id) {
        log.debug("REST request to delete AnexoContrato : {}", id);
        
        log.debug("REST request to delete Convenio : {}", id);

		try {
			anexoContratoService.deleteAnexoContrato(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();        
    }
}

package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import es.princast.gepep.domain.GastoAlumno;
import es.princast.gepep.domain.VisitaTutor;
import es.princast.gepep.domain.dto.DistribucionDTO;
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

import es.princast.gepep.domain.AlumDisAnexoiii;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.service.DistribucionService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Distribucion.
 */
@RestController
@RequestMapping("/api")
public class DistribucionResource {

    private final Logger log = LoggerFactory.getLogger(DistribucionResource.class);

    private static final String ENTITY_NAME = "distribucion";

    @Autowired
    private DistribucionService distribucionService;


    /**
     * POST  /distribuciones : Create a new distribucion.
     *
     * @param distribucion the distribucion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new distribucion, or with status 400 (Bad Request) if the distribucion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/distribuciones")
    @Timed
    public ResponseEntity<Distribucion> createDistribucion(@Valid @RequestBody Distribucion distribucion) throws URISyntaxException {
        log.debug("REST request to save Distribucion : {}", distribucion);
        return distribucionService.createDistribucion(distribucion);
       /* try {
		    return ResponseEntity.ok(distribucionService.createDistribucion(distribucion));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       } */
    }

    /**
     * PUT  /distribuciones : Updates an existing distribucion.
     *
     * @param distribucion the distribucion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated distribucion,
     * or with status 400 (Bad Request) if the distribucion is not valid,
     * or with status 500 (Internal Server Error) if the distribucion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/distribuciones")
    @Timed
    public ResponseEntity<String> updateDistribucion(@Valid @RequestBody Distribucion distribucion) throws URISyntaxException {
        log.debug("REST request to update Distribucion : {}", distribucion);
        try {
            distribucionService.updateDistribucion(distribucion);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/distribuciones-baja")
    @Timed
    public ResponseEntity<String> updateBaja(@Valid @RequestBody Distribucion distribucionModificada) throws URISyntaxException {
        distribucionService.updateBaja(distribucionModificada);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();


    }


    /**
     * GET  /distribuciones : get all the distribucions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of distribucions in body
     */
    @GetMapping("/distribuciones")
    @Timed
    public ResponseEntity<List<Distribucion>> getAllDistribuciones() {
        log.debug("REST request to get all Distribuciones");
        Iterable<Distribucion> listaDistribuiciones = distribucionService.getAllDistribuciones();
        return ResponseEntity.ok((List<Distribucion>) listaDistribuiciones);
    }

    /**
     * GET  /distribuciones/:id : get the "id" distribucion.
     *
     * @param id the id of the distribucion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the distribucion, or with status 404 (Not Found)
     */
    @GetMapping("/distribuciones/{id}")
    @Timed
    public ResponseEntity<Distribucion> getDistribucion(@PathVariable Long id) {
        log.debug("REST request to get Distribucion : {}", id);
        try {
            //  return ResponseEntity.ok(distribucionService.getDistribucion(id));
            return ResponseEntity.ok(distribucionService.getDistribucionAndContenidosEF(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }




    /**
     * GET  /anexo-contrato : get all the  Anexos by OfertasCentro
     *
     * @return the ResponseEntity with status 200 (OK) and the list of anexoContratoes in body
     */
    @GetMapping("/distribuciones/anexos/{cn_anexo}")
    @Timed
    public Iterable<Distribucion> getAllDistribucionesByAnexo(@PathVariable (value="cn_anexo") Long id)  {
        log.debug("REST request to get all AnexoContratos By Convenio");
        // return anexoContratoRepository.findAllByConvenio(convenioRepository.getOne(id),pageable);
        return distribucionService.getAllByAnexos(id);
    }



    @GetMapping("/distribuciones/matricula/{idMatricula}")
    @Timed
    public Iterable<Distribucion> getAllDistribucionesByMatricula(@PathVariable (value="idMatricula") String id)  {
        log.debug("REST request to get all AnexoContratos By Convenio");

        return distribucionService.getAllByMatricula(id);
    }


    @GetMapping("/distribuciones/matricula/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}")
    @Timed
    public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidas(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio, @PathVariable Long idPeriodoPractica) {
        log.debug("REST request to get all distribuciones by oferta distribuida.");

        return distribucionService.getAllDistribucionesByMatriculasDistribuidasByPeriodo(idOferCen,  idUnidad,   anio,   idPeriodoPractica);
    }

    @GetMapping("/distribuciones/matricula/gastosPeriodo/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}")
    @Timed
    public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidasWithGastosByPeriodo(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio, @PathVariable Long idPeriodoPractica) {
        log.debug("REST request to get all distribuciones by oferta distribuida.");

        return distribucionService.getAllDistribucionesByMatriculasDistribuidasWithGastosByPeriodo(idOferCen,  idUnidad,   anio,   idPeriodoPractica);
    }

    @GetMapping("/distribuciones/matricula/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}/{tutor}")
    @Timed
    public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidasByPeriodoTutor(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio, @PathVariable Long idPeriodoPractica,@PathVariable String tutor) {
        log.debug("REST request to get all distribuciones by oferta distribuida.");

        return distribucionService.getAllDistribucionesByMatriculasDistribuidasByPeriodoTutor(idOferCen,  idUnidad,   anio,   idPeriodoPractica,tutor);
    }

    @GetMapping("/distribuciones/matricula/gastosPeriodo/{idOferCen}/{idUnidad}/{anio}/{idPeriodoPractica}/{tutor}")
    @Timed
    public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidasByPeriodoTutorWithGastosByPeriodo(@PathVariable String idOferCen,@PathVariable String idUnidad,@PathVariable Integer anio, @PathVariable Long idPeriodoPractica,@PathVariable String tutor) {
        log.debug("REST request to get all distribuciones by oferta distribuida.");

        return distribucionService.getAllDistribucionesByMatriculasDistribuidasByPeriodoTutorWithGastosByPeriodo(idOferCen,  idUnidad,   anio,   idPeriodoPractica,tutor);
    }




    /**
     * DELETE  /distribuciones/:id : delete the "id" distribucion.
     *
     * @param id the id of the distribucion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/distribuciones/{id}")
    @Timed
    public ResponseEntity<String> deleteDistribucion(@PathVariable Long id) {
        log.debug("REST request to delete Distribucion : {}", id);

        try {
            distribucionService.deleteDistribucion(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/distribuciones/alumDisAnexoiii/{idAnexo}")
    public ResponseEntity<List<AlumDisAnexoiii>> getListaAlumDisByAnexo (@PathVariable Integer idAnexo){

        Iterable<AlumDisAnexoiii> listado= distribucionService.getListaAlumDisByAnexo(idAnexo);
        return ResponseEntity.ok((List<AlumDisAnexoiii>)listado);
    }

    @GetMapping("/distribuciones/distribucionesAndPeriodos/{idCentro}/{anio}/{tutor}")
    public Iterable<Distribucion> getDistribucionesAndPeriodosByCentroAndAnio (@PathVariable String idCentro, @PathVariable Integer anio, @PathVariable String tutor){
        log.debug("REST request to getDistribucionesAndPeriodosByCentroAndAnio.");

        return distribucionService.getDistribucionesAndPeriodosByCentroAndAnio(idCentro,anio, tutor);
    }

    @GetMapping("/distribuciones/distribucionesAndPeriodosNative/{idCentro}/{anio}/{tutor}")
    public Iterable<DistribucionDTO> getDistribucionesAndPeriodosByCentroAndAnioNativeQuery (@PathVariable String idCentro, @PathVariable Integer anio, @PathVariable String tutor){
        log.debug("REST request to getDistribucionesAndPeriodosByCentroAndAnioNative.");

        return distribucionService.getDistribucionesAndPeriodosByCentroAndAnioDTO(idCentro,anio, tutor);
    }


}

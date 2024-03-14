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

import es.princast.gepep.domain.VisitaAgrupada;
import es.princast.gepep.domain.VisitaTutor;
import es.princast.gepep.service.VisitaTutorService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing VisitaTutor.
 */
@RestController
@RequestMapping("/api")
public class VisitaTutorResource {

	private final Logger log = LoggerFactory.getLogger(VisitaTutorResource.class);

	private static final String ENTITY_NAME = "visitaTutor";

	@Autowired
	private VisitaTutorService visitaTutorService;


	/**
	 * POST /visitas-tutores : Create a new visitaTutor.
	 *
	 * @param visitaTutor
	 *            the visitaTutor to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         visitaTutor, or with status 400 (Bad Request) if the visitaTutor has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/visitas-tutor")
	@Timed
	public ResponseEntity<Object> createVisitaTutor(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to save VisitaTutor : {}", visitaTutor);
		try {
		    return ResponseEntity.ok(visitaTutorService.createVisitaTutor(visitaTutor));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	    
	}

	/**
	 * PUT /visitas-tutores : Updates an existing visitaTutor.
	 *
	 * @param visitaTutor
	 *            the visitaTutor to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         visitaTutor, or with status 400 (Bad Request) if the visitaTutor is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         visitaTutor couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/visitas-tutor")
	@Timed
	public ResponseEntity<String> updateVisitaTutor(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaTutor(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	 
	
	
	@PutMapping("/visitas-tutor/autorizarTodas")
	@Timed
	public ResponseEntity<String> updateVisitaAutorizarTodas(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaAutorizarTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping("/visitas-tutor/autorizar")
	@Timed
	public ResponseEntity<String> updateVisitaAutorizarByTutor(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaTutorAutorizarTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping("/visitas-tutor/noautorizarTodas")
	@Timed
	public ResponseEntity<String> updateVisitaNoAutorizarTodas(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaNoAutorizarTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	
	@PutMapping("/visitas-tutor/noautorizar")
	@Timed
	public ResponseEntity<String> updateVisitaNoAutorizarByTutor(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaTutorNoAutorizarTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping("/visitas-tutor/realizarTodas")
	@Timed
	public ResponseEntity<String> updateVisitaTutorRealizarTodas(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaRealizarTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	

	@PutMapping("/visitas-tutor/realizar")
	@Timed
	public ResponseEntity<String> updateVisitaTutorRealizarByTutor(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaTutorRealizarTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping("/visitas-tutor/bloquearTodas")
	@Timed
	public ResponseEntity<String> updateVisitaTutorBloquearTodas(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaBloquearTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
 
	@PutMapping("/visitas-tutor/bloquear")
	@Timed
	public ResponseEntity<String> updateVisitaTutorBloquearByTutor(@Valid @RequestBody VisitaTutor visitaTutor)
			throws URISyntaxException {
		log.debug("REST request to update VisitaTutor : {}", visitaTutor);
		try {
			visitaTutorService.updateVisitaTutorBloquearTodas(visitaTutor);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	

	/**
	 * GET /visitas-tutores : get all the visitaTutors.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of visitaTutors
	 *         in body
	 */
	@GetMapping("/visitas-tutor")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getAllVisitaTutores() {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getAllVisitasTutores();
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);
	}
 

	@GetMapping("/visitas-tutor/{anio}/{mes}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getVisitaTutoresByAnioAndMes(@PathVariable Integer mes, @PathVariable Integer anio) {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getVisitaTutoriaByAnioAndMes(anio, mes);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);
	}
	
	
	

	@GetMapping("/visitas-tutor/agrupada/{anio}/{mes}")
	@Timed
	public ResponseEntity<List<VisitaAgrupada>> getVisitaAgrupadaByAnioAndMes(@PathVariable Integer anio,@PathVariable Integer mes) {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaAgrupada> listaVisitas = visitaTutorService.getVisitaAgrupadaByAnioAndMes(anio, mes);
		return ResponseEntity.ok((List<VisitaAgrupada>) listaVisitas);
	}

	

	@GetMapping("/visitas-tutor/agrupada/tutor/{anio}/{mes}/{tutor}")
	@Timed
	public ResponseEntity<List<VisitaAgrupada>> getVisitaAgrupadaByAnioAndMesAndTutor(@PathVariable Integer anio,@PathVariable Integer mes, @PathVariable String tutor) {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaAgrupada> listaVisitas = visitaTutorService.getVisitaAgrupadaByAnioAndMesAndTutor(anio, mes,tutor);
		return ResponseEntity.ok((List<VisitaAgrupada>) listaVisitas);
	}

	

	@GetMapping("/visitas-tutor/agrupada/{centro}/{anio}/{mes}")
	@Timed
	public ResponseEntity<List<VisitaAgrupada>> getVisitaAgrupadaByAnioAndMes(@PathVariable String centro, @PathVariable Integer anio,@PathVariable Integer mes) {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaAgrupada> listaVisitas = visitaTutorService.getVisitaAgrupadaByCentroAndAnioAndMes(centro, anio, mes);
		return ResponseEntity.ok((List<VisitaAgrupada>) listaVisitas);
	}	
	

	@GetMapping("/visitas-tutor/agrupada/tutor/{centro}/{anio}/{mes}/{tutor}")
	@Timed
	public ResponseEntity<List<VisitaAgrupada>> getVisitaAgrupadaByAnioAndMesAndTutor(@PathVariable String centro, @PathVariable Integer anio,@PathVariable Integer mes,@PathVariable String tutor) {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaAgrupada> listaVisitas = visitaTutorService.getVisitaAgrupadaByCentroAndAnioAndMesAndTutor(centro, anio, mes,tutor);
		return ResponseEntity.ok((List<VisitaAgrupada>) listaVisitas);
	}	
	
	
	@GetMapping("/visitas-tutor/agrupada/anio/{centro}/{anio}/{mes}/{idTipoPractica}/{cursoAca}")
	@Timed
	public ResponseEntity<List<VisitaAgrupada>> getVisitaAgrupadaByAnioAndMesTipoPractica(@PathVariable String centro, @PathVariable Integer anio,@PathVariable Integer mes, @PathVariable Integer idTipoPractica,@PathVariable Integer cursoAca) {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaAgrupada> listaVisitas = visitaTutorService.getVisitaAgrupadaByCentroAndAnioAndMesAndTipoPractica(centro, anio, mes, idTipoPractica,cursoAca);
		return ResponseEntity.ok((List<VisitaAgrupada>) listaVisitas);
	}	
	

	@GetMapping("/visitas-tutor/agrupada/tutor/{centro}/{anio}/{mes}/{idTipoPractica}/{tutor}")
	@Timed
	public ResponseEntity<List<VisitaAgrupada>> getVisitaAgrupadaByAnioAndMesTipoPracticaAndTutor(@PathVariable String centro, @PathVariable Integer anio,@PathVariable Integer mes, @PathVariable Integer idTipoPractica,@PathVariable String tutor) {
		log.debug("REST request to get all VisitaTutors");
		Iterable<VisitaAgrupada> listaVisitas = visitaTutorService.getVisitaAgrupadaByCentroAndAnioAndMesAndTipoPracticaAndTutor(centro, anio, mes, idTipoPractica, tutor);
		return ResponseEntity.ok((List<VisitaAgrupada>) listaVisitas);
	}	

	
	/**
	 * GET /visitas-tutores : get all the visitaTutors by tutor.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of visitaTutors
	 *         in body
	 */
	@GetMapping("/profesores/{ca_profesor}/visitas-tutores")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getAllVisitaTutoresByTutor(@PathVariable(value = "ca_profesor") String idProfesor,
			Pageable pageable) {
		log.debug("REST request to get all VisitaTutoresByTutor");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getVisitaTutoriaByProfesor(idProfesor);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);
	}
	
	/**
	 * GET /visitas-tutores : get all the visitaTutors by tutor and Autorizada.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of visitaTutors
	 *         in body
	 */
	@GetMapping("/tutores/visitas-tutores/{idProfesor}/{autorizada}/{mes}/{anio}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getAllVisitaTutoresByProfesorByAutorizadaByMesByAnio(@PathVariable String idProfesor, @PathVariable Boolean autorizada, @PathVariable Integer mes, @PathVariable Integer anio) {
		log.debug("REST request to get VisitaTutor : {}", idProfesor, autorizada);
		try {
			return ResponseEntity.ok(visitaTutorService.getVisitaTutoriaByProfesorAndAutorizadaAndMesAndAnio(idProfesor, autorizada, mes, anio) );
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}


	@GetMapping("/visitas-tutor/profesor/{ca_profesor}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getVisitasByTutor(@PathVariable(value = "ca_profesor") String idProfesor) {
		log.debug("REST request to get all VisitaTutoresByTutor");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getVisitaTutoriaByProfesor(idProfesor);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);

	}
	
	@GetMapping("/visitas-tutor/profesor/{ca_profesor}/{idAnio}/{mes}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getVisitasByTutorAndAnioAndMes(@PathVariable(value = "ca_profesor") String idProfesor,@PathVariable(value = "idAnio") Integer idAnio,@PathVariable(value = "mes") Integer mes ) {
		log.debug("REST request to get all VisitaTutoresByTutor");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getVisitaTutoriaByProfesorAndAnioAndMes(idProfesor,idAnio,mes);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);

	}
	
	@GetMapping("/visitas-tutor/profesorTipoPractica/{ca_profesor}/{idAnio}/{mes}/{idTipoPractica}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getVisitasByTutorAndAnioAndMesAndTipoPractica(@PathVariable(value = "ca_profesor") String idProfesor,@PathVariable(value = "idAnio") Integer idAnio,@PathVariable(value = "mes") Integer mes, @PathVariable(value = "idTipoPractica") Long idTipoPractica ) {
		log.debug("REST request to get all VisitaTutoresByTutor");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getVisitaTutoriaByProfesorAndAnioAndMesAndTipoPractica(idProfesor,idAnio,mes,idTipoPractica);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);

	}
	
	@GetMapping("/visitas-tutor/profesor/{ca_profesor}/{idAnio}/{mes}/{idCiclo}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getVisitasByTutorAndAnioAndMesAndCiclo(@PathVariable(value = "ca_profesor") String idProfesor,@PathVariable(value = "idAnio") Integer idAnio,@PathVariable(value = "mes") Integer mes , @PathVariable(value = "idCiclo") String idCiclo) {
		log.debug("REST request to get all VisitaTutoresByTutor");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getVisitaTutoriaByProfesorAndAnioAndMesAndCiclo(idProfesor,idAnio,mes,idCiclo);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);

	}
	
	
	@GetMapping("/visitas-tutor/profesor/{ca_profesor}/{idAnio}/{mes}/{idCiclo}/{idTipoPractica}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> getVisitasByTutorAndAnioAndMesAndCicloAndTipoPractica(@PathVariable(value = "ca_profesor") String idProfesor,@PathVariable(value = "idAnio") Integer idAnio,@PathVariable(value = "mes") Integer mes , @PathVariable(value = "idCiclo") String idCiclo,@PathVariable(value = "idTipoPractica") Long idTipoPractica) {
		log.debug("REST request to get all VisitaTutoresByTutor");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.getVisitaTutoriaByProfesorAndAnioAndMesAndCicloAndTipoPractica(idProfesor,idAnio,mes,idCiclo, idTipoPractica);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);

	}
	
	
	
	@GetMapping("/visitas-tutor/findByAnioCentroProfesor/{idProfesor}/{idAnio}/{idCentro}")
	@Timed
	public ResponseEntity<List<VisitaTutor>> findByAnioCentroProfesor(@PathVariable String idProfesor,@PathVariable Integer idAnio, @PathVariable String idCentro ) {
		log.debug("REST request to get all findByAnioCentroProfesor");
		Iterable<VisitaTutor> listaVisitas = visitaTutorService.findByAnioCentroProfesor(idProfesor,idAnio,idCentro);
		return ResponseEntity.ok((List<VisitaTutor>) listaVisitas);

	}
	
	
	
	/**
	 * GET /visitas-tutores/:id : get the "id" visitaTutor.
	 *
	 * @param id
	 *            the id of the visitaTutor to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         visitaTutor, or with status 404 (Not Found)
	 */
	@GetMapping("/visitas-tutor/{id}")
	@Timed
	public ResponseEntity<VisitaTutor> getVisitaTutor(@PathVariable Long id) {
		log.debug("REST request to get VisitaTutor : {}", id);
		try {
			return ResponseEntity.ok(visitaTutorService.getVisitaTutoria(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /visitas-tutores/:id : delete the "id" visitaTutor.
	 *
	 * @param id
	 *            the id of the visitaTutor to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/visitas-tutor/{id}")
	@Timed
	public ResponseEntity<String> deleteVisitaTutor(@PathVariable Long id) {
		log.debug("REST request to delete VisitaTutor : {}", id);
		  try {
			  visitaTutorService.deleteTutor(id);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
	                    .body(e.getMessage());
	        }
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.CriterioEvaluacion;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.repository.CriterioEvaluacionRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CriterioEvaluacionService {

	private static final String ENTITY_NAME = "criterioEvaluacion";

	@Autowired
	private CriterioEvaluacionRepository criterioRepository;
	
    @Autowired    
    private CicloRepository cicloRepository;


	public ResponseEntity<CriterioEvaluacion> createCriterioEvaluacion(final CriterioEvaluacion nuevoCriterio) throws URISyntaxException {
		log.debug("SERVICE request to save CriterioEvaluacion : {}", nuevoCriterio);

		if (nuevoCriterio.getIdCriterio() != null) {
			throw new BadRequestAlertException("A new Criterio Evaluacion cannot already have an ID", ENTITY_NAME, "idexists");
		}

		CriterioEvaluacion result = criterioRepository.save(nuevoCriterio);
		return ResponseEntity.created(new URI("/api/criterios/" + result.getIdCriterio()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdCriterio().toString()))
				.body(result);
	}

	public CriterioEvaluacion getCriterioEvaluacion(final Long idCriterioEvaluacion) {
		Optional<CriterioEvaluacion> criterio = criterioRepository.findById(idCriterioEvaluacion);
		if (!criterio.isPresent()) {
			throw new IllegalArgumentException("No existe un criterio con ese identificador.");
		}
		return criterio.get();
	}

	public List<CriterioEvaluacion> getAllCriterios() {
		return criterioRepository.findAll();
	}
	
	
	public List<CriterioEvaluacion> getAllCriteriosByCiclo(final String idCiclo ,Pageable pageable) {
		
		 log.debug("SERVICE request to get all RealizacionsEvaluacion by Ciclo");  
		 Ciclo ciclo = cicloRepository.getOne(idCiclo);	      
	        if (ciclo == null) {
				throw new IllegalArgumentException("No existe el ciclo referido en la busqueda.");
			}
	        return criterioRepository.findAllByCiclo(ciclo,pageable);
	}

	
	
	public CriterioEvaluacion getCriteriosByCiclo (final String	 idCiclo) {
				
		 log.debug("SERVICE request to get all CriteriosEvaluacion by Ciclo");  
		 Ciclo ciclo = cicloRepository.getOne(idCiclo);	       
	        if (ciclo == null) {
				throw new IllegalArgumentException("No existe el ciclo referido en la busqueda.");
			}
	        
	        Optional<CriterioEvaluacion> criterio = criterioRepository.findCriterioByCiclo(cicloRepository.getOne(idCiclo));
	    	if (!criterio.isPresent()) {
				//throw new IllegalArgumentException("No existe un criterio con el ciclo suministrado.");
	    		return new CriterioEvaluacion();
			}
			return criterio.get();        
	}
	 
	

	public ResponseEntity<CriterioEvaluacion> updateCriterioEvaluacion(final CriterioEvaluacion criterioModificado) throws URISyntaxException {
		log.debug("SERVICE request to update CriterioEvaluacion : {}", criterioModificado);
		if (criterioModificado.getIdCriterio()== null) {
			return createCriterioEvaluacion(criterioModificado);
		}

		CriterioEvaluacion result = criterioRepository.save(criterioModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, criterioModificado.getIdCriterio().toString()))
				.body(result);

	}
	public void deleteCriterioEvaluacion(final Long idCriterioEvaluacion) {
		Optional<CriterioEvaluacion> criterio = criterioRepository.findById(idCriterioEvaluacion);
		if (!criterio.isPresent()) {
			throw new IllegalArgumentException("No existe un criterio con ese identificador.");
		}
		
		criterioRepository.deleteById(idCriterioEvaluacion);
	}
	
}

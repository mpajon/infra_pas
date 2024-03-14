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
import es.princast.gepep.domain.ResultadoAprendizaje;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.repository.ResultadoAprendizajeRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ResultadoAprendizajeService {

	private static final String ENTITY_NAME = "resultadoAprendizaje";

	@Autowired
	private ResultadoAprendizajeRepository resultadoRepository;
	
	  
    @Autowired    
    private CicloRepository cicloRepository;

    
	public ResponseEntity<ResultadoAprendizaje> createResultadoAprendizaje(final ResultadoAprendizaje nuevoResultado) throws URISyntaxException {
		log.debug("SERVICE request to save ResultadoAprendizaje : {}", nuevoResultado);

		if (nuevoResultado.getIdResultadoAprendizaje() != null) {
			throw new BadRequestAlertException("A new Resultado Aprendizaje cannot already have an ID", ENTITY_NAME, "idexists");
		}

		ResultadoAprendizaje result = resultadoRepository.save(nuevoResultado);
		return ResponseEntity.created(new URI("/api/resultados-aprendizaje/" + result.getIdResultadoAprendizaje()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdResultadoAprendizaje().toString()))
				.body(result);
	}

	public ResultadoAprendizaje getResultadoAprendizaje(final Long idResultadoAprendizaje) {
		Optional<ResultadoAprendizaje> resultado = resultadoRepository.findById(idResultadoAprendizaje);
		if (!resultado.isPresent()) {
			throw new IllegalArgumentException("No existe un resultado con ese identificador.");
		}
		return resultado.get();
	}

	public List<ResultadoAprendizaje> getAllResultados() {
		return resultadoRepository.findAll();
	}
	
	
	
	public List<ResultadoAprendizaje> getAllResultadosByCiclo(String idCiclo ,Pageable pageable) {
				
		 log.debug("SERVICE request to get all ResultadosEvaluacion by Ciclo");  
		 Ciclo ciclo = cicloRepository.getOne(idCiclo);	      
	        if (ciclo == null) {
				throw new IllegalArgumentException("No existe el ciclo referido en la busqueda.");
			}
	     return resultadoRepository.findAllByCiclo(ciclo,pageable);	        
	}
	
	
	public ResultadoAprendizaje getResultadosByCiclo (final String	 idCiclo) {
		
		 log.debug("SERVICE request to get all ResultadosEvaluacion by Ciclo");  
		 Ciclo ciclo = cicloRepository.getOne(idCiclo);	       
	        if (ciclo == null) {
				throw new IllegalArgumentException("No existe el ciclo referido en la busqueda.");
			}
	        
	        Optional<ResultadoAprendizaje> criterio = resultadoRepository.findResultadoByCiclo(cicloRepository.getOne(idCiclo));
	    	if (!criterio.isPresent()) {
				return new ResultadoAprendizaje();
			}
			return criterio.get();        
	}
	

	public ResponseEntity<ResultadoAprendizaje> updateResultadoAprendizaje(final ResultadoAprendizaje resultadoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update ResultadoAprendizaje : {}", resultadoModificado);
		if (resultadoModificado.getIdResultadoAprendizaje()== null) {
			return createResultadoAprendizaje(resultadoModificado);
		}

		ResultadoAprendizaje result = resultadoRepository.save(resultadoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resultadoModificado.getIdResultadoAprendizaje().toString()))
				.body(result);

	}
	public void deleteResultadoAprendizaje(final Long idResultadoAprendizaje) {
		Optional<ResultadoAprendizaje> resultado = resultadoRepository.findById(idResultadoAprendizaje);
		if (!resultado.isPresent()) {
			throw new IllegalArgumentException("No existe un resultado con ese identificador.");
		}
		
		resultadoRepository.deleteById(idResultadoAprendizaje);
	}
	
}

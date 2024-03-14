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
import es.princast.gepep.domain.Realizacion;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.repository.RealizacionRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RealizacionService {

	private static final String ENTITY_NAME = "realizacion";

	@Autowired
	private RealizacionRepository realizacionRepository;
	
	  
    @Autowired    
    private CicloRepository cicloRepository;


	public ResponseEntity<Realizacion> createRealizacion(final Realizacion nuevaRealizacion) throws URISyntaxException {
		log.debug("SERVICE request to save Realizacion : {}", nuevaRealizacion);

		if (nuevaRealizacion.getIdRealizacion() != null) {
			throw new BadRequestAlertException("A new ciclo cannot already have an ID", ENTITY_NAME, "idexists");
		}

		Realizacion result = realizacionRepository.save(nuevaRealizacion);
		return ResponseEntity.created(new URI("/api/realizaciones/" + result.getIdRealizacion()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdRealizacion().toString()))
				.body(result);
	}

	public Realizacion getRealizacion(final Long idRealizacion) {
		Optional<Realizacion> realizacion = realizacionRepository.findById(idRealizacion);
		if (!realizacion.isPresent()) {
			throw new IllegalArgumentException("No existe una realizacion con ese identificador.");
		}
		return realizacion.get();
	}

	public List<Realizacion> getAllRealizaciones() {
		return realizacionRepository.findAll();
	}
	
	public Realizacion getRealizacionByCiclo(final String idCiclo ) {
		
		 log.debug("SERVICE request to get all RealizacionsEvaluacion by Ciclo");  
		 Ciclo ciclo = cicloRepository.getOne(idCiclo);	       
	        if (ciclo == null) {
				throw new IllegalArgumentException("No existe el ciclo referido en la busqueda.");
			}
	        
	        Optional<Realizacion> realizacion = realizacionRepository.findRealizacionByCiclo(cicloRepository.getOne(idCiclo));
			if (!realizacion.isPresent()) {
				return new Realizacion();
			}
			return realizacion.get();
	       
	}
	
 
	
	public List<Realizacion> getAllRealizacionesByCiclo(final String idCiclo ,Pageable pageable) {
				
		 log.debug("SERVICE request to get all RealizacionsEvaluacion by Ciclo");  
		 Ciclo ciclo = cicloRepository.getOne(idCiclo);	      
	        if (ciclo == null) {
				throw new IllegalArgumentException("No existe el ciclo referido en la busqueda.");
			}
	        return realizacionRepository.findAllByCiclo(ciclo,pageable);
	}

	public ResponseEntity<Realizacion> updateRealizacion(final Realizacion realizacionModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Realizacion : {}", realizacionModificado);
		if (realizacionModificado.getIdRealizacion()== null) {
			return createRealizacion(realizacionModificado);
		}

		Realizacion result = realizacionRepository.save(realizacionModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, realizacionModificado.getIdRealizacion().toString()))
				.body(result);

	}
	public void deleteRealizacion(final Long idRealizacion) {
		Optional<Realizacion> realizacion = realizacionRepository.findById(idRealizacion);
		if (!realizacion.isPresent()) {
			throw new IllegalArgumentException("No existe una realizacion con ese identificador.");
		}
		
		realizacionRepository.deleteById(idRealizacion);
	}
	
}

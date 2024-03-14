package es.princast.gepep.service.saucesincro;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.repository.CentroRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroCentroService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "centro";

	@Autowired
	private CentroRepository centroRepository;
	
	 @Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<Centro> createCentro(final Centro nuevoCentro) throws URISyntaxException {
		log.debug("SERVICE request to save Centro : {}", nuevoCentro);		
 
	 	  if (nuevoCentro.getIdCentro() == null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_centro')").getSingleResult();
			String generatedId =  nextId.toString();
			nuevoCentro.setIdCentro(generatedId);		    	 
		 }	 
	 	nuevoCentro.setCreatedBy(SauceSincroService.USU_SINCRO);
	 	Centro result = centroRepository.save(nuevoCentro); 
	 	incrementarInsertados();
	 	return ResponseEntity.created(new URI("/api/centros/" + result.getIdCentro()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdCentro().toString()))
				.body(result);
		
	}

	public Centro getCentro(final String idCentro) {
		Optional<Centro> centro = centroRepository.findById(idCentro);
		if (!centro.isPresent()) {
			throw new IllegalArgumentException("No existe un centro con ese identificador.");
		}
		return centro.get();
	}
	
	
	public ResponseEntity<Centro> updateCentro(final Centro centroModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Centro : {}", centroModificado);
		if (centroModificado.getIdCentro() == null) {
			return createCentro(centroModificado);
		}
		
		centroModificado.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		Centro result = centroRepository.save(centroModificado);
		incrementarActualizados();
		return ResponseEntity.ok()
							.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, centroModificado.getIdCentro().toString()))
							.body(result);
	 
	}

	
}

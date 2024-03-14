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

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroCicloService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "ciclo";

	@Autowired
	private CicloRepository cicloRepository;
	
	 @Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<Ciclo> createCiclo(final Ciclo nuevoCiclo) throws URISyntaxException {
		log.debug("SERVICE request to save Ciclo : {}", nuevoCiclo);
		
		 if (nuevoCiclo.getIdCiclo()==null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_ciclo')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevoCiclo.setIdCiclo(generatedId);		    	 
			 }	
		nuevoCiclo.setCreatedBy(SauceSincroService.USU_SINCRO);
		Ciclo result = cicloRepository.save(nuevoCiclo);
		incrementarInsertados();
		return ResponseEntity.created(new URI("/api/ciclos/" + result.getIdCiclo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdCiclo().toString()))
				.body(result);
	}

	public Ciclo getCiclo(final String idCiclo) {
		Optional<Ciclo> ciclo = cicloRepository.findById(idCiclo);
		if (!ciclo.isPresent()) {
			throw new IllegalArgumentException("No existe una ciclo con ese identificador.");
		}
		return ciclo.get();
	}

	
	public ResponseEntity<Ciclo> updateCiclo(final Ciclo cicloModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Ciclo : {}", cicloModificado);
		if (cicloModificado.getIdCiclo() == null) {
			return createCiclo(cicloModificado);
		}		 
		cicloModificado.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		 Ciclo result = cicloRepository.save(cicloModificado);
		 incrementarActualizados();
			return ResponseEntity.ok()
					.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cicloModificado.getIdCiclo().toString()))
					.body(result);

	} 
	

	
}

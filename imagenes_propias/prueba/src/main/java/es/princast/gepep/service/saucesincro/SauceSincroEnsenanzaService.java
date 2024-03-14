package es.princast.gepep.service.saucesincro;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.repository.EnsenanzaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroEnsenanzaService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "Ensenanza";

	@Autowired
	private EnsenanzaRepository EnsenanzaRepository;

	@Autowired
	private MessageSource messageSource;
	 
	 @Autowired
	 private EntityManager entityManager; 
	
	 
	public Ensenanza getEnsenanza(final String idEnsenanza) {
		Optional<Ensenanza> Ensenanza = EnsenanzaRepository.findByIdEnsenanza(idEnsenanza);
		if (!Ensenanza.isPresent()){
       	  throw new IllegalArgumentException(messageSource.getMessage("error.ensenanza.id.no.encontrado",
	                   null, LocaleContextHolder.getLocale()));	   
       }
		 
		return Ensenanza.get();
	}
 

	public ResponseEntity<Ensenanza> updateEnsenanza(final Ensenanza ensenanzaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Ensenanza : {}", ensenanzaModificada);
		if (ensenanzaModificada.getIdEnsenanza() == null) {
			return createEnsenanza(ensenanzaModificada);
		}
		ensenanzaModificada.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		Ensenanza result = EnsenanzaRepository.save(ensenanzaModificada);
		this.incrementarActualizados();
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ensenanzaModificada.getIdEnsenanza().toString()))
				.body(result);

	}


	public ResponseEntity<Ensenanza> createEnsenanza(final Ensenanza nuevaEnsenanza) throws URISyntaxException {
		log.debug("SERVICE request to save Ensenanza : {}", nuevaEnsenanza);	
	 	  if (nuevaEnsenanza.getIdEnsenanza()==null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_ensenanza')").getSingleResult();
			String generatedId =  nextId.toString();
		    nuevaEnsenanza.setIdEnsenanza(generatedId);		    	 
		 }	 
	 	 nuevaEnsenanza.setCreatedBy(SauceSincroService.USU_SINCRO);
	 	 Ensenanza result = EnsenanzaRepository.save(nuevaEnsenanza); 
	 	this.incrementarInsertados();
		return ResponseEntity.created(new URI("/api/Ensenanzas/" + result.getIdEnsenanza()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdEnsenanza().toString()))
				.body(result);
		
	}
		
	
}

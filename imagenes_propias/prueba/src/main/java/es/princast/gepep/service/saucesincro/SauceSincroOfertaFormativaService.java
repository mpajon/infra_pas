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

import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.repository.OfertaFormativaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroOfertaFormativaService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "ofertaFormativa";

	@Autowired
	private OfertaFormativaRepository ofertaFormativaRepository;
	
	
	@Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<OfertaFormativa> createOfertaFormativa(final OfertaFormativa nuevaOfertaFormativa) throws URISyntaxException {
		log.debug("SERVICE request to save OfertaFormativa : {}", nuevaOfertaFormativa);

		 if (nuevaOfertaFormativa.getIdOfertaFormativa() == null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_oferta_formativa')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevaOfertaFormativa.setIdOfertaFormativa(generatedId);		    	 
			 }	 
		nuevaOfertaFormativa.setCreatedBy(SauceSincroService.USU_SINCRO);
		OfertaFormativa result = ofertaFormativaRepository.save(nuevaOfertaFormativa);
		incrementarInsertados();
		return ResponseEntity.created(new URI("/api/ofertas-formativas/" + result.getIdOfertaFormativa()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdOfertaFormativa().toString()))
				.body(result);
	}
	
	

	public OfertaFormativa getOfertaFormativa(final String idOfertaFormativa) {
		Optional<OfertaFormativa> ofertaFormativa = ofertaFormativaRepository.findById(idOfertaFormativa);
		if (!ofertaFormativa.isPresent()) {
			throw new IllegalArgumentException("No existe una ofertaFormativa con ese identificador.");
		}	
		
		return ofertaFormativa.get();
	}
	
	
	
	public ResponseEntity<OfertaFormativa> updateOfertaFormativa(final OfertaFormativa ofertaFormativaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update OfertaFormativa : {}", ofertaFormativaModificada);
		if (ofertaFormativaModificada.getIdOfertaFormativa() == null) {
			return createOfertaFormativa(ofertaFormativaModificada);
		}
		ofertaFormativaModificada.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		OfertaFormativa result = ofertaFormativaRepository.save(ofertaFormativaModificada);
		incrementarActualizados();
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ofertaFormativaModificada.getIdOfertaFormativa().toString()))
				.body(result);
	}



}

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

import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.repository.OfertasCentroRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroOfertasCentroService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "ofertasCentro";

	@Autowired
	private OfertasCentroRepository ofertasCentroRepository;


	
	@Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<OfertasCentro> createOfertasCentro(final OfertasCentro nuevaOfertasCentro) throws URISyntaxException {
		log.debug("SERVICE request to save OfertasCentro : {}", nuevaOfertasCentro);

		 if (nuevaOfertasCentro.getIdOfertaCentro() == null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_ofertas_centro')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevaOfertasCentro.setIdOfertaCentro(generatedId);		    	 
			 }	 
		nuevaOfertasCentro.setCreatedBy(SauceSincroService.USU_SINCRO);
		OfertasCentro result = ofertasCentroRepository.save(nuevaOfertasCentro);
		this.incrementarInsertados();
		return ResponseEntity.created(new URI("/api/ofertas-centro/" + result.getIdOfertaCentro()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdOfertaCentro().toString()))
				.body(result);
	}
	
	
	public OfertasCentro getOfertasCentro(final String idOfertasCentro) {
		Optional<OfertasCentro> OfertasCentro = ofertasCentroRepository.findById(idOfertasCentro);
		if (!OfertasCentro.isPresent()) {
			throw new IllegalArgumentException("No existe una OfertasCentro con ese identificador.");
		}
		return OfertasCentro.get();
	}
	

	public ResponseEntity<OfertasCentro> updateOfertasCentro(final OfertasCentro OfertasCentroModificada) throws URISyntaxException {
		log.debug("SERVICE request to update OfertasCentro : {}", OfertasCentroModificada);
		if (OfertasCentroModificada.getIdOfertaCentro() == null) {
			return createOfertasCentro(OfertasCentroModificada);
		}
		OfertasCentroModificada.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		OfertasCentro result = ofertasCentroRepository.save(OfertasCentroModificada);
		this.incrementarActualizados();
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, OfertasCentroModificada.getIdOfertaCentro().toString()))
				.body(result);

	}


}

package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.repository.CentroRepository;
import es.princast.gepep.repository.OfertasCentroRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OfertasCentroService {

	private static final String ENTITY_NAME = "ofertasCentro";

	@Autowired
	private OfertasCentroRepository ofertasCentroRepository;

	@Autowired
	private CentroRepository centroRepository;
	
	@Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<OfertasCentro> createOfertasCentro(final OfertasCentro nuevaOfertasCentro) throws URISyntaxException {
		log.debug("SERVICE request to save OfertasCentro : {}", nuevaOfertasCentro);

		 if (nuevaOfertasCentro.getIdOfertaCentro() == null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_ofertas_centro')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevaOfertasCentro.setIdOfertaCentro(generatedId);		    	 
			 }	 
		OfertasCentro result = ofertasCentroRepository.save(nuevaOfertasCentro);
		return ResponseEntity.created(new URI("/api/ofertas-centro/" + result.getIdOfertaCentro()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdOfertaCentro().toString()))
				.body(result);
	}
	
	public void createOfertasCentroTodos(final OfertasCentro ofertaCentro) throws URISyntaxException {
		log.debug("SERVICE request to save OfertasCentro : {}", ofertaCentro);
		
		List <Centro> listaCentros = centroRepository.findAllByFechaBajaIsNull();

		OfertasCentro nuevaOfertaCentro = new OfertasCentro();
		
		for (int i=0; i<listaCentros.size();i++) {			
			nuevaOfertaCentro.setVigente(true);
			nuevaOfertaCentro.setOferta(ofertaCentro.getOferta());			 
			nuevaOfertaCentro.setCentro(listaCentros.get(i));
			ResponseEntity<OfertasCentro> nueva = this.createOfertasCentro(nuevaOfertaCentro);
			nuevaOfertaCentro = new OfertasCentro();
		}
	}

	public OfertasCentro getOfertasCentro(final String idOfertasCentro) {
		Optional<OfertasCentro> OfertasCentro = ofertasCentroRepository.findById(idOfertasCentro);
		if (!OfertasCentro.isPresent()) {
			throw new IllegalArgumentException("No existe una OfertasCentro con ese identificador.");
		}
		return OfertasCentro.get();
	}
	
	public OfertasCentro getOfertasCentroByCentro(final String idOfertasCentro) {
		Optional<OfertasCentro> OfertasCentro = ofertasCentroRepository.findById(idOfertasCentro);
		if (!OfertasCentro.isPresent()) {
			throw new IllegalArgumentException("No existe una OfertasCentro con ese identificador.");
		}
		return OfertasCentro.get();
	}

	public  List<OfertasCentro>  getAllOfertasCentros() {
		 log.debug("SERVICE request to get all OfertasCentro");
		 return ofertasCentroRepository.findAll();
	}

	public ResponseEntity<OfertasCentro> updateOfertasCentro(final OfertasCentro OfertasCentroModificada) throws URISyntaxException {
		log.debug("SERVICE request to update OfertasCentro : {}", OfertasCentroModificada);
		if (OfertasCentroModificada.getIdOfertaCentro() == null) {
			return createOfertasCentro(OfertasCentroModificada);
		}

		OfertasCentro result = ofertasCentroRepository.save(OfertasCentroModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, OfertasCentroModificada.getIdOfertaCentro().toString()))
				.body(result);

	}

	public void deleteOfertasCentro(final String idOfertasCentro) {
		Optional<OfertasCentro> OfertasCentro = ofertasCentroRepository.findById(idOfertasCentro);
		if (!OfertasCentro.isPresent()) {
			throw new IllegalArgumentException("No existe una OfertasCentro con ese identificador.");
		}
		
		ofertasCentroRepository.deleteById(idOfertasCentro);
	}

}

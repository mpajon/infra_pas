package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.NivelEducativo;
import es.princast.gepep.repository.NivelEducativoRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class NivelEducativoService {

	 private static final String ENTITY_NAME = "nivelEducativo";
	 
	@Autowired
    private NivelEducativoRepository nivelEducativoRepository;
	
	
	 public NivelEducativo getNivelEducativo(final Integer idNivel) {
	        Optional<NivelEducativo> nivelEducativo = nivelEducativoRepository.findById(idNivel);
	        if (!nivelEducativo.isPresent()) {
	            throw new IllegalArgumentException("No existe un nivelEducativo con ese identificador.");
	        }
	        return nivelEducativo.get();
	    }


	private Sort sortByNombreAsc() {
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nivel").ignoreCase();
		return Sort.by(order);
	}


	public List<NivelEducativo> getAllNivelEducativos() {
		return (List<NivelEducativo>) nivelEducativoRepository.findAll(sortByNombreAsc());
	}

	public ResponseEntity<NivelEducativo> updateNivelEducativo(final NivelEducativo nivelEducativoModificado)
			throws URISyntaxException {
		log.debug("SERVICE request to update NivelEducativo : {}", nivelEducativoModificado);
		if (nivelEducativoModificado.getIdNivel() == null) {
			return createNivelEducativo(nivelEducativoModificado);
		}

		NivelEducativo result = nivelEducativoRepository.save(nivelEducativoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nivelEducativoModificado.getIdNivel().toString()))
				.body(result);
	}

	public void deleteNivelEducativo(final Integer idNivel) {
		Optional<NivelEducativo> nivelEducativo = nivelEducativoRepository.findById(idNivel);
		if (!nivelEducativo.isPresent()) {
			throw new IllegalArgumentException("No existe un nivelEducativo con ese identificador.");
		}

		nivelEducativoRepository.deleteById(idNivel);
	}

	public ResponseEntity<NivelEducativo> createNivelEducativo(final NivelEducativo nuevoNivelEducativo)
			throws URISyntaxException {
		log.debug("SERVICE request to save NivelEducativo : {}", nuevoNivelEducativo);
		if (nuevoNivelEducativo.getIdNivel() != null) {
			throw new BadRequestAlertException("A new NivelEducativo cannot already have an ID", ENTITY_NAME, "idexists");
		}

		NivelEducativo result = nivelEducativoRepository.save(nuevoNivelEducativo);
		return ResponseEntity.created(new URI("/api/nivel-educativo/" + result.getIdNivel()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdNivel().toString()))
				.body(result);
	}

	    
	    

}



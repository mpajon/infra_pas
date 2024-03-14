package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Precios;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.repository.PreciosRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PreciosService {

	private static final String ENTITY_NAME = "precios";

	@Autowired
	private PreciosRepository preciosRepository;
	
	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;


	public ResponseEntity<Precios> createPrecios(final Precios nuevoPrecios) throws URISyntaxException {
		log.debug("SERVICE request to save Precios : {}", nuevoPrecios);

		if (nuevoPrecios.getIdPrecio() != null) {
			throw new BadRequestAlertException("A new precios cannot already have an ID", ENTITY_NAME, "idexists");
		}
		
		Optional<Precios> precios = preciosRepository.findPreciosByTipoPractica(nuevoPrecios.getTipoPractica());		
		 if (precios.isPresent()) {
			throw new IllegalArgumentException("La práctica ya tiene precios definidos. Actualízelos por favor.");
		}
			 
		Precios result = preciosRepository.save(nuevoPrecios);
		return ResponseEntity.created(new URI("/api/precios/" + result.getIdPrecio()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPrecio().toString()))
				.body(result);
	}

	public Precios getPrecios(final Long idPrecios) {
		Optional<Precios> precios = preciosRepository.findById(idPrecios);
		if (!precios.isPresent()) {
			throw new IllegalArgumentException("No existe un precios con ese identificador.");
		}
		return precios.get();
	}

	public List<Precios> getAllPrecios() {
		return preciosRepository.findAll();
	}

	public ResponseEntity<Precios> updatePrecios(final Precios preciosModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Precios : {}", preciosModificado);
		if (preciosModificado.getIdPrecio() == null) {
			return createPrecios(preciosModificado);
		}
		
		 
		Precios result = preciosRepository.save(preciosModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, preciosModificado.getIdPrecio().toString()))
				.body(result);

	}
	
	public Precios getPreciosByTipoPractica (final Long idTipoPractica ) {
		
		 log.debug("SERVICE request to get all Precios By TipoPractica");
		 TipoPractica practica = tipoPracticaRepository.getOne(idTipoPractica);		 
	        if (practica == null) {
				throw new IllegalArgumentException("No existe tipo practica referido en la busqueda.");
			}
	        
	        Optional<Precios> precios = preciosRepository.findPreciosByTipoPractica(practica);
			if (!precios.isPresent()) {
				throw new IllegalArgumentException("No existen precios para la practica indicada.");		 
			}
			return precios.get();
	       
	}
	

	public void deletePrecios(final Long idPrecios) {
		Optional<Precios> precios = preciosRepository.findById(idPrecios);
		if (!precios.isPresent()) {
			throw new IllegalArgumentException("No existen precios con ese identificador el sistema.");
		}	 

		preciosRepository.deleteById(idPrecios);
	}
	
}

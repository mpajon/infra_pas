package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Clausula;
import es.princast.gepep.repository.ClausulaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ClausulaService {

	private static final String ENTITY_NAME = "clausula";

	@Autowired
	private ClausulaRepository clausulaRepository;

	@Autowired
	private MessageSource messageSource;

	public ResponseEntity<Clausula> createClausula(final Clausula nuevaClausula) throws URISyntaxException {
		log.debug("SERVICE request to save Clausula : {}", nuevaClausula);

		if (nuevaClausula.getIdClausula() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.clausula.nuevo.id", null, LocaleContextHolder.getLocale()));
		}	
		  
	 	 List <Clausula> clausulaDuplicada  = this.clausulaRepository.findAllByTexto(nuevaClausula.getTexto());
	 	 if (clausulaDuplicada.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.clausula.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
		Clausula result = clausulaRepository.save(nuevaClausula);
		return ResponseEntity.created(new URI("/api/clausulas/" + result.getIdClausula()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdClausula().toString())).body(result);
	}

	public Clausula getClausula(final Long idClausula) {
		Optional<Clausula> clausula = clausulaRepository.findById(idClausula);
		if (!clausula.isPresent()) {
			throw new IllegalArgumentException("No existe una clausula con ese identificador.");
		}
		return clausula.get();
	}

	private Sort sortByOrdenAsc() {
	 
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "orden").ignoreCase();
		return Sort.by(order);
	}
	
	public List<Clausula> getAllClausulas() {
		return clausulaRepository.findAll(sortByOrdenAsc());		
	}

	public ResponseEntity<Clausula> updateClausula(final Clausula clausulaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Clausula : {}", clausulaModificada);
		if (clausulaModificada.getIdClausula() == null) {
			return createClausula(clausulaModificada);
		}
		 List <Clausula> clausulaDuplicada  = this.clausulaRepository.findAllByTexto(clausulaModificada.getTexto());
	 	 
		 if (clausulaDuplicada.size()>0)
	 	 {
			if(clausulaDuplicada.size()==1 && clausulaDuplicada.get(0).getIdClausula() == clausulaModificada.getIdClausula()) {
				Clausula result = clausulaRepository.save(clausulaModificada);
				return ResponseEntity.ok()
						.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clausulaModificada.getIdClausula().toString()))
						.body(result);

			}
			else {
				throw new IllegalArgumentException(messageSource.getMessage("error.clausula.existe",
		                   null, LocaleContextHolder.getLocale()));	 	  
			}
	 	 }
		 
	 	 
	 	 
		Clausula result = clausulaRepository.save(clausulaModificada);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clausulaModificada.getIdClausula().toString()))
				.body(result);

	}

	public void deleteClausula(final Long idClausula) {
		Optional<Clausula> clausula = clausulaRepository.findById(idClausula);
		if (!clausula.isPresent()) {
			throw new IllegalArgumentException("No existe una clausula con ese identificador.");
		}

		clausulaRepository.deleteById(idClausula);
	}

}

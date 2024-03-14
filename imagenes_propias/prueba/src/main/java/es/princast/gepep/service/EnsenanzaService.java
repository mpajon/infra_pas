package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.repository.EnsenanzaRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EnsenanzaService {

	private static final String ENTITY_NAME = "Ensenanza";

	@Autowired
	private EnsenanzaRepository EnsenanzaRepository;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;
	 
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
 
	 private Sort sortByIdAsc() {
		 	Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		 	return Sort.by(order);
	    }
	
	public List<Ensenanza> getAllEnsenanzas() {
		return EnsenanzaRepository.findAll(sortByIdAsc());
		
	}
	
	public List<Ensenanza> getAllEnsenanzasActivas() {		
		LocalDate fecha = LocalDate.now(); 
		return EnsenanzaRepository.findAllByFechaFinVigenciaOrderByNombre(fecha);
	}

	public ResponseEntity<Ensenanza> updateEnsenanza(final Ensenanza ensenanzaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Ensenanza : {}", ensenanzaModificada);
		if (ensenanzaModificada.getIdEnsenanza() == null) {
			return createEnsenanza(ensenanzaModificada);
		}
		LocalDate fecha = LocalDate.now(); 
		 List <Ensenanza> ensenanzaDuplicada  = this.EnsenanzaRepository.findAllByNombreAndFechaFinVigenciaOrderByNombre(ensenanzaModificada.getNombre(),fecha);
		 
		 if (ensenanzaDuplicada.size()>0)
	 	 {
			if(ensenanzaDuplicada.size()==1 && ensenanzaDuplicada.get(0).getIdEnsenanza().equals(ensenanzaModificada.getIdEnsenanza())) {
				Ensenanza result = EnsenanzaRepository.save(ensenanzaModificada);
				return ResponseEntity.ok().headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ensenanzaModificada.getIdEnsenanza().toString()))
						.body(result);
			}
			else {
				throw new IllegalArgumentException(messageSource.getMessage("error.ensenanza.existe",
		                   null, LocaleContextHolder.getLocale()));	 		 
			}
	 	 }
	 

		Ensenanza result = EnsenanzaRepository.save(ensenanzaModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ensenanzaModificada.getIdEnsenanza().toString()))
				.body(result);

	}

	public void deleteEnsenanza(final String idEnsenanza) {
		Optional<Ensenanza> Ensenanza = EnsenanzaRepository.findById(idEnsenanza);
		
		if (!Ensenanza.isPresent()) {
			  throw new IllegalArgumentException(messageSource.getMessage("error.ensenanza.id.no.encontrado",
	                   null, LocaleContextHolder.getLocale()));
			  }			
	    Iterable<TipoPractica> listaRelaciones = this.tipoPracticaRepository.findAllByEnsenanzas(Ensenanza.get());	    
	    if (listaRelaciones.iterator().hasNext()){ 
	    	throw new IllegalArgumentException(messageSource.getMessage("error.ensenanza.referenciado.tipopractica",
	                   null, LocaleContextHolder.getLocale()));	
	    	}
		EnsenanzaRepository.deleteById(idEnsenanza);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Ensenanza> createEnsenanza(final Ensenanza nuevaEnsenanza) throws URISyntaxException {
		log.debug("SERVICE request to save Ensenanza : {}", nuevaEnsenanza);	
	 	  if (nuevaEnsenanza.getIdEnsenanza()==null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_ensenanza')").getSingleResult();
			String generatedId =  nextId.toString();
		    nuevaEnsenanza.setIdEnsenanza(generatedId);		    	 
		 }	 
	 	 LocalDate fecha = LocalDate.now(); 
	 	 List <Ensenanza> ensenanzaDuplicada  = this.EnsenanzaRepository.findAllByNombreAndFechaFinVigenciaOrderByNombre(nuevaEnsenanza.getNombre(),fecha);
	 	 if (ensenanzaDuplicada.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.ensenanza.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }		
	 	  Ensenanza result = EnsenanzaRepository.save(nuevaEnsenanza); 
		return ResponseEntity.created(new URI("/api/Ensenanzas/" + result.getIdEnsenanza()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdEnsenanza().toString()))
				.body(result);
		
	}


}

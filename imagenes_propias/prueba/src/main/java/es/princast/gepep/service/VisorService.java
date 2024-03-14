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

import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.domain.Visor;
import es.princast.gepep.repository.EnsenanzaRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.repository.VisorRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class VisorService {

	private static final String ENTITY_NAME = "visor";

	@Autowired
	private VisorRepository visorRepository;
	
	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;

	@Autowired
	private MessageSource messageSource;

	public ResponseEntity<Visor> createVisor(final Visor nuevoVisor) throws URISyntaxException {
		log.debug("SERVICE request to save Visor : {}", nuevoVisor);

		if (nuevoVisor.getIdVisor() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.visor.nuevo.id", null, LocaleContextHolder.getLocale()));
		}
		  
	 	 List <Visor> visorDuplicado  = this.visorRepository.findAllByNombreAndApellidos(nuevoVisor.getNombre(),nuevoVisor.getApellidos());
	 	 if (visorDuplicado.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.visor.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }

		Visor result = visorRepository.save(nuevoVisor);
		return ResponseEntity.created(new URI("/api/visores/" + result.getIdVisor()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdVisor().toString()))
				.body(result);
	}

	public Visor getVisor(final Long idVisor) {
		Optional<Visor> visor = visorRepository.findById(idVisor);
		if (!visor.isPresent()) {
			throw new IllegalArgumentException("No existe una visor con ese identificador.");
		}
		return visor.get();
	}

	private Sort sortByIdAsc() {
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		return Sort.by(order);
	}
	 
	public List<Visor> getAllVisores() {
		return visorRepository.findAll(sortByIdAsc());
	}

	public ResponseEntity<Visor> updateVisor(final Visor visorModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Visor : {}", visorModificado);
		if (visorModificado.getIdVisor() == null) {
			return createVisor(visorModificado);
		}

		 List <Visor> visorDuplicado  = this.visorRepository.findAllByNombreAndApellidos(visorModificado.getNombre(),visorModificado.getApellidos());
		 
		 

		 if (visorDuplicado.size()>0)
	 	 {
			if(visorDuplicado.size()==1 && visorDuplicado.get(0).getIdVisor() == visorModificado.getIdVisor()) {
				Visor result = visorRepository.save(visorModificado);
				return ResponseEntity.ok()
						.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visorModificado.getIdVisor().toString()))
						.body(result);
			}
			else {
				throw new IllegalArgumentException(messageSource.getMessage("error.visor.existe",
		                   null, LocaleContextHolder.getLocale()));	 	 
			}
	 	 }
	 
		Visor result = visorRepository.save(visorModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visorModificado.getIdVisor().toString()))
				.body(result);

	}
	public void deleteVisor(final Long idVisor) {
		Optional<Visor> visor = visorRepository.findById(idVisor);
		if (!visor.isPresent()) {
			throw new IllegalArgumentException("No existe una visor con ese identificador.");
		}	
		
	    Iterable<TipoPractica> listaPracticas = tipoPracticaRepository.findAllByVisor(visor.get());
        if (listaPracticas.iterator().hasNext()){ 
        	   throw new IllegalArgumentException(messageSource.getMessage("error.visor.referenciado.tipopractica",
	                   null, LocaleContextHolder.getLocale()));      	
        }
		visorRepository.deleteById(idVisor);
	}
	
}

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

import es.princast.gepep.domain.HorasPeriodo;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.repository.HorasPeriodoRepository;
import es.princast.gepep.repository.PeriodoPracticaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class HorasPeriodoService {

	 private static final String ENTITY_NAME = "horasPeriodo";
	 
	@Autowired
    private PeriodoPracticaRepository periodoPracticaRepository;
	
	   
    @Autowired
    private  HorasPeriodoRepository horasPeriodoRepository;
    
	 @Autowired
	  private MessageSource messageSource;
	
	 public HorasPeriodo getHorasPeriodo(final Long idhoras) {
	        Optional<HorasPeriodo> horasPeriodo = horasPeriodoRepository.findById(idhoras);
	        if (!horasPeriodo.isPresent()) {	            
	            throw new IllegalArgumentException(messageSource.getMessage("error.periodo.id.no.encontrado",
		                   null, LocaleContextHolder.getLocale()));
	        }
	        return horasPeriodo.get();
	    }

	 private Sort sortByIdAsc() {
	       // Sort sort = new Sort(Sort.Direction.ASC, "nombre");
	        
		 	Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		 	return Sort.by(order);
	    }
	
	    public List<HorasPeriodo> getAllHoras(){
	        return horasPeriodoRepository.findAll(sortByIdAsc());
	    }
	    
	    
	    public ResponseEntity<HorasPeriodo> updateHorasPeriodo(final HorasPeriodo horasPeriodoModificado) throws URISyntaxException {
	        log.debug("SERVICE request to update periodoPractica : {}", horasPeriodoModificado);
	         if (horasPeriodoModificado.getIdHorasPeriodo() == null) {
	            return this.createHorasPeriodo(horasPeriodoModificado);
	         }
	        
	         HorasPeriodo result = horasPeriodoRepository.save(horasPeriodoModificado);
	         return ResponseEntity.ok()
	                 .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, horasPeriodoModificado.getIdHorasPeriodo().toString()))
	                 .body(result);
	    }    
 
 
	    public ResponseEntity<HorasPeriodo> createHorasPeriodo(final HorasPeriodo nuevoHorario) throws URISyntaxException {
	    	log.debug("SERVICE request to save periodoPractica : {}", nuevoHorario);	         
	       if (nuevoHorario.getIdHorasPeriodo() != null) {
	    	   
	    	   throw new IllegalArgumentException(messageSource.getMessage("error.horario.nuevo.id",
	    			   null,LocaleContextHolder.getLocale()));
	    	   
	         }
	       	      
	       HorasPeriodo result = horasPeriodoRepository.save(nuevoHorario);
	             return ResponseEntity.created(new URI("/api/horas-periodo/" + result.getIdHorasPeriodo()))
	    	             .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdHorasPeriodo().toString()))
	    	             .body(result);
             }

 	    
	
	    public void deleteHorasPeriodo(final Long idhorario) {
	        Optional<HorasPeriodo> horarioPeriodo = horasPeriodoRepository.findById(idhorario);
	        if (!horarioPeriodo.isPresent()) {
	        	   throw new IllegalArgumentException(messageSource.getMessage("error.horario.id.no.encontrado",
		                   null, LocaleContextHolder.getLocale()));
	        }
	    	 
	        Iterable<PeriodoPractica> listaPeriodos = periodoPracticaRepository.findAllByHorario(horarioPeriodo.get());
	        if (listaPeriodos.iterator().hasNext()){
	           // No se puede borrar el horario porque está referenciada en, al menos, un periordo
	          throw new IllegalArgumentException(messageSource.getMessage("error.horario.referenciado.periodo",
	                   null, LocaleContextHolder.getLocale()));
	        	
	        }	     
	        
	        horasPeriodoRepository.deleteById(idhorario);
	    }

}



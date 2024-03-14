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

import es.princast.gepep.domain.Familia;
import es.princast.gepep.repository.FamiliaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FamiliaService {

	 private static final String ENTITY_NAME = "familia";
	 
	@Autowired
    private FamiliaRepository familiaRepository;
	
	
	 public Familia getFamilia(final Long idFamilia) {
	        Optional<Familia> familia = familiaRepository.findById(idFamilia);
	        if (!familia.isPresent()) {
	            throw new IllegalArgumentException("No existe una familia con ese identificador.");
	        }
	        return familia.get();
	    }
	 

	 private Sort sortByNombreAsc() {
		 	Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		 	return Sort.by(order);
	    }
	


	    public List<Familia> getAllFamilias(){
	        return (List<Familia>) familiaRepository.findAll(sortByNombreAsc());
	    }
 
	    public ResponseEntity<Familia> updateFamilia(final Familia familiaModificado) throws URISyntaxException {
	        log.debug("SERVICE request to update Familia : {}", familiaModificado);
	         if (familiaModificado.getIdFamilia() == null) {
	            return  createFamilia(familiaModificado);
	         }
	        
	         Familia result = familiaRepository.save(familiaModificado);
	         return ResponseEntity.ok()
	                 .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, familiaModificado.getIdFamilia().toString()))
	                 .body(result);
	    }

	     public void deleteFamilia(final Long idFamilia) {
	        Optional<Familia> familia = familiaRepository.findById(idFamilia);
	        if (!familia.isPresent()) {
	            throw new IllegalArgumentException("No existe una familia con ese identificador.");
	        }
	     
	        familiaRepository.deleteById(idFamilia);
	    }
 
	    public ResponseEntity<Familia> createFamilia(final Familia nuevoFamilia) throws URISyntaxException {
	    	log.debug("SERVICE request to save Familia : {}", nuevoFamilia);	         
	       if (nuevoFamilia.getIdFamilia() != null) {
	             throw new BadRequestAlertException("A new Familia cannot already have an ID", ENTITY_NAME, "idexists");
	         }
	       	      
	         Familia result = familiaRepository.save(nuevoFamilia);
	             return ResponseEntity.created(new URI("/api/familias/" + result.getIdFamilia()))
	    	             .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdFamilia().toString()))
	    	             .body(result);
             }

	    
	    

}



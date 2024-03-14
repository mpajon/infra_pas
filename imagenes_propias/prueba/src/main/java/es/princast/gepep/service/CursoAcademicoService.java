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

import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.repository.CursoAcademicoRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CursoAcademicoService {

	 private static final String ENTITY_NAME = "cursoAcademico";
	 
	@Autowired
    private CursoAcademicoRepository cursoRepository;
	
	 @Autowired
	  private MessageSource messageSource;
	
	 public CursoAcademico getCursoAcademico(final Integer idAnio) {
	        Optional<CursoAcademico> curso = cursoRepository.findById(idAnio);
	        if (!curso.isPresent()) {
	        	  throw new IllegalArgumentException(messageSource.getMessage("error.curso.id.no.encontrado",
		                   null, LocaleContextHolder.getLocale()));
	        }
	        return curso.get();
	    }
	 	
	 private Sort sortByIdDesc() {
		 	Sort.Order order = new Sort.Order(Sort.Direction.DESC, "idAnio").ignoreCase();
			 return Sort.by(order);
	    }
	    public List<CursoAcademico> getAllCursosAcademicos(){
	        return cursoRepository.findAll(sortByIdDesc());
	    }
	    
		 public CursoAcademico getCursoActual() {
		        Optional<CursoAcademico> curso = cursoRepository.findByActual(true);
		        if (!curso.isPresent()) {
		        	  throw new IllegalArgumentException(messageSource.getMessage("error.curso.id.no.encontrado",
			                   null, LocaleContextHolder.getLocale()));
		        }
		        return curso.get();
		    }
		 
		 
	    public ResponseEntity<CursoAcademico> updateCursoAcademico(final CursoAcademico cursoModificado) throws URISyntaxException {
	        log.debug("SERVICE request to update CursoAcademico : {}", cursoModificado);
	         if (cursoModificado.getIdAnio() == null) {
	            return  createCursoAcademico(cursoModificado);
	         }
	         CursoAcademico result = cursoRepository.save(cursoModificado);
	         
	         if (result.getActual() == true)
	         {
	        	 cursoRepository.updateCursoAcademico(cursoModificado);	        	      
	         }
	         return ResponseEntity.ok()
	                 .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cursoModificado.getIdAnio().toString()))
	                 .body(result);
	    }

	     public void deleteCursoAcademico(final Integer idCurso) {
	        Optional<CursoAcademico> curso = cursoRepository.findById(idCurso);
	        if (!curso.isPresent()) {
	        	  throw new IllegalArgumentException(messageSource.getMessage("error.curso.id.no.encontrado",
		                   null, LocaleContextHolder.getLocale()));
	        }
	     
	        cursoRepository.deleteById(idCurso);
	    }
	     
	   
 
	    public ResponseEntity<CursoAcademico> createCursoAcademico(final CursoAcademico nuevoCurso) throws URISyntaxException {
	    	log.debug("SERVICE request to save CursoAcademico : {}", nuevoCurso);	         
	      /* if (nuevoCurso.getIdAnio()!= null) {
	    	   throw new IllegalArgumentException(messageSource.getMessage("error.curso.nuevo.id",
	                   null, LocaleContextHolder.getLocale()));
	         }*/	       
	         
	         Optional<CursoAcademico> cursoExiste = cursoRepository.findById(nuevoCurso.getIdAnio());
		     
	         if (cursoExiste.isPresent()) {
	        	 throw new IllegalArgumentException(messageSource.getMessage("error.curso.existe",
		                   null, LocaleContextHolder.getLocale()));
	         }
	          
	        	 
	         CursoAcademico result = cursoRepository.save(nuevoCurso);	
	         
	         if (result.getActual() == true)
	         {
	        	 cursoRepository.updateCursoAcademico(nuevoCurso);	        	      
	         }
	       
	             return ResponseEntity.created(new URI("/api/cursos/" + result.getIdAnio()))
	    	             .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdAnio().toString()))
	    	             .body(result);
             }
    

}



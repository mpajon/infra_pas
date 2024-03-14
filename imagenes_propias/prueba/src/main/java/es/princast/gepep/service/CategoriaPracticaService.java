package es.princast.gepep.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.CategoriaPractica;
import es.princast.gepep.repository.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CategoriaPracticaService {

	 private static final String ENTITY_NAME = "categoria";
	 
	@Autowired
    private CategoriaRepository categoriaRepository;
	
	 @Autowired
	  private MessageSource messageSource;
	
	 public CategoriaPractica getCategoria(final Long idCategoria) {
	        Optional<CategoriaPractica> categoria = categoriaRepository.findById(idCategoria);
	        if (!categoria.isPresent()) {
	            throw new IllegalArgumentException("No existe una categoria con ese identificador.");
	        }
	        return categoria.get();
	    }


	private Sort sortByIdAsc() {
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idCategoria").ignoreCase();
		return Sort.by(order);
	}


	public List<CategoriaPractica> getAllCategorias() {
		return (List<CategoriaPractica>) categoriaRepository.findByFechaBajaIsNullOrderByIdCategoriaAsc();
	}
	    
 
	    

}



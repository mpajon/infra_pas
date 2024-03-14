package es.princast.gepep.repository;


import es.princast.gepep.domain.CategoriaPractica;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Familia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaPractica, Long> {
		
    
    Iterable<CategoriaPractica> findByCategoria(String categoria);
    Iterable<CategoriaPractica> findByFechaBajaIsNullOrderByIdCategoriaAsc();
 

}

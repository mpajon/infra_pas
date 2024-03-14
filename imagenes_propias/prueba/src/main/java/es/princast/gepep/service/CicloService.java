package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.ContenidoEF;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.domain.Realizacion;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.domain.VisitaTutor;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.repository.OfertaFormativaRepository;
import es.princast.gepep.repository.RealizacionRepository;
import es.princast.gepep.repository.VisitaTutorRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CicloService {

	private static final String ENTITY_NAME = "ciclo";

	@Autowired
	private CicloRepository cicloRepository;

	@Autowired
	private MessageSource messageSource;
    
	@Autowired
	private RealizacionRepository realizacionRepository;
	
	@Autowired
	private VisitaTutorRepository visitaTutorRepository;
	
	@Autowired
	private OfertaFormativaRepository ofertaRepository;
	
	@Autowired
	private ContenidoEFService contenidoEFService;
	
	@Autowired
	private TipoPracticaService tipoPracticaService;
	
	
	 @Autowired
	 private EntityManager entityManager; 

	public Ciclo createCiclo(final Ciclo nuevoCiclo) throws URISyntaxException {
		log.debug("SERVICE request to save Ciclo : {}", nuevoCiclo);
		
		 if (nuevoCiclo.getIdCiclo()==null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_ciclo')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevoCiclo.setIdCiclo(generatedId);		    	 
			 }	
 

		 List <Ciclo> cicloDuplicado  = this.cicloRepository.findAllByCodigo(nuevoCiclo.getCodigo());
	 	 if (cicloDuplicado.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.ciclo.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
		 
		 
		Ciclo cicloSaved = cicloRepository.save(nuevoCiclo);
		
		
		// Guardamos los contenidos de elementos formativos //
		
		if(!nuevoCiclo.getContenidosEF().isEmpty()) {
		    for (ContenidoEF contenidoEF : nuevoCiclo.getContenidosEF()) {
			contenidoEF.setCiclo(cicloSaved);
			contenidoEFService.updateContenidoEF(contenidoEF);
		    }
		}
		
		return cicloSaved;

	}

	public Ciclo getCiclo(final String idCiclo) throws IllegalArgumentException {
		Optional<Ciclo> ciclo = cicloRepository.findById(idCiclo);
		if (!ciclo.isPresent()) {
			throw new IllegalArgumentException("No existe una ciclo con ese identificador.");
		}
		return ciclo.get();
	}

	public Ciclo getCicloAndContenidosEF(final String idCiclo) throws IllegalArgumentException {
		Optional<Ciclo> ciclo = cicloRepository.findById(idCiclo);
		if (!ciclo.isPresent()) {
			throw new IllegalArgumentException("No existe una ciclo con ese identificador.");
		}
		List<ContenidoEF> contenidosEF = contenidoEFService.getContenidoEFByCiclo(ciclo.get());
		ciclo.get().setContenidosEF(contenidosEF);
		return ciclo.get();
	}
	
	public List<Ciclo> getAllCiclos() {
		String [] propiedades = {"codigo","nombre"};
    	Sort sort = Sort.by(Sort.Direction.ASC, propiedades);
		 return cicloRepository.findAll(sort);
		 
	}

	public List<Ciclo> findAllByFechaBajaIsNull() {
		 return cicloRepository.findAllByFechaBajaIsNullOrderByNombreAsc();
		 
	}
	
	public Ciclo updateCiclo(final Ciclo cicloModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Ciclo : {}", cicloModificado);
		if (cicloModificado.getIdCiclo() == null) {
			return createCiclo(cicloModificado);
		}
		
		 List<Ciclo> ciclosDuplicado  = this.cicloRepository.findAllByCodigoAndDeSauce(cicloModificado.getCodigo(),cicloModificado.getDeSauce());
		 
		 
		 if (ciclosDuplicado.size()>0)
	 	 {
			if(ciclosDuplicado.size()==1 && ciclosDuplicado.get(0).getIdCiclo().equals(cicloModificado.getIdCiclo())) {
				Ciclo result = cicloRepository.save(cicloModificado);
			
				// Guardamos los contenidos de elementos formativos //
				 saveContenidosEF(cicloModificado);
			
				return result;
			}
			else {
				throw new IllegalArgumentException(messageSource.getMessage("error.ciclo.existe",
		                   null, LocaleContextHolder.getLocale()));	 	
			}
	 	 }
		 

		 Ciclo cicloSaved = cicloRepository.save(cicloModificado);
		
		  // Guardamos los contenidos de elementos formativos //
		 saveContenidosEF(cicloModificado);
		
		return cicloSaved;

	}

	/**
	 * Guarda los contenidos de elementos formativos asociados a un ciclo
	 * @param cicloSaved
	 * @throws URISyntaxException
	 */
	private void saveContenidosEF(Ciclo cicloSaved) throws URISyntaxException {
	    // Guardamos los contenidos de elementos formativos //
	    
	    if(!cicloSaved.getContenidosEF().isEmpty()) {
	        for (ContenidoEF contenidoEF : cicloSaved.getContenidosEF()) {
	    	contenidoEF.setCiclo(cicloSaved);
	    	contenidoEFService.updateContenidoEF(contenidoEF);
	        }
	    }
	} 
	
    /**
     * Realiza la actualizacion sin comprobacion de duplicados de un ciclo de sauce
     * dado que existen casos de ciclos con codigos duplicados en sauce. Se necesita
     * este update para actualizar criterios de evaluacion, resultados de
     * aprendizaje y realizaciones.
     * 
     * @param cicloModificado
     * @return
     * @throws URISyntaxException
     */
    public Ciclo updateCicloSauce(final Ciclo cicloModificado) throws URISyntaxException {
	log.debug("SERVICE request to updateCicloSauce : {}", cicloModificado);
	if (cicloModificado.getIdCiclo() == null) {
	    return createCiclo(cicloModificado);
	}

	Ciclo cicloSaved = cicloRepository.save(cicloModificado);

	 // Guardamos los contenidos de elementos formativos //
	 saveContenidosEF(cicloModificado);
	 
	return cicloSaved;

    }

	public void deleteCiclo(final String idCiclo) {
		Optional<Ciclo> ciclo = cicloRepository.findById(idCiclo);
		if (!ciclo.isPresent()) {
			throw new IllegalArgumentException("No existe una ciclo con ese identificador.");
		}
		
		Pageable pageable = null;		
		
		List<Realizacion> listaRealizaciones = realizacionRepository.findAllByCiclo(ciclo.get(), pageable);
                if (listaRealizaciones.size()>0){
                   // No se puede borrar porque está referencenciado en realizacion
                  throw new IllegalArgumentException(messageSource.getMessage("error.ciclo.referenciado.realizacion",
                           null, LocaleContextHolder.getLocale()));}
                
                List<VisitaTutor> listaVisitas = visitaTutorRepository.findAllByCiclo(ciclo.get());
                if (listaVisitas.size()>0){
                   // No se puede borrar porque está referencenciado en visitas
                  throw new IllegalArgumentException(messageSource.getMessage("error.ciclo.referenciado.visitas",
                           null, LocaleContextHolder.getLocale()));}
                
                List<OfertaFormativa> listaOfertas = ofertaRepository.findAllByCicloAndAnioFinIsNull(ciclo.get());
                if (listaOfertas.size()>0){
                   // No se puede borrar porque está referencenciado en oferta activa
                  throw new IllegalArgumentException(messageSource.getMessage("error.ciclo.referenciado.oferta",
                           null, LocaleContextHolder.getLocale()));}
        		 
                
                // Borramos los contenidos EF asociados //
                contenidoEFService.getContenidoEFByCiclo(ciclo.get())
			.forEach(contenidoEF -> contenidoEFService.deleteContenidoEF(contenidoEF.getIdContenidoEF()));             	
             		
        	cicloRepository.deleteById(idCiclo);
	
	}
	
}

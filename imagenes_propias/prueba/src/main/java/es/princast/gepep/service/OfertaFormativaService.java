package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.OfertaEducativa;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.repository.CentroRepository;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.repository.OfertaEducativaRepository;
import es.princast.gepep.repository.OfertaFormativaRepository;
import es.princast.gepep.repository.OfertasCentroRepository;
import es.princast.gepep.repository.ProfesorRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OfertaFormativaService {

	private static final String ENTITY_NAME = "ofertaFormativa";

	@Autowired
	private OfertaFormativaRepository ofertaFormativaRepository;
	
	@Autowired
	private OfertaEducativaRepository ofertaEducativaRepository;
	
	@Autowired
	private OfertasCentroService ofertasCentroService;
		
	@Autowired
	CentroRepository centroRepository;
	
	@Autowired
	private OfertasCentroRepository ofertasCentroRepository;


	@Autowired
	private ProfesorRepository profesorRepository;
	
	@Autowired
	private CicloRepository cicloRepository;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<OfertaFormativa> createOfertaFormativa(final OfertaFormativa nuevaOfertaFormativa) throws URISyntaxException {
		log.debug("SERVICE request to save OfertaFormativa : {}", nuevaOfertaFormativa);

		 if (nuevaOfertaFormativa.getIdOfertaFormativa() == null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_oferta_formativa')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevaOfertaFormativa.setIdOfertaFormativa(generatedId);		    	 
			 }	 
		 
		 List <OfertaFormativa> ofertaDuplicada  = this.ofertaFormativaRepository.findAllByCicloAndCodigoAndAnioFinIsNull(nuevaOfertaFormativa.getCiclo(),nuevaOfertaFormativa.getNombre());
	 	 if (ofertaDuplicada.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.oferta.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
	 	  	
	 	 
		OfertaFormativa result = ofertaFormativaRepository.save(nuevaOfertaFormativa);
		
		return ResponseEntity.created(new URI("/api/ofertas-formativas/" + result.getIdOfertaFormativa()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdOfertaFormativa().toString()))
				.body(result);
	}
	
	public ResponseEntity<OfertaFormativa> createOfertaFormativaNoSauce(final OfertaFormativa nuevaOfertaFormativa) throws URISyntaxException {
		log.debug("SERVICE request to save OfertaFormativa : {}", nuevaOfertaFormativa);

		 if (nuevaOfertaFormativa.getIdOfertaFormativa() == null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_oferta_formativa')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevaOfertaFormativa.setIdOfertaFormativa(generatedId);		    	 
			 }	
		 
		 List <OfertaFormativa> ofertaDuplicada  = this.ofertaFormativaRepository.findAllByCicloAndCodigoAndAnioFinIsNull(nuevaOfertaFormativa.getCiclo(),nuevaOfertaFormativa.getCodigo());
	 	 if (ofertaDuplicada.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.oferta.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
	 	  	
	 	 
		OfertaFormativa result = ofertaFormativaRepository.save(nuevaOfertaFormativa);

		if (nuevaOfertaFormativa.getCentros() != null) {			
			for (Centro centro : nuevaOfertaFormativa.getCentros()) {				
					OfertasCentro ofeCen = new OfertasCentro();
					ofeCen.setCentro(centro);
					ofeCen.setOferta(nuevaOfertaFormativa);
					ofeCen.setVigente(Boolean.TRUE);					
					ofertasCentroService.createOfertasCentro(ofeCen);			
			}
		}
		
		return ResponseEntity.created(new URI("/api/ofertas-formativas/" + result.getIdOfertaFormativa()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdOfertaFormativa().toString()))
				.body(result);
	}

	public OfertaFormativa getOfertaFormativa(final String idOfertaFormativa) {
		Optional<OfertaFormativa> ofertaFormativa = ofertaFormativaRepository.findById(idOfertaFormativa);
		if (!ofertaFormativa.isPresent()) {
			throw new IllegalArgumentException("No existe una ofertaFormativa con ese identificador.");
		}	
		
		List<OfertasCentro> listaOfertaCentro = ofertasCentroRepository.findAllByOferta(ofertaFormativa.get());
		Set<Centro> listaCentros = new HashSet<Centro>();
		
		for (OfertasCentro ofertaCentro : listaOfertaCentro) {
			listaCentros.add(ofertaCentro.getCentro());	
		}
				
		ofertaFormativa.get().setCentros(listaCentros);
		
		return ofertaFormativa.get();
	}
	
	
	
	public List<OfertaFormativa> getOfertasByLista(@PathVariable String listaIds) {
		 
		listaIds ="'S4628','S100528','S4686','S4822'";		
		List<OfertaFormativa> listaOfertas = ofertaFormativaRepository.getOfertasFormativasBylistaIds(listaIds);
		return listaOfertas;	
		  /*  StringBuilder sb = new StringBuilder("from OfertaFormativa o where o.idOfertaFormativa in :ids ");
		    Query q = em.createQuery(sb.toString(), OfertaFormativa.class)
		                .setParameter("Ids", Arrays.asList(ids));*/
	}

	public List<OfertaEducativa> getOfertasByCentroAndAnio(@PathVariable String centro,@PathVariable Integer anio) {
		 
		log.debug("SERVICE request to get all Ofertas Formativas by centro and anio");
		Iterable<OfertaEducativa> listaOfertas = ofertaEducativaRepository.findByCentroAndAnio(centro, anio);		
		return (List<OfertaEducativa>) listaOfertas;
	}
	
	
	public  List<OfertaFormativa>  getAllOfertaFormativas() {
		 log.debug("SERVICE request to get all OfertaFormativa");
		 return ofertaFormativaRepository.findAll();
	}
	
	public  List<OfertaFormativa> getAllOfertaFormativasByCiclo(String idCiclo) {
		 log.debug("SERVICE request to get all OfertaFormativa por ciclo");
		 Optional<Ciclo> ciclo = cicloRepository.findById(idCiclo);
			if (!ciclo.isPresent()) {
				throw new IllegalArgumentException("No existe un ciclo con ese identificador.");
			}		 

			return ofertaFormativaRepository.findAllByCiclo(ciclo);
	}

	public ResponseEntity<OfertaFormativa> updateOfertaFormativa(final OfertaFormativa ofertaFormativaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update OfertaFormativa : {}", ofertaFormativaModificada);
		if (ofertaFormativaModificada.getIdOfertaFormativa() == null) {
			return createOfertaFormativa(ofertaFormativaModificada);
		}
		
		 List <OfertaFormativa> ofertaDuplicada  = this.ofertaFormativaRepository.findAllByCicloAndCodigoAndAnioFinIsNull(ofertaFormativaModificada.getCiclo(),ofertaFormativaModificada.getCodigo());
	 	
		 if (ofertaDuplicada.size()>0)
	 	 {
			if(ofertaDuplicada.size()>1 || (ofertaDuplicada.size()==1 && !ofertaDuplicada.get(0).getIdOfertaFormativa().equals(ofertaFormativaModificada.getIdOfertaFormativa()))) {
				throw new IllegalArgumentException(messageSource.getMessage("error.informacion.existe",
		                   null, LocaleContextHolder.getLocale()));	
			}			
	 	 }
	 	 
	 	OfertaFormativa ofeBD = this.getOfertaFormativa(ofertaFormativaModificada.getIdOfertaFormativa()); 
	 	  	
	 	Set<Centro> centrosBD = ofeBD.getCentros();
	 	Set<Centro> centrosModificado =  ofertaFormativaModificada.getCentros();
	 	List<Centro> centrosInsertar = null;
	 	List<Centro> centrosBorrar = null;
	 	
	 	if ((centrosBD == null || centrosBD.isEmpty()) && (centrosModificado==null ||centrosModificado.isEmpty())) {
	 		// no hacemos nada
	 	} else if (centrosBD != null && !centrosBD.isEmpty() && centrosModificado!= null && !centrosModificado.isEmpty()) {
			Set<String> idsBd = ofeBD.getCentros().stream().map(Centro::getIdCentro).collect(Collectors.toSet());

			Set<String> idsModificado = ofertaFormativaModificada.getCentros().stream().map(Centro::getIdCentro)
					.collect(Collectors.toSet());

			centrosInsertar = centrosModificado.stream().filter(centro -> !idsBd.contains(centro.getIdCentro()))
					.collect(Collectors.toList());

			centrosBorrar = centrosBD.stream().filter(centro -> !idsModificado.contains(centro.getIdCentro()))
					.collect(Collectors.toList());
		} else if ((centrosBD == null || centrosBD.isEmpty())) {
			centrosInsertar = new ArrayList<Centro>();
			centrosInsertar.addAll(centrosModificado);
		} else if ((centrosModificado==null ||centrosModificado.isEmpty())) {
			centrosBorrar = new ArrayList<Centro>();
			centrosBorrar.addAll(centrosBD);
		} 
		 	
		if (centrosInsertar != null) {
			for (Centro centro : centrosInsertar) {				
				OfertasCentro ofeCen = new OfertasCentro();
				ofeCen.setCentro(centro);
				ofeCen.setOferta(ofeBD);
				ofeCen.setVigente(Boolean.TRUE);

				ofertasCentroService.createOfertasCentro(ofeCen);
			}
		}	
		
		
		
		/* Ruth - Cuidado porque está dando error de integridad referencial al intentar borrar una ofertacetnro que est� dada de alta en tabla unidad
		 * ERROR: update or delete on table "ofertas_centro" violates foreign key constraint "unidad_ofertas_centro_fk" on table "unidad".
		 **/ 
		 if (centrosBorrar != null) {
			for (Centro centro : centrosBorrar) {
				Optional<OfertasCentro> ofeCen = ofertasCentroRepository.findOneByOfertaAndCentro(ofeBD, centro);
				if (ofeCen.isPresent()) {
					try {
						ofertasCentroService.deleteOfertasCentro(ofeCen.get().getIdOfertaCentro());
					}catch (Exception ex)
					{
						//las ofertas-centro vinculadas con unidades no se borrar�n.						
					}
				}
			}
		}
		
		OfertaFormativa result = ofertaFormativaRepository.save(ofertaFormativaModificada);
		
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ofertaFormativaModificada.getIdOfertaFormativa().toString()))
				.body(result);
	}

	public void deleteOfertaFormativa(final String idOfertaFormativa) {
		Optional<OfertaFormativa> ofertaFormativa = ofertaFormativaRepository.findById(idOfertaFormativa);
		if (!ofertaFormativa.isPresent()) {
			throw new IllegalArgumentException("No existe una ofertaFormativa con ese identificador.");
		}
		
		        
        List<OfertasCentro> listaOfertaCentro = ofertasCentroRepository.findAllByOferta(ofertaFormativa.get());
        if (listaOfertaCentro.size()>0){
           // No se puede borrar porque est� referencenciado en convenio activo
          throw new IllegalArgumentException(messageSource.getMessage("error.oferta.referenciado.ofercen",
                   null, LocaleContextHolder.getLocale()));}
        
        List<Profesor> listaProfesor = profesorRepository.findAllByOferta(ofertaFormativa.get());
        if (listaProfesor.size()>0){
           // No se puede borrar porque est� referencenciado en convenio activo
          throw new IllegalArgumentException(messageSource.getMessage("error.oferta.referenciado.profesor",
                   null, LocaleContextHolder.getLocale()));}
        
		ofertaFormativaRepository.deleteById(idOfertaFormativa);
	}

}

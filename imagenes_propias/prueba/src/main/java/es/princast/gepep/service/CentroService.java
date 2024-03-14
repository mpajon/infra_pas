package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
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

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Convenio;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Usuario;
import es.princast.gepep.repository.CentroRepository;
import es.princast.gepep.repository.ConvenioRepository;
import es.princast.gepep.repository.OfertasCentroRepository;
import es.princast.gepep.repository.UsuarioRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CentroService {

	private static final String ENTITY_NAME = "centro";

	@Autowired
	private CentroRepository centroRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ConvenioRepository convenioRepository;
	
	@Autowired
	private OfertasCentroRepository ofertaCentroRepository;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private EntityManager entityManager; 

	public ResponseEntity<Centro> createCentro(final Centro nuevoCentro) throws URISyntaxException {
		log.debug("SERVICE request to save Centro : {}", nuevoCentro);		
 
	 	  if (nuevoCentro.getIdCentro() == null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_centro')").getSingleResult();
			String generatedId =  nextId.toString();
			nuevoCentro.setIdCentro(generatedId);		    	 
		 }	 
	 	  
	 	 List <Centro> centroDuplicado  = this.centroRepository.findAllByCodigo(nuevoCentro.getCodigo());
	 	 if (centroDuplicado.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.centro.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
	 	  		
	 	Centro result = centroRepository.save(nuevoCentro); 
	 	return ResponseEntity.created(new URI("/api/centros/" + result.getIdCentro()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdCentro().toString()))
				.body(result);
		
	}

	public Centro getCentro(final String idCentro) {
		Optional<Centro> centro = centroRepository.findById(idCentro);
		if (!centro.isPresent()) {
			throw new IllegalArgumentException("No existe un centro con ese identificador.");
		}
		return centro.get();
	}

	private Sort sortByNombreAsc() {

		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		return Sort.by(order);
	}

	/**
	 * Retorna los centros vigentes en el anio de cursoAcademico. Sin cargar relaciones jpa. Usado desde los combos de la vista.
	 * @param anio
	 * @return
	 */
	public List<Centro> getCentroComboList(Integer anio) {
		
		String sql =  "select  ca_centro as idCentro, dc_codigo as codigo, dc_nombre as nombre, if_cif as cif "
 	    		+ " from centro "
 	    		+ " where centro.fe_fbaja IS NULL OR fe_fbaja >= (select fe_final from curso_academico where nu_anio = :anio )"
 	    		+ " order by dc_nombre ASC";
		
		return entityManager.createNativeQuery(sql, "CentroComboListMapping")
				.setParameter("anio", anio)
				.getResultList();
		
	}
	
	public List<Centro> getAllCentros() {
		return centroRepository.findAll(sortByNombreAsc());
	}

	
	public List<Centro> getAllCentrosActivos() {
		return centroRepository.findAllActivosOrderByNombreAsc();
	}
	
	
	public ResponseEntity<Centro> updateCentro(final Centro centroModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Centro : {}", centroModificado);
		if (centroModificado.getIdCentro() == null) {
			return createCentro(centroModificado);
		}
		
		 List <Centro> centroDuplicado  = this.centroRepository.findAllByCodigo(centroModificado.getCodigo());
		 
		 
		 if (centroDuplicado.size()>0)
	 	 {
			 if (centroDuplicado.size()==1 &&  centroDuplicado.get(0).getIdCentro().equals(centroModificado.getIdCentro()))
		 	 {
					Centro result = centroRepository.save(centroModificado);
					return ResponseEntity.ok()
							.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, centroModificado.getIdCentro().toString()))
							.body(result);
		 	}
				else {
				throw new IllegalArgumentException(messageSource.getMessage("error.centro.existe",
		                   null, LocaleContextHolder.getLocale()));	 	 
				}

	 	 }	 
		
		 
		Centro result = centroRepository.save(centroModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, centroModificado.getIdCentro().toString()))
				.body(result);

	}

	public void deleteCentro(final String idCentro) {
		Optional<Centro> centro = centroRepository.findById(idCentro);
		if (!centro.isPresent()) {
			throw new IllegalArgumentException("No existe un centro con ese identificador.");
		}
		 
		List<Convenio> listaConvenios = convenioRepository.findAllByCentroAndFechaBajaIsNullAndValidadoIsTrue(centro.get());
        if (listaConvenios.size()>0){
           // No se puede borrar porque está referencenciado en convenio activo
          throw new IllegalArgumentException(messageSource.getMessage("error.centro.referenciado.convenio",
                   null, LocaleContextHolder.getLocale()));}
        
        List<OfertasCentro> listaOfertaCentro = ofertaCentroRepository.findAllByCentro(centro.get());
        if (listaOfertaCentro.size()>0){
           // No se puede borrar porque está referencenciado en convenio activo
          throw new IllegalArgumentException(messageSource.getMessage("error.centro.referenciado.oferta",
                   null, LocaleContextHolder.getLocale()));}
        
        List<Usuario> listaUsuarios = usuarioRepository.findAllByCentroAndFechaBajaIsNull(centro.get());
        if (listaUsuarios.size()>0){
           // No se puede borrar porque está referencenciado en convenio activo
          throw new IllegalArgumentException(messageSource.getMessage("error.centro.referenciado.usuario",
                   null, LocaleContextHolder.getLocale()));}
        
		centroRepository.deleteById(idCentro);
	}
	
}

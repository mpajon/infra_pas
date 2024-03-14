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

import es.princast.gepep.domain.ElementoFormativo;
import es.princast.gepep.domain.Actividad;
import es.princast.gepep.domain.ConfigElemFormativo;
import es.princast.gepep.domain.ContenidoEF;


import es.princast.gepep.repository.ConfigElemFormativoRepository;
import es.princast.gepep.repository.ContenidoEFRepository;
import es.princast.gepep.repository.ElementoFormativoRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ConfigElemFormativoService {

	private static final String ENTITY_NAME = "configElemFormativo";

	@Autowired
	private ElementoFormativoRepository elementoFormativoRepository;

	@Autowired
	private ConfigElemFormativoRepository configElemFormativoRepository;
	
	@Autowired
	private ContenidoEFRepository contenidoEFRepository;
	
	
	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;
	

	@Autowired
	private MessageSource messageSource;

	public ConfigElemFormativo getConfigElemFormativo(final Long idConfigElemFormativo) {
		log.debug("SERVICE request to getElementoFormativo ");
		Optional<ConfigElemFormativo> configElemFormativo = configElemFormativoRepository.findById(idConfigElemFormativo);
		if (!configElemFormativo.isPresent()) {
			throw new IllegalArgumentException("No existe una configuración de elemento formativo con ese identificador.");
		}
		return configElemFormativo.get();
	}
	
	public ResponseEntity<ConfigElemFormativo> createConfigElemFormativo(final ConfigElemFormativo nuevoConfigElemFormativo) throws URISyntaxException {
		log.debug("SERVICE request to save ElementoFormativo : {}", nuevoConfigElemFormativo);
		if (nuevoConfigElemFormativo.getIdConfigElemFormativo() != null) {
			throw new BadRequestAlertException("A new ElementoFormativo cannot already have an ID", ENTITY_NAME, "idexists");
		}

		List<ConfigElemFormativo> elementoFormativoDuplicadoCodigo = this.configElemFormativoRepository.findAllByDenominacionAndElementoFormativo(nuevoConfigElemFormativo.getDenominacion(),nuevoConfigElemFormativo.getElementoFormativo());
		

		if (elementoFormativoDuplicadoCodigo.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.configElemFormativo.existe.codigo", null, LocaleContextHolder.getLocale()));
		}

		ConfigElemFormativo result = configElemFormativoRepository.save(nuevoConfigElemFormativo);
		return ResponseEntity.created(new URI("/api/configEF/" + result.getIdConfigElemFormativo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdConfigElemFormativo().toString()))
				.body(result);
	}

	public List<ConfigElemFormativo> getAllConfigElemFormativos() {
		String[] propiedades = { "elementoFormativo.codigo" };
		Sort sort = Sort.by(Sort.Direction.ASC, propiedades);
		return configElemFormativoRepository.findAll(sort);
	}

	public ResponseEntity<ConfigElemFormativo> updateConfigElemFormativo(final ConfigElemFormativo configElemFormativoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update ElementoFormativo : {}", configElemFormativoModificado);
		if (configElemFormativoModificado.getIdConfigElemFormativo() == null) {
			return createConfigElemFormativo(configElemFormativoModificado);
		}
		
	   ConfigElemFormativo configOrigen= this.configElemFormativoRepository.getOne(configElemFormativoModificado.getIdConfigElemFormativo());
	   
	   /*si se actualiza la configuracion pero ya tiene contenido asociado - no se permite cambiar la misma.*/
	   if(configOrigen.getConfiguracion().toString().toUpperCase() != configElemFormativoModificado.getConfiguracion().toString().toUpperCase()) {		   

		   List<ContenidoEF> listaContenido =(List<ContenidoEF>) this.contenidoEFRepository.findAllByConfigElemFormativo(configElemFormativoModificado);
			if (listaContenido.size()>0) {
		   throw new IllegalArgumentException(
					messageSource.getMessage("error.configElemFormativo.existe.contenido", null, LocaleContextHolder.getLocale()));
			}
		   
	   } 
		
		ConfigElemFormativo result = configElemFormativoRepository.save(configElemFormativoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configElemFormativoModificado.getIdConfigElemFormativo().toString()))
				.body(result);
	}


	public Iterable<ConfigElemFormativo> getConfigElemFormativoByElementoFormativo(final Long idElementoFormativo) {
	
		Iterable<ConfigElemFormativo> listaConfiguraciones = configElemFormativoRepository.findAllByElementoFormativoOrderByOrdenAsc(elementoFormativoRepository.getOne(idElementoFormativo));
		return listaConfiguraciones;
	}


	public void deleteConfigElemFormativo(final Long idConfigElemFormativo) {
		Optional<ConfigElemFormativo> configElemFormativo = configElemFormativoRepository.findById(idConfigElemFormativo);
		if (!configElemFormativo.isPresent()) {
			throw new IllegalArgumentException("No existe una configuracion para un elemento formativo con ese identificador.");
		}

		List<ContenidoEF> listaContenidoEF= (List<ContenidoEF>) this.contenidoEFRepository.findAllByConfigElemFormativo(configElemFormativo.get());
		
		if (listaContenidoEF.size() > 0) {
			// No se puede borrar porque está referencenciado en configElemFormativo
			throw new IllegalArgumentException(messageSource.getMessage("error.configElemFormativo.referenciado.contenidoEF", null,
					LocaleContextHolder.getLocale()));
		} 
		configElemFormativoRepository.deleteById(idConfigElemFormativo);
	}



}

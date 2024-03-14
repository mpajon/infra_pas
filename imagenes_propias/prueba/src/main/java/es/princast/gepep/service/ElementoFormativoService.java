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
import es.princast.gepep.domain.ConfigElemFormativo;

import es.princast.gepep.repository.ElementoFormativoRepository;
import es.princast.gepep.repository.ConfigElemFormativoRepository;

import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ElementoFormativoService {

	private static final String ENTITY_NAME = "elementoFormativo";

	@Autowired
	private ElementoFormativoRepository elementoFormativoRepository;

	@Autowired
	private ConfigElemFormativoRepository configElemFormativoRepository;


	@Autowired
	private MessageSource messageSource;

	public ElementoFormativo getElementoFormativo(final Long idElementoFormativo) {
		log.debug("SERVICE request to getElementoFormativo ");
		Optional<ElementoFormativo> elementoFormativo = elementoFormativoRepository.findById(idElementoFormativo);
		if (!elementoFormativo.isPresent()) {
			throw new IllegalArgumentException("No existe un elementoFormativo con ese identificador.");
		}
		return elementoFormativo.get();
	}

	public List<ElementoFormativo> getAllElementoFormativos() {
		String[] propiedades = { "codigo"};
		Sort sort = Sort.by(Sort.Direction.ASC, propiedades);
		return elementoFormativoRepository.findAll(sort);
	}

	public ResponseEntity<ElementoFormativo> updateElementoFormativo(final ElementoFormativo elementoFormativoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update ElementoFormativo : {}", elementoFormativoModificado);
		if (elementoFormativoModificado.getIdElementoFormativo() == null) {
			return createElementoFormativo(elementoFormativoModificado);
		}
		
		List<ElementoFormativo> elementoFormativoDuplicadoCodigo = this.elementoFormativoRepository.findAllByCodigo(elementoFormativoModificado.getCodigo());
		

		if (elementoFormativoDuplicadoCodigo.size() > 0) {
			if (!(elementoFormativoDuplicadoCodigo.size() == 1
					&& elementoFormativoDuplicadoCodigo.get(0).getIdElementoFormativo().equals(elementoFormativoModificado.getIdElementoFormativo()))) {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.elementoFormativo.existe.codigo", null, LocaleContextHolder.getLocale()));
			}
		} 
		 
		
		ElementoFormativo result = elementoFormativoRepository.save(elementoFormativoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, elementoFormativoModificado.getIdElementoFormativo().toString()))
				.body(result);
	}

	public void deleteElementoFormativo(final Long idElementoFormativo) {
		Optional<ElementoFormativo> elementoFormativo = elementoFormativoRepository.findById(idElementoFormativo);
		if (!elementoFormativo.isPresent()) {
			throw new IllegalArgumentException("No existe un elementoFormativo con ese identificador.");
		}

		List<ConfigElemFormativo> listaConfigElemFormativos = (List<ConfigElemFormativo>) configElemFormativoRepository.findAllByElementoFormativoOrderByOrdenAsc(elementoFormativo.get());
		if (listaConfigElemFormativos.size() > 0) {
			// No se puede borrar porque está referencenciado en configElemFormativo
			throw new IllegalArgumentException(messageSource.getMessage("error.elementoFormativo.referenciado.configElemFormativo", null,
					LocaleContextHolder.getLocale()));
		}

		elementoFormativoRepository.deleteById(idElementoFormativo);
	}

	public ResponseEntity<ElementoFormativo> createElementoFormativo(final ElementoFormativo nuevoElementoFormativo) throws URISyntaxException {
		log.debug("SERVICE request to save ElementoFormativo : {}", nuevoElementoFormativo);
		if (nuevoElementoFormativo.getIdElementoFormativo() != null) {
			throw new BadRequestAlertException("A new ElementoFormativo cannot already have an ID", ENTITY_NAME, "idexists");
		}

		List<ElementoFormativo> elementoFormativoDuplicadoCodigo = this.elementoFormativoRepository.findAllByCodigo(nuevoElementoFormativo.getCodigo());
		

		if (elementoFormativoDuplicadoCodigo.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.elementoFormativo.existe.codigo", null, LocaleContextHolder.getLocale()));
		}

		ElementoFormativo result = elementoFormativoRepository.save(nuevoElementoFormativo);
		return ResponseEntity.created(new URI("/api/elementoFormativos/" + result.getIdElementoFormativo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdElementoFormativo().toString()))
				.body(result);
	}

}

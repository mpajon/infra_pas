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

import es.princast.gepep.domain.TextosDocumento;
import es.princast.gepep.repository.DocumentoRepository;
import es.princast.gepep.repository.TextosDocumentoRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TextosDocumentoService {

	private static final String ENTITY_NAME = "textosDocumento";

	@Autowired
	private  DocumentoRepository documentoRepository;
	

	@Autowired
	private TextosDocumentoRepository textosDocumentoRepository;
	
	@Autowired
	private MessageSource messageSource;

	public ResponseEntity<TextosDocumento> createTextosDocumento(final TextosDocumento nuevoTextosDocumento) throws URISyntaxException {
		log.debug("SERVICE request to save TextosDocumento : {}", nuevoTextosDocumento);

		if (nuevoTextosDocumento.getIdTextosDocumento() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.textos.nuevo.id", null, LocaleContextHolder.getLocale()));		}
		
		Iterable<TextosDocumento> listaTextos = textosDocumentoRepository.findAllByDocumento(nuevoTextosDocumento.getDocumento());
		if (listaTextos.iterator().hasNext()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.textoDocumento.mismo.documento", null,
					LocaleContextHolder.getLocale()));
		}
		 
		TextosDocumento result = textosDocumentoRepository.save(nuevoTextosDocumento);
		return ResponseEntity.created(new URI("/api/textos-documentacion/" + result.getIdTextosDocumento()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdTextosDocumento().toString()))
				.body(result);
		
	}

	public TextosDocumento getTextosDocumento(final Long idTextosDocumento) {
		Optional<TextosDocumento> textosDocumento = textosDocumentoRepository.findById(idTextosDocumento);
		if (!textosDocumento.isPresent()) {
			throw new IllegalArgumentException("No existe un texto con ese identificador.");
		}
		return textosDocumento.get();
	}

	public List<TextosDocumento> getAllTextosDocumento() {
		return textosDocumentoRepository.findAll(sortByIdAsc());
	}	
	
	private Sort sortByIdAsc() {

		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idTextosDocumento").ignoreCase();
		return Sort.by(order);
	}

	public ResponseEntity<TextosDocumento> updateTextosDocumento(final TextosDocumento textosDocumentoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update TextosDocumento : {}", textosDocumentoModificado);
		if (textosDocumentoModificado.getIdTextosDocumento()== null) {
			return createTextosDocumento(textosDocumentoModificado);
		}
		
		Optional<TextosDocumento> textoAnterior = textosDocumentoRepository.findById(textosDocumentoModificado.getIdTextosDocumento());		
		
		if (!textoAnterior.get().getDocumento().getIdDocumento().toString().equals(textosDocumentoModificado.getDocumento().getIdDocumento().toString())) {			
			Iterable<TextosDocumento> listaTextos = textosDocumentoRepository.findAllByDocumento(textosDocumentoModificado.getDocumento());
			if (listaTextos.iterator().hasNext()) {
				throw new IllegalArgumentException(messageSource.getMessage("error.textoDocumento.mismo.documento", null,
						LocaleContextHolder.getLocale()));
			}			
		}			
		
		TextosDocumento result = textosDocumentoRepository.save(textosDocumentoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, textosDocumentoModificado.getIdTextosDocumento().toString()))
				.body(result);

	}
	public void deleteTextosDocumento(final Long idTextosDocumento) {
		Optional<TextosDocumento> textoDocumento = textosDocumentoRepository.findById(idTextosDocumento);
		if (!textoDocumento.isPresent()) {
			throw new IllegalArgumentException("No existe un texto con ese identificador.");
		}	
		
		
		textosDocumentoRepository.deleteById(idTextosDocumento);
	}	

	public Iterable<TextosDocumento> getAllTextosDocumentoByDocumento(final Long idDocumento) {
		log.debug("SERVICE request to get all Textos by Documento");
		Iterable<TextosDocumento> listaTextos = this.textosDocumentoRepository
				.findAllByDocumento(this.documentoRepository.getOne(idDocumento));
		return listaTextos;
	}

	
}

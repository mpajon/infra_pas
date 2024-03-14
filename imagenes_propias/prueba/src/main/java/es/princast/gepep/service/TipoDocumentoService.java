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

import es.princast.gepep.domain.TipoDocumento;
import es.princast.gepep.repository.TipoDocumentoRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TipoDocumentoService {

	private static final String ENTITY_NAME = "tipoDocumento";

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	public TipoDocumento getTipoDocumento(final Long idTipoDocumento) {
		Optional<TipoDocumento> tipocentro = tipoDocumentoRepository.findById(idTipoDocumento);
		if (!tipocentro.isPresent()) {
			throw new IllegalArgumentException("No existe un tipo de documento con ese identificador.");
		}
		return tipocentro.get();
		
	}

	private Sort sortByIdAsc() {
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idTipoDocumento").ignoreCase();
		return Sort.by(order);
	}
	 
	public List<TipoDocumento> getAllTiposDocumento() {
		return tipoDocumentoRepository.findAll(sortByIdAsc());
	}

	public ResponseEntity<TipoDocumento> updateTipoDocumento(final TipoDocumento TipoDocumentoModificada)
			throws URISyntaxException {
		log.debug("SERVICE request to update TipoDocumento : {}", TipoDocumentoModificada);
		if (TipoDocumentoModificada.getIdTipoDocumento() == null) {
			return createTipoDocumento(TipoDocumentoModificada);
		}

		TipoDocumento result = tipoDocumentoRepository.save(TipoDocumentoModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, TipoDocumentoModificada.getIdTipoDocumento().toString()))
				.body(result);

	}

	public void deleteTipoDocumento(final Long idTipoDocumento) {
		Optional<TipoDocumento> TipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento);
		if (!TipoDocumento.isPresent()) {
			throw new IllegalArgumentException("No existe un TipoDocumento con ese identificador.");
		}

		tipoDocumentoRepository.deleteById(idTipoDocumento);
		
	}

	public ResponseEntity<TipoDocumento> createTipoDocumento(final TipoDocumento nuevaTipoDocumento) throws URISyntaxException {
		log.debug("SERVICE request to save TipoDocumento : {}", nuevaTipoDocumento);

		if (nuevaTipoDocumento.getIdTipoDocumento() != null) {
			throw new BadRequestAlertException("A new TipoDocumento cannot already have an ID", ENTITY_NAME, "idexists");
		}

		TipoDocumento result = tipoDocumentoRepository.save(nuevaTipoDocumento);
		return ResponseEntity.created(new URI("/api/tipos-documento/" + result.getIdTipoDocumento()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdTipoDocumento().toString()))
				.body(result);
	}

}

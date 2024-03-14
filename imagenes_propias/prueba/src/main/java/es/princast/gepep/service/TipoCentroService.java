package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.TipoCentro;
import es.princast.gepep.repository.TipoCentroRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TipoCentroService {

	private static final String ENTITY_NAME = "tipoCentro";

	@Autowired
	private TipoCentroRepository tipoCentroRepository;

	public TipoCentro getTipoCentro(final Long idTipoCentro) {
		Optional<TipoCentro> tipocentro = tipoCentroRepository.findById(idTipoCentro);
		if (!tipocentro.isPresent()) {
			throw new IllegalArgumentException("No existe un tipo de centro con ese identificador.");
		}
		return tipocentro.get();
	}

	public List<TipoCentro> getAllTiposCentros() {
		return tipoCentroRepository.findAll();
	}

	public ResponseEntity<TipoCentro> updateTipoCentro(final TipoCentro TipoCentroModificada)
			throws URISyntaxException {
		log.debug("SERVICE request to update TipoCentro : {}", TipoCentroModificada);
		if (TipoCentroModificada.getIdTipoCentro() == null) {
			return createTipoCentro(TipoCentroModificada);
		}

		TipoCentro result = tipoCentroRepository.save(TipoCentroModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, TipoCentroModificada.getIdTipoCentro().toString()))
				.body(result);

	}

	public void deleteTipoCentro(final Long idTipoCentro) {
		Optional<TipoCentro> TipoCentro = tipoCentroRepository.findById(idTipoCentro);
		if (!TipoCentro.isPresent()) {
			throw new IllegalArgumentException("No existe una TipoCentro con ese identificador.");
		}

		tipoCentroRepository.deleteById(idTipoCentro);
	}

	public ResponseEntity<TipoCentro> createTipoCentro(final TipoCentro nuevaTipoCentro) throws URISyntaxException {
		log.debug("SERVICE request to save TipoCentro : {}", nuevaTipoCentro);

		if (nuevaTipoCentro.getIdTipoCentro() != null) {
			throw new BadRequestAlertException("A new TipoCentro cannot already have an ID", ENTITY_NAME, "idexists");
		}

		TipoCentro result = tipoCentroRepository.save(nuevaTipoCentro);
		return ResponseEntity.created(new URI("/api/tipos-centro/" + result.getIdTipoCentro()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdTipoCentro().toString()))
				.body(result);
	}

}

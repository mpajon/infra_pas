package es.princast.gepep.service;

import es.princast.gepep.domain.TipoGasto;
import es.princast.gepep.repository.TipoGastoRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TipoGastoService {

	private static final String ENTITY_NAME = "tipoGasto";

	@Autowired
	private TipoGastoRepository tipoGastoRepository;

	public TipoGasto getTipoGasto(final Long idTipoGasto) {
		Optional<TipoGasto> tipogasto = tipoGastoRepository.findById(idTipoGasto);
		if (!tipogasto.isPresent()) {
			throw new IllegalArgumentException("No existe un tipo de gasto con ese identificador.");
		}
		return tipogasto.get();
	}

	public List<TipoGasto> getAllTipoGasto() {
		return tipoGastoRepository.findAll();
	}

	public ResponseEntity<TipoGasto> updateTipoGasto(final TipoGasto TipoGastoModificado)
			throws URISyntaxException {
		log.debug("SERVICE request to update TipoGasto : {}", TipoGastoModificado);
		if (TipoGastoModificado.getIdTipoGasto() == null) {
			return createTipoGasto(TipoGastoModificado);
		}

		TipoGasto result = tipoGastoRepository.save(TipoGastoModificado);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, TipoGastoModificado.getIdTipoGasto().toString()))
				.body(result);

	}

	public void deleteTipoGasto(final Long idTipoGasto) {
		Optional<TipoGasto> TipoGasto = tipoGastoRepository.findById(idTipoGasto);
		if (!TipoGasto.isPresent()) {
			throw new IllegalArgumentException("No existe una TipoGasto con ese identificador.");
		}

		tipoGastoRepository.deleteById(idTipoGasto);
	}

	public ResponseEntity<TipoGasto> createTipoGasto(final TipoGasto nuevaTipoGasto) throws URISyntaxException {
		log.debug("SERVICE request to save TipoGasto : {}", nuevaTipoGasto);

		if (nuevaTipoGasto.getIdTipoGasto() != null) {
			throw new BadRequestAlertException("A new TipoGasto cannot already have an ID", ENTITY_NAME, "idexists");
		}

		TipoGasto result = tipoGastoRepository.save(nuevaTipoGasto);
		return ResponseEntity.created(new URI("/api/tipo-gasto/" + result.getIdTipoGasto()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdTipoGasto().toString()))
				.body(result);
	}

}

package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.EstadoEmpresa;
import es.princast.gepep.repository.EstadoEmpresaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EstadoEmpresaService {

	private static final String ENTITY_NAME = "estadoEmpresa";

	@Autowired
	private EstadoEmpresaRepository estadoEmpresaRepository;


	public ResponseEntity<EstadoEmpresa> createEstadoEmpresa(final EstadoEmpresa nuevaEstadoEmpresa) throws URISyntaxException {
		log.debug("SERVICE request to save EstadoEmpresa : {}", nuevaEstadoEmpresa);

		if (nuevaEstadoEmpresa.getIdEstadoEmpresa() != null) {
			throw new BadRequestAlertException("A new estadoEmpresa cannot already have an ID", ENTITY_NAME, "idexists");
		}

		EstadoEmpresa result = estadoEmpresaRepository.save(nuevaEstadoEmpresa);
		return ResponseEntity.created(new URI("/api/estado-empresa/" + result.getIdEstadoEmpresa()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdEstadoEmpresa().toString()))
				.body(result);
	}

	public EstadoEmpresa getEstadoEmpresa(final Long idEstadoEmpresa) {
		Optional<EstadoEmpresa> estadoEmpresa = estadoEmpresaRepository.findById(idEstadoEmpresa);
		if (!estadoEmpresa.isPresent()) {
			throw new IllegalArgumentException("No existe un estadoEmpresa con ese identificador.");
		}
		return estadoEmpresa.get();
	}

	public List<EstadoEmpresa> getAllEstadoEmpresas() {
		return estadoEmpresaRepository.findAll();
	}

	public ResponseEntity<EstadoEmpresa> updateEstadoEmpresa(final EstadoEmpresa estadoEmpresaModificado) throws URISyntaxException {
		log.debug("SERVICE request to update EstadoEmpresa : {}", estadoEmpresaModificado);
		if (estadoEmpresaModificado.getIdEstadoEmpresa() == null) {
			return createEstadoEmpresa(estadoEmpresaModificado);
		}

		EstadoEmpresa result = estadoEmpresaRepository.save(estadoEmpresaModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, estadoEmpresaModificado.getIdEstadoEmpresa().toString()))
				.body(result);

	}

	public void deleteEstadoEmpresa(final Long idEstadoEmpresa) {
		Optional<EstadoEmpresa> estadoEmpresa = estadoEmpresaRepository.findById(idEstadoEmpresa);
		if (!estadoEmpresa.isPresent()) {
			throw new IllegalArgumentException("No existe un estado con ese identificador.");
		}	 

		estadoEmpresaRepository.deleteById(idEstadoEmpresa);
	}
 
}

package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.MotivoEstado;
import es.princast.gepep.repository.MotivoEstadoRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MotivoEstadoService {

	private static final String ENTITY_NAME = "motivoEstado";

	@Autowired
	private MotivoEstadoRepository motivoEstadoRepository;


	public ResponseEntity<MotivoEstado> createMotivoEstado(final MotivoEstado nuevaMotivoEstado) throws URISyntaxException {
		log.debug("SERVICE request to save MotivoEstado : {}", nuevaMotivoEstado);

		if (nuevaMotivoEstado.getIdMotivoEstado() != null) {
			throw new BadRequestAlertException("A new motivoEstado cannot already have an ID", ENTITY_NAME, "idexists");
		}

		MotivoEstado result = motivoEstadoRepository.save(nuevaMotivoEstado);
		return ResponseEntity.created(new URI("/api/estado-empresa/" + result.getIdMotivoEstado()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdMotivoEstado().toString()))
				.body(result);
	}

	public MotivoEstado getMotivoEstado(final Long idMotivoEstado) {
		Optional<MotivoEstado> motivoEstado = motivoEstadoRepository.findById(idMotivoEstado);
		if (!motivoEstado.isPresent()) {
			throw new IllegalArgumentException("No existe un motivoEstado con ese identificador.");
		}
		return motivoEstado.get();
	}

	public List<MotivoEstado> getAllMotivoEstados() {
		return motivoEstadoRepository.findAll();
	}

	public ResponseEntity<MotivoEstado> updateMotivoEstado(final MotivoEstado motivoEstadoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update MotivoEstado : {}", motivoEstadoModificado);
		if (motivoEstadoModificado.getIdMotivoEstado() == null) {
			return createMotivoEstado(motivoEstadoModificado);
		}

		MotivoEstado result = motivoEstadoRepository.save(motivoEstadoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, motivoEstadoModificado.getIdMotivoEstado().toString()))
				.body(result);

	}

	public void deleteMotivoEstado(final Long idMotivoEstado) {
		Optional<MotivoEstado> motivoEstado = motivoEstadoRepository.findById(idMotivoEstado);
		if (!motivoEstado.isPresent()) {
			throw new IllegalArgumentException("No existe un motivo con ese identificador.");
		}	 

		motivoEstadoRepository.deleteById(idMotivoEstado);
	}
 
}

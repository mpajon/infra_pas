package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Perfil;
import es.princast.gepep.repository.PerfilRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PerfilService {

	private static final String ENTITY_NAME = "perfil";

	@Autowired
	private PerfilRepository perfilRepository;

	public ResponseEntity<Perfil> createPerfil(final Perfil nuevoPerfil) throws URISyntaxException {
		log.debug("SERVICE request to save Perfil : {}", nuevoPerfil);

		if (nuevoPerfil.getIdPerfil() != null) {
			throw new BadRequestAlertException("A new perfil cannot already have an ID", ENTITY_NAME, "idexists");
		}

		Perfil result = perfilRepository.save(nuevoPerfil);
		return ResponseEntity.created(new URI("/api/perfiles/" + result.getIdPerfil()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPerfil().toString()))
				.body(result);
	}

	public Perfil getPerfil(final Long idPerfil) {
		Optional<Perfil> perfil = perfilRepository.findById(idPerfil);
		if (!perfil.isPresent()) {
			throw new IllegalArgumentException("No existe un perfil con ese identificador.");
		}
		return perfil.get();
	}

	public List<Perfil> getAllPerfiles() {
		return perfilRepository.findAll();
	}

	public ResponseEntity<Perfil> updatePerfil(final Perfil perfilModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Perfil : {}", perfilModificado);
		if (perfilModificado.getIdPerfil() == null) {
			return createPerfil(perfilModificado);
		}

		Perfil result = perfilRepository.save(perfilModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, perfilModificado.getIdPerfil().toString()))
				.body(result);

	}

	public void deletePerfil(final Long idPerfil) {
		Optional<Perfil> perfil = perfilRepository.findById(idPerfil);
		if (!perfil.isPresent()) {
			throw new IllegalArgumentException("No existe un perfil con ese identificador.");
		}
	 
		perfilRepository.deleteById(idPerfil);
	}
	
}

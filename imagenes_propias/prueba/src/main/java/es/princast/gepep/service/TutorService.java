package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Tutor;
import es.princast.gepep.repository.TutorRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TutorService {

	private static final String ENTITY_NAME = "tutor";

	@Autowired
	private TutorRepository tutorRepository;


	public ResponseEntity<Tutor> createTutor(final Tutor nuevoTutor) throws URISyntaxException {
		log.debug("SERVICE request to save Tutor : {}", nuevoTutor);

		if (nuevoTutor.getIdTutor() != null) {
			throw new BadRequestAlertException("A new tutor cannot already have an ID", ENTITY_NAME, "idexists");
		}

		Tutor result = tutorRepository.save(nuevoTutor);
		return ResponseEntity.created(new URI("/api/tutores/" + result.getIdTutor()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdTutor().toString()))
				.body(result);
	}

	public Tutor getTutor(final Long idTutor) {
		Optional<Tutor> tutor = tutorRepository.findById(idTutor);
		if (!tutor.isPresent()) {
			throw new IllegalArgumentException("No existe una tutor con ese identificador.");
		}
		return tutor.get();
	}

	public List<Tutor> getAllTutores() {
		return tutorRepository.findAll();
	}

	public ResponseEntity<Tutor> updateTutor(final Tutor tutorModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Tutor : {}", tutorModificado);
		if (tutorModificado.getIdTutor() == null) {
			return createTutor(tutorModificado);
		}

		Tutor result = tutorRepository.save(tutorModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tutorModificado.getIdTutor().toString()))
				.body(result);

	}

	public void deleteTutor(final Long idTutor) {
		Optional<Tutor> tutor = tutorRepository.findById(idTutor);
		if (!tutor.isPresent()) {
			throw new IllegalArgumentException("No existe una tutor con ese identificador.");
		}
		tutorRepository.deleteById(idTutor);
	}

	
}

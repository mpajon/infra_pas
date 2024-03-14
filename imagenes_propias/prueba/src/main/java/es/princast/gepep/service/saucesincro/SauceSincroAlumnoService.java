package es.princast.gepep.service.saucesincro;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.repository.AlumnoRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroAlumnoService extends SauceSincroAuditoriaServices {

	private static final String ENTITY_NAME = "alumno";

	@Autowired
	private AlumnoRepository alumnoRepository;
	
	 @Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<Alumno> createAlumno(final Alumno nuevoAlumno) throws URISyntaxException {
		log.debug("SERVICE request to save Alumno : {}", nuevoAlumno);

		if (nuevoAlumno.getIdAlumno() == null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_alumno')").getSingleResult();
			String generatedId =  nextId.toString();
			nuevoAlumno.setIdAlumno(generatedId);		
		}
		nuevoAlumno.setCreatedBy(SauceSincroService.USU_SINCRO);
		Alumno result = alumnoRepository.save(nuevoAlumno);
		incrementarInsertados();
		return ResponseEntity.created(new URI("/api/alumnos/" + result.getIdAlumno()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdAlumno().toString()))
				.body(result);
	}

	public Alumno getAlumno(final String idAlumno) {
		Optional<Alumno> alumno = alumnoRepository.findById(idAlumno);
		if (!alumno.isPresent()) {
			throw new IllegalArgumentException("No existe un alumno con ese identificador.");
		}
		return alumno.get();
	}

	
	public ResponseEntity<Alumno> updateAlumno(final Alumno alumnoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Alumno : {}", alumnoModificado);
		if (alumnoModificado.getIdAlumno() == null) {
			return createAlumno(alumnoModificado);
		}
		alumnoModificado.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		Alumno result = alumnoRepository.save(alumnoModificado);
		incrementarActualizados();
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alumnoModificado.getIdAlumno().toString()))
				.body(result);

	}

	
}

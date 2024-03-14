package es.princast.gepep.service.saucesincro;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.repository.AlumnoRepository;
import es.princast.gepep.repository.MatriculaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroMatriculaService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "matricula";

	@Autowired
	private MatriculaRepository matriculaRepository;

	
	@Autowired
	private AlumnoRepository alumnoRepository;
	
	@Autowired
	private EntityManager entityManager; 

	public ResponseEntity<Matricula> createMatricula(final Matricula nuevaMatricula) throws URISyntaxException {
		log.debug("SERVICE request to save Matricula : {}", nuevaMatricula);
		
	 	  if (nuevaMatricula.getIdMatricula() == null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_matricula')").getSingleResult();
			String generatedId =  nextId.toString();
			nuevaMatricula.setIdMatricula(generatedId);		    	 
		 }	 
		
//	 	if (nuevaMatricula.getFechaMatricula() == null )
//	 		nuevaMatricula.setFechaMatricula(LocalDate.now());
	 	
//	 	if (nuevaMatricula.getAlumno().getIdAlumno() == null) {
//	 		Optional<Alumno> nuevoAlumno = alumnoRepository.getOneByNif(nuevaMatricula.getAlumno().getNif());
//	 		if (nuevoAlumno.get() !=null)
//	 			nuevaMatricula.setAlumno(nuevoAlumno.get());
//	 		else
//	 			throw new IllegalArgumentException("No existe alumno con ese nif.");
//	 	}
	 	nuevaMatricula.setCreatedBy(SauceSincroService.USU_SINCRO);
		Matricula result = matriculaRepository.save(nuevaMatricula);
		incrementarInsertados();
		return ResponseEntity.created(new URI("/api/matriculas/" + result.getIdMatricula()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdMatricula().toString()))
				.body(result);
	}

	public Matricula getMatricula(final String idMatricula) {
		Optional<Matricula> matricula = matriculaRepository.findById(idMatricula);
		if (!matricula.isPresent()) {
			throw new IllegalArgumentException("No existe un matricula con ese identificador.");
		}
		return matricula.get();
	}
 
	

	public ResponseEntity<Matricula> updateMatricula(final Matricula matriculaModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Matricula : {}", matriculaModificado);
		if (matriculaModificado.getIdMatricula() == null) {
			return createMatricula(matriculaModificado);
		}
		matriculaModificado.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		Matricula result = matriculaRepository.save(matriculaModificado);
		incrementarActualizados();
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, matriculaModificado.getIdMatricula().toString()))
				.body(result);

	}



}

package es.princast.gepep.service.saucesincro;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Profesor;
import es.princast.gepep.repository.ProfesorRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroProfesorService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "profesor";

	@Autowired
	private ProfesorRepository profesorRepository;
	
	@Autowired
	 private EntityManager entityManager; 

	public void createProfesor(final Profesor nuevoProfesor) throws URISyntaxException {
		log.debug("SERVICE request to save Profesor : {}", nuevoProfesor);
		try {
		 if (nuevoProfesor.getIdProfesor() == null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_profesor')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevoProfesor.setIdProfesor(generatedId);		    	 
			 }	
		 
		nuevoProfesor.setCreatedBy(SauceSincroService.USU_SINCRO);	
		profesorRepository.save(nuevoProfesor);
		this.incrementarInsertados();
		}catch(Exception e) {
			log.error("SauceSincroProfesorService.createProfesor " + nuevoProfesor.getIdProfesor(), e);
		}
		
	}

	public Profesor getProfesor(final String idProfesor) {
		
		Optional<Profesor> profesor = profesorRepository.findById(idProfesor);
		if (!profesor.isPresent()) {
			throw new IllegalArgumentException("No existe una profesor con ese identificador.");
		}
		return profesor.get();
		
	}
	
	public Profesor getProfesorActivoByNif(final String nif) {		
		
	    Optional<Profesor> profesor = profesorRepository.getProfesorActivoByNifAndFeBaja(nif, LocalDate.now());
		if (profesor.isPresent())
			return profesor.get();
		else 
			return null;		
	}
	

	public void updateProfesor(final Profesor profesorModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Profesor : {}", profesorModificado);
		try {
		if (profesorModificado.getIdProfesor() == null) {
			createProfesor(profesorModificado);
		}
		profesorModificado.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		profesorRepository.save(profesorModificado);
		this.incrementarActualizados();
		}catch(Exception e) {
			log.error("SauceSincroProfesorService.updateProfesor "+ profesorModificado.getIdProfesor(), e);
		}
		

	}



}

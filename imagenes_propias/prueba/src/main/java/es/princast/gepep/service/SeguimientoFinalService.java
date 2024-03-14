package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.ControlSeguimiento;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.SeguimientoFinal;
import es.princast.gepep.repository.ControlSeguimientoRepository;
import es.princast.gepep.repository.SeguimientoFinalRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SeguimientoFinalService {

	private static final String ENTITY_NAME = "seguimientoFinal";

	@Autowired
	private SeguimientoFinalRepository seguimientoFinalRepository;
	
	@Autowired
	private ControlSeguimientoRepository controlSeguimientoRepository;
	

	public ResponseEntity<SeguimientoFinal> createSeguimientoFinal(final SeguimientoFinal nuevoSeguimientoFinal) throws URISyntaxException {
		log.debug("SERVICE request to save SeguimientoFinal : {}", nuevoSeguimientoFinal);

		if (nuevoSeguimientoFinal.getIdSeguimientoFinal() != null) {
			throw new BadRequestAlertException("A new seguimientoFinal cannot already have an ID", ENTITY_NAME, "idexists");
		}

		SeguimientoFinal result = seguimientoFinalRepository.save(nuevoSeguimientoFinal);
		return ResponseEntity.created(new URI("/api/seguimiento-final/" + result.getIdSeguimientoFinal()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdSeguimientoFinal().toString()))
				.body(result);
	}

	public SeguimientoFinal getSeguimientoFinal(final Long idSeguimientoFinal) {
		Optional<SeguimientoFinal> seguimientoFinal = seguimientoFinalRepository.findById(idSeguimientoFinal);
		if (!seguimientoFinal.isPresent()) {
			throw new IllegalArgumentException("No existe un seguimientoFinal con ese identificador.");
		}
		return seguimientoFinal.get();
	}


	public SeguimientoFinal getSeguimientoFinalByMatricula(final String idMatricula) {
		
		Matricula matricula = new Matricula();
		matricula.setIdMatricula(idMatricula);		 
		Optional<SeguimientoFinal> seguimientoFinal = seguimientoFinalRepository.findOneByMatricula(matricula);
		if (!seguimientoFinal.isPresent()) {
			//throw new IllegalArgumentException("No existe un seguimientoFinal con ese identificador.");
			return null;
		}
		return seguimientoFinal.get();
		
		 
	}


	public List<SeguimientoFinal> getSeguimientoFinalByCursoAndCentroAndTutor(String idCentro,Integer anio, String tutor){
		return (List<SeguimientoFinal>) seguimientoFinalRepository.getSeguimientoFinalByCursoAndCentroAndTutor(idCentro, anio, tutor);
	}
	
	
	public List<SeguimientoFinal> getAllSeguimientoFinales() {
		return seguimientoFinalRepository.findAll();
	}
	

	public ResponseEntity<SeguimientoFinal> updateSeguimientoFinal(final SeguimientoFinal seguimientoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update SeguimientoFinal : {}", seguimientoModificado);
		if (seguimientoModificado.getIdSeguimientoFinal() == null) {
			return createSeguimientoFinal(seguimientoModificado);
		}

		SeguimientoFinal result = seguimientoFinalRepository.save(seguimientoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, seguimientoModificado.getIdSeguimientoFinal().toString()))
				.body(result);

	}

	public void deleteSeguimientoFinal(final Long idSeguimientoFinal) {
		Optional<SeguimientoFinal> seguimientoFinal = seguimientoFinalRepository.findById(idSeguimientoFinal);
		if (!seguimientoFinal.isPresent()) {
			throw new IllegalArgumentException("No existe un seguimientoFinal con ese identificador.");
		}
		

		seguimientoFinalRepository.deleteById(idSeguimientoFinal);
	}
	
	
	public List<ControlSeguimiento> getResumenControlSeguimiento(String idCentro,Integer anio, String tutor){
		return (List<ControlSeguimiento>) controlSeguimientoRepository.getResumenControlSeguimiento(idCentro, anio, tutor);
	}
	
	public List<ControlSeguimiento> getContadoresControlSeguimiento(String idCentro,Integer anio, String tutor){
		return (List<ControlSeguimiento>) controlSeguimientoRepository.getContadoresControlSeguimiento(idCentro, anio, tutor);
	}
	
	
	
}

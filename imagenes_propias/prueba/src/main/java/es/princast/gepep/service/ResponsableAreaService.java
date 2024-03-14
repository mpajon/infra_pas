package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.ResponsableArea;
import es.princast.gepep.repository.AreaRepository;
import es.princast.gepep.repository.ResponsableAreaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ResponsableAreaService {

	private static final String ENTITY_NAME = "responsableArea";

	@Autowired
	private ResponsableAreaRepository responsableAreaRepository;
	@Autowired
	private AreaRepository areaRepository;


	public ResponseEntity<ResponsableArea> createResponsableArea(final ResponsableArea nuevaResponsableArea) throws URISyntaxException {
		log.debug("SERVICE request to save ResponsableArea : {}", nuevaResponsableArea);

		if (nuevaResponsableArea.getIdResponsableArea() != null) {
			throw new BadRequestAlertException("A new responsableArea cannot already have an ID", ENTITY_NAME, "idexists");
		}

		ResponsableArea result = responsableAreaRepository.save(nuevaResponsableArea);
		return ResponseEntity.created(new URI("/api/responsable-area/" + result.getIdResponsableArea()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdResponsableArea().toString()))
				.body(result);
	}

	public ResponsableArea getResponsableArea(final Long idResponsableArea) {
		Optional<ResponsableArea> responsableArea = responsableAreaRepository.findById(idResponsableArea);
		if (!responsableArea.isPresent()) {
			throw new IllegalArgumentException("No existe una responsableArea con ese identificador.");
		}
		return responsableArea.get();
	}

	public List<ResponsableArea> getAllResponsableAreaes() {
		return responsableAreaRepository.findAll();
	}

	public ResponseEntity<ResponsableArea> updateResponsableArea(final ResponsableArea responsableAreaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update ResponsableArea : {}", responsableAreaModificada);
		if (responsableAreaModificada.getIdResponsableArea() == null) {
			return createResponsableArea(responsableAreaModificada);
		}

		ResponsableArea result = responsableAreaRepository.save(responsableAreaModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, responsableAreaModificada.getIdResponsableArea().toString()))
				.body(result);

	}

	public void deleteResponsableArea(final Long idResponsableArea) {
		Optional<ResponsableArea> responsableArea = responsableAreaRepository.findById(idResponsableArea);
		if (!responsableArea.isPresent()) {
			throw new IllegalArgumentException("No existe una responsableArea con ese identificador.");
		}
	 
		responsableAreaRepository.deleteById(idResponsableArea);
	}

	public Iterable<ResponsableArea> getResponsablByArea(final Long idArea) {
	
		Iterable<ResponsableArea> listaResponsableArea =  responsableAreaRepository.findByArea(areaRepository.getOne(idArea));
		
		return listaResponsableArea;
	}
 
}

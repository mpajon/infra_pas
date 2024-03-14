package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Municipio;
import es.princast.gepep.repository.MunicipioRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MunicipioService {

	private static final String ENTITY_NAME = "municipio";

	@Autowired
	private MunicipioRepository municipioRepository;


	public ResponseEntity<Municipio> createMunicipio(final Municipio nuevoMunicipio) throws URISyntaxException {
		log.debug("SERVICE request to save Municipio : {}", nuevoMunicipio);

		if (nuevoMunicipio.getIdMunicipio() != null) {
			throw new BadRequestAlertException("A new municipio cannot already have an ID", ENTITY_NAME, "idexists");
		}

		nuevoMunicipio.setMunicipio(nuevoMunicipio.getMunicipio().toUpperCase());
		
		Municipio result = municipioRepository.save(nuevoMunicipio);
		return ResponseEntity.created(new URI("/api/municipios/" + result.getIdMunicipio()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdMunicipio().toString()))
				.body(result);
	}

	public Municipio getMunicipio(final Long idMunicipio) {
		Optional<Municipio> municipio = municipioRepository.findById(idMunicipio);
		if (!municipio.isPresent()) {
			throw new IllegalArgumentException("No existe un municipio con ese identificador.");
		}
		return municipio.get();
	}

	private Sort sortByAsc() {
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "municipio").ignoreCase();
		return Sort.by(order);
	}
	
	
	public List<Municipio> getAllMunicipios() {
		return municipioRepository.findAll(sortByAsc());
	}

	public ResponseEntity<Municipio> updateMunicipio(final Municipio municipioModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Municipio : {}", municipioModificado);
		if (municipioModificado.getIdMunicipio() == null) {
			return createMunicipio(municipioModificado);
		}

		Municipio result = municipioRepository.save(municipioModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, municipioModificado.getIdMunicipio().toString()))
				.body(result);

	}

	public void deleteMunicipio(final Long idMunicipio) {
		Optional<Municipio> municipio = municipioRepository.findById(idMunicipio);
		if (!municipio.isPresent()) {
			throw new IllegalArgumentException("No existe una municipio con ese identificador.");
		}
	 

		municipioRepository.deleteById(idMunicipio);
	}
	
}

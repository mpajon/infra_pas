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

import es.princast.gepep.domain.Unidad;
import es.princast.gepep.repository.UnidadRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SauceSincroUnidadService extends SauceSincroAuditoriaServices{

	private static final String ENTITY_NAME = "unidad";

	@Autowired
	private UnidadRepository unidadRepository;

	@Autowired
	private EntityManager entityManager;


	public ResponseEntity<Unidad> createUnidad(final Unidad nuevaUnidad) throws URISyntaxException {
		log.debug("SERVICE request to save Unidad : {}", nuevaUnidad);

		if (nuevaUnidad.getIdUnidad() == null) {
			BigInteger nextId = (BigInteger) entityManager.createNativeQuery("select NEXTVAL('public.sec_unidad')")
					.getSingleResult();
			String generatedId = nextId.toString();
			nuevaUnidad.setIdUnidad(generatedId);
		}
		nuevaUnidad.setCreatedBy(SauceSincroService.USU_SINCRO);
		Unidad result = unidadRepository.save(nuevaUnidad);
		this.incrementarInsertados();
		return ResponseEntity.created(new URI("/api/unidades/" + result.getIdUnidad()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdUnidad().toString()))
				.body(result);
	}

	public Unidad getUnidad(final String idUnidad) {
		Optional<Unidad> unidad = unidadRepository.findById(idUnidad);
		if (!unidad.isPresent()) {
			throw new IllegalArgumentException("No existe un unidad con ese identificador.");
		}
		return unidad.get();
	}


	public ResponseEntity<Unidad> updateUnidad(final Unidad unidadModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Unidad : {}", unidadModificada);
		if (unidadModificada.getIdUnidad() == null) {
			return createUnidad(unidadModificada);
		}
		unidadModificada.setLastModifiedBy(SauceSincroService.USU_SINCRO);
		Unidad result = unidadRepository.save(unidadModificada);
		this.incrementarActualizados();
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, unidadModificada.getIdUnidad().toString()))
				.body(result);

	}

	
		

}

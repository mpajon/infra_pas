package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Actividad;
import es.princast.gepep.repository.ActividadRepository;
import es.princast.gepep.repository.AreaRepository;
import es.princast.gepep.repository.SectorRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ActividadService {

	private static final String ENTITY_NAME = "actividad";

	@Autowired
	private ActividadRepository actividadRepository;

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private MessageSource messageSource;

	public ResponseEntity<Actividad> createActividad(final Actividad nuevaActividad) throws URISyntaxException {
		log.debug("SERVICE request to save Actividad : {}", nuevaActividad);

		if (nuevaActividad.getIdActividad() != null) {
			throw new BadRequestAlertException("A new actividad cannot already have an ID", ENTITY_NAME, "idexists");
		}

		List<Actividad> actividadDuplicada = this.actividadRepository
				.findAllByCodigoAndNombre(nuevaActividad.getCodigo(), nuevaActividad.getNombre());
		List<Actividad> actividadDuplicadaSector = this.actividadRepository
				.findAllByCodigoAndSector(nuevaActividad.getCodigo(), nuevaActividad.getSector());

		if (actividadDuplicadaSector.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.actividad.existe.sector", null, LocaleContextHolder.getLocale()));
		} else if (actividadDuplicada.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.actividad.existe", null, LocaleContextHolder.getLocale()));
		}

		Actividad result = actividadRepository.save(nuevaActividad);
		return ResponseEntity.created(new URI("/api/actividades/" + result.getIdActividad()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdActividad().toString()))
				.body(result);
	}

	public Actividad getActividad(final Long idActividad) {
		Optional<Actividad> actividad = actividadRepository.findById(idActividad);
		if (!actividad.isPresent()) {
			throw new IllegalArgumentException("No existe una actividad con ese identificador.");
		}
		return actividad.get();
	}

	public List<Actividad> getAllActividades() {
		String[] propiedades = { "codigo", "nombre" };
		Sort sort = Sort.by(Sort.Direction.ASC, propiedades);
		return actividadRepository.findAll(sort);
	}

	public ResponseEntity<Actividad> updateActividad(final Actividad actividadModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Actividad : {}", actividadModificada);
		if (actividadModificada.getIdActividad() == null) {
			return createActividad(actividadModificada);
		}

		List<Actividad> actividadDuplicada = this.actividadRepository
				.findAllByCodigoAndNombre(actividadModificada.getCodigo(), actividadModificada.getNombre());
		List<Actividad> actividadDuplicadaSector = this.actividadRepository
				.findAllByCodigoAndSector(actividadModificada.getCodigo(), actividadModificada.getSector());

		if (actividadDuplicadaSector.size() > 0) {
			if (actividadDuplicadaSector.size() == 1
					&& actividadDuplicadaSector.get(0).getIdActividad() == actividadModificada.getIdActividad()) {
				Actividad result = actividadRepository.save(actividadModificada);
				return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
						actividadModificada.getIdActividad().toString())).body(result);
			} else {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.actividad.existe.sector", null, LocaleContextHolder.getLocale()));
			}
		} else

		if (actividadDuplicada.size() > 0) {
			if (actividadDuplicada.size() == 1
					&& actividadDuplicada.get(0).getIdActividad() == actividadModificada.getIdActividad()) {
				Actividad result = actividadRepository.save(actividadModificada);
				return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
						actividadModificada.getIdActividad().toString())).body(result);
			} else {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.actividad.existe", null, LocaleContextHolder.getLocale()));
			}
		}

		Actividad result = actividadRepository.save(actividadModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actividadModificada.getIdActividad().toString()))
				.body(result);

	}

	public void deleteActividad(final Long idActividad) {
		Optional<Actividad> actividad = actividadRepository.findById(idActividad);
		if (!actividad.isPresent()) {
			throw new IllegalArgumentException("No existe una actividad con ese identificador.");
		}

		List<es.princast.gepep.domain.Area> listaAreas = areaRepository.findAllByActividad(actividad.get());
		if (listaAreas.size() > 0) {
			// No se puede borrar porque está referencenciado en actividad
			throw new IllegalArgumentException(messageSource.getMessage("error.actividad.referenciado.area", null,
					LocaleContextHolder.getLocale()));
		}

		actividadRepository.deleteById(idActividad);
	}

	public Iterable<Actividad> getActividadesBySector(final Long idSector) {

		Iterable<Actividad> listaActividad = actividadRepository.findBySector(sectorRepository.getOne(idSector));

		return listaActividad;
	}

	// public List<Actividad> getAllActividadesByCriteria(final Actividad
	// partialMatch) {
	// return actividadRepository.findAll(new Specification<Actividad>() {
	//
	// @Override
	// public Predicate toPredicate(Root<Actividad> root, CriteriaQuery<?> query,
	// CriteriaBuilder builder) {
	// List<Predicate> predicates = new ArrayList<>();
	//
	// if (!StringUtils.isEmpty(partialMatch.getCodigo())) {
	// predicates.add(builder.like(builder.upper(root.get("codigo")),
	// "%" + partialMatch.getCodigo().toUpperCase() + "%"));
	// }
	//
	// if (!StringUtils.isEmpty(partialMatch.getNombre())) {
	// predicates.add(builder.like(builder.upper(root.get("codigoExterno")),
	// "%" + partialMatch.getNombre().toUpperCase() + "%"));
	// }
	// return builder.and(predicates.toArray(new Predicate[]{}));
	// }
	//
	// });
	// }
}

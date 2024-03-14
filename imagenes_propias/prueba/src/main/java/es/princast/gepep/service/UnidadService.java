package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.Unidad;
import es.princast.gepep.repository.OfertasCentroRepository;
import es.princast.gepep.repository.ProfesorRepository;
import es.princast.gepep.repository.UnidadRepository;
import es.princast.gepep.web.rest.filter.UnidadFilter;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UnidadService {

	private static final String ENTITY_NAME = "unidad";

	@Autowired
	private UnidadRepository unidadRepository;
	
	@Autowired
	private OfertasCentroRepository ofertasCentroRepository;
	
	@Autowired
	private ProfesorRepository profesorRepository;

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

		Unidad result = unidadRepository.save(nuevaUnidad);
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

	public List<Unidad> getAllUnidades() {
		return unidadRepository.findAll();
	}

	public ResponseEntity<Unidad> updateUnidad(final Unidad unidadModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Unidad : {}", unidadModificada);
		if (unidadModificada.getIdUnidad() == null) {
			return createUnidad(unidadModificada);
		}

		Unidad result = unidadRepository.save(unidadModificada);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, unidadModificada.getIdUnidad().toString()))
				.body(result);

	}

	public void deleteUnidad(final String idUnidad) {
		Optional<Unidad> unidad = unidadRepository.findById(idUnidad);
		if (!unidad.isPresent()) {
			throw new IllegalArgumentException("No existe una unidad con ese identificador.");
		}

		unidadRepository.deleteById(idUnidad);
	}
	
	
	@SuppressWarnings({"serial" })
	public List<Unidad> getAllUnidadesByOfertaCentro(final String idOfertaCentro, Integer anio) {
		Optional<OfertasCentro> ofertaCentro = ofertasCentroRepository.findById(idOfertaCentro);
		if (!ofertaCentro.isPresent()) {
			throw new IllegalArgumentException("No existe una oferta con ese identificador.");
		}		 

		return unidadRepository.findAllByOfertaCentroAndAnio(ofertaCentro, anio);
	}

	
	@SuppressWarnings({"serial" })
	public List<Unidad> getAllUnidadesByOfertaCentroAndNombre(final String idOfertaCentro, Integer anio,String nombre) {
		Optional<OfertasCentro> ofertaCentro = ofertasCentroRepository.findById(idOfertaCentro);
		if (!ofertaCentro.isPresent()) {
			throw new IllegalArgumentException("No existe una oferta con ese identificador.");
		}		 

		return unidadRepository.findAllByOfertaCentroAndAnioAndNombre(ofertaCentro, anio,nombre);
	}

	
	public List<Unidad> getAllUnidadesByTutor(final String idTutor) {
		Optional<Profesor> tutor = profesorRepository.findById(idTutor);
		if (!tutor.isPresent()) {
			throw new IllegalArgumentException("No existe un tutor con ese identificador.");
		}		 

		return unidadRepository.findAllByTutorOrTutorAdicional(sortByAnioDesc(), tutor, tutor);
	}
	
	public List<Unidad> findAllUnidadesConTutor() {
		return unidadRepository.findAllByTutorIsNotNull();
	}
	
	private Sort sortByAnioDesc() {
	 	Sort.Order order = new Sort.Order(Sort.Direction.DESC, "anio").ignoreCase();
		return Sort.by(order);
    }


	
	public List<Unidad> getAllUnidadesByTutorAndAnio(final String idTutor, Integer anio) {
		Optional<Profesor> tutor = profesorRepository.findById(idTutor);
		if (!tutor.isPresent()) {
			throw new IllegalArgumentException("No existe un tutor con ese identificador.");
		}		 

		return unidadRepository.findAllByTutorOrTutorAdicionalAndAnio(tutor.get(), tutor.get(), anio);
	}

						
	public Page<Unidad> getAllUnidadesByCriteria(final UnidadFilter partialMatch, Pageable pageable) {
		return unidadRepository.findAll(new Specification<Unidad>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Unidad> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();

				if (!StringUtils.isEmpty(partialMatch.getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("nombre")),
							"%" + partialMatch.getNombre().toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getNombreOferta())) {
					predicates.add(builder.like(builder.upper(root.get("ofertaCentro").get("oferta").get("nombre")),
							"%" + partialMatch.getNombreOferta().toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getNombreCentro())) {
					predicates.add(builder.like(builder.upper(root.get("ofertaCentro").get("centro").get("nombre")),
							"%" + partialMatch.getNombreCentro().toUpperCase() + "%"));
				}
			
				if (partialMatch.getAnio() != null && partialMatch.getAnio() != -1) {
					predicates.add(builder.equal(root.get("anio"), partialMatch.getAnio()));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getIdCentro())) {
					predicates.add(builder.equal(root.get("ofertaCentro").get("centro").get("idCentro"), partialMatch.getIdCentro()));
				}
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		}, pageable);
	}
		

}

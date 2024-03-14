package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.Area;
import es.princast.gepep.domain.Empresa;
import es.princast.gepep.domain.ResponsableArea;
import es.princast.gepep.repository.AreaRepository;
import es.princast.gepep.repository.EmpresaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AreaService {

	private static final String ENTITY_NAME = "area";

	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;


	public ResponseEntity<Area> createArea(final Area nuevaArea) throws URISyntaxException {
		log.debug("SERVICE request to save Area : {}", nuevaArea);

		if (nuevaArea.getIdArea() != null) {
			throw new BadRequestAlertException("A new area cannot already have an ID", ENTITY_NAME, "idexists");
		}
		
		// Cargamos la empresa en contexto //
		if(nuevaArea.getEmpresa().getIdEmpresa()!=null) {
			Empresa empresa = empresaRepository.findById(nuevaArea.getEmpresa().getIdEmpresa()).get();
			nuevaArea.setEmpresa(empresa);
		}
		
		// Guardamos el area //
		Area result = areaRepository.save(nuevaArea);
		return ResponseEntity.created(new URI("/api/areas/" + result.getIdArea()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdArea().toString())).body(result);
	}

	public Area getArea(final Long idArea) {
		Optional<Area> area = areaRepository.findById(idArea);
		if (!area.isPresent()) {
			throw new IllegalArgumentException("No existe una area con ese identificador.");
		}
		Hibernate.initialize(area.get().getResponsables());
		return area.get();
	}
	
	private Sort sortByIdAsc() {

		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		return Sort.by(order);
	}
	
	
	public Page<Area> getAllAreas(Pageable pageable) {
		return areaRepository.findAll(pageable);
	}

	
	
	public Page<Area> getAllAreasByCriteria(final Area partialMatch, Pageable pageable) {
		return areaRepository.findAll(new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				
				

				if (!StringUtils.isEmpty(partialMatch.getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("nombre")),
							"%" + partialMatch.getNombre().toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getLocalidad())) {
					predicates.add(builder.like(builder.upper(root.get("localidad")),
							"%" + partialMatch.getLocalidad().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getNombreEmpresaPro())) {
					predicates.add(builder.like(builder.upper(root.get("nombreEmpresaPro")),
							"%" + partialMatch.getNombreEmpresaPro().toUpperCase() + "%"));
				}

				if(partialMatch.getEmpresa()!=null) {
					if (!StringUtils.isEmpty(partialMatch.getEmpresa().getNombre())) {
						predicates.add(builder.like(builder.upper(root.get("empresa").get("nombre")),
								"%" + partialMatch.getEmpresa().getNombre().toUpperCase() + "%"));
					}
					if (!StringUtils.isEmpty(partialMatch.getEmpresa().getCif())) {
						predicates.add(builder.like(builder.upper(root.get("empresa").get("cif")),
								"%" + partialMatch.getEmpresa().getCif().toUpperCase() + "%"));
					}
					if (!StringUtils.isEmpty(partialMatch.getEmpresa().getCiclos())) {
						predicates.add(builder.like(builder.upper(root.get("empresa").get("ciclos")),
								"%" + partialMatch.getEmpresa().getCiclos().toUpperCase() + "%"));
					}
				}
				
				if(partialMatch.getVisada()!=null) {
					if(partialMatch.getVisada()==true) {
						predicates.add(builder.equal(root.get("visada"),partialMatch.getVisada()));
					}else {
						// Visada false or null //
						List<Predicate> predicateOr = new ArrayList<>();
						predicateOr.add(builder.isNull(root.get("visada")));
						predicateOr.add(builder.equal(root.get("visada"),partialMatch.getVisada()));
						predicates.add(builder.or(predicateOr.toArray(new Predicate[] {})));
						
						if(partialMatch.getMotivoNoVisada()!=null) {
							// Motivo rechazo no null ni cadena vacia //
							predicates.add(builder.isNotNull(root.get("motivoNoVisada")));
							predicates.add(builder.notEqual(root.get("motivoNoVisada"),""));
							predicates.add(builder.like(builder.upper(root.get("motivoNoVisada")),
									"%" + partialMatch.getMotivoNoVisada().toUpperCase() + "%"));
							
						}else {
							// Motivo rechazo nulo o cadena vacia //
							List<Predicate> predicateOrMotivo = new ArrayList<>();
							predicateOrMotivo.add(builder.isNull(root.get("motivoNoVisada")));
							predicateOrMotivo.add(builder.equal(root.get("motivoNoVisada"),""));
							predicates.add(builder.or(predicateOrMotivo.toArray(new Predicate[] {})));
							
						}
						
					}
				}
				
			
				

				return builder.and(predicates.toArray(new Predicate[] {}));

			}

		}, pageable);
	}

	public ResponseEntity<Area> updateArea(Area areaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Area : {}", areaModificada);
		if (areaModificada.getIdArea() == null) {
			return createArea(areaModificada);
		}

		// Si no recibimos el area dentro de los responsables se la asignamos//
		List<ResponsableArea> responsables = areaModificada.getResponsables();
		for (ResponsableArea responsableArea : responsables) {
			if(responsableArea.getArea()==null) {
				responsableArea.setArea(new Area());
				responsableArea.getArea().setIdArea(areaModificada.getIdArea());
			}
		}

		if(Boolean.TRUE.equals(areaModificada.getVisada())){
			areaModificada.getEmpresa().setNombre(areaModificada.getNombreEmpresaPro());
		}

		Area result = areaRepository.save(areaModificada);

		if(Boolean.TRUE.equals(areaModificada.getVisada())){
			this.updateAreasNombreEmpresaProByEmpresa(areaModificada);
		}

		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, areaModificada.getIdArea().toString()))
				.body(result);

	}


	public  Iterable<Area> getAreasByEmpresa(final Long idEmpresa) {
		log.debug("SERVICE request to get all Areas by Empresa");
		Iterable<Area> areas = areaRepository.findAllByEmpresa(this.empresaRepository.getOne(idEmpresa));
		return areas;
	}
	
	public  Iterable<Area> getAreasByEmpresaVisada(final Long idEmpresa) {
		log.debug("SERVICE request to get all Areas by Empresa");
		Iterable<Area> areas = areaRepository.findAllByEmpresaAndVisadaIsTrue(this.empresaRepository.getOne(idEmpresa));
		return areas;
	}
 	
 	
	public void deleteArea(final Long idArea) {
		Optional<Area> area = areaRepository.findById(idArea);
		if (!area.isPresent()) {
			throw new IllegalArgumentException("No existe una area con ese identificador.");
		}

		areaRepository.deleteById(idArea);
	}

	@Transactional
	private void updateAreasNombreEmpresaProByEmpresa (Area area){
		areaRepository.updateAreasNombreEmpresaProByEmpresa(area.getNombreEmpresaPro(), area.getEmpresa().getIdEmpresa());
	}
	
	
}

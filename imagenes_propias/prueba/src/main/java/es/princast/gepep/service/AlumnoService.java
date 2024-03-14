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
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.AlumnoExportSauce;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.repository.AlumnoExportSauceRepository;
import es.princast.gepep.repository.AlumnoRepository;
import es.princast.gepep.repository.MatriculaRepository;
import es.princast.gepep.web.rest.filter.AlumnoFilter;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AlumnoService {

	private static final String ENTITY_NAME = "alumno";

	@Autowired
	private AlumnoRepository alumnoRepository;
	
	@Autowired
	private AlumnoExportSauceRepository alumnoExportSauceRepository;
	
	@Autowired
	private MatriculaRepository matriculaRepository;

	@Autowired
	private MessageSource messageSource;
	
	 @Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<Alumno> createAlumno(final Alumno nuevoAlumno) throws URISyntaxException {
		log.debug("SERVICE request to save Alumno : {}", nuevoAlumno);

		if (nuevoAlumno.getIdAlumno() == null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_alumno')").getSingleResult();
			String generatedId =  nextId.toString();
			nuevoAlumno.setIdAlumno(generatedId);		
		}
		
		if (!StringUtils.isEmpty(nuevoAlumno.getNif())) {

			Optional<Alumno> alumnoDuplicado = this.alumnoRepository.getOneByNif(nuevoAlumno.getNif());
			if (alumnoDuplicado.isPresent() && alumnoDuplicado.get() != null) {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.alumno.existe", null, LocaleContextHolder.getLocale()));
			}

		}

		Alumno result = alumnoRepository.save(nuevoAlumno);
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

	
	public Alumno  getAlumnoByNif(final String nif) {
		Optional<Alumno> alumno = alumnoRepository.getOneByNif(nif);
		if (!alumno.isPresent()) {
			throw new IllegalArgumentException("No existe un alumno con ese NIf.");
		}
		return alumno.get();
	}


	public Page<Alumno> getAllAlumnos(Pageable page) {
		return alumnoRepository.findAll(page);
	}
	
	

	public ResponseEntity<Alumno> updateAlumno(final Alumno alumnoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Alumno : {}", alumnoModificado);
		if (alumnoModificado.getIdAlumno() == null) {
			return createAlumno(alumnoModificado);
		}
		

		 List<Alumno> alumnoDuplicado  = this.alumnoRepository.findAllByNif(alumnoModificado.getNif());
	 	 if (alumnoDuplicado.size()>1)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.alumno.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }

		Alumno result = alumnoRepository.save(alumnoModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alumnoModificado.getIdAlumno().toString()))
				.body(result);

	}

	public void deleteAlumno(final String idAlumno) {
		Optional<Alumno> alumno = alumnoRepository.findById(idAlumno);
		if (!alumno.isPresent()) {
			throw new IllegalArgumentException("No existe un alumno con ese identificador.");
		}
		
		List<Matricula> listaMatriculas = matriculaRepository.findAllByAlumnoOrderByAnioAscCursoAsc(alumno.get());
        if (listaMatriculas.size()>0){
           // No se puede borrar porque está referencenciado en matricula activo
          throw new IllegalArgumentException(messageSource.getMessage("error.alumno.referenciado.matricula",
                   null, LocaleContextHolder.getLocale()));}
		
		alumnoRepository.deleteById(idAlumno);
	}

	public Page<Alumno> getAllAlumnosByCriteria(final AlumnoFilter partialMatch, Pageable pageable) {
		return alumnoRepository.findAll(new Specification<Alumno>() {

			@Override
			public Predicate toPredicate(Root<Alumno> rootAlumno, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();

				if (!StringUtils.isEmpty(partialMatch.getNombre())) {
					predicates.add(builder.like(builder.upper(rootAlumno.get("nombre")),
							"%" + partialMatch.getNombre().toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getNif())) {
					predicates.add(builder.like(builder.upper(rootAlumno.get("nif")),
							"%" + partialMatch.getNif().toUpperCase() + "%"));
				}
			
				
				if (!StringUtils.isEmpty(partialMatch.getApellido1())) {
					predicates.add(builder.like(builder.upper(rootAlumno.get("apellido1")),
							"%" + partialMatch.getApellido1().toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getApellido2())) {
					predicates.add(builder.like(builder.upper(rootAlumno.get("apellido2")),
							"%" + partialMatch.getApellido2().toUpperCase() + "%"));
				}

				// Si tuvieramos un mapeo bidireccional de hibernate podriamos obtener las matriculas asi: 
				//Join<Alumno, Matricula> matricula = rootAlumno.join("matriculas", JoinType.LEFT);
				//matricula.on(builder.equal(matricula.get("alumno").get("idAlumno"), rootAlumno.get("idAlumno")));
				
				if (partialMatch.getIdCentro() != null || partialMatch.getAnioMatricula() != null) {

					// IN subquery de matriculas del centro (para obtener alumnos de un centro)//

					final CriteriaQuery<Matricula> cq = builder.createQuery(Matricula.class);
					Subquery<Matricula> subquery = cq.subquery(Matricula.class);
					Root subRoot = subquery.from(Matricula.class);
					List<Predicate> subPredicates = new ArrayList<>();
					if (partialMatch.getIdCentro() != null) {
						subPredicates.add(builder.equal(subRoot.get("ofertaCentro").get("centro").get("idCentro"),
								partialMatch.getIdCentro()));
					}
					if (partialMatch.getAnioMatricula() != null) {
						subPredicates.add(builder.equal(subRoot.get("anio"), partialMatch.getAnioMatricula()));
					}
					subquery.where(builder.and(subPredicates.toArray(new Predicate[] {})));
					subquery.select(subRoot.get("alumno"));
					subquery.groupBy(subRoot.get("alumno"));

					predicates.add(rootAlumno.in(subquery));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		}, pageable);
	}
	
	
	
	public List<AlumnoExportSauce> getAlumnosExportSauce( Integer anio ) {
		return  (List<AlumnoExportSauce>)alumnoExportSauceRepository.getAlumnosExportSauce(anio);
	}
	
	
	
	
}

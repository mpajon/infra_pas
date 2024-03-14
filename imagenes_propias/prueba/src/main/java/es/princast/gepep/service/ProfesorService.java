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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.ProfesorCentro;
import es.princast.gepep.domain.TutorCentro;
import es.princast.gepep.domain.Usuario;
import es.princast.gepep.domain.VisitaTutor;
import es.princast.gepep.repository.MatriculaRepository;
import es.princast.gepep.repository.ProfesorCentroRepositoy;
import es.princast.gepep.repository.ProfesorRepository;
import es.princast.gepep.repository.TutorCentroRepository;
import es.princast.gepep.repository.VisitaTutorRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProfesorService {

	private static final String ENTITY_NAME = "profesor";

	@Autowired
	private ProfesorRepository profesorRepository;
	
	@Autowired
	private ProfesorCentroRepositoy profesorCentroRepository;

	@Autowired
	private TutorCentroRepository tutorCentroRepository;
	
	@Autowired
	private VisitaTutorRepository visitaTutorRepository;
	
	@Autowired
	private MatriculaRepository matriculaRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	 private EntityManager entityManager; 

	public ResponseEntity<Profesor> createProfesor(final Profesor nuevoProfesor) throws URISyntaxException {
		log.debug("SERVICE request to save Profesor : {}", nuevoProfesor);

		 if (nuevoProfesor.getIdProfesor() == null) {
				BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_profesor')").getSingleResult();
				String generatedId =  nextId.toString();
				nuevoProfesor.setIdProfesor(generatedId);		    	 
			 }	
		 
			
		 List<Profesor> profesorDuplicado  = this.profesorRepository.findAllByNif(nuevoProfesor.getNif());
	 	 if (profesorDuplicado.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.profesor.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }

		Profesor result = profesorRepository.save(nuevoProfesor);
		return ResponseEntity.created(new URI("/api/profesores/" + result.getIdProfesor()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdProfesor().toString()))
				.body(result);
	}

	public Profesor getProfesor(final String idProfesor) {
		Optional<Profesor> profesor = profesorRepository.findById(idProfesor);
		if (!profesor.isPresent()) {
			throw new IllegalArgumentException("No existe una profesor con ese identificador.");
		}
		return profesor.get();
	}
	
	public Profesor getProfesorByNif(final String nif) {
		Optional<Profesor> profesor = profesorRepository.findOneByNif(nif);
		if (!profesor.isPresent()) {
			throw new IllegalArgumentException("No existe una profesor con ese identificador (NIF/Pasaporte/Tarjeta)");
		}
		return profesor.get();
	}
	
	public Profesor getProfesorByUsuario(final Usuario usuario) {
		Optional<Profesor> profesor = profesorRepository.findOneByUsuario(usuario);
		if (!profesor.isPresent()) {
			return null;
		}
		return profesor.get();
	}

	public Page<Profesor> getAllProfesores(Pageable page) {
		return profesorRepository.findAll(page);
	}	
	
	public List<TutorCentro> getTutoresByCentroAndAnio(final String centro, Integer anio){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findListadoTutoresCentroAnio(centro, anio);
		return (List<TutorCentro>) listaTutoresCentro;
	}	
	
	public List<TutorCentro> getTutoresByCentroAndAnioAndTipoPractica(final String centro, Integer anio, Long idTipoPractica){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findListadoTutoresCentroAnioAndTipoPractica(centro, anio, idTipoPractica);
		return (List<TutorCentro>) listaTutoresCentro;
	}
	
	public List<TutorCentro> getTutoresByCentroAndAnioAndTipoPracticaDeportiva(final String centro, Integer anio, Long idTipoPractica){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findListadoTutoresCentroAnioAndTipoPracticaDeportiva(centro, anio, idTipoPractica);
		return (List<TutorCentro>) listaTutoresCentro;
	}
	
	public List<TutorCentro> getProfesoresByCentroAndAnioLite(final String centro, Integer anio){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findListadoTutoresCentroAnioLite(centro, anio);
		return (List<TutorCentro>) listaTutoresCentro;
	}
	
	public List<TutorCentro> findTutorByUsuario(final String nif){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findTutorByUsuario(nif);
		return (List<TutorCentro>) listaTutoresCentro;
	}
	
	public List<TutorCentro> findTutorByUsuarioAndAnio(final String nif,final Integer anio){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findTutorByUsuarioAndAnio(nif,anio);
		return (List<TutorCentro>) listaTutoresCentro;
	}	
	
	
	public List<TutorCentro> findTutorByUsuarioAndAnioAndCentro(final String nif,final String centro, Integer anio){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findTutorByUsuarioAndAnioAndCentro(nif, centro, anio);
		return (List<TutorCentro>) listaTutoresCentro;
	}	
	
	public List<TutorCentro> findTutorByUsuarioAndAnioAndCentroAndTipoPractica(final String nif,final String centro, Integer anio, Integer idTipoPractica){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findTutorByUsuarioAndAnioAndCentroAndTipoPractica(nif, centro, anio, idTipoPractica);
		return (List<TutorCentro>) listaTutoresCentro;
	}	
	
	
	public List<TutorCentro> getProfesoresByCentroAndAnioAndOfertaCentro(final String centro, Integer anio, String ofercen){
		Iterable<TutorCentro> listaTutoresCentro = tutorCentroRepository.findListadoTutoresCentroAnioOfertaCentro(centro, anio, ofercen);
		return (List<TutorCentro>) listaTutoresCentro;
	}	
 

	public ResponseEntity<Profesor> updateProfesor(final Profesor profesorModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Profesor : {}", profesorModificado);
		if (profesorModificado.getIdProfesor() == null) {
			return createProfesor(profesorModificado);
		}
		 List<Profesor> profesorDuplicado  = this.profesorRepository.findAllByNif(profesorModificado.getNif());
	 	 
	 	if (profesorDuplicado.size()>0)
	 	 {
			if(profesorDuplicado.size()==1 && profesorDuplicado.get(0).getIdProfesor().equals(profesorModificado.getIdProfesor())) {
				Profesor result = profesorRepository.save(profesorModificado);	
				
				return ResponseEntity.ok().headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profesorModificado.getIdProfesor().toString()))
						.body(result);
			}
			else {
				throw new IllegalArgumentException(messageSource.getMessage("error.profesor.existe",
		                   null, LocaleContextHolder.getLocale()));	 	 
			}
	 	 }
	 	 
	 	 
		Profesor result = profesorRepository.save(profesorModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profesorModificado.getIdProfesor().toString()))
				.body(result);

	}

	public void deleteProfesor(final String idProfesor) {
		Optional<Profesor> profesor = profesorRepository.findById(idProfesor);
		if (!profesor.isPresent()) {
			throw new IllegalArgumentException("No existe una profesor con ese identificador.");
		}
	
	    List<VisitaTutor> listaVisitas = visitaTutorRepository.findAllByProfesor(profesor.get());
        if (listaVisitas.size()>0){
           // No se puede borrar porque está referencenciado en visitas
          throw new IllegalArgumentException(messageSource.getMessage("error.profesor.referenciado.visitas",
                   null, LocaleContextHolder.getLocale()));}
        
        List<Matricula> listaMatriculas = matriculaRepository.findAllByTutor(profesor.get());
        if (listaMatriculas.size()>0){
           // No se puede borrar porque está referencenciado en matricula activo
          throw new IllegalArgumentException(messageSource.getMessage("error.profesor.referenciado.matricula",
                   null, LocaleContextHolder.getLocale()));}
		

		profesorRepository.deleteById(idProfesor);
	}

	public Iterable<ProfesorCentro> getListaProfesorByCentroByTipoPractica (  Integer curso, String idCentro, Integer idTipoPractica){
		
		Iterable<ProfesorCentro> listaProfCen =  profesorCentroRepository.getListaProfesorByCentroByTipoPractica( curso, idCentro, idTipoPractica);		
		return  listaProfCen;		
		
	}
	
	public Iterable<ProfesorCentro> getListaProfesorByCentroByTipoPracticaByTutor (  Integer curso, String idCentro, Integer idTipoPractica, String tutor){
		
		Iterable<ProfesorCentro> listaProfCen =  profesorCentroRepository.getListaProfesorByCentroByTipoPracticaByTutor(curso, idCentro, idTipoPractica, tutor);		
		return  listaProfCen;		
		
	}
	
	public Page<Profesor> getAllProfesoresByCriteria(final Profesor partialMatch, Pageable pageable) {
		return profesorRepository.findAll(new Specification<Profesor>() {

			@Override
			public Predicate toPredicate(Root<Profesor> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();

				if (!StringUtils.isEmpty(partialMatch.getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("nombre")),
							"%" + partialMatch.getNombre().toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getNif())) {
					predicates.add(builder.like(builder.upper(root.get("nif")),
							"%" + partialMatch.getNif().toUpperCase() + "%"));
				}
			
				
				if (!StringUtils.isEmpty(partialMatch.getApellido1())) {
					predicates.add(builder.like(builder.upper(root.get("apellido1")),
							"%" + partialMatch.getApellido1().toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(partialMatch.getApellido2())) {
					predicates.add(builder.like(builder.upper(root.get("apellido2")),
							"%" + partialMatch.getApellido2().toUpperCase() + "%"));
				}
				
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		}, pageable);
	}

	public Iterable<Profesor> getProfesoresByCentroAndAnio(String centro, Integer anio) {	
		return profesorRepository.getProfesoresByCentroAndAnio(centro, anio);
	}
	
	public Iterable<Profesor> getProfesoresByCentro(String centro) {	
		return profesorRepository.getProfesoresByCentro(centro);
	}
}

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
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.repository.DistribucionPeriodoRepository;
import es.princast.gepep.repository.PeriodoPracticaRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PeriodoPracticaService {

	private static final String ENTITY_NAME = "periodoPractica";

	@Autowired
	private PeriodoPracticaRepository periodoPracticaRepository;

	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;	

	@Autowired
	private DistribucionPeriodoRepository distribucionPeriodoRepository;
	
	
	@Autowired
	private MessageSource messageSource;

	public PeriodoPractica getperiodoPractica(final Long idperiodoPractica) {
		Optional<PeriodoPractica> periodoPractica = periodoPracticaRepository.findById(idperiodoPractica);
		if (!periodoPractica.isPresent()) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.periodo.id.no.encontrado", null, LocaleContextHolder.getLocale()));
		}
		return periodoPractica.get();
	}

	public List<PeriodoPractica> getAllperiodoPracticas() {
		return periodoPracticaRepository.findAll();
	}

	public ResponseEntity<PeriodoPractica> createperiodoPractica(final PeriodoPractica nuevoperiodoPractica)
			throws URISyntaxException {
		log.debug("SERVICE request to save periodoPractica : {}", nuevoperiodoPractica);
		if (nuevoperiodoPractica.getIdPeriodo() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.periodo.nuevo.id", null, LocaleContextHolder.getLocale()));
		}
		
		List<PeriodoPractica> periodoRepetido = this.periodoPracticaRepository.findAllByTipoPracticaAndNombre(nuevoperiodoPractica.getTipoPractica(), nuevoperiodoPractica.getNombre());
		if(periodoRepetido.size()>0)
		{
			throw new IllegalArgumentException(messageSource.getMessage("error.periodo.existe",
	                   null, LocaleContextHolder.getLocale())); 
		
		}		
		
		Long idPractica = nuevoperiodoPractica.getTipoPractica().getIdTipoPractica();
		TipoPractica tipoPractica = tipoPracticaRepository.getOne(idPractica);
		nuevoperiodoPractica.setTipoPractica(tipoPractica);
		
		if (!ViableNuevoPeriodo(idPractica,nuevoperiodoPractica)) {
			throw new IllegalArgumentException(messageSource.getMessage("error.practica.nperiodos.completo", null,
					LocaleContextHolder.getLocale()));
		}		
	

	/*Revision fase 1 FCT 20180829 - quitar validacion de horas periodos vs Practica. 
	 * Se necesita que el sistema no realice esta comprobación (suma de la duración de los periodos por curso académico) y permita poner 380 H en el tipo de práctica.
	 * /
		
	 /* if (idPractica != null && !DisposicionDeHoras(idPractica, nuevoperiodoPractica.getCursoAcademico())
				&& nuevoperiodoPractica.getHoras() > 0) {
			throw new IllegalArgumentException(messageSource.getMessage("error.practica.horas.globales.completa", null,
					LocaleContextHolder.getLocale()));
		}

		if (idPractica != null && !EsViableHoraPeriodos(idPractica, nuevoperiodoPractica.getHoras(), 0,
				nuevoperiodoPractica.getCursoAcademico())) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.practica.maximo.horas", null, LocaleContextHolder.getLocale()));
		}*/
		
		

		PeriodoPractica result = periodoPracticaRepository.save(nuevoperiodoPractica);		
		return ResponseEntity.created(new URI("/api/periodos-practicas/" + result.getIdPeriodo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPeriodo().toString()))
				.body(result);
	}

	public ResponseEntity<PeriodoPractica> updateperiodoPractica(final PeriodoPractica periodoPracticaModificado)
			throws URISyntaxException {
		log.debug("SERVICE request to update periodoPractica : {}", periodoPracticaModificado);
		if (periodoPracticaModificado.getIdPeriodo() == null) {
			return createperiodoPractica(periodoPracticaModificado);
		}

		List<PeriodoPractica> periodoRepetido = this.periodoPracticaRepository.findAllByTipoPracticaAndNombre(periodoPracticaModificado.getTipoPractica(), periodoPracticaModificado.getNombre());
		
		
		 if (periodoRepetido.size()>0)
	 	 {
			if(periodoRepetido.size()==1 && periodoRepetido.get(0).getIdPeriodo() == periodoPracticaModificado.getIdPeriodo()) {
				PeriodoPractica result = periodoPracticaRepository.save(periodoPracticaModificado);
				return ResponseEntity.ok().headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodoPracticaModificado.getIdPeriodo().toString()))
						.body(result);
			}
			else {
				throw new IllegalArgumentException(messageSource.getMessage("error.periodo.existe",
		                   null, LocaleContextHolder.getLocale())); 	 
			}
	 	 }
	
		


		/*Revision fase 1 FCT 20180829 - quitar validacion de horas periodos vs Practica. 
		 * Se necesita que el sistema no realice esta comprobación (suma de la duración de los periodos por curso académico) y permita poner 380 H en el tipo de práctica.
		 * /
		/*if (idPractica != null && !EsViableHoraPeriodos(idPractica, periodoPracticaModificado.getHoras(),
				anterior.getHoras(), cursoAcademico)) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.practica.maximo.horas", null, LocaleContextHolder.getLocale()));
		}*/

		PeriodoPractica result = periodoPracticaRepository.save(periodoPracticaModificado);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodoPracticaModificado.getIdPeriodo().toString()))
				.body(result);
	}

	public void deleteperiodoPractica(final Long idperiodoPractica) {
		Optional<PeriodoPractica> periodoPractica = periodoPracticaRepository.findById(idperiodoPractica);
		if (!periodoPractica.isPresent()) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.periodo.id.no.encontrado", null, LocaleContextHolder.getLocale()));
		}
		
		//miro si hay horarios o algo con estos periodos.		
		
		if (periodoPractica.isPresent()) {			
			if (periodoPractica.get().getHorario()!= null)
			throw new IllegalArgumentException(messageSource.getMessage("error.periodo.referenciado.horario", null,
					LocaleContextHolder.getLocale()));
		}
		
		//miro si hay alumnado distribuido en el periodo.		

		Iterable<DistribucionPeriodo> listaPeriodos = distribucionPeriodoRepository.findAllByPeriodo(periodoPractica.get());
		if (listaPeriodos.iterator().hasNext()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.periodo.referenciado.distribucion", null,
					LocaleContextHolder.getLocale()));

		}
	 
		periodoPracticaRepository.deleteById(idperiodoPractica);
	}

	public Iterable<PeriodoPractica> getAllPeriodosPracticaByTipoPractica(final Long idTipoPractica) {
		log.debug("SERVICE request to get all Periodos by TipoPractica");
		Iterable<PeriodoPractica> listaPeriodos = this.periodoPracticaRepository
				.findAllByTipoPractica(this.tipoPracticaRepository.getOne(idTipoPractica));
		return listaPeriodos;
	}

	public Iterable<PeriodoPractica> getAllPeriodosPracticaByTipoPracticaAndCursoAcademico(final Long idTipoPractica,
			CursoAcademico cursoAcademico) {
		log.debug("SERVICE request to get all Periodos by TipoPractica and Curso Academico");
		Iterable<PeriodoPractica> listaPeriodos = this.periodoPracticaRepository.findAllByTipoPracticaAndCursoAcademico(
				this.tipoPracticaRepository.getOne(idTipoPractica), cursoAcademico);
		return listaPeriodos;
	}

	/**
	 * Valida que el número de periodos para el curso activo no supere los definidos
	 * en la practica.
	 * 
	 * @param idTipoPractica
	 * @return
	 */

	public boolean ViableNuevoPeriodo(final Long idTipoPractica, PeriodoPractica nuevoperiodoPractica) {
		
		CursoAcademico cursoAcademico = nuevoperiodoPractica.getCursoAcademico();

		List<PeriodoPractica> listaPeriodos = (List<PeriodoPractica>) this
				.getAllPeriodosPracticaByTipoPracticaAndCursoAcademico(idTipoPractica, cursoAcademico);
		
		if (listaPeriodos.size() >= nuevoperiodoPractica.getTipoPractica().getNPeriodos())
			return false;

		return true;
	}

	/**
	 * Obtiene el listado de periodos que pertenecen a una practica y curso
	 * academico , si está cubierto.
	 * 
	 * @param idTipoPractica
	 * @param cursoAcademico
	 * @return
	 */
	public boolean DisposicionDeHoras(final Long idTipoPractica, CursoAcademico cursoAcademico) {

		List<PeriodoPractica> listaPeriodos;

		if (cursoAcademico != null)
			listaPeriodos = (List<PeriodoPractica>) this
					.getAllPeriodosPracticaByTipoPracticaAndCursoAcademico(idTipoPractica, cursoAcademico);
		else
			listaPeriodos = (List<PeriodoPractica>) this.getAllPeriodosPracticaByTipoPractica(idTipoPractica);

		/*
		 * List<PeriodoPractica> listaPeriodos = (List<PeriodoPractica>)
		 * this.periodoPracticaRepository .findAllByTipoPractica(practica);
		 */

		if (listaPeriodos.isEmpty()) {
			return true;
		}
		Integer sum = 0;
		for (PeriodoPractica periodo : listaPeriodos) {
			sum += periodo.getHoras();
		}

		TipoPractica practica = this.tipoPracticaRepository.getOne(idTipoPractica);

		if (practica != null && practica.getNHoras() > sum)
			return true;
		else
			return false;

	}

	/**
	 * Funcion que valida que es viable añadir / editar periodo con el numero de
	 * horas que tenga al tipo practica que pertenece.
	 * 
	 * @param idTipoPractica
	 * @param horas
	 * @param horasPeriodoAntes
	 * @return
	 */
	public boolean EsViableHoraPeriodos(final Long idTipoPractica, Integer horas, Integer horasPeriodoAntes,
			CursoAcademico cursoAcademico) {
		TipoPractica practica = this.tipoPracticaRepository.getOne(idTipoPractica);

		if (GetHorasPeriodos(idTipoPractica, cursoAcademico) - horasPeriodoAntes + horas <= practica.getNHoras())
			return true;
		return false;

	}

	/**
	 * Obtiene el numero de horas de todos los periodos que pertenecen a una
	 * practica y curso academico, si está cubierto.
	 * 
	 * @param idTipoPractica
	 * @param cursoAcademico
	 * @return
	 */
	public Integer GetHorasPeriodos(final Long idTipoPractica, CursoAcademico cursoAcademico) {
		List<PeriodoPractica> listaPeriodos;
		if (String.valueOf(cursoAcademico) != "")
			listaPeriodos = (List<PeriodoPractica>) this
					.getAllPeriodosPracticaByTipoPracticaAndCursoAcademico(idTipoPractica, cursoAcademico);
		else
			listaPeriodos = (List<PeriodoPractica>) this.getAllPeriodosPracticaByTipoPractica(idTipoPractica);

		/*
		 * List<PeriodoPractica> listaPeriodos = (List<PeriodoPractica>) this
		 * .getAllPeriodosPracticaByTipoPractica(idTipoPractica);
		 */

		if (listaPeriodos.isEmpty()) {
			return -1;
		}

		Integer sum = 0;
		for (PeriodoPractica periodo : listaPeriodos) {
			sum += periodo.getHoras();
		}
		return sum;

	}

	public ResponseEntity<PeriodoPractica> createPeriodoPracticaInTipoPractica(final Long idTipoPractica,
			@Valid @RequestBody PeriodoPractica periodoPractica) throws URISyntaxException {
		log.debug("SERVICE request to save PeriodoPractica : {}", periodoPractica);
		if (periodoPractica.getIdPeriodo() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.periodo.nuevo.id", null, LocaleContextHolder.getLocale()));
		}

		CursoAcademico cursoAcademico = periodoPractica.getCursoAcademico();
		if (!DisposicionDeHoras(idTipoPractica, cursoAcademico)) {
			throw new IllegalArgumentException(messageSource.getMessage("error.practica.horas.globales.completa", null,
					LocaleContextHolder.getLocale()));

		}

		if (!EsViableHoraPeriodos(idTipoPractica, periodoPractica.getHoras(), 0, cursoAcademico)) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.practica.maximo.horas", null, LocaleContextHolder.getLocale()));
		}

		PeriodoPractica result = tipoPracticaRepository.findById(idTipoPractica).map(cn_tipo_practica -> {
			periodoPractica.setTipoPractica(cn_tipo_practica);
			return periodoPracticaRepository.save(periodoPractica);
		}).orElseThrow(() -> new IllegalArgumentException(messageSource
				.getMessage("error.tipopractica.id.no.encontrado", null, LocaleContextHolder.getLocale())));

		return ResponseEntity.created(new URI("/api/periodo-practicas/" + result.getIdPeriodo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPeriodo().toString()))
				.body(result);
	}

	public ResponseEntity<PeriodoPractica> updatePeriodoPracticaInTipoPractica(final Long idTipoPractica,
			final Long idPeriodo, final PeriodoPractica periodoPracticaModificado) throws URISyntaxException {
		log.debug("SERVICE request to update periodoPractica : {}", periodoPracticaModificado);
		if (periodoPracticaModificado.getIdPeriodo() == null) {
			return createPeriodoPracticaInTipoPractica(idTipoPractica, periodoPracticaModificado);
		}

		if (!tipoPracticaRepository.existsById(idTipoPractica)) {
			throw new IllegalArgumentException(messageSource.getMessage("error.tipopractica.id.no.encontrado", null,
					LocaleContextHolder.getLocale()));

		}

		PeriodoPractica result = periodoPracticaRepository.save(periodoPracticaModificado);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodoPracticaModificado.getIdPeriodo().toString()))
				.body(result);
	}

	public void deleteperiodoPracticaByTipoPractica(final Long IdTipoPractica, final Long idperiodoPractica) {

		if (!tipoPracticaRepository.existsById(IdTipoPractica)) {
			throw new IllegalArgumentException(messageSource.getMessage("error.tipopractica.id.no.encontrado", null,
					LocaleContextHolder.getLocale()));
		}

		periodoPracticaRepository.findById(idperiodoPractica).map(periodo -> {
			periodoPracticaRepository.delete(periodo);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new BadRequestAlertException(
				messageSource.getMessage("error.periodo.id.no.encontrado", null, LocaleContextHolder.getLocale()),
				ENTITY_NAME, "idnotexits"));
	}

	@SuppressWarnings("unchecked")
	public List<PeriodoPractica> getAllPeriodosByCriteria(final PeriodoPractica partialMatch) {
		return periodoPracticaRepository.findAll(new Specification<PeriodoPractica>() {
			@Override
			public Predicate toPredicate(Root<PeriodoPractica> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				if (!StringUtils.isEmpty(partialMatch.getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("nombre")),
							"%" + partialMatch.getNombre().toUpperCase() + "%"));
				}
				if (!StringUtils.isEmpty(partialMatch.getFechaInicio())) {
					predicates
							.add(builder.greaterThanOrEqualTo(root.get("fechaInicio"), partialMatch.getFechaInicio()));
				}
				if (!StringUtils.isEmpty(partialMatch.getFechaFin())) {
					predicates.add(builder.lessThanOrEqualTo(root.get("fechaFin"), partialMatch.getFechaFin()));
				}

				if (!StringUtils.isEmpty(partialMatch.getCursoAcademico())) {
					predicates.add(builder.equal(builder.upper(root.get("cursoAcademico")),
							"%" + partialMatch.getCursoAcademico().getIdAnio() + "%"));
				}
				// if (!StringUtils.isEmpty(partialMatch.getTipoPractica())) {
				// predicates.add(builder.equal(root.get("tipoPractica").get("idTipoPractica"),
				// partialMatch.getTipoPractica().getIdTipoPractica()));
				// }
				query.orderBy(builder.asc(builder.upper(root.get("nombre"))));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		});
	}
}

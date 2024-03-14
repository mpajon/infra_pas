package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Convenio;
import es.princast.gepep.domain.PeriodoLiquidacion;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.repository.ConvenioRepository;
import es.princast.gepep.repository.NormaReguladoraRepository;
import es.princast.gepep.repository.PeriodoLiquidacionRepository;
import es.princast.gepep.repository.PeriodoPracticaRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.repository.VisorRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TipoPracticaService {

	private static final String ENTITY_NAME = "tipoPractica";

	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PeriodoPracticaRepository periodoPracticaRepository;
	
	@Autowired
	private NormaReguladoraRepository normaReguladoraRepository;
	
	@Autowired
	private ConvenioRepository convenioRepository;
	
	@Autowired
	private PeriodoLiquidacionRepository periodoLiquidacionRepository;

	@Autowired
	private VisorRepository visorRepository;
	
	
	 @Autowired
	 private EntityManager entityManager; 
	
	public TipoPractica getTipoPractica(final Long idTipoPractica) {
		Optional<TipoPractica> tipopractica = tipoPracticaRepository.findById(idTipoPractica);
		if (!tipopractica.isPresent()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.tipopractica.id.no.encontrado", null,
					LocaleContextHolder.getLocale()));
		}
		return tipopractica.get();
	}


	public List<TipoPractica> getAllTiposPracticas() {
		return tipoPracticaRepository.findAllWithEagerRelationships();
		
	}
	

	/**
	 * Retorna los tipos de practica vigentes en el anio de cursoAcademico. Sin cargar relaciones jpa. Usado desde los combos de la vista.
	 * @param anio
	 * @return
	 */
	public List<TipoPractica> getTipoPracticaComboList(Integer anio) {
		
		String sql = " select cn_tipo_practica as idTipoPractica, dc_nombre as nombre, dl_descripcion as descripcion "
				+ " from tipo_practica "
				+ " where "
				+ " fe_finvigencia is null OR fe_finvigencia >= (select fe_final from curso_academico where nu_anio = :anio ) " 
				+ " order by dc_nombre asc ";

		return entityManager.createNativeQuery(sql, "TipoPracticaComboListMapping")
				.setParameter("anio", anio)
				.getResultList();
		
	}
	
	/**
	 * Modifico el Tipo de Practica. No dejo modificar las horas globales -->
	 * revisar con FP para determinar si con CdA lo hacemos.
	 * 
	 * @param tipoPracticaModificada
	 * @return
	 * @throws URISyntaxException
	 */
	public ResponseEntity<TipoPractica> updateTipoPractica(final TipoPractica tipoPracticaModificada)
			throws URISyntaxException {
		log.debug("SERVICE request to update TipoPractica : {}", tipoPracticaModificada);
		if (tipoPracticaModificada.getIdTipoPractica() == null) {
			return createTipoPractica(tipoPracticaModificada);
		}
		List<PeriodoPractica> listaPeriodos = (List<PeriodoPractica>) periodoPracticaRepository
				.findAllByTipoPractica(tipoPracticaModificada);

		/*Revision fase 1 FCT 20180829 - quitar validacion de horas periodos vs Practica. 
		 * Se necesita que el sistema no realice esta comprobación (suma de la duración de los periodos por curso académico) y permita poner 380 H en el tipo de práctica.
		 * Como consecuencia se elimina esta validación tambien.
		 * /
			
		/ * TipoPractica origen = tipoPracticaRepository.getOne(tipoPracticaModificada.getIdTipoPractica());	 
	
			if (origen.getNHoras() != tipoPracticaModificada.getNHoras() && listaPeriodos.size() > 0) {
				throw new IllegalArgumentException(messageSource.getMessage("error.practica.horas.no.modificar", null,
						LocaleContextHolder.getLocale()));
			}
		 */
		
		
		TipoPractica tipoPracticaExistente = tipoPracticaRepository.getOne(tipoPracticaModificada.getIdTipoPractica());	
		
		if (tipoPracticaExistente.getNPeriodos() != tipoPracticaModificada.getNPeriodos() && listaPeriodos.size() > tipoPracticaModificada.getNPeriodos()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.practica.numero.periodos", null,
					LocaleContextHolder.getLocale()));
		}
		LocalDate fecha = LocalDate.now(); 
		 List <TipoPractica> practicaDuplicada  = this.tipoPracticaRepository.findAllByNombreAndFechaFinVigencia(tipoPracticaModificada.getNombre(),fecha);
	 	 
		 if (practicaDuplicada.size()>0)
	 	 {
			if(practicaDuplicada.size()==1 && practicaDuplicada.get(0).getIdTipoPractica() == tipoPracticaModificada.getIdTipoPractica()) {
				
				if(tipoPracticaExistente.getNormasReguladoras()!=null) {
					tipoPracticaExistente.getNormasReguladoras().clear(); // JPA/Hibernate: limpiamos la coleccion existente para que actualice con las nuevas normas
				}
				
				TipoPractica result = tipoPracticaRepository.save(tipoPracticaModificada);

				return ResponseEntity.ok().headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipoPracticaModificada.getIdTipoPractica().toString()))
						.body(result);
			}
			else {
			throw new IllegalArgumentException(messageSource.getMessage("error.informacion.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
			}
	 	 }

	 	 
		TipoPractica result = tipoPracticaRepository.save(tipoPracticaModificada);

		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipoPracticaModificada.getIdTipoPractica().toString()))
				.body(result);

	}

	public void deleteTipoPractica(final Long idTipoPractica) {

		Optional<TipoPractica> TipoPractica = tipoPracticaRepository.findById(idTipoPractica);
		if (!TipoPractica.isPresent()) {

			throw new IllegalArgumentException(messageSource.getMessage("error.tipopractica.id.no.encontrado", null,
					LocaleContextHolder.getLocale()));
		}

		Iterable<PeriodoPractica> listaPeriodos = periodoPracticaRepository.findAllByTipoPractica(TipoPractica.get());
		if (listaPeriodos.iterator().hasNext()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.tipopractica.referenciado.periodo", null,
					LocaleContextHolder.getLocale()));

		}
		
		Iterable<PeriodoLiquidacion> periodosLiquidacion = periodoLiquidacionRepository.findAllByTipoPractica(TipoPractica.get());
		if (periodosLiquidacion.iterator().hasNext()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.tipopractica.referenciado.liquidacion", null,
					LocaleContextHolder.getLocale()));

		}
		
		Iterable<Convenio> convenios = convenioRepository.findAllByTipoPractica(TipoPractica.get());
		if (convenios.iterator().hasNext()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.tipopractica.referenciado.convenio", null,
					LocaleContextHolder.getLocale()));

		}

		tipoPracticaRepository.deleteById(idTipoPractica);
	}

	public ResponseEntity<TipoPractica> createTipoPractica(final TipoPractica nuevaTipoPractica)
			throws URISyntaxException {
		log.debug("SERVICE request to save TipoPractica : {}", nuevaTipoPractica);

		if (nuevaTipoPractica.getIdTipoPractica() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.tipopractica.nuevo.id", null, LocaleContextHolder.getLocale()));
		}

		
		LocalDate fecha = LocalDate.now(); 		
		 List <TipoPractica> practicaDuplicada  = this.tipoPracticaRepository.findAllByNombreAndFechaFinVigencia(nuevaTipoPractica.getNombre(),fecha);
	 	 if (practicaDuplicada.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.practica.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
		
		TipoPractica result = tipoPracticaRepository.save(nuevaTipoPractica);
		return ResponseEntity.created(new URI("/api/tipos-practica/" + result.getIdTipoPractica()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdTipoPractica().toString()))
				.body(result);
	}
	
	public Iterable<TipoPractica> getAllTipoPracticaByVisor(final Long idVisor) {
		log.debug("SERVICE request to get all TipoPracticas by Visor");
		Iterable<TipoPractica> listaPracticas = tipoPracticaRepository.findAllByVisor(visorRepository.getOne(idVisor));
		return listaPracticas;
	}
	

}

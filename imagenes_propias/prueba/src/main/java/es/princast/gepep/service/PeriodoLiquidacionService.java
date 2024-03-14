package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.BloqueoGastos;
import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.GastoAlumno;
import es.princast.gepep.domain.PeriodoLiquidacion;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.repository.BloqueoGastosRepository;
import es.princast.gepep.repository.CursoAcademicoRepository;
import es.princast.gepep.repository.GastoAlumnoRepository;
import es.princast.gepep.repository.PeriodoLiquidacionRepository;
import es.princast.gepep.repository.PeriodoPracticaRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PeriodoLiquidacionService {

	private static final String ENTITY_NAME = "periodoLiquidacion";

	@Autowired
	private PeriodoLiquidacionRepository periodoLiquidacionRepository;


	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;
	
	@Autowired
	private PeriodoPracticaRepository periodoPracticaRepository;
	
	@Autowired
	private GastoAlumnoRepository gastoAlumnoRepository;
	
	@Autowired
	private BloqueoGastosRepository bloqueoGastosRepository;
	
	@Autowired
	private CursoAcademicoRepository cursoAcademicoRepository;

	 
	@Autowired
	private MessageSource messageSource;

	public PeriodoLiquidacion getperiodoLiquidacion(final Integer idperiodoLiquidacion) {
		Optional<PeriodoLiquidacion> periodoLiquidacion = periodoLiquidacionRepository.findById(idperiodoLiquidacion);
		if (!periodoLiquidacion.isPresent()) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.periodo.id.no.encontrado", null, LocaleContextHolder.getLocale()));
		}
		return periodoLiquidacion.get();
	}

	public List<PeriodoLiquidacion> getAllperiodoLiquidacion() {
		return periodoLiquidacionRepository.findAll();
	}

	public ResponseEntity<PeriodoLiquidacion> createPeriodoLiquidacion(final PeriodoLiquidacion nuevoperiodoLiquidacion)
			throws URISyntaxException {
		log.debug("SERVICE request to save periodoLiquidacion : {}", nuevoperiodoLiquidacion);
		if (nuevoperiodoLiquidacion.getIdPeriodoLiquidacion() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.periodo.nuevo.id", null, LocaleContextHolder.getLocale()));
		}
		
		//validar que no se solapen periodos.
		List<PeriodoLiquidacion> listaPeriodosLiquidacion = (List<PeriodoLiquidacion>) this.getAllPeriodosLiquidacionByTipoPractica(nuevoperiodoLiquidacion.getTipoPractica().getIdTipoPractica());
		PeriodoLiquidacion ultimo= new PeriodoLiquidacion();
		ultimo = nuevoperiodoLiquidacion;
		ultimo.setIdPeriodoLiquidacion(Integer.valueOf(99));
		
		listaPeriodosLiquidacion.add(nuevoperiodoLiquidacion);
		if (listaPeriodosLiquidacion.size()>1) {
			if (PeriodosSolapados(listaPeriodosLiquidacion,nuevoperiodoLiquidacion)) {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.periodosLiquidacion.solapados", null, LocaleContextHolder.getLocale()));
			}
		}
		

		
		PeriodoLiquidacion result = periodoLiquidacionRepository.save(nuevoperiodoLiquidacion);
		return ResponseEntity.created(new URI("/api/periodos-liquidacion/" + result.getIdPeriodoLiquidacion()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPeriodoLiquidacion().toString()))
				.body(result);
	}
	
	public boolean PeriodosSolapados(List<PeriodoLiquidacion> listaPeriodos, PeriodoLiquidacion nuevoPeriodo) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
 
		for (int i=0;i<listaPeriodos.size();i++) {
			
			String fechaInicioStr1 = listaPeriodos.get(i).getDiaIni()+"/"+ listaPeriodos.get(i).getMesIni()+"/"+"2018";
			String fechaFinStr1 = listaPeriodos.get(i).getDiaFin()+"/"+ listaPeriodos.get(i).getMesFin()+"/"+"2018";
			DateTime fechaInicio1 = formatter.parseDateTime(fechaInicioStr1);
			DateTime fechaFin1 = formatter.parseDateTime(fechaFinStr1);		

			for (int j=0 ; j<listaPeriodos.size();j++) {				
				if (i!=j) {
				String fechaInicioStr2 = listaPeriodos.get(j).getDiaIni()+"/"+ listaPeriodos.get(j).getMesIni()+"/"+"2018";
				String fechaFinStr2 = listaPeriodos.get(j).getDiaFin()+"/"+ listaPeriodos.get(j).getMesFin()+"/"+"2018";
				
				DateTime fechaInicio2 = formatter.parseDateTime(fechaInicioStr2);
				DateTime fechaFin2 = formatter.parseDateTime(fechaFinStr2);

				Interval intervalo1 = new Interval( fechaInicio1, fechaFin1 );
				Interval intervalo2 = new Interval( fechaInicio2, fechaFin2 );

				if (intervalo1.overlaps(intervalo2))
					return true;
				}
			
			}	 
		}
		
		return false;
	}

	public ResponseEntity<PeriodoLiquidacion> updatePeriodoLiquidacion(final PeriodoLiquidacion periodoLiquidacionModificado)
			throws URISyntaxException {
		log.debug("SERVICE request to update periodoLiquidacion : {}", periodoLiquidacionModificado);
		if (periodoLiquidacionModificado.getIdPeriodoLiquidacion() == null) {
			return createPeriodoLiquidacion(periodoLiquidacionModificado);
		}		

		PeriodoLiquidacion result = periodoLiquidacionRepository.save(periodoLiquidacionModificado);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodoLiquidacionModificado.getIdPeriodoLiquidacion().toString()))
				.body(result);
	}

	public void deletePeriodoLiquidacion(final Integer idperiodoLiquidacion) {
		Optional<PeriodoLiquidacion> periodoLiquidacion = periodoLiquidacionRepository.findById(idperiodoLiquidacion);
		if (!periodoLiquidacion.isPresent()) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.periodo.id.no.encontrado", null, LocaleContextHolder.getLocale()));
		}
		
		Iterable <GastoAlumno> listaGastos = this.gastoAlumnoRepository.findAllByPeriodoLiquidacion(periodoLiquidacion.get());	    
		    if (listaGastos.iterator().hasNext()){ 
		    	throw new IllegalArgumentException(messageSource.getMessage("error.periodoLiquidacion.referenciado.gastoAlumno",
		                   null, LocaleContextHolder.getLocale()));	
		    	}
		
		periodoLiquidacionRepository.deleteById(idperiodoLiquidacion);
	}

	public Iterable<PeriodoLiquidacion> getAllPeriodosLiquidacionByTipoPractica(final Long idTipoPractica) {
		log.debug("SERVICE request to get all Periodos by TipoLiquidacion");
	
		Iterable<PeriodoLiquidacion> listaPeriodos = this.periodoLiquidacionRepository
				.findAllByTipoPractica(this.tipoPracticaRepository.getOne(idTipoPractica));
		/*if(!listaPeriodos.iterator().hasNext())
		{
			throw new IllegalArgumentException(messageSource.getMessage("error.periodoLiquidacion.tipoPractica.noExiste",
	                   null, LocaleContextHolder.getLocale()));	
		}*/
		return listaPeriodos;
	}
	
	/**
	 * Obtiene el periodo de liquidacion correspondiente al tipo de practica/periodoPractica recibido.
	 * Si la fecha de fin del periodo practica está entre inicio/fin de las fechas de liquidacion lo escogemos
	 * @param idTipoPractica
	 * @param idPeriodoPractica
	 * @return
	 */
	public PeriodoLiquidacion getPeriodoLiquidacionByPeriodoPractica(final Long idTipoPractica, final Long idPeriodoPractica, final Integer anio) {
		log.debug("SERVICE request to getPeriodoLiquidacionByPeriodoPractica");
	
		PeriodoPractica periodoPrac = this.periodoPracticaRepository.getOne(idPeriodoPractica);
		Integer anioPeriodoPrac = periodoPrac.getFechaFin().getYear();
		
		
		// periodos de liquidacion 
		Iterable<PeriodoLiquidacion> listaPeriodosLiquidacion = this.periodoLiquidacionRepository
				.findAllByTipoPractica(this.tipoPracticaRepository.getOne(idTipoPractica));
		
		for (PeriodoLiquidacion periodoLiquidacion : listaPeriodosLiquidacion) {
			
			// componemos fechas en el mismo año //
			LocalDate dateInicioLiq = LocalDate.of(anioPeriodoPrac, periodoLiquidacion.getMesIni(), periodoLiquidacion.getDiaIni());
			LocalDate dateFinLiq = LocalDate.of(anioPeriodoPrac, periodoLiquidacion.getMesFin(), periodoLiquidacion.getDiaFin());
			
			// si la fecha de fin del periodo practica está entre inicio/fin de las fechas de liquidacion lo escogemos //
			if((periodoPrac.getFechaFin().isAfter(dateInicioLiq) ||  periodoPrac.getFechaFin().equals(dateInicioLiq))
					&& (periodoPrac.getFechaFin().isBefore(dateFinLiq) ||  periodoPrac.getFechaFin().equals(dateFinLiq))) {
				
				Optional<CursoAcademico> cursoAca =  cursoAcademicoRepository.findById(anio);
								
				if (cursoAca.isPresent()) {
					Optional<BloqueoGastos> bloqueo = bloqueoGastosRepository.findOneByPeriodoAndCursoAcademico(periodoLiquidacion,cursoAca.get());
					if (bloqueo.isPresent()) {
						periodoLiquidacion.setBloqueado(bloqueo.get().getBloqueado());						
					} else {
						periodoLiquidacion.setBloqueado(Boolean.FALSE);
					}					
						
				}
								
				return periodoLiquidacion;
				
			}
		}
		
		throw new IllegalArgumentException(
				messageSource.getMessage("error.periodosLiquidacion.periodo.noEncontrado", null, LocaleContextHolder.getLocale()));
		
	}
 

}

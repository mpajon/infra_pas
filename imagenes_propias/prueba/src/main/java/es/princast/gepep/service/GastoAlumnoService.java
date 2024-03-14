package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.codahale.metrics.annotation.Timed;
import es.princast.gepep.domain.*;
import es.princast.gepep.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@Transactional
@Slf4j
public class  GastoAlumnoService {

	private static final String ENTITY_NAME = "gastoGastoAlumno";

	@Autowired
	private GastoAlumnoRepository gastoGastoAlumnoRepository;
	
	
	@Autowired
    private MatriculaRepository matriculaRepository;
	
	
	@Autowired
    private PeriodoLiquidacionRepository liquidacionRepository;
	
	
	@Autowired
	private GastoAgrupadoRepository gastoAgrupadoRepository;
	@Autowired
	private ImportesTipoGasto2Service importesTipoGasto2Service;

	@Autowired
	private DistribucionPeriodoRepository distribucionPeriodoRepository;

	@Autowired
	private GastoAlumnoRepository gastoAlumnoRepository;


	public ResponseEntity<GastoAlumno> createGastoAlumno(final GastoAlumno nuevoGastoAlumno) throws URISyntaxException {
		log.debug("SERVICE request to save GastoAlumno : {}", nuevoGastoAlumno);

		if (nuevoGastoAlumno.getIdGastoAlumno() != null) {
			throw new BadRequestAlertException("A new gastoGastoAlumno cannot already have an ID", ENTITY_NAME, "idexists");
		}
		if(nuevoGastoAlumno.getFlValidado() && nuevoGastoAlumno.getFValidacion() == null){
			nuevoGastoAlumno.setFValidacion(LocalDate.now());
		}
		GastoAlumno result = gastoGastoAlumnoRepository.save(nuevoGastoAlumno);
		if (result.getdistanciaTotal2() != null) {
			result.setGastoTotal2(importesTipoGasto2Service.getImporteByKilometros(result.getdistanciaTotal2()));
		}
		return ResponseEntity.created(new URI("/api/gasto-alumnos/" + result.getIdGastoAlumno()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdGastoAlumno().toString()))
				.body(result);
	}

	public GastoAlumno getGastoAlumno(final Integer idGastoAlumno) {
		Optional<GastoAlumno> gastoGastoAlumno = gastoGastoAlumnoRepository.findById(idGastoAlumno);

		if (!gastoGastoAlumno.isPresent()) {
			throw new IllegalArgumentException("No existe un gastoGastoAlumno con ese identificador.");
		}

		GastoAlumno gasto  = gastoGastoAlumno.get();
		gasto.setGastoTotal2(importesTipoGasto2Service.getImporteByKilometros(gasto.getdistanciaTotal2()));
		return gasto;
	}

	public List<GastoAlumno> getAllGastoAlumnos() {
		return gastoGastoAlumnoRepository.findAll();
	}

	
	public  List<GastoAlumno> getAllByMatricula(final String idMatricula) {
		log.debug("SERVICE request to get all Gastos de Alumnado by Matricula");	
		Matricula matricula = matriculaRepository.getOne(idMatricula);
		Iterable <GastoAlumno> listaGastoAlumno = gastoGastoAlumnoRepository.findAllByMatricula(matricula);		 
		return (List<GastoAlumno>) listaGastoAlumno;
        
	}
	
	
	public  List<GastoAlumno> getAllByMatriculaAndPeriodoLiquidacion(final String idMatricula, final Integer idPeriodoLiquidacion) {
		log.debug("SERVICE request to get all Gastos de Alumnado by Matricula and Liquidacion");	
		Matricula matricula = matriculaRepository.getOne(idMatricula);
		PeriodoLiquidacion liquidacion = liquidacionRepository.getOne(idPeriodoLiquidacion);		
		Iterable <GastoAlumno> listaGastoAlumno = gastoGastoAlumnoRepository.findAllByMatriculaAndPeriodoLiquidacion(matricula,liquidacion);		 
		return (List<GastoAlumno>) listaGastoAlumno;
        
	}
	
	
	
	public  List<GastoAlumno> getAllByPeriodoLiquidacion(final Integer idPeriodoLiquidacion) {
		log.debug("SERVICE request to get all Gastos de Alumnado by liquidacion");	
		PeriodoLiquidacion liquidacion = liquidacionRepository.getOne(idPeriodoLiquidacion);		
		Iterable<GastoAlumno> gastoAlumnado = this.gastoGastoAlumnoRepository.findAllByPeriodoLiquidacion(liquidacion);		
		return (List<GastoAlumno>) gastoAlumnado;
        
	}
	

	public ResponseEntity<GastoAlumno> updateGastoAlumno(final GastoAlumno gastoGastoAlumnoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update GastoAlumno : {}", gastoGastoAlumnoModificado);
		if (gastoGastoAlumnoModificado.getIdGastoAlumno() == null) {
			return createGastoAlumno(gastoGastoAlumnoModificado);
		}
		GastoAlumno result = gastoGastoAlumnoRepository.save(gastoGastoAlumnoModificado);
		result.setGastoTotal2(importesTipoGasto2Service.getImporteByKilometros(result.getdistanciaTotal2()));
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gastoGastoAlumnoModificado.getIdGastoAlumno().toString()))
				.body(result);

	}

	public void deleteGastoAlumno(final Integer idGastoAlumno) {
		Optional<GastoAlumno> gastoGastoAlumno = gastoGastoAlumnoRepository.findById(idGastoAlumno);
		if (!gastoGastoAlumno.isPresent()) {
			throw new IllegalArgumentException("No existe un gastoGastoAlumno con ese identificador.");
		}
	 

		gastoGastoAlumnoRepository.deleteById(idGastoAlumno);
	}

	public List<GastoAlumno> getGastoAlumnoCurso( final String idCentro,final String idCiclo,final Integer periodo,
												  final Integer curso,String regimen, String grupo, final String anioAcademico){
		Iterable <GastoAlumno> listaGastoAlumno = gastoGastoAlumnoRepository.getGastoAlumnoCurso(idCentro,idCiclo,periodo,curso,regimen,grupo,anioAcademico);
		return (List<GastoAlumno>) listaGastoAlumno;

	}

	//CRQ000000561434
	public List<GastoAlumno> getGastoAlumnoCursoNuevo( final String idCentro,final String idCiclo,final Integer periodo,
			  final Integer curso,String regimen, String grupo, final Integer anioAcademico, final Integer tipo){
		List <GastoAlumno> listaGastoAlumno = new ArrayList<GastoAlumno>();
		Iterable<GastoAgrupado> listaGastosAgrupados;
		if (tipo == 1) {
			listaGastosAgrupados = gastoAgrupadoRepository.findGastoAlumnoCursoNuevo(idCentro, idCiclo, periodo, curso, regimen, grupo, anioAcademico);
		} else
		{
			listaGastosAgrupados = gastoAgrupadoRepository.findGastoAlumnoCursoNuevoTipo2(idCentro, idCiclo, periodo, curso, regimen, grupo, anioAcademico);
		}

		for (GastoAgrupado gasto: listaGastosAgrupados){
            GastoAlumno ga = new GastoAlumno();
            ga.setIdGastoAlumno(gasto.getIdGastoAlumno());
            ga.setNumBillete(gasto.getNumBillete());
            ga.setNumDieta(gasto.getNumDieta());
            ga.setNumKm(gasto.getNumKm());
            ga.setNumPension(gasto.getNumPension());
            ga.setOtrosGastos(gasto.getOtrosGastos());
            ga.setPrecioBillete(gasto.getPrecioBillete());
            log.debug(" ---------- precio" + gasto.getPrecioKm());
            ga.setPrecioKm(gasto.getPrecioKm());
            ga.setPrecioDieta(gasto.getPrecioDieta());
            ga.setPrecioPension(gasto.getPrecioPension());
            PeriodoLiquidacion pl = new PeriodoLiquidacion();
            pl.setIdPeriodoLiquidacion(gasto.getIdPeriodoLiquidacion());
            ga.setPeriodoLiquidacion(pl);
            ga.setDistanciaUnitaria(gasto.getDistanciaUnitaria());
            ga.setNumeroDias(gasto.getNumDias());
			DistribucionPeriodo  dp = new DistribucionPeriodo();
			dp.setIdDistribucionPeriodo(gasto.getIdDistribucionPeriodo());
			ga.setDistribucionPeriodo(dp);
            ga.setLocalidadCentroTrabajo(gasto.getLocalidadCentroTrabajo());
            Optional<Matricula> mat = matriculaRepository.findById(gasto.getIdMatricula());          
            ga.setMatricula(mat.get());
            listaGastoAlumno.add(ga);            
            
		}
		return (List<GastoAlumno>) listaGastoAlumno;

	}

	
	public List<GastoAlumno> getGastosAlumnosByCursoAndCentroAndTutor( final String idCentro,
			  final Integer curso, final String tutor){
	List <GastoAlumno> listaGastoAlumno = gastoGastoAlumnoRepository.getGastosAlumnosByCursoAndCentroAndTutor(idCentro,curso,tutor);
	for (GastoAlumno gasto: listaGastoAlumno) {
		if (gasto.getdistanciaTotal2()!= null  && gasto.getNumeroDias() != null && gasto.getNumeroDias()>0){
			gasto.setGastoTotal2(importesTipoGasto2Service.getImporteByKilometros(gasto.getdistanciaTotal2()));
		}
	}
	
	// Ordenamos gastos por centro, tipoPractica (no necesario, viene ordenado del repository)
	// Comparator<GastoAlumno> comparator = Comparator.comparing(g -> (g.getMatricula()!=null && g.getMatricula().getUnidad()!=null && g.getMatricula().getUnidad().getOfertaCentro()!=null && g.getMatricula().getUnidad().getOfertaCentro().getCentro()!=null) ? g.getMatricula().getUnidad().getOfertaCentro().getCentro().getNombre() :"");
	// comparator = comparator.thenComparing(Comparator.comparing(g -> (g.getPeriodoLiquidacion()!=null && g.getPeriodoLiquidacion().getTipoPractica()!=null)? g.getPeriodoLiquidacion().getTipoPractica().getNombre(): ""));
	// listaGastoAlumno = listaGastoAlumno.stream().sorted(comparator).collect(Collectors.toList());
	
	return listaGastoAlumno;
}

	public List<GastoAlumno> getGastosPeriodo( final Long periodo){
		List <GastoAlumno> listaGastosPeriodo = gastoGastoAlumnoRepository.getGastosPeriodo(periodo);
		return listaGastosPeriodo;

	}

	public void updateValidarTodosGastos(final String idOfertaCentro,final Integer anio,
										 final Long idPeriodo, boolean validar) throws URISyntaxException {
		log.debug("SERVICE request to updateGastosValidarTodos : {}");
		List<Distribucion> listaDistribuciones = new ArrayList<Distribucion>();
		List<DistribucionPeriodo> consultaDistribucionesPeriodo = distribucionPeriodoRepository
				.getDistribucionesPeriodoByOfertaCentroAndAnioAndPeriodo(idOfertaCentro, anio,idPeriodo);
		for (DistribucionPeriodo distribucionPeriodo : consultaDistribucionesPeriodo) {
			// Recuperamos los gastos //
			List<GastoAlumno> gastosAlumno = gastoAlumnoRepository.findByDistribucionPeriodo(distribucionPeriodo);
			for (GastoAlumno ga : gastosAlumno) {
				if (validar)
					ga.setFValidacion(LocalDate.now());
				else
					ga.setFValidacion(null);
				gastoAlumnoRepository.updateValidaGasto(ga.getIdGastoAlumno().intValue(), validar,ga.getFValidacion());
			}
		}
	}

	public void updateValidarGasto(final GastoAlumno gasto) throws URISyntaxException {
		log.debug("SERVICE request to updateValidarGasto : {}", gasto);
		if (gasto.getFlValidado())
			gasto.setFValidacion(LocalDate.now());
		else
			gasto.setFValidacion(null);
		gastoAlumnoRepository.updateValidaGasto(gasto.getIdGastoAlumno(),gasto.getFlValidado(),gasto.getFValidacion());

	}


 
}

package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.BloqueoGastos;
import es.princast.gepep.domain.ControlBloqueoGastos;
import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.PeriodoLiquidacion;
import es.princast.gepep.repository.BloqueoGastosRepository;
import es.princast.gepep.repository.ControlBloqueoGastosRepository;
import es.princast.gepep.repository.CursoAcademicoRepository;
import es.princast.gepep.repository.PeriodoLiquidacionRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BloqueoGastosService {

	private static final String ENTITY_NAME = "bloqueo_gastos";

	@Autowired
	private BloqueoGastosRepository bloqueoGastosRepository;
	
	@Autowired
	private PeriodoLiquidacionRepository periodoLiquidacionRepository;
	
	@Autowired
	private CursoAcademicoRepository cursoAcademicoRepository;
		
	@Autowired
	private ControlBloqueoGastosRepository controlBloqueoGastosRepository;

	public ResponseEntity<BloqueoGastos> createBloqueoGastos(final BloqueoGastos nuevoBloqueo) throws URISyntaxException {
		log.debug("SERVICE request to save Bloqueo Gastos : {}", nuevoBloqueo);

		if (nuevoBloqueo.getIdBloqueoGastos()!= null) {
			throw new BadRequestAlertException("A new bloqueo cannot already have an ID", ENTITY_NAME, "idexists");
		}
		BloqueoGastos result = bloqueoGastosRepository.save(nuevoBloqueo);
		return ResponseEntity.created(new URI("/api/bloqueos/" + result.getIdBloqueoGastos()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdBloqueoGastos().toString()))
				.body(result);
	}

	public BloqueoGastos getBloqueoGastos(final Long idBloqueoGastos) {
		Optional<BloqueoGastos> bloqueoGastos = bloqueoGastosRepository.findById(idBloqueoGastos);
		if (!bloqueoGastos.isPresent()) {
			throw new IllegalArgumentException("No existe un bloqueoGastos con ese identificador.");
		}
		return bloqueoGastos.get();
	}

	public List<BloqueoGastos> getAllBloqueoGastos() {
		String [] propiedades = {"unidad.nombre"};
    	Sort sort = Sort.by(Sort.Direction.ASC, propiedades);
		return bloqueoGastosRepository.findAll(sort);
	}

	public ResponseEntity<BloqueoGastos> updateBloqueoGastos(final BloqueoGastos bloqueoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update BloqueoGastos : {}", bloqueoModificado);
		if (bloqueoModificado.getIdBloqueoGastos() == null) {
			return createBloqueoGastos(bloqueoModificado);
		}
		BloqueoGastos result = bloqueoGastosRepository.save(bloqueoModificado);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bloqueoModificado.getIdBloqueoGastos().toString()))
				.body(result);

	}

	public void deleteBloqueoGastos(final Long idBloqueoGastos) {
		Optional<BloqueoGastos> actividad = bloqueoGastosRepository.findById(idBloqueoGastos);
		if (!actividad.isPresent()) {
			throw new IllegalArgumentException("No existe una bloqueoGastos con ese identificador.");
		}
		bloqueoGastosRepository.deleteById(idBloqueoGastos);
	}
	
	
	public Iterable<BloqueoGastos> getBloqueoGastosByPeriodoLiquidacion(final Integer idPeriodoLiquidacion) {
		
		Iterable<BloqueoGastos> listaBloqueoGastos =  bloqueoGastosRepository.findAllByPeriodo(periodoLiquidacionRepository.getOne(idPeriodoLiquidacion));
		return listaBloqueoGastos;
	}
	
	public List<ControlBloqueoGastos> getPeriodosResumen(){
		
		return (List<ControlBloqueoGastos>) controlBloqueoGastosRepository.getPeriodosResumen();
	}

	public List<ControlBloqueoGastos> getPeriodosResumenByAnio(Integer anio) {

		return (List<ControlBloqueoGastos>) controlBloqueoGastosRepository.getPeriodosResumenByAnio(anio);
	}

	public List<ControlBloqueoGastos> getPeriodosResumenByAnioAndTipoPractica(Integer idTipoPrac, Integer anio) {

		return (List<ControlBloqueoGastos>) controlBloqueoGastosRepository
				.getPeriodosResumenByAnioAndTipoPractica(idTipoPrac, anio);
	}

	public List<ControlBloqueoGastos> getPeriodosResumenByAnioAndTipoPracticaAndPeriodoLiquidacion(Integer idPerLiq,
			Integer idTipoPrac, Integer anio) {

		return (List<ControlBloqueoGastos>) controlBloqueoGastosRepository
				.getPeriodosResumenByAnioAndTipoPracticaAndPeriodoLiquidacion(idPerLiq, idTipoPrac, anio);
	}
	
	public  ResponseEntity<BloqueoGastos> bloquearGastos(final ControlBloqueoGastos bloqueoGastos) throws URISyntaxException {
		log.debug("SERVICE request to update BloqueoGastos : {}", bloqueoGastos);
		
		Optional<CursoAcademico> cursoAca = cursoAcademicoRepository.findById(bloqueoGastos.getAnio());
		Optional<PeriodoLiquidacion> periodo = periodoLiquidacionRepository.findById(bloqueoGastos.getIdPeriodo());
		if (!periodo.isPresent() || !cursoAca.isPresent()) {
			throw new IllegalArgumentException("Error al bloquear los gastos.");
		}
		Optional<BloqueoGastos> bloqueoOp = bloqueoGastosRepository.findOneByPeriodoAndCursoAcademico(periodo.get(), cursoAca.get());		
		
		BloqueoGastos bloqueo = new BloqueoGastos();
		
		
		if (!bloqueoOp.isPresent()) {	
			bloqueo.setCursoAcademico(cursoAca.get());
			bloqueo.setPeriodo(periodo.get());
		} else {
			bloqueo = bloqueoOp.get();
		}
		bloqueo.setBloqueado(bloqueoGastos.getBloqueado());
		BloqueoGastos result = bloqueoGastosRepository.save(bloqueo);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "bloquearGastos"))
				.body(result);
		
	}

}

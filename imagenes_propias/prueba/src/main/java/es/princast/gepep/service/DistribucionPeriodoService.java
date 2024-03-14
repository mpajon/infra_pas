package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.domain.PeriodoPractica;
import es.princast.gepep.repository.DistribucionPeriodoRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DistribucionPeriodoService {

	private static final String ENTITY_NAME = "distribucionPeriodo";

	@Autowired
	private DistribucionPeriodoRepository distribucionPeriodoRepository;


	public ResponseEntity<DistribucionPeriodo> createDistribucionPeriodo(final DistribucionPeriodo nuevaDistribucionPeriodo) throws URISyntaxException {
		log.debug("SERVICE request to save DistribucionPeriodo : {}", nuevaDistribucionPeriodo);

		if (nuevaDistribucionPeriodo.getIdDistribucionPeriodo() != null) {
			throw new BadRequestAlertException("A new distribucionPeriodo cannot already have an ID", ENTITY_NAME, "idexists");
		}

		DistribucionPeriodo result = distribucionPeriodoRepository.save(nuevaDistribucionPeriodo);
		return ResponseEntity.created(new URI("/api/distribucion-periodos/" + result.getIdDistribucionPeriodo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdDistribucionPeriodo().toString()))
				.body(result);
	}

	public DistribucionPeriodo getDistribucionPeriodo(final Long idDistribucionPeriodo) {
		Optional<DistribucionPeriodo> distribucionPeriodo = distribucionPeriodoRepository.findById(idDistribucionPeriodo);
		if (!distribucionPeriodo.isPresent()) {
			throw new IllegalArgumentException("No existe una distribucionPeriodo con ese identificador.");
		}
		return distribucionPeriodo.get();
	}

	public  List<DistribucionPeriodo>  getAllDistribucionPeriodos() {
		 log.debug("SERVICE request to get all DistribucionPeriodo");
		 return distribucionPeriodoRepository.findAll();
	}
	
	public Iterable<DistribucionPeriodo> getAllByDistribucion(final Long idDistribucion) {
		log.debug("SERVICE request to get all Distribución by Convenio");
		Distribucion distribucion = new Distribucion();
		distribucion.setIdDistribucion(idDistribucion);
		Iterable<DistribucionPeriodo> listaPeriodos = this.distribucionPeriodoRepository.findAllByDistribucion(distribucion);
		return listaPeriodos;
	}
	
	public Iterable<DistribucionPeriodo> getAllByDistribucionAndPeriodo(final Long idDistribucion, final Long idPeriodoPractica) {
		log.debug("SERVICE request to get all Distribución by Convenio");
		Distribucion distribucion = new Distribucion();
		distribucion.setIdDistribucion(idDistribucion);
		PeriodoPractica periodo = new PeriodoPractica();
		periodo.setIdPeriodo(idPeriodoPractica);
		Iterable<DistribucionPeriodo> listaPeriodos = this.distribucionPeriodoRepository.findAllByDistribucionAndPeriodo(distribucion,periodo);
		return listaPeriodos;
	}
	
	
	public Integer getHorasEnDistribucion(final String idMatricula, final  Long idDistribucion , final Long idTipoPractica )
	{
		 log.debug("SERVICE request to get horas ");
		 return distribucionPeriodoRepository.getHorasDistribuidasByMatriculaDistribucionAndTipoPractica(idMatricula, idDistribucion, idTipoPractica);
	}
	
	public Iterable<DistribucionPeriodo> getAllByDistribucionAndTipoPractica(final Long idDistribucion, final Long idTipoPractica) {
		log.debug("SERVICE request to get all Distribución by Convenio");
		Distribucion distribucion = new Distribucion();
		distribucion.setIdDistribucion(idDistribucion);
		Iterable<DistribucionPeriodo> listaPeriodos = this.distribucionPeriodoRepository.findAllByDistribucion(distribucion);
		return listaPeriodos;
	}

	public ResponseEntity<DistribucionPeriodo> updateDistribucionPeriodo(final DistribucionPeriodo distribucionPeriodoModificada) throws URISyntaxException {
		log.debug("SERVICE request to update DistribucionPeriodo : {}", distribucionPeriodoModificada);
		if (distribucionPeriodoModificada.getIdDistribucionPeriodo() == null) {
			return createDistribucionPeriodo(distribucionPeriodoModificada);
		}

		DistribucionPeriodo result = distribucionPeriodoRepository.save(distribucionPeriodoModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, distribucionPeriodoModificada.getIdDistribucionPeriodo().toString()))
				.body(result);

	}

	public void deleteDistribucionPeriodo(final Long idDistribucionPeriodo) {
		Optional<DistribucionPeriodo> distribucionPeriodo = distribucionPeriodoRepository.findById(idDistribucionPeriodo);
		if (!distribucionPeriodo.isPresent()) {
			throw new IllegalArgumentException("No existe una distribucionPeriodo con ese identificador.");
		}
		
		distribucionPeriodoRepository.deleteById(idDistribucionPeriodo);
	}

}

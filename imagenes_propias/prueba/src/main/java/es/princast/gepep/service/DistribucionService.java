package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import es.princast.gepep.domain.dto.DistribucionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.AlumDisAnexoiii;
import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.ContenidoEF;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.domain.GastoAlumno;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.repository.AlumDisAnexoiiiRepositoy;
import es.princast.gepep.repository.AnexoContratoRepository;
import es.princast.gepep.repository.DistribucionPeriodoRepository;
import es.princast.gepep.repository.DistribucionRepository;
import es.princast.gepep.repository.GastoAlumnoRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DistribucionService {

	private static final String UNDEFINED = "undefined";

	private static final String ENTITY_NAME = "distribucion";

	@Autowired
	private DistribucionRepository distribucionRepository;

	@Autowired
	private AnexoContratoRepository anexoRepository;

	@Autowired
	private AlumDisAnexoiiiRepositoy alumDisAnexoiiiRepositoy;

	@Autowired
	private DistribucionPeriodoRepository distribucionPeriodoRepository;

	@Autowired
	private GastoAlumnoRepository gastoAlumnoRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ContenidoEFService contenidoEFService;

	@Autowired
	private ImportesTipoGasto2Service importesTipoGasto2Service;

	public ResponseEntity<Distribucion> createDistribucion(final Distribucion nuevaDistribucion)
			throws URISyntaxException {
		log.debug("SERVICE request to save Distribucion : {}", nuevaDistribucion);

		if (nuevaDistribucion.getIdDistribucion() != null) {
			throw new BadRequestAlertException("A new distribucion cannot already have an ID", ENTITY_NAME, "idexists");
		}

		Distribucion result = distribucionRepository.save(nuevaDistribucion);

		// Guardamos los contenidos de elementos formativos //

		if (!nuevaDistribucion.getContenidosEF().isEmpty()) {
			for (ContenidoEF contenidoEF : nuevaDistribucion.getContenidosEF()) {
				contenidoEF.setDistribucion(result);
				contenidoEFService.updateContenidoEF(contenidoEF);
			}
		}

		return ResponseEntity.created(new URI("/api/distribuciones/" + result.getIdDistribucion()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdDistribucion().toString()))
				.body(result);
	}

	public Distribucion getDistribucion(final Long idDistribucion) {
		Optional<Distribucion> distribucion = distribucionRepository.findById(idDistribucion);
		if (!distribucion.isPresent()) {
			throw new IllegalArgumentException("No existe una distribucion con ese identificador.");
		}
		return distribucion.get();
	}

	public Iterable<Distribucion> getAllByAnexos(final Long idAnexo) {
		log.debug("SERVICE request to get all Anexos by Convenio");
		Iterable<Distribucion> listaDistribucion = this.distribucionRepository
				.findAllByAnexoContrato(this.anexoRepository.getOne(idAnexo));
		return listaDistribucion;
	}

	public Iterable<Distribucion> getAllByMatricula(final String idMatricula) {
		log.debug("SERVICE request to get all Anexos by Convenio");
		Matricula matricula = new Matricula();
		matricula.setIdMatricula(idMatricula);
		Iterable<Distribucion> listaDistribucion = this.distribucionRepository.findAllByMatricula(matricula);
		return listaDistribucion;
	}

	/**
	 * Este metodo recupera lo datos de las distribuciones para una
	 * oferta/unidad/año/periodoPractica incluye los gastos asociados a la
	 * distribucion periodo
	 *
	 * @param idOferCen
	 * @param idUnidad
	 * @param anio
	 * @param idPeriodoPractica
	 * @return
	 */
	public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidasByPeriodo(final String idOferCen,
																						String idUnidad, Integer anio, Long idPeriodoPractica) {
		log.debug("REST request to get all distribuciones by oferta distribuida.");

		List<Distribucion> listaDistribuciones = new ArrayList<Distribucion>();

		List<DistribucionPeriodo> consultaDistribucionesPeriodo = distribucionPeriodoRepository
				.getDistribucionesPeriodoByOfertaCentroAndUnidadAndAnioAndPeriodo(idOferCen, idUnidad, anio,
						idPeriodoPractica);

		for (DistribucionPeriodo distribucionPeriodo : consultaDistribucionesPeriodo) {
			Distribucion dist = distribucionPeriodo.getDistribucion();
			dist.setDistribucionPeriodo(distribucionPeriodo);

			// Recuperamos los gastos //
			List<GastoAlumno> gastosAlumno = gastoAlumnoRepository.findByDistribucionPeriodo(distribucionPeriodo);
			dist.getMatricula().setGastosAlumno(gastosAlumno);

			listaDistribuciones.add(dist);
		}

		return listaDistribuciones;

	}

	/**
	 * Este metodo recupera lo datos de las distribuciones para una
	 * oferta/unidad/año/periodoPractica incluye los gastos asociados a la
	 * distribucion periodo
	 *
	 * @param idOferCen
	 * @param idUnidad
	 * @param anio
	 * @param idPeriodoPractica
	 * @return
	 */
	public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidasWithGastosByPeriodo(
			final String idOferCen, String idUnidad, Integer anio, Long idPeriodoPractica) {
		log.debug("REST request to get all distribuciones by oferta distribuida.");

		List<Distribucion> listaDistribuciones = new ArrayList<Distribucion>();

		List<DistribucionPeriodo> consultaDistribucionesPeriodo = distribucionPeriodoRepository
				.getDistribucionesPeriodoByOfertaCentroAndUnidadAndAnioAndPeriodo(idOferCen, idUnidad, anio,
						idPeriodoPractica);

		for (DistribucionPeriodo distribucionPeriodo : consultaDistribucionesPeriodo) {
			// Recuperamos los gastos //
			List<GastoAlumno> gastosAlumno = gastoAlumnoRepository.findByDistribucionPeriodo(distribucionPeriodo);
			distribucionPeriodo.setGastosAlumno(gastosAlumno);
			for (GastoAlumno gasto : gastosAlumno) {
				gasto.setGastoTotal2(importesTipoGasto2Service.getImporteByKilometros(gasto.getdistanciaTotal2()));
			}

			Distribucion dist = distribucionPeriodo.getDistribucion();
			dist.setDistribucionPeriodo(distribucionPeriodo);

			listaDistribuciones.add(dist);
		}

		return listaDistribuciones;

	}

	/**
	 * Este metodo analogo al anterior pero ademas filtra por TUTOR para recuperar
	 * lo datos de las distribuciones para una oferta/unidad/año/periodoPractica
	 * incluye los gastos asociados a la distribucion periodo
	 *
	 * @param idOferCen
	 * @param idUnidad
	 * @param anio
	 * @param idPeriodoPractica
	 * @param tutor
	 * @return
	 */
	public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidasByPeriodoTutor(final String idOferCen,
																							 String idUnidad, Integer anio, Long idPeriodoPractica, String tutor) {
		log.debug("REST request to get all distribuciones by oferta distribuida.");

		List<Distribucion> listaDistribuciones = new ArrayList<Distribucion>();

		List<DistribucionPeriodo> consultaDistribucionesPeriodo = distribucionPeriodoRepository
				.getDistribucionesPeriodoByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutor(idOferCen, idUnidad, anio,
						idPeriodoPractica, tutor);

		for (DistribucionPeriodo distribucionPeriodo : consultaDistribucionesPeriodo) {
			// Recuperamos los gastos //
			List<GastoAlumno> gastosAlumno = gastoAlumnoRepository.findByDistribucionPeriodo(distribucionPeriodo);

			for (GastoAlumno gasto : gastosAlumno) {
				gasto.setGastoTotal2(importesTipoGasto2Service.getImporteByKilometros(gasto.getdistanciaTotal2()));
			}
			Distribucion dist = distribucionPeriodo.getDistribucion();
			dist.getMatricula().setGastosAlumno(gastosAlumno);

			listaDistribuciones.add(dist);
		}

		return listaDistribuciones;

	}

	/**
	 * Este metodo analogo al anterior pero ademas filtra por TUTOR para recuperar
	 * lo datos de las distribuciones para una oferta/unidad/año/periodoPractica
	 * incluye los gastos asociados a la distribucion periodo
	 *
	 * @param idOferCen
	 * @param idUnidad
	 * @param anio
	 * @param idPeriodoPractica
	 * @param tutor
	 * @return
	 */
	public Iterable<Distribucion> getAllDistribucionesByMatriculasDistribuidasByPeriodoTutorWithGastosByPeriodo(
			final String idOferCen, String idUnidad, Integer anio, Long idPeriodoPractica, String tutor) {
		log.debug("REST request to get all distribuciones by oferta distribuida.");

		List<Distribucion> listaDistribuciones = new ArrayList<Distribucion>();

		List<DistribucionPeriodo> consultaDistribucionesPeriodo = distribucionPeriodoRepository
				.getDistribucionesPeriodoByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutor(idOferCen, idUnidad, anio,
						idPeriodoPractica, tutor);

		for (DistribucionPeriodo distribucionPeriodo : consultaDistribucionesPeriodo) {

			// Recuperamos los gastos //
			List<GastoAlumno> gastosAlumno = gastoAlumnoRepository.findByDistribucionPeriodo(distribucionPeriodo);
			distribucionPeriodo.setGastosAlumno(gastosAlumno);

			for (GastoAlumno gasto : gastosAlumno) {
				gasto.setGastoTotal2(importesTipoGasto2Service.getImporteByKilometros(gasto.getdistanciaTotal2()));
			}
			Distribucion dist = distribucionPeriodo.getDistribucion();
			dist.setDistribucionPeriodo(distribucionPeriodo);

			listaDistribuciones.add(dist);

		}

		return listaDistribuciones;

	}

	public List<Distribucion> getAllDistribuciones() {
		return distribucionRepository.findAll();
	}

	public Distribucion getDistribucionAndContenidosEF(final Long idDistribucion) throws IllegalArgumentException {
		Optional<Distribucion> distribucion = distribucionRepository.findById(idDistribucion);
		if (!distribucion.isPresent()) {
			throw new IllegalArgumentException("No existe una distribucion con ese identificador.");
		}
		List<ContenidoEF> contenidosEF = contenidoEFService.getContenidoEFByDistribucion(idDistribucion);
		distribucion.get().setContenidosEF(contenidosEF);
		return distribucion.get();
	}

	/**
	 * Consulta la lista de distribuciones incluyendo la informacion sobre el
	 * periodo de la distribucion. Usado en el export de distribuciones.
	 *
	 * @return
	 */
	public List<Distribucion> getDistribucionesAndPeriodosByCentroAndAnio(String idCentro, Integer anio, String tutor) {

		List<Distribucion> listaDist = new ArrayList<>();

		// Planteamos la consulta desde la distribucionPeriodo para que el JQL sea mas
		// rápido //

		String jqlSelect = " select dpe ";
		String jqlFrom = " from Matricula m  "
				+ " inner join Distribucion d on d.matricula.idMatricula = m.idMatricula  "
				+ " inner join DistribucionPeriodo dpe on d.idDistribucion = dpe.distribucion.idDistribucion  ";
		String jqlWhere = " where m.anio= :anio ";
		String jqlOrder = " order by dpe.periodo.tipoPractica.nombre , dpe.periodo.nombre asc ";

		// Where dinamico //

		if (!StringUtils.isEmpty(idCentro) && !idCentro.equals(UNDEFINED)) {
			jqlWhere += " and m.ofertaCentro.centro.idCentro =:idCentro ";
		}

		if (!StringUtils.isEmpty(tutor) && !tutor.equals(UNDEFINED)) {
			jqlWhere += " and (m.unidad.tutor.idProfesor= :tutor or m.unidad.tutorAdicional.idProfesor= :tutor) ";
		}

		// Create Query ((

		TypedQuery<DistribucionPeriodo> query = entityManager.createQuery(jqlSelect + jqlFrom + jqlWhere + jqlOrder,
				DistribucionPeriodo.class);

		// Set Query parameters dinamico //

		query.setParameter("anio", anio);

		if (!StringUtils.isEmpty(idCentro) && !idCentro.equals(UNDEFINED)) {
			query.setParameter("idCentro", idCentro);
		}

		if (!StringUtils.isEmpty(tutor) && !tutor.equals(UNDEFINED)) {
			query.setParameter("tutor", tutor);
		}

		// Recomponemos una lista con la distribucion y su distribucionPeriodo asociado
		// //

		List<DistribucionPeriodo> listaDispe = query.getResultList();
		listaDispe.forEach(item -> {
			item.getDistribucion().setDistribucionPeriodo(item);
			listaDist.add(item.getDistribucion());
		});

		return listaDist;

	}

	public List<DistribucionDTO> getDistribucionesAndPeriodosByCentroAndAnioDTO(String idCentro, Integer anio, String tutor) {
		return distribucionRepository.findDistribucionesDTOByCentroAnioTutor(idCentro, anio, tutor);
	}

	public ResponseEntity<Distribucion> updateDistribucion(final Distribucion distribucionModificada)
			throws URISyntaxException {
		log.debug("SERVICE request to update Distribucion : {}", distribucionModificada);
		if (distribucionModificada.getIdDistribucion() == null) {
			return createDistribucion(distribucionModificada);
		}

		Distribucion result = distribucionRepository.save(distribucionModificada);
		// Guardamos los contenidos de elementos formativos //
		saveContenidosEF(distribucionModificada);

		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, distribucionModificada.getIdDistribucion().toString()))
				.body(result);

	}

	public ResponseEntity<Distribucion> updateBaja(final Distribucion distribucionModificada)
			throws URISyntaxException {
		log.debug("SERVICE request to update Distribucion baja : {}", distribucionModificada);

		distribucionRepository.updateBaja(distribucionModificada);

		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, distribucionModificada.getIdDistribucion().toString()))
				.body(distribucionModificada);

	}

	public void deleteDistribucion(final Long idDistribucion) {
		Optional<Distribucion> distribucion = distribucionRepository.findById(idDistribucion);
		if (!distribucion.isPresent()) {
			throw new IllegalArgumentException("No existe una distribucion con ese identificador.");
		}

		// Borramos los contenidos EF asociados //
		contenidoEFService.getContenidoEFByDistribucion(idDistribucion)
				.forEach(contenidoEF -> contenidoEFService.deleteContenidoEF(contenidoEF.getIdContenidoEF()));

		distribucionRepository.deleteById(idDistribucion);
	}

	public Iterable<AlumDisAnexoiii> getListaAlumDisByAnexo(Integer idAnexo) {

		Iterable<AlumDisAnexoiii> listaAlumdis = alumDisAnexoiiiRepositoy.findAlumDisByAnexo(idAnexo);
		return listaAlumdis;

	}

	/**
	 * Guarda los contenidos de elementos formativos asociados a un ciclo
	 *
	 * @param distribucionSaved
	 * @throws URISyntaxException
	 */
	private void saveContenidosEF(Distribucion distribucionSaved) throws URISyntaxException {
		// Guardamos los contenidos de elementos formativos //

		if (!distribucionSaved.getContenidosEF().isEmpty()) {
			for (ContenidoEF contenidoEF : distribucionSaved.getContenidosEF()) {
				contenidoEF.setDistribucion(distribucionSaved);
				contenidoEFService.updateContenidoEF(contenidoEF);
			}
		}
	}

}

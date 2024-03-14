package es.princast.gepep.service.saucesincro;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.SauceSincro;
import es.princast.gepep.domain.TareaLog;
import es.princast.gepep.domain.Unidad;
import es.princast.gepep.domain.Usuario;
import es.princast.gepep.repository.CursoAcademicoRepository;
import es.princast.gepep.repository.ProfesorRepository;
import es.princast.gepep.repository.SauceSincroRepository;
import es.princast.gepep.repository.UnidadRepository;
import es.princast.gepep.repository.UsuarioRepository;
import es.princast.gepep.service.TareaLogService;
import es.princast.gepep.service.dto.AlumnoDTO;
import es.princast.gepep.service.dto.CentroDTO;
import es.princast.gepep.service.dto.CicloDTO;
import es.princast.gepep.service.dto.EnsenanzaDTO;
import es.princast.gepep.service.dto.MatriculaDTO;
import es.princast.gepep.service.dto.OfertaCentroDTO;
import es.princast.gepep.service.dto.OfertaDTO;
import es.princast.gepep.service.dto.ProfesorDTO;
import es.princast.gepep.service.dto.UnidadDTO;
import es.princast.gepep.service.util.GepepHelper;
import es.princast.gepep.web.rest.util.FormatUtil;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.sauce._1_0.sauce_wsdl.BuscaCentrosCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaCiclosCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaEnsenanzasCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaMatriculasCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaOfertasCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaOfertasCentrosCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaProfesoresCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaUnidadesCCFFErrorMensaje;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SauceSincroService {

	private static final int REINTENTOS_LLAMADA_WS_SAUCE = 3;

	private static final String DD_MM_YYYY = "dd/MM/yyyy";

	private static final String FIN = "fin";

	private static final String INICIO = "inicio";

	public static final String USU_SINCRO = "SINCRO";

	private static final String ERROR_BAJA = "Error baja";

	private static final String ERROR_CONSULTANDO_SAUCE_WS = "Error consultando SauceWS";

	private static final String ERROR_SINCRONIZANDO = "Error sincronizando";

	private static final String ERROR_INSERTANDO = "Error insertando";

	private static final String ERROR_NO_CONTROLADO = "Error no controlado";

	private static final String TAREA_SAUCE_SINCRO = "SauceSincro";

	private static final String TAREA_SAUCE_SINCRO_ALL = "all";

	private static final String SUBTAREA_ENSENANZAS = "ensenanzasall";

	private static final String SUBTAREA_CICLOS = "ciclosall";

	private static final String SUBTAREA_OFERTASFORMATIVAS = "ofertasformativasall";

	private static final String SUBTAREA_CENTROS = "centrosall";

	private static final String SUBTAREA_ALUMNOS = "alumnosall";

	private static final String SUBTAREA_OFERTASCENTROS = "ofertascentrosall";

	private static final String SUBTAREA_PROFESORES = "profesoresall";

	private static final String SUBTAREA_UNIDADES = "unidadesall";

	private static final String SUBTAREA_MATRICULAS = "matriculasall";

	private static final String ENTITY_NAME = "sauceSincro";

	@Value("${application.plaintsis.sincrosauce.registros}")
	private Integer maxRegistros;

	@Value("${application.plaintsis.sincrosauce.ofuscar:#{false}}")
	private Boolean ofuscar;

	@Value("${application.plaintsis.sincrosauce.permiteAnio:#{false}}")
	private Boolean permiteAnio;

	@Autowired
	private SauceSincroRepository sauceSincroRepository;

	@Autowired
	private SauceSincroAlumnoService alumnoService;

	@Autowired
	private SauceSincroEnsenanzaService ensenanzaService;

	@Autowired
	private SauceSincroCentroService centroService;

	@Autowired
	private SauceSincroCicloService cicloService;

	@Autowired
	private SauceSincroOfertaFormativaService ofertaService;

	@Autowired
	private SauceSincroOfertasCentroService ofertasCentroService;

	@Autowired
	private SauceSincroProfesorService profesorService;

	@Autowired
	private SauceSincroMatriculaService matriculaService;

	@Autowired
	private SauceSincroUnidadService unidadService;

	@Autowired
	private TareaLogService tareaLogService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SauceService sauceService;

	@Autowired
	private CursoAcademicoRepository cursoAcademicoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ProfesorRepository profesorRepository;

	@Autowired
	private UnidadRepository unidadRepository;

	private BigInteger idTareaLog;

	public ResponseEntity<SauceSincro> createSauceSincro(final SauceSincro nuevoSauceSincro) throws URISyntaxException {
		log.debug("SERVICE request to save SauceSincro : {}", nuevoSauceSincro);

		if (nuevoSauceSincro.getIdSincro() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.sauceSincro.nuevo.id", null, LocaleContextHolder.getLocale()));

		}

		SauceSincro result = sauceSincroRepository.save(nuevoSauceSincro);
		return ResponseEntity.created(new URI("/api/sauceSincroes/" + result.getIdSincro()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdSincro().toString()))
				.body(result);
	}

	public SauceSincro getSauceSincro(final String idSauceSincro) {
		log.debug("SauceSincroService.getSauceSincro " + idSauceSincro);
		Optional<SauceSincro> sauceSincro = sauceSincroRepository.findById(idSauceSincro);
		if (!sauceSincro.isPresent()) {
			throw new IllegalArgumentException("No existe una sauceSincro con ese identificador.");
		}
		return sauceSincro.get();
	}

	private Sort sortByOrden() {

		String[] propiedades = { "orden" };
		Sort sort = Sort.by(Sort.Direction.ASC, propiedades);
		return sort;
	}

	public List<SauceSincro> getAllSauceSincro() {
		return sauceSincroRepository.findAll(sortByOrden());
	}

	public ResponseEntity<SauceSincro> updateSauceSincro(final SauceSincro sauceSincroModificado)
			throws URISyntaxException {
		log.debug("SERVICE request to update SauceSincro : {}", sauceSincroModificado);
		if (sauceSincroModificado.getIdSincro() == null) {
			return createSauceSincro(sauceSincroModificado);
		}

		SauceSincro result = sauceSincroRepository.save(sauceSincroModificado);
		return ResponseEntity.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sauceSincroModificado.getIdSincro().toString()))
				.body(result);

	}

	/**
	 * Servicio que lanza las sincronizaciones de datos de BD gepep con los datos de
	 * las consultas sobre SauceWS.
	 * @param idSauceSincro
	 * @param finicio
	 * @param cursoAcademico
	 * @throws Exception
	 */
	public void sincronizar(final String idSauceSincro, final String finicio, Integer cursoAcademico) throws Exception {
		log.debug("SauceSincroService.sincronizar : " + idSauceSincro);

		// Inicializamos el log de auditoria del proceso de sincronizacion //
		this.idTareaLog = tareaLogService.getTareaId();
		createLog(INICIO, null);
		tareaLogService.limpiarLogMes();

		boolean existeErrorNoControlado = false;
		boolean existeErrorControlado = false;

		SauceSincro sincro = getSauceSincro(idSauceSincro);

		String ultimaActualizacion = this.getFechaFormateada(sincro.getFechaActualizacion());

		if (finicio != null && !finicio.equals("")) {
			ultimaActualizacion = finicio.replace("_", "/");
		}

		if(!permiteAnio){
			cursoAcademico = null;
		}

		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) || sincro.getIdSincro().equals(SUBTAREA_ENSENANZAS)) {
			try {
				createLog(SUBTAREA_ENSENANZAS, INICIO);

				if (!sincronizarEnsenanzas(ultimaActualizacion)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				createErrorLog(SUBTAREA_ENSENANZAS, ERROR_NO_CONTROLADO, e1);
				throw e1;
			} finally {
				createLog(SUBTAREA_ENSENANZAS, FIN + ensenanzaService.resultadosAuditoria());
			}
		}

		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) || sincro.getIdSincro().equals(SUBTAREA_CICLOS)) {
			try {
				createLog(SUBTAREA_CICLOS, INICIO);

				if (!sincronizarCiclos(ultimaActualizacion)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				createErrorLog(SUBTAREA_CICLOS, ERROR_NO_CONTROLADO, e1);
				throw e1;
			} finally {
				createLog(SUBTAREA_CICLOS, FIN + cicloService.resultadosAuditoria());
			}

		}
		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL)
				|| sincro.getIdSincro().equals(SUBTAREA_OFERTASFORMATIVAS)) {
			try {
				createLog(SUBTAREA_OFERTASFORMATIVAS, INICIO);
				log.debug("SauceSincroService.sincronizarOfertas");

				if (!sincronizarOfertas(ultimaActualizacion)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				createErrorLog(SUBTAREA_OFERTASFORMATIVAS, ERROR_NO_CONTROLADO, e1);
				throw e1;
			} finally {
				createLog(SUBTAREA_OFERTASFORMATIVAS, FIN + ofertaService.resultadosAuditoria());
			}
		}
		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) || sincro.getIdSincro().equals(SUBTAREA_CENTROS)) {
			try {
				createLog(SUBTAREA_CENTROS, INICIO);

				if (!sincronizarCentros(ultimaActualizacion)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				createErrorLog(SUBTAREA_CENTROS, ERROR_NO_CONTROLADO, e1);
				throw e1;
			} finally {
				createLog(SUBTAREA_CENTROS, FIN + centroService.resultadosAuditoria());
			}
		}
		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) || sincro.getIdSincro().equals(SUBTAREA_ALUMNOS)) {
			try {
				createLog(SUBTAREA_ALUMNOS, INICIO);

				if (!sincronizarAlumnos(ultimaActualizacion, cursoAcademico)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				createErrorLog(SUBTAREA_ALUMNOS, ERROR_NO_CONTROLADO, e1);
				throw e1;
			} finally {
				createLog(SUBTAREA_ALUMNOS, FIN + alumnoService.resultadosAuditoria());
			}
		}
		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL)
				|| sincro.getIdSincro().equals(SUBTAREA_OFERTASCENTROS)) {
			try {
				createLog(SUBTAREA_OFERTASCENTROS, INICIO);

				if (!sincronizarOfertasCentros(ultimaActualizacion)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				createErrorLog(SUBTAREA_OFERTASCENTROS, ERROR_NO_CONTROLADO, e1);
				throw e1;

			} finally {
				createLog(SUBTAREA_OFERTASCENTROS, FIN + ofertasCentroService.resultadosAuditoria());
			}

		}
		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) || sincro.getIdSincro().equals(SUBTAREA_PROFESORES)) {
			try {
				createLog(SUBTAREA_PROFESORES, INICIO);

				if (!sincronizarProfesores(ultimaActualizacion, cursoAcademico)) {
					existeErrorControlado = true;
				}

			} catch (Exception e) {
				existeErrorNoControlado = true;
				log.error("Error al sincronizar profesores " + e.getMessage(), e);
				createErrorLog(SUBTAREA_PROFESORES, ERROR_NO_CONTROLADO, e);
				throw e;
			} finally {
				createLog(SUBTAREA_PROFESORES, FIN + profesorService.resultadosAuditoria());
			}
		}
		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) || sincro.getIdSincro().equals(SUBTAREA_UNIDADES)) {
			try {
				createLog(SUBTAREA_UNIDADES, INICIO);

				if (!sincronizarUnidades(ultimaActualizacion, cursoAcademico)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				log.error("Error al sincronizar unidades " + e1.getMessage());
				createErrorLog(SUBTAREA_UNIDADES, ERROR_NO_CONTROLADO, e1);
				throw e1;
			} finally {
				createLog(SUBTAREA_UNIDADES, FIN + unidadService.resultadosAuditoria());
			}
		}
		if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) || sincro.getIdSincro().equals(SUBTAREA_MATRICULAS)) {
			try {
				createLog(SUBTAREA_MATRICULAS, INICIO);

				if (!sincronizarMatriculas(ultimaActualizacion, cursoAcademico)) {
					existeErrorControlado = true;
				}

			} catch (Exception e1) {
				existeErrorNoControlado = true;
				log.error("Error al sincronizar matriculas " + e1.getMessage());
				createErrorLog(SUBTAREA_MATRICULAS, ERROR_NO_CONTROLADO, e1);
				throw e1;
			} finally {
				createLog(SUBTAREA_MATRICULAS, FIN + matriculaService.resultadosAuditoria());
			}
		}

		try {
			if (sincro.getIdSincro().equals(TAREA_SAUCE_SINCRO_ALL) && !existeErrorNoControlado) {

				if (!existeErrorControlado) {
					sincro.setFechaExito(Instant.now());
				}

				sincro.setFechaActualizacion(Instant.now());
				this.updateSauceSincro(sincro);
			}
		} catch (Exception e) {
			createErrorLog("Actualizacion registro SauceSincro", ERROR_NO_CONTROLADO, e);
			throw e;
		} finally {
			createLog(FIN, null);
		}
	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @param cursoAcademico
	 * @return boolean
	 * @throws BuscaMatriculasCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarMatriculas(String ultimaActualizacion, Integer cursoAcademico)
			throws BuscaMatriculasCCFFErrorMensaje, URISyntaxException {

		matriculaService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<MatriculaDTO> matriculas = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = 0;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, cursoAcademico);
			try {
				matriculas = sauceService.getListaMatriculasCCFF(id, maxRegistros, ultimaActualizacion, cursoAcademico);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_MATRICULAS, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}

			if (matriculas.size() < maxRegistros)
				pedirMas = false;
			else
				id = matriculas.get(maxRegistros - 1).getXMatricula();

			for (Iterator<MatriculaDTO> iterator = matriculas.iterator(); iterator.hasNext();) {
				MatriculaDTO matriculaDTO = (MatriculaDTO) iterator.next();
				Matricula matricula = matriculaDTO.toVO();
				String idMatricula = FormatUtil.convertIdSauceToGepep(matriculaDTO.getIdMatricula());
				matricula.setIdMatricula(idMatricula);
				try {

					matriculaService.getMatricula(idMatricula);
					matriculaService.updateMatricula(matricula);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe
					try {
						matriculaService.createMatricula(matricula);
					} catch (Exception ex) {
						createErrorLog(SUBTAREA_MATRICULAS, ERROR_INSERTANDO + " matricula id="
								+ matricula.getIdMatricula() + datosPeticionSauce + matricula, ex);
						procesoSinError = false;
					}
				} catch (Exception ex) {

					createErrorLog(SUBTAREA_MATRICULAS, ERROR_SINCRONIZANDO + " matricula id="
							+ matricula.getIdMatricula() + datosPeticionSauce + matricula, ex);
					procesoSinError = false;
				}

				// Actualizar los tutores de las unidades cargadas en gepep con los tutores
				// indicados por las matriculas que vienen de Sauce//
				// No actualizar los tutores de unidades de Centros concertados!!

				try {
					if (matriculaDTO.getTutor() != null) {
						String idSauceTutor = FormatUtil.convertIdSauceToGepep(matriculaDTO.getTutor());
						Unidad unidad = unidadService.getUnidad(matricula.getUnidad().getIdUnidad());
						Unidad unidadOld = unidad.clone();
						// Si la unidad no tiene tutor o es distinto al existente la actualizamos //
						// revisado: Los centros concertados no disponen de profesorado en SAUCE.
						// revisado: Si se trata de una unidad de un centro concertado no actualizamos
						// su tutor //
						if ((unidadOld != null && unidadOld.getOfertaCentro() != null
								&& unidadOld.getOfertaCentro().getCentro() != null
								&& !unidadOld.getOfertaCentro().getCentro().getConcertado())
								&& (unidad.getTutor() == null
										|| !unidad.getTutor().getIdProfesor().equals(idSauceTutor))) {
							unidad.setTutor(profesorService.getProfesor(idSauceTutor));
							unidadService.updateUnidad(unidad);
							actualizaEstadoUsuario(unidadOld, unidad);
						}
					}
				} catch (Exception ex) {

					createErrorLog(SUBTAREA_MATRICULAS,
							ERROR_SINCRONIZANDO + " al actualizar unidad con el tutor indicado por la matricula con id="
									+ matricula.getIdMatricula() + " Tutor: " + matriculaDTO.getTutor()
									+ datosPeticionSauce + matricula,
							ex);
					procesoSinError = false;
				}

				matriculaService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_MATRICULAS);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;
	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @param cursoAcademico
	 * @return boolean
	 * @throws BuscaProfesoresCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarProfesores(String ultimaActualizacion, Integer cursoAcademico)
			throws BuscaProfesoresCCFFErrorMensaje, URISyntaxException {

		profesorService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<ProfesorDTO> profes = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = 0;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, cursoAcademico);
			int reintentos = 0;
			boolean errorLlamadaSauceWS;
			do {
				errorLlamadaSauceWS = false;
				long TInicio = System.currentTimeMillis();
				try {

					profes = sauceService.getListaProfesoresCCFF(id, maxRegistros, ultimaActualizacion, cursoAcademico);

				} catch (Exception ex) {
					errorLlamadaSauceWS = true;
					reintentos++;
					long TFin = System.currentTimeMillis();
					long tiempo = TFin - TInicio;
					createErrorLog(SUBTAREA_PROFESORES, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce
							+ " TEjecucionMiliSeg=" + tiempo + " Reintento=" + reintentos, ex);
					if (reintentos == REINTENTOS_LLAMADA_WS_SAUCE) {
						throw ex;
					}
				}
			} while (errorLlamadaSauceWS && reintentos < REINTENTOS_LLAMADA_WS_SAUCE);

			if (profes.size() < maxRegistros)
				pedirMas = false;
			else
				id = profes.get(maxRegistros - 1).getIdEmpleado();

			for (Iterator<ProfesorDTO> iterator = profes.iterator(); iterator.hasNext();) {
				ProfesorDTO profesorDTO = (ProfesorDTO) iterator.next();
				Profesor profesor = profesorDTO.toVO();
				String idProfesor = FormatUtil.convertIdSauceToGepep(profesorDTO.getIdEmpleado());

				profesor.setIdProfesor(idProfesor);

				if (ofuscar) {
					GepepHelper.ofuscarProfesor(profesor);
				}
				try {
					Profesor profesorGepep = profesorService.getProfesor(idProfesor);
					if (profesorGepep != null) {
						profesor.setMatricula(profesorGepep.getMatricula());
						profesor.setVehiculo(profesorGepep.getVehiculo());
						profesor.setGrupo(profesorGepep.getGrupo());
						profesor.setCuenta(profesorGepep.getCuenta());
						profesor.setCuerpo(profesorGepep.getCuerpo());
					}
					profesorService.updateProfesor(profesor);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe
					try {
						Profesor profesorNIF = profesorService.getProfesorActivoByNif(profesor.getNif().toString());
						if (profesorNIF != null
								&& !profesor.getIdProfesor().equals(profesorNIF.getIdProfesor().toString())) {
							// son entidades distintas pero mismo NIF. Doy de baja el ya existente para
							// insertar el nuevo.
							profesorNIF.setFechaBaja(LocalDate.now());
							try {
								profesorService.updateProfesor(profesorNIF);
							} catch (Exception exBaja) {
								createErrorLog(SUBTAREA_PROFESORES,
										ERROR_BAJA + " profesor id=" + idProfesor + datosPeticionSauce + profesorNIF,
										exBaja);
								procesoSinError = false;
							}

						}
						profesorService.createProfesor(profesor);
					} catch (Exception ex) {

						createErrorLog(SUBTAREA_PROFESORES,
								ERROR_INSERTANDO + " profesor id=" + idProfesor + datosPeticionSauce + profesor, ex);
						procesoSinError = false;

					}
				} catch (Exception excep) {

					createErrorLog(SUBTAREA_PROFESORES,
							ERROR_SINCRONIZANDO + " profesor id=" + idProfesor + datosPeticionSauce + profesor, excep);
					procesoSinError = false;
				}

				profesorService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_PROFESORES);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;
	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @return boolean
	 * @throws BuscaOfertasCentrosCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarOfertasCentros(String ultimaActualizacion)
			throws BuscaOfertasCentrosCCFFErrorMensaje, URISyntaxException {

		ofertasCentroService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<OfertaCentroDTO> ofertasCentros = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = 0;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, null);
			try {
				ofertasCentros = sauceService.getListaOfertasCentrosCCFF(id, maxRegistros, ultimaActualizacion);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_OFERTASCENTROS, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}

			if (ofertasCentros.size() < maxRegistros)
				pedirMas = false;
			else
				id = ofertasCentros.get(maxRegistros - 1).getIdOfertaCentro();

			for (Iterator<OfertaCentroDTO> iterator = ofertasCentros.iterator(); iterator.hasNext();) {
				OfertaCentroDTO ofertaCentroDTO = (OfertaCentroDTO) iterator.next();
				OfertasCentro ofertaCentro = ofertaCentroDTO.toVO();
				String idOfertaCentro = FormatUtil.convertIdSauceToGepep(ofertaCentroDTO.getIdOfertaCentro());
				ofertaCentro.setIdOfertaCentro(idOfertaCentro);
				try {
					ofertasCentroService.getOfertasCentro(idOfertaCentro);
					ofertasCentroService.updateOfertasCentro(ofertaCentro);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe la ofertacentro bien alguna
					// entidad dependiente
					try {
						ofertasCentroService.createOfertasCentro(ofertaCentro);
					} catch (Exception ex) {
						createErrorLog(SUBTAREA_OFERTASCENTROS, ERROR_INSERTANDO + "oferta con id=" + idOfertaCentro
								+ datosPeticionSauce + ofertaCentro, ex);
						procesoSinError = false;
					}
				} catch (Exception excep) {
					createErrorLog(SUBTAREA_OFERTASCENTROS,
							ERROR_SINCRONIZANDO + "oferta con id=" + idOfertaCentro + datosPeticionSauce + ofertaCentro,
							excep);
					procesoSinError = false;
				}
				ofertasCentroService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_OFERTASCENTROS);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;

	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @param cursoAcademico
	 * @return boolean
	 * @throws Exception
	 * @throws URISyntaxException
	 */
	private boolean sincronizarAlumnos(String ultimaActualizacion, Integer cursoAcademico) throws Exception, URISyntaxException {

		alumnoService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<AlumnoDTO> alumnos = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = 0;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, cursoAcademico);
			try {
				alumnos = sauceService.getListaAlumnosCCFF(id, maxRegistros, ultimaActualizacion, cursoAcademico);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_ALUMNOS, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}

			if (alumnos.size() < maxRegistros)
				pedirMas = false;
			else
				id = alumnos.get(maxRegistros - 1).getIdAlumno();

			for (Iterator<AlumnoDTO> iterator = alumnos.iterator(); iterator.hasNext();) {
				AlumnoDTO alumnoDTO = (AlumnoDTO) iterator.next();
				Alumno alumno = alumnoDTO.toVO();
				String idAlumno = FormatUtil.convertIdSauceToGepep(alumnoDTO.getIdAlumno());
				alumno.setIdAlumno(idAlumno);

				if (ofuscar) {
					GepepHelper.ofuscarAlumno(alumno);
				}

				try {
					alumnoService.getAlumno(idAlumno);
					alumnoService.updateAlumno(alumno);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe
					try {
						alumnoService.createAlumno(alumno);
					} catch (Exception ex) {
						createErrorLog(SUBTAREA_ALUMNOS,
								ERROR_INSERTANDO + " alumno id=" + idAlumno + datosPeticionSauce + alumno, ex);
						procesoSinError = false;
					}
				} catch (Exception excep) {
					createErrorLog(SUBTAREA_ALUMNOS,
							ERROR_SINCRONIZANDO + " alumno id=" + idAlumno + datosPeticionSauce + alumno, excep);
					procesoSinError = false;
				}
				alumnoService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_ALUMNOS);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);
		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;

	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @return boolean
	 * @throws BuscaCentrosCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarCentros(String ultimaActualizacion)
			throws BuscaCentrosCCFFErrorMensaje, URISyntaxException {

		centroService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<CentroDTO> centros = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = 0;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, null);
			try {
				centros = sauceService.getListaCentrosCCFF(id, maxRegistros, ultimaActualizacion);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_CENTROS, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}

			if (centros.size() < maxRegistros)
				pedirMas = false;
			else
				id = centros.get(maxRegistros - 1).getIdCentro();

			for (Iterator<CentroDTO> iterator = centros.iterator(); iterator.hasNext();) {
				CentroDTO centroDTO = (CentroDTO) iterator.next();
				Centro centro = centroDTO.toVO();
				String idCentro = FormatUtil.convertIdSauceToGepep(centroDTO.getIdCentro());
				centro.setIdCentro(idCentro);
				try {
					centroService.getCentro(idCentro);
					centroService.updateCentro(centro);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe
					try {
						centroService.createCentro(centro);
					} catch (Exception ex) {

						createErrorLog(SUBTAREA_CENTROS,
								ERROR_INSERTANDO + " centro id=" + idCentro + datosPeticionSauce + centro, ex);
						procesoSinError = false;
					}
				} catch (Exception excep) {

					createErrorLog(SUBTAREA_CENTROS,
							ERROR_SINCRONIZANDO + " centro id=" + idCentro + datosPeticionSauce + centro, excep);
					procesoSinError = false;
				}
				centroService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_CENTROS);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;

	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @return boolean
	 * @throws BuscaOfertasCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarOfertas(String ultimaActualizacion)
			throws BuscaOfertasCCFFErrorMensaje, URISyntaxException {

		ofertaService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<OfertaDTO> ofertas = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = 0;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, null);
			try {
				ofertas = sauceService.getListaOfertasCCFF(id, maxRegistros, ultimaActualizacion);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_OFERTASFORMATIVAS, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}

			if (ofertas.size() < maxRegistros)
				pedirMas = false;
			else
				id = ofertas.get(maxRegistros - 1).getIdOferta();

			for (Iterator<OfertaDTO> iterator = ofertas.iterator(); iterator.hasNext();) {
				OfertaDTO ofertaDTO = (OfertaDTO) iterator.next();
				OfertaFormativa oferta = ofertaDTO.toVO();
				String idOferta = FormatUtil.convertIdSauceToGepep(ofertaDTO.getIdOferta());
				oferta.setIdOfertaFormativa(idOferta);
				try {
					ofertaService.getOfertaFormativa(idOferta);
					ofertaService.updateOfertaFormativa(oferta);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe
					try {
						ofertaService.createOfertaFormativa(oferta);
					} catch (Exception ex) {
						createErrorLog(SUBTAREA_OFERTASFORMATIVAS,
								ERROR_INSERTANDO + " oferta id=" + idOferta + datosPeticionSauce + oferta, ex);
						procesoSinError = false;
					}
				} catch (Exception excep) {
					createErrorLog(SUBTAREA_OFERTASFORMATIVAS,
							ERROR_SINCRONIZANDO + " oferta id=" + idOferta + datosPeticionSauce + oferta, excep);
					procesoSinError = false;
				}
				ofertaService.incrementarProcesados();
			}
		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_OFERTASFORMATIVAS);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;

	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @return boolean
	 * @throws BuscaCiclosCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarCiclos(String ultimaActualizacion)
			throws BuscaCiclosCCFFErrorMensaje, URISyntaxException {

		cicloService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<CicloDTO> ciclos = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = -1;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, null);
			try {
				ciclos = sauceService.getListaCiclosCCFF(id, maxRegistros, ultimaActualizacion);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_CICLOS, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}

			if (ciclos.size() < maxRegistros)
				pedirMas = false;
			else
				id = ciclos.get(maxRegistros - 1).getIdCiclo();

			for (Iterator<CicloDTO> iterator = ciclos.iterator(); iterator.hasNext();) {
				CicloDTO cicloDTO = (CicloDTO) iterator.next();
				Ciclo ciclo = cicloDTO.toVO();
				String idCiclo = FormatUtil.convertIdCicloSauceToGepep(cicloDTO.getIdCiclo().toString(),
						cicloDTO.getIdEtapa().toString());
				ciclo.setIdCiclo(idCiclo);

				try {
					cicloService.getCiclo(idCiclo);
					cicloService.updateCiclo(ciclo);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe
					try {
						cicloService.createCiclo(ciclo);
					} catch (Exception ex) {

						createErrorLog(SUBTAREA_CICLOS,
								ERROR_INSERTANDO + " ciclo id=" + idCiclo + datosPeticionSauce + ciclo, ex);
						procesoSinError = false;
					}

				} catch (Exception excep) {
					createErrorLog(SUBTAREA_CICLOS,
							ERROR_SINCRONIZANDO + " ciclo id=" + idCiclo + datosPeticionSauce + ciclo, excep);
					procesoSinError = false;

				}
				cicloService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_CICLOS);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;

	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @return boolean
	 * @throws BuscaEnsenanzasCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarEnsenanzas(String ultimaActualizacion)
			throws BuscaEnsenanzasCCFFErrorMensaje, URISyntaxException {

		ensenanzaService.resetAuditoria();
		boolean pedirMas = true;
		int id = 0;
		int numPeticionSauce = 0;
		List<EnsenanzaDTO> ensenanzas = null;
		boolean procesoSinError = true;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, null);
			try {
				ensenanzas = sauceService.getListaEnsenanzasCCFF(id, maxRegistros, ultimaActualizacion);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_ENSENANZAS, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}
			if (ensenanzas.size() < maxRegistros)
				pedirMas = false;
			else
				id = ensenanzas.get(maxRegistros - 1).getIdEtapa();

			for (Iterator<EnsenanzaDTO> iterator = ensenanzas.iterator(); iterator.hasNext();) {
				EnsenanzaDTO ensenanzaDTO = (EnsenanzaDTO) iterator.next();
				Ensenanza ensenanza = ensenanzaDTO.toVO();
				String idEnsenanza = FormatUtil.convertIdSauceToGepep(ensenanzaDTO.getIdEtapa());
				ensenanza.setIdEnsenanza(idEnsenanza);
				ensenanza.setDeSauce(Boolean.TRUE);
				try {
					ensenanzaService.getEnsenanza(idEnsenanza);
					ensenanzaService.updateEnsenanza(ensenanza);
				} catch (IllegalArgumentException e) {
					// si se lanza una excepci\F3n es porque no existe
					try {
						ensenanzaService.createEnsenanza(ensenanza);
					} catch (Exception ex) {
						createErrorLog(SUBTAREA_ENSENANZAS,
								ERROR_INSERTANDO + " ensenanza id=" + idEnsenanza + datosPeticionSauce + ensenanza, ex);
						procesoSinError = false;
					}

				} catch (Exception excep) {
					createErrorLog(SUBTAREA_ENSENANZAS,
							ERROR_SINCRONIZANDO + " ensenanza id=" + idEnsenanza + datosPeticionSauce + ensenanza,
							excep);
					procesoSinError = false;
				}

				ensenanzaService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_ENSENANZAS);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;

	}

	/**
	 * 
	 * @param ultimaActualizacion
	 * @param cursoAcademico
	 * @return boolean
	 * @throws BuscaUnidadesCCFFErrorMensaje
	 * @throws URISyntaxException
	 */
	private boolean sincronizarUnidades(String ultimaActualizacion, Integer cursoAcademico)
			throws BuscaUnidadesCCFFErrorMensaje, URISyntaxException {

		unidadService.resetAuditoria();
		boolean pedirMas;
		int id;
		int numPeticionSauce = 0;
		List<UnidadDTO> unidades = null;
		boolean procesoSinError = true;

		pedirMas = true;
		id = 0;

		while (pedirMas) {
			String datosPeticionSauce = getStringDatosPeticionSauce(++numPeticionSauce, ultimaActualizacion, id, cursoAcademico);
			try {
				unidades = sauceService.getListaUnidadesCCFF(id, maxRegistros, ultimaActualizacion, cursoAcademico);
			} catch (Exception ex) {
				createErrorLog(SUBTAREA_UNIDADES, ERROR_CONSULTANDO_SAUCE_WS + datosPeticionSauce, ex);
				throw ex;
			}

			if (unidades.size() < maxRegistros)
				pedirMas = false;
			else
				id = unidades.get(maxRegistros - 1).getIdUnidad();

			for (Iterator<UnidadDTO> iterator = unidades.iterator(); iterator.hasNext();) {
				UnidadDTO unidadDTO = (UnidadDTO) iterator.next();
				Unidad unidadSauce = unidadDTO.toVO();
				String idUnidad = FormatUtil.convertIdSauceToGepep(unidadDTO.getIdUnidad());
				unidadSauce.setIdUnidad(idUnidad);

				try {
					Unidad unidadExistente = unidadService.getUnidad(idUnidad);

					// Los centros concertados no disponen de profesorado en SAUCE. Por tanto las
					// unidades de la oferta (que s\ED est\E1n en SAUCE) no tienen asignado tutor/a.
					// Si se trata de una unidad de un centro concertado no actualizamos su tutor //
					if (unidadExistente != null && unidadExistente.getOfertaCentro() != null
							&& unidadExistente.getOfertaCentro().getCentro() != null
							&& unidadExistente.getOfertaCentro().getCentro().getConcertado()) {

						unidadSauce.setTutor(unidadExistente.getTutor());
						unidadSauce.setTutorAdicional(unidadExistente.getTutorAdicional());
					} else {
						comprobarExistenciaTutores(unidadSauce);
					}

					unidadService.updateUnidad(unidadSauce).getBody();

					actualizaEstadoUsuario(unidadExistente, unidadSauce);

				} catch (IllegalArgumentException e) {
					// si se lanza una excepci�n es porque no existe
					try {
						comprobarExistenciaTutores(unidadSauce);
						unidadService.createUnidad(unidadSauce).getBody();
						actualizaEstadoUsuario(null, unidadSauce);
					} catch (Exception ex) {
						createErrorLog(SUBTAREA_UNIDADES, ERROR_INSERTANDO + " unidad id=" + unidadSauce.getIdUnidad()
								+ datosPeticionSauce + unidadSauce, ex);
						procesoSinError = false;
					}
				}

				catch (Exception excep) {
					createErrorLog(SUBTAREA_UNIDADES, ERROR_SINCRONIZANDO + " unidad id=" + unidadSauce.getIdUnidad()
							+ datosPeticionSauce + unidadSauce, excep);
					procesoSinError = false;
				}

				unidadService.incrementarProcesados();
			}

		}

		try {

			SauceSincro sincro = getSauceSincro(SUBTAREA_UNIDADES);

			sincro.setFechaActualizacion(Instant.now());

			if (procesoSinError) {
				sincro.setFechaExito(Instant.now());
			}

			this.updateSauceSincro(sincro);

		} catch (Exception e) {
			log.error("NO EXISTE EL PROCESO DE SINCRONIZACI�N");
		}

		return procesoSinError;
	}

	/**
	 * Comprueba si los tutores de la unidad existen en gepep. Si no existen se le
	 * setean a null para insertar/actualizar la unidad sin tutores. Esto es debido
	 * a que hay veces que vienen unidades con profesores ficticios o con fechas de
	 * cese antiguas (profesores que no tenemos en gepep)
	 * 
	 * @param unidadSauce
	 */
	private void comprobarExistenciaTutores(Unidad unidadSauce) {

		if (unidadSauce.getTutor() != null) {
			try {
				profesorService.getProfesor(unidadSauce.getTutor().getIdProfesor());

			} catch (IllegalArgumentException e) {
				unidadSauce.setTutor(null);
			}

		}

		if (unidadSauce.getTutorAdicional() != null) {
			try {
				profesorService.getProfesor(unidadSauce.getTutorAdicional().getIdProfesor());

			} catch (IllegalArgumentException e) {
				unidadSauce.setTutorAdicional(null);
			}

		}
	}

	private String getFechaFormateada(Date fecha) {

		if (fecha != null) {
			DateFormat df = new SimpleDateFormat(DD_MM_YYYY);
			return df.format(fecha);
		} else
			return null;
	}

	/**
	 * Funci�n de actualizaci�n del estado y centro de los usuarios asociados a
	 * unidades.
	 * 
	 * @param unidadOld
	 * @param unidadNew
	 */
	private void actualizaEstadoUsuario(Unidad unidadOld, Unidad unidadNew) {

		try {

			Optional<CursoAcademico> curso = cursoAcademicoRepository.findByActual(Boolean.TRUE);
			Integer cAnnoActual = curso.isPresent() ? curso.get().getIdAnio() : null;
			// Comprobamos que el curso acad�mico de la unidad sea el actual
			if (unidadNew.getAnio().equals(cAnnoActual)) {
				if (unidadNew.getTutor() != null) {
					String nifTutor = unidadNew.getTutor().getNif();
					// Comprobamos que el tutor asociado a la unidad exista como usuario y
					// actualizamos el estado y centro.
					Optional<Usuario> usuarioOP = usuarioRepository.findOneByDni(nifTutor);
					if (usuarioOP.isPresent()) {
						Centro centro = unidadNew.getOfertaCentro() != null ? unidadNew.getOfertaCentro().getCentro()
								: null;
						Usuario user = usuarioOP.get();
						user.setActivo(Boolean.TRUE);
						// INC000003505385 . Tutores centros concertados
						// si es director y no es concertado , actualizo centro --> Si es concertado, no actualizo.
						if (("ROLE_DIRECTOR".equals(usuarioOP.get().getPerfil().getDescripcion().trim().toUpperCase())))
							if (!usuarioOP.get().getCentro().getConcertado()){
								user.setCentro(centro);
							}//si es cualquier otro rol, actualizo.
						else {
								user.setCentro(centro);
							}
						usuarioRepository.save(user);
					}
				}

				if (unidadOld != null) {

					String nifTutorOld = unidadOld.getTutor() != null ? unidadOld.getTutor().getNif() : null;

					// Comprobamos que el tutor anterior asociado a la unidad exista como usuario.
					Optional<Usuario> usuarioOpOld = usuarioRepository.findOneByDni(nifTutorOld);
					// Si el profesor antiguo NO esta asociado a una unidad del curso acad�mico
					// actual --> se desactiva.
					Optional<Profesor> profesor = profesorRepository.findOneByNif(nifTutorOld);

					if (usuarioOpOld.isPresent()) {
						// Comprobamos que el tutor anterior de la unidad no est� asociado a una unidad
						// del curso acad�mico actual.
						// en el caso de que no est� asociado, modificaremos el centro y el estado del
						// usuario.

						// if (unidadRepository.findAllByTutor(unidadOld.getTutor()).isEmpty()) {
						if (unidadRepository.findAllByTutorAndAnio(profesor, cAnnoActual).isEmpty()) {
							Usuario usuarioOld = usuarioOpOld.get();
							// INC000003505385 . Tutores centros concertados
							// si es director y no es concertado , actualizo centro --> Si es concertado, no actualizo.
							if ( ("ROLE_DIRECTOR".equals(usuarioOld.getPerfil().getDescripcion().trim().toUpperCase()))){
								if (!usuarioOld.getCentro().getConcertado()) {
									usuarioOld.setActivo(Boolean.FALSE);
									usuarioOld.setCentro(null);
								}
							}//si es cualquier otro rol, actualizo.
							else {
								usuarioOld.setActivo(Boolean.FALSE);
								usuarioOld.setCentro(null);
							}
							usuarioRepository.save(usuarioOld);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("Error al actualizar el estado del usuario");
		}

	}

	private String getFechaFormateada(Instant instant) {

		Date fecha = null;

		if (instant != null) {
			fecha = Date.from(instant);
		} else {
			fecha = new Date();
		}

		return getFechaFormateada(fecha);
	}

	/**
	 * Crea una entidad errorLog para indicar el inicio/fin de una subtarea
	 * 
	 * @param subtarea
	 */
	private void createLog(String subtarea, String mensaje) {
		TareaLog log = new TareaLog();
		log.setTarea(TAREA_SAUCE_SINCRO);
		log.setIdTarea(this.idTareaLog);
		log.setSubtarea(subtarea);
		log.setMensaje(mensaje);
		tareaLogService.createTareaLog(log);
	}

	/**
	 * Crea una entidad errorLog con los datos de entrada y la inserta en BD
	 * 
	 * @param subtarea
	 * @param error
	 */
	private void createErrorLog(String subtarea, String error, Exception exception) {
		TareaLog errorLog = new TareaLog();
		errorLog.setTarea(TAREA_SAUCE_SINCRO);
		errorLog.setIdTarea(this.idTareaLog);
		errorLog.setSubtarea(subtarea);
		errorLog.setError(error);
		if (exception != null) {
			errorLog.setException(exception.getClass() + ": " + exception.getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			errorLog.setTraza(sw.toString());
		}
		if (error != null || exception != null)
			log.error(errorLog.toString(), exception);
		tareaLogService.createTareaLog(errorLog);
	}

	/**
	 * Compone un string con los datos de entrada de la peticion al SauceWS para
	 * mostrar en el errorLog en caso de error
	 * 
	 * @param ultimaActualizacion
	 * @param id
	 * @return
	 */
	private String getStringDatosPeticionSauce(int numPeticionSauce, String ultimaActualizacion, int id, Integer cursoAcademico) {
		return " PeticionSauce_" + numPeticionSauce + "=[id=" + id + " maxRegistros=" + maxRegistros
				+ " ultimaActualizacion=" + ultimaActualizacion + " cursoAcademico=" + cursoAcademico + "]";
	}

}

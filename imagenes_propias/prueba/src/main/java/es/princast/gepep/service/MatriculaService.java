package es.princast.gepep.service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.AlumAnexov;
import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.OfertaMatric;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.SeguimientoFinal;
import es.princast.gepep.repository.AlumAnexovRepositoy;
import es.princast.gepep.repository.AlumnoRepository;
import es.princast.gepep.repository.DistribucionRepository;
import es.princast.gepep.repository.MatriculaRepository;
import es.princast.gepep.repository.OfertaMatricRepository;
import es.princast.gepep.repository.OfertasCentroRepository;
import es.princast.gepep.repository.SeguimientoFinalRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MatriculaService {

	private static final String ENTITY_NAME = "matricula";

	@Autowired
	private MatriculaRepository matriculaRepository;
	
	@Autowired
	private OfertasCentroRepository ofertaCentroRepository;
	
	@Autowired
	private AlumnoRepository alumnoRepository;
	
	@Autowired
	private GastoAlumnoService gastoAlumnoService;
	
	@Autowired
	private DistribucionRepository distribucionRepository;
	
    @Autowired
    private SeguimientoFinalRepository seguimientoFinalRepository;
	
	@Autowired
	private EntityManager entityManager; 

	@Autowired
	private OfertaMatricRepository ofertaMatricRepository;
	@Autowired
	private AlumAnexovRepositoy alumAnexovRepository;

	public ResponseEntity<Matricula> createMatricula(final Matricula nuevaMatricula) throws URISyntaxException {
		log.debug("SERVICE request to save Matricula : {}", nuevaMatricula);
		
	 	  if (nuevaMatricula.getIdMatricula() == null) {
			BigInteger nextId = (BigInteger)  entityManager.createNativeQuery("select NEXTVAL('public.sec_matricula')").getSingleResult();
			String generatedId =  nextId.toString();
			nuevaMatricula.setIdMatricula(generatedId);		    	 
		 }	 
		
	 	if (nuevaMatricula.getFechaMatricula() == null )
	 		nuevaMatricula.setFechaMatricula(LocalDate.now());
	 	
	 	if (nuevaMatricula.getAlumno().getIdAlumno() == null) {
	 		Optional<Alumno> nuevoAlumno = alumnoRepository.getOneByNif(nuevaMatricula.getAlumno().getNif());
	 		if (nuevoAlumno.get() !=null)
	 			nuevaMatricula.setAlumno(nuevoAlumno.get());
	 		else
	 			throw new IllegalArgumentException("No existe alumno con ese nif.");
	 	}
	 	
		Matricula result = matriculaRepository.save(nuevaMatricula);
		return ResponseEntity.created(new URI("/api/matriculas/" + result.getIdMatricula()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdMatricula().toString()))
				.body(result);
	}

	public Matricula getMatricula(final String idMatricula) {
		Optional<Matricula> matricula = matriculaRepository.findById(idMatricula);
		if (!matricula.isPresent()) {
			throw new IllegalArgumentException("No existe un matricula con ese identificador.");
		}
		return matricula.get();
	}


	public List<Matricula>  getAllByOfertasCentro(final String idOfertaCentro) {
		OfertasCentro ofertaCentro = ofertaCentroRepository.getOne(idOfertaCentro);
			
		Iterable<Matricula> matriculas = matriculaRepository.findAllByOfertaCentro(ofertaCentro);
		
		return (List<Matricula>) matriculas;
		
	}
	
	public List<Matricula> getAllMatriculas() {
		return matriculaRepository.findAll(); 
	}
	
	 

	public Iterable<OfertaMatric> getListaOfertaMatric (final String centro, Integer anio){
		
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findListadoOferMatric(centro, anio);		
		return  listaOfertaMatric;		
		
	}
	
	public Iterable<OfertaMatric> getListOfertaParaMatriculacion(final String centro, Integer anio){
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findListadoParaMatriculacion(centro, anio);		
		return  listaOfertaMatric;	}
	
	
	public Iterable<OfertaMatric> getListaOfertaMatricTutor (final String centro, Integer anio, String tutor){
		
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findListadoOferMatricTutor(centro, anio,tutor);		
		return  listaOfertaMatric;		
		
	}
	
	public Iterable<OfertaMatric> getListaOfertaMatricByTipoPractica (final String centro, Integer anio, Integer idTipoPractica){
		
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findByTipoPractica(centro, anio,idTipoPractica);		
		return  listaOfertaMatric;		
		
	}
	
	/**
	 * Obtiene el listado de ofertaMatric 
	 */
	public Iterable<OfertaMatric> getListaOfertaMatricByTipoPracticaAndPeriodo (final String centro, Integer anio, Integer idTipoPractica, Integer idPeriodoPractica, boolean sinConvenio){
		
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findByTipoPracticaAndPeriodo(centro, anio,idTipoPractica, idPeriodoPractica);		
				
		return  listaOfertaMatric;		
		
	}
	
	public Iterable<OfertaMatric> getListaOfertaMatricByTipoPracticaAndPeriodoAndUnidad (final String centro, Integer anio, Integer idTipoPractica, Integer idPeriodoPractica, String idUnidad,String idOfercen,Integer curso,String turno){
		
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findByTipoPracticaAndPeriodoAndUnidad(centro, anio,idTipoPractica, idPeriodoPractica,idUnidad,idOfercen,curso,turno);		
				
		return  listaOfertaMatric;		
		
	}
	
	public Iterable<OfertaMatric> getListaOfertaMatricByTipoPracticaAndTutor (final String centro, Integer anio, Integer idTipoPractica, String tutor){
		
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findByTipoPracticaAndTutor(centro, anio,idTipoPractica,tutor);		
		return  listaOfertaMatric;		
		
	}

	/**
	 * Obtiene el listado de ofertaMatric 
	 */
	public Iterable<OfertaMatric> getListaOfertaMatricByTipoPracticaAndPeriodoAndTutor (final String centro, Integer anio, Integer idTipoPractica,Integer idPeriodoPractica, boolean sinConvenio, String tutor){
		
		Iterable<OfertaMatric> listaOfertaMatric = ofertaMatricRepository.findByTipoPracticaAndPeriodoAndTutor(centro, anio,idTipoPractica, idPeriodoPractica, tutor);		
				
		return  listaOfertaMatric;		
		
	}
	
		public List<Matricula> getMatriculasByOfertaCentroAndAnio (final String idOferCen, Integer anio){
		
		OfertasCentro oferCen = ofertaCentroRepository.getOne(idOferCen);		
		Iterable<Matricula> matriculas = matriculaRepository.getMatriculasByOfertaCentroAndAnio(oferCen.getIdOfertaCentro(), anio);		
		return (List<Matricula>) matriculas;
		
		
	}
		
		public List<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnio (final String idOferCen, String idUnidad, Integer anio){
			
			Iterable<Matricula> matriculas = matriculaRepository.getMatriculasByOfertaCentroAndUnidadAndAnio(idOferCen, idUnidad,anio);
			
			return (List<Matricula>) matriculas;
			
			
		}
		
		/**
		 * Obtiene la informacion de matriculas no distribuidas de una unidad. 
		 * Por rendimiento se usa una consulta nativa para evitar la carga de todas las relaciones jpa.
		 * @param idOferCen
		 * @param nombreUnidad
		 * @param anio
		 * @param idPeriodo
		 * @return
		 */
		public List<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoNoDistribuidas (final String idOferCen, String nombreUnidad, Integer anio,Long idPeriodoPractica){
			
			
			String sqlQuery = "  SELECT  "
			
			// Datos de matricula //
			+ " mat.ca_matricula as idMatricula,  "
			+ " mat.nu_anio as anio, "
			+ " mat.cn_x_matricula as xMatricula, "
			+ " mat.dl_desoferta as desOferta, "
			+ " mat.fe_fmatricula as fechaMatricula,"
			+ " mat.fl_esdual as esDual, "	
			+ " mat.dl_desciclo as desCiclo, "
			+ " mat.dc_codciclo as codCiclo, " 
			+ " mat.cn_curso as curso, " 
			+ " mat.ca_turno as turno, "  
			+ " mat.cn_alumnos as numAlumnos, "  
			
			// Datos de OfertaCentro //
			+ " oc.ca_ofercen as idOfertaCentro, "
			
			// Datos de Unidad //
			+ " mat.ca_unidad as idUnidad, "
			+ " uni.ca_nombre as nombreUnidad, "
			
			// Datos de Alumno //
			+ " al.ca_alumno as idAlumno, "
			+ " al.if_nif as nifAlumno, "
			+ " al.dc_nombre as nombreAlumno, "
			+ " al.dl_apellido1 as apellido1, "
			+ " al.dl_apellido2 as apellido2, "
			
			// Datos de Seguimiento //
			+ " seg.cn_seguimiento_final idSeguimientoFinal "
			
			+ " FROM matricula mat "
			+ " inner join alumno al on al.ca_alumno = mat.ca_alumno "
			+ " inner join ofertas_centro oc on mat.ca_ofercen = oc.ca_ofercen "
			+ " inner join unidad uni on uni.ca_unidad = mat.ca_unidad "
			+ " left join seguimiento_final seg on seg.ca_matricula = mat.ca_matricula "
		
			// left join con la subquery de distribuciones en el periodo practica //
			+ " left join ( select dis.ca_matricula, dis.cn_distribucion " 
			+ " 			from distribucion dis "	 		
			+ " 			inner join  distribucion_periodo dispe on dispe.cn_distribucion = dis.cn_distribucion "
			+ " 			inner join  periodo_practica pe on pe.cn_periodo = dispe.cn_periodo and dispe.cn_periodo = :idPeriodoPractica"
			+ "             where dis.fe_fbaja is null "
			+ " ) as dist_en_periodo on dist_en_periodo.ca_matricula = mat.ca_matricula "
			
			+ " WHERE "
			+ " dist_en_periodo.cn_distribucion is null " // Nos quedamos con la matriculas de la unidad que no tienen distribuciones en el periodo de practica // 
			+ " and oc.ca_ofercen = :idOferCen and uni.ca_nombre = :nombreUnidad and mat.nu_anio = :anio "
			
			+ "	order by al.dl_apellido1, al.dl_apellido2, al.dc_nombre asc ";
			
			
			return entityManager.createNativeQuery(sqlQuery, "MatriculaNativeQueryMapping")
			.setParameter("anio", anio)
			.setParameter("idOferCen", idOferCen)
			.setParameter("nombreUnidad", nombreUnidad)
			.setParameter("idPeriodoPractica", idPeriodoPractica)
			.getResultList();
			
		}
		
		
		public List<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndTutor (final String idOferCen, String idUnidad, Integer anio, String idProfesor){
			
			Iterable<Matricula> matriculas = matriculaRepository.getMatriculasByOfertaCentroAndUnidadAndAnioAndTutor(idOferCen, idUnidad,  anio, idProfesor);		
			return (List<Matricula>) matriculas;			
			
		}
		
		
		public List<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioDistribuidas (final String idOferCen, String idUnidad,  Integer anio){		 
			Iterable<Matricula> matriculas = matriculaRepository.getMatriculasByOfertaCentroAndUnidadAndAnioDistribuidas(idOferCen,idUnidad, anio);		
			return (List<Matricula>) matriculas;
			
			
		}
		
		
		
		/**
		 * Obtiene las matriculas distribuidas con la infomacion de seguimiento final
		 */
		public List<Matricula> getMatriculasSeguimientosByOfertaCentroAndUnidadAndAnioAndPeriodoDistribuidas (final String idOferCen, String idUnidad,  Integer anio,  Long idPeriodoPractica){		 
			
			Iterable<Matricula> matriculasDistribuidas = matriculaRepository.getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoDistribuidas(idOferCen,idUnidad, anio, idPeriodoPractica);		
		
			// consultamos seguimiento de matricula //
			for (Matricula matricula : matriculasDistribuidas) {
					
				Optional<SeguimientoFinal> seguimientoFinal = seguimientoFinalRepository.getOneByMatricula(matricula);
				
				if(seguimientoFinal.isPresent()) {
					matricula.setSeguimientoFinal(seguimientoFinal.get());
				}

			}	
			return (List<Matricula>) matriculasDistribuidas;
			
		}
		
		public List<Matricula> getMatriculasByOfertaCentroAndAnioAndTutorDistribuidas (final String idOferCen, Integer anio, String tutor){		 
			Iterable<Matricula> matriculas = matriculaRepository.getMatriculasByOfertaCentroAndAnioAndTutorDistribuidas(idOferCen, anio,tutor);		
			return (List<Matricula>) matriculas;
			
			
		}
		
		public List<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndTutorDistribuidas (final String idOferCen,  String idUnidad, Integer anio, String tutor){		 
			Iterable<Matricula> matriculas = matriculaRepository.getMatriculasByOfertaCentroAndUnidadAndAnioAndTutorDistribuidas(idOferCen,idUnidad, anio,tutor);		
			return (List<Matricula>) matriculas;
			
			
		}
		
		
		/**
		 * Obtiene las matriculas distribuidas con la infomacion de seguimiento final
		 */
		public List<Matricula> getMatriculasSeguimientosByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorDistribuidas(
				final String idOferCen, String idUnidad, Integer anio, Long idPeriodoPractica, String tutor) {
			Iterable<Matricula> matriculasDistribuidas = matriculaRepository
					.getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorDistribuidas(idOferCen, idUnidad, anio,
							idPeriodoPractica, tutor);
	
			// consultamos seguimiento de matricula //
			for (Matricula matricula : matriculasDistribuidas) {
	
				Optional<SeguimientoFinal> seguimientoFinal = seguimientoFinalRepository.getOneByMatricula(matricula);
	
				if (seguimientoFinal.isPresent()) {
					matricula.setSeguimientoFinal(seguimientoFinal.get());
				}
	
			}
			return (List<Matricula>) matriculasDistribuidas;
		}
		
		/**
		 * Obtiene las matriculas No distribuidas con la infomacion de seguimiento final
		 */
		public List<Matricula> getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorNoDistribuidas(
				final String idOferCen, String idUnidad, Integer anio, Long idPeriodoPractica, String tutor) {
			Iterable<Matricula> matriculas = matriculaRepository
					.getMatriculasByOfertaCentroAndUnidadAndAnioAndPeriodoAndTutorNoDistribuidas(idOferCen, idUnidad, anio,
							idPeriodoPractica, tutor);
	
			// consultamos seguimiento de matricula //
			for (Matricula matricula : matriculas) {
	
				Optional<SeguimientoFinal> seguimientoFinal = seguimientoFinalRepository.getOneByMatricula(matricula);
	
				if (seguimientoFinal.isPresent()) {
					matricula.setSeguimientoFinal(seguimientoFinal.get());
				}
	
			}
	
			return (List<Matricula>) matriculas;
		}
		

	public ResponseEntity<Matricula> updateMatricula(final Matricula matriculaModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Matricula : {}", matriculaModificado);
		if (matriculaModificado.getIdMatricula() == null) {
			return createMatricula(matriculaModificado);
		}

		Matricula result = matriculaRepository.save(matriculaModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, matriculaModificado.getIdMatricula().toString()))
				.body(result);

	}

	public void deleteMatricula(final String idMatricula) {
		Optional<Matricula> matricula = matriculaRepository.findById(idMatricula);
		if (!matricula.isPresent()) {
			throw new IllegalArgumentException("No existe un matricula con ese identificador.");
		}
		
		// Borramos la distribucion asociada //
		
		Optional<Distribucion> distribucion = distribucionRepository.getOneByMatricula(matricula.get());
		if(distribucion.isPresent()) {
			distribucionRepository.delete(distribucion.get());
		}
				
		
		// Borramos el seguimiento asociado //
		
		Optional<SeguimientoFinal> seguimiento = seguimientoFinalRepository.getOneByMatricula(matricula.get());
		if(seguimiento.isPresent()) {
			seguimientoFinalRepository.delete(seguimiento.get());
		}
		
		
		matriculaRepository.deleteById(idMatricula);
	}
	
	public Iterable<AlumAnexov> getListaAlumByAnexo ( String idCentro, Integer curso, String idOferta,Integer idPeriodoPractica){
		
		Iterable<AlumAnexov> listaAlum =  alumAnexovRepository.findAlumByAnexo(idCentro, curso, idOferta, idPeriodoPractica);		
		return  listaAlum;		
		
	}
	
	public Iterable<AlumAnexov> getListaAlumByAnexoByTutor ( String idCentro, Integer curso, String idOferta,Integer idPeriodoPractica, String tutor){
		
		Iterable<AlumAnexov> listaAlum =  alumAnexovRepository.findAlumByAnexoByTutor(idCentro, curso, idOferta, idPeriodoPractica, tutor);		
		return  listaAlum;		
		
	}
	
	public Iterable<AlumAnexov> getListaAlumByUnidadAnexo ( String idCentro, Integer curso, String idOferta,Integer idPeriodoPractica, String idUnidad){
		
		Iterable<AlumAnexov> listaAlum =  alumAnexovRepository.findAlumByUnidadAnexo(idCentro, curso, idOferta, idPeriodoPractica, idUnidad);		
		return  listaAlum;		
		
	}
	
	public Iterable<AlumAnexov> getListaAlumByUnidadAnexoByTutor ( String idCentro, Integer curso, String idOferta,Integer idPeriodoPractica, String idUnidad, String tutor){
		
		Iterable<AlumAnexov> listaAlum =  alumAnexovRepository.findAlumByUnidadAnexoByTutor(idCentro, curso, idOferta, idPeriodoPractica, idUnidad , tutor);		
		return  listaAlum;		
		
	}
	
	public List<Matricula> findByAlumno(final String idAlumno) {
		Optional<Alumno> alumno = alumnoRepository.findById(idAlumno);
		if (!alumno.isPresent()) {
			throw new IllegalArgumentException("No existe un alumno con ese identificador.");
		}
		List<Matricula> listaMatriculas = matriculaRepository.findAllByAlumnoOrderByAnioAscCursoAsc(alumno.get());
		
		return listaMatriculas;
		
	}
	
	public List<Matricula> findByAlumnoAndAnio(final String idAlumno,final Integer anio) {
		Optional<Alumno> alumno = alumnoRepository.findById(idAlumno);
		if (!alumno.isPresent()) {
			throw new IllegalArgumentException("No existe un alumno con ese identificador.");
		}
		List<Matricula> listaMatriculas = matriculaRepository.findAllByAlumnoAndAnioOrderByCursoAsc(alumno.get(),anio);
		
		return listaMatriculas;
		
	}
	
	public List<Matricula> findByAlumnoAndAnioAndCentro(final String idAlumno,final Integer anio,final String idCentro) {
		Optional<Alumno> alumno = alumnoRepository.findById(idAlumno);
		if (!alumno.isPresent()) {
			throw new IllegalArgumentException("No existe un alumno con ese identificador.");
		}
		List<Matricula> listaMatriculas = matriculaRepository.getMatriculasByAlumnoAndAnioAndCentro(idAlumno,anio,idCentro);
		
		return listaMatriculas;
		
	}
	
 
	
	

}

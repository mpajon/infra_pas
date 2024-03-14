package es.princast.gepep.service.saucesincro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.princast.gepep.service.dto.AlumnoDTO;
import es.princast.gepep.service.dto.CentroDTO;
import es.princast.gepep.service.dto.CicloDTO;
import es.princast.gepep.service.dto.EnsenanzaDTO;
import es.princast.gepep.service.dto.MatriculaDTO;
import es.princast.gepep.service.dto.OfertaCentroDTO;
import es.princast.gepep.service.dto.OfertaDTO;
import es.princast.gepep.service.dto.ProfesorDTO;
import es.princast.gepep.service.dto.UnidadDTO;
import es.princast.sauce._1_0.sauce_wsdl.BuscaCentrosCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaCiclosCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaEnsenanzasCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaMatriculasCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaOfertasCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaOfertasCentrosCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaProfesoresCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.BuscaUnidadesCCFFErrorMensaje;
import es.princast.sauce._1_0.sauce_wsdl.SaucePuerto;
import es.princast.registro.sauce._1_0.sauce.TAlumnoCCFF;
import es.princast.registro.sauce._1_0.sauce.TBuscaAlumnosCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaAlumnosCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaCentrosCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaCentrosCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaCiclosCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaCiclosCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaEnsenanzasCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaEnsenanzasCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaMatriculasCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaMatriculasCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaOfertasCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaOfertasCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaOfertasCentrosCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaOfertasCentrosCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaProfesoresCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaProfesoresCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TBuscaUnidadesCCFFPeticion;
import es.princast.registro.sauce._1_0.sauce.TBuscaUnidadesCCFFRespuesta;
import es.princast.registro.sauce._1_0.sauce.TCentroCompleto;
import es.princast.registro.sauce._1_0.sauce.TCiclo;
import es.princast.registro.sauce._1_0.sauce.TEnsenanza;
import es.princast.registro.sauce._1_0.sauce.TMatricula;
import es.princast.registro.sauce._1_0.sauce.TOferta;
import es.princast.registro.sauce._1_0.sauce.TOfertaCentro;
import es.princast.registro.sauce._1_0.sauce.TProfesor;
import es.princast.registro.sauce._1_0.sauce.TUnidadCompleta;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SauceService {

	@Autowired
	private SaucePuerto saucePuerto;

	public List<AlumnoDTO> getListaAlumnosCCFF(int id, int maxRegistros, String ultimaActualizacion, Integer cursoAcademico) throws Exception {
		List<AlumnoDTO> alumnos = new ArrayList<>();

		TBuscaAlumnosCCFFPeticion peticion = new TBuscaAlumnosCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);
		peticion.setCursoAcademico(cursoAcademico);

		TBuscaAlumnosCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaAlumnosCCFF(peticion);
			List<TAlumnoCCFF> listaAlumnos = respuesta.getAlumnos().getAlumno();
			for (TAlumnoCCFF alumno : listaAlumnos) {
				alumnos.add(AlumnoDTO.builder().idAlumno(alumno.getIdAlumno()).nombre(alumno.getNombre())
						.apellido1(alumno.getApellido1()).apellido2(alumno.getApellido2()).sexo(alumno.getSexo())
						.email(alumno.getEmail()).codigoPostal(alumno.getDireccion().getCodigoPostal())
						.via(alumno.getDireccion().getNombreVia()).numVia(alumno.getDireccion().getNumeroVia())
						.letra(alumno.getDireccion().getLetra()).piso(alumno.getDireccion().getPiso())
						.localidad(alumno.getDireccion().getLocalidad().getNombre())
						.municipio(alumno.getDireccion().getMunicipio().getNombre())
						.provincia(alumno.getDireccion().getProvincia().getNombre())
						.pais(alumno.getDireccion().getPais().getNombre()).telefono(alumno.getTelefono())
						.movil(alumno.getTelefonoUrgencias()).fechaNacimiento(alumno.getFechaNacimiento())
						.nif(alumno.getNumIde()).esSauce(Boolean.TRUE).esProfesor(Boolean.FALSE)
						.numEscolar(alumno.getNumEscolar()).build());

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 

		return alumnos;
	}

	public List<ProfesorDTO> getListaProfesoresCCFF(int id, int maxRegistros, String ultimaActualizacion, Integer cursoAcademico) throws BuscaProfesoresCCFFErrorMensaje {
		List<ProfesorDTO> profesores = new ArrayList<>();

		TBuscaProfesoresCCFFPeticion peticion = new TBuscaProfesoresCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);
		peticion.setCursoAcademico(cursoAcademico);

		TBuscaProfesoresCCFFRespuesta respuesta = null;
		try {
			respuesta = saucePuerto.buscaProfesoresCCFF(peticion);
		} catch (Exception e) {
			log.error("getListaProfesoresCCFF - saucePuerto.buscaProfesoresCCFF ", e);
			throw e;
		} 
		try {
			if (respuesta != null && respuesta.getProfesores() != null) {
				List<TProfesor> listaProfesores = respuesta.getProfesores().getProfesores();
				for (TProfesor profesor : listaProfesores) {
					profesores.add(ProfesorDTO.builder().idEmpleado(profesor.getIdEmpleado())
							.nombre(profesor.getNombre()).apellido1(profesor.getApellido1())
							.apellido2(profesor.getApellido2()).nif(profesor.getNif())
							.fechaNacimiento(profesor.getFechaNacimiento()).fechaBaja(profesor.getFechaBaja())
							.sexo(profesor.getSexo()).provincia(profesor.getDireccion().getProvincia().getNombre())
							.municipio(profesor.getDireccion().getMunicipio().getNombre())
							.localidad(profesor.getDireccion().getLocalidad().getNombre())
							.codigoPostal(profesor.getDireccion().getCodigoPostal())
							.via(profesor.getDireccion().getNombreVia()).telefono(profesor.getTelefono())
							.movil(profesor.getMovil()).cuentaSS(profesor.getCuentaSS()).deSauce(Boolean.TRUE).build());
				}
			}
		} catch (Exception e) {
			log.error("getListaProfesoresCCFF - builder ProfesorDTO " , e);
			throw e;
		} 

		return profesores;
	}

	public List<EnsenanzaDTO> getListaEnsenanzasCCFF(int id, int maxRegistros, String ultimaActualizacion) throws BuscaEnsenanzasCCFFErrorMensaje {
		List<EnsenanzaDTO> ensenanzas = new ArrayList<>();

		TBuscaEnsenanzasCCFFPeticion peticion = new TBuscaEnsenanzasCCFFPeticion( );
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);
		
		TBuscaEnsenanzasCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaEnsenanzasCCFF(peticion);
			if (respuesta != null && respuesta.getEnsenanzas() != null) {
				List<TEnsenanza> listaEnsenanzas = respuesta.getEnsenanzas().getEnsenanza();
				for (TEnsenanza ensenanza : listaEnsenanzas) {					
					ensenanzas.add(EnsenanzaDTO.builder().idEtapa(ensenanza.getIdEtapa())
							.nombreEtapa(ensenanza.getNombreEtapa()).descEtapa(ensenanza.getDescEtapa())
							.fechaActualiza(ensenanza.getFechaActualiza()).fechaCreacion(ensenanza.getFechaCreacion())
							.fechaIncioVigencia(ensenanza.getFechaIncioVigencia())
							.fechaFinVigencia(ensenanza.getFechaFinVigencia()).nivel(ensenanza.getNivel())
							.vigente(ensenanza.getVigente().equals("S") ? Boolean.TRUE : Boolean.FALSE)
							.usuActualiza(ensenanza.getUsuActualiza()).usuCreacion(ensenanza.getUsuCreacion()).build());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return ensenanzas;
	}

	public List<CicloDTO> getListaCiclosCCFF(int id, int maxRegistros, String ultimaActualizacion) throws BuscaCiclosCCFFErrorMensaje {
		List<CicloDTO> ciclos = new ArrayList<>();

		TBuscaCiclosCCFFPeticion peticion = new TBuscaCiclosCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);

		TBuscaCiclosCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaCiclosCCFF(peticion);
			if (respuesta != null && respuesta.getCiclos() != null) {
				List<TCiclo> listaCiclos = respuesta.getCiclos().getCiclo();
				for (TCiclo ciclo : listaCiclos) {
					ciclos.add(CicloDTO.builder().idCiclo(ciclo.getIdCiclo()).idEtapa(ciclo.getIdEtapa().toString()).codigo(ciclo.getCodigo())
							.nombre(ciclo.getNombre()).familia(ciclo.getFamilia()).fechaBaja(ciclo.getFechaBaja())
							.build());
				}
			}
		} catch (BuscaCiclosCCFFErrorMensaje e) {			
			e.printStackTrace();
			throw e;
		}

		return ciclos;
	}

	public List<OfertaDTO> getListaOfertasCCFF(int id, int maxRegistros, String ultimaActualizacion) throws BuscaOfertasCCFFErrorMensaje {
		List<OfertaDTO> ofertas = new ArrayList<>();

		TBuscaOfertasCCFFPeticion peticion = new TBuscaOfertasCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);
		
		TBuscaOfertasCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaOfertasCCFF(peticion);
			if (respuesta != null && respuesta.getOfertas() != null) {
				List<TOferta> listaOfertas = respuesta.getOfertas().getOferta();
				for (TOferta oferta : listaOfertas) {
					ofertas.add(OfertaDTO.builder().idOferta(oferta.getIdOferta())
							.idCiclo(oferta.getIdCiclo()).idEtapa(oferta.getIdEtapa()).nombre(oferta.getNombre()).vigente(oferta.isVigente())
							.anioFin(oferta.getAnioFin()).anioInicio(oferta.getAnioInicio())
							.abreviatura(oferta.getAbreviatura()).curso(oferta.getCurso()).build());
				}
			}
		} catch (BuscaOfertasCCFFErrorMensaje e) {		
			e.printStackTrace();
			throw e;
		}
		
		return ofertas;
	    
	}

	public List<CentroDTO> getListaCentrosCCFF(int id, int maxRegistros, String ultimaActualizacion) throws BuscaCentrosCCFFErrorMensaje {
		List<CentroDTO> centros = new ArrayList<>();

		TBuscaCentrosCCFFPeticion peticion = new TBuscaCentrosCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);

		TBuscaCentrosCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaCentrosCCFF(peticion);
			if (respuesta != null && respuesta.getCentros() != null) {
				List<TCentroCompleto> listaCentros = respuesta.getCentros().getCentro();
				for (TCentroCompleto centro : listaCentros) {
					centros.add(CentroDTO.builder().idCentro(centro.getIdCentro())
							.codigoCentro(centro.getCodigoCentro().toString()).nombreCentro(centro.getNombreCentro())
							.cifCentro(centro.getCifCentro()).mail(centro.getMail()).fax(centro.getFax())
							.idMunicipio(centro.getDireccion().getMunicipio().getIdMunicipio())
							.localidad(centro.getDireccion().getLocalidad().getNombre())
							.direccion(centro.getDireccion().getNombreVia())
							.codigoPostal(centro.getDireccion().getCodigoPostal()).tipoCentro(centro.getTipoCentro())
							.fechaBaja(centro.getFechaBaja()).director(centro.getNombreDirector())
							.nifDirector(centro.getNifDirector()).esConcertado(centro.isEsConcertado())
							.telefono(centro.getTelefono()).secretario(centro.getSecretario())
							.jefeEstudios(centro.getJefeEstudios()).build());

				}
			}
		} catch (BuscaCentrosCCFFErrorMensaje e) {		
			e.printStackTrace();
			throw e;
		}

		return centros;
	}

	public List<OfertaCentroDTO> getListaOfertasCentrosCCFF(int id, int maxRegistros, String ultimaActualizacion) throws BuscaOfertasCentrosCCFFErrorMensaje {
		
		List<OfertaCentroDTO> ofertasCentros = new ArrayList<>();

		TBuscaOfertasCentrosCCFFPeticion peticion = new TBuscaOfertasCentrosCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);

		TBuscaOfertasCentrosCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaOfertasCentrosCCFF(peticion);
			if (respuesta != null && respuesta.getOfertasCentros() != null) {
				List<TOfertaCentro> listaOfertasCentro = respuesta.getOfertasCentros().getOferta();
				for (TOfertaCentro ofertaCentro : listaOfertasCentro) {
					ofertasCentros.add(OfertaCentroDTO.builder().idCentro(ofertaCentro.getIdCentro().toString())
							.idOferta(ofertaCentro.getIdOferta().toString()).idOfertaCentro(ofertaCentro.getIdOfertaCentro())
							.vigente(ofertaCentro.isVigente()).build());
				}
			}
		} catch (BuscaOfertasCentrosCCFFErrorMensaje e) {
			e.printStackTrace();
			throw e;
		}

		return ofertasCentros;
	}
	
	public List<MatriculaDTO> getListaMatriculasCCFF(int id, int maxRegistros, String ultimaActualizacion, Integer cursoAcademico) throws BuscaMatriculasCCFFErrorMensaje {

		List<MatriculaDTO> matriculas = new ArrayList<>();

		TBuscaMatriculasCCFFPeticion peticion = new TBuscaMatriculasCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);
		peticion.setCursoAcademico(cursoAcademico);

		TBuscaMatriculasCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaMatriculasCCFF(peticion);
			if (respuesta != null && respuesta.getMatriculas() != null) {
				List<TMatricula> listaMatriculas = respuesta.getMatriculas().getMatricula();
				for (TMatricula matricula : listaMatriculas) {
					matriculas.add(MatriculaDTO.builder().idMatricula(matricula.getIdMatricula()).xMatricula(matricula.getIdMatricula())
							.idAlumno(matricula.getIdAlumno().toString()).idCentro(matricula.getIdCentro().toString())
							.idOferta(matricula.getIdOferta().toString()).idOfertaCentro(matricula.getIdOfertaCentro().toString())
							.codCiclo(matricula.getCodCiclo()).desCiclo(matricula.getDesCiclo())
							.unidad(matricula.getUnidad()).turno(matricula.getTurno())
							.desOferta(matricula.getDesOferta()).fechaMatricula(matricula.getFechaMatricula())
							.esDual(matricula.isEsDual()).anio(matricula.getAnio()).curso(matricula.getCurso())
							.tutor(matricula.getTutor() != null ? matricula.getTutor().toString() : null ).numAlumnos(matricula.getNumAlumnos()).build());
				}

			}
		} catch (BuscaMatriculasCCFFErrorMensaje e) {
			e.printStackTrace();
			throw e;
		}
		return matriculas;
	}
	
	public List<UnidadDTO> getListaUnidadesCCFF(int id, int maxRegistros, String ultimaActualizacion, Integer cursoAcademico)
			throws BuscaUnidadesCCFFErrorMensaje {

		List<UnidadDTO> unidades = new ArrayList<>();

		TBuscaUnidadesCCFFPeticion peticion = new TBuscaUnidadesCCFFPeticion();
		peticion.setFechaUltimaActualizacion(ultimaActualizacion);
		peticion.setId(id);
		peticion.setMaxRegistros(maxRegistros);
		peticion.setCursoAcademico(cursoAcademico);

		TBuscaUnidadesCCFFRespuesta respuesta;
		try {
			respuesta = saucePuerto.buscaUnidadesCCFF(peticion);
			if (respuesta != null && respuesta.getUnidades() != null) {
				List<TUnidadCompleta> listaUnidades = respuesta.getUnidades().getUnidad();
				for (TUnidadCompleta unidad : listaUnidades) {
					unidades.add(UnidadDTO.builder().idUnidad(unidad.getIdUnidad()).nombre(unidad.getNombre())
							.anio(unidad.getAnio()).capacidad(unidad.getCapacidad()).curso(unidad.getCurso())
							.idOfertaCentro(unidad.getIdOfertaCentro()).numAlumnos(unidad.getNumAlumnos())
							.tutor(unidad.getTutor()).tutorAdicional(unidad.getTutorAdicional()).turno(unidad.getTurno()).build());
				}

			}
		} catch (BuscaUnidadesCCFFErrorMensaje e) {
			e.printStackTrace();
			throw e;
		}
		return unidades;
	}

}

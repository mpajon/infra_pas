package es.princast.gepep.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.Matricula;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.Unidad;
import es.princast.gepep.service.util.Constantes;
import es.princast.gepep.web.rest.util.FormatUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatriculaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer idMatricula;
	String idAlumno;
	String idCentro;
	String idOferta;
	String idOfertaCentro;
	String codCiclo;
	String desCiclo;
	String unidad;
	String turno;	
	String desOferta;
	String fechaMatricula;
	Integer anio; 
	Integer curso;
	Integer numAlumnos; 
	String tutor;
	Boolean esDual;
	Integer xMatricula;
	
	
	public Matricula toVO() {
		Matricula matricula = new Matricula();
		
		Alumno alumno = new Alumno();
		alumno.setIdAlumno(FormatUtil.convertIdSauceToGepep(this.idAlumno));
		matricula.setAlumno(alumno);
		OfertasCentro ofertaCentro = new OfertasCentro();
		ofertaCentro.setIdOfertaCentro(FormatUtil.convertIdSauceToGepep(this.idOfertaCentro));
		matricula.setOfertaCentro(ofertaCentro);
		matricula.setCodCiclo(this.codCiclo);
		matricula.setDesCiclo(this.desCiclo);
		Unidad unidad = new Unidad();
		unidad.setIdUnidad(FormatUtil.convertIdSauceToGepep(this.unidad));
		matricula.setUnidad(unidad);
		matricula.setTurno(this.turno);
		matricula.setDesOferta(this.desOferta);
		matricula.setAnio(this.anio);
		matricula.setCurso(this.curso);
		matricula.setNumAlumnos(this.numAlumnos);
		matricula.setEsDual(this.esDual);
		if (this.tutor != null) {
			Profesor tutor = new Profesor();
			tutor.setIdProfesor(FormatUtil.convertIdSauceToGepep(this.tutor));
			// matricula.setTutor(tutor);	
		}		
		matricula.setXMatricula(this.xMatricula);
	
		if (this.fechaMatricula != null) {
    		matricula.setFechaMatricula(LocalDate.parse(this.fechaMatricula, Constantes.DATE_FORMAT));
		}  
		return matricula;
	}
}


package es.princast.gepep.service.dto;

import java.io.Serializable;

import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.Unidad;
import es.princast.gepep.web.rest.util.FormatUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnidadDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer idUnidad; 
	String nombre;
	Integer anio;		
	Integer idOfertaCentro;
	Integer tutor;		
	Integer tutorAdicional;		
	Integer numAlumnos;	
	Integer capacidad;		
	Integer curso;
	String turno;
	Boolean deSauce;
		
	public Unidad toVO() {
	    Unidad unidad = new Unidad();
	    
	    unidad.setNombre(this.nombre);
	    unidad.setAnio(this.anio);
		
		OfertasCentro ofertaCentro = new OfertasCentro();
		ofertaCentro.setIdOfertaCentro(FormatUtil.convertIdSauceToGepep(this.idOfertaCentro));		
		unidad.setOfertaCentro(ofertaCentro);	
		if (this.tutor != null) {
			Profesor tutor = new Profesor();
			tutor.setIdProfesor(FormatUtil.convertIdSauceToGepep(this.tutor));
			unidad.setTutor(tutor);	
		}	
		if (this.tutorAdicional != null) {
			Profesor tutorAdicional = new Profesor();
			tutorAdicional.setIdProfesor(FormatUtil.convertIdSauceToGepep(this.tutorAdicional));
			unidad.setTutorAdicional(tutorAdicional);	
		}
		unidad.setNumAlumnos(this.numAlumnos);
		unidad.setCapacidad(this.capacidad);
		unidad.setCurso(this.curso);		
		unidad.setTurno(this.turno);
		unidad.setDeSauce(Boolean.TRUE);
		  
		return unidad;
	}
}


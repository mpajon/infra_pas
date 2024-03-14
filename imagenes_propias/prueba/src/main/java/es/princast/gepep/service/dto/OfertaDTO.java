package es.princast.gepep.service.dto;

import java.io.Serializable;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.web.rest.util.FormatUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfertaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	String abreviatura;
	Integer anioFin; 
	Integer anioInicio;
	Integer curso;	
	Integer idCiclo;
	Integer idEtapa;
	Integer idOferta;
	String nombre;
	Boolean vigente;
	
	
	public OfertaFormativa toVO() {
		OfertaFormativa oferta = new OfertaFormativa();
		
		oferta.setCodigo(this.abreviatura);
		oferta.setAnioFin(this.anioFin);
		oferta.setAnioInicio(this.anioInicio);
		oferta.setCurso(this.curso);
		Ciclo ciclo = new Ciclo();
		ciclo.setIdCiclo(FormatUtil.convertIdCicloSauceToGepep(this.idCiclo.toString(), this.idEtapa.toString()));
		oferta.setCiclo(ciclo);
		oferta.setNombre(this.nombre);
		oferta.setVigente(this.vigente);
		oferta.setDeSauce(Boolean.TRUE);
		
		return oferta;
	}
	
}

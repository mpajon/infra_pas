package es.princast.gepep.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.domain.NivelEducativo;
import es.princast.gepep.service.util.Constantes;
import es.princast.gepep.web.rest.util.FormatUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnsenanzaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Integer idEtapa;
	String nombreEtapa;
	String descEtapa;
	String fechaIncioVigencia;
	String fechaFinVigencia;
	Integer nivel;
	String fechaCreacion;
	String usuCreacion;
	String fechaActualiza;
	String usuActualiza;
	Boolean vigente;
	Boolean deSauce;

	public Ensenanza toVO() {
		Ensenanza ensenanza = new Ensenanza();		
				
		ensenanza.setNombre(this.nombreEtapa);
		ensenanza.setDescripcion(this.descEtapa);
		if (this.fechaIncioVigencia != null) {
			ensenanza.setFechaInicioVigencia(LocalDate.parse(this.fechaIncioVigencia, Constantes.DATE_FORMAT));
		}
		if (this.fechaFinVigencia != null) {
			ensenanza.setFechaInicioVigencia(LocalDate.parse(this.fechaFinVigencia, Constantes.DATE_FORMAT));
		}
		/*cambio diciembre RFJ*/
		//ensenanza.setNivel(this.nivel) 
		if(this.nivel!=null) {
        		NivelEducativo nivelEducativo = new NivelEducativo();
        		nivelEducativo.setIdNivel(this.nivel);
        		ensenanza.setNivel(nivelEducativo);
		}
		
		ensenanza.setVigente(this.vigente);
		
		ensenanza.setDeSauce(Boolean.TRUE);
		
		return ensenanza;
	}

}
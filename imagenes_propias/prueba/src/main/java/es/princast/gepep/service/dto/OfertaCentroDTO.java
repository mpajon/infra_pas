package es.princast.gepep.service.dto;

import java.io.Serializable;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.domain.OfertasCentro;
import es.princast.gepep.web.rest.util.FormatUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfertaCentroDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Integer idOfertaCentro;
	String idOferta;
	String idCentro;
    Boolean vigente;
    
    
    public OfertasCentro toVO() {
    	OfertasCentro ofertaCentro = new OfertasCentro();
    	
    	OfertaFormativa formativa = new OfertaFormativa();
    	formativa.setIdOfertaFormativa(FormatUtil.convertIdSauceToGepep(this.idOferta));
    	ofertaCentro.setOferta(formativa);
    	Centro centro = new Centro();
    	centro.setIdCentro(FormatUtil.convertIdSauceToGepep(this.idCentro));
    	ofertaCentro.setCentro(centro);    			
    	ofertaCentro.setVigente(this.vigente);
    		
		return ofertaCentro;
	}
}

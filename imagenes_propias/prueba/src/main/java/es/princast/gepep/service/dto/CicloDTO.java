package es.princast.gepep.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.Ensenanza;
import es.princast.gepep.service.util.Constantes;
import es.princast.gepep.web.rest.util.FormatUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CicloDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer idCiclo;
	String idEtapa;
	String codigo;
	String nombre;
	String familia;
	String fechaBaja;	
	
	
	public Ciclo toVO() {
		Ciclo ciclo = new Ciclo();
	
		Ensenanza ensenanza = new Ensenanza();
		ensenanza.setIdEnsenanza(FormatUtil.convertIdSauceToGepep(this.idEtapa));
		ciclo.setEnsenanza(ensenanza);
		
		ciclo.setCodigo(this.codigo);
		ciclo.setNombre(this.nombre);
		ciclo.setFamilia(this.familia);
		if (this.fechaBaja != null) {
			ciclo.setFechaBaja(LocalDate.parse(this.fechaBaja, Constantes.DATE_FORMAT));
		}
		ciclo.setDeSauce(Boolean.TRUE);
				
		return ciclo;
	}
}

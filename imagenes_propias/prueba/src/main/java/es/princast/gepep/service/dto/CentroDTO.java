package es.princast.gepep.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Municipio;
import es.princast.gepep.service.util.Constantes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CentroDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Integer idCentro;
    String codigoCentro;
    String nombreCentro;
    String cifCentro;  
    String mail;
    String fax;    
    int idMunicipio;
    String localidad;
    String direccion;
    String codigoPostal;    
    String tipoCentro;
    String telefono;
    String fechaBaja;   
    String nifDirector;
    String director;  
    Boolean esConcertado;
    String secretario;
    String jefeEstudios;
    
    public Centro toVO() {
    	Centro centro = new Centro();
		
    	centro.setCodigo(this.codigoCentro);
    	centro.setNombre(this.nombreCentro);
    	centro.setCif(this.cifCentro);
    	centro.setEmail(this.mail);
    	centro.setFax(this.fax);
    	Municipio municipio = new Municipio();
    	municipio.setIdMunicipio(Long.valueOf(this.idMunicipio));
    	centro.setMunicipio(municipio);
    	centro.setLocalidad(this.localidad);
    	centro.setDireccion(this.direccion);
    	centro.setCp(this.codigoPostal);
    	centro.setTipoCentro(this.tipoCentro);
    	centro.setTelefono(this.telefono);
    	centro.setNifDirector(this.nifDirector);
    	centro.setDirector(this.director);
    	centro.setConcertado(this.esConcertado);    	
    	if (this.fechaBaja != null) {
			centro.setFechaBaja(LocalDate.parse(this.fechaBaja, Constantes.DATE_FORMAT));
		}   
    	centro.setDeSauce(Boolean.TRUE);
    	centro.setSecretario(this.secretario);
    	centro.setJefeEstudios(this.jefeEstudios);
    		
		return centro;
	}
  
}

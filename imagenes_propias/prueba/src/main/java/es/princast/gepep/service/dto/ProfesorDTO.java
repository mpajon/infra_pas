package es.princast.gepep.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import es.princast.gepep.domain.Profesor;
import es.princast.gepep.service.util.Constantes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfesorDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Integer idEmpleado;
    String nombre;
    String apellido1;
    String apellido2;     
    String nif;    
    String fechaNacimiento;  
    String sexo;
    String provincia;
    String municipio;
    String localidad;
    String via;   
    String codigoPostal;  
    String email;
    String movil;
    String telefono;   
    String fechaBaja;
    String cuentaSS; 
    Boolean deSauce;
    
    public Profesor toVO() {
    	Profesor profesor = new Profesor();
    	
    	profesor.setDeSauce(Boolean.TRUE);
    	profesor.setApellido1(this.apellido1);
    	profesor.setApellido2(this.apellido2);
    	profesor.setNombre(this.nombre);
    	profesor.setNif(this.nif);
    	profesor.setSexo(this.sexo);
    	profesor.setProvincia(this.provincia);
    	profesor.setMunicipio(this.municipio);
    	profesor.setLocalidad(this.localidad);
    	profesor.setDireccion(this.via);
    	profesor.setCp(this.codigoPostal);
    	profesor.setEmail(this.email);
    	profesor.setMovil(this.movil);
    	profesor.setTelefono(this.telefono);
    	profesor.setCuentaSS(this.cuentaSS);
    	if (this.fechaBaja != null) {
    		profesor.setFechaBaja(LocalDate.parse(this.fechaBaja, Constantes.DATE_FORMAT));
		}  
    	
    	if (this.fechaNacimiento != null) {
    		profesor.setFechaNacimiento(LocalDate.parse(this.fechaNacimiento, Constantes.DATE_FORMAT));
		}   	
    		
		return profesor;
	}
}

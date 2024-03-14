package es.princast.gepep.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.service.util.Constantes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlumnoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Integer idAlumno;
    String nombre;
    String apellido1;
    String apellido2;  
    String sexo;
    String nif;    
    String fechaNacimiento;  
    String pais;
    String provincia;
    String municipio;
    String localidad;
    String via;
    Integer numVia;
    String piso;
    String letra;
    String codigoPostal;
    String email;
    String movil;
    String telefono;
    String tipoFiscal;   
    String fechaAlta;
    Boolean esSauce;
    Boolean esProfesor; 
    String numEscolar;
    
    
	public Alumno toVO() {
		Alumno alumno = new Alumno();

		alumno.setNombre(this.nombre);
		alumno.setApellido1(this.apellido1);
		alumno.setApellido2(this.apellido2);
		alumno.setSexo(this.sexo);
		alumno.setNif(this.nif);
		if (this.fechaNacimiento != null) {
			alumno.setFechaNacimiento(LocalDate.parse(this.fechaNacimiento, Constantes.DATE_FORMAT));
		}
		alumno.setPais(this.pais);
		alumno.setProvincia(this.provincia);
		alumno.setMunicipio(this.municipio);
		alumno.setLocalidad(this.localidad);
		alumno.setCalle(this.via);
		alumno.setCp(this.codigoPostal);
		if (this.getNumVia() != null) {
			alumno.setNumero(this.numVia.toString());
		}
		alumno.setPiso(this.piso);
		alumno.setLetra(this.letra);
		alumno.setEmail(this.email);
		alumno.setMovil(this.movil);
		alumno.setTelefono(this.telefono);
		alumno.setTipoFiscal(this.tipoFiscal);
		alumno.setDeSauce(this.esSauce);
		alumno.setEsProfesor(this.esProfesor);
		if (this.fechaAlta != null) {
			alumno.setFechaAlta(LocalDate.parse(this.fechaAlta, Constantes.DATE_FORMAT));
		}
		alumno.setDeSauce(Boolean.TRUE);
		alumno.setNumEscolar(this.numEscolar);
	
		return alumno;
	}

    
}

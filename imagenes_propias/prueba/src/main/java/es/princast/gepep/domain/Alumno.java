package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
 
import javax.persistence.Id;
 
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


import lombok.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.StringUtils;

/**
 * A Alumno.
 */
@Entity
@Table(name = "alumno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Alumno extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ca_alumno")
    @Id
   // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secAlumno")
   // @SequenceGenerator(name = "secAlumno",sequenceName="sec_alumno",  allocationSize=1)
    private String idAlumno;
    
    @NotNull
    @Column(name = "fl_es_profesor", nullable = false,columnDefinition="BOOLEAN DEFAULT FALSE")
    private Boolean esProfesor;

    @NotNull
    @Column(name = "fl_de_Sauce", nullable = false,columnDefinition="BOOLEAN DEFAULT TRUE")
    private Boolean deSauce;
    
    @Column(name = "ti_tipo_fiscal")
    private String tipoFiscal;

    @Column(name = "if_nif")
    private String nif;

    @Column(name = "dc_nombre")
    private String nombre;

    @Column(name = "dl_apellido1")
    private String apellido1;
    
    @Column(name = "dl_apellido2")
    private String apellido2;

    @Column(name = "fe_fnacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name = "dl_pais")
    private String pais;
    
    @Column(name = "dl_provincia")
    private String provincia;
    
    @Column(name = "dl_municipio")
    private String municipio;

    @Column(name = "dl_localidad")
    private String localidad;

    @Column(name = "dl_calle")
    private String calle;
    
    @Column(name = "dc_numero")
    private String numero;
    
    @Column(name = "dc_piso")
    private String piso;
    
    @Column(name = "dc_letra")
    private String letra;

    @Column(name = "ca_cp")
    private String cp;

    @Column(name = "dc_telefono")
    private String telefono;

    @Column(name = "dc_movil")
    private String movil;

    @Column(name = "dl_email")
    private String email;

    @Column(name = "dc_sexo")
    private String sexo;

    @Column(name = "fe_falta")
    private LocalDate fechaAlta;     
    
    @Column(name = "dc_nie")
    private String numEscolar;
    
    public String getNombreCompleto(){
        return getNombre() +" " + getApellido1() + " " + getApellido2();
    }
    public String getDireccion(){
        return getCalle()+" n" + getNumero() + " " + getPiso()+ getLetra();
    }
    public String getTelefonos(){
        return getTelefono() + (!StringUtils.isEmpty(getTelefono()) && !StringUtils.isEmpty(getMovil()) ? " // " : "" )+ getMovil();
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Alumno alumno = (Alumno) o;
        if (alumno.getIdAlumno() == null || getIdAlumno() == null) {
            return false;
        }
        return Objects.equals(getIdAlumno(), alumno.getIdAlumno());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdAlumno());
    }


}

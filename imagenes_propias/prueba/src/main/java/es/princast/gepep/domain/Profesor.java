package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * A Profesor.
 */
@Entity
@Table(name = "profesor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Profesor extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ca_profesor")
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProfesor")
//    @SequenceGenerator(name = "seqProfesor", sequenceName="sec_profesor", allocationSize=1)
    private String idProfesor;

    @NotNull
    @Column(name = "fl_de_sauce", nullable = false,columnDefinition="BOOLEAN DEFAULT TRUE")
    private Boolean deSauce;
    
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

    @Column(name = "ca_cuentass")
    private String cuentaSS;

    @Column(name = "dl_localidad")    
    private String localidad;
    
    @Column(name = "dl_provincia")    
    private String provincia;
    
    @Column(name = "dl_municipio")    
    private String municipio;
    
    @Column(name = "dl_direccion")
    private String direccion;

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

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;

    
    @Column(name = "dc_cuerpo")
    private String cuerpo;
    
    @Column(name = "ca_grupo")
    private String grupo;
    
    @Column(name = "dc_cuenta")
    private String cuenta;

    
    @Column(name = "dc_vehiculo")
    private String vehiculo;

    
    @Column(name = "dc_matricula")
    private String matricula;    


    @OneToOne
    @JoinColumn(name="ca_oferta_formativa", referencedColumnName="ca_oferta_formativa",unique = false)
    private OfertaFormativa oferta;
    
    @OneToOne
    @JoinColumn(name="cn_usuario", referencedColumnName="cn_usuario")
    private Usuario usuario;


    public String getNombreCompleto(){
        return getNombre() +" " + getApellido1() + " " + getApellido2();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Profesor profesor = (Profesor) o;
        if (profesor.getIdProfesor() == null || getIdProfesor() == null) {
            return false;
        }
        return Objects.equals(getIdProfesor(), profesor.getIdProfesor());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdProfesor());
    }
}

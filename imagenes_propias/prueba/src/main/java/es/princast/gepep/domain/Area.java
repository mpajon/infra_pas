package es.princast.gepep.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * A Area.
 */
@Entity
@Table(name = "area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Area extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_area")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqArea")
    @SequenceGenerator(name = "seqArea",sequenceName="sec_area", allocationSize=1)
    private Long idArea;

    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @Column(name = "dl_direccion")
    private String direccion;

    @Column(name = "dl_localidad")
    private String localidad;

    @Column(name = "cn_cp")
    private String cp;

    @Column(name = "dc_telefono")
    private String telefono;

    @Column(name = "dc_fax")
    private String fax;

    @Column(name = "dl_email")
    private String email;

    @Column(name = "te_observaciones")
    private String observaciones;

    @Column(name = "fl_visada")
    private Boolean visada;

    @Column(name = "dl_motivo_no_visada")
    private String motivoNoVisada;

    @Column(name = "dc_estado")
    private String estado;

    @Column(name = "dc_provincia")
    private String provincia;

    @NotNull
    @Column(name = "dc_nombre_emp_pro", nullable = false)
    private String nombreEmpresaPro;
  

    @ManyToOne 
    @JoinColumn(name = "cn_empresa", referencedColumnName="cn_empresa")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private Empresa empresa;
        

    @OneToOne
    @JoinColumn(name="cn_actividad",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  
    private Actividad actividad;
    
    
   @OneToMany(//mappedBy = "area",  // Si usamos mappedBy en vez de joinColumn a la hora de insertar me inserta en la tabla responsable sin el cn_area
     		fetch=FetchType.EAGER)     
    @JoinColumn(name="cn_area", referencedColumnName="cn_area",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "area"})   
    @Cascade({CascadeType.ALL})
    private List<ResponsableArea> responsables;
    
   @Override
   public boolean equals(Object o) {
       if (this == o) {
           return true;
       }
       if (o == null || getClass() != o.getClass()) {
           return false;
       }
       Area area = (Area) o;
       if (area.getIdArea() == null || getIdArea() == null) {
           return false;
       }
       return Objects.equals(getIdArea(), area.getIdArea());
   }

   @Override
   public int hashCode() {
       return Objects.hashCode(getIdArea());
   }

}

package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Actividad.
 */
@Entity
@Table(name = "actividad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Setter
@Getter
public class Actividad extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;
 
    @Column(name = "cn_actividad")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secActividad")
    @SequenceGenerator(name = "secActividad", sequenceName ="sec_actividad",allocationSize=1)
    private Long idActividad; 

    @NotNull
    @Column(name = "ca_codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "dc_nombre", nullable = false,length=300)
    private String nombre;

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;

    @OneToOne
    @JoinColumn(name="cn_sector",referencedColumnName="cn_sector",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private Sector sector;
    
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Actividad actividad = (Actividad) o;
        if (actividad.getIdActividad() == null || getIdActividad() == null) {
            return false;
        }
        return Objects.equals(getIdActividad(), actividad.getIdActividad());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdActividad());
    }
  
}

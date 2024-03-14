package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Ciclo.
 */
@Entity
@Table(name = "ciclo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class Ciclo extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ca_ciclo")
    @Id
   /* @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCiclo")
    @SequenceGenerator(name = "seqCiclo",sequenceName="sec_ciclo", allocationSize=1)*/
    private String idCiclo;

    @NotNull
    @Column(name = "dc_codigo", nullable = false,length=15)
    private String codigo;

    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @Column(name = "dc_familia")
    private String familia;

    @Column(name = "nu_hpractica")
    private Integer horasPractica;

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;

    
    @Column(name = "fl_desauce", nullable = false, columnDefinition="BOOLEAN DEFAULT TRUE")
    private Boolean deSauce;
    
    @OneToOne
    @JoinColumn(name = "ca_ensenanza", referencedColumnName="ca_ensenanza",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ensenanza ensenanza;
    
    @Transient
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","ciclo", "distribucion","contenidosEF"}, allowSetters =true)
    private List<ContenidoEF> contenidosEF;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ciclo ciclo = (Ciclo) o;
        if (ciclo.getIdCiclo() == null || getIdCiclo() == null) {
            return false;
        }
        return Objects.equals(getIdCiclo(), ciclo.getIdCiclo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdCiclo());
    }

    @Override
    public String toString() {
	return "Ciclo [idCiclo=" + idCiclo + ", codigo=" + codigo + ", nombre=" + nombre + ", familia=" + familia
		+ ", horasPractica=" + horasPractica + ", fechaBaja=" + fechaBaja + "]";
    }
    
    
    
    

    }

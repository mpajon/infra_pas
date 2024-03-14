package es.princast.gepep.domain;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.princast.gepep.domain.Matricula.MatriculaBuilder;

/**
 * A DistribucionPeriodo.
 */
@Entity
@Table(name = "distribucion_periodo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class DistribucionPeriodo extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_distribucion_periodo")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqDistrPeriodo")
    @SequenceGenerator(name = "seqDistrPeriodo", sequenceName="sec_distr_periodo", allocationSize=1)
    private Long idDistribucionPeriodo;
  
    @ManyToOne
    @JoinColumn(name = "cn_periodo",referencedColumnName="cn_periodo", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PeriodoPractica periodo;
 
    
    @OneToOne
    @JoinColumn(name = "cn_distribucion",referencedColumnName="cn_distribucion", nullable = false)
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler", "seguimientoFinal", "matricula", "createdBy"}, allowSetters = true)
    private Distribucion distribucion;
    
    @Transient
    @Builder.Default
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "distribucionPeriodo"})  
    private List<GastoAlumno> gastosAlumno = new ArrayList<>();
    


    
    
    @Column(name = "cn_horas")
    private Integer horas;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DistribucionPeriodo disper = (DistribucionPeriodo) o;
        if (disper.getIdDistribucionPeriodo() == null || getIdDistribucionPeriodo() == null) {
            return false;
        }
        return Objects.equals(getIdDistribucionPeriodo(), disper.getIdDistribucionPeriodo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdDistribucionPeriodo());
    }
    


  
}

package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A CriterioEvaluacion.
 */
@Entity
@Table(name = "criterio_evaluacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class CriterioEvaluacion extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_criterio")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCriterio")
    @SequenceGenerator(name = "seqCriterio", sequenceName ="sec_criterio", allocationSize=1)
    private Long idCriterio;

    @NotNull
    @Lob
    @Type(type="text")
    @Column( name = "te_texto", nullable = false, length=4000)
    private String texto;

    @OneToOne 
    @JoinColumn(name = "ca_ciclo",referencedColumnName = "ca_ciclo",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private Ciclo ciclo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CriterioEvaluacion cri = (CriterioEvaluacion) o;
        if (cri.getIdCriterio() == null || getIdCriterio() == null) {
            return false;
        }
        return Objects.equals(getIdCriterio(), cri.getIdCriterio());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdCriterio());
    }
}

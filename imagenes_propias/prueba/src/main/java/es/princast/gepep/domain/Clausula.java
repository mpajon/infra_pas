package es.princast.gepep.domain;

 
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

/**
 * A Clausula.
 */
@Entity
@Table(name = "clausula")
@Proxy(lazy = false)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Clausula extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_clausula")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqClausula")
    @SequenceGenerator(name = "seqClausula",sequenceName ="sec_clausula", allocationSize=1)
    private Long idClausula;

    @NotNull
    @Lob
    @Type(type="text")
    @Column(name = "te_texto", nullable = false,length=4000)
    private String texto;
    
    @Column(name = "nu_orden")
    private Integer orden;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Clausula cla = (Clausula) o;
        if (cla.getIdClausula() == null || getIdClausula() == null) {
            return false;
        }
        return Objects.equals(getIdClausula(), cla.getIdClausula());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdClausula());
    }
   
   
}

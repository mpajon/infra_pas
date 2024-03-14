package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ElementoFormativo.
 */
@Entity
@Table(name = "elemento_formativo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class ElementoFormativo extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

 
    @Column(name = "cn_elemento_formativo")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secElementoFormativo")
    @SequenceGenerator(name = "secElementoFormativo", sequenceName ="sec_elemento_formativo", allocationSize=1)
    private Long idElementoFormativo;
    
    @NotNull
    @Column(name = "ca_codigo", nullable = false)
    private String codigo;
 
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ElementoFormativo sector = (ElementoFormativo) o;
        if (sector.getIdElementoFormativo() == null || getIdElementoFormativo() == null) {
            return false;
        }
        return Objects.equals(getIdElementoFormativo(), sector.getIdElementoFormativo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdElementoFormativo());
    }

    @Override
    public String toString() {
	return "ElementoFormativo [idElementoFormativo=" + idElementoFormativo + ", codigo=" + codigo + "]";
    }


    
}

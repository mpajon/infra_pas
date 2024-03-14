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
 * A Ciclo.
 */
@Entity
@Table(name = "nivel_educativo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class NivelEducativo extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_nivel")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqNivel")
    @SequenceGenerator(name = "seqNivel",sequenceName="sec_nivel_educativo", allocationSize=1)
    private Integer idNivel;

    @NotNull
    @Column(name = "dc_nivel", nullable = false,length=30)
    private String nivel;   
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NivelEducativo nivel = (NivelEducativo) o;
        if (nivel.getIdNivel() == null || getIdNivel() == null) {
            return false;
        }
        return Objects.equals(getIdNivel(), nivel.getIdNivel());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdNivel());
    }

    }


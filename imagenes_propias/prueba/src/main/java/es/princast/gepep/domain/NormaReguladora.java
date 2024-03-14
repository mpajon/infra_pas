package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A NormaReguladora.
 */
@Entity
@Table(name = "norma_reguladora")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class NormaReguladora extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_norma_reguladora")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqNormaReguladora")
    @SequenceGenerator(name = "seqNormaReguladora", sequenceName="sec_norma_reguladora",allocationSize=1)
    private Long idNormaReguladora;

    @Lob
    @Type(type="text")
    @Column(name = "te_norma_reguladora")
    private String texto;
    
    @NotNull
    @Column(name = "fe_fpublica", nullable = false)
    private LocalDate fechaPublica;
    
//    @NotNull
//    @Column(name = "cn_tipo_practica", nullable = false,  unique = false)
//    //@JoinColumn(name = "cn_tipo_practica", referencedColumnName = "cn_tipo_practica")
//    private Long idTipoPractica;
    
   
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NormaReguladora textos = (NormaReguladora) o;
        if (textos.getIdNormaReguladora() == null || getIdNormaReguladora() == null) {
            return false;
        }
        return Objects.equals(getIdNormaReguladora(), textos.getIdNormaReguladora());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdNormaReguladora());
    }
}

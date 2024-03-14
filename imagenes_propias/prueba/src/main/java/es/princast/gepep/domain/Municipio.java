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
 * A Municipio.
 */
@Entity
@Table(name = "municipio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Municipio extends  AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_municipio")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqMunicipio")
    @SequenceGenerator(name = "seqMunicipio",sequenceName="sec_municipio", allocationSize=1)
    private Long idMunicipio;

    @NotNull
    @Column(name = "dl_municipio", nullable = false)
    private String municipio;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Municipio municipio = (Municipio) o;
        if (municipio.getIdMunicipio() == null || getIdMunicipio() == null) {
            return false;
        }
        return Objects.equals(getIdMunicipio(), municipio.getIdMunicipio());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdMunicipio());
    }
   
}

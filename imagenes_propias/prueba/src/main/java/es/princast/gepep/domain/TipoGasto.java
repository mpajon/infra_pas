package es.princast.gepep.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A TipoGasto.
 */
@Entity
@Table(name = "tipo_gasto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class TipoGasto extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_tipo_gasto")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secTipoGasto")
    @SequenceGenerator(name = "secTipoGasto",sequenceName ="sec_tipo_gasto",allocationSize=1)
    private Long idTipoGasto;

    @NotNull
    @Column(name = "dl_descripcion", nullable = false)
    private String descripcion;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoGasto tipoCentro = (TipoGasto) o;
        if (tipoCentro.getIdTipoGasto() == null || getIdTipoGasto() == null) {
            return false;
        }
        return Objects.equals(getIdTipoGasto(), tipoCentro.getIdTipoGasto());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdTipoGasto());
    }

}

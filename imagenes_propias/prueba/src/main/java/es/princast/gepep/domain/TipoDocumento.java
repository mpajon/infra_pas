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
 * A TipoCentro.
 */
@Entity
@Table(name = "tipo_documento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class TipoDocumento extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_tipo_documento")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secTipoDocumento")
    @SequenceGenerator(name = "secTipoDocumento",sequenceName ="sec_tipo_documento",allocationSize=1)
    private Long idTipoDocumento;

    @NotNull
    @Column(name = "ca_codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "dl_descripcion", nullable = false)
    private String descripcion;
    
    @NotNull
    @Column(name = "fl_activo", nullable = false)
    private Boolean activo;

    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoDocumento tipoDocumento = (TipoDocumento) o;
        if (tipoDocumento.getIdTipoDocumento() == null || getIdTipoDocumento() == null) {
            return false;
        }
        return Objects.equals(getIdTipoDocumento(), tipoDocumento.getIdTipoDocumento());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdTipoDocumento());
    }
    
}

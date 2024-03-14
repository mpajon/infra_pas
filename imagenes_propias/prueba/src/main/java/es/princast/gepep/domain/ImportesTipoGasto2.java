package es.princast.gepep.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A importes_tipogasto2.
 */
@Entity
@Table(name = "importes_tipogasto2")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class ImportesTipoGasto2 extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_importes_tipogasto2")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secImportes")
    @SequenceGenerator(name = "secImportes",sequenceName ="sec_importes_tipogasto2",allocationSize=1)
    private Long idImporteTipoGasto2;

    @NotNull
    @Column(name = "dl_dt", nullable = false)
    private String dt;

    @NotNull
    @Column(name = "nu_km_desde", nullable = false)
    private Integer nKmDesde;

    @NotNull
    @Column(name = "nu_km_hasta", nullable = false)
    private Integer nKmHasta;

    @NotNull
    @Column(name = "nu_gasto_total", nullable = false)
    private Integer nGastoTotal;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImportesTipoGasto2 importesTipoGasto2 = (ImportesTipoGasto2) o;
        if (importesTipoGasto2.getIdImporteTipoGasto2() == null || getIdImporteTipoGasto2() == null) {
            return false;
        }
        return Objects.equals(getIdImporteTipoGasto2(), importesTipoGasto2.getIdImporteTipoGasto2());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdImporteTipoGasto2());
    }

}

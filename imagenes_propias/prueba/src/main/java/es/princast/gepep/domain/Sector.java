package es.princast.gepep.domain;

import java.time.LocalDate;
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
 * A Sector.
 */
@Entity
@Table(name = "sector")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Sector extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

 
    @Column(name = "cn_sector")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secSector")
    @SequenceGenerator(name = "secSector", sequenceName ="sec_sector", allocationSize=1)
    private Long idSector;
    
    @NotNull
    @Column(name = "ca_codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sector sector = (Sector) o;
        if (sector.getIdSector() == null || getIdSector() == null) {
            return false;
        }
        return Objects.equals(getIdSector(), sector.getIdSector());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdSector());
    }


}

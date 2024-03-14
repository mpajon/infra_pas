package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OpcionMenu.
 */
@Entity
@Table(name = "opcion_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class OpcionMenu extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_opcion_menu")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOpcionMenu")
    @SequenceGenerator(name = "seqOpcionMenu", sequenceName ="sec_opcion_menu", allocationSize=1)
    private Long idOpcionMenu;

    @NotNull
    @Column(name = "dl_titulo", nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "dc_tag", nullable = false)
    private String tag;

    @NotNull
    @Column(name = "nu_nivel", nullable = false)
    private Integer nivel;

    @NotNull
    @Column(name = "ca_ruta", nullable = false)
    private String ruta;

    @OneToOne
    @JoinColumn(name="cn_padre",referencedColumnName="cn_opcion_menu",unique = true, nullable =true)
    private OpcionMenu padre;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpcionMenu opcionMenu = (OpcionMenu) o;
        if (opcionMenu.getIdOpcionMenu() == null || getIdOpcionMenu() == null) {
            return false;
        }
        return Objects.equals(getIdOpcionMenu(), opcionMenu.getIdOpcionMenu());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdOpcionMenu());
    }
}

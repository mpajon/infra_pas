package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A PermisoMenu.
 */
@Entity
@Table(name = "permiso_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class PermisoMenu extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_permiso_menu")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPermisoMenu")
    @SequenceGenerator(name = "seqPermisoMenu", sequenceName ="sec_menu", allocationSize=1)
    private Long idPermisoMenu;

    @ManyToOne
    @JoinColumn(name = "cn_opcion_menu", referencedColumnName="cn_opcion_menu",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private OpcionMenu opcionMenu;

    @ManyToOne
    @JoinColumn(name = "cn_perfil", referencedColumnName="cn_perfil", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Perfil perfil;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermisoMenu permiso = (PermisoMenu) o;
        if (permiso.getIdPermisoMenu() == null || getIdPermisoMenu() == null) {
            return false;
        }
        return Objects.equals(getIdPermisoMenu(), permiso.getIdPermisoMenu());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPermisoMenu());
    }

}

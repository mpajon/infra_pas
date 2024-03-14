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
 * A Perfil.
 */
@Entity
@Table(name = "perfil")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Perfil extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_perfil")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPerfil")
    @SequenceGenerator(name = "seqPerfil" , sequenceName ="sec_perfil",allocationSize=1)
    private Long idPerfil;

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
        Perfil perfil = (Perfil) o;
        if (perfil.getIdPerfil() == null || getIdPerfil() == null) {
            return false;
        }
        return Objects.equals(getIdPerfil(), perfil.getIdPerfil());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPerfil());
    }
    
}

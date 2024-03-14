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

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * A Tutor.
 */
@Entity
@Table(name = "tutor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Tutor extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_tutor")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTutor")
    @SequenceGenerator(name = "seqTutor", sequenceName="sec_tutor",  allocationSize=1)
    private Long idTutor;

    @Column(name = "dl_cuerpo")
    private String cuerpo;

    @Column(name = "ca_cuenta")
    private String cuenta;

    @Column(name = "ca_vehiculo")
    private String vehiculo;

    @Column(name = "ca_matricula")
    private String matricula;

    @Column(name = "ca_curso_aca")
    private String cursoAcademico;

    @Column(name = "fe_fbaja")
    private String fechaBaja;

    @OneToOne
    @JoinColumn(name="ca_oferta_formativa",referencedColumnName="ca_oferta_formativa",unique = false)
    @Cascade({CascadeType.ALL})
    private OfertaFormativa oferta;

    @OneToOne
    @JoinColumn(name="cn_usuario", referencedColumnName="cn_usuario",unique = true)
    @Cascade({CascadeType.ALL})
    private Usuario usuario;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tutor tutor = (Tutor) o;
        if (tutor.getIdTutor() == null || getIdTutor() == null) {
            return false;
        }
        return Objects.equals(getIdTutor(), tutor.getIdTutor());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdTutor());
    }

  
  
}

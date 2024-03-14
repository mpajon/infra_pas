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
 * A HorasPeriodo.
 */
@Entity
@Table(name = "horas_periodo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class HorasPeriodo extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_horas_periodo")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secHoras")
    @SequenceGenerator(name = "secHoras", sequenceName="sec_horas_periodo", allocationSize=1)
    private Long idHorasPeriodo;

    @NotNull
    @Column(name = "dc_nombre", nullable = false, unique = true)
    private String nombre;
    
    @Column(name = "nu_h_entrada_man")
    private Integer hEntradaMan;

    @Column(name = "nu_h_salida_man")
    private Integer hSalidaMan;

    @Column(name = "nu_min_entrada_man")
    private Integer minEntradaMan;

    @Column(name = "nu_min_salida_man")
    private Integer minSalidaMan;

    @Column(name = "nu_h_entrada_tard")
    private Integer hEntradaTard;

    @Column(name = "nu_h_salida_tard")
    private Integer hSalidaTard;

    @Column(name = "nu_min_entrada_tard")
    private Integer minEntradaTard; 
 
    @Column(name = "nu_min_salida_tard")
    private Integer minSalidaTard;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HorasPeriodo horasPeriodo = (HorasPeriodo) o;
        if (horasPeriodo.getIdHorasPeriodo() == null || getIdHorasPeriodo() == null) {
            return false;
        }
        return Objects.equals(getIdHorasPeriodo(), horasPeriodo.getIdHorasPeriodo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdHorasPeriodo());
    }
}

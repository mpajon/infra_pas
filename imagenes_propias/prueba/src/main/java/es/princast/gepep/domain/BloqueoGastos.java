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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Actividad.
 */
@Entity
@Table(name = "bloqueo_gastos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class BloqueoGastos extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_bloqueo_gastos")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secBloqueo")
    @SequenceGenerator(name = "secBloqueo", sequenceName ="sec_bloqueo_gastos",allocationSize=1)
    private Long idBloqueoGastos; 

    @ManyToOne
    @JoinColumn(name="cn_anio",referencedColumnName="nu_anio",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CursoAcademico cursoAcademico;

    @ManyToOne
    @JoinColumn(name="cn_periodo_liquidacion",referencedColumnName="cn_periodo_liquidacion",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PeriodoLiquidacion periodo;

    @Column(name = "fl_bloqueo")
    private Boolean bloqueado;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BloqueoGastos bloqueo = (BloqueoGastos) o;
        if (bloqueo.getIdBloqueoGastos() == null || getIdBloqueoGastos() == null) {
            return false;
        }
        return Objects.equals(getIdBloqueoGastos(), bloqueo.getIdBloqueoGastos());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdBloqueoGastos());
    }


}


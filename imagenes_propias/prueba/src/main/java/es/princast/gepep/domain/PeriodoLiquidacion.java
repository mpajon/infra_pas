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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A PeriodoPractica.
 */
@Entity
@Table(name = "periodos_liquidacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class PeriodoLiquidacion extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_periodo_liquidacion")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPeriodoLiquidacion")
    @SequenceGenerator(name = "seqPeriodoLiquidacion", sequenceName ="sec_periodos_liquidacion", allocationSize=1)
    private Integer idPeriodoLiquidacion;
    
    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;
 

    @Column(name = "nu_dia_ini")
    private Integer diaIni;

    @Column(name = "nu_mes_ini")
    private Integer mesIni;
    
    @Column(name = "nu_dia_fin")
    private Integer diaFin;
    
    @Column(name = "nu_mes_fin")
    private Integer mesFin;
    
     
    @ManyToOne 
    @JoinColumn(name = "cn_tipo_practica", referencedColumnName="cn_tipo_practica")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private TipoPractica tipoPractica;
   
    @Transient
    private Boolean bloqueado;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeriodoLiquidacion pl = (PeriodoLiquidacion) o;
        if (pl.getIdPeriodoLiquidacion() == null || getIdPeriodoLiquidacion() == null) {
            return false;
        }
        return Objects.equals(getIdPeriodoLiquidacion(), pl.getIdPeriodoLiquidacion());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPeriodoLiquidacion());
    }
     
    
}

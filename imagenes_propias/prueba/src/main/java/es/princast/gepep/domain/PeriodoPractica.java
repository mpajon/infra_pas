package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A PeriodoPractica.
 */
@Entity
@Table(name = "periodo_practica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class PeriodoPractica extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_periodo")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPeriodo")
    @SequenceGenerator(name = "seqPeriodo", sequenceName ="sec_periodo_practica", allocationSize=1)
    private Long idPeriodo;
   
    @ManyToOne
    @JoinColumn(name="cn_curso_aca",referencedColumnName="nu_anio",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CursoAcademico cursoAcademico;

    @NotNull
    @Column(name = "fe_finicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fe_ffin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "nu_horas")
    private Integer horas;
    
    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    
    @OneToOne
    @JoinColumn(name="cn_horario",referencedColumnName="cn_horas_periodo",unique = false)
    private HorasPeriodo horario;
    
  
    @ManyToOne 
    @JoinColumn(name = "cn_tipo_practica", referencedColumnName="cn_tipo_practica")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private TipoPractica tipoPractica;

    @ManyToOne
    @JoinColumn(name = "cn_tipo_gasto", referencedColumnName="cn_tipo_gasto")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoGasto tipoGasto;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeriodoPractica periodo = (PeriodoPractica) o;
        if (periodo.getIdPeriodo() == null || getIdPeriodo() == null) {
            return false;
        }
        return Objects.equals(getIdPeriodo(), periodo.getIdPeriodo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPeriodo());
    }
     
    
}

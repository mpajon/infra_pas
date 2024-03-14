package es.princast.gepep.domain;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * A AnexoContrato.
 */
@Entity
@Table(name = "anexo_contrato")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class AnexoContrato extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_anexo")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secAnexo")
    @SequenceGenerator(name = "secAnexo",sequenceName="sec_anexo", allocationSize=1)
    private Long idAnexo;

    @NotNull
    @Column(name = "ca_codigo", nullable = false)
    private Integer codAnexo;

    @NotNull
    @Column(name = "ca_cursoaca")
    private String cursoAcademico;
    
    @OneToOne //(fetch=FetchType.LAZY)
    @JoinColumn(name = "cn_convenio", nullable = false)    
   // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})    
    private Convenio convenio ;
         

    @ManyToOne 
    @JoinColumn(name = "ca_ofercen", referencedColumnName="ca_ofercen")  
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"}, allowSetters = true)    
    private OfertasCentro ofertaCentro;
 
    
    @ManyToOne 
    @JoinColumn(name = "ca_unidad", referencedColumnName="ca_unidad")
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"}, allowSetters = true)  
    private Unidad unidad;
    
    
    @ManyToOne 
    @JoinColumn(name = "cn_periodo", referencedColumnName="cn_periodo")  
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"}, allowSetters = true)   
    private PeriodoPractica periodo;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnexoContrato anexoContrato = (AnexoContrato) o;
        if (anexoContrato.getIdAnexo() == null || getIdAnexo() == null) {
            return false;
        }
        return Objects.equals(getIdAnexo(), anexoContrato.getIdAnexo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdAnexo());
    }
    
   
}

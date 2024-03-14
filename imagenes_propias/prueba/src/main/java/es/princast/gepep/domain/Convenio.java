package es.princast.gepep.domain;

 
import java.time.LocalDate;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Convenio.
 */
@Entity
@Table(name = "convenio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Convenio extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_convenio")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secConvenio")
    @SequenceGenerator(name = "secConvenio", sequenceName ="sec_convenio", allocationSize=1)
    private Long idConvenio;

    @NotNull
    @Column(name = "nu_numero", nullable = false)
    private Integer numero;

    @NotNull
    @Column(name = "dc_codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "ca_anexo_inicial", nullable = false)
    private Integer anexoInicial;

    @Column(name = "fe_fconvenio")
    private LocalDate fechaConvenio;
    

   @Column(name = "nu_diaconvenio")
    private Integer diaConvenio;
    

    @Column(name = "nu_mesconvenio" )
    private Integer mesConvenio;
    

    @Column(name = "nu_anioconvenio" )
    private Integer anioConvenio;
    
         
    @Column(name = "fe_ffin")
    private LocalDate fechaFin;
    
   @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;
    

    @Column(name = "fl_validado",columnDefinition="BOOLEAN DEFAULT FALSE")
    private Boolean validado;
    

    @Column(name = "fl_antiguo", columnDefinition="BOOLEAN DEFAULT FALSE")
    private Boolean antiguo;
 
    
    @OneToOne
    @JoinColumn(name="ca_centro",referencedColumnName="ca_centro",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Centro centro;

    @OneToOne
    @JoinColumn(name="cn_area",referencedColumnName="cn_area",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Area area;
    
    @OneToOne
    @JoinColumn(name="cn_tipo_practica",referencedColumnName="cn_tipo_practica",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoPractica tipoPractica;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Convenio con = (Convenio) o;
        if (con.getIdConvenio() == null || getIdConvenio() == null) {
            return false;
        }
        return Objects.equals(getIdConvenio(), con.getIdConvenio());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdConvenio());
    }
   
 
    
    
}

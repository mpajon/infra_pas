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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A configElemFormativo.
 */
@Entity
@Table(name = "config_elem_formativo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class ConfigElemFormativo extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

 
    @Column(name = "cn_config_elem_formativo")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secConfigElemFormativo")
    @SequenceGenerator(name = "secConfigElemFormativo", sequenceName ="sec_config_elem_formativo", allocationSize=1)
    private Long idConfigElemFormativo;
    
    @NotNull
    @Column(name = "dc_denominacion", nullable = false)
    private String denominacion;
    
    @NotNull
    @Column(name = "dc_configuracion", nullable = false)
    private String configuracion;
    
    
    @NotNull
    @Column(name = "fl_ligado", nullable = false)
    private Boolean ligado;
    

    @NotNull
    @Column(name = "fl_activo", nullable = false)
    private Boolean activo;
    
    @NotNull
    @Column(name = "cn_orden", nullable = false)
    private Integer orden;
 
    
    
    @OneToOne
    @JoinColumn(name="cn_elemento_formativo",referencedColumnName="cn_elemento_formativo",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private ElementoFormativo elementoFormativo;
    
 /*   @OneToOne
    @JoinColumn(name="cn_tipo_practica",referencedColumnName="cn_tipo_practica",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private TipoPractica tipoPractica;*/
    
    @Column(name = "cn_tipo_practica", insertable = false, updatable = false)
    private Long idTipoPractica;
 
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigElemFormativo sector = (ConfigElemFormativo) o;
        if (sector.getIdConfigElemFormativo() == null || getIdConfigElemFormativo() == null) {
            return false;
        }
        return Objects.equals(getIdConfigElemFormativo(), sector.getIdConfigElemFormativo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdConfigElemFormativo());
    }


}

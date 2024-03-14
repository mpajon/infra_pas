package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Contenido_ef_ciclo.
 */
@Entity
@Table(name = "contenido_ef")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class ContenidoEF extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

 
    @Column(name = "cn_contenido_ef")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secContenidoEF")
    @SequenceGenerator(name = "secContenidoEF", sequenceName ="sec_contenido_ef", allocationSize=1)
    private Long idContenidoEF;
    
    @NotNull
    @Lob
    @Type(type="text")
    @Column( name = "te_texto", nullable = false, length=10000)
    private String texto;
    
    
    @OneToOne
    @JoinColumn(name="cn_config_elem_formativo",referencedColumnName="cn_config_elem_formativo",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private ConfigElemFormativo configElemFormativo;
    
    @OneToOne
    @JoinColumn(name="ca_ciclo",referencedColumnName="ca_ciclo",unique = false)
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "contenidosEF"}, allowSetters = true)   
    private Ciclo ciclo;
 
    
    @OneToOne
    @JoinColumn(name="cn_distribucion",referencedColumnName="cn_distribucion",unique = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
//    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler", "distribucion"}, allowSetters = true)
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "contenidosEF"}, allowSetters = true)   
    private Distribucion distribucion;
 
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContenidoEF sector = (ContenidoEF) o;
        if (sector.getIdContenidoEF() == null || getIdContenidoEF() == null) {
            return false;
        }
        return Objects.equals(getIdContenidoEF(), sector.getIdContenidoEF());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdContenidoEF());
    }

    @Override
    public String toString() {
	return "ContenidoEF [idContenidoEF=" + idContenidoEF + ", texto=" + texto + ", configElemFormativo="
		+ configElemFormativo + "]";
    }


}

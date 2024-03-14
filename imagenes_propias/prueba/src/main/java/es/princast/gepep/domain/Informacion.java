package es.princast.gepep.domain;

import java.time.Instant;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Informacion.
 */
@Entity
@Table(name = "informacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Informacion extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_informacion")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqInformacion")
    @SequenceGenerator(name = "seqInformacion",sequenceName ="sec_informacion", allocationSize=1)
    private Long idInformacion;

    @NotNull
    @Column(name = "dc_nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "dl_descripcion")
    private String descripcion;

    @Column(name = "fl_particular")
    private Boolean particular;
    
    @Column(name = "fe_fbaja")
    private Instant fechaBaja;
    
    
    @ManyToOne  
    @JoinColumn(name = "cn_tipo_practica", referencedColumnName="cn_tipo_practica")    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoPractica tipoPractica;
    
    
    private static final String FOLDER_INFORMACION_TIPOPRAC = "informacion_tipoprac";
    public String getFolderInformacionTipoPractica () {
		return FOLDER_INFORMACION_TIPOPRAC + this.getTipoPractica().getIdTipoPractica();
	}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Informacion informacion = (Informacion) o;
        if (informacion.getIdInformacion() == null || getIdInformacion() == null) {
            return false;
        }
        return Objects.equals(getIdInformacion(), informacion.getIdInformacion());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdInformacion());
    }
 
 

}

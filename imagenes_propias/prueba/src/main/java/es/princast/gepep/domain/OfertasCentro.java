package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A PracticaOferta.
 */
@Entity
@Table(name = "ofertas_centro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class OfertasCentro extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ca_ofercen")
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOferCen")
//    @SequenceGenerator(name = "seqOferCen", sequenceName="sec_ofertas_centro", allocationSize=1)
    private String idOfertaCentro;   

    @Column(name = "fl_vigente")
    private Boolean vigente;    
    
    @ManyToOne
    @JoinColumn(name = "ca_centro",referencedColumnName="ca_centro", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private Centro centro;

    @ManyToOne
    @JoinColumn(name = "ca_oferta_formativa", referencedColumnName="ca_oferta_formativa",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"}, allowSetters = true)
    private OfertaFormativa oferta;
    
    
    public OfertasCentro(String idOfertaCentro) {
    	this.idOfertaCentro = idOfertaCentro;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfertasCentro oc = (OfertasCentro) o;
        if (oc.getIdOfertaCentro() == null || getIdOfertaCentro() == null) {
            return false;
        }
        return Objects.equals(getIdOfertaCentro(), oc.getIdOfertaCentro());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdOfertaCentro());
    }

  
}

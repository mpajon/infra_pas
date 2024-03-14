package es.princast.gepep.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
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
 * A OfertaFormativa.
 */
@Entity
@Table(name = "oferta_formativa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class OfertaFormativa extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ca_oferta_formativa", nullable = false)
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOferform")
//    @SequenceGenerator(name = "seqOferform", sequenceName ="sec_oferta_formativa", allocationSize=1)
    private String idOfertaFormativa;

    
    @Column(name = "ca_curso", nullable = true)
    private Integer curso;

    @Column(name = "cn_anioinicio")
    private Integer anioInicio;

    @Column(name = "cn_aniofin")
    private Integer anioFin;
    
    @Column(name = "dc_nombre")
    private String nombre;
    
    @Column(name = "dc_codigo")
    private String codigo;
    
    @Column(name = "ca_regimen")
    private String regimen;
    
    @Column(name = "ca_grupo")
    private String unidad;
    
    
    @Column(name = "fl_vigente", nullable = false)
    private Boolean vigente;    
    
    @NotNull
    @Column(name = "fl_desauce", nullable = false, columnDefinition="BOOLEAN DEFAULT TRUE")
    private Boolean deSauce;

    
    @OneToOne
    @JoinColumn(name = "ca_ciclo", referencedColumnName="ca_ciclo",unique = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"}, allowSetters = true)
    private Ciclo ciclo;
    
    @Transient
    @Builder.Default
    private Set<Centro> centros = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfertaFormativa ofertaFormativa = (OfertaFormativa) o;
        if (ofertaFormativa.getIdOfertaFormativa() == null || getIdOfertaFormativa() == null) {
            return false;
        }
        return Objects.equals(getIdOfertaFormativa(), ofertaFormativa.getIdOfertaFormativa());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdOfertaFormativa());
    }

    

}

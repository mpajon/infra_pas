package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Ensenanza.
 */
@Entity
@Table(name = "ensenanza")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
//@Data
@Setter
@Getter

public class Ensenanza extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    
    @Column(name = "ca_ensenanza",nullable =false) 
    @Id   
    private String idEnsenanza;
 
    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @Column(name = "dl_descripcion")
    private String descripcion;

    
   /* @Column(name = "nu_nivel", nullable = false)
    private Integer nivel;*/
    
    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn( name = "nu_nivel", referencedColumnName="cn_nivel", nullable = true)  
    private NivelEducativo nivel;
    

    @Column(name = "nu_padre")
    private Integer padre;

    
    @Column(name = "fe_fini_vigencia")
    private LocalDate fechaInicioVigencia;

    
    @Column(name = "fe_ffin_vigencia")
    private LocalDate fechaFinVigencia;

    @NotNull
    @Column(name = "fl_vigente", nullable = false)
    private Boolean vigente;


    @Column(name = "dl_norma")
    private String norma;

    @NotNull
    @Column(name = "fl_desauce", nullable = false,columnDefinition="BOOLEAN DEFAULT TRUE")
    private Boolean deSauce;   
   
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ensenanza ensenanza = (Ensenanza) o;
        if (ensenanza.getIdEnsenanza() == null || getIdEnsenanza() == null) {
            return false;
        }
        return Objects.equals(getIdEnsenanza(), ensenanza.getIdEnsenanza());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdEnsenanza());
    }
  
}

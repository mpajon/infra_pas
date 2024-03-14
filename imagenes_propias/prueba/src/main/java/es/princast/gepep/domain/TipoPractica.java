package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A TipoPractica.
 */
@Entity
@Table(name = "tipo_practica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
//@Data
@Setter
@Getter


/** Mapping para queries Nativas: (usadas para evitar que jpa nos inicialice todas las relaciones y afecte al rendimiento) **/
@SqlResultSetMapping(
        name = "TipoPracticaComboListMapping",
        classes = @ConstructorResult(
                targetClass = TipoPractica.class,
                columns = {
                    @ColumnResult(name = "idTipoPractica", type = Long.class),
                    @ColumnResult(name = "nombre", type = String.class),
                    @ColumnResult(name = "descripcion")
                    }))

public class TipoPractica extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_tipo_practica")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTipoPractica")
    @SequenceGenerator(name = "seqTipoPractica", sequenceName="sec_tipo_practica", allocationSize=1)  
    private Long idTipoPractica;

    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "dl_descripcion", nullable = false)
    private String descripcion;
    
 
    @NotNull
    @Column(name = "nu_horas", nullable = false)
    private Integer nHoras;

    @NotNull
    @Column(name = "nu_periodos", nullable = false)
    private Integer nPeriodos;

    @NotNull
    @Column(name = "fl_profesorado",columnDefinition= "BOOLEAN DEFAULT FALSE", nullable = false)
    private Boolean esProfesorado;

    @NotNull
    @Column(name = "fl_tut_centro", columnDefinition= "BOOLEAN DEFAULT TRUE", nullable = false)
    private Boolean tutoriaCentro;

    @NotNull
    @Column(name = "fl_tut_empresa", columnDefinition= "BOOLEAN DEFAULT TRUE",nullable = false)
    private Boolean tutoriaEmpresa;
    
    
    @Column(name = "fe_finvigencia" )
    private LocalDate fechaFinVigencia;
    
    @Column(name = "nu_bloqueo")
    private Integer bloqueoVisita;
    
    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn( name = "cn_visor", referencedColumnName="cn_visor", nullable = true)  
    private Visor visor;

    @OneToOne
    @JoinColumn(name="cn_categoria",referencedColumnName="cn_categoria",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private CategoriaPractica categoria;
    
    
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval =true) // OrphanRemoval es para que cuando borremos/actualicemos las normas existentes nos actualice toda la coleccion//
    @JoinColumn(name="cn_tipo_practica", referencedColumnName="cn_tipo_practica", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "tipoPractica"})   
    @OrderBy("orden ASC")
    private Set<ConfigElemFormativo> configuracionesEF;
    
    
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval =true) // OrphanRemoval es para que cuando borremos/actualicemos las normas existentes nos actualice toda la coleccion//
    @JoinColumn(name="cn_tipo_practica", referencedColumnName="cn_tipo_practica", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "tipoPractica"})   
    @OrderBy("fechaPublica ASC")
    private Set<NormaReguladora> normasReguladoras;
    

    @ManyToMany(fetch=FetchType.EAGER)   
    @JoinTable(name = "tipopractica_ensenanzas",
               joinColumns = @JoinColumn(name="cn_tipo_practica", referencedColumnName="cn_tipo_practica"),
               inverseJoinColumns = @JoinColumn(name="ca_ensenanza", referencedColumnName="ca_ensenanza"))
    @Builder.Default
    private Set<Ensenanza> ensenanzas = new HashSet<>();
   

    public Set<Ensenanza> getEnsenanzas() {
        return ensenanzas;
    }

    public TipoPractica ensenanzas(Set<Ensenanza> ensenanzas) {
        this.ensenanzas = ensenanzas;
        return this;
    }
    
    public void setEnsenanzas(Set<Ensenanza> ensenanzas) {
        this.ensenanzas = ensenanzas;
    }

    // Comentado y establecido Transient: esta entidad estaba dando problemas porque recuperaba todos los periodos de 
    // practica porque no estaba filtrando por tido de practica.
    //@OneToMany(cascade = CascadeType.ALL, mappedBy ="tipoPractica",fetch=FetchType.LAZY)
    @Transient
    @Builder.Default
    @JsonIgnore
    private Set<PeriodoPractica> periodos = new HashSet<>(); 
    

    public TipoPractica addPeriodos(PeriodoPractica periodoPractica) {
        this.periodos.add(periodoPractica);
        periodoPractica.setTipoPractica(this);
        return this;
    }

    public TipoPractica removePeriodos(PeriodoPractica periodoPractica) {
        this.periodos.remove(periodoPractica);
        periodoPractica.setTipoPractica(null);
        return this;
    } 
    
    
    /**
     * Constructor para TipoPracticaComboListMapping
     */
    public TipoPractica(
			Long idTipoPractica, @NotNull String nombre, @NotNull String descripcion) {
		this.idTipoPractica = idTipoPractica;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

    
       
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoPractica tipoPractica = (TipoPractica) o;
        if (tipoPractica.getIdTipoPractica() == null || getIdTipoPractica() == null) {
            return false;
        }
        return Objects.equals(getIdTipoPractica(), tipoPractica.getIdTipoPractica());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdTipoPractica());
    }

	


}

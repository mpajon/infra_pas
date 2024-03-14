package es.princast.gepep.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Documento.
 */
@Entity
@Table(name = "documento")
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
        name = "DocumentoListMapping",
        classes = @ConstructorResult(
                targetClass = Documento.class,
                columns = {
                    @ColumnResult(name = "idDocumento", type = Long.class),
                    @ColumnResult(name = "nombre", type = String.class),
                    @ColumnResult(name = "logo1", type = String.class),
                    @ColumnResult(name = "logo2", type = String.class),
                    @ColumnResult(name = "encabezado", type = String.class),
                    @ColumnResult(name = "encabezado2", type = String.class),
                    @ColumnResult(name = "encabezadoClausulas", type = String.class),
                    @ColumnResult(name = "encabezadoFirma", type = String.class),
                    @ColumnResult(name = "tieneTextos", type = Boolean.class),
                    @ColumnResult(name = "tieneClausula", type = Boolean.class),
                    @ColumnResult(name = "firmaCentro", type = Boolean.class),
                    @ColumnResult(name = "firmaEmpresa", type = Boolean.class),
                    @ColumnResult(name = "firmaTutor", type = Boolean.class),
                    // Datos tipo documento //
                    @ColumnResult(name = "idTipoDocumento", type = Long.class),
                    @ColumnResult(name = "codigoTipoDocumento", type = String.class),
                    @ColumnResult(name = "descripcionTipoDocumento", type = String.class),
                    @ColumnResult(name = "activo", type = Boolean.class),
                    // Datos tipo practica //
                    @ColumnResult(name = "idTipoPractica", type = Long.class),
                    @ColumnResult(name = "nombreTipoPractica", type = String.class),
                    @ColumnResult(name = "descripcionTipoPractica", type = String.class),
                    }))

public class Documento extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_documento")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqDocumento")
    @SequenceGenerator(name = "seqDocumento", sequenceName ="sec_documento", allocationSize=1)
    private Long idDocumento;
    
    @NotNull
    @Column(name = "dc_nombre")
    private String nombre;
     
    @Column(name = "dl_logo1")
    private String logo1;

    @Column(name = "dl_logo2")
    private String logo2;

    @Column(name = "dl_encabezado")
    private String encabezado;

    @Column(name = "dl_encabezado2")
    private String encabezado2;
    
    @Column(name = "te_encabezado_clausulas")
    private String encabezadoClausulas;

    @Column(name = "fl_textos")
    private Boolean tieneTextos;

    @Column(name = "fl_clausula")
    private Boolean tieneClausula;

    @Column(name = "fl_firma_centro")
    private Boolean firmaCentro;

    @Column(name = "fl_firma_empresa")
    private Boolean firmaEmpresa;

    @Column(name = "fl_firma_tutor")
    private Boolean firmaTutor;
    
    @Column(name = "te_encabezado_firma")
    private String encabezadoFirma;

    /*@OneToOne
    @JoinColumn(name="cn_tipo_practica",referencedColumnName = "cn_tipo_practica",unique = false)
    @Cascade({CascadeType.ALL})
    private TipoPractica tipoPractica;*/ 
    

    @OneToOne
    @JoinColumn(name="cn_tipo_documento",referencedColumnName="cn_tipo_documento",unique = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoDocumento tipoDocumento;
    
    

    @ManyToOne  
    @JoinColumn(name = "cn_tipo_practica", referencedColumnName="cn_tipo_practica")    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoPractica tipoPractica;
    


    @ManyToMany(fetch = FetchType.EAGER)    
    @JoinTable(name = "clausula_documento",
    			joinColumns = @JoinColumn(name = "cn_documento", referencedColumnName = "cn_documento"), 
    			inverseJoinColumns = @JoinColumn(name = "cn_clausula", referencedColumnName = "cn_clausula"))
    @Builder.Default
    @SortNatural
    @OrderBy ("nu_orden ASC")
    private Set<Clausula> clausulas = new HashSet<>();
    
    public Set<Clausula> getClausulas() {
        return clausulas;
    }

    public Documento clausulas(Set<Clausula> clausulas) {
        this.clausulas = clausulas;
        return this;
    }
    
    public void setClausulas(Set<Clausula> clausulas) {
        this.clausulas = clausulas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Documento doc = (Documento) o;
        if (doc.getIdDocumento() == null || getIdDocumento() == null) {
            return false;
        }
        return Objects.equals(getIdDocumento(), doc.getIdDocumento());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdDocumento());
    }
    /**
     * Constructor para DocumentoListMapping
     */
	public Documento(
			Long idDocumento, @NotNull String nombre, String logo1, String logo2, String encabezado, String encabezado2,
			String encabezadoClausulas, String encabezadoFirma, Boolean tieneTextos, Boolean tieneClausula, Boolean firmaCentro,
			Boolean firmaEmpresa, Boolean firmaTutor,
			// Datos tipoDocumento //
			Long idTipoDocumento, String codigoTipoDocumento, String descripcionTipoDocumento, Boolean activo,
			// Datos de tipoPractica //
			Long idTipoPractica, String nombreTipoPractica, String descripcionTipoPractica
			) {
		this.idDocumento = idDocumento;
		this.nombre = nombre;
		this.logo1 = logo1;
		this.logo2 = logo2;
		this.encabezado = encabezado;
		this.encabezado2 = encabezado2;
		this.encabezadoClausulas = encabezadoClausulas;
		this.encabezadoFirma = encabezadoFirma;
		this.tieneTextos = tieneTextos;
		this.tieneClausula = tieneClausula;
		this.firmaCentro = firmaCentro;
		this.firmaEmpresa = firmaEmpresa;
		this.firmaTutor = firmaTutor;
		
		// Datos tipoDocumento//
		this.tipoDocumento = new TipoDocumento( idTipoDocumento,  codigoTipoDocumento,  descripcionTipoDocumento,  activo);
		
		// Datos de tipoPractica //
		this.tipoPractica = new TipoPractica(idTipoPractica, nombreTipoPractica, descripcionTipoPractica);
	}
    

}

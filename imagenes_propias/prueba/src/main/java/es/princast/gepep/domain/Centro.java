package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Centro.
 */
@Entity
@Table(name = "centro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
/** Mapping para queries Nativas: (usadas para evitar que jpa nos inicialice todas las relaciones y afecte al rendimiento) **/
@SqlResultSetMapping(
        name = "CentroComboListMapping",
        classes = @ConstructorResult(
                targetClass = Centro.class,
                columns = {
                    @ColumnResult(name = "idCentro", type = String.class),
                    @ColumnResult(name = "codigo", type = String.class),
                    @ColumnResult(name = "nombre", type = String.class),
                    @ColumnResult(name = "cif", type = String.class),
                    }))

public class Centro extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    /*no tiene secuencia, se guarda mismo id que viene de sauce*/
    /*Si se crea manual , igualmente el codigo existirá en sauce*/
    @Id
    @Column(name="ca_centro", nullable = false)
    private String idCentro ;

    @NotNull
    @Column(name = "dc_codigo")
    private String codigo;
    
    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @Column(name = "dc_telefono")
    private String telefono;

    @Column(name = "dc_fax")
    private String fax;

    @Column(name = "dl_email")
    private String email;
    

    @Column(name = "dl_zona")
    private String zona;

    @Column(name = "dl_direccion")
    private String direccion;

    @Column(name = "dl_localidad")
    private String localidad;

    @Column(name = "cn_cp")
    private String cp;

    @Column(name = "if_cif")
    private String cif;

    @Column(name = "dl_director")
    private String director;

    @Column(name = "if_nif_director")
    private String nifDirector;
    
    @Column(name = "dc_tipo_centro")
    private String tipoCentro;

    @Column(name = "fl_concertado")
    private Boolean concertado;

    @Column(name = "fl_desauce", nullable = false, columnDefinition="BOOLEAN DEFAULT TRUE")
    private Boolean deSauce;
    
    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;
    
    @Column(name = "dl_secretario")
    private String secretario;

    @Column(name = "dl_jefeestudios")
    private String jefeEstudios;
       

  /*  @OneToOne
    @JoinColumn(name="dc_tipo_centro",referencedColumnName="cn_tipo_centro",unique = false)
    @Cascade({CascadeType.ALL})
    private TipoCentro tipo;*/
    

    @OneToOne
    @JoinColumn(name="cn_municipio",referencedColumnName="cn_municipio")    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private Municipio municipio;
    
    
    /**
     * Constructor para CentroComboListMapping
     */
	public Centro(String idCentro, @NotNull String codigo, @NotNull String nombre, String cif) {
		this.idCentro = idCentro;
		this.codigo = codigo;
		this.nombre = nombre;
		this.cif = cif;
	}
  
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Centro centro = (Centro) o;
        if (centro.getIdCentro() == null || getIdCentro() == null) {
            return false;
        }
        return Objects.equals(getIdCentro(), centro.getIdCentro());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdCentro());
    }


    
}

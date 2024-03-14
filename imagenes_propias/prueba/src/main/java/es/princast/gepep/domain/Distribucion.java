package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.List;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Distribucion.
 */
@Entity
@Table(name = "distribucion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Distribucion extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_distribucion")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqDistribucion")
    @SequenceGenerator(name = "seqDistribucion", sequenceName ="sec_distribucion", allocationSize=1)
    private Long idDistribucion;

    @NotNull
    @Column(name = "nu_dia_ini", nullable = false)
    private Integer diaIni;

    @NotNull
    @Column(name = "nu_mes_ini", nullable = false)
    private Integer mesIniDistribucion;

    @NotNull
    @Column(name = "nu_anio_ini", nullable = false)
    private Integer anioIni;

    @NotNull
    @Column(name = "nu_dia_fin", nullable = false)
    private Integer diaFin;

    @NotNull
    @Column(name = "nu_mes_fin", nullable = false)
    private Integer mesFinDistribucion;

    @NotNull
    @Column(name = "nu_anio_fin", nullable = false)
    private Integer anioFin;

    @Column(name = "dc_centro",length=200)
    private String centro;

    @Column(name = "nu_h_entrada_man")
    private Integer hEntradaMan;

    @Column(name = "nu_h_salida_man")
    private Integer hSalidaMan;

    @Column(name = "nu_min_entrada_man")
    private Integer minEntradaMan;

    @Column(name = "nu_min_salida_man")
    private Integer minSalidaMan;

    @Column(name = "nu_h_entrada_tard")
    private Integer hEntradaTard;

    @Column(name = "nu_h_salida_tard")
    private Integer hSalidaTard;

    @Column(name = "nu_min_entrada_tard")
    private Integer minEntradaTard;

    @Column(name = "nu_min_salida_tard")
    private Integer minSalidaTard;

    @Column(name = "dc_tutor_empresa")
    private String tutorEmpresa;

    @Column(name = "te_horario_flex",length = 1000)
    private String horarioFlexible;

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;
    
  
    @Column(name = "fe_finicio" )
    private LocalDate fechaInicio;

 	@Lob
    @Type(type="text")
	@Column(name = "te_realizacion", nullable = false,length=10000)
	private String realizacion;
    
 
    @Column(name = "fe_ffin" )
    private LocalDate fechaFin;
    
    @OneToOne
    @JoinColumn(name = "ca_matricula",referencedColumnName="ca_matricula", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler", "distribucion", "matricula"}, allowSetters = true)   
    private Matricula matricula;
    
    
    @OneToOne
    @JoinColumn(name = "cn_anexo",referencedColumnName="cn_anexo", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler", "distribucion", "anexoContrato"}, allowSetters = true)   
    private AnexoContrato anexoContrato;
    
    
    @Transient
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler", "distribucion"}, allowSetters = true)  
    private DistribucionPeriodo distribucionPeriodo;
    
    @Transient
    @JsonIgnoreProperties(value={"ciclo", "distribucion","contenidosEF"}, allowSetters = true)
    private List<ContenidoEF> contenidosEF;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distribucion dis = (Distribucion) o;
        if (dis.getIdDistribucion() == null || getIdDistribucion() == null) {
            return false;
        }
        return Objects.equals(getIdDistribucion(), dis.getIdDistribucion());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdDistribucion());
    }
    
}

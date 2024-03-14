package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.princast.gepep.service.util.GepepHelper;
import es.princast.gepep.repository.ImportesTipoGasto2Repository;
import es.princast.gepep.domain.ImportesTipoGasto2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A GastoAlumno.
 */
@Entity
@Table(name = "gasto_alumno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class GastoAlumno extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_gasto_alumno")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGastoAlumno")
    @SequenceGenerator(name = "seqGastoAlumno", sequenceName ="sec_gasto_alumno",allocationSize=1)
    private Integer idGastoAlumno;

    @Column(name = "im_precio_billete",columnDefinition="FLOAT DEFAULT 0.0")
    private Float precioBillete;


    @Column(name = "im_num_billete", columnDefinition="INTEGER DEFAULT 0")
    private Integer numBillete;


    @Column(name = "im_precio_dieta",columnDefinition="FLOAT DEFAULT 0.0")
    private Float precioDieta;


    @Column(name = "nu_num_dieta", columnDefinition="INTEGER DEFAULT 0")
    private Integer numDieta;


    @Column(name = "im_precio_pension", columnDefinition="FLOAT DEFAULT 0.0")
    private Float precioPension;


    @Column(name = "nu_num_pension",  columnDefinition="INTEGER DEFAULT 0")
    private Integer numPension;


    @Column(name = "im_precio_km", columnDefinition="FLOAT DEFAULT 0.00")
    private Float precioKm;


    @Column(name = "nu_num_km",columnDefinition="INTEGER DEFAULT 0")
    private Integer numKm;


    @Column(name = "im_otros_gastos", columnDefinition="FLOAT DEFAULT 0.0")
    private Float otrosGastos;
 
    @Column(name = "ca_alumno")
    private String alumno;

    @Column(name = "nu_distancia_unitaria",columnDefinition="FLOAT DEFAULT 0.0")
    private Float distanciaUnitaria ;

    @Column(name = "nu_numero_dias",columnDefinition="INTEGER DEFAULT 0")
    private Integer numeroDias;

    @Column(name = "dl_localidad_centro_trabajo")
    private String localidadCentroTrabajo;

    @Column(name = "fl_Validado")
    private Boolean flValidado ;

    @Column(name = "fe_fvalidacion")
    private LocalDate fValidacion ;

    @OneToOne
    @JoinColumn(name="cn_periodo_liquidacion", referencedColumnName="cn_periodo_liquidacion",unique = false)   
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PeriodoLiquidacion periodoLiquidacion; 
    
    @ManyToOne
    @JoinColumn(name="ca_matricula", referencedColumnName="ca_matricula",unique = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","matricula", "ofertaCentro"}, allowSetters =true)
    private Matricula matricula; 

    
    @OneToOne
    @JoinColumn(name="cn_distribucion_periodo", referencedColumnName="cn_distribucion_periodo",unique = false)   
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler", "matricula"}, allowSetters = true)
    private DistribucionPeriodo distribucionPeriodo;
    
    
    
    @Transient
    private Float gastoTotal;

    @Transient
    private Integer gastoTotal2;

    @Transient
    private Float distanciaTotal2;

    @Autowired
    @Transient
    private ImportesTipoGasto2Repository importesTipoGasto2Repository;
    
    public Float getGastoTotal() {
    	return calculaTotalGastos();
    }
    public Integer getdistanciaTotal2() {
        return calculaDistanciaTotalGastos2();
    }
    
    public Float calculaTotalGastos() {
        Float totalGastos = Float.valueOf(0);
        // Gasto Billetes
        if (this.getPrecioBillete() != null && this.getNumBillete() != null) {
            totalGastos += this.getPrecioBillete() * this.getNumBillete();
        }
        // Gasto Dietas
        if (this.getPrecioDieta() != null && this.getNumDieta() != null) {
            totalGastos += this.getPrecioDieta() * this.getNumDieta();
        }
        if (this.getPrecioPension() != null && this.getNumPension() != null) {
            // Gasto Pension
            totalGastos += this.getPrecioPension() * this.getNumPension();
        }
        // Gasto KM
        if (this.getPrecioKm() != null && this.getNumKm() != null) {
            totalGastos += this.getPrecioKm() * this.getNumKm();
        }
        // Otros Gastos
        if (this.getOtrosGastos() != null ) {
            totalGastos += this.getOtrosGastos();
        }
        return GepepHelper.redondearDecimalesFloat(totalGastos, 2);
    }




    public Integer calculaDistanciaTotalGastos2() {
        Float totaldistancia = Float.valueOf(0);
        // Gasto Billetes
        if (this.getDistanciaUnitaria() != null && this.getNumeroDias() != null) {
            totaldistancia += 2 * this.getDistanciaUnitaria() * this.getNumeroDias();
        }
        return totaldistancia.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GastoAlumno gastoAlumno = (GastoAlumno) o;
        if (gastoAlumno.getIdGastoAlumno() == null || getIdGastoAlumno() == null) {
            return false;
        }
        return Objects.equals(getIdGastoAlumno(), gastoAlumno.getIdGastoAlumno());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdGastoAlumno());
    }
    
    
}

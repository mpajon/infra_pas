package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A SeguimientoFinal.
 */
@Entity
@Table(name = "seguimiento_final")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class SeguimientoFinal extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_seguimiento_final")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSgmtoFinal")
    @SequenceGenerator(name = "seqSgmtoFinal", sequenceName="sec_seguimiento",allocationSize=1)
    private Long idSeguimientoFinal;

    @NotNull
    @Column(name = "fl_titulacion", nullable = false, columnDefinition="BOOLEAN DEFAULT FALSE")
    private Boolean obtuvotitulacion;


    @Column(name = "fl_trab_misma_empresa")
    private Boolean trabajaMismaEmpresa;

 
    @Column(name = "fl_trab_otra_empresa")
    private Boolean trabajaOtraEmpresa;

  
    @Column(name = "fl_trab_misma_profesion")
    private Boolean trabajaMismaProfesion;


    @Column(name = "fl_trab_otra_profesion")
    private Boolean trabajaOtraprofesion;

    @Column(name = "fl_cursa_estudios_ocup")
    private Boolean cursaEstudiosOcup;

 
    @Column(name = "fl_cursa_estudios_univ")
    private Boolean cursaEstudiosUniv;

  
    @Column(name = "fl_trab_cuenta_propia")
    private Boolean trabajaCuentaPropia;

  
    @Column(name = "fl_en_paro")
    private Boolean enParo;


    @Column(name = "fl_sit_desconocida")
    private Boolean situacionDesconocida;

 
    @Column(name = "fl_cuenta_ajena")
    private Boolean cuentaAjena;

    @Column(name = "fl_cursa_ciclo")
    private Boolean cursaCiclo;
  
    @Column(name = "fl_cursa_bach")
    private Boolean cursaBachillerato;

    @Column(name = "fl_acciones_orientacion")
    private Boolean accionesOrientacion;

 
    @Column(name = "fl_escuela_taller")
    private Boolean escuelaTaller;

  
    @Column(name = "fl_otros_estudios")
    private Boolean otrosEstudios;


    @Column(name = "dl_texto_otros_ciclo")
    private String textoOtrosCiclo;


    @Column(name = "dl_texto_univ")
    private String textoUniversitarios;

    @Column(name = "dl_texto_otros_estudios")
    private String textoOtrosEstudios;
    
    @OneToOne
    @JoinColumn(name = "ca_matricula",referencedColumnName="ca_matricula", nullable = false)
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler", "seguimientoFinal", "matricula"}, allowSetters = true)   
    private Matricula matricula;
    
    
    public SeguimientoFinal (Long idSeguimientoFinal) {
    	this.idSeguimientoFinal = idSeguimientoFinal;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeguimientoFinal seguimiento = (SeguimientoFinal) o;
        if (seguimiento.getIdSeguimientoFinal() == null || getIdSeguimientoFinal() == null) {
            return false;
        }
        return Objects.equals(getIdSeguimientoFinal(), seguimiento.getIdSeguimientoFinal());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdSeguimientoFinal());
    }
   
}

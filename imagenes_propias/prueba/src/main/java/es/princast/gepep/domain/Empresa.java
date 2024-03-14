package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Empresa extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_empresa")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secEmpresa")
    @SequenceGenerator(name = "secEmpresa",sequenceName ="sec_empresa",allocationSize=1)    
    private Long idEmpresa;

    @NotNull
    @Column(name = "if_cif", nullable = false,length = 15)
    private String cif;

    @NotNull
    @Column(name = "ti_tipo", nullable = false,length = 250)
    private String tipo;

    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @Column(name = "fl_es_publica")
    private Boolean esPublica;

    @Column(name = "fl_es_pyme")
    private Boolean esPyme;

    @Column(name = "te_observaciones",length = 1000)
    private String observaciones;

    @Column(name = "fl_es_arsi")
    private Boolean esArsi;

    @Column(name = "fl_es_arma")
    private Boolean esArma;

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;
    
    @Column(name = "te_ciclos",length = 2000)
    private String ciclos;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empresa em = (Empresa) o;
        if (em.getIdEmpresa() == null || getIdEmpresa() == null) {
            return false;
        }
        return Objects.equals(getIdEmpresa(), em.getIdEmpresa());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdEmpresa());
    }

}

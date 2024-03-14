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

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Precios.
 */
@Entity
@Table(name = "precios")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Precios extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_precios")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPrecios")
    @SequenceGenerator(name = "seqPrecios", sequenceName="sec_precios", allocationSize=1)
    private Long idPrecio;

    @Column(name = "im_precio_km")
    private Float precioKm;

    @Column(name = "im_precio_bill")
    private Float precioBilletes;

    @Column(name = "im_precio_mmanut")
    private Float precioMediaManut;

    @Column(name = "im_precio_entmanut")
    private Float precioEnteraManut;

    @Column(name = "im_precio_tmanut")
    private Float precioCompletaManut;
    
    @Column(name = "im_precio_alojamiento")
    private Float precioAlojamiento;

    @Column(name = "im_precio_km_alumnos")
    private Float precioKmAlumnos;

    @Column(name = "im_precio_max_dieta_alumnos")
    private Float precioMaxDietaAlumnos;

    @Column(name = "im_precioMaxPensionAlumno")
    private Float precioMaxPensionAlumno;

    @Column(name = "im_compempresa")
    private Float compensacionEmpresa;

    @OneToOne
    @JoinColumn(name="cn_tipo_practica",referencedColumnName="cn_tipo_practica",unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   
    private TipoPractica tipoPractica;
  
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Precios precios = (Precios) o;
        if (precios.getIdPrecio() == null || getIdPrecio() == null) {
            return false;
        }
        return Objects.equals(getIdPrecio(), precios.getIdPrecio());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPrecio());
    }
}

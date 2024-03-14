package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A PracticaOferta.
 */
@Entity
@Table(name = "unidad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Unidad extends AbstractAuditingEntity implements Cloneable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ca_unidad",nullable =false) 
    @Id
    private String idUnidad;   
    
    @Column(name = "ca_nombre")   
    private String nombre;  
   
    @ManyToOne
    @JoinColumn(name = "ca_ofercen", referencedColumnName="ca_ofercen",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"}, allowSetters = true)  
    private OfertasCentro ofertaCentro;
    
    @ManyToOne
    @JoinColumn(name = "ca_tutor", referencedColumnName="ca_profesor",nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","tutor", "unidad"}, allowSetters =true)
    private Profesor tutor;
    
    @ManyToOne
    @JoinColumn(name = "ca_tutor_adicional", referencedColumnName="ca_profesor",nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","tutorAdicional", "unidad"}, allowSetters =true)
    private Profesor tutorAdicional;
    
    @Column(name = "nu_anio")
    private Integer anio;

    @Column(name = "cn_curso")   
    private Integer curso;   
    
    @Column(name = "ca_turno")   
    private String turno;   
  
    @Column(name = "cn_alumnos")   
    private Integer numAlumnos;   
    
    @Column(name = "cn_capacidad")   
    private Integer capacidad;
    
    @Column(name = "fl_desauce", nullable = false, columnDefinition="BOOLEAN DEFAULT TRUE")
    private Boolean deSauce;
    
    
    public Unidad(String idUnidad) {
    	this.idUnidad = idUnidad;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Unidad unidad = (Unidad) o;
        if (unidad.getIdUnidad() == null || getIdUnidad() == null) {
            return false;
        }
        return Objects.equals(getIdUnidad(), unidad.getIdUnidad());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdUnidad());
    }
    
    public Unidad clone() throws CloneNotSupportedException {
        return (Unidad) super.clone();
    }
	
}

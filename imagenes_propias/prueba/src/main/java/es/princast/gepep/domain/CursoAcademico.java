package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sector.
 */
@Entity
@Table(name = "curso_academico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class CursoAcademico extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

 
    @Column(name = "nu_anio")
    @Id
    private Integer idAnio;
    
    @NotNull
    @Column(name = "fe_inicio", nullable = false)
    private LocalDate fe_inicio;

    @NotNull
    @Column(name = "fe_final", nullable = false)
    private LocalDate fe_final;

    @NotNull
    @Column(name = "fl_actual", nullable = false)
    private Boolean actual;
        
    @NotNull
    @Column(name = "dc_anio", nullable = false)
    private String descripcion;
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CursoAcademico curso = (CursoAcademico) o;
        if (curso.getIdAnio() == null || getIdAnio() == null) {
            return false;
        }
        return Objects.equals(getIdAnio(), curso.getIdAnio());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdAnio());
    }

}

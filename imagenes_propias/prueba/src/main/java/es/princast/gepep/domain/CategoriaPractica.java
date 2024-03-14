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
 * A CategoriaEmpresa.
 */
@Entity
@Table(name = "categoria_practica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class CategoriaPractica extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_categoria")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqcategoria")
    @SequenceGenerator(name = "seqcategoria", sequenceName ="sec_categoria",allocationSize=1)
    private Long idCategoria;

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;

    
    @NotNull
    @Column(name = "dc_categoria", nullable = false)
    private String categoria;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoriaPractica cat = (CategoriaPractica) o;
        if (cat.getIdCategoria() == null || getIdCategoria() == null) {
            return false;
        }
        return Objects.equals(getIdCategoria(), cat.getIdCategoria());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdCategoria());
    }
}

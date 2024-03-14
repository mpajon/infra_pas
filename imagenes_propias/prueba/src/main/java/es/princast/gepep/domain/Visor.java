package es.princast.gepep.domain;

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

import io.swagger.annotations.ApiModel;

/**
 * Responables Visor.
 * Puede ser uno para todas las enseñanzas, uno por enseñanaza
 * Se define por enseñanza
 */
@ApiModel(description = "Responable Visor. Puede ser uno para todas las enseñanzas, uno por enseñanaza Se define por enseñanza")
@Entity
@Table(name = "visor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
//@ToString
@Getter
@Setter
public class Visor extends AbstractAuditingEntity  {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_visor")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqVisor")
    @SequenceGenerator(name = "seqVisor", sequenceName="sec_visor", allocationSize=1)
    private Long idVisor;

    @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "dl_apellidos", nullable = false)
    private String apellidos;

    @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Visor visor = (Visor) o;
		return !(visor.getIdVisor() == null || getIdVisor() == null)
				&& Objects.equals(getIdVisor(), visor.getIdVisor());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getIdVisor());
	}


   
}

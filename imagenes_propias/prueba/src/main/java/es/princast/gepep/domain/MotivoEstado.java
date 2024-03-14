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

/**
 * A MotivoEstado.
 */
@Entity
@Table(name = "motivo_estado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class MotivoEstado extends AbstractAuditingEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "cn_motivo_estado")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqMotivo")
	@SequenceGenerator(name = "seqMotivo",sequenceName="sec_motivoestado", allocationSize=1)
	private Long idMotivoEstado;

	@NotNull
	@Column(name = "dl_motivo", nullable = false)
	private String motivo;
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (o == null || getClass() != o.getClass()) {
	            return false;
	        }
	        MotivoEstado motivo = (MotivoEstado) o;
	        if (motivo.getIdMotivoEstado() == null || getIdMotivoEstado() == null) {
	            return false;
	        }
	        return Objects.equals(getIdMotivoEstado(), motivo.getIdMotivoEstado());
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hashCode(getIdMotivoEstado());
	    }
}

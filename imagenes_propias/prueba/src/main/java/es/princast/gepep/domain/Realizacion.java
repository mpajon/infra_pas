package es.princast.gepep.domain;

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
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Realizacion.
 */
@Entity
@Table(name = "realizacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Realizacion extends AbstractAuditingEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "cn_realizacion")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRealizacion")
	@SequenceGenerator(name = "seqRealizacion", sequenceName="sec_realizacion", allocationSize=1)
	private Long idRealizacion;

	@NotNull
	@Lob
    @Type(type="text")
	@Column(name = "te_realizacion", nullable = false,length=10000)
	private String realizacion;

	 @OneToOne
	 @JoinColumn(name = "ca_ciclo", referencedColumnName="ca_ciclo",nullable = false)
	 @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Ciclo ciclo;
	 
	  @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (o == null || getClass() != o.getClass()) {
	            return false;
	        }
	        Realizacion realizacion = (Realizacion) o;
	        if (realizacion.getIdRealizacion() == null || getIdRealizacion() == null) {
	            return false;
	        }
	        return Objects.equals(getIdRealizacion(), realizacion.getIdRealizacion());
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hashCode(getIdRealizacion());
	    }

}

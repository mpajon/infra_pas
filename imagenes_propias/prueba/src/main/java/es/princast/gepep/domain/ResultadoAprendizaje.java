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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A ResultadoAprendizaje.
 */
@Entity
@Table(name = "resultado_aprendizaje")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class ResultadoAprendizaje extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_resultado")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqResultado")
    @SequenceGenerator(name = "seqResultado", sequenceName="sec_resultado", allocationSize=1)
    private Long idResultadoAprendizaje;

    @NotNull
    @Lob
    @Type(type="text")
    @Column(name = "te_resultado", nullable = false,length=4000)
    private String resultado;
    
    @OneToOne 
    @JoinColumn(name = "ca_ciclo",referencedColumnName = "ca_ciclo",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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
		ResultadoAprendizaje resultado = (ResultadoAprendizaje) o;
		if (resultado.getIdResultadoAprendizaje() == null || getIdResultadoAprendizaje() == null) {
			return false;
		}
		return Objects.equals(getIdResultadoAprendizaje(), resultado.getIdResultadoAprendizaje());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getIdResultadoAprendizaje());
	}

    
}

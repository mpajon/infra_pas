package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A ResponsableArea.
 */
@Entity
@Table(name = "responsable_area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class ResponsableArea extends AbstractAuditingEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "cn_responsable_area")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRespArea")
	@SequenceGenerator(name = "seqRespArea", sequenceName = "sec_responsable_area", allocationSize = 1)
	private Long idResponsableArea;

	@NotNull
	@Column(name = "dc_nombre", nullable = false)
	private String nombre;
	
    @NotNull
    @Column(name = "ti_tipo", nullable = false,length = 250)
    private String tipoDocumento;

	@NotNull
	@Column(name = "if_nif", nullable = false)
	private String nif;

	@ManyToOne
	@JoinColumn(name = "cn_area", referencedColumnName = "cn_area", unique = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Area area;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ResponsableArea responsableArea = (ResponsableArea) o;
		if (responsableArea.getIdResponsableArea() == null || getIdResponsableArea() == null) {
			return false;
		}
		return Objects.equals(getIdResponsableArea(), responsableArea.getIdResponsableArea());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getIdResponsableArea());
	}

}

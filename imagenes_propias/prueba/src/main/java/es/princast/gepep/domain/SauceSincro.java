package es.princast.gepep.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import io.swagger.annotations.ApiModel;

/**
 * Fechas de sincronización con Sauce
 */
@ApiModel(description = "Sincronización con sauce")
@Entity
@Table(name = "saucesincro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@EqualsAndHashCode(callSuper = false)
public class SauceSincro extends AbstractAuditingEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "ca_id")
	@Id
	private String idSincro;

	@Column(name = "ca_descripcion", nullable = true)
	private String descripcion;

	@Column(name = "fe_fultimaactualizacion")
	private Instant fechaActualizacion;

	@Column(name = "fe_fexito")
	private Instant fechaExito;

	@Column(name = "nu_orden")
	private Integer orden;

	@Transient
	private String entidad;

	@Transient
	private String finicio;
	
	@Transient
	private Integer cursoAcademico;
	
	
}

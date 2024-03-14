package es.princast.gepep.domain;

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
 * A EstadoEmpresa.
 */
@Entity
@Table(name = "estado_empresa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class EstadoEmpresa extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_estado_empresa")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqestadoempresa")
    @SequenceGenerator(name = "seqestadoempresa", sequenceName ="sec_estado_empresa",allocationSize=1)
    private Long idEstadoEmpresa;

    @NotNull
    @Column(name = "dc_estado", nullable = false)
    private String estado;
}

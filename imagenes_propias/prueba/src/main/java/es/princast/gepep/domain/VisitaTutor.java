package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A VisitaTutor.
 */
@Entity
@Table(name = "visita_tutor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class VisitaTutor extends AbstractAuditingEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "cn_visita_tutor")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqVisitaTutor")
	@SequenceGenerator(name = "seqVisitaTutor", sequenceName = "sec_visita_tutor", allocationSize = 1)
	private Long idVisitaTutor;

	@Column(name = "dl_empresa")
	private String empresa;

	/*
	 * @Column(name = "fe_fprevista") private LocalDate fechaPrevista;
	 */

	@Column(name = "nu_anio")
	private Integer anio;

	@Column(name = "nu_mes")
	private Integer mes;

	@Column(name = "nu_dia")
	private Integer dia;

	@NotNull
	@Column(name = "nu_km", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer km;

	@NotNull
	@Column(name = "im_imp_km", nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
	private Float importeKm;

	@NotNull
	@Column(name = "im_imp_billetes", nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
	private Float importeBilletes;

	@NotNull
	@Column(name = "im_imp_dietas", nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
	private Float importeDietas;

	@NotNull
	@Column(name = "im_imp_otros", nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
	private Float importeOtros;

	@NotNull
	@Column(name = "im_imp_total", nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
	private Float importeTotal;

	@Column(name = "fl_bloqueada")
	private Boolean bloqueada;

	@Column(name = "fl_autorizada")
	private Boolean autorizada;

	@Column(name = "fl_realizada")
	private Boolean realizada;

	@Column(name = "fl_mismo_municipio")
	private Boolean mismoMunicipio;

	@Column(name = "fl_transp_publico", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean transportePublico;

	@Column(name = "fl_dietas", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean dietas;

	@Column(name = "nu_hora_ent")
	private Integer horaEntrada;

	@Column(name = "nu_min_ent")
	private Integer minEntrada;

	@Column(name = "nu_hora_sal")
	private Integer horaSalida;

	@Column(name = "nu_min_salida")
	private Integer minSalida;

	@Column(name = "dl_loc_origen")
	private String localidadOrigen;

	@Column(name = "dl_loc_destino")
	private String localidadDestino;

	@Column(name = "dl_itinerario")
	private String itinerario;

	@Column(name = "fl_motivo1")
	private Boolean motivo1;

	@Column(name = "fl_motivo2")
	private Boolean motivo2;

	@Column(name = "fl_motivo3")
	private Boolean motivo3;

	@Column(name = "fl_motivo4")
	private Boolean motivo4;

	@Column(name = "te_observaciones", length = 2000)
	private String observaciones;

	@ManyToOne
	@JoinColumn(name = "ca_profesor", referencedColumnName = "ca_profesor", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Profesor profesor;

	@OneToOne
	@JoinColumn(name = "ca_ciclo", referencedColumnName = "ca_ciclo", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Ciclo ciclo;

	@OneToOne
	@JoinColumn(name = "cn_tipo_practica", referencedColumnName = "cn_tipo_practica", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private TipoPractica tipoPractica;

	@Transient
	private Centro centro;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		VisitaTutor visitaTutor = (VisitaTutor) o;
		return !(visitaTutor.getIdVisitaTutor() == null || getIdVisitaTutor() == null)
				&& Objects.equals(getIdVisitaTutor(), visitaTutor.getIdVisitaTutor());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getIdVisitaTutor());
	}

	public VisitaTutor(VisitaTutor v, Centro c) {

		this.idVisitaTutor = v.idVisitaTutor;
		this.empresa = v.empresa;
		this.anio = v.anio;
		this.mes = v.mes;
		this.dia = v.dia;
		this.km = v.km;
		this.importeKm = v.importeKm;
		this.importeBilletes = v.importeBilletes;
		this.importeDietas = v.importeDietas;
		this.importeOtros = v.importeOtros;
		this.importeTotal = v.importeTotal;
		this.bloqueada = v.bloqueada;
		this.autorizada = v.autorizada;
		this.realizada = v.realizada;
		this.mismoMunicipio = v.mismoMunicipio;
		this.transportePublico = v.transportePublico;
		this.dietas = v.dietas;
		this.horaEntrada = v.horaEntrada;
		this.minEntrada = v.minEntrada;
		this.horaSalida = v.horaSalida;
		this.minSalida = v.minSalida;
		this.localidadOrigen = v.localidadOrigen;
		this.localidadDestino = v.localidadDestino;
		this.itinerario = v.itinerario;
		this.motivo1 = v.motivo1;
		this.motivo2 = v.motivo2;
		this.motivo3 = v.motivo3;
		this.motivo4 = v.motivo4;
		this.observaciones = v.observaciones;
		this.profesor = v.profesor;
		this.ciclo = v.ciclo;
		this.tipoPractica = v.tipoPractica;

		// Transient //
		this.centro = c;

	}

}

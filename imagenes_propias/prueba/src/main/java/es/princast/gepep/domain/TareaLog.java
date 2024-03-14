package es.princast.gepep.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A tareaLog.
 */
@Entity
@Table(name = "tarea_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class TareaLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "cn_tarea_log")
	@Id
	private BigInteger idTareaLog;

	@Column(name = "cn_tarea_id")
	private BigInteger idTarea;

	@NotNull
	@Column(name = "dc_tarea", nullable = false)
	private String tarea;

	@Column(name = "dc_subtarea")
	private String subtarea;

	@Column(name = "dl_mensaje")
	private String mensaje;

	@Column(name = "dl_error")
	private String error;

	@Column(name = "dl_exception")
	private String exception;

	@Column(name = "dl_traza")
	private String traza;

	@CreatedBy
	@Column(name = "ca_usucrea", columnDefinition = "VARCHAR(50) DEFAULT 'SYSTEM'", nullable = false, updatable = false)
	@JsonIgnore
	@Builder.Default
	private String createdBy = "SYSTEM";

	@CreatedDate
	@Column(name = "fe_fcreacion", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
	@JsonIgnore
	@Builder.Default
	private Instant createdDate = Instant.now();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TareaLog aux = (TareaLog) o;
		if (aux.getIdTareaLog() == null || getIdTareaLog() == null) {
			return false;
		}
		return Objects.equals(getIdTareaLog(), aux.getIdTareaLog());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getIdTareaLog());
	}

	@Override
	public String toString() {
		return "TareaLog [idTareaLog=" + idTareaLog + ", tarea=" + tarea + ", subtarea=" + subtarea + ", mensaje="
				+ mensaje + ", error=" + error + ", exception=" + exception + ", traza=" + traza + "]";
	}

}

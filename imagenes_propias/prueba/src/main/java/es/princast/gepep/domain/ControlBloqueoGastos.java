package es.princast.gepep.domain;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@ToString
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedNativeQueries({
@NamedNativeQuery(name ="ControlBloqueoGastos.getPeriodosResumen",
		query ="select distinct(ca.nu_anio) anio, tp.cn_tipo_practica idTipoPractica, tp.dc_nombre nombreTipoPractica, pl.cn_periodo_liquidacion idPeriodo, " +
				"	    pl.dc_nombre nombrePeriodo, bg.cn_bloqueo_gastos idBloqueo, bg.fl_bloqueo bloqueado " +
				"from " +
				"		tipo_practica tp " + 
				"		inner join periodos_liquidacion pl on pl.cn_tipo_practica = tp.cn_tipo_practica " + 
				"		inner join periodo_practica pp on pp.cn_tipo_practica = tp.cn_tipo_practica " + 
				"		inner join curso_academico ca on ca.nu_anio = pp.cn_curso_aca " + 
				"		left join bloqueo_gastos bg on bg.cn_periodo_liquidacion = pl.cn_periodo_liquidacion " +	
				"where (tp.fe_finvigencia is null or tp.fe_finvigencia > current_date) " +
				"order by anio desc, nombreTipoPractica, idPeriodo ",
		resultSetMapping = "ControlBloqueoGastosResumenMapping"),
@NamedNativeQuery(name ="ControlBloqueoGastos.getPeriodosResumenByAnio",
query ="select distinct(ca.nu_anio) anio, tp.cn_tipo_practica idTipoPractica, tp.dc_nombre nombreTipoPractica, pl.cn_periodo_liquidacion idPeriodo, " +
		"	    pl.dc_nombre nombrePeriodo, bg.cn_bloqueo_gastos idBloqueo, bg.fl_bloqueo bloqueado " +
		"from " +
		"		tipo_practica tp " + 
		"		inner join periodos_liquidacion pl on pl.cn_tipo_practica = tp.cn_tipo_practica " + 
		"		inner join periodo_practica pp on pp.cn_tipo_practica = tp.cn_tipo_practica " + 
		"		inner join curso_academico ca on ca.nu_anio = pp.cn_curso_aca " + 
		"		left join bloqueo_gastos bg on bg.cn_periodo_liquidacion = pl.cn_periodo_liquidacion and bg.cn_anio= :anio " +
		"where ca.nu_anio = :anio " +
		" and (tp.fe_finvigencia is null or tp.fe_finvigencia > current_date) " +
		"order by anio desc, nombreTipoPractica, idPeriodo ",
resultSetMapping = "ControlBloqueoGastosResumenMapping"),

@NamedNativeQuery(name ="ControlBloqueoGastos.getPeriodosResumenByAnioAndTipoPractica",
query ="select distinct(ca.nu_anio) anio, tp.cn_tipo_practica idTipoPractica, tp.dc_nombre nombreTipoPractica, pl.cn_periodo_liquidacion idPeriodo, " +
		"	    pl.dc_nombre nombrePeriodo, bg.cn_bloqueo_gastos idBloqueo, bg.fl_bloqueo bloqueado " +
		"from " +
		"		tipo_practica tp " + 
		"		inner join periodos_liquidacion pl on pl.cn_tipo_practica = tp.cn_tipo_practica " + 
		"		inner join periodo_practica pp on pp.cn_tipo_practica = tp.cn_tipo_practica " + 
		"		inner join curso_academico ca on ca.nu_anio = pp.cn_curso_aca " + 
		"		left join bloqueo_gastos bg on bg.cn_periodo_liquidacion = pl.cn_periodo_liquidacion and bg.cn_anio= :anio " +
		"where ca.nu_anio = :anio and tp.cn_tipo_practica = :idTipoPrac " +
		" and (tp.fe_finvigencia is null or tp.fe_finvigencia > current_date) " +
		"order by anio desc, nombreTipoPractica, idPeriodo ",
resultSetMapping = "ControlBloqueoGastosResumenMapping"),

@NamedNativeQuery(name ="ControlBloqueoGastos.getPeriodosResumenByAnioAndTipoPracticaAndPeriodoLiquidacion",
query ="select distinct(ca.nu_anio) anio, tp.cn_tipo_practica idTipoPractica, tp.dc_nombre nombreTipoPractica, pl.cn_periodo_liquidacion idPeriodo, " +
		"	    pl.dc_nombre nombrePeriodo, bg.cn_bloqueo_gastos idBloqueo, bg.fl_bloqueo bloqueado " +
		"from " +
		"		tipo_practica tp " + 
		"		inner join periodos_liquidacion pl on pl.cn_tipo_practica = tp.cn_tipo_practica " + 
		"		inner join periodo_practica pp on pp.cn_tipo_practica = tp.cn_tipo_practica " + 
		"		inner join curso_academico ca on ca.nu_anio = pp.cn_curso_aca " + 
		"		left join bloqueo_gastos bg on bg.cn_periodo_liquidacion = pl.cn_periodo_liquidacion and bg.cn_anio= :anio " +
		"where ca.nu_anio = :anio and tp.cn_tipo_practica = :idTipoPrac and pl.cn_periodo_liquidacion = :idPerLiq " +
		" and (tp.fe_finvigencia is null or tp.fe_finvigencia > current_date) " +
		"order by anio desc, nombreTipoPractica, idPeriodo ", 		
resultSetMapping = "ControlBloqueoGastosResumenMapping")
})


@SqlResultSetMappings({

	@SqlResultSetMapping(name="ControlBloqueoGastosResumenMapping",
	        classes = {
	                @ConstructorResult(targetClass = ControlBloqueoGastos.class,
	                        columns = {@ColumnResult(name="anio", type=Integer.class), 
	                        		@ColumnResult(name="idTipoPractica", type=Integer.class),
	                        		@ColumnResult(name="nombreTipoPractica", type=String.class),
	                        		@ColumnResult(name="idPeriodo", type=Integer.class),
	                        		@ColumnResult(name="nombrePeriodo", type=String.class),
	                        		@ColumnResult(name="idBloqueo", type=Integer.class),
	                        		@ColumnResult(name="bloqueado", type=Boolean.class)
	                        		}
	                )})
	
	
})

public class ControlBloqueoGastos implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	public Integer anio;
	public Integer idTipoPractica;
	public String nombreTipoPractica;
	public Integer idPeriodo;
	public String nombrePeriodo;
	public Integer idBloqueo;	
	public Boolean bloqueado;

	public ControlBloqueoGastos() {
	}

	public ControlBloqueoGastos(Integer anio, Integer idTipoPractica, String nombreTipoPractica, Integer idPeriodo,
			String nombrePeriodo, Integer idBloqueo, Boolean bloqueado) {
		super();
		this.anio = anio;
		this.idTipoPractica = idTipoPractica;
		this.idPeriodo = idPeriodo;
		this.idBloqueo = idBloqueo;
		this.nombreTipoPractica = nombreTipoPractica;
		this.nombrePeriodo = nombrePeriodo;
		this.bloqueado = bloqueado;
	}

}

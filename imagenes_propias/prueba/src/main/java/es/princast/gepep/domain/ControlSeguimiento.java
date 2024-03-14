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
@NamedNativeQuery(name ="ControlSeguimiento.getResumenControlSeguimiento",
query ="select ce.dc_codigo as codigoCentro , ce.dc_nombre as nombreCentro, ci.dc_codigo as codigoCiclo, off.ca_curso as curso, mat.ca_turno as regimen, uni.ca_nombre as unidad," + 
		"       count(distinct mat.ca_matricula) as numMatriculas," + 
		"       sum(case when (seg.fl_titulacion = true) then 1 else 0 end) as numTitulan," + 
		"       (count(distinct mat.ca_matricula) - sum(case when (seg.fl_titulacion = true) then 1 else 0 end)) as numOtros " + 
		"from" + 
		"		centro ce" + 
		"		left join ofertas_centro ofc on ofc.ca_centro = ce.ca_centro" + 
		"		left join oferta_formativa off on ofc.ca_oferta_formativa = off.ca_oferta_formativa" + 
		"		left join ciclo ci on off.ca_ciclo = ci.ca_ciclo" + 
		"		left join matricula mat on mat.cn_curso = off.ca_curso and mat.ca_ofercen = ofc.ca_ofercen" + 
		"		left join unidad uni on uni.ca_unidad = mat.ca_unidad " +
		"		left join seguimiento_final seg on seg.ca_matricula = mat.ca_matricula " + 
		"where " + 
		"	  ci.fe_fbaja is null " + 
		"     and mat.nu_anio = :anio "+
		"	  and (:idCentro = 'undefined' or ce.ca_centro = :idCentro) " + // parametro opcional (valor 'undefined') //
		"	  and (:tutor = 'undefined' or uni.ca_tutor = :tutor or uni.ca_tutor_adicional = :tutor) " + 
		"	  group by ce.dc_codigo, ce.dc_nombre, ci.dc_codigo, off.ca_curso,  mat.ca_turno, uni.ca_nombre        " + 
		"	  order by ce.dc_codigo, ce.dc_nombre, ci.dc_codigo, off.ca_curso,  mat.ca_turno, uni.ca_nombre        "
,resultSetMapping = "ControlSeguimientoResumenMapping"),

@NamedNativeQuery(name ="ControlSeguimiento.getContadoresControlSeguimiento",
query ="select ce.dc_codigo as codigoCentro , ce.dc_nombre as nombreCentro, ci.dc_codigo as codigoCiclo, off.ca_curso as curso, mat.ca_turno as regimen, uni.ca_nombre as unidad," + 
		"       count(distinct mat.ca_matricula) as numMatriculas," + 
		"       sum(case when (seg.fl_titulacion = true) then 1 else 0 end) as numTitulan," + 
		"       (count(distinct mat.ca_matricula) - sum(case when (seg.fl_titulacion = true) then 1 else 0 end)) as numOtros, " + 
		// CONTADORES //
		"				count(distinct seg.cn_seguimiento_final) as totalAlumnosConSeguimiento," + 
		"				sum(case when (seg.fl_titulacion = true) then 1 else 0 end) as alumnosTitulados," + 
		"				sum(case when (seg.fl_trab_misma_empresa = true OR seg.fl_trab_otra_empresa = true "
		+ "								OR seg.fl_trab_misma_profesion = true  OR seg.fl_trab_otra_profesion = true "
		+ "								OR seg.fl_trab_cuenta_propia = true OR seg.fl_cuenta_ajena = true ) then 1 else 0 end) as alumnosTrabajando," + 
		"				sum(case when (seg.fl_cuenta_ajena = true) then 1 else 0 end) porCuentaAjena," + 
		"				sum(case when (seg.fl_trab_misma_profesion = true and seg.fl_cuenta_ajena = true ) then 1 else 0 end) caMismoSector, " + 
		"				sum(case when (seg.fl_cuenta_ajena = true and seg.fl_trab_misma_profesion and seg.fl_trab_misma_empresa = true) then 1 else 0 end) camsMismaEmpresa, " + 
		"				sum(case when (seg.fl_cuenta_ajena = true and seg.fl_trab_misma_profesion and seg.fl_trab_otra_empresa = true) then 1 else 0 end) camsOtraEmpresa, " + 
		"				sum(case when (seg.fl_trab_otra_profesion = true and seg.fl_cuenta_ajena = true ) then 1 else 0 end) caOtroSector, " + 
		"				sum(case when (seg.fl_trab_cuenta_propia = true) then 1 else 0 end) cuentaPropia," + 
		"				sum(case when (seg.fl_trab_misma_profesion = true and seg.fl_trab_cuenta_propia = true ) then 1 else 0 end) cpMismoSector, " + 
		"				sum(case when (seg.fl_trab_otra_profesion = true and seg.fl_trab_cuenta_propia = true ) then 1 else 0 end) cpOtroSector, " + 
		"				sum(case when (seg.fl_en_paro = true) then 1 else 0 end) demandandoEmpleo," + 
		"				sum(case when (seg.fl_cursa_estudios_ocup = true) then 1 else 0 end) estudiosOcupacionales," + 
		"				sum(case when (seg.fl_acciones_orientacion = true) then 1 else 0 end) accionesBusquedaEmpleo," + 
		"				sum(case when (seg.fl_escuela_taller = true) then 1 else 0 end) escuelaTaller," + 
		"				sum(case when (seg.fl_otros_estudios = true) then 1 else 0 end) alumnosEstudiando," + 
		"				sum(case when (seg.dl_texto_otros_ciclo is null or seg.dl_texto_otros_ciclo = '') then 0 else 1 end) otroCicloFormativo," + 
		"				sum(case when (seg.fl_cursa_bach = true) then 1 else 0 end) bachillerato," + 
		"				sum(case when (seg.fl_cursa_estudios_univ = true) then 1 else 0 end) universidad," + 
		"				sum(case when (seg.fl_otros_estudios = true) then 1 else 0 end) otrosEstudios, " + 
		"				sum(case when (seg.fl_sit_desconocida = true) then 1 else 0 end) alumnosEnSituacionDesconocida " +
		"from " + 
		"		centro ce" + 
		"		left join ofertas_centro ofc on ofc.ca_centro = ce.ca_centro" + 
		"		left join oferta_formativa off on ofc.ca_oferta_formativa = off.ca_oferta_formativa" + 
		"		left join ciclo ci on off.ca_ciclo = ci.ca_ciclo" + 
		"		left join matricula mat on mat.cn_curso = off.ca_curso and mat.ca_ofercen = ofc.ca_ofercen" + 
		"		left join seguimiento_final seg on seg.ca_matricula = mat.ca_matricula " + 
		"		left join unidad uni on uni.ca_unidad = mat.ca_unidad " +
		"where " + 
		"	  ci.fe_fbaja is null " + 
		"     and mat.nu_anio = :anio "+
		"	  and (:idCentro = 'undefined' or ce.ca_centro = :idCentro) " + // parametro opcional (valor 'undefined') //
		"	  and (:tutor = 'undefined' or uni.ca_tutor = :tutor or uni.ca_tutor_adicional = :tutor) " + 
		"	  group by ce.dc_codigo, ce.dc_nombre, ci.dc_codigo, off.ca_curso, mat.ca_turno, uni.ca_nombre      " + 
		"	  order by ce.dc_codigo, ce.dc_nombre, ci.dc_codigo, off.ca_curso, mat.ca_turno, uni.ca_nombre      "
,resultSetMapping = "ControlSeguimientoContadoresMapping")

})


@SqlResultSetMappings({

	@SqlResultSetMapping(name="ControlSeguimientoResumenMapping",
	        classes = {
	                @ConstructorResult(targetClass = ControlSeguimiento.class,
	                        columns = {@ColumnResult(name="codigoCentro", type=String.class), 
	                        		@ColumnResult(name="nombreCentro", type=String.class),
	                        		@ColumnResult(name="codigoCiclo", type=String.class),
	                        		@ColumnResult(name="curso", type=Integer.class),
	                        		@ColumnResult(name="regimen", type=String.class),
	                        		@ColumnResult(name="unidad", type=String.class),
	                        		@ColumnResult(name="numMatriculas", type=Integer.class),
	                        		@ColumnResult(name="numTitulan", type=Integer.class),                        		
	                        		@ColumnResult(name="numOtros", type=Integer.class)
	                        		}
	                )}),
	
	
	@SqlResultSetMapping(name="ControlSeguimientoContadoresMapping",
	        classes = {
	                @ConstructorResult(targetClass = ControlSeguimiento.class,
	                        columns = {@ColumnResult(name="codigoCentro", type=String.class), 
	                        		@ColumnResult(name="nombreCentro", type=String.class),
	                        		@ColumnResult(name="codigoCiclo", type=String.class),
	                        		@ColumnResult(name="curso", type=Integer.class),
	                        		@ColumnResult(name="regimen", type=String.class),
	                        		@ColumnResult(name="unidad", type=String.class),
	                        		@ColumnResult(name="numMatriculas", type=Integer.class),
	                        		@ColumnResult(name="numTitulan", type=Integer.class),                        		
	                        		@ColumnResult(name="numOtros", type=Integer.class),
	                        		@ColumnResult(name="totalAlumnosConSeguimiento", type=Integer.class),
	                        		@ColumnResult(name="alumnosTitulados", type=Integer.class),
	                        		@ColumnResult(name="alumnosTrabajando", type=Integer.class),
	                        		@ColumnResult(name="porCuentaAjena", type=Integer.class),
	                        		@ColumnResult(name="caMismoSector", type=Integer.class),
	                        		@ColumnResult(name="camsMismaEmpresa", type=Integer.class),
	                        		@ColumnResult(name="camsOtraEmpresa", type=Integer.class),
	                        		@ColumnResult(name="caOtroSector", type=Integer.class),
	                        		@ColumnResult(name="cuentaPropia", type=Integer.class),
	                        		@ColumnResult(name="cpMismoSector", type=Integer.class),
	                        		@ColumnResult(name="cpOtroSector", type=Integer.class),
	                        		@ColumnResult(name="demandandoEmpleo", type=Integer.class),
	                        		@ColumnResult(name="estudiosOcupacionales", type=Integer.class),
	                        		@ColumnResult(name="accionesBusquedaEmpleo", type=Integer.class),
	                        		@ColumnResult(name="escuelaTaller", type=Integer.class),
	                        		@ColumnResult(name="alumnosEstudiando", type=Integer.class),
	                        		@ColumnResult(name="otroCicloFormativo", type=Integer.class),
	                        		@ColumnResult(name="bachillerato", type=Integer.class),
	                        		@ColumnResult(name="universidad", type=Integer.class),
	                        		@ColumnResult(name="otrosEstudios", type=Integer.class),
	                        		@ColumnResult(name="alumnosEnSituacionDesconocida", type=Integer.class)
	                        		}
	                )})
	
	
})

public class ControlSeguimiento implements Serializable {

	private static final String STRING_VACIO = "";
	private static final char SEPARADOR_CICLO = '-';
	/**
	 * 
	 */
	private static final long serialVersionUID = 6028405008813862099L;
	@Id
	public String codigoCentro;
	public String nombreCentro;
	public String codigoCiclo;
	public Integer curso; 
	public String regimen; 
	public String unidad; 
	public String ciclo; 
    
    public Integer numMatriculas; 
    public Integer numTitulan; 
    public Integer numOtros;  
    
    public Integer totalAlumnosConSeguimiento;
    public Integer alumnosTitulados;
    public Integer alumnosTrabajando;
    public Integer porCuentaAjena;
    public Integer caMismoSector; //cuenta ajena mismo sector
    public Integer camsMismaEmpresa; //cuenta ajena mismo sector misma empresa
    public Integer camsOtraEmpresa; //cuenta ajena mismo sector otra empresa
    public Integer caOtroSector; //cuenta ajena otro sector
    public Integer cuentaPropia;
    public Integer cpMismoSector; //cuenta propia mismo sector
    public Integer cpOtroSector; // cuenta propia otro sector
    public Integer demandandoEmpleo;
    public Integer estudiosOcupacionales;
    public Integer accionesBusquedaEmpleo;
    public Integer escuelaTaller;
    public Integer alumnosEstudiando;
    public Integer otroCicloFormativo;
    public Integer bachillerato;
    public Integer universidad;
    public Integer otrosEstudios;
    public Integer alumnosEnSituacionDesconocida;
	

    public ControlSeguimiento() {
    }


	public ControlSeguimiento(String codigoCentro, String nombreCentro, String codigoCiclo, Integer curso,
			String regimen, String unidad, Integer numMatriculas, Integer numTitulan, Integer numOtros) {
		super();
		this.codigoCentro = codigoCentro;
		this.nombreCentro = nombreCentro;
		this.codigoCiclo = codigoCiclo;
		this.curso = curso;
		this.regimen = regimen;
		this.unidad = unidad;
		this.numMatriculas = numMatriculas;
		this.numTitulan = numTitulan;
		this.numOtros = numOtros;
		this.ciclo = construirCiclo(codigoCiclo, curso, regimen, unidad);
	}
    
    
	public ControlSeguimiento(String codigoCentro, String nombreCentro, String codigoCiclo, Integer curso,
			String regimen, String unidad, Integer numMatriculas, Integer numTitulan, Integer numOtros,
			Integer totalAlumnosConSeguimiento, Integer alumnosTitulados, Integer alumnosTrabajando,
			Integer porCuentaAjena, Integer caMismoSector, Integer camsMismaEmpresa, Integer camsOtraEmpresa,
			Integer caOtroSector, Integer cuentaPropia, Integer cpMismoSector, Integer cpOtroSector,
			Integer demandandoEmpleo, Integer estudiosOcupacionales, Integer accionesBusquedaEmpleo,
			Integer escuelaTaller, Integer alumnosEstudiando, Integer otroCicloFormativo, Integer bachillerato,
			Integer universidad, Integer otrosEstudios, Integer alumnosEnSituacionDesconocida) {
		super();
		this.codigoCentro = codigoCentro;
		this.nombreCentro = nombreCentro;
		this.codigoCiclo = codigoCiclo;
		this.curso = curso;
		this.regimen = regimen;
		this.unidad = unidad;
		this.numMatriculas = numMatriculas;
		this.numTitulan = numTitulan;
		this.numOtros = numOtros;
		this.totalAlumnosConSeguimiento = totalAlumnosConSeguimiento;
		this.alumnosTitulados = alumnosTitulados;
		this.alumnosTrabajando = alumnosTrabajando;
		this.porCuentaAjena = porCuentaAjena;
		this.caMismoSector = caMismoSector;
		this.camsMismaEmpresa = camsMismaEmpresa;
		this.camsOtraEmpresa = camsOtraEmpresa;
		this.caOtroSector = caOtroSector;
		this.cuentaPropia = cuentaPropia;
		this.cpMismoSector = cpMismoSector;
		this.cpOtroSector = cpOtroSector;
		this.demandandoEmpleo = demandandoEmpleo;
		this.estudiosOcupacionales = estudiosOcupacionales;
		this.accionesBusquedaEmpleo = accionesBusquedaEmpleo;
		this.escuelaTaller = escuelaTaller;
		this.alumnosEstudiando = alumnosEstudiando;
		this.otroCicloFormativo = otroCicloFormativo;
		this.bachillerato = bachillerato;
		this.universidad = universidad;
		this.otrosEstudios = otrosEstudios;
		this.alumnosEnSituacionDesconocida = alumnosEnSituacionDesconocida;
		this.ciclo = construirCiclo(codigoCiclo, curso, regimen, unidad);
	}


	public ControlSeguimiento(String codigoCentro, String nombreCentro, String codigoCiclo, Integer curso,
			String regimen, String unidad, String ciclo, Integer numMatriculas, Integer numTitulan, Integer numOtros,
			Integer totalAlumnosConSeguimiento, Integer alumnosTitulados, Integer alumnosTrabajando,
			Integer porCuentaAjena, Integer caMismoSector, Integer camsMismaEmpresa, Integer camsOtraEmpresa,
			Integer caOtroSector, Integer cuentaPropia, Integer cpMismoSector, Integer cpOtroSector,
			Integer demandandoEmpleo, Integer estudiosOcupacionales, Integer accionesBusquedaEmpleo,
			Integer escuelaTaller, Integer alumnosEstudiando, Integer otroCicloFormativo, Integer bachillerato,
			Integer universidad, Integer otrosEstudios, Integer alumnosEnSituacionDesconocida) {
		super();
		this.codigoCentro = codigoCentro;
		this.nombreCentro = nombreCentro;
		this.codigoCiclo = codigoCiclo;
		this.curso = curso;
		this.regimen = regimen;
		this.unidad = unidad;
		this.ciclo = construirCiclo(codigoCentro, curso, regimen, unidad);
		this.numMatriculas = numMatriculas;
		this.numTitulan = numTitulan;
		this.numOtros = numOtros;
		this.totalAlumnosConSeguimiento = totalAlumnosConSeguimiento;
		this.alumnosTitulados = alumnosTitulados;
		this.alumnosTrabajando = alumnosTrabajando;
		this.porCuentaAjena = porCuentaAjena;
		this.caMismoSector = caMismoSector;
		this.camsMismaEmpresa = camsMismaEmpresa;
		this.camsOtraEmpresa = camsOtraEmpresa;
		this.caOtroSector = caOtroSector;
		this.cuentaPropia = cuentaPropia;
		this.cpMismoSector = cpMismoSector;
		this.cpOtroSector = cpOtroSector;
		this.demandandoEmpleo = demandandoEmpleo;
		this.estudiosOcupacionales = estudiosOcupacionales;
		this.accionesBusquedaEmpleo = accionesBusquedaEmpleo;
		this.escuelaTaller = escuelaTaller;
		this.alumnosEstudiando = alumnosEstudiando;
		this.otroCicloFormativo = otroCicloFormativo;
		this.bachillerato = bachillerato;
		this.universidad = universidad;
		this.otrosEstudios = otrosEstudios;
		this.alumnosEnSituacionDesconocida = alumnosEnSituacionDesconocida;
		this.ciclo = construirCiclo(codigoCiclo, curso, regimen, unidad);
	}


	private String construirCiclo(String codigoCiclo, Integer curso, String regimen, String unidad) {
		return new StringBuilder().append(codigoCiclo!=null?codigoCiclo:STRING_VACIO).append(SEPARADOR_CICLO).append(curso!=null?curso:STRING_VACIO).append(SEPARADOR_CICLO).append(regimen!=null?regimen.substring(0, 1):STRING_VACIO).append(SEPARADOR_CICLO).append(unidad!=null?unidad:STRING_VACIO).toString();
	}


   
}

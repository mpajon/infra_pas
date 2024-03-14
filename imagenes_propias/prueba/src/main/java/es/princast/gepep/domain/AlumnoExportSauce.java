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

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@ToString
@Entity
@NamedNativeQueries({
@NamedNativeQuery(name ="AlumnoExportSauce.getAlumnosExportSauce",
query =" select " + 
		" alumExpSauce.idAlumno,	" + 
		" alumExpSauce.codigoCentro,	" + 
		" alumExpSauce.nieAlumno,	" + 
		" alumExpSauce.cifEmpresa,			" + 
		" alumExpSauce.anio,		" + 
		" alumExpSauce.idDistribucion," + 
		" alumExpSauce.idUltimaDistribucion," + 
		" alumExpSauce.nombreAlumno,	" + 
		" alumExpSauce.nifAlumno,	" + 
		" alumExpSauce.apellidosAlumno,		" +  
		" alumExpSauce.nombreCiclo		" +  
		" from ( " + 
		"		select " + 
		"				cent.dc_codigo as codigoCentro, " +
		"				alu.dc_nie as nieAlumno, " + 
		"				emp.if_cif as cifEmpresa, " + 
		"				mat.nu_anio as anio, " + 
		"				alu.ca_alumno as idAlumno, " + 
		"				dist.cn_distribucion as idDistribucion, " + 
		"				(select dist_mat.cn_distribucion " + 
		"					from (" + 
		"						select row_number() OVER (ORDER BY d.nu_anio_ini desc, d.nu_mes_ini desc, d.nu_dia_ini desc) as fila, D.cn_distribucion, d.nu_anio_ini, d.nu_mes_ini, d.nu_dia_ini " + 
		"						from distribucion d " + 
		"				   		where d.ca_matricula = mat.ca_matricula " + 
		"				) dist_mat where dist_mat.fila = 1) idUltimaDistribucion, " + 
		"				alu.dc_nombre as nombreAlumno, " + 
		"				(alu.dl_apellido1 || ' ' || alu.dl_apellido2) as apellidosAlumno, " + 
		"				alu.if_nif as nifAlumno, " + 
		"				ci.dc_nombre as nombreCiclo " + 
		"		from  " + 
		"		        matricula mat " + 
		"		        inner join alumno alu on mat.ca_alumno = alu.ca_alumno " + 
		"		        inner join ofertas_centro oc on oc.ca_ofercen = mat.ca_ofercen " + 
		"		        left join oferta_formativa off on oc.ca_oferta_formativa = off.ca_oferta_formativa " + 
		"		        left join ciclo ci on off.ca_ciclo = ci.ca_ciclo " + 
		"		        inner join distribucion dist on dist.ca_matricula = mat.ca_matricula " + 
		"		        left join anexo_contrato anx on anx.cn_anexo = dist.cn_anexo " + 
		"		        left join convenio conv on conv.cn_convenio = anx.cn_convenio " + 
		"		        left join area area on area.cn_area = conv.cn_area " + 
		"		        left join centro cent on cent.ca_centro = conv.ca_centro " + 
		"		        left join empresa emp on area.cn_empresa = emp.cn_empresa " + 
		"		where " + 
		"		      	dist.fe_fbaja is null " + 
		"		      	and mat.nu_anio = :anio " + 
		"		order by mat.nu_anio, alu.ca_alumno, cn_distribucion" + 
		") alumExpSauce"
,resultSetMapping = "AlumnoExportSauceMapping")

})


@SqlResultSetMappings({

	@SqlResultSetMapping(name="AlumnoExportSauceMapping",
	        classes = {
	                @ConstructorResult(targetClass = AlumnoExportSauce.class,
	                        columns = {@ColumnResult(name="idAlumno", type=String.class), 
	                        		@ColumnResult(name="codigoCentro", type=String.class),
	                        		@ColumnResult(name="nieAlumno", type=String.class),
	                        		@ColumnResult(name="cifEmpresa", type=String.class),
	                        		@ColumnResult(name="anio", type=Integer.class),
	                        		@ColumnResult(name="idDistribucion", type=Integer.class),
	                        		@ColumnResult(name="idUltimaDistribucion", type=Integer.class),
	                        		@ColumnResult(name="nombreAlumno", type=String.class),                        		
	                        		@ColumnResult(name="nifAlumno", type=String.class),
	                        		@ColumnResult(name="apellidosAlumno", type=String.class),
	                        		@ColumnResult(name="nombreCiclo", type=String.class)
	                        		}
	                )})
	
})

public class AlumnoExportSauce implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7385862670636625733L;

	@Id
	public String idAlumno; 
	public String codigoCentro;
	public String nieAlumno;
	public String cifEmpresa;
	public Integer anio; 
	public Integer idDistribucion; 
	public Integer idUltimaDistribucion; 
	public String nombreAlumno;
	public String nifAlumno;
	public String apellidosAlumno;
	public String nombreCiclo;
    
    
	

    public AlumnoExportSauce() {
    }




	public AlumnoExportSauce(String idAlumno, String codigoCentro, String nieAlumno, String cifEmpresa, Integer anio,
			Integer idDistribucion, Integer idUltimaDistribucion, String nombreAlumno, String nifAlumno,
			String apellidosAlumno, String nombreCiclo) {
		super();
		this.idAlumno = idAlumno;
		this.codigoCentro = codigoCentro;
		this.nieAlumno = nieAlumno;
		this.cifEmpresa = cifEmpresa;
		this.anio = anio;
		this.idDistribucion = idDistribucion;
		this.idUltimaDistribucion = idUltimaDistribucion;
		this.nombreAlumno = nombreAlumno;
		this.nifAlumno = nifAlumno;
		this.apellidosAlumno = apellidosAlumno;
		this.nombreCiclo = nombreCiclo;
	}




	

   


   
}

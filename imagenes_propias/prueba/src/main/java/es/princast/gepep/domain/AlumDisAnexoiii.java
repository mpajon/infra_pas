package es.princast.gepep.domain;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

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
 
//CRQ151434
@NamedNativeQuery(name ="AlumDisAnexoiii.findAlumDisByAnexo",
query =" select distinct distr.cn_anexo as idAnexo, distr.cn_distribucion as idDistribucion,sum(case when distper.cn_horas is null then 0 else distper.cn_horas end )as horas ,  " +
	    "distr.nu_dia_ini ||'/'|| distr.nu_mes_ini ||'/' ||distr.nu_anio_ini ||' - '|| distr.nu_dia_fin ||'/'|| distr.nu_mes_fin ||'/' ||distr.nu_anio_fin as periodo,  " +
	    "al.ca_alumno as idAlumno , al.dc_nombre as nombre , concat(dl_apellido1 ,' ', dl_apellido2) as apellidos , al.dc_telefono as movil, concat (al.dl_calle,' ',al.dc_numero,' ',al.dc_piso, al.dc_letra) as direccion, " +
	    "al.ca_cp as cp, al.dl_localidad as localidad, al.dl_municipio as municipio, al.if_nif as nif, " +
	    "distr.dc_tutor_empresa as tutorEmpresa " +
		"from distribucion distr " + 
		"inner join matricula mat on distr.ca_matricula = mat.ca_matricula " + 
		"inner join alumno al on mat.ca_alumno = al.ca_alumno " + 
		"left join distribucion_periodo distper on distr.cn_distribucion = distper.cn_distribucion " + 
		"where distr.cn_anexo = :idAnexo group by distr.cn_distribucion,al.ca_alumno ",		
		  resultSetMapping = "AlumDisAnexoiiiMapping")

@SqlResultSetMapping(name="AlumDisAnexoiiiMapping",
        classes = {
                @ConstructorResult(targetClass = AlumDisAnexoiii.class,
                        columns = {@ColumnResult(name="idAnexo", type=Integer.class), 
                        		@ColumnResult(name="idDistribucion", type=Integer.class),
                        		@ColumnResult(name="horas", type=Integer.class),
                        		@ColumnResult(name="periodo", type=String.class),
                        		@ColumnResult(name="idAlumno", type=String.class),                        		
                        		@ColumnResult(name="nombre", type=String.class),
                        		@ColumnResult(name="apellidos", type=String.class),
                        		@ColumnResult(name="movil", type=String.class),
                        		@ColumnResult(name="direccion", type=String.class),
                        		@ColumnResult(name="cp", type=String.class),
                        		@ColumnResult(name="localidad", type=String.class),
                        		@ColumnResult(name="municipio", type=String.class),
                        		@ColumnResult(name="nif", type=String.class),
                        		@ColumnResult(name="tutorEmpresa", type=String.class)}
                )}
)



public class AlumDisAnexoiii implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    public int idAnexo;
    public int idDistribucion;
    @Builder.Default
    public Integer horas = 0;
    public String periodo;
    public String idAlumno;
    @Builder.Default
    public String nombre="";
    @Builder.Default
    public String apellidos ="";
    @Builder.Default
    public String movil = "";
    @Builder.Default
    public String direccion = "";
    @Builder.Default
    public String cp = "";
    @Builder.Default
    public String localidad = "";
    @Builder.Default
    public String municipio = "";
    public String nif;
    public String tutorEmpresa;

    public AlumDisAnexoiii() {
    }

    public AlumDisAnexoiii(int idAnexo, int idDistribucion, Integer horas, String periodo,  String idAlumno, String nombre, String apellidos, String movil,String direccion,String cp, String localidad, String municipio,String nif,String tutorEmpresa) {
        
    	this.idAnexo = idAnexo;
        this.idDistribucion = idDistribucion;
        if (horas!=null) {
        	this.horas = horas;
        }
        this.periodo = periodo;
        this.idAlumno = idAlumno;      
        this.nombre = nombre;
        this.apellidos = apellidos;
        if(movil!=null) {
        	this.movil = movil;
        }
        if(direccion!=null) {
        	this.direccion = direccion;
        }
        if (cp!=null) {
        	this.cp=cp;
        }
        if (localidad!=null) {
        	this.localidad=localidad;
        }
        if (municipio!=null) {
        	this.municipio=municipio;
        }
        this.nif = nif;
        
        this.tutorEmpresa=tutorEmpresa;
        
    }
}

package es.princast.gepep.domain;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
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

@NamedNativeQueries({
@NamedNativeQuery(name ="AlumAnexov.findAlumByAnexo",
query ="SELECT distinct AL.IF_NIF as nif, AL.DC_NOMBRE as nombre, AL.DL_APELLIDO1 as apellido1, AL.DL_APELLIDO2 as apellido2, " +
		"conv.cn_convenio AS idConvenio, conv.ca_centro as codCentro, conv.dc_codigo as codConvenio, ANX.CA_CODIGO AS idAnexo,  ci.nu_hpractica as horas, DIS.CN_DISTRIBUCION as idDistribucion " + 
		"FROM MATRICULA MAT " + 
		"INNER JOIN ALUMNO AL ON MAT.CA_ALUMNO = AL.CA_ALUMNO " + 
		"INNER JOIN OFERTAS_CENTRO OFC ON OFC.CA_OFERCEN = MAT.CA_OFERCEN " + 
		"INNER JOIN DISTRIBUCION DIS ON MAT.CA_MATRICULA = DIS.CA_MATRICULA " +
		"inner join distribucion_periodo DISP on DIS.cn_distribucion = DISP.cn_distribucion " + 
		"inner join periodo_practica PER on PER.cn_periodo = DISP.cn_periodo  " + 
		"INNER JOIN OFERTA_FORMATIVA OFF ON OFC.CA_OFERTA_FORMATIVA = OFF.CA_OFERTA_FORMATIVA " +
		"INNER JOIN ANEXO_CONTRATO ANX ON DIS.cn_anexo = ANX.cn_anexo " +
		"inner join convenio conv on conv.cn_convenio = anx.cn_convenio " + 
		"inner join ciclo ci on ci.ca_ciclo = off.ca_ciclo " + 
		"WHERE OFC.CA_CENTRO = :idCentro " + 
		" AND MAT.NU_ANIO = :curso " + 
		" AND MAT.CA_OFERCEN = :idOferta " +
		" and PER.cn_periodo = :idPeriodoPractica ",
		 resultSetMapping = "AlumAnexovMapping"),

@NamedNativeQuery(name ="AlumAnexov.findAlumByAnexoByTutor",
query ="SELECT distinct AL.IF_NIF as nif, AL.DC_NOMBRE as nombre, AL.DL_APELLIDO1 as apellido1, AL.DL_APELLIDO2 as apellido2, " +
		"conv.cn_convenio AS idConvenio, conv.ca_centro as codCentro, conv.dc_codigo as codConvenio, ANX.CA_CODIGO AS idAnexo, ci.nu_hpractica as horas, DIS.CN_DISTRIBUCION as idDistribucion " + 
		"FROM MATRICULA MAT " + 
		"INNER JOIN ALUMNO AL ON MAT.CA_ALUMNO = AL.CA_ALUMNO " + 
		"INNER JOIN OFERTAS_CENTRO OFC ON OFC.CA_OFERCEN = MAT.CA_OFERCEN " + 
		"INNER JOIN DISTRIBUCION DIS ON MAT.CA_MATRICULA = DIS.CA_MATRICULA " +
		"inner join distribucion_periodo DISP on DIS.cn_distribucion = DISP.cn_distribucion " + 
		"inner join periodo_practica PER on PER.cn_periodo = DISP.cn_periodo  " + 
		"INNER JOIN OFERTA_FORMATIVA OFF ON OFC.CA_OFERTA_FORMATIVA = OFF.CA_OFERTA_FORMATIVA " + 
		"INNER JOIN ANEXO_CONTRATO ANX ON DIS.cn_anexo = ANX.cn_anexo " +
		"inner join convenio conv on conv.cn_convenio = anx.cn_convenio " + 
		"inner join ciclo ci on ci.ca_ciclo = off.ca_ciclo " +
		"inner join Centro centro on oc.ca_centro = centro.ca_centro " +
		"inner join unidad uni on uni.ca_ofercen = ofc.ca_ofercen and mat.ca_unidad = uni.ca_unidad " +		
		" WHERE "+ 
		" mat.nu_anio = :curso "+ 
		" and uni.nu_anio= :curso "+ 
		" and centro.ca_centro = :idCentro "+ 
		" and uni.ca_tutor= :tutor  " +
		" AND MAT.CA_OFERCEN = :idOferta " +
		" and PER.cn_periodo = :idPeriodoPractica " ,
		 resultSetMapping = "AlumAnexovMapping"),

@NamedNativeQuery(name ="AlumAnexov.findAlumByUnidadAnexo",
query =" select "+
		" 	distinct al.if_nif as nif, "+
		" 	al.dc_nombre as nombre, "+
		" 	al.dl_apellido1 as apellido1, "+
		" 	al.dl_apellido2 as apellido2, "+
		" 	conv.cn_convenio as idConvenio, "+
		" 	conv.ca_centro as codCentro, "+
		" 	ce.dc_codigo || conv.dc_codigo as codConvenio, "+
		" 	anx.ca_codigo as idAnexo, "+
		" 	ci.nu_hpractica as horas, "+
		" 	dis.cn_distribucion as idDistribucion "+
		" from "+
		" 	matricula mat  "+
		" 	inner join unidad uni on mat.ca_unidad = uni.ca_unidad  "+
		" 	inner join alumno al on	mat.ca_alumno = al.ca_alumno  "+
		" 	inner join ofertas_centro ofc on ofc.ca_ofercen = mat.ca_ofercen  "+
		" 	inner join distribucion dis on	mat.ca_matricula = dis.ca_matricula  "+
		" 	inner join distribucion_periodo disp on	dis.cn_distribucion = disp.cn_distribucion  "+
		" 	inner join periodo_practica per on	per.cn_periodo = disp.cn_periodo  "+
		" 	inner join oferta_formativa off on	ofc.ca_oferta_formativa = off.ca_oferta_formativa  "+
		" 	inner join anexo_contrato anx on	dis.cn_anexo = anx.cn_anexo  "+
		" 	inner join convenio conv on	conv.cn_convenio = anx.cn_convenio  "+
		" 	inner join ciclo ci on	ci.ca_ciclo = off.ca_ciclo  "+
		" 	inner join centro ce on	ce.ca_centro = ofc.ca_centro "+
		" where "+
		" 	ofc.ca_centro =:idCentro "+
		" 	and mat.nu_anio =:curso "+
		" 	and mat.ca_ofercen =:idOferta "+
		" 	and per.cn_periodo =:idPeriodoPractica "+
		" 	and uni.ca_nombre =:idUnidad ",
		 resultSetMapping = "AlumAnexovMapping"),

@NamedNativeQuery(name ="AlumAnexov.findAlumByUnidadAnexoByTutor",
query =" select "+
		" 	distinct al.if_nif as nif, "+
		" 	al.dc_nombre as nombre, "+
		" 	al.dl_apellido1 as apellido1, "+
		" 	al.dl_apellido2 as apellido2, "+
		" 	conv.cn_convenio as idConvenio, "+
		" 	conv.ca_centro as codCentro, "+
		" 	ce.dc_codigo || conv.dc_codigo as codConvenio, "+
		" 	anx.ca_codigo as idAnexo, "+
		" 	ci.nu_hpractica as horas, "+
		" 	dis.cn_distribucion as idDistribucion "+
		" from "+
		" 	matricula mat  "+
		" 	inner join unidad uni on mat.ca_unidad = uni.ca_unidad  "+
		" 	inner join alumno al on	mat.ca_alumno = al.ca_alumno  "+
		" 	inner join ofertas_centro ofc on ofc.ca_ofercen = mat.ca_ofercen  "+
		" 	inner join distribucion dis on	mat.ca_matricula = dis.ca_matricula  "+
		" 	inner join distribucion_periodo disp on	dis.cn_distribucion = disp.cn_distribucion  "+
		" 	inner join periodo_practica per on	per.cn_periodo = disp.cn_periodo  "+
		" 	inner join oferta_formativa off on	ofc.ca_oferta_formativa = off.ca_oferta_formativa  "+
		" 	inner join anexo_contrato anx on	dis.cn_anexo = anx.cn_anexo  "+
		" 	inner join convenio conv on	conv.cn_convenio = anx.cn_convenio  "+
		" 	inner join ciclo ci on	ci.ca_ciclo = off.ca_ciclo  "+
		" 	inner join centro ce on	ce.ca_centro = ofc.ca_centro "+
		" where "+
		" 	ofc.ca_centro =:idCentro "+
		" 	and mat.nu_anio =:curso "+
		" 	and mat.ca_ofercen =:idOferta "+
		" 	and per.cn_periodo =:idPeriodoPractica "+
		" 	and uni.ca_nombre =:idUnidad "+
		"	and (uni.ca_tutor =:tutor or uni.ca_tutor_adicional =:tutor) ",
		 resultSetMapping = "AlumAnexovMapping"),
})

@SqlResultSetMapping(name="AlumAnexovMapping",
        classes = {
                @ConstructorResult(targetClass = AlumAnexov.class,
                        columns = {@ColumnResult(name="nif", type=String.class), 
                        		@ColumnResult(name="nombre", type=String.class),
                        		@ColumnResult(name="apellido1", type=String.class),
                        		@ColumnResult(name="apellido2", type=String.class),
                        		@ColumnResult(name="idConvenio", type=Integer.class),
                        		@ColumnResult(name="codCentro", type=String.class),
                        		@ColumnResult(name="codConvenio", type=String.class),
                        		@ColumnResult(name="idAnexo", type=Integer.class),
                        		@ColumnResult(name="horas", type=Integer.class),
                        		@ColumnResult(name="idDistribucion", type=Integer.class)}
                )}
)



public class AlumAnexov implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	public String nif;
	public String nombre;    
    public String apellido1;
    public String apellido2;
    public int idConvenio;
    public String codCentro;
    public String codConvenio;
    public int idAnexo;
	@Builder.Default
    public Integer horas = 0;
    public int idDistribucion;

    

    public AlumAnexov() {
    }

    public AlumAnexov(String nif, String nombre, String apellido1, String apellido2, int idConvenio, String codCentro, String codConvenio,  int idAnexo, Integer horas , int idDistribucion) {
    	this.nif = nif;
    	this.nombre = nombre;
    	this.apellido1 = apellido1;
    	this.apellido2 = apellido2;
    	this.idConvenio = idConvenio;
    	this.codCentro = codCentro;
    	this.codConvenio = codConvenio;
    	this.idAnexo = idAnexo;
    	if (horas!=null) {
        	this.horas = horas;
        }
        this.idDistribucion = idDistribucion;
                
    }
}

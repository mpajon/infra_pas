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
@NamedNativeQuery(name ="VisitaAgrupada.findListadoByAnioAndMes",
query ="select idProfesor, nombre, apellido1, apellido2, nif, count (idVisita) as viajes,  sum(euros) as euros, " + 
 		"anio, mes, '' as idCentro, '' as nombreCentro, '' as codigoCentro, " + 
 		"count( nullif( autorizada = false, true )) as autorizadas, " + 
 		"count( nullif( bloqueada = false, true )) as bloqueadas, " + 
 		"count( nullif( realizada = false, true )) as realizadas " +
 		"from ( "+
		" select distinct   vt.cn_visita_tutor as idVisita,vt.ca_profesor as idProfesor, pf.dc_nombre as nombre ,pf.dl_apellido1 as apellido1 ,pf.dl_apellido2 as apellido2, pf.if_nif as nif,  vt.im_imp_total as euros, vt.nu_anio as anio, vt.nu_mes as mes, " +
		" fl_autorizada as autorizada, fl_bloqueada as bloqueada,fl_realizada as realizada  "+		
		" from visita_tutor vt" + 
		" inner join profesor pf on vt.ca_profesor=pf.ca_profesor " + 
		" inner join unidad uni on pf.ca_profesor = uni.ca_tutor "+ 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen "+
		" where vt.nu_anio = :anio and vt.nu_mes= :mes " + 
		" group by idVisita, idProfesor,nombre,apellido1,apellido2, nif, anio, mes" +
		" ) as visitas " + 
		" group by idProfesor,nombre,	apellido1,	apellido2, nif, anio, mes, idCentro , nombreCentro, codigoCentro" +
		" order by  nombreCentro, nif ",
		resultSetMapping = "VisitaAgrupadaMapping"),
 

@NamedNativeQuery(name ="VisitaAgrupada.findListadoByAnioAndMesAndTutor",
query =	"select idProfesor, nombre, apellido1, apellido2, nif, count (idVisita) as viajes,  sum(euros) as euros, " + 
 		"anio, mes, '' as idCentro, '' as nombreCentro, '' as codigoCentro, " + 
 		"count( nullif( autorizada = false, true )) as autorizadas, " + 
 		"count( nullif( bloqueada = false, true )) as bloqueadas, " + 
 		"count( nullif( realizada = false, true )) as realizadas " +
 		"from ( "+
		" select distinct   vt.cn_visita_tutor as idVisita, vt.ca_profesor as idProfesor, pf.dc_nombre as nombre ,pf.dl_apellido1 as apellido1 ,pf.dl_apellido2 as apellido2, pf.if_nif as nif, vt.im_imp_total as euros, vt.nu_anio as anio, vt.nu_mes as mes, " +
		" fl_autorizada as autorizada, fl_bloqueada as bloqueada,fl_realizada as realizada  "+
		" from visita_tutor vt" + 
		" inner join profesor pf on vt.ca_profesor=pf.ca_profesor " + 
		" inner join unidad uni on pf.ca_profesor = uni.ca_tutor "+ 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen "+
		" where vt.nu_anio = :anio and vt.nu_mes= :mes and vt.ca_profesor= :tutor" + 
		" group by  idVisita,idProfesor,nombre,apellido1,apellido2, nif, anio, mes," +
		" ) as visitas " + 
		" group by idProfesor,nombre,	apellido1,	apellido2, nif,	anio, mes , idCentro , nombreCentro, codigoCentro" +
		" order by  nombreCentro, nif ",
		  resultSetMapping = "VisitaAgrupadaMapping"),
 

@NamedNativeQuery(name ="VisitaAgrupada.findListadoByCentroAndAnioAndMesAndTutor",
query =	" select idprofesor,nombre,apellido1,apellido2, nif, count(numvisita) as viajes,sum(euros) as euros,anio,mes, idCentro, nombreCentro, codigoCentro, count (nullif (autorizada = false, true)) as autorizadas , count(nullif (bloqueada = false, true)) as bloqueadas, count(nullif (realizada= false, true)) as realizadas "+
		" from ( " +
	 	" select  distinct vt.ca_profesor as idProfesor, pf.dc_nombre as nombre ,pf.dl_apellido1 as apellido1 ,pf.dl_apellido2 as apellido2, pf.if_nif as nif, vt.cn_visita_tutor as numVisita, vt.im_imp_total as euros,vt.nu_anio as anio, vt.nu_mes as mes, oc.ca_centro as idCentro,  cen.dc_nombre as nombreCentro, cen.dc_codigo as codigoCentro,"+
		" fl_autorizada as autorizada, fl_bloqueada as bloqueada, fl_realizada as realizada " +
	 	" from visita_tutor vt " +
	 	" inner join profesor pf on vt.ca_profesor=pf.ca_profesor " +
	 	" inner join unidad uni on pf.ca_profesor = uni.ca_tutor "+ 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " +
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +
	 	" where vt.nu_anio = :anio and vt.nu_mes= :mes and oc.ca_centro = :centro and vt.ca_profesor= :tutor " + 
		" ) as foo " +
		" group by  idprofesor,nombre,apellido1,apellido2,nif, anio,mes, idCentro , nombreCentro, codigoCentro" +
		" order by  nombreCentro, nif ",
		  resultSetMapping = "VisitaAgrupadaMapping"),

@NamedNativeQuery(name ="VisitaAgrupada.findListadoByCentroAndAnioAndMes",
query =	" select idprofesor,nombre,apellido1,apellido2, nif, count(numvisita) as viajes,sum(euros) as euros,anio,mes, idCentro, nombreCentro, codigoCentro, count (nullif (autorizada = false, true)) as autorizadas , count(nullif (bloqueada = false, true)) as bloqueadas, count(nullif (realizada= false, true)) as realizadas "+
		" from ( " +
	 	" select  distinct vt.ca_profesor as idProfesor, pf.dc_nombre as nombre ,pf.dl_apellido1 as apellido1 ,pf.dl_apellido2 as apellido2, pf.if_nif as nif, vt.cn_visita_tutor as numVisita, vt.im_imp_total as euros,vt.nu_anio as anio, vt.nu_mes as mes, oc.ca_centro as idCentro, cen.dc_nombre as nombreCentro, cen.dc_codigo as codigoCentro," + 
	 	"  fl_autorizada as autorizada, fl_bloqueada as bloqueada, fl_realizada as realizada " +
	 	" from visita_tutor vt " +
	 	" inner join profesor pf on vt.ca_profesor=pf.ca_profesor " +
	 	" inner join unidad uni on pf.ca_profesor = uni.ca_tutor "+ 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " +
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +
	 	" where vt.nu_anio = :anio and vt.nu_mes= :mes and oc.ca_centro = :centro " + 
		" ) as foo " +
		" group by  idprofesor,nombre,apellido1,apellido2,nif, anio,mes, idCentro , nombreCentro, codigoCentro" +
		" order by  nombreCentro, nif ",
		  resultSetMapping = "VisitaAgrupadaMapping"),

@NamedNativeQuery(name ="VisitaAgrupada.findListadoByCentroAndAnioAndMesAndTipoPracticaAndTutor",
query =	" select idprofesor,nombre,apellido1,apellido2, nif, count(numvisita) as viajes,sum(euros) as euros,anio,mes, idCentro, nombreCentro, codigoCentro, count (nullif (autorizada = false, true)) as autorizadas , count(nullif (bloqueada = false, true)) as bloqueadas, count(nullif (realizada= false, true)) as realizadas "+
		" from ( " +
	 	" select  distinct vt.ca_profesor as idProfesor, pf.dc_nombre as nombre ,pf.dl_apellido1 as apellido1 ,pf.dl_apellido2 as apellido2, pf.if_nif as nif, vt.cn_visita_tutor as numVisita, vt.im_imp_total as euros,vt.nu_anio as anio, vt.nu_mes as mes, oc.ca_centro as idCentro, cen.dc_nombre as nombreCentro, cen.dc_codigo as codigoCentro,"+
		" fl_autorizada as autorizada, fl_bloqueada as bloqueada, fl_realizada as realizada " +
	 	" from visita_tutor vt " +
	 	" inner join profesor pf on vt.ca_profesor=pf.ca_profesor " +
	 	" inner join unidad uni on pf.ca_profesor = uni.ca_tutor "+ 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " +
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +
	 	" where vt.nu_anio = :anio and vt.nu_mes= :mes and oc.ca_centro = :centro and vt.ca_profesor= :tutor " + 
	 	" and vt.cn_tipo_practica = :idTipoPractica " + 
		" ) as foo " +
		" group by  idprofesor,nombre,apellido1,apellido2,nif, anio,mes, idCentro , nombreCentro, codigoCentro" +
		" order by  nombreCentro, nif ",
		  resultSetMapping = "VisitaAgrupadaMapping"),


@NamedNativeQuery(name ="VisitaAgrupada.findListadoByCentroAndAnioAndMesAndTipoPractica",
query =	" select idprofesor,nombre,apellido1,apellido2, nif, count(numvisita) as viajes,sum(euros) as euros,anio,mes, idCentro, nombreCentro, codigoCentro, count (nullif (autorizada = false, true)) as autorizadas , count(nullif (bloqueada = false, true)) as bloqueadas, count(nullif (realizada= false, true)) as realizadas "+
		" from ( " +
	 	" select  distinct vt.ca_profesor as idProfesor, pf.dc_nombre as nombre ,pf.dl_apellido1 as apellido1 ,pf.dl_apellido2 as apellido2, pf.if_nif as nif, vt.cn_visita_tutor as numVisita, vt.im_imp_total as euros,vt.nu_anio as anio, vt.nu_mes as mes, oc.ca_centro as idCentro, cen.dc_nombre as nombreCentro, cen.dc_codigo as codigoCentro, " + 
	 	"  fl_autorizada as autorizada, fl_bloqueada as bloqueada, fl_realizada as realizada " +
	 	" from visita_tutor vt " +
	 	" inner join profesor pf on vt.ca_profesor=pf.ca_profesor " +
	 	" inner join unidad uni on pf.ca_profesor = uni.ca_tutor and uni.nu_anio = :cursoAca"+ 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " +
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo and ci.ca_ciclo = vt.ca_ciclo " +
	 	" inner join centro cen on oc.ca_centro = cen.ca_centro " +
	 	" where vt.nu_anio = :anio and vt.nu_mes= :mes and (oc.ca_centro = :centro or :centro = 'undefined') " + 
		" and vt.cn_tipo_practica = :idTipoPractica " + 
		" ) as foo " +
		" group by  idprofesor,nombre,apellido1,apellido2,nif, anio,mes, idCentro , nombreCentro, codigoCentro" + 
		" order by  nombreCentro, nif ",
		  resultSetMapping = "VisitaAgrupadaMapping")

})

@SqlResultSetMappings({
@SqlResultSetMapping(name="VisitaAgrupadaMapping",
        classes = {
                @ConstructorResult(targetClass = VisitaAgrupada.class,
                        columns = {@ColumnResult(name="idProfesor", type=String.class), 
                        		@ColumnResult(name="nombre", type=String.class),
                        		@ColumnResult(name="apellido1", type=String.class),
                        		@ColumnResult(name="apellido2", type=String.class),
                        		@ColumnResult(name="nif", type=String.class),
                        		@ColumnResult(name="viajes", type=Integer.class),
                        		@ColumnResult(name="euros", type=Float.class),                        	 
                        		@ColumnResult(name="anio", type=Integer.class),
                        		@ColumnResult(name="mes", type=Integer.class),                        		
                        		@ColumnResult(name="idCentro", type=String.class),
                        		@ColumnResult(name="nombreCentro", type=String.class),
                        		@ColumnResult(name="codigoCentro", type=String.class),
                        		@ColumnResult(name="autorizadas", type=Integer.class),
                        		@ColumnResult(name="bloqueadas", type=Integer.class),
                        		@ColumnResult(name="realizadas", type=Integer.class),
                        		}
                )})

})


public class VisitaAgrupada implements Serializable {

    @Id
    public String idProfesor;
    public String nombre;
    public String apellido1;
    public String apellido2;
    public String nif;
    public int viajes;
    public float euros;
    public int anio;
    public int mes;    
    public String idCentro;
    public String nombreCentro;
    public String codigoCentro;
    public int autorizadas;
    public int bloqueadas;
    public int realizadas;
    

    public VisitaAgrupada() {
    }

    public VisitaAgrupada(String idProfesor, String nombre, String apellido1, String nif, String apellido2, int viajes, float euros, int anio, int mes, String idCentro, String nombreCentro, String codigoCentro , int autorizadas, int bloqueadas, int realizadas) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nif = nif;
        this.viajes = viajes;
        this.euros = euros;
        this.anio = anio;
        this.mes = mes;        
        this.idCentro = idCentro;
        this.nombreCentro = nombreCentro;
        this.codigoCentro = codigoCentro;
        this.autorizadas = autorizadas;
        this.bloqueadas = bloqueadas;
        this.realizadas = realizadas;
        	
        
          
    }
}

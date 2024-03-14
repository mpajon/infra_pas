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
@NamedNativeQuery(name ="TutorCentro.findListadoTutoresCentroAnio",
query = " select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, " +
		" cen.ca_centro as centro, ci.ca_ensenanza as idEnsenanza,0 as diaBloqueo,ci.ca_ciclo as idCiclo,  ci.dc_codigo as codCiclo, " +
		" ci.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo," +
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad " +
		" from profesor prof " +
		" inner join unidad uni on uni.ca_tutor = prof.ca_profesor or uni.ca_tutor_adicional = prof.ca_profesor" +
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join ensenanza ense on	ci.ca_ensenanza = ense.ca_ensenanza " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +	
		" where uni.nu_anio = :anio and cen.ca_centro = :centro " + 
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) " + 
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping"),

@NamedNativeQuery(name ="TutorCentro.findListadoTutoresCentroAnioAndTipoPractica",
query = " select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, " +
		" cen.ca_centro as centro, ci.ca_ensenanza as idEnsenanza, tp.nu_bloqueo as diaBloqueo, ci.ca_ciclo as idCiclo,  ci.dc_codigo as codCiclo, " +
		" ci.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo," +
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad " +
		" from profesor prof " +
		" inner join unidad uni on uni.ca_tutor = prof.ca_profesor or uni.ca_tutor_adicional = prof.ca_profesor" +
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join ensenanza ense on	ci.ca_ensenanza = ense.ca_ensenanza " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +	
		" inner join tipopractica_ensenanzas te on te.ca_ensenanza = ci.ca_ensenanza " +
		" inner join tipo_practica tp on tp.cn_tipo_practica = te.cn_tipo_practica " +
		" where uni.nu_anio = :anio and cen.ca_centro = :centro " + 
		" and te.cn_tipo_practica =  :idTipoPractica " + 
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) " + 
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping"),


@NamedNativeQuery(name ="TutorCentro.findListadoTutoresCentroAnioAndTipoPracticaDeportiva",
query = " select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, " +
		" cen.ca_centro as centro, ci.ca_ensenanza as idEnsenanza, tp.nu_bloqueo as diaBloqueo, ci.ca_ciclo as idCiclo,  of.dc_codigo as codCiclo, " +
		" of.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo," +
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad " +
		" from profesor prof " +
		" inner join unidad uni on uni.ca_tutor = prof.ca_profesor or uni.ca_tutor_adicional = prof.ca_profesor" +
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join ensenanza ense on	ci.ca_ensenanza = ense.ca_ensenanza " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +	
		" inner join tipopractica_ensenanzas te on te.ca_ensenanza = ci.ca_ensenanza " +
		" inner join tipo_practica tp on tp.cn_tipo_practica = te.cn_tipo_practica " +
		" where uni.nu_anio = :anio and cen.ca_centro = :centro " + 
		" and te.cn_tipo_practica =  :idTipoPractica " + 
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) " + 
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping"),

@NamedNativeQuery(name ="TutorCentro.findListadoTutoresCentroAnioLite",
query =	" select distinct tutor.idProfesor as idProfesor,tutor.nombre,tutor.apellido1,tutor.apellido2,tutor.dni,tutor.deSauce from (" +
		" select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, prof.if_nif as dni, prof.fl_de_sauce as deSauce " +
		" from profesor prof " + 
		" left outer join unidad on prof.ca_profesor = unidad.ca_tutor or unidad.ca_tutor_adicional = prof.ca_profesor" +
		" left outer join ofertas_centro oc on unidad.ca_ofercen = oc.ca_ofercen " + 
		" left outer join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" left outer join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" left outer join centro cen on oc.ca_centro = cen.ca_centro " +		 
		" where " +
		" unidad.nu_anio = :anio and cen.ca_centro = :centro " +
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" +
		" UNION " +
		" select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, prof.if_nif as dni, prof.fl_de_sauce as deSauce " +
		" from profesor prof " +
		" left outer join unidad on prof.ca_profesor = unidad.ca_tutor  or unidad.ca_tutor_adicional = prof.ca_profesor" +
		" left outer join ofertas_centro oc on unidad.ca_ofercen = oc.ca_ofercen " +
		" left outer join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " +
		" left outer join ciclo ci on of.ca_ciclo = ci.ca_ciclo " +
		" left outer join centro cen on oc.ca_centro = cen.ca_centro " +
		" where fl_de_sauce is false) tutor " +
		" order by tutor.apellido1,tutor.apellido2, tutor.nombre asc",
		  resultSetMapping = "TutorMatricLiteMapping"),

@NamedNativeQuery(name ="TutorCentro.findListadoTutoresCentroAnioOfertaCentro",
query = " select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2," +
		" cen.ca_centro as centro,ci.ca_ensenanza as idEnsenanza,0 as diaBloqueo, ci.ca_ciclo as idCiclo,  ci.dc_codigo as codCiclo," +
		" ci.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo," +		
		" unidad.cn_curso as curso, unidad.ca_turno as turno, unidad.ca_nombre as unidad " +
		" from profesor prof " + 
		" inner join unidad on prof.ca_profesor = unidad.ca_tutor or unidad.ca_tutor_adicional = prof.ca_profesor " +
		" inner join ofertas_centro oc on unidad.ca_ofercen = oc.ca_ofercen " + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +		 
		" where unidad.nu_anio = :anio and cen.ca_centro = :centro and oc.ca_ofercen= :ofercen" +
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping"),

@NamedNativeQuery(name ="TutorCentro.findTutorByUsuarioAndAnio",
query = " select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, "+
		" cen.ca_centro as centro,ci.ca_ensenanza as idEnsenanza,0 as diaBloqueo,ci.ca_ciclo as idCiclo,  ci.dc_codigo as codCiclo, "+
		" ci.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo, " +
		" unidad.cn_curso as curso, unidad.ca_turno as turno, unidad.ca_nombre as unidad " +		
		" from profesor prof " + 
		" inner join unidad on prof.ca_profesor = unidad.ca_tutor or unidad.ca_tutor_adicional = prof.ca_profesor " +
		" inner join ofertas_centro oc on unidad.ca_ofercen = oc.ca_ofercen " + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join ensenanza ense on	ci.ca_ensenanza = ense.ca_ensenanza " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +		 
		" where prof.if_nif= :nif " +
		" and unidad.nu_anio= :anio " + 
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) " +
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping"),

@NamedNativeQuery(name ="TutorCentro.findTutorByUsuario",
query = " select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, "+
		" cen.ca_centro as centro,ci.ca_ensenanza as idEnsenanza,0 as diaBloqueo,ci.ca_ciclo as idCiclo,  ci.dc_codigo as codCiclo, "+
		" ci.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo, " +
		" unidad.cn_curso as curso, unidad.ca_turno as turno, unidad.ca_nombre as unidad " +		
		" from profesor prof " + 
		" inner join unidad on prof.ca_profesor = unidad.ca_tutor or unidad.ca_tutor_adicional = prof.ca_profesor " +
		" inner join ofertas_centro oc on unidad.ca_ofercen = oc.ca_ofercen " + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join ensenanza ense on	ci.ca_ensenanza = ense.ca_ensenanza " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +		 
		" where prof.if_nif= :nif " +		
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) " +
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping"),


@NamedNativeQuery(name ="TutorCentro.findTutorByUsuarioAndAnioAndCentro",
query =" select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, "+
		" cen.ca_centro as centro,ci.ca_ensenanza as idEnsenanza, 0 as diaBloqueo, ci.ca_ciclo as idCiclo,  ci.dc_codigo as codCiclo, "+
		" ci.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo, "+
		" unidad.cn_curso as curso, unidad.ca_turno as turno, unidad.ca_nombre as unidad " +
		" from profesor prof " + 
		" inner join unidad on prof.ca_profesor = unidad.ca_tutor or unidad.ca_tutor_adicional = prof.ca_profesor " +
		" inner join ofertas_centro oc on unidad.ca_ofercen = oc.ca_ofercen " + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +		 
		" where prof.if_nif= :nif " +
		" and unidad.nu_anio = :anio and cen.ca_centro = :centro " + 
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) "+
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping"),

@NamedNativeQuery(name ="TutorCentro.findTutorByUsuarioAndAnioAndCentroAndTipoPractica",
query =" select distinct  prof.ca_profesor as idProfesor, prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1,prof.dl_apellido2 as apellido2, "+
		" cen.ca_centro as centro,ci.ca_ensenanza as idEnsenanza, tp.nu_bloqueo as diaBloqueo, ci.ca_ciclo as idCiclo,  ci.dc_codigo as codCiclo, "+
		" ci.dc_nombre as nombreCiclo, prof.ca_cuentass as cuenta, prof.dc_cuerpo as cuerpo, prof.dc_matricula as matricula, prof.dc_vehiculo as vehiculo, "+
		" unidad.cn_curso as curso, unidad.ca_turno as turno, unidad.ca_nombre as unidad " +
		" from profesor prof " + 
		" inner join unidad on prof.ca_profesor = unidad.ca_tutor or unidad.ca_tutor_adicional = prof.ca_profesor" +
		" inner join ofertas_centro oc on unidad.ca_ofercen = oc.ca_ofercen " + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ci on of.ca_ciclo = ci.ca_ciclo " + 
		" inner join centro cen on oc.ca_centro = cen.ca_centro " +		
		" inner join tipopractica_ensenanzas te on te.ca_ensenanza = ci.ca_ensenanza " +
		" inner join tipo_practica tp on tp.cn_tipo_practica = te.cn_tipo_practica " +
		" where prof.if_nif= :nif " +
		" and unidad.nu_anio = :anio and cen.ca_centro = :centro " + 
		" and te.cn_tipo_practica =  :idTipoPractica " +  
		" and ci.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) "+
		" order by apellido1 asc , apellido2 asc " ,
		  resultSetMapping = "TutorMatricMapping")

 

})

@SqlResultSetMappings({
		@SqlResultSetMapping(name = "TutorMatricMapping", classes = {
				@ConstructorResult(targetClass = TutorCentro.class, columns = {
						@ColumnResult(name = "idProfesor", type = String.class),
						@ColumnResult(name = "nombre", type = String.class),
						@ColumnResult(name = "apellido1", type = String.class),
						@ColumnResult(name = "apellido2", type = String.class),
						@ColumnResult(name = "centro", type = String.class),
						@ColumnResult(name = "idEnsenanza", type=String.class),
                        			@ColumnResult(name = "diaBloqueo", type=Integer.class),
                        			@ColumnResult(name = "idCiclo", type = String.class),
						@ColumnResult(name = "codCiclo", type = String.class),
						@ColumnResult(name = "nombreCiclo", type = String.class),
						@ColumnResult(name = "cuenta", type = String.class),
						@ColumnResult(name = "cuerpo", type = String.class),
						@ColumnResult(name = "matricula", type = String.class),
						@ColumnResult(name = "vehiculo", type = String.class),
						@ColumnResult(name = "curso", type = Integer.class),
						@ColumnResult(name = "turno", type = String.class),
						@ColumnResult(name = "unidad", type = String.class) }) }),
		@SqlResultSetMapping(name = "TutorMatricLiteMapping", classes = {
				@ConstructorResult(targetClass = TutorCentro.class, columns = {
						@ColumnResult(name = "idProfesor", type = String.class),
						@ColumnResult(name = "nombre", type = String.class),
						@ColumnResult(name = "apellido1", type = String.class),
						@ColumnResult(name = "apellido2", type = String.class),
						@ColumnResult(name = "dni", type = String.class),
						@ColumnResult(name = "deSauce", type = Boolean.class)}) }) })

		 

public class TutorCentro implements Serializable {

    @Id
    public String idProfesor;
    public String nombre;    
    public String apellido1;
    public String apellido2;
    public String centro;
    public String idEnsenanza;
    public int diaBloqueo;
    public String idCiclo;
    public String codCiclo;
    public String nombreCiclo;
    public String cuenta;
    public String cuerpo; 
    public String matricula;    
    public String vehiculo;
    public int curso;
    public String turno; 
    public String unidad;  
    public String dni;
    public Boolean deSauce;

    public TutorCentro() {
    }

    public TutorCentro(String idProfesor, String nombre, String apellido1, String apellido2, String centro,String idEnsenanza, int diaBloqueo, String idCiclo, String codCiclo, String nombreCiclo, String cuenta, String  cuerpo, String matricula, String vehiculo, int curso, String turno, String unidad) {
        
    	this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.centro = centro;
        this.idEnsenanza = idEnsenanza;
        this.diaBloqueo = diaBloqueo;
        this.idCiclo = idCiclo;
        this.codCiclo = codCiclo;
        this.nombreCiclo = nombreCiclo;      
        this.cuenta = cuenta;
        this.cuerpo = cuerpo;
        this.matricula = matricula;
        this.vehiculo = vehiculo;
        this.curso = curso;
        this.turno = turno;      
        this.unidad = unidad;
    }
    
    public TutorCentro(String idProfesor, String nombre, String apellido1, String apellido2, String centro,String idEnsenanza, int diaBloqueo, String idCiclo, String codCiclo, String nombreCiclo, String cuenta, String  cuerpo, String matricula, String vehiculo, int curso, String turno, String unidad,String dni, Boolean deSauce) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.centro = centro;
        this.idEnsenanza = idEnsenanza;
        this.diaBloqueo = diaBloqueo;
        this.idCiclo = idCiclo;
        this.codCiclo = codCiclo;
        this.nombreCiclo = nombreCiclo;      
        this.cuenta = cuenta;
        this.cuerpo = cuerpo;
        this.matricula = matricula;
        this.vehiculo = vehiculo;
        this.curso = curso;
        this.turno = turno;      
        this.unidad = unidad;
        this.dni = dni;
        this.deSauce = deSauce;
    }
    
    public TutorCentro(String idProfesor, String nombre, String apellido1, String apellido2,String dni, Boolean deSauce) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.dni = dni;
        this.deSauce = deSauce;
    }
}

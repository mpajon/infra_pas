
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
@NamedNativeQuery(name ="OfertaMatric.findListadoOferMatric",
query ="  select idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo, desOferta, curso, turno,unidad, count(distinct matriculas)  as numAlumnos, count(matdistribucion) as numDistribuidos, count(anexo) as numAnexos from ( " +
		" select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza, ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo ,mat.dl_desCiclo as desCiclo ,mat.dl_desoferta as desOferta, " +
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, "+			 
		" mat.ca_matricula as matriculas, di.ca_matricula as matdistribucion, di.cn_anexo as anexo  " +
		" from Matricula mat " +		 
		" left outer join distribucion di on di.ca_matricula = mat.ca_matricula " +
		" inner join ofertas_centro oc on mat.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa "+
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " +
		" inner join Centro centro on oc.ca_centro = centro.ca_centro " +
		" inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.ca_unidad = uni.ca_unidad " +
		" where mat.nu_anio = :anio and uni.nu_anio= :anio and  centro.ca_centro = :centro " +
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		" group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,mat.dl_desoferta , uni.cn_curso, uni.ca_turno, uni.ca_nombre, mat.ca_matricula, di.ca_matricula, di.cn_anexo "+
		" ) as tabla group by idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo , desOferta, curso, turno,unidad "+
		" order by desOferta asc, curso asc, codCiclo asc, desCiclo asc " ,		
		  resultSetMapping = "OfertaMatricMapping"),


@NamedNativeQuery(name ="OfertaMatric.findListadoParaMatriculacion",
query ="  select idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo, desOferta, curso, turno,unidad, count(distinct matriculas)  as numAlumnos, count(matdistribucion<>'') as numDistribuidos, count(anexo>0) as numAnexos from ( " +
		" select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza, ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo ,ciclo.dc_nombre as desCiclo ,mat.dl_desoferta as desOferta, " +
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, "+			 
		" mat.ca_matricula as matriculas, di.ca_matricula as matdistribucion, di.cn_anexo as anexo  " +
		" from Matricula mat " +		 
		" left outer join distribucion di on di.ca_matricula = mat.ca_matricula " +
		" inner join ofertas_centro oc on mat.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa "+
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " +
		" inner join Centro centro on oc.ca_centro = centro.ca_centro " +
		" inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.ca_unidad = uni.ca_unidad " +
		" where mat.nu_anio = :anio and uni.nu_anio= :anio and  centro.ca_centro = :centro " +
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		" group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,mat.dl_desoferta , uni.cn_curso, uni.ca_turno, uni.ca_nombre, mat.ca_matricula, di.ca_matricula, di.cn_anexo "+
		" union " + 
		" select distinct oc.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, ciclo.dc_codigo as codCiclo ,ciclo.dc_nombre as desCiclo ,of.dc_nombre as desOferta," + 
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, NULL as matriculas, NULL  as matdistribucion, -1 as anexo " + 
		" from ofertas_centro oc" + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " + 
		" inner join Centro centro on oc.ca_centro = centro.ca_centro " + 
		" inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen " + 
		" where uni.nu_anio= :anio  and  centro.ca_centro = :centro" + 
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		" group by oc.ca_ofercen, ciclo.ca_ensenanza,  ciclo.ca_ciclo , of.dc_codigo  ,ciclo.dc_nombre  ,of.dc_nombre ,  uni.cn_curso, uni.ca_turno, uni.ca_nombre " +		
		" ) as tabla group by idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo , desOferta, curso, turno,unidad "+
		" order by desOferta asc, curso asc, codCiclo asc, desCiclo asc " ,		
		  resultSetMapping = "OfertaMatricMapping"),

//Incidencia producción, para una unidad creada de forma manual, no se puede matricular a ningún alumno/a.

@NamedNativeQuery(name ="OfertaMatric.findListadoOferMatricTutor",
query ="select idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo, desOferta, curso, turno,unidad, " + 

		" count( case when matriculas <> 'mat' then matriculas end ) as numAlumnos, " + 
		"	count( case when matdistribucion <> '' then matdistribucion end ) as numDistribuidos, " + 
		"	count(  case when anexo <> 0 then anexo end ) as numAnexos from (" + 	 
		" select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza, ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo ,of.dc_nombre as desCiclo ,mat.dl_desoferta as desOferta, " +
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, "+	
		" mat.ca_matricula as matriculas, di.ca_matricula as matdistribucion, di.cn_anexo as anexo  " +
		" from Matricula mat " +		 
		" left outer join distribucion di on di.ca_matricula = mat.ca_matricula " +
		" inner join ofertas_centro oc on mat.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa "+
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " +
		" inner join Centro centro on oc.ca_centro = centro.ca_centro " +
		" inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.ca_unidad = uni.ca_unidad " +
		" where mat.nu_anio = :anio and uni.nu_anio= :anio and centro.ca_centro = :centro and (uni.ca_tutor= :tutor OR uni.ca_tutor_adicional = :tutor) " +
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		" group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,of.dc_nombre,mat.dl_desoferta , uni.cn_curso, uni.ca_turno, uni.ca_nombre, mat.ca_matricula, di.ca_matricula, di.cn_anexo "+
		//subquery no sauce...
		" union " +
		" select distinct oc.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza, ciclo.ca_ciclo as idCiclo, ciclo.dc_codigo as codCiclo ,of.dc_nombre as desCiclo ,of.dc_nombre as desOferta, " + 
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad," + 
		" 'mat' as matriculas, '' as matdistribucion, 0 as anexo " +
		"  from  unidad uni " + 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo  " + 
		" inner join Centro centro on oc.ca_centro = centro.ca_centro   " + 
		" where uni.nu_anio= :anio  and uni.fl_desauce is false and centro.ca_centro = :centro  and (uni.ca_tutor is null OR uni.ca_tutor_adicional is null) " +		
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) " + 
		" group by oc.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, ciclo.dc_codigo,  uni.cn_curso, uni.ca_turno, uni.ca_nombre, of.dc_nombre, uni.ca_nombre " + 
		//Nov2019. subquery para tutor con unidades que no tiene alumnado matriculado
		 " union " +
		" select distinct oc.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza, ciclo.ca_ciclo as idCiclo, ciclo.dc_codigo as codCiclo ,of.dc_nombre as desCiclo ,of.dc_nombre as desOferta, " + 
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad," + 
		" 'mat' as matriculas, '' as matdistribucion, 0 as anexo " +
		"  from  unidad uni " + 
		" inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " + 
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo  " + 
		" inner join Centro centro on oc.ca_centro = centro.ca_centro   " + 
		" where uni.nu_anio= :anio  and uni.fl_desauce is true and centro.ca_centro = :centro  and  (uni.ca_tutor= :tutor OR uni.ca_tutor_adicional = :tutor)  " +	
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL) " + 
		" group by oc.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, ciclo.dc_codigo,  uni.cn_curso, uni.ca_turno, uni.ca_nombre, of.dc_nombre, uni.ca_nombre " +
		" ) as tabla " + 
		" group by 	idOfertaCentro,	idEnsenanza,idCiclo,codCiclo,desCiclo,desOferta,curso,turno,unidad " +
		" order by desOferta asc, curso asc, codCiclo asc, desCiclo asc "  ,				
		  resultSetMapping = "OfertaMatricMapping"),


@NamedNativeQuery(name ="OfertaMatric.findByTipoPractica",
query ="select idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo, desOferta, curso, turno,unidad, count(distinct matriculas)  as numAlumnos, count(matdistribucion) as numDistribuidos, count(anexo) as numAnexos  from (" +
		" select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo ,mat.dl_desCiclo as desCiclo ,mat.dl_desoferta as desOferta, " +
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, "+	
		" mat.ca_matricula as matriculas, di.ca_matricula as matdistribucion, di.cn_anexo as anexo  " +
		" from Matricula mat " +	
		" left outer join distribucion di on di.ca_matricula = mat.ca_matricula " +
		" inner join ofertas_centro oc on mat.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa "+
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " +
		" inner join Centro centro on oc.ca_centro = centro.ca_centro " + 
		" inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.ca_unidad = uni.ca_unidad " +
		" where mat.nu_anio = :anio and uni.nu_anio= :anio  and centro.ca_centro = :centro " +
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" +
		" and ciclo.ca_ensenanza in (select distinct ca_ensenanza from tipopractica_ensenanzas te where te.cn_tipo_practica =  :idTipoPractica) " + 
		" group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,mat.dl_desoferta , uni.cn_curso, uni.ca_turno, uni.ca_nombre, mat.ca_matricula, di.ca_matricula, di.cn_anexo  "+
		" ) as tabla group by idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo , desOferta, curso, turno,unidad "+
		" order by desOferta asc, curso asc, codCiclo asc, desCiclo asc " ,			
		  resultSetMapping = "OfertaMatricMapping"),


@NamedNativeQuery(name ="OfertaMatric.findByTipoPracticaAndTutor",
query ="select idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo, desOferta, curso, turno,unidad, count(distinct matriculas)  as numAlumnos, count(matdistribucion) as numDistribuidos, count(anexo) as numAnexos  from ( "+
		" select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo ,mat.dl_desCiclo as desCiclo ,mat.dl_desoferta as desOferta, "+
		" uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, "+
		" mat.ca_matricula as matriculas, di.ca_matricula as matdistribucion, di.cn_anexo as anexo  " +
		" from Matricula mat " +	
		" left outer join distribucion di on di.ca_matricula = mat.ca_matricula " +
		" inner join ofertas_centro oc on mat.ca_ofercen = oc.ca_ofercen " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa "+
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " +
		" inner join Centro centro on oc.ca_centro = centro.ca_centro " +
		" inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.ca_unidad = uni.ca_unidad " +
		" where mat.nu_anio = :anio and uni.nu_anio= :anio and centro.ca_centro = :centro and (uni.ca_tutor= :tutor OR uni.ca_tutor_adicional= :tutor)" +
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" +
		" and ciclo.ca_ensenanza in (select distinct ca_ensenanza from tipopractica_ensenanzas te where te.cn_tipo_practica =  :idTipoPractica) " + 
		" group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,mat.dl_desoferta , uni.cn_curso, uni.ca_turno, uni.ca_nombre, mat.ca_matricula, di.ca_matricula, di.cn_anexo "+
		" ) as tabla group by idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desCiclo , desOferta, curso, turno,unidad "+
		" order by desOferta asc, curso asc, codCiclo asc, desCiclo asc " ,				
		  resultSetMapping = "OfertaMatricMapping"),


@NamedNativeQuery(name ="OfertaMatric.findByTipoPracticaAndPeriodo",
query = "select distinct idOfertaCentro, idEnsenanza, idCiclo, codCiclo , desOferta as desCiclo, desOferta, curso, turno, unidad, ca_unidad , " + 
		" count(case when matriculas <> 'mat' then matriculas end) as numAlumnos, " +
		" count(case when distribucion<>0 then distribucion end) as numDistribuidos " + 
		// Subquery contador de anexos de tipo/periodo practica  -- no coger la matricula porque si no hay niños entonces no sale nada.//
		" ,(select count(distinct anx.cn_anexo) " + 
		" from anexo_contrato anx inner join convenio conv on conv.cn_convenio = anx.cn_convenio " + 
		" where conv.cn_tipo_practica = :idTipoPractica and anx.ca_ofercen = tabla.idOfertaCentro and anx.cn_periodo =:idPeriodoPractica and anx.ca_unidad = tabla.ca_unidad ) as numAnexos " + 
		" from ( " + 
		"			select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo , mat.dl_desoferta as desOferta, " + 
		"			uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, uni.ca_unidad,  mat.ca_matricula as matriculas, dist_en_el_periodo.ca_matricula as matdistribucion,dist_en_el_periodo.cn_distribucion as distribucion, " + 
		"			dist_en_el_periodo.cn_periodo as periodoDis" + 

		"			 from Matricula mat " + 
			"			 inner join ofertas_centro oc on	mat.ca_ofercen = oc.ca_ofercen " + 
			"			 inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
			"			 inner join Centro centro on	oc.ca_centro = centro.ca_centro" + 
			"			 inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " + 
			"			 inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.nu_anio = uni.nu_anio and mat.ca_unidad = uni.ca_unidad and uni.nu_anio = :anio" + 
						
						//-- SUBQUERY: distribuciones en el periodo --
			"			 left join (SELECT di.cn_distribucion, di.ca_matricula, di_pe.cn_periodo, di.cn_anexo " + 
			"              				FROM distribucion di" + 
			"              				inner join distribucion_periodo di_pe on di_pe.cn_distribucion = di.cn_distribucion and di_pe.cn_periodo =:idPeriodoPractica " + 
			"			 				inner join periodo_practica pe on pe.cn_periodo = di_pe.cn_periodo and pe.cn_periodo =:idPeriodoPractica " + 
			"			 				where di.fe_fbaja is null" + 
			"          	 			) dist_en_el_periodo" + 
			"          	 on dist_en_el_periodo.ca_matricula = mat.ca_matricula" + 
		"			 where " + 
		"			 mat.nu_anio =:anio" + 
		"			 and centro.ca_centro =:centro" + 
		"			 and ciclo.fe_fbaja is null " + 
		"			 and oc.fl_vigente = true " + 
		"			 and ciclo.ca_ensenanza in(select distinct ca_ensenanza from tipopractica_ensenanzas te where te.cn_tipo_practica =:idTipoPractica) " + 
		"			 group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,mat.dl_desoferta , uni.cn_curso, " + 
		"			 uni.ca_turno, uni.ca_nombre, uni.ca_unidad, mat.ca_matricula, dist_en_el_periodo.ca_matricula, dist_en_el_periodo.cn_distribucion," + 
		"			 dist_en_el_periodo.cn_periodo" +	
		 //Nov2019. tiene que salir unidades de sauce sin matriculas
		" 		union " +  
		"		 	select distinct oc.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, ciclo.dc_codigo as codCiclo , of.dc_nombre as desOferta," + 
		"		 	uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad,uni.ca_unidad, 'mat' as matriculas, " + 
		"		 	'' as matdistribucion, 0 as distribucion, " + 
		"		 	0 as periodoDis " + 
		"		 	from ofertas_centro oc " + 
		"		 	inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		"		 	inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " + 
		"		 	inner join Centro centro on oc.ca_centro = centro.ca_centro " + 
		"		 	inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen" + 	  
		"		 	where uni.nu_anio= :anio	 	" + 		 
		"			and centro.ca_centro =:centro" + 
		"		 	and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		"  			group by oc.ca_ofercen , ciclo.ca_ensenanza , ciclo.ca_ciclo , ciclo.dc_codigo , ciclo.dc_nombre , of.dc_nombre , uni.cn_curso, " + 
		"			uni.ca_turno, uni.ca_nombre , uni.ca_unidad" +
		"		 	) as tabla" +	 
		" group by idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desOferta, curso, turno, unidad, ca_unidad" + 
		" order by desOferta asc, curso asc, codCiclo asc ",
		  resultSetMapping = "OfertaMatricMapping"),


@NamedNativeQuery(name ="OfertaMatric.findByTipoPracticaAndPeriodoAndUnidad",
query = "select distinct idOfertaCentro, idEnsenanza, idCiclo, codCiclo , desOferta as desCiclo, desOferta, curso, turno, unidad, ca_unidad , " + 
		" count(case when matriculas <> 'mat' then matriculas end) as numAlumnos, " +
		" count(case when distribucion<>0 then distribucion end) as numDistribuidos " + 
		// Subquery contador de anexos de tipo/periodo practica  -- no coger la matricula porque si no hay niños entonces no sale nada.//
		" ,(select count(distinct anx.cn_anexo) " + 
		" from anexo_contrato anx inner join convenio conv on conv.cn_convenio = anx.cn_convenio " + 
		" where conv.cn_tipo_practica = :idTipoPractica and anx.ca_ofercen = tabla.idOfertaCentro and anx.cn_periodo =:idPeriodoPractica and anx.ca_unidad = tabla.ca_unidad ) as numAnexos " + 
		" from ( " + 
		"			select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo , mat.dl_desoferta as desOferta, " + 
		"			uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, uni.ca_unidad,  mat.ca_matricula as matriculas, dist_en_el_periodo.ca_matricula as matdistribucion,dist_en_el_periodo.cn_distribucion as distribucion, " + 
		"			dist_en_el_periodo.cn_periodo as periodoDis" + 

		"			 from Matricula mat " + 
			"			 inner join ofertas_centro oc on	mat.ca_ofercen = oc.ca_ofercen " + 
			"			 inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
			"			 inner join Centro centro on	oc.ca_centro = centro.ca_centro" + 
			"			 inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " + 
			"			 inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.nu_anio = uni.nu_anio and mat.ca_unidad = uni.ca_unidad and uni.nu_anio = :anio" + 
						
						//-- SUBQUERY: distribuciones en el periodo --
			"			 left join (SELECT di.cn_distribucion, di.ca_matricula, di_pe.cn_periodo, di.cn_anexo " + 
			"              				FROM distribucion di" + 
			"              				inner join distribucion_periodo di_pe on di_pe.cn_distribucion = di.cn_distribucion and di_pe.cn_periodo =:idPeriodoPractica " + 
			"			 				inner join periodo_practica pe on pe.cn_periodo = di_pe.cn_periodo and pe.cn_periodo =:idPeriodoPractica " + 
			"			 				where di.fe_fbaja is null" + 
			"          	 			) dist_en_el_periodo" + 
			"          	 on dist_en_el_periodo.ca_matricula = mat.ca_matricula" + 
		"			 where " + 
		"			 mat.nu_anio =:anio" + 
		"			 and centro.ca_centro =:centro " + 
		"			 and uni.ca_nombre= :unidad and uni.ca_ofercen= :ofercen and uni.cn_curso= :curso and uni.ca_turno= :turno"+
		"			 and ciclo.fe_fbaja is null " + 
		"			 and oc.fl_vigente = true " + 
		"			 and ciclo.ca_ensenanza in(select distinct ca_ensenanza from tipopractica_ensenanzas te where te.cn_tipo_practica =:idTipoPractica) " + 
		"			 group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,mat.dl_desoferta , uni.cn_curso, " + 
		"			 uni.ca_turno, uni.ca_nombre, uni.ca_unidad, mat.ca_matricula, dist_en_el_periodo.ca_matricula, dist_en_el_periodo.cn_distribucion," + 
		"			 dist_en_el_periodo.cn_periodo" +	
		 //Nov2019. tiene que salir unidades de sauce sin matriculas
		" 		union " +  
		"		 	select distinct oc.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, ciclo.dc_codigo as codCiclo , of.dc_nombre as desOferta," + 
		"		 	uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad,uni.ca_unidad, 'mat' as matriculas, " + 
		"		 	'' as matdistribucion, 0 as distribucion, " + 
		"		 	0 as periodoDis " + 
		"		 	from ofertas_centro oc " + 
		"		 	inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		"		 	inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " + 
		"		 	inner join Centro centro on oc.ca_centro = centro.ca_centro " + 
		"		 	inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen" + 	  
		"		 	where uni.nu_anio= :anio and uni.ca_nombre= :unidad and uni.ca_ofercen= :ofercen and uni.cn_curso= :curso and uni.ca_turno= :turno " + 		 
		"			and centro.ca_centro =:centro" + 
		"		 	and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		"  			group by oc.ca_ofercen , ciclo.ca_ensenanza , ciclo.ca_ciclo , ciclo.dc_codigo , ciclo.dc_nombre , of.dc_nombre , uni.cn_curso, " + 
		"			uni.ca_turno, uni.ca_nombre , uni.ca_unidad" +
		"		 	) as tabla" +	 
		" group by idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desOferta, curso, turno, unidad, ca_unidad" + 
		" order by desOferta asc, curso asc, codCiclo asc ",
		  resultSetMapping = "OfertaMatricMapping"),

@NamedNativeQuery(name ="OfertaMatric.findByTipoPracticaAndPeriodoAndTutor",
query = "select distinct idOfertaCentro, idEnsenanza, idCiclo, codCiclo , desOferta as desCiclo, desOferta, curso, turno, unidad, ca_unidad , " + 
		" count(case when matriculas <> 'mat' then matriculas end) as numAlumnos, " +
		" count(case when distribucion<>0 then distribucion end) as numDistribuidos " + 
		// Subquery contador de anexos de tipo/periodo practica  -- no coger la matricula porque si no hay niños entonces no sale nada.//
		" ,(select count(distinct anx.cn_anexo) " + 
		" from anexo_contrato anx inner join convenio conv on conv.cn_convenio = anx.cn_convenio " + 
		" where conv.cn_tipo_practica = :idTipoPractica and anx.ca_ofercen = tabla.idOfertaCentro and anx.cn_periodo =:idPeriodoPractica and anx.ca_unidad = tabla.ca_unidad ) as numAnexos " + 
		" from ( " + 
		"			select distinct mat.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, mat.dc_codCiclo as codCiclo , mat.dl_desoferta as desOferta, " + 
		"			uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad, uni.ca_unidad,  mat.ca_matricula as matriculas, dist_en_el_periodo.ca_matricula as matdistribucion,dist_en_el_periodo.cn_distribucion as distribucion, " + 
		"			dist_en_el_periodo.cn_periodo as periodoDis" + 

		"			 from Matricula mat " + 
			"			 inner join ofertas_centro oc on	mat.ca_ofercen = oc.ca_ofercen " + 
			"			 inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
			"			 inner join Centro centro on	oc.ca_centro = centro.ca_centro" + 
			"			 inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " + 
			"			 inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen and mat.nu_anio = uni.nu_anio and mat.ca_unidad = uni.ca_unidad and uni.nu_anio = :anio" + 
						
						//-- SUBQUERY: distribuciones en el periodo --
			"			 left join (SELECT di.cn_distribucion, di.ca_matricula, di_pe.cn_periodo, di.cn_anexo " + 
			"              				FROM distribucion di" + 
			"              				inner join distribucion_periodo di_pe on di_pe.cn_distribucion = di.cn_distribucion and di_pe.cn_periodo =:idPeriodoPractica " + 
			"			 				inner join periodo_practica pe on pe.cn_periodo = di_pe.cn_periodo and pe.cn_periodo =:idPeriodoPractica " + 
			"			 				where di.fe_fbaja is null" + 
			"          	 			) dist_en_el_periodo" + 
			"          	 on dist_en_el_periodo.ca_matricula = mat.ca_matricula" + 
		"			 where " + 
		"			 mat.nu_anio =:anio" + 
		" 			 and (uni.ca_tutor= :tutor OR uni.ca_tutor_adicional = :tutor)" + // Esta consulta es igual que la anterior excepto por este filtro //
		"			 and centro.ca_centro =:centro" + 
		"			 and ciclo.fe_fbaja is null " + 
		"			 and oc.fl_vigente = true " + 
		"			 and ciclo.ca_ensenanza in(select distinct ca_ensenanza from tipopractica_ensenanzas te where te.cn_tipo_practica =:idTipoPractica) " + 
		"			 group by mat.ca_ofercen, ciclo.ca_ensenanza, ciclo.ca_ciclo, mat.dc_codCiclo ,mat.dl_desCiclo  ,mat.dl_desoferta , uni.cn_curso, " + 
		"			 uni.ca_turno, uni.ca_nombre, uni.ca_unidad, mat.ca_matricula, dist_en_el_periodo.ca_matricula, dist_en_el_periodo.cn_distribucion," + 
		"			 dist_en_el_periodo.cn_periodo" +	
		 //Nov2019. tiene que salir unidades de sauce sin matriculas
		" 		union " +  
		"		 	select distinct oc.ca_ofercen as  idOfertaCentro, ciclo.ca_ensenanza as idEnsenanza,ciclo.ca_ciclo as idCiclo, ciclo.dc_codigo as codCiclo , of.dc_nombre as desOferta," + 
		"		 	uni.cn_curso as curso, uni.ca_turno as turno, uni.ca_nombre as unidad,uni.ca_unidad, 'mat' as matriculas, " + 
		"		 	'' as matdistribucion, 0 as distribucion, " + 
		"		 	0 as periodoDis " + 
		"		 	from ofertas_centro oc " + 
		"		 	inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		"		 	inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " + 
		"		 	inner join Centro centro on oc.ca_centro = centro.ca_centro " + 
		"		 	inner join unidad uni on uni.ca_ofercen = oc.ca_ofercen" + 	  
		"		 	where uni.nu_anio= :anio	 	" + 
		"		 	and (uni.ca_tutor= :tutor OR uni.ca_tutor_adicional = :tutor) " + 
		"			and centro.ca_centro =:centro" + 
		"		 	and ciclo.fe_fbaja is null and oc.fl_vigente = true and (of.fl_vigente = true OR of.cn_aniofin is NULL)" + 
		"  			group by oc.ca_ofercen , ciclo.ca_ensenanza , ciclo.ca_ciclo , ciclo.dc_codigo , ciclo.dc_nombre , of.dc_nombre , uni.cn_curso, " + 
		"			uni.ca_turno, uni.ca_nombre , uni.ca_unidad" +
		"		 	) as tabla" +	 
		" group by idOfertaCentro, idEnsenanza, idCiclo, codCiclo ,desOferta, curso, turno, unidad, ca_unidad" + 
		" order by desOferta asc, curso asc, codCiclo asc ",					
		  resultSetMapping = "OfertaMatricMapping"), 

})



@SqlResultSetMapping(name="OfertaMatricMapping",
        classes = {
                @ConstructorResult(targetClass = OfertaMatric.class,
                        columns = {@ColumnResult(name="idOfertaCentro", type=String.class), 
                        		@ColumnResult(name="idEnsenanza", type=String.class),
                        		@ColumnResult(name="idCiclo", type=String.class),
                        		@ColumnResult(name="codCiclo", type=String.class),
                        		@ColumnResult(name="desCiclo", type=String.class),
                        		@ColumnResult(name="desOferta", type=String.class),
                        		@ColumnResult(name="curso", type=Integer.class),
                        		@ColumnResult(name="turno", type=String.class),                        		
                        		@ColumnResult(name="unidad", type=String.class),
                        		@ColumnResult(name="numAlumnos", type=Integer.class),
                        		@ColumnResult(name="numDistribuidos", type=Integer.class),
                        		@ColumnResult(name="numAnexos", type=Integer.class)}
                )}
)



public class OfertaMatric implements Serializable {

    @Id
    public String idOfertaCentro;
    public String idEnsenanza;
    public String idCiclo;
    public String codCiclo;
    public String desCiclo;
    public String desOferta;
    public int curso;
    public String turno; 
    public String unidad;    
    public int numAlumnos;
    public int numDistribuidos;
    public int numAnexos;

    public OfertaMatric() {
    }

    public OfertaMatric(String idOfertaCentro, String idEnsenanza, String idCiclo, String codCiclo, String desCiclo, String desOferta, int curso, String turno, String unidad, int numAlumnos, int numDistribuidos, int numAnexos) {
        this.idOfertaCentro = idOfertaCentro;
        this.idCiclo = idCiclo;
        this.idEnsenanza = idEnsenanza;
        this.codCiclo = codCiclo;
        this.desCiclo = desCiclo;
        this.desOferta = desOferta;
        this.curso = curso;
        this.turno = turno;      
        this.unidad = unidad;
        this.numAlumnos = numAlumnos;
        this.numDistribuidos = numDistribuidos;
        this.numAnexos = numAnexos;
    }
}

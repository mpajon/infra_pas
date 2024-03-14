package es.princast.gepep.domain;

import java.io.Serializable;

import javax.persistence.*;

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
@NamedNativeQuery(name ="GastoAgrupado.findGastoAlumnoCursoNuevo",
query ="SELECT  -1 as idGastoAlumno, sum(ga.im_num_billete) as numBillete, sum(ga.nu_num_dieta) as numDieta, sum(ga.nu_num_km) as numKm, sum(ga.nu_num_pension) as numPension, " + 
		"  sum(ga.im_otros_gastos)  as otrosGastos, ga.im_precio_billete as precioBillete, ga.im_precio_km  as precioKm,  ga.im_precio_dieta as precioDieta,  ga.im_precio_pension  as precioPension," +
		" ga.cn_periodo_liquidacion as idPeriodoLiquidacion,ga.ca_matricula as idMatricula, " +
		" ga.nu_distancia_unitaria as distanciaUnitaria,ga.nu_numero_dias as numDias, ga.dl_localidad_centro_trabajo as localidadCentroTrabajo, ga.cn_distribucion_periodo as idDistribucionPeriodo " +
		" FROM ( " + 
		" 	select distinct ga.cn_gasto_alumno, ga.im_num_billete,ga.nu_num_dieta,ga.nu_num_km,ga.nu_num_pension,ga.im_otros_gastos,ga.im_precio_billete,ga.im_precio_km,ga.im_precio_dieta, ga.im_precio_pension," + 
		"	ga.cn_periodo_liquidacion,ga.ca_matricula,ga.nu_distancia_unitaria, ga.nu_numero_dias, ga.dl_localidad_centro_trabajo, ga.cn_distribucion_periodo" +
		"	FROM matricula m inner join ofertas_centro oc on oc.ca_ofercen = m.ca_ofercen  "+		
		"	inner join oferta_formativa off on off.ca_oferta_formativa = oc.ca_oferta_formativa "+ 
		"   inner join distribucion d on d.ca_matricula = m.ca_matricula "+
		"   inner join distribucion_periodo dp on dp.cn_distribucion = d.cn_distribucion "+
		"   inner join periodo_practica pp on  pp.cn_curso_aca= :anioAcademico and pp.cn_periodo=dp.cn_periodo  and pp.cn_tipo_gasto=1 "+
		"   inner join alumno a on a.ca_alumno = m.ca_alumno "+
		"   inner join gasto_alumno ga on ga.ca_matricula = m.ca_matricula and ga.cn_distribucion_periodo = dp.cn_distribucion_periodo   "+
		" 	inner join unidad u on u.ca_unidad = m.ca_unidad "+
		"   WHERE off.ca_ciclo=  :idCiclo " +
		"   and  oc.ca_centro = :idCentro " +
		"   and m.nu_anio = :curso " +
		"	and  u.ca_turno= :regimen " + 
		"   and  u.ca_nombre= :grupo " + 
		"   and d.fe_fbaja is null " + 
		"   and ga.cn_periodo_liquidacion = :periodo " + 
		" ) ga group by ga.cn_periodo_liquidacion,ga.ca_matricula,ga.im_precio_km,ga.im_precio_pension,ga.im_precio_dieta, ga.im_precio_billete, ga.nu_distancia_unitaria, ga.nu_numero_dias, ga.dl_localidad_centro_trabajo, ga.cn_distribucion_periodo ",
		resultSetMapping = "GastoAgrupadoMapping"),

		@NamedNativeQuery(name ="GastoAgrupado.findGastoAlumnoCursoNuevoTipo2",
				query ="SELECT  -1 as idGastoAlumno, sum(ga.im_num_billete) as numBillete, sum(ga.nu_num_dieta) as numDieta, sum(ga.nu_num_km) as numKm, sum(ga.nu_num_pension) as numPension, " +
						"  sum(ga.im_otros_gastos)  as otrosGastos, ga.im_precio_billete as precioBillete, ga.im_precio_km  as precioKm,  ga.im_precio_dieta as precioDieta,  ga.im_precio_pension  as precioPension," +
						" ga.cn_periodo_liquidacion as idPeriodoLiquidacion,ga.ca_matricula as idMatricula, " +
						" ga.nu_distancia_unitaria as distanciaUnitaria,ga.nu_numero_dias as numDias, ga.dl_localidad_centro_trabajo as localidadCentroTrabajo, ga.cn_distribucion_periodo as idDistribucionPeriodo  " +
						" FROM ( " +
						" 	select distinct ga.cn_gasto_alumno, ga.im_num_billete,ga.nu_num_dieta,ga.nu_num_km,ga.nu_num_pension,ga.im_otros_gastos,ga.im_precio_billete,ga.im_precio_km,ga.im_precio_dieta, ga.im_precio_pension," +
						"	ga.cn_periodo_liquidacion,ga.ca_matricula,ga.nu_distancia_unitaria, ga.nu_numero_dias, ga.dl_localidad_centro_trabajo, ga.cn_distribucion_periodo" +
						"	FROM matricula m inner join ofertas_centro oc on oc.ca_ofercen = m.ca_ofercen  "+
						"	inner join oferta_formativa off on off.ca_oferta_formativa = oc.ca_oferta_formativa "+
						"   inner join distribucion d on d.ca_matricula = m.ca_matricula "+
						"   inner join distribucion_periodo dp on dp.cn_distribucion = d.cn_distribucion "+
						"   inner join periodo_practica pp on  pp.cn_curso_aca= :anioAcademico and pp.cn_periodo=dp.cn_periodo and pp.cn_tipo_gasto=2 "+
						"   inner join alumno a on a.ca_alumno = m.ca_alumno "+
						"   inner join gasto_alumno ga on ga.ca_matricula = m.ca_matricula and ga.cn_distribucion_periodo = dp.cn_distribucion_periodo   "+
						" 	inner join unidad u on u.ca_unidad = m.ca_unidad "+
						"   WHERE off.ca_ciclo=  :idCiclo " +
						"   and  oc.ca_centro = :idCentro " +
						"   and m.nu_anio = :curso " +
						"	and  u.ca_turno= :regimen " +
						"   and  u.ca_nombre= :grupo " +
						"   and d.fe_fbaja is null " +
						"   and ga.cn_periodo_liquidacion = :periodo " +
						"   and ga.fl_validado = true " +
						" ) ga group by ga.cn_periodo_liquidacion,ga.ca_matricula,ga.im_precio_km,ga.im_precio_pension,ga.im_precio_dieta, ga.im_precio_billete, ga.nu_distancia_unitaria, ga.nu_numero_dias, ga.dl_localidad_centro_trabajo, ga.cn_distribucion_periodo ",
				resultSetMapping = "GastoAgrupadoMapping")

})

@SqlResultSetMappings({
@SqlResultSetMapping(name="GastoAgrupadoMapping",
        classes = {
                @ConstructorResult(targetClass = GastoAgrupado.class,
                        columns = {@ColumnResult(name="idGastoAlumno", type=Integer.class), 
                        		@ColumnResult(name="numBillete", type=Integer.class),
                        		@ColumnResult(name="numDieta", type=Integer.class),
                        		@ColumnResult(name="numKm", type=Integer.class),
                        		@ColumnResult(name="numPension", type=Integer.class),
                        		@ColumnResult(name="otrosGastos", type=Float.class),
                        		@ColumnResult(name="precioBillete", type=Float.class),                        	 
                        		@ColumnResult(name="precioKm", type=Float.class),
                        		@ColumnResult(name="precioDieta", type=Float.class),                        		
                        		@ColumnResult(name="precioPension", type=Float.class),
                        		@ColumnResult(name="idPeriodoLiquidacion", type=Integer.class),
                        		@ColumnResult(name="idMatricula", type=String.class),
								@ColumnResult(name="distanciaUnitaria",type=Float.class),
								@ColumnResult(name="numDias", type=Integer.class),
								@ColumnResult(name="localidadCentroTrabajo", type=String.class),
								@ColumnResult(name="idDistribucionPeriodo", type=Long.class),
                        		}
                )})

})


public class GastoAgrupado implements Serializable {

    @Id
    public Integer idGastoAlumno;
    public Integer numBillete;
    public Integer numDieta;
    public Integer numKm;
    public Integer numPension;
    public float otrosGastos;
    public float precioBillete;
    public float precioKm;
    public float precioDieta;    
    public float precioPension;
    public int idPeriodoLiquidacion;
    public String idMatricula;
    public float distanciaUnitaria;
    public Integer numDias;
    public String localidadCentroTrabajo;
	public Long idDistribucionPeriodo;


    public GastoAgrupado() {
    }

    public GastoAgrupado(int idGastoAlumno, int numBillete, int numDieta, int numKm, int numPension, float otrosGastos, float precioBillete, float precioKm, float precioDieta, float precioPension, int idPeriodoLiquidacion, String idMatricula,float distanciaUnitaria, int numDias, String localidadCentroTrabajo,Long idDistribucionPeriodo) {
        this.idGastoAlumno = idGastoAlumno;
        this.numBillete = numBillete;
        this.numDieta = numDieta;
        this.numKm = numKm;
        this.numPension = numPension;
        this.otrosGastos = otrosGastos;
        this.precioBillete = precioBillete;
		this.precioKm = precioKm;
        this.precioDieta = precioDieta;
        this.precioPension = precioPension;
        this.idPeriodoLiquidacion = idPeriodoLiquidacion;
        this.idMatricula = idMatricula;
        this.distanciaUnitaria = distanciaUnitaria;
        this.numDias = numDias;
        this.localidadCentroTrabajo = localidadCentroTrabajo;
		this.idDistribucionPeriodo = idDistribucionPeriodo;
    }

}

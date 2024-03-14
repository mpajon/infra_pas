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
@NamedNativeQuery(name ="ProfesorCentro.getListaProfesorByCentroByTipoPractica",
query =	"select distinct  prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1, prof.dl_apellido2 as apellido2, " +
		"ci.dc_nombre as ciclo, ci.dc_codigo as codCiclo, ci.ca_ciclo as idCiclo , prof.ca_profesor as idProfesor, " +
		"uni.nu_anio as curso, uni.ca_turno as regimen, uni.ca_nombre as grupo " + 
		"from profesor prof " +
		"inner join unidad uni on uni.ca_tutor = prof.ca_profesor or uni.ca_tutor_adicional = prof.ca_profesor " +
		"inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " + 
		"inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		"inner join centro cen on oc.ca_centro = cen.ca_centro inner " +
		"join ciclo ci on ci.ca_ciclo = of.ca_ciclo " + 
		"where uni.nu_anio = :curso and cen.ca_centro = :idCentro  " +
		"and ci.ca_ensenanza in (select distinct ca_ensenanza from tipopractica_ensenanzas te where te.cn_tipo_practica =  :idTipoPractica) " +
		"order by ciclo, nombre, apellido1, apellido2",
		 resultSetMapping = "ProfesorCentroMapping"),

@NamedNativeQuery(name ="ProfesorCentro.getListaProfesorByCentroByTipoPracticaByTutor",
query =	"select distinct  prof.dc_nombre as nombre, prof.dl_apellido1 as apellido1, prof.dl_apellido2 as apellido2, " +
		"ci.dc_nombre as ciclo, ci.dc_codigo as codCiclo, ci.ca_ciclo as idCiclo , prof.ca_profesor as idProfesor, " +
		"uni.nu_anio as curso, uni.ca_turno as regimen, uni.ca_nombre as grupo " +
		"from profesor prof " +
		"inner join unidad uni on uni.ca_tutor = prof.ca_profesor or uni.ca_tutor_adicional = prof.ca_profesor " +
		"inner join ofertas_centro oc on uni.ca_ofercen = oc.ca_ofercen " + 
		"inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa " + 
		"inner join centro cen on oc.ca_centro = cen.ca_centro inner " +
		"join ciclo ci on ci.ca_ciclo = of.ca_ciclo " + 
		"where uni.nu_anio = :curso and cen.ca_centro = :idCentro and ( uni.ca_tutor= :tutor  or uni.ca_tutor_adicional= :tutor) " +
		"and ci.ca_ensenanza in (select distinct ca_ensenanza from tipopractica_ensenanzas te where te.cn_tipo_practica =  :idTipoPractica) " +
		"order by ciclo, nombre, apellido1, apellido2",
		 resultSetMapping = "ProfesorCentroMapping")
})

@SqlResultSetMapping(name="ProfesorCentroMapping",
        classes = {
                @ConstructorResult(targetClass = ProfesorCentro.class,
                        columns = {@ColumnResult(name="nombre", type=String.class),
                        		@ColumnResult(name="apellido1", type=String.class),
                        		@ColumnResult(name="apellido2", type=String.class),
                        		@ColumnResult(name="ciclo", type=String.class),
                        		@ColumnResult(name="codCiclo", type=String.class),
                        		@ColumnResult(name="idCiclo", type=String.class),
                        		@ColumnResult(name="idProfesor", type=String.class),
                        		@ColumnResult(name="curso", type=Integer.class),
                        		@ColumnResult(name="regimen", type=String.class),
                        		@ColumnResult(name="grupo", type=String.class),}
                )}
)



public class ProfesorCentro implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	public String nombre;    
    public String apellido1;
    public String apellido2;
    public String ciclo;
    public String codCiclo;
    public String idCiclo;
    public String idProfesor;
    public Integer curso;
    public String regimen;
    public String grupo;
    

    public ProfesorCentro() {
    }

    public ProfesorCentro( String nombre, String apellido1, String apellido2, String ciclo, String codCiclo, String idCiclo, String idProfesor, Integer curso, String regimen, String grupo) {
    	this.nombre = nombre;
    	this.apellido1 = apellido1;
    	this.apellido2 = apellido2;
    	this.ciclo = ciclo;
    	this.codCiclo = codCiclo;
    	this.idCiclo = idCiclo;
    	this.idProfesor = idProfesor;
    	this.curso = curso;
    	this.regimen = regimen;
    	this.grupo = grupo;
    }
}

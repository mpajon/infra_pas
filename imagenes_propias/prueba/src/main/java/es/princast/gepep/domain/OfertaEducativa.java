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
@NamedNativeQuery(name ="OfertaEducativa.findByCentroAndAnio",
query ="  select distinct oc.ca_ofercen as  idOfertaCentro, of.ca_oferta_formativa as idOfertaFormativa, ciclo.ca_ensenanza as idEnsenanza, ciclo.ca_ciclo as idCiclo, ciclo.dc_codigo as codCiclo , " + 
		" of.dc_nombre as nombreOferta,of.dc_codigo as codigoOferta, of.fl_deSauce as deSauce" +
		" from ofertas_centro oc " +
		" inner join oferta_formativa of on oc.ca_oferta_formativa = of.ca_oferta_formativa "+
		" inner join ciclo ciclo on of.ca_ciclo = ciclo.ca_ciclo " +
		" inner join Centro centro on oc.ca_centro = centro.ca_centro " + 
		" where centro.ca_centro = :centro " +		 
		" and of.cn_anioinicio <= :anio and (of.cn_aniofin is null or of.cn_aniofin >= :anio) " +
		" and ciclo.fe_fbaja is null and oc.fl_vigente = true and of.fl_vigente = true " +
		" order by of.dc_nombre asc ",
		  resultSetMapping = "OfertaEducativaMapping")


 
})
@SqlResultSetMapping(name="OfertaEducativaMapping",
        classes = {
                @ConstructorResult(targetClass = OfertaEducativa.class,
                        columns = {@ColumnResult(name="idOfertaCentro", type=String.class), 
                        		@ColumnResult(name="idOfertaFormativa", type=String.class),
                        		@ColumnResult(name="idEnsenanza", type=String.class),
                        		@ColumnResult(name="idCiclo", type=String.class),
                        		@ColumnResult(name="codCiclo", type=String.class),
                        		@ColumnResult(name="nombreOferta", type=String.class),
                        		@ColumnResult(name="codigoOferta", type=String.class),                        		
                        		@ColumnResult(name="deSauce", type=Boolean.class)}
                )}
)



public class OfertaEducativa implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Id
    public String idOfertaCentro;
    public String idOfertaFormativa;
    public String idEnsenanza;
    public String idCiclo;
    public String codCiclo;
    public String nombreOferta;
    public String codigoOferta;  
    public Boolean deSauce;

    public OfertaEducativa() {
    }

    public OfertaEducativa(String idOfertaCentro, String idOfertaFormativa,String idEnsenanza, String idCiclo, String codCiclo, String nombreOferta, String codigoOferta, Boolean deSauce) {
        this.idOfertaCentro = idOfertaCentro;
        this.idOfertaFormativa = idOfertaFormativa;
        this.idEnsenanza = idEnsenanza;
        this.idCiclo = idCiclo;        
        this.codCiclo = codCiclo;
        this.nombreOferta = nombreOferta;
        this.codigoOferta = codigoOferta;
        this.deSauce = deSauce;
    }
}

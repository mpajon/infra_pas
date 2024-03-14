package es.princast.gepep.web.rest.filter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Convenio.
 */

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Data
public class ConvenioFilter {

	private String codigo;
	private String fechaConvenio;
	private String idCentro;
	private String codigoCentro;
	private String nombreCentro;
	private String nombreEmpresa;
	private String cifEmpresa;
	private Boolean vigente;
	private Boolean antiguo;
	private Long idPractica;
	private Boolean validado;
	private String fechaDesde;
	private String fechaHasta;
}

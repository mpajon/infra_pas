package es.princast.gepep.web.rest.filter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Filtro Unidad.
 */

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Data
public class UnidadFilter {

	private String nombre;
	private String nombreOferta;
	private String nombreCentro;
	private Integer anio;
	private String idCentro;
}

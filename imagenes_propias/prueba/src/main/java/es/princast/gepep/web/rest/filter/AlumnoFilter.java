package es.princast.gepep.web.rest.filter;

import es.princast.gepep.domain.Alumno;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Filtro de alumno. Contiene los campos de la entidad alumno mas campos de filtro adicionales.
 *
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class AlumnoFilter extends Alumno {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 8703480635707376709L;
	
	private String idCentro;
	private Integer anioMatricula;
	
}

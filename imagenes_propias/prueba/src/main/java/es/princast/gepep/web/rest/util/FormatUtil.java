package es.princast.gepep.web.rest.util;

public class FormatUtil {
	private static final String DESAUCE = "S";
    

    /**
	 * @param alumnoDTO
	 */
	public static String convertIdSauceToGepep(Integer id) {
		return DESAUCE.concat(id.toString());
	}


	public static String convertIdSauceToGepep(String id) {
		return DESAUCE.concat(id);
	}
	

	public static String convertIdCicloSauceToGepep(String idCicloSauce, String idEnsenanzaSauce) {
		return DESAUCE.concat(idCicloSauce).concat(DESAUCE).concat(idEnsenanzaSauce);
	}

}

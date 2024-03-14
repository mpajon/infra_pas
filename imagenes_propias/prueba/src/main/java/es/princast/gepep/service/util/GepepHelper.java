package es.princast.gepep.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import es.princast.gepep.domain.Alumno;
import es.princast.gepep.domain.Profesor;

//TODO: Arreglar un poco esto...
@Component
public class GepepHelper {


    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss");

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat(
            "dd/MM/yyyy");


    public static String tipoIdentificador(String acreditacion) {
        if (acreditacion.startsWith("x") || acreditacion.startsWith("X")
                || acreditacion.startsWith("y") || acreditacion.startsWith("Y")
                || acreditacion.startsWith("z") || acreditacion.startsWith("Z")) {
            return Constantes.LETRANIE;
        } else {
            return Constantes.LETRANIF;
        }
    }

    /**
     * Devuelve el nif que se le pasa como parametro con un tamaño igual al que
     * se le indica.En caso de que sea un NIE, sólo se hará la correspondiente
     * transofrmación a mayúsculas
     * <p>
     * Si el nif tiene un tamaño menor se rellena con ceros, si fuese mayor se
     * quitan los primeros caracteres.
     *
     * @param idFiscal idFiscal que se quiere tratar
     * @param tamano   tamaño en numero de caracteres que se desea
     * @return el nif con un tamaño igual al indicado
     */
    public static final String formatIdFiscal(String idFiscal, int tamano) {
        /* MPA - 25/03/2008 - Se añade para que rellene ceros por la izquierda
         * hasta llegar a la longitud pedida en el caso de que sea NIE
         * Ademas se reordena el codigo.
         */
        String documento = idFiscal;

        //comprobamos que no este vacio
        if (!idFiscal.equals(Constantes.CADENA_VACIA)) {

            /* Comprobamos si ya está correcto.
             * Si ya está correcto lo deciudadanolvemos, si no lo tratamos
             */
            if (idFiscal.length() < tamano) {

                // Si es un NIF
                if ((tipoDocumento(idFiscal).equals(Constantes.NIF))) {

                    for (int i = idFiscal.length(); i < tamano; i++) {
                        documento = "0" + documento;
                    }
                }

                // Si es un NIE
                if ((tipoDocumento(idFiscal).equals(Constantes.NIE))) {
                    /* MPA - JCC - 11/08/2010 - Cambios de modernización */
                    tamano = 9;
                    /* MPA - JCC - 19/04/2010 - Se corrige los calculos del tipo de identificacion */
                    String letra = documento.substring(0, 1);
                    /* le quitamos la X del principio para añadir los ceros a la izquierda*/
                    documento = documento.substring(1);
                    /* FIN MPA - JCC - 11/08/2010 */
                    /* le añadimos los ceros a la izquierda hasta que sean los que se piden -1 */
                    tamano = tamano - 1;
                    for (int i = documento.length(); i < tamano; i++) {
                        documento = ("0" + documento);
                    }

                    /* le añadimos la letra correspondiente y queda el string con el tamaño pedido */
                    documento = (letra + documento);
                    /* FIN MPA - JCC - 19/04/2010 */

                }

            } else {
                int substring = idFiscal.length() - tamano;
                documento = idFiscal.substring(substring);
            }
            documento = documento.toUpperCase();
        }
        return documento;
 
    }

    public static String tipoDocumento(String acreditacion) {
        if (acreditacion.startsWith("x") || acreditacion.startsWith("X")
                || acreditacion.startsWith("y") || acreditacion.startsWith("Y")
                || acreditacion.startsWith("z") || acreditacion.startsWith("Z")) {
            return Constantes.NIE;
        } else {
            return Constantes.NIF;
        }
    }


    /**
     * @param fecha1 de tipo Date
     * @param fecha2 de tipo Date
     * @return true si las dos fechas corresponden al mismo dia, false en caso
     * contrario.
     */
    public static boolean mismoDia(Date fecha1, Date fecha2) {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(fecha1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(fecha2);

        return ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) && (c1
                .get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)));
    }


    /**
     * Comprueba si un ciuddadano es menor de edad
     *
     * @param fechaNacimiento
     * @param fecha
     * @return true si el ciudadano es menor
     * @throws BusinessException
     */
    public static boolean isMenor(Date fechaNacimiento, Date fecha) throws URISyntaxException {

        if (fechaNacimiento != null && fecha != null) {
            // Calendar cal = Calendar.getInstance();
            // cal.setTime(fsol);
            long _18YearsMilis = 18L * 365L * 24L * 60L * 60L * 1000L;
            long mayor = fechaNacimiento.getTime() + _18YearsMilis;
            Date dMayor = new Date(mayor);
            return (fecha.before(dMayor));
        }
      return false;

    }



    private static boolean iguales(Integer a, Integer b) {
        if (a == null && b != null)
            return false;
        if (a != null && b == null)
            return false;
        if (a == null && b == null)
            return true;
        return a.intValue() == b.intValue();
    }

    private static boolean igualesIncluyendoVacios(String a, String b) {
        //primero comprobamos si los dos son null
        if (a == null && b == null){
            return true;
        }else{
            /*comprobamos si la igualdad es null y vacio... si no.. es que son diferentes.
             * tenemos que comprobar antes si es distinto de null para que no nos de un NULL POINTER
             * al hacer el equals
             */
            if(a != null && b == null){
                if(a.equals("")){
                    //a es vavio y b es null... son iguales
                    return true;
                }
                else{
                    //a es un valor y b es null... son diferentes
                    return false;
                }
            }
            if(a == null && b != null){
                if(b.equals("")){
                    //b es vavio y a es null... son iguales
                    return true;
                }
                else{
                    //b es un valor y a es null... son diferentes
                    return false;
                }
            }
        }
        // si llegamos aqui es que a y b tienen un valor... los comparamos
        return (a.equalsIgnoreCase(b));
    }

    /**
     * Redondea a numDecimales el float valor
     * @param valor
     * @param numDecimales
     * @return
     */
	public static float redondearDecimalesFloat (float valor, int numDecimales) {
		 BigDecimal bd = new BigDecimal(Float.toString(valor));
		 bd = bd.setScale(numDecimales, RoundingMode.HALF_UP);
		 return bd.floatValue();
	}

	/**
	 * Ofusca datos de un alumno
	 * @param alumno
	 */
	public static void ofuscarAlumno(Alumno alumno) {

		alumno.setNombre(ofuscarTexto(alumno.getNombre(), alumno.getNumEscolar()));
		alumno.setApellido1(ofuscarTexto(alumno.getApellido1(), alumno.getNumEscolar()));
		alumno.setApellido2(ofuscarTexto(alumno.getApellido2(), alumno.getNumEscolar()));
		alumno.setLocalidad(ofuscarTexto(alumno.getLocalidad(), alumno.getNumEscolar()));
		alumno.setCalle(ofuscarTexto(alumno.getCalle(), alumno.getNumEscolar()));
		alumno.setTelefono(ofuscarTelefono(alumno.getTelefono()));
		alumno.setEmail(ofuscarEmail(alumno.getNumEscolar()));
		alumno.setMovil(ofuscarTelefono(alumno.getMovil()));
		alumno.setLetra("0");
		alumno.setNumero("0");
		alumno.setPiso("0");
	}

	/**
	 * Ofusca datos de un profesor
	 * @param profesor
	 */
	public static void ofuscarProfesor(Profesor profesor) {
		
		profesor.setNombre(ofuscarTexto(profesor.getNombre(), profesor.getIdProfesor()));
		profesor.setApellido1(ofuscarTexto(profesor.getApellido1(), profesor.getIdProfesor()));
		profesor.setApellido2(ofuscarTexto(profesor.getApellido2(), profesor.getIdProfesor()));
		profesor.setLocalidad(ofuscarTexto(profesor.getLocalidad(), profesor.getIdProfesor()));
		profesor.setDireccion(ofuscarTexto(profesor.getDireccion(), profesor.getIdProfesor()));
		profesor.setTelefono(ofuscarTelefono(profesor.getTelefono()));
		profesor.setEmail(ofuscarEmail(profesor.getIdProfesor()));
		profesor.setMovil(ofuscarTelefono(profesor.getMovil()));
		
	}


	/**
	 * Ofusca una cadena cogiendo los 3 primeros caracteres y concatenándole una segunda parte
	 * @param texto
	 * @param parte2
	 * @return
	 */
	private static String ofuscarTexto(String texto, String parte2) {
		if ( texto!=null && !texto.isEmpty() && texto.length()>2 ) {
			return texto!= null ? texto.substring(0, 3).concat("-").concat(parte2) : null;
			}		
			return  texto!= null ? texto.concat("-").concat(parte2) : null;
		}

	/**
	 * Ofusca un email sustituyéndolo por una cadena
	 * @param texto
	 * @return
	 */
	private static String ofuscarEmail(String texto) {
		return texto!= null ? "email-".concat(texto).concat("@gmail.com") : null;
	}

	/**
	 * Ofusca un teléfono reemplazando cada número por un 0
	 * @param texto
	 * @return
	 */
	private static String ofuscarTelefono(String texto) {
		return texto!= null ? IntStream.range(0, texto.length()).mapToObj(i -> "0").collect(Collectors.joining()) : null;
	}


}

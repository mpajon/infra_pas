package es.princast.gepep.service.util;

import java.time.format.DateTimeFormatter;

public final class Constantes {


    public static final String NIE = "NIE";

    public static String NIF = "NIF";

	//MNT Nuevas constantes
	public static final String LETRANIE = "E";
	public static final String LETRANIF = "N";
	
   public static String FECHA_DBP = "yyyy_MM_dd";

    public static final int ERROR_CODE = 0;

    //MNT 
    //public static final int EXITO_CODE = 0;
    public static final int EXITO_CODE = 1;
    
    public static final String CADENA_VACIA = "";
    
    public static final String PUNTO = ".";

    public static final String COMA = ",";
    
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  

    //Constantes para la generación excel de los detalles del proceso de sincronización
    public static final String TAREA_LOG_SHEET = "Logs Sincro.";
    
    public static final String TAREA_LOG_ID = "ID LOG";
    public static final String TAREA_LOG_ID_TAREA = "ID TAREA";
    public static final String TAREA_LOG_TAREA = "TAREA";
    public static final String TAREA_LOG_SUBTAREA = "SUBTAREA";
    public static final String TAREA_LOG_MENSAJE = "MENSAJE";
    public static final String TAREA_LOG_ERROR = "ERROR";
    public static final String TAREA_LOG_EXCEPCION = "EXCEPCIÓN";
    public static final String TAREA_LOG_TRAZA = "TRAZA";
    public static final String TAREA_LOG_FECHA_CREACION = "FECHA CREACIÓN";
    

}
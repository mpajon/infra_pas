package es.princast.gepep.service.saucesincro;

/**
 * Clase de la que heredan los servicios de la sincronizacion con Sauce.
 * Recoge informacion de registros creados/actualizados/procesados.
 *
 */
public class SauceSincroAuditoriaServices {

    private long procesados;
    private long insertados;
    private long actualizados;

    
    public void resetAuditoria() {
	this.procesados = 0;
	this.insertados = 0;
	this.actualizados = 0;
    }
    
    public void incrementarInsertados() {
	insertados++;
    }
    
    public void incrementarActualizados () {
	actualizados++;
    }
    
    public void incrementarProcesados() {
	procesados++;
    }
    
    public String resultadosAuditoria() {
	return " [procesados=" + procesados + ", insertados=" + insertados + ", actualizados=" + actualizados + "]";
    }

}

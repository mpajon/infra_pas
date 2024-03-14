package es.princast.gepep.domain.reports;

import es.princast.gepep.domain.GastoAlumno;
import lombok.*;


@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class GastosAlumnosReport {

    private String nombreAlumno,apellido1Alumno,apellido2Alumno;
    private String nombreFormateado;
    private String nombreCentroTrabajo;
    private GastoAlumno gastoAlumno;
    private Float totalGastos;

//    private Integer numBilletes, numDietas,numDías;
//    private Float precioBillete, precioDieta, precioPension;
//    private Float totalTransportePublico, totalTransportePrivado, totalDietas, totalOtros,totalGastos;

}

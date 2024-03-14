package es.princast.gepep.domain.reports;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.Profesor;
import lombok.*;


@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class GastosProfesorReport {

    private String nombreProfesor,apellido1Profesor,apellido2Profesor;
    private String nombreCiclo,idCiclo,codigoCiclo;
    private Float totalTransportePublico, totalTransportePrivado, totalDietas, totalOtros,totalGastos;

}

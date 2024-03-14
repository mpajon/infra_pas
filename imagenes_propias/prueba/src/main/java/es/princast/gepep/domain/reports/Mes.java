package es.princast.gepep.domain.reports;

import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Mes {
    String nombre;
    @Builder.Default
    Boolean[] diasLectivos = new Boolean[32]; // para no usar el índice 0
    float totalHoras;

    public Mes(){
        for (int i=1;i<=31;i++){
            diasLectivos[i] = true;
        }
    }
}

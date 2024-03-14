package es.princast.gepep.domain.reports;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class FichaIndividualProg {

    @Builder.Default private String resultadoAprendizaje = "";
    @Builder.Default private String realizaciones= "";
    @Builder.Default private String criteriosEvaluacion= "";
    
    
    @Builder.Default private String textoEF1= "";
    @Builder.Default private String textoEF2= "";
    @Builder.Default private String textoEF3= "";
    @Builder.Default private String textoEF4= "";
    

}

package es.princast.gepep.domain.reports;

import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Calendario {

    Curso curso1;
    Curso curso2;

}


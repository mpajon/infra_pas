package es.princast.gepep.service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MunicipioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

    private String name;
}

package es.princast.gepep.service.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class InformeExcelDto {

    /**
     * Nombre de la hoja excel
     */
    private String sheetName;

    /**
     * Header columnas excel
     */
    private String[] header;

    /**
     * Datos del informe
     */
    @Builder.Default
    private List<String[]> data = new ArrayList<String[]>();


}



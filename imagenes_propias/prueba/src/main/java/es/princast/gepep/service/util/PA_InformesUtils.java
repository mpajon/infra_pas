package es.princast.gepep.service.util;

import java.io.ByteArrayOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import es.princast.gepep.service.dto.InformeExcelDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PA_InformesUtils {

	/**
	 * Método para la exportación de datos a excel
	 *
	 * @param informeExcelDto
	 * @return
	 */
	public static byte[] getFicheroExcel(InformeExcelDto informeExcelDto) {

		try {

			// Creamos el libro de trabajo de Excel
			Workbook workbook = new XSSFWorkbook();

			// Creamos la hoja
			Sheet pagina = workbook.createSheet(informeExcelDto.getSheetName());

			// Establecemos estilos
			CellStyle styleHeader = getCellStyleHeader(workbook);
			CellStyle csRows = getCellStyleRows(workbook);

			String[] titulos = informeExcelDto.getHeader();

			// Creamos una fila en la hoja en la posicion 0 para la cabecera
			Row fila = pagina.createRow(0);

			// Creamos el encabezado
			for (int i = 0; i < titulos.length; i++) {

				// Creamos celda por cada elemento de la cabecera y aplicamos estilo
				Cell celda = fila.createCell(i);
				celda.setCellStyle(styleHeader);
				celda.setCellValue(titulos[i]);
				pagina.autoSizeColumn(i);
			}

			// Recorremos los datos y vamos creando las filas.
			int rowIndex = 1;
			for (String[] data : informeExcelDto.getData()) {
				fila = pagina.createRow(rowIndex);
				rowIndex++;

				// Insertamos los datos de la fila
				for (int i = 0; i < data.length; i++) {
					Cell celda = fila.createCell(i);
					celda.setCellValue(data[i]);
					celda.setCellStyle(csRows);
				}
			}

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			workbook.close();

			return outByteStream.toByteArray();

		} catch (Exception ex) {
			log.error("Error generando exportación excel " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Método para establecer formato excel de la cabecera
	 *
	 * @param workbook
	 * @return
	 */
	private static CellStyle getCellStyleHeader(Workbook workbook) {

		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return style;
	}

	/**
	 * Método para establecer formato excel de la cabecera
	 *
	 * @param workbook
	 * @return
	 */
	private static CellStyle getCellStyleRows(Workbook workbook) {

		CellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		return style;
	}

}

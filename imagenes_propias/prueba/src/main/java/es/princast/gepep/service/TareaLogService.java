package es.princast.gepep.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Period;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.TareaLog;
import es.princast.gepep.repository.TareaLogRepository;
import es.princast.gepep.service.dto.InformeExcelDto;
import es.princast.gepep.service.util.Constantes;
import es.princast.gepep.service.util.PA_InformesUtils;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TareaLogService {

	private static final String ENTITY_NAME = "tareaLog";

	@Autowired
	private TareaLogRepository errorLogRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EntityManager entityManager;

	public TareaLog createTareaLog(TareaLog tareaLog) {
		log.debug("SERVICE request to save ErrorLog : {}", tareaLog);
		if (tareaLog.getIdTareaLog() != null) {
			throw new BadRequestAlertException("A new tareaLog cannot already have an ID", ENTITY_NAME, "idexists");
		} else {
			BigInteger nextId = (BigInteger) entityManager.createNativeQuery("select NEXTVAL('public.sec_tarea_log')")
					.getSingleResult();
			tareaLog.setIdTareaLog(nextId);
		}

		TareaLog result = errorLogRepository.save(tareaLog);
		return result;
	}

	public BigInteger getTareaId() {
		BigInteger nextId = (BigInteger) entityManager.createNativeQuery("select NEXTVAL('public.sec_tarea')")
				.getSingleResult();
		return nextId;
	}

	/**
	 * Elimina los registros que tengan mas de 30 dias de antiguedad
	 */
	public void limpiarLogMes() {

		errorLogRepository.deleteTareaLogToDate(Instant.now().minus(Period.ofDays(30)));
	}

	public List<TareaLog> getAllTareaLogs() {

		log.debug("SERVICE getAllTareaLogs");

		String[] propiedades = { "createdDate" };
		Sort sort = Sort.by(Sort.Direction.DESC, propiedades);
		return errorLogRepository.findAll(sort);
	}

	/**
	 * Exportación datos a Excel
	 *
	 * @return bytes contenido excel
	 */
	public byte[] generateExportExcel() throws Exception {

		InformeExcelDto excelDTO = new InformeExcelDto().toBuilder().sheetName(Constantes.TAREA_LOG_SHEET)
				.header(new String[] { Constantes.TAREA_LOG_ID, Constantes.TAREA_LOG_ID_TAREA,
						Constantes.TAREA_LOG_TAREA, Constantes.TAREA_LOG_SUBTAREA, Constantes.TAREA_LOG_MENSAJE,
						Constantes.TAREA_LOG_ERROR, Constantes.TAREA_LOG_EXCEPCION, Constantes.TAREA_LOG_TRAZA,
						Constantes.TAREA_LOG_FECHA_CREACION })
				.build();

		List<TareaLog> listEnt = this.getAllTareaLogs();
		
		Date myDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String formattedDate = "";

		for (TareaLog tareaLog : listEnt) {

			myDate = Date.from(tareaLog.getCreatedDate());

			formattedDate = formatter.format(myDate);

			excelDTO.getData()
					.add(new String[] { tareaLog.getIdTareaLog().toString(),
							tareaLog.getIdTarea() == null ? "" : tareaLog.getIdTarea().toString(), tareaLog.getTarea(),
							tareaLog.getSubtarea(), tareaLog.getMensaje(), tareaLog.getError(), tareaLog.getException(),
							tareaLog.getTraza(), formattedDate });
		}

		byte[] data = PA_InformesUtils.getFicheroExcel(excelDTO);

		if (data == null) {
			throw new Exception("Se ha producido un error en la generación del informe");
		}
		return data;

	}

}

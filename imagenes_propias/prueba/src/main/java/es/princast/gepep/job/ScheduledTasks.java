



package es.princast.gepep.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.princast.gepep.service.saucesincro.SauceSincroService;

@Component
public class ScheduledTasks {
	
	@Autowired
	private SauceSincroService sincroService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Scheduled(cron="${application.plaintsis.instructionSchedularTime}")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        log.info("Iniciamos las sincronización");
        try {
			sincroService.sincronizar("all",null, null);
		} catch (Exception e) {
			log.error("Error al sincronizar con Sauce");
			e.printStackTrace();
		}
        log.info("Sincronización finalizada a las {}", dateFormat.format(new Date()));
    }
}
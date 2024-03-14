package es.princast.gepep.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.TareaLog;


/**
 * Spring Data JPA repository for the Centro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TareaLogRepository extends JpaRepository<TareaLog, String> {
	
    @Modifying
    @Query("delete from TareaLog tareaLog where tareaLog.createdDate < :toDate")
    void deleteTareaLogToDate( @Param("toDate") Instant toDate);
}

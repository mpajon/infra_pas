package es.princast.gepep.repository;


import es.princast.gepep.domain.HorasPeriodo;
import es.princast.gepep.domain.PeriodoPractica;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HorasPeriodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HorasPeriodoRepository extends JpaRepository<HorasPeriodo, Long> {
	

}

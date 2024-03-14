package es.princast.gepep.repository;


import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.ImportesTipoGasto2;
import es.princast.gepep.domain.ResultadoAprendizaje;
import es.princast.gepep.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the GastoAlumno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImportesTipoGasto2Repository extends JpaRepository<ImportesTipoGasto2, Long> {


	@Query("SELECT importe FROM ImportesTipoGasto2 importe " +
			" WHERE  :valor between importe.nKmDesde AND importe.nKmHasta "
	)
	Optional <ImportesTipoGasto2> getImporteGasto2(@Param("valor") Integer valor);

	@Query("SELECT max(importe) FROM ImportesTipoGasto2 importe ")
	Optional <ImportesTipoGasto2> getImporteMaxValor();
}

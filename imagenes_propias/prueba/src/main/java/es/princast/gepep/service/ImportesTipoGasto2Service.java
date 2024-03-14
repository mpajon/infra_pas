package es.princast.gepep.service;


import es.princast.gepep.domain.ImportesTipoGasto2;
import es.princast.gepep.domain.Usuario;
import es.princast.gepep.repository.ImportesTipoGasto2Repository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ImportesTipoGasto2Service {

	private static final String ENTITY_NAME = "importesTipoGasto";

	@Autowired
	private ImportesTipoGasto2Repository importesTipoGasto2Repository;

	public ImportesTipoGasto2 getImportesTipoGasto2(final Long idImporteTipoGasto2) {
		Optional<ImportesTipoGasto2> importesTipoGasto2 = importesTipoGasto2Repository.findById(idImporteTipoGasto2);
		if (!importesTipoGasto2.isPresent()) {
			//throw new IllegalArgumentException("No existe un idImporteTipoGasto2 con ese identificador.");
			if (idImporteTipoGasto2.toString() != "")
			{

			}
		}
		return importesTipoGasto2.get();
	}

	public Integer getImporteByKilometros(final Integer valor) {
		//Optional<ImportesTipoGasto2> importesTipoGasto2 = importesTipoGasto2Repository.findBynKmDesdeLowerThanAndnKmHastaGreatherThan(valor);
		Optional<ImportesTipoGasto2> importesTipoGasto2 = importesTipoGasto2Repository.getImporteGasto2(valor);
		if (!importesTipoGasto2.isPresent()) {
			//throw new IllegalArgumentException("No existe un idImporteTipoGasto2 con ese identificador.");
			importesTipoGasto2 = importesTipoGasto2Repository.getImporteMaxValor();
		}
		return importesTipoGasto2.get().getNGastoTotal();
	}
	public List<ImportesTipoGasto2> getAllImportesTipoGasto2() {
		return importesTipoGasto2Repository.findAll();
	}





}

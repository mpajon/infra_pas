package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Actividad;
import es.princast.gepep.domain.Sector;
import es.princast.gepep.repository.ActividadRepository;
import es.princast.gepep.repository.SectorRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SectorService {

	private static final String ENTITY_NAME = "sector";

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private ActividadRepository actividadRepository;

	@Autowired
	private MessageSource messageSource;

	public Sector getSector(final Long idSector) {
		log.debug("SERVICE request to getSector ");
		Optional<Sector> sector = sectorRepository.findById(idSector);
		if (!sector.isPresent()) {
			throw new IllegalArgumentException("No existe un sector con ese identificador.");
		}
		return sector.get();
	}

	public List<Sector> getAllSectores() {
		String[] propiedades = { "codigo", "nombre" };
		Sort sort = Sort.by(Sort.Direction.ASC, propiedades);
		return sectorRepository.findAll(sort);
	}

	public ResponseEntity<Sector> updateSector(final Sector sectorModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Sector : {}", sectorModificado);
		if (sectorModificado.getIdSector() == null) {
			return createSector(sectorModificado);
		}
		
		List<Sector> sectorDuplicadoCodigo = this.sectorRepository.findAllByCodigo(sectorModificado.getCodigo());
		List<Sector> sectorDuplicadoNombre = this.sectorRepository.findAllByNombre(sectorModificado.getNombre());

		if (sectorDuplicadoCodigo.size() > 0) {
			if (!(sectorDuplicadoCodigo.size() == 1
					&& sectorDuplicadoCodigo.get(0).getIdSector().equals(sectorModificado.getIdSector()))) {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.sector.existe.codigo", null, LocaleContextHolder.getLocale()));
			}
		} 

		if (sectorDuplicadoNombre.size() > 0) {
			if (!(sectorDuplicadoNombre.size() == 1
					&& sectorDuplicadoNombre.get(0).getIdSector().equals(sectorModificado.getIdSector()))) {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.sector.existe.nombre", null, LocaleContextHolder.getLocale()));
			}
		}
		
		Sector result = sectorRepository.save(sectorModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sectorModificado.getIdSector().toString()))
				.body(result);
	}

	public void deleteSector(final Long idSector) {
		Optional<Sector> sector = sectorRepository.findById(idSector);
		if (!sector.isPresent()) {
			throw new IllegalArgumentException("No existe un sector con ese identificador.");
		}

		List<Actividad> listaActividades = (List<Actividad>) actividadRepository.findAllBySector(sector.get());
		if (listaActividades.size() > 0) {
			// No se puede borrar porque está referencenciado en actividad
			throw new IllegalArgumentException(messageSource.getMessage("error.sector.referenciado.actividad", null,
					LocaleContextHolder.getLocale()));
		}

		sectorRepository.deleteById(idSector);
	}

	public ResponseEntity<Sector> createSector(final Sector nuevoSector) throws URISyntaxException {
		log.debug("SERVICE request to save Sector : {}", nuevoSector);
		if (nuevoSector.getIdSector() != null) {
			throw new BadRequestAlertException("A new Sector cannot already have an ID", ENTITY_NAME, "idexists");
		}

		List<Sector> sectorDuplicadoCodigo = this.sectorRepository.findAllByCodigo(nuevoSector.getCodigo());
		List<Sector> sectorDuplicadoNombre = this.sectorRepository.findAllByNombre(nuevoSector.getNombre());

		if (sectorDuplicadoCodigo.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.sector.existe.codigo", null, LocaleContextHolder.getLocale()));
		} else if (sectorDuplicadoNombre.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.sector.existe.nombre", null, LocaleContextHolder.getLocale()));
		}

		Sector result = sectorRepository.save(nuevoSector);
		return ResponseEntity.created(new URI("/api/sectores/" + result.getIdSector()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdSector().toString()))
				.body(result);
	}

}

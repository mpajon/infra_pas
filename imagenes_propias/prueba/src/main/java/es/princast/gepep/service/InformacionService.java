package es.princast.gepep.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Informacion;
import es.princast.gepep.repository.InformacionRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class InformacionService {

	private static final String ENTITY_NAME = "informacion";

	@Autowired
	private InformacionRepository informacionRepository;
	
	@Autowired
	private FileStorageService fileStorageService;


	@Autowired
	private MessageSource messageSource;

	
	
	
	
	public ResponseEntity<Informacion> createInformacion(final Informacion nuevaInformacion) throws URISyntaxException, IOException {
		log.debug("SERVICE request to save Informacion : {}", nuevaInformacion);

		if (nuevaInformacion.getIdInformacion() != null) {
			throw new BadRequestAlertException("A new informacion cannot already have an ID", ENTITY_NAME, "idexists");
		}

		 List <Informacion> informacionDuplicada  = this.informacionRepository.findAllByNombreAndIdTipoPractica(nuevaInformacion.getNombre(), nuevaInformacion.getTipoPractica().getIdTipoPractica());
	 	 if (informacionDuplicada.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.informacion.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
	 	 
		Informacion result = informacionRepository.save(nuevaInformacion);
		return ResponseEntity.created(new URI("/api/informaciones/" + result.getIdInformacion()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdInformacion().toString()))
				.body(result);
	}

	public Informacion getInformacion(final Long idInformacion) {
		Optional<Informacion> informacion = informacionRepository.findById(idInformacion);
		if (!informacion.isPresent()) {
			throw new IllegalArgumentException("No existe una informacion con ese identificador.");
		}
		return informacion.get();
	}

	public List<Informacion> getAllInformaciones() {
		return informacionRepository.findAllByFechaBajaIsNull();
	}

	public ResponseEntity<Informacion> updateInformacion(final Informacion informacionModificada) throws URISyntaxException, IOException {
		log.debug("SERVICE request to update Informacion : {}", informacionModificada);
		if (informacionModificada.getIdInformacion() == null) {
			return createInformacion(informacionModificada);
		}

		 List <Informacion> informacionDuplicada  = this.informacionRepository.findAllByNombreAndIdTipoPractica(informacionModificada.getNombre(),informacionModificada.getTipoPractica().getIdTipoPractica());
	 	 if (informacionDuplicada.size()>0)
	 	 {
			if(informacionDuplicada.size()==1 && informacionDuplicada.get(0).getIdInformacion() == informacionModificada.getIdInformacion()) {
				
				
				//Informacion infoExistente = informacionDuplicada.get(0);
				
//				// Si ha cambiado el fichero asoiado tenemos que borrar el anterior //
//				//if(informacionModificada.isUpdatedFile()) {
//					//if(! infoExistente.getTipoPractica().getIdTipoPractica().equals(informacionModificada.getTipoPractica().getIdTipoPractica())) {
//				//	fileStorageService.deleteFile(infoExistente.getFolderInformacionTipoPractica(), infoExistente.getNombre());
//					//Resource resource = fileStorageService.loadFileAsResource(infoExistente.getNombre(), infoExistente.getFolderInformacionTipoPractica());
//					//MultipartFile multipartFile = new MockMultipartFile(infoExistente.getNombre(), IOUtils.toByteArray(resource.getInputStream()));
//					//fileStorageService.storeFile(multipartFile, informacionModificada.getFolderInformacionTipoPractica());
//				}
				
				Informacion result = informacionRepository.save(informacionModificada);
				return ResponseEntity.ok().headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, informacionModificada.getIdInformacion().toString()))
						.body(result);
			}
			else {
			throw new IllegalArgumentException(messageSource.getMessage("error.informacion.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
			}
			
			
			
	 	 }
	 	 
	 	Informacion result = informacionRepository.save(informacionModificada);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, informacionModificada.getIdInformacion().toString()))
				.body(result);

	}

	public void deleteInformacion(final Long idInformacion) {
		Optional<Informacion> informacion = informacionRepository.findById(idInformacion);
		if (!informacion.isPresent()) {
			throw new IllegalArgumentException("No existe una informacion con ese identificador.");
		}
		informacionRepository.delete(informacion.get());
		String folderInformacionTipoPractica = informacion.get().getFolderInformacionTipoPractica();
		fileStorageService.deleteFile(folderInformacionTipoPractica, informacion.get().getNombre());
		
	}

	
}

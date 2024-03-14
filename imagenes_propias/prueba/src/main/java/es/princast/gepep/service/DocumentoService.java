package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import es.princast.gepep.domain.Documento;
import es.princast.gepep.domain.TextosDocumento;
import es.princast.gepep.repository.DocumentoRepository;
import es.princast.gepep.repository.TextosDocumentoRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DocumentoService {

	private static final String ENTITY_NAME = "documento";

	@Autowired
	private DocumentoRepository documentoRepository;

	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;
	
	
	@Autowired
	private TextosDocumentoRepository textosDocumentoRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private EntityManager entityManager; 

	public Documento getDocumento(final Long idDocumento) {
		Optional<Documento> documento = documentoRepository.findById(idDocumento);
		if (!documento.isPresent()) {
			throw new IllegalArgumentException("No existe un Documento con ese identificador.");
		}
		return documento.get();
	}
	
	 private Sort sortByIdAsc() {
		 	Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		 	return Sort.by(order);
	    }
	 
	/**
	 * Obtiene todos los documentos pero sin todas sus relaciones con una consulta nativa para mejora rendimiento.
	 * @return
	 */
	public List<Documento> getAllDocumentosList() {
		String sql =  " SELECT  "
				// Datos de documento //
				+ " d.cn_documento as idDocumento, d.dc_nombre as nombre , d.dl_logo1 as logo1, d.dl_logo2 as logo2, d.dl_encabezado as encabezado, d.dl_encabezado2 as encabezado2, "
				+ " d.te_encabezado_clausulas as encabezadoClausulas, d.te_encabezado_firma as encabezadoFirma, d.fl_textos as tieneTextos, d.fl_clausula as tieneClausula, d.fl_firma_centro as firmaCentro, "
				+ " d.fl_firma_empresa as firmaEmpresa, d.fl_firma_tutor as firmaTutor, "
				// Datos tipo Documento //
				+ " tdoc.cn_tipo_documento as idTipoDocumento, tdoc.ca_codigo as codigoTipoDocumento, tdoc.dl_descripcion as descripcionTipoDocumento , tdoc.fl_activo as activo, "
				// Datos tipo practica //
				+ " tp.cn_tipo_practica as idTipoPractica, tp.dc_nombre as nombreTipoPractica, tp.dl_descripcion as descripcionTipoPractica "
 	    		+ " FROM "
 	    		+ " documento d inner join tipo_documento tdoc on tdoc.cn_tipo_documento = d.cn_tipo_documento "
 	    		+ " inner join tipo_practica tp on tp.cn_tipo_practica = d.cn_tipo_practica "
 	    		+ " order by d.dc_nombre, tp.dc_nombre  ASC";
				
		return entityManager.createNativeQuery(sql, "DocumentoListMapping")
				.getResultList();
		}

	/**
	 * Obtiene todos los documentos con su infomracion completa recuperada a traves de jpa.
	 * @return
	 */
	public List<Documento> getAllDocumentos() {
		return documentoRepository.findAll(sortByIdAsc());
	}

	public ResponseEntity<Documento> updateDocumento(final Documento documentoModificado) throws URISyntaxException {
		log.debug("SERVICE request to update periodoPractica : {}", documentoModificado);
		if (documentoModificado.getIdDocumento() == null) {
			return createDocumento(documentoModificado);
		}
		
		 List <Documento> documentoDuplicado  = this.documentoRepository.findAllByNombreAndTipoPractica(documentoModificado.getNombre(),documentoModificado.getTipoPractica());
	 	
		 if (documentoDuplicado.size()>0)
	 	 {
			if(documentoDuplicado.size()==1 && documentoDuplicado.get(0).getIdDocumento().equals(documentoModificado.getIdDocumento())) {
				Documento result = documentoRepository.save(documentoModificado);	
				if (!documentoModificado.getTieneTextos()) { 
					TextosDocumento textoOriginal = textosDocumentoRepository.findByDocumento(documentoModificado);
					
					if (textoOriginal != null)
						textosDocumentoRepository.delete(textoOriginal);
				}
				return ResponseEntity.ok().headers(
						HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentoModificado.getIdDocumento().toString()))
						.body(result);
			}
			else {
				throw new IllegalArgumentException(messageSource.getMessage("error.documento.existe",
		                   null, LocaleContextHolder.getLocale()));	 	 
			}
	 	 }
		 
		 
		
		Documento result = documentoRepository.save(documentoModificado);
		
		if (!documentoModificado.getTieneTextos()) { 
			TextosDocumento textoOriginal = textosDocumentoRepository.findByDocumento(documentoModificado);
			
			if (textoOriginal != null)
				textosDocumentoRepository.delete(textoOriginal);
		}
		
		 
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentoModificado.getIdDocumento().toString()))
				.body(result);
	}

	public void deleteDocumento(final Long idDocumento) {
		Optional<Documento> documento = documentoRepository.findById(idDocumento);
		if (!documento.isPresent()) {
			throw new IllegalArgumentException("No existe un documento con ese identificador.");
		}
					
		Iterable<TextosDocumento> listaTextos = textosDocumentoRepository.findAllByDocumento(documento.get());
		if (listaTextos.iterator().hasNext()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.documento.referenciado.textosDocumento", null,
					LocaleContextHolder.getLocale()));
		}
		
		documentoRepository.deleteById(idDocumento);
	}

	public ResponseEntity<Documento> createDocumento(final Documento nuevoDocumento) throws URISyntaxException {
		log.debug("SERVICE request to save Documento : {}", nuevoDocumento);
		
		if (nuevoDocumento.getIdDocumento()  != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.documento.nuevo.id", null, LocaleContextHolder.getLocale()));
		}
		
		  
	 	 List <Documento> documentoDuplicado  = this.documentoRepository.findAllByNombreAndTipoPractica(nuevoDocumento.getNombre(),nuevoDocumento.getTipoPractica());
	 	 if (documentoDuplicado.size()>0)
	 	 {
	 		throw new IllegalArgumentException(messageSource.getMessage("error.documento.existe",
	                   null, LocaleContextHolder.getLocale()));	 	 
	 	 }
	 	 
		Documento result = documentoRepository.save(nuevoDocumento);
		return ResponseEntity.created(new URI("/api/documentos/" + result.getIdDocumento()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdDocumento().toString()))
				.body(result);
	}

	public Iterable<Documento> getAllDocumentosByTipoPractica(final Long idTipoPractica) {
		log.debug("SERVICE request to get all Documentos by TipoPractica");
		Iterable<Documento> listaDocumentos = this.documentoRepository
				.findAllByTipoPractica(this.tipoPracticaRepository.getOne(idTipoPractica));
		return listaDocumentos;
	}
 
	
	public void DeleteDocumentoTipoPractica(final Long IdTipoPractica, final Long idDocumento) {

		if (!tipoPracticaRepository.existsById(IdTipoPractica)) {
			throw new BadRequestAlertException("El tipo de practica asociado al documento no existe", ENTITY_NAME,
					"idnotexits");
		}
		Optional<Documento> doc = documentoRepository.findById(idDocumento);
		Iterable<TextosDocumento> listaTextos = textosDocumentoRepository.findAllByDocumento(doc.get());
		if (listaTextos.iterator().hasNext()) {
			throw new IllegalArgumentException(messageSource.getMessage("error.documento.referenciado.textosDocumento", null,
					LocaleContextHolder.getLocale()));
		}		
		
		documentoRepository.findById(idDocumento).map(documento -> {
			documentoRepository.delete(documento);
			return ResponseEntity.ok().build();
		}).orElseThrow(
				() -> new BadRequestAlertException("El documento a eliminar no existe", ENTITY_NAME, "idnotexits"));

	}

	public ResponseEntity<Documento> createDocumentoInTipoPractica(final Long idTipoPractica,
			@Valid @RequestBody Documento documento) throws URISyntaxException {
		log.debug("SERVICE request to save PeriodoPractica : {}", documento);
		if (documento.getIdDocumento() != null) {
			throw new BadRequestAlertException("A new documento cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Documento result = tipoPracticaRepository.findById(idTipoPractica).map(cn_tipo_practica -> {
			documento.setTipoPractica(cn_tipo_practica);
			return documentoRepository.save(documento);
		}).orElseThrow(() -> new BadRequestAlertException("El tipo de practica asociado al documento no existe",
				ENTITY_NAME, "idnotexits"));

		return ResponseEntity.created(new URI("/api/documentos/" + result.getIdDocumento()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdDocumento().toString()))
				.body(result);
	}

	
	
}

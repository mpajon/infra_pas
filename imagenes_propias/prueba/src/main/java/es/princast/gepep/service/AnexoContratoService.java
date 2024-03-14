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

import es.princast.gepep.domain.AnexoContrato;
import es.princast.gepep.domain.Convenio;
import es.princast.gepep.repository.AnexoContratoRepository;
import es.princast.gepep.repository.ConvenioRepository;
import es.princast.gepep.repository.OfertasCentroRepository;
import es.princast.gepep.repository.PeriodoPracticaRepository;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AnexoContratoService {

	private static final String ENTITY_NAME = "anexoContrato";

	@Autowired
	private AnexoContratoRepository anexoRepository;
	

	@Autowired
	private OfertasCentroRepository ofertaCentroRepository;


	@Autowired
	private ConvenioRepository convenioRepository;
	
	@Autowired
	private PeriodoPracticaRepository periodoPracticaRepository;

	@Autowired
	private MessageSource messageSource;

	public ResponseEntity<AnexoContrato> createAnexoContrato(final AnexoContrato nuevoAnexoContrato) throws URISyntaxException {
		log.debug("SERVICE request to save AnexoContrato : {}", nuevoAnexoContrato);

		if (nuevoAnexoContrato.getIdAnexo() != null) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.anexoContrato.nuevo.id", null, LocaleContextHolder.getLocale()));

		}
		Long idConvenio = nuevoAnexoContrato.getConvenio().getIdConvenio().longValue();
		Integer codAnexo = GetCodigoAnexo(nuevoAnexoContrato);
		nuevoAnexoContrato.setCodAnexo(codAnexo);
		AnexoContrato result = anexoRepository.save(nuevoAnexoContrato);
		return ResponseEntity.created(new URI("/api/anexo-contrato/" + result.getIdAnexo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdAnexo().toString())).body(result);
	}
	
	public Integer GetCodigoAnexo(final AnexoContrato anexo) { 
		Integer codigo = 0;
		//List<AnexoContrato> listaAnexos = anexoRepository.findAllByOfertaCentroAndConvenio(anexo.getOfertaCentro(),anexo.getConvenio());
		Long idConvenio = anexo.getConvenio().getIdConvenio().longValue();
		Convenio convenioAnexo = convenioRepository.getOne(anexo.getConvenio().getIdConvenio());
		List <AnexoContrato> listaAnexos = (List<AnexoContrato>) anexoRepository.findAllByConvenioOrderByCodAnexoDesc(convenioAnexo);
		if (listaAnexos != null && !listaAnexos.isEmpty())	{ 		
			codigo = listaAnexos.get(0).getCodAnexo()+1; 
 		}
		//si el convenio no tiene anexos, tomamos el anexo inicial del convenio.
		if (codigo.toString().equals("0")) {			
			codigo=convenioAnexo.getAnexoInicial();
			
		}
			 
		return codigo;	
		

	}

	public AnexoContrato getAnexoContrato(final Long idAnexoContrato) {
		//Optional<AnexoContrato> anexoContrato = anexoRepository.findById(idAnexoContrato);		
		Optional<AnexoContrato>  anexoContrato = anexoRepository.findOneAnxWithEagerRelationships(idAnexoContrato);		
		if (!anexoContrato.isPresent()) {
			throw new IllegalArgumentException("No existe una anexoContrato con ese identificador.");
		}
		return anexoContrato.get();
	}

	private Sort sortByOrdenAsc() {
	 
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "codAnexo").ignoreCase();
		return Sort.by(order);
	}
	
	public List<AnexoContrato> getAllAnexoContratos() {
		return anexoRepository.findAll(sortByOrdenAsc());		
	}

	public ResponseEntity<AnexoContrato> updateAnexoContrato(final AnexoContrato anexoContratoModificada) throws URISyntaxException {
		log.debug("SERVICE request to update AnexoContrato : {}", anexoContratoModificada);
		if (anexoContratoModificada.getIdAnexo() == null) {
			return createAnexoContrato(anexoContratoModificada);
		}
		AnexoContrato result = anexoRepository.save(anexoContratoModificada);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, anexoContratoModificada.getIdAnexo().toString()))
				.body(result);
	}

	public void deleteAnexoContrato(final Long idAnexoContrato) {
		Optional<AnexoContrato> anexoContrato = anexoRepository.findById(idAnexoContrato);
		if (!anexoContrato.isPresent()) {
			throw new IllegalArgumentException("No existe una anexoContrato con ese identificador.");
		}
		anexoRepository.deleteById(idAnexoContrato);
	}
	
	public Iterable<AnexoContrato> getAllByConvenio(final Long idConvenio) {
		log.debug("SERVICE request to get all Anexos by Convenio");
		//Iterable<AnexoContrato> listaAnexos = this.anexoRepository.findAllByConvenio(this.convenioRepository.getOne(idConvenio), pageable)
		Iterable<AnexoContrato> listaAnexos = this.anexoRepository.findAllByConvenioOrderByCodAnexoAsc(this.convenioRepository.getOne(idConvenio));
		return listaAnexos;
	}
	
	public Iterable<AnexoContrato> getAllByOfertasCentro(final String idOfertaCentro) {
		log.debug("SERVICE request to get all Anexos by Convenio");
		Iterable<AnexoContrato> listaAnexos = this.anexoRepository.findAllByOfertaCentro(this.ofertaCentroRepository.getOne(idOfertaCentro));
		return listaAnexos;
	}
	
	public Iterable<AnexoContrato> getAllByOfertasCentroPeriodo(final String idOfertaCentro, final Long idPeriodo) {
		log.debug("SERVICE request to get all Anexos by Convenio");
		Iterable<AnexoContrato> listaAnexos = this.anexoRepository.findAllByOfertaCentroAndPeriodo(this.ofertaCentroRepository.getOne(idOfertaCentro),this.periodoPracticaRepository.getOne(idPeriodo));
		return listaAnexos;
	}
	
	public Iterable<AnexoContrato> getAllByOfertasCentroAndConvenioAndPeriodo(final String idOfertaCentro, final Long idConvenio,final Long idPeriodo) {
		log.debug("SERVICE request to get all Anexos by Convenio");
		Iterable<AnexoContrato> listaAnexos = this.anexoRepository.findAllByOfertaCentroAndConvenioAndPeriodo(this.ofertaCentroRepository.getOne(idOfertaCentro),this.convenioRepository.getOne(idConvenio),this.periodoPracticaRepository.getOne(idPeriodo));
		return listaAnexos;
	}
		

 

}

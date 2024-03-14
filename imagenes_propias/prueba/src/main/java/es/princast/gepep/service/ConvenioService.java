package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import es.princast.gepep.config.ConvenioProperties;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Convenio;
import es.princast.gepep.domain.Area;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.repository.AreaRepository;
import es.princast.gepep.repository.CentroRepository;
import es.princast.gepep.repository.ConvenioRepository;
import es.princast.gepep.repository.TipoPracticaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.filter.ConvenioFilter;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
public class ConvenioService {

	private static final String ENTITY_NAME = "convenio";


	@Autowired
	ConvenioProperties convenioProperties;


	@Autowired
	private ConvenioRepository convenioRepository;

	@Autowired
	private CentroRepository centroRepository;


	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private TipoPracticaRepository tipoPracticaRepository;

	@Autowired
	private MessageSource messageSource;

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public ResponseEntity<Convenio> createConvenio(final Convenio nuevoConvenio) throws URISyntaxException {
		log.debug("SERVICE request to save Convenio : {}", nuevoConvenio);

		if (nuevoConvenio.getIdConvenio() != null) {
			throw new BadRequestAlertException("A new convenio cannot already have an ID", ENTITY_NAME, "idexists");
		}

		LocalDateTime fechaConvenio = convenioProperties.getFechaconveniob();

		nuevoConvenio.setAnioConvenio(nuevoConvenio.getFechaConvenio().getYear());
		nuevoConvenio.setMesConvenio(nuevoConvenio.getFechaConvenio().getMonthValue());
		nuevoConvenio.setDiaConvenio(nuevoConvenio.getFechaConvenio().getDayOfYear());

		if (nuevoConvenio.getTipoPractica().getNombre().equals("F.C.T.")) {
			if (LocalDate.now().isAfter(fechaConvenio.toLocalDate()) || LocalDate.now().isEqual(fechaConvenio.toLocalDate())) {
				if (!nuevoConvenio.getCodigo().substring(0, 1).equals("B")) {
					nuevoConvenio.setCodigo("B" + nuevoConvenio.getCodigo());
				}
			} else {
				if (!nuevoConvenio.getCodigo().substring(0, 1).equals("A")) {
					nuevoConvenio.setCodigo("A" + nuevoConvenio.getCodigo());
				}
			}
		} else if (!nuevoConvenio.getCodigo().substring(0, 1).equals("A")) {
			nuevoConvenio.setCodigo("A" + nuevoConvenio.getCodigo());
		}

		// comprobar duplicados antes de guardar
		Centro centro = this.centroRepository.getOne(nuevoConvenio.getCentro().getIdCentro());

		//ENS.DEPORTIVAS Y ARTISTICAS : 
		/*List<Convenio> convenioDuplicado = this.convenioRepository
				.findAllByCodigoAndCentroAndFechaBajaIsNullAndAntiguoIsFalse(nuevoConvenio.getCodigo(), centro);*/

		List<Convenio> convenioDuplicado = this.convenioRepository
				.findAllByCodigoAndTipoPracticaAndCentroAndFechaBajaIsNullAndAntiguoIsFalse(nuevoConvenio.getCodigo(), nuevoConvenio.getTipoPractica(), centro);
		if (convenioDuplicado.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.convenio.existe", null, LocaleContextHolder.getLocale()));
		}

		Convenio result = convenioRepository.save(nuevoConvenio);
		return ResponseEntity.created(new URI("/api/convenios/" + result.getIdConvenio()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdConvenio().toString()))
				.body(result);
	}

	public Convenio getConvenio(final Long idConvenio) {
		Optional<Convenio> convenio = convenioRepository.findById(idConvenio);
		if (!convenio.isPresent()) {
			throw new IllegalArgumentException("No existe una convenio con ese identificador.");
		}
		return convenio.get();
	}

	private Sort sortByIdAsc() {

		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "centro.codigo").ignoreCase();
		return Sort.by(order);
	}

	public List<Convenio> getAllConveniosByFechaBajaIsNull() {

		return convenioRepository.findAll(sortByIdAsc());
	}

	public List<Convenio> getAllConvenios() {

		return convenioRepository.findAll(sortByIdAsc());
	}

	public Page<Convenio> getAllConveniosPaginados(Pageable pageable) {
		return convenioRepository.findAll(pageable);
	}

	public ResponseEntity<Convenio> updateConvenio(final Convenio convenioModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Convenio : {}", convenioModificado);
		if (convenioModificado.getIdConvenio() == null) {
			return createConvenio(convenioModificado);
		}
		//ENS.DEPORTIVAS Y ARTISTICAS : 
		/*List<Convenio> convenioDuplicado = this.convenioRepository
				.findAllByCodigoAndCentroAndFechaBajaIsNullAndAntiguoIsFalse(convenioModificado.getCodigo(),
						convenioModificado.getCentro());*/
		LocalDateTime fechaConvenio = convenioProperties.getFechaconveniob();

		if (convenioModificado.getTipoPractica().getNombre().equals("F.C.T.")) {
			if ((!convenioModificado.getCodigo().substring(0, 1).equals("A")) && (!convenioModificado.getCodigo().substring(0, 1).equals("B"))) {
				if (LocalDate.now().isAfter(fechaConvenio.toLocalDate()) || LocalDate.now().isEqual(fechaConvenio.toLocalDate())) {
					convenioModificado.setCodigo("B" + convenioModificado.getCodigo());
				} else {
					convenioModificado.setCodigo("A" + convenioModificado.getCodigo());
				}
			}
		}
		 else if (!convenioModificado.getCodigo().substring(0, 1).equals("A")) {
			convenioModificado.setCodigo("A" + convenioModificado.getNumero().toString());
		}

		List<Convenio> convenioDuplicado = this.convenioRepository
				.findAllByCodigoAndTipoPracticaAndCentroAndFechaBajaIsNullAndAntiguoIsFalse(convenioModificado.getCodigo(), convenioModificado.getTipoPractica(),
						convenioModificado.getCentro());

		if (convenioDuplicado.size() > 0) {
			if (convenioDuplicado.size() == 1 && convenioDuplicado.get(0).getIdConvenio().toString()
					.equals(convenioModificado.getIdConvenio().toString())) {
				Convenio result = convenioRepository.save(convenioModificado);
				return ResponseEntity.ok().headers(
								HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, convenioModificado.getIdConvenio().toString()))
						.body(result);
			} else {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.convenio.existe", null, LocaleContextHolder.getLocale()));
			}

		}

		Convenio result = convenioRepository.save(convenioModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, convenioModificado.getIdConvenio().toString()))
				.body(result);

	}

	public void deleteConvenio(final Long idConvenio) {
		Optional<Convenio> convenio = convenioRepository.findById(idConvenio);
		if (!convenio.isPresent()) {
			throw new IllegalArgumentException("No existe una convenio con ese identificador.");
		}

		convenioRepository.deleteById(idConvenio);
	}

	public List<Convenio> getAllConveniosByCentro(final String idCentro) {

		log.debug("SERVICE request to get all Convenios by Centro");

		Centro centro = centroRepository.getOne(idCentro);
		if (centro == null) {
			throw new IllegalArgumentException("No existe el centro referido en la busqueda.");
		}
		// return convenioRepository.findAllByCentroAndFechaBajaIsNull(centro);
		return convenioRepository.findAllByCentroAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(centro);
	}

	public List<Convenio> getAllConveniosByCentroAndTipoPractica(final String idCentro, final Long idTipoPractica) {

		log.debug("SERVICE request to get all Convenios by Centro and TipoPractica");

		Centro centro = centroRepository.getOne(idCentro);

		TipoPractica tipoPractica = tipoPracticaRepository.getOne(idTipoPractica);

		if (centro == null) {
			throw new IllegalArgumentException("No existe el centro referido en Convenio.");
		}
		if (tipoPractica == null) {
			throw new IllegalArgumentException("No existe el tipoPractica de dicho Convenio.");
		}

		LocalDateTime fechaConvenio = convenioProperties.getFechaconveniob();

		if (tipoPractica.getNombre().equals("F.C.T.")) {
			if (LocalDate.now().isAfter(fechaConvenio.toLocalDate()) || LocalDate.now().isEqual(fechaConvenio.toLocalDate())) {
				//buscamos convenios tipo B -->
				return convenioRepository.findAllByCentroAndTipoPracticaAndCodigoStartsWithAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(centro,tipoPractica,"B");
			} else {
				return  convenioRepository.findAllByCentroAndTipoPracticaAndCodigoStartsWithAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(centro,tipoPractica,"A");

			}
		}
		return convenioRepository.findAllByCentroAndTipoPracticaAndFechaBajaIsNullAndAntiguoIsFalseOrderByNumeroAsc(centro, tipoPractica);

	}
	
	
	
	/**
	 * Recupera convenios por area.
	 * @param idArea
	 * @return
	 */
	public List<Convenio> getAllConveniosByArea(final Long idArea) {

		log.debug("SERVICE request to get all Convenios by Area");
		Area area = areaRepository.getOne(idArea);		 
		return convenioRepository.findAllByArea(area);
	}
	
	public List<Convenio> getAllConveniosByCentroAndPracticaValidado(final String idCentro,final Long idTipoPractica) {

		log.debug("SERVICE request to get all Convenios by Centro");

		Centro centro = centroRepository.getOne(idCentro);
		if (centro == null) {
			throw new IllegalArgumentException("No existe el centro referido en la busqueda.");
		}
		
		TipoPractica practica = tipoPracticaRepository.getOne(idTipoPractica);
 
		//return convenioRepository.findAllByCentroAndTipoPracticaAndFechaBajaIsNullAndAntiguoIsFalseAndValidadoIsTrueOrderByNumeroAsc(centro,practica);
		return convenioRepository.findConveniosToDistribucion(idTipoPractica, idCentro);	
		
	}

	@SuppressWarnings({ "serial" })
	public Page<Convenio> getAllConveniosByCriteria(final Convenio partialMatch, Pageable pageable) {
		return convenioRepository.findAll(new Specification<Convenio>() {
			@Override
			public Predicate toPredicate(Root<Convenio> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();

				if (partialMatch.getTipoPractica() != null && partialMatch.getTipoPractica().getIdTipoPractica() != null
						&& partialMatch.getTipoPractica().getIdTipoPractica() != 0) {
					predicates.add(builder.equal(root.get("idTipoPractica"), partialMatch.getTipoPractica()));
				}

				if (partialMatch.getCentro() != null && !StringUtils.isEmpty(partialMatch.getCentro().getCodigo())) {
					predicates.add(builder.like(builder.upper(root.get("codigo")),
							"%" + partialMatch.getCentro().getCodigo().toUpperCase() + "%"));
				}

				if (partialMatch.getCentro() != null && !StringUtils.isEmpty(partialMatch.getCentro().getIdCentro())) {
					predicates.add(
							builder.equal(root.get("centro").get("idCentro"), partialMatch.getCentro().getIdCentro()));
				}

				if (partialMatch.getCentro() != null && !StringUtils.isEmpty(partialMatch.getCentro().getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("centro").get("nombre")),
							"%" + partialMatch.getCentro().getNombre().toUpperCase() + "%"));
				}

				if (partialMatch.getArea() != null && partialMatch.getArea().getEmpresa() != null
						&& partialMatch.getArea().getEmpresa().getCif() != null
						&& !StringUtils.isEmpty(partialMatch.getArea().getEmpresa().getCif())) {
					predicates.add(builder.like(builder.upper(root.get("area").get("empresa").get("cif")),
							"%" + partialMatch.getArea().getEmpresa().getCif().toUpperCase() + "%"));
				}

				if (partialMatch.getArea() != null && partialMatch.getArea().getEmpresa() != null
						&& !StringUtils.isEmpty(partialMatch.getArea().getEmpresa().getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("area").get("empresa").get("nombre")),
							"%" + partialMatch.getArea().getEmpresa().getNombre().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getCodigo())) {
					predicates.add(builder.like(builder.upper(root.get("codigo")),
							"%" + partialMatch.getCodigo().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getFechaConvenio())) {
					predicates.add(builder.equal(root.get("fechaConvenio"), partialMatch.getFechaConvenio()));
				}

				/*
				 * if (!StringUtils.isEmpty(partialMatch.getFechaConvenio())) { LocalDate fecha
				 * = LocalDate.parse(partialMatch.getFechaConvenio(), FORMAT); predicates.add(
				 * builder.equal(root.get("fechaConvenio"), fecha)); }
				 */

				/*
				 * if (!StringUtils.isEmpty(fechaDesde)) { predicates
				 * .add(builder.greaterThanOrEqualTo(root.get("fechaConvenio"),
				 * partialMatch.getFechaConvenio())); } if (!StringUtils.isEmpty(fechaHasta)) {
				 * predicates.add(builder.lessThanOrEqualTo(root.get("fechaConvenio"),
				 * partialMatch.getFechaConvenio())); }
				 * 
				 * if(!StringUtils.isEmpty(fechaDesde))
				 * predicates.add(builder.between(root.get("idRegistroActividad").get(
				 * "fechaHoraRegistro"), fechaDesde, fechaHasta));
				 */

				/*
				 * if (!StringUtils.isEmpty(partialMatch.getFechaBaja()))
				 * predicates.add(builder.isNull(root.get("fechaBaja"))); else
				 * predicates.add(builder.isNotNull(root.get("fechaBaja")));
				 * 
				 */

				predicates.add(builder.equal(root.get("antiguo"), false));

				query.orderBy(builder.asc(builder.upper(root.get("codigo"))));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		}, pageable);
	}

	@SuppressWarnings({ "serial" })
	public Page<Convenio> getAllConveniosByCriteriaFilter(final ConvenioFilter partialMatch, Pageable pageable) {
		return convenioRepository.findAll(new Specification<Convenio>() {
			@Override
			public Predicate toPredicate(Root<Convenio> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
			 
				if (partialMatch.getIdPractica() != null && partialMatch.getIdPractica() != 0) {
					predicates.add(builder.equal(root.get("tipoPractica").get("idTipoPractica"),
							partialMatch.getIdPractica()));
				}

				if (!StringUtils.isEmpty(partialMatch.getCodigoCentro())) {
					predicates.add(builder.like(builder.upper(root.get("centro").get("codigo")),
							"%" + partialMatch.getCodigoCentro().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getIdCentro())) {
					predicates.add(builder.equal(root.get("centro").get("idCentro"), partialMatch.getIdCentro()));
				}

				if (!StringUtils.isEmpty(partialMatch.getNombreCentro())) {
					predicates.add(builder.like(builder.upper(root.get("centro").get("nombre")),
							"%" + partialMatch.getNombreCentro().toUpperCase() + "%"));
				}

				if (partialMatch.getCifEmpresa() != null && !StringUtils.isEmpty(partialMatch.getCifEmpresa())) {
					predicates.add(builder.like(builder.upper(root.get("area").get("empresa").get("cif")),
							"%" + partialMatch.getCifEmpresa().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getNombreEmpresa())) {
					predicates.add(builder.like(builder.upper(root.get("area").get("empresa").get("nombre")),
							"%" + partialMatch.getNombreEmpresa().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getCodigo())) {
					predicates.add(builder.like(builder.upper(root.get("codigo")),
							"%" + partialMatch.getCodigo().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getFechaConvenio())) {
					LocalDate fecha = LocalDate.parse(partialMatch.getFechaConvenio(), FORMAT);
					predicates.add(builder.equal(root.get("fechaConvenio"), fecha));
				}		
								 
				
				if (!StringUtils.isEmpty(partialMatch.getFechaDesde())
						&& !StringUtils.isEmpty(partialMatch.getFechaHasta())) {
					
					LocalDate fechaDesde = LocalDate.parse(partialMatch.getFechaDesde(), FORMAT);
					LocalDate fechaHasta = LocalDate.parse(partialMatch.getFechaHasta(), FORMAT);
					
					predicates.add(builder.between(root.get("fechaConvenio"), fechaDesde,fechaHasta));
				} else {

					if (!StringUtils.isEmpty(partialMatch.getFechaDesde())) {
						LocalDate fechaDesde = LocalDate.parse(partialMatch.getFechaDesde(), FORMAT);
						predicates.add(
								builder.greaterThanOrEqualTo(root.get("fechaConvenio"), fechaDesde));
					} else

					{
						if (!StringUtils.isEmpty(partialMatch.getFechaHasta())) {
							LocalDate fechaHasta = LocalDate.parse(partialMatch.getFechaHasta(), FORMAT);
							predicates.add(
									builder.lessThanOrEqualTo(root.get("fechaConvenio"), fechaHasta));
						}
					}

				}
				
				Predicate conFecha;
				Predicate sinFecha;
				Predicate fechaVigencia = null;

				if (!StringUtils.isEmpty(partialMatch.getVigente())) {
					LocalDate fechaActual = LocalDate.now();					
					if (partialMatch.getVigente()) {
						//predicates.add(builder.isNull(root.get("fechaFin")));
						
						 conFecha = builder.greaterThanOrEqualTo(root.get("fechaFin"), fechaActual);
						 sinFecha = builder.isNull(root.get("fechaFin"));
						 fechaVigencia = builder.or(conFecha,sinFecha);
					
					} else {
						//predicates.add(builder.isNotNull(root.get("fechaFin")));
						conFecha = builder.lessThanOrEqualTo(root.get("fechaFin"), fechaActual);
						sinFecha =builder.isNotNull(root.get("fechaFin"));
						fechaVigencia = builder.or(conFecha,sinFecha);					
					}
				}
				
				
				predicates.add(builder.equal(root.get("antiguo"), false));

				if (!StringUtils.isEmpty(partialMatch.getValidado())) {
					if (partialMatch.getValidado()) {
						predicates.add(builder.equal(root.get("validado"), true));
					} else {
						predicates.add(builder.equal(root.get("validado"), false));
					}
				}

				query.orderBy(builder.desc(builder.upper(root.get("codigo"))));
				
				Predicate pFinal = builder.and(predicates.toArray(new Predicate[] {}));
				
				if (!StringUtils.isEmpty(partialMatch.getVigente()))
					 return builder.and(pFinal,fechaVigencia);
				else 
					return builder.and(predicates.toArray(new Predicate[] {}));
					
				
			}

		}, pageable);
	}

}

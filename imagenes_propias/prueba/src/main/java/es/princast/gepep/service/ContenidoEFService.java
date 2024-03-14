package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.ConfigElemFormativo;
import es.princast.gepep.domain.ContenidoEF;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.repository.CicloRepository;
import es.princast.gepep.repository.ConfigElemFormativoRepository;
import es.princast.gepep.repository.ContenidoEFRepository;
import es.princast.gepep.repository.DistribucionRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ContenidoEFService {

	private static final String CONFIG_DISTRIBUCION = "DISTRIBUCION";



	private static final String CONFIG_CICLO = "CICLO";



	private static final String ENTITY_NAME = "contenidoEF";

	 

	@Autowired
	private ConfigElemFormativoRepository configElemFormativoRepository;
	
	@Autowired
	private ContenidoEFRepository contenidoEFRepository;
	
	
	@Autowired
	private CicloRepository cicloRepository;
	
	@Autowired
	private TipoPracticaService tipoPracticaService;
	
	@Autowired
	private CicloService cicloService;
	
	@Autowired
	private DistribucionService distribucionService;



	@Autowired
	private DistribucionRepository distribucionRepository;

	@Autowired
	private MessageSource messageSource;

	public ContenidoEF getContenidoEF(final Long idContenidoEF) {
		log.debug("SERVICE request to getContenidoEF ");
		Optional<ContenidoEF> contenidoEF = contenidoEFRepository.findById(idContenidoEF);
		if (!contenidoEF.isPresent()) {
			throw new IllegalArgumentException("No existe un contenido con ese identificador.");
		}
		return contenidoEF.get();
	}
	
	public ResponseEntity<ContenidoEF> createContenidoEF(final ContenidoEF nuevoContenidoEF) throws URISyntaxException {
		log.debug("SERVICE request to save ElementoFormativo : {}", nuevoContenidoEF);
		if (nuevoContenidoEF.getIdContenidoEF()!= null) {
			throw new BadRequestAlertException("Ya existe un contenido con  ", ENTITY_NAME, "idexists");
		}
		List<ContenidoEF> contenidoEFDuplicado = null;
		
		if (nuevoContenidoEF.getCiclo()!=null )	
			contenidoEFDuplicado = this.contenidoEFRepository.findAllByCicloAndConfigElemFormativo(nuevoContenidoEF.getCiclo(),nuevoContenidoEF.getConfigElemFormativo());
		if(nuevoContenidoEF.getDistribucion() != null)
			contenidoEFDuplicado = this.contenidoEFRepository.findAllByDistribucionAndConfigElemFormativo(nuevoContenidoEF.getDistribucion(),nuevoContenidoEF.getConfigElemFormativo());
		

		if (contenidoEFDuplicado.size() > 0) { 
			
			throw new IllegalArgumentException(
					messageSource.getMessage("error.contenidoEF.existe", null, LocaleContextHolder.getLocale()));
		}
		nuevoContenidoEF.setTexto(tratarSaltosLineas(nuevoContenidoEF.getTexto()));
		ContenidoEF result = contenidoEFRepository.save(nuevoContenidoEF);
		return ResponseEntity.created(new URI("/api/contenidoEF/" + result.getIdContenidoEF()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdContenidoEF().toString()))
				.body(result);
	}
	
	/**
	 * Elimina los saltos de linea consecutivos 
	 * @param texto
	 * @return
	 */
	private String tratarSaltosLineas(String texto){
	    String [] splited = texto.split("\n");
	    String result = "";
	    String aux = "";
	    for (int i = 0; i < splited.length; i++) {
		aux=splited[i].trim().replaceAll("\t", "");
		if(aux!=null && !aux.equals("")) {
		    result+=splited[i];
		    if(i<splited.length-1) {
			    result+="\n";
			}
		}
		
	    }
	    return result;
	}

	public List<ContenidoEF> getAllContenidoEF() {
		//String[] propiedades = { "denominacion" };
		//Sort sort = new Sort(Sort.Direction.ASC, propiedades);
		return contenidoEFRepository.findAll();
	}
	
	public List<ContenidoEF> getContenidoEFByCiclo(final Ciclo ciclo) {
		
		List<ContenidoEF> contenidosEF = contenidoEFRepository.findAllByCiclo(ciclo);
	
		
		// Ordenar contenidos por orden establecido en la configuracion //
	    	
	    	orderContenidosEFList(contenidosEF);
	    	
	    	return contenidosEF;
	}
	

	public List<ContenidoEF> getContenidoEFByDistribucion(final Long idDistribucion) {	
		
		Optional<Distribucion> distribucion = distribucionRepository.findById(idDistribucion);
		if (!distribucion.isPresent()) {
			throw new IllegalArgumentException("No existe una distribucion con ese identificador.");
		}
		List<ContenidoEF>  listaContenidoEF = contenidoEFRepository.findAllByDistribucion(distribucion.get());
		orderContenidosEFList(listaContenidoEF);
    	
    	return listaContenidoEF;
	}
	
	 
	public Iterable<ContenidoEF> getContenidoEFByConfig(final Long idConfigElemFormativo) {
			 
		Iterable<ContenidoEF> listaContenidoEF = contenidoEFRepository.findAllByConfigElemFormativo(this.configElemFormativoRepository.getOne(idConfigElemFormativo));
		return listaContenidoEF;
	}
	
	
	public Iterable<ContenidoEF> getContenidoEFByCicloAndConfig(final String idCiclo,final Long idConfigElemFormativo) {
		 
		Optional<Ciclo> ciclo = cicloRepository.findById(idCiclo);
		if (!ciclo.isPresent()) {
			throw new IllegalArgumentException("No existe una ciclo con ese identificador.");
		}
		
		Iterable<ContenidoEF> listaContenidoEF = contenidoEFRepository.findAllByCicloAndConfigElemFormativo(ciclo.get(),this.configElemFormativoRepository.getOne(idConfigElemFormativo));
		return listaContenidoEF;
	}
	
	public Iterable<ContenidoEF> getContenidoEFByCicloAndConfig(final Ciclo ciclo,final ConfigElemFormativo configElemFormativo) {
		 
		Iterable<ContenidoEF> listaContenidoEF = contenidoEFRepository.findAllByCicloAndConfigElemFormativo(ciclo,configElemFormativo);
		return listaContenidoEF;
	}	 
	
	public Iterable<ContenidoEF> getContenidoEFByDistribucionAndConfig(final Distribucion distribucion,final ConfigElemFormativo configElemFormativo) {
		
		Iterable<ContenidoEF> listaContenidoEF = contenidoEFRepository.findAllByDistribucionAndConfigElemFormativo(distribucion,configElemFormativo);
		return listaContenidoEF;
	}
	 
	
	/**
	 * Obtenemos los contenidos asociados a las configuraciones del tipo de practica filtrados por ciclo o distribucion segun la configuracion de los elementos formativos
	 * @param tipoPractica
	 * @param ciclo
	 * @param distribucion
	 * @return
	 */
	public List<ContenidoEF> getContenidosEFByTipoPracticaCicloDist(final TipoPractica tipoPractica , final Ciclo ciclo, final Distribucion distribucion ) {
	List<ContenidoEF> contenidosEF = new ArrayList<>();
    	for (ConfigElemFormativo configElemFormativo : tipoPractica.getConfiguracionesEF()) {
    	    contenidosEF.addAll(getContenidosEFByDistCicloConfig(ciclo, distribucion, configElemFormativo));
	}
    	// Ordenar contenidos por orden establecido en la configuracion //
    	orderContenidosEFList(contenidosEF);
    	
    	return contenidosEF;
	}
    	
    	/**
	 * Obtenemos los contenidos asociados a las configuraciones del tipo de practica filtrados por ciclo o distribucion segun la configuracion de los elementos formativos
	 * @param idtipoPractica
	 * @param idciclo
	 * @param iddistribucion
	 * @return
	 */
	public List<ContenidoEF> getContenidosEFByTipoPracticaCicloDist(final Long idTipoPractica , final String idCiclo, final Long idDistribucion ) {
	TipoPractica tipoPractica = tipoPracticaService.getTipoPractica(idTipoPractica);
	Distribucion distribucion  =null;
	Ciclo ciclo = null;
	if(idCiclo!=null && !idCiclo.equals("undefined")) {
	    try {
	    ciclo = cicloService.getCiclo(idCiclo);
	    } catch (IllegalArgumentException e) {
		ciclo = null;
	    }
	}
	if(idDistribucion!= null) {
	    distribucion = distribucionService.getDistribucion(idDistribucion);
	}
    	return getContenidosEFByTipoPracticaCicloDist(tipoPractica, ciclo, distribucion);
    	
	}
	
	/**
	 * Obtiene el contenido asociado a la configuracion de elemento formativo tanto si esta asociado a ciclo como a distribucion
	 * @param ciclo
	 * @param distribucion
	 * @param configElemFormativo
	 * @return
	 */
	public List<ContenidoEF>  getContenidosEFByDistCicloConfig(final Ciclo ciclo, final Distribucion distribucion, ConfigElemFormativo configElemFormativo) {
	    List<ContenidoEF> contenidosEF = new ArrayList<>();
	    if(ciclo != null && configElemFormativo.getConfiguracion().equalsIgnoreCase(CONFIG_CICLO)) {
    		contenidosEF.addAll((List<ContenidoEF>)getContenidoEFByCicloAndConfig(ciclo, configElemFormativo));
    	    }
    	    if(distribucion != null && configElemFormativo.getConfiguracion().equalsIgnoreCase(CONFIG_DISTRIBUCION)) {
    		contenidosEF.addAll((List<ContenidoEF>)getContenidoEFByDistribucionAndConfig(distribucion, configElemFormativo));
    	    }
    	    return contenidosEF;
	}
	
	public ResponseEntity<ContenidoEF> updateContenidoEF(final ContenidoEF contenidoEFModificado) throws URISyntaxException {
		log.debug("SERVICE request to update ElementoFormativo : {}", contenidoEFModificado);
		if (contenidoEFModificado.getIdContenidoEF() == null) {
			return this.createContenidoEF(contenidoEFModificado);
		}
		
		if (contenidoEFModificado.getDistribucion()!=null)
		{
			
			List<ContenidoEF> configElemFormativoDuplicado= this.contenidoEFRepository.findAllByDistribucionAndConfigElemFormativo(contenidoEFModificado.getDistribucion(),contenidoEFModificado.getConfigElemFormativo());
			if (configElemFormativoDuplicado.size() > 0) {
				if (!(configElemFormativoDuplicado.size() == 1
						&& configElemFormativoDuplicado.get(0).getIdContenidoEF().equals(contenidoEFModificado.getIdContenidoEF()))) {
					throw new IllegalArgumentException(
							messageSource.getMessage("error.contenidoEF.existe", null, LocaleContextHolder.getLocale()));
				}
			} 
			
		} else {
		
		List<ContenidoEF> configElemFormativoDuplicado= this.contenidoEFRepository.findAllByCicloAndConfigElemFormativo(contenidoEFModificado.getCiclo(),contenidoEFModificado.getConfigElemFormativo());		

		if (configElemFormativoDuplicado.size() > 0) {
			if (!(configElemFormativoDuplicado.size() == 1
					&& configElemFormativoDuplicado.get(0).getIdContenidoEF().equals(contenidoEFModificado.getIdContenidoEF()))) {
				throw new IllegalArgumentException(
						messageSource.getMessage("error.contenidoEF.existe", null, LocaleContextHolder.getLocale()));
			}
		} 
		}
		 
		contenidoEFModificado.setTexto(tratarSaltosLineas(contenidoEFModificado.getTexto()));
		ContenidoEF result = contenidoEFRepository.save(contenidoEFModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contenidoEFModificado.getIdContenidoEF().toString()))
				.body(result);
	}
	
	
	public void deleteContenidoEF(final Long idContenidoEF) {
		Optional<ContenidoEF> contenidoEF = contenidoEFRepository.findById(idContenidoEF);
		if (!contenidoEF.isPresent()) {
			throw new IllegalArgumentException("No existe un contenido con ese identificador");
		}
		 
		contenidoEFRepository.deleteById(idContenidoEF);
	}

	
	/**
	 * Ordena una lista de contenidos de elemento formativo en funcion del orden establecido en su configuracion interna
	 * @param contenidosEF
	 */
	private void orderContenidosEFList(List<ContenidoEF> contenidosEF) {
	    contenidosEF.sort(new Comparator<ContenidoEF>() {
	        @Override
	        public int compare(ContenidoEF c1, ContenidoEF c2) {
	            if(c1.getConfigElemFormativo().getOrden() == c2.getConfigElemFormativo().getOrden()){
	                return 0;
	            }
	            return c1.getConfigElemFormativo().getOrden() < c2.getConfigElemFormativo().getOrden() ? -1 : 1;
	         }
	    });
	}


}

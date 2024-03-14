package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.princast.gepep.domain.Centro;
import es.princast.gepep.domain.Ciclo;
import es.princast.gepep.domain.Precios;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.TipoPractica;
import es.princast.gepep.domain.VisitaAgrupada;
import es.princast.gepep.domain.VisitaTutor;
import es.princast.gepep.repository.PreciosRepository;
import es.princast.gepep.repository.VisitaAgrupadaRepository;
import es.princast.gepep.repository.VisitaTutorRepository;
import es.princast.gepep.service.util.GepepHelper;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class VisitaTutorService {

	private static final String ENTITY_NAME = "VisitaTutor";
	@Autowired
	private VisitaTutorRepository visitaTutorRepository;
	
	@Autowired
	private PreciosRepository preciosRepository;
	   
	@Autowired
	private VisitaAgrupadaRepository visitaAgrupadaRepository;
	  
	@Autowired
	private CentroService centroService;
	    
	@Autowired
	private EntityManager entityManager; 
	
	@Autowired
	private MessageSource messageSource;

	public ResponseEntity<VisitaTutor> createVisitaTutor(final VisitaTutor nuevaVisita) throws URISyntaxException {
		log.debug("SERVICE request to save createVisitaTutor : {}", nuevaVisita);

		if (nuevaVisita.getIdVisitaTutor() != null) {
			throw new BadRequestAlertException("A new visitaTutor cannot already have an ID", ENTITY_NAME, "idexists");
		}
		
		// Comprobamos visitas del mismo tipo ya creadas //
		
		List<VisitaTutor> visitasExistentes = visitaTutorRepository.findByProfesorDiaCicloTipoPractica(nuevaVisita);
		if(visitasExistentes != null && visitasExistentes.size()>0) {
			throw new IllegalArgumentException(messageSource.getMessage("error.visita.existe",
	                   null, LocaleContextHolder.getLocale()));	 	
		
		}
		
		// Calculo de precios //
		
		Optional<Precios> precios = preciosRepository.findPreciosByTipoPractica(nuevaVisita.getTipoPractica());
		if (precios.isPresent()) {	
		
			if (nuevaVisita.getDietas())
			{
				float importe_dietas = precios.get().getPrecioMediaManut();
				nuevaVisita.setImporteDietas(importe_dietas);			
			}
			
			float importe_km = nuevaVisita.getKm()*precios.get().getPrecioKm();
			nuevaVisita.setImporteKm(GepepHelper.redondearDecimalesFloat(importe_km, 2));
			float importeTotal = nuevaVisita.getImporteDietas()+nuevaVisita.getImporteBilletes()+nuevaVisita.getImporteKm()+nuevaVisita.getImporteOtros();
			nuevaVisita.setImporteTotal(GepepHelper.redondearDecimalesFloat(importeTotal,2));
			calcularImportes(nuevaVisita, precios);
		
		}

		VisitaTutor result = visitaTutorRepository.save(nuevaVisita);
		return ResponseEntity.created(new URI("/api/visitas-tutor/" + result.getIdVisitaTutor()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdVisitaTutor().toString()))
				.body(result);
	}

	public VisitaTutor getVisitaTutoria(final Long idVisitaTutor) {
		Optional<VisitaTutor> visitaTutor = visitaTutorRepository.findById(idVisitaTutor);
		if (!visitaTutor.isPresent()) {
			throw new IllegalArgumentException("No existe una visitaTutor con ese identificador.");
		}
		return visitaTutor.get();
	}


	private Sort sortByIdAsc() {
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idVisitaTutor").ignoreCase();
		return Sort.by(order);
	}

	private Sort sortByEmpresaAsc() {
		// Sort sort = new Sort(Sort.Direction.ASC, "nombre");
		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "empresa").ignoreCase();
		return Sort.by(order);
	}

	
	public  List<VisitaTutor> getVisitaTutoriaByProfesor(final String idProfesor) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaTutoriaByProfesor");
	        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesor(getInstanceProfesor(idProfesor));
	        return  (List<VisitaTutor>) listaVisitas;       
	}

	public  List<VisitaTutor> getVisitaTutoriaByProfesorAndMesAndAnioAndTipoPractica(String idProfesor, Integer mes, Integer anio, Long idTipoPractica) {
		 log.debug("SERVICE request to get all Visitas by Profesor and Autorizada getVisitaTutoriaByProfesorAndMesAndAnioAndTipoPractica");
	        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndMesAndAnioAndTipoPracticaOrderByDiaAscMesAsc(getInstanceProfesor(idProfesor), mes, anio, getInstanceTipoPractica(idTipoPractica));
	        return  (List<VisitaTutor>) listaVisitas;       
	}

	
	public  List<VisitaTutor> getVisitaTutoriaByProfesorAndAutorizadaAndMesAndAnio(String idProfesor, Boolean autorizada, Integer mes, Integer anio) {
		 log.debug("SERVICE request to get all Visitas by Profesor and Autorizada getVisitaTutoriaByProfesorAndAutorizadaAndMesAndAnio");
	        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAutorizadaAndMesAndAnio(getInstanceProfesor(idProfesor), autorizada, mes, anio);
	        return  (List<VisitaTutor>) listaVisitas;       
	}

	public  List<VisitaTutor> getVisitaTutoriaByProfesorAndAutorizadaAndMesAndAnioAndTipoPractica(String idProfesor, Boolean autorizada, Integer mes, Integer anio, Long idTipoPractica) {
		 log.debug("SERVICE request to get all Visitas by Profesor and Autorizada getVisitaTutoriaByProfesorAndAutorizadaAndMesAndAnioAndTipoPractica");
	        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAutorizadaAndMesAndAnioAndTipoPractica(getInstanceProfesor(idProfesor), autorizada, mes, anio, getInstanceTipoPractica(idTipoPractica));
	        return  (List<VisitaTutor>) listaVisitas;       
	}
	
	public  List<VisitaTutor> getVisitaTutoriaByProfesorAndAnioAndMes(final String idProfesor, final Integer anio, final Integer mes) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaTutoriaByProfesorAndAnioAndMes");
	        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAnioAndMesOrderByIdVisitaTutor(getInstanceProfesor(idProfesor),anio,mes);
	        return  (List<VisitaTutor>) listaVisitas;       
	}
	
	public  List<VisitaTutor> getVisitaTutoriaByProfesorAndAnioAndMesAndTipoPractica(final String idProfesor, final Integer anio, final Integer mes, final Long idTipoPractica) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaTutoriaByProfesorAndAnioAndMesAndTipoPractica");
		 //CRQ151434
		 //Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAnioAndMesAndTipoPracticaOrderByIdVisitaTutor(getInstanceProfesor(idProfesor),anio,mes, getInstanceTipoPractica(idTipoPractica));		 
		 Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAnioAndMesAndTipoPracticaOrderByDiaAscMesAsc(getInstanceProfesor(idProfesor),anio,mes, getInstanceTipoPractica(idTipoPractica));
	        return  (List<VisitaTutor>) listaVisitas;       
	}

	
	public  List<VisitaTutor> getVisitaTutoriaByProfesorAndAnioAndMesAndCiclo(final String idProfesor, final Integer anio, final Integer mes, final String idCiclo) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaTutoriaByProfesorAndAnioAndMesAndCiclo");
		
	        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAnioAndMesAndCicloOrderByDiaAscMesAsc(getInstanceProfesor(idProfesor),anio,mes,getInstanceCiclo(idCiclo));
	        return  (List<VisitaTutor>) listaVisitas;       
	}

	
	public  List<VisitaTutor> getVisitaTutoriaByProfesorAndAnioAndMesAndCicloAndTipoPractica(final String idProfesor, final Integer anio, final Integer mes, final String idCiclo, final Long idTipoPractica) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaTutoriaByProfesorAndAnioAndMesAndCicloAndTipoPractica");
		 //CRQ151434
		 //Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAnioAndMesAndCicloAndTipoPracticaOrderByIdVisitaTutor(getInstanceProfesor(idProfesor),anio,mes, getInstanceCiclo(idCiclo),  getInstanceTipoPractica(idTipoPractica));
		 Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorAndAnioAndMesAndCicloAndTipoPracticaOrderByDiaAscMesAsc(getInstanceProfesor(idProfesor),anio,mes, getInstanceCiclo(idCiclo),  getInstanceTipoPractica(idTipoPractica));
	     return  (List<VisitaTutor>) listaVisitas;       
	}
	
	public  List<VisitaTutor> findByAnioCentroProfesor(final String idProfesor, final Integer anio, final String idCentro) {
		 log.debug("SERVICE request to get all Visitas by Tutor findByAnioCentroProfesor");
		 
		 List<VisitaTutor> listaVisitas = new ArrayList<>();
		 
		 if(idCentro.equals("undefined")) {
			 List<Centro> centros = centroService.getAllCentrosActivos();
			 for (Centro centro : centros) {
				 listaVisitas.addAll(queryVisitasByAnioCentroProfesor(idProfesor, anio, centro.getIdCentro()));
			}
		 }
		 else {
			 listaVisitas = queryVisitasByAnioCentroProfesor(idProfesor, anio, idCentro);
		 }
		 
		 return listaVisitas;
	}
	

	private List<VisitaTutor> queryVisitasByAnioCentroProfesor(final String idProfesor, final Integer anio, final String idCentro){
		 log.debug("SERVICE request to queryVisitasByAnioCentroProfesor" );
	String query = 
			"Select new VisitaTutor(visit, cent) "
			+ " from VisitaTutor visit "
			+ " inner join Centro cent on cent.idCentro = :idCentro "
			+ " where visit.idVisitaTutor in (" +
						"select distinct visita.idVisitaTutor " +
			 	 		" from  OfertasCentro oc " +
			 			" inner join Unidad unidad on unidad.ofertaCentro.idOfertaCentro = oc.idOfertaCentro " + // unidades del centro //
			 			" inner join Centro centro on oc.centro.idCentro = centro.idCentro " +
			 			" inner join OfertaFormativa offor on offor.idOfertaFormativa = oc.oferta.idOfertaFormativa " +
			 			" inner join Ciclo ciclo on ciclo.idCiclo = offor.ciclo.idCiclo " +
			 			" inner join VisitaTutor visita on visita.ciclo.idCiclo = ciclo.idCiclo " +
			 			" inner join CursoAcademico ca on ca.idAnio = :anio " +
			  	 		" WHERE " +
			 			" (unidad.tutor.idProfesor = visita.profesor.idProfesor or unidad.tutorAdicional.idProfesor = visita.profesor.idProfesor ) " + // unidades del tutor/tutorAdicional de centro //
			  	 		" and (visita.profesor.idProfesor = :idProfesor or 'undefined' = :idProfesor)" + // visitas del tutor // 
			  	 		" and (" + // la visita ha de estar entre las fechas de inicio/final de curso academico //
			  	 		" (visita.anio = year(ca.fe_inicio) and ( (visita.mes > month(ca.fe_inicio)) or (visita.mes = month(ca.fe_inicio) and visita.dia >= day(ca.fe_inicio)) )  )" + 
			  	 		" or" +
			  	 		" (visita.anio = year(ca.fe_final) and ( (visita.mes < month(ca.fe_final)) or (visita.mes = month(ca.fe_final) and visita.dia <= day(ca.fe_final)) )  )" + 
			  	 		" )" +	
			  	 		" and ( centro.idCentro = :idCentro or :idCentro = 'undefined' )"
  	 		+ ") order by visit.tipoPractica.nombre, visit.anio, visit.mes, visit.dia "
  	 		;

	
	List<VisitaTutor> lista = entityManager.createQuery(query, VisitaTutor.class).setParameter("idProfesor", idProfesor).setParameter("anio", anio).setParameter("idCentro", idCentro).getResultList();

	return lista;
	
	}
	

	public  List<VisitaAgrupada> getVisitaAgrupadaByAnioAndMes(Integer anio,Integer mes) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaAgrupadaByAnioAndMes" );
	        Iterable <VisitaAgrupada> listaVisitas =  this.visitaAgrupadaRepository.findListadoByAnioAndMes(anio,mes);
	        return  (List<VisitaAgrupada>) listaVisitas;       
	}
	

	public  List<VisitaAgrupada> getVisitaAgrupadaByAnioAndMesAndTutor(Integer anio,Integer mes,String tutor) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaAgrupadaByAnioAndMesAndTutor");
	        Iterable <VisitaAgrupada> listaVisitas =  this.visitaAgrupadaRepository.findListadoByAnioAndMesAndTutor(anio,mes,tutor);
	        return  (List<VisitaAgrupada>) listaVisitas;       
	}
	

	public  List<VisitaAgrupada> getVisitaAgrupadaByCentroAndAnioAndMes(String centro, Integer anio,Integer mes) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaAgrupadaByCentroAndAnioAndMes");
	        Iterable <VisitaAgrupada> listaVisitas =  this.visitaAgrupadaRepository.findListadoByCentroAndAnioAndMes(centro, anio, mes);
	        return  (List<VisitaAgrupada>) listaVisitas;       
	}
	
	public  List<VisitaAgrupada> getVisitaAgrupadaByCentroAndAnioAndMesAndTutor(String centro, Integer anio,Integer mes,String tutor) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaAgrupadaByCentroAndAnioAndMesAndTutor");
	        Iterable <VisitaAgrupada> listaVisitas =  this.visitaAgrupadaRepository.findListadoByCentroAndAnioAndMesAndTutor(centro, anio, mes,tutor);
	        return  (List<VisitaAgrupada>) listaVisitas;       
	}
	
	
	public  List<VisitaAgrupada> getVisitaAgrupadaByCentroAndAnioAndMesAndTipoPractica(String centro, Integer anio,Integer mes, Integer idTipoPractica,Integer cursoAca) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaAgrupadaByCentroAndAnioAndMesAndTipoPractica");
	        Iterable <VisitaAgrupada> listaVisitas =  this.visitaAgrupadaRepository.findListadoByCentroAndAnioAndMesAndTipoPractica(centro, anio, mes, idTipoPractica,cursoAca);
	        return  (List<VisitaAgrupada>) listaVisitas;       
	}
	
	public  List<VisitaAgrupada> getVisitaAgrupadaByCentroAndAnioAndMesAndTipoPracticaAndTutor(String centro, Integer anio,Integer mes, Integer idTipoPractica, String tutor) {
		 log.debug("SERVICE request to get all Visitas by Tutor getVisitaAgrupadaByCentroAndAnioAndMesAndTipoPracticaAndTutor");
	        Iterable <VisitaAgrupada> listaVisitas =  this.visitaAgrupadaRepository.findListadoByCentroAndAnioAndMesAndTipoPracticaAndTutor(centro, anio, mes, idTipoPractica, tutor);
	        return  (List<VisitaAgrupada>) listaVisitas;       
	}
	
	public  List<VisitaTutor> getVisitaTutoriaByAnioAndMes(Integer anio,Integer mes) {
		 log.debug("SERVICE request to get all Visitas by anio and mes getVisitaTutoriaByAnioAndMes");		  
	        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByAnioAndMes(anio,mes);
	        return  (List<VisitaTutor>) listaVisitas;       
	}
	 
	
	public List<VisitaTutor> getAllVisitasTutores() {
		return visitaTutorRepository.findAll();
	}

	public ResponseEntity<VisitaTutor> updateVisitaTutor(final VisitaTutor visitaTutorModificado) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaTutor : {}", visitaTutorModificado);
		if (visitaTutorModificado.getIdVisitaTutor() == null) {
			return createVisitaTutor(visitaTutorModificado);
		}
		 
		// Comprobamos visitas del mismo tipo ya creadas //
		
		List<VisitaTutor> visitasExistentes = visitaTutorRepository.findByProfesorDiaCicloTipoPractica(visitaTutorModificado);
		
		 if (visitasExistentes.size()>0)
	 	 {
			 if (!(visitasExistentes.size()==1 &&  visitasExistentes.get(0).getIdVisitaTutor().equals(visitaTutorModificado.getIdVisitaTutor())))
		 	 {
				 throw new IllegalArgumentException(messageSource.getMessage("error.visita.existe",
		                   null, LocaleContextHolder.getLocale()));	 	 
				}
		 }
				
		
		Optional<Precios> precios = preciosRepository.findPreciosByTipoPractica(visitaTutorModificado.getTipoPractica());
		if (precios.isPresent()) {	
		
		calcularImportes(visitaTutorModificado, precios);
		
		}
		
		
		VisitaTutor result = visitaTutorRepository.save(visitaTutorModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visitaTutorModificado.getIdVisitaTutor().toString()))
				.body(result);

	}

	private void calcularImportes(final VisitaTutor visitaTutor, Optional<Precios> precios) {
		/*calculos de importes*/
		if (visitaTutor.getDietas())
		{
			float importe_dietas = precios.get().getPrecioMediaManut();
			visitaTutor.setImporteDietas(importe_dietas);
		}else {
			visitaTutor.setImporteDietas(0f);
		}
		
		// si no estamos en el municipio se calculan importes en funcion de km si es vehiculo propio o billete si es publico
		if(!visitaTutor.getMismoMunicipio()) {
			if(!visitaTutor.getTransportePublico()) {
				// transporte privado //
				float importe_km = visitaTutor.getKm()*precios.get().getPrecioKm();
				visitaTutor.setImporteKm(GepepHelper.redondearDecimalesFloat(importe_km,2));
				float importeTotal = visitaTutor.getImporteDietas()+visitaTutor.getImporteKm()+visitaTutor.getImporteOtros();
				visitaTutor.setImporteTotal(GepepHelper.redondearDecimalesFloat(importeTotal,2));
			}else {
				// transporte publico //
				visitaTutor.setImporteKm(0f);
				float importeTotal = visitaTutor.getImporteDietas()+visitaTutor.getImporteBilletes()+visitaTutor.getImporteOtros();
				visitaTutor.setImporteTotal(GepepHelper.redondearDecimalesFloat(importeTotal,2));
			}
		}else {
			visitaTutor.setImporteKm(0f);
			visitaTutor.setImporteTotal(GepepHelper.redondearDecimalesFloat(visitaTutor.getImporteDietas(),2));
		}
		
	}
	
	public void updateVisitaTutorAutorizarTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaTutorAutorizarTodas - realizada : {}", visita.getRealizada());
		visitaTutorRepository.updateAutorizaVisitaByTutorAndMesAndAnioAndTipoPractica(
				visita.getProfesor().getIdProfesor(), visita.getAnio(), visita.getMes(), visita.getAutorizada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());

	}

	public void updateVisitaTutorNoAutorizarTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaTutorNoAutorizarTodas : {}", visita);
		visitaTutorRepository.updateNoAutorizaVisitaByTutorAndMesAndAnioAndTipoPractica(
				visita.getProfesor().getIdProfesor(), visita.getAnio(), visita.getMes(), visita.getAutorizada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());

	}

	public void updateVisitaTutorBloquearTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaTutorBloquearTodas : {}", visita);
		visitaTutorRepository.updateBloqueaVisitaByTutorAndMesAndAnioAndTipoPractica(
				visita.getProfesor().getIdProfesor(), visita.getAnio(), visita.getMes(), visita.getBloqueada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());

	}
	
	public void updateVisitaTutorRealizarTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaTutorRealizarTodas : {}", visita);
		visitaTutorRepository.updateRealizaVisitaByTutorAndMesAndAnioAndTipoPractica(
				visita.getProfesor().getIdProfesor(), visita.getAnio(), visita.getMes(), visita.getRealizada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());
		
	}

	public void updateVisitaAutorizarTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaAutorizarTodas : {}", visita);
		visitaTutorRepository.updateAutorizaVisitaByMesAndAnioAndTipoPractica(visita.getAnio(),
				visita.getMes(), visita.getAutorizada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());

	}

	public void updateVisitaNoAutorizarTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaNoAutorizarTodas : {}", visita);
		visitaTutorRepository.updateNoAutorizaVisitaByMesAndAnioAndTipoPractica(visita.getAnio(),
				visita.getMes(), visita.getAutorizada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());

	}

	public void updateVisitaBloquearTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaBloquearTodas : {}", visita);
		visitaTutorRepository.updateBloqueaVisitaByMesAndAnioAndTipoPractica(visita.getAnio(),
				visita.getMes(), visita.getBloqueada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());

	}

	public void updateVisitaRealizarTodas(final VisitaTutor visita) throws URISyntaxException {
		log.debug("SERVICE request to updateVisitaRealizarTodas : {}", visita);
		visitaTutorRepository.updateRealizaVisitaMesAndAnioAndTipoPractica(visita.getAnio(), visita.getMes(),
				visita.getRealizada(),
				visita.getTipoPractica() == null ? null : visita.getTipoPractica().getIdTipoPractica());

	}
	
	
	 
	
	

	public void deleteTutor(final Long idTutor) {
		Optional<VisitaTutor> visitaTutor = visitaTutorRepository.findById(idTutor);
		if (!visitaTutor.isPresent()) {
			throw new IllegalArgumentException("No existe una visitaTutor con ese identificador.");
		}
		visitaTutorRepository.deleteById(idTutor);
	}


	// recuperar visitas por año y bimestre
	public List<VisitaTutor> getVisitasTutorAnioBiMestral ( String idProfesor, Integer anio, Integer biMestral){
		log.debug("SERVICE request getVisitasTutorAnioBiMestral");
		Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorIdProfesorAndAnioAndAutorizadaAndRealizadaAndMesBetweenOrderByMesAscDiaAsc(idProfesor,anio,true,true,(biMestral-1), biMestral);
		return  (List<VisitaTutor>) listaVisitas;
	}

    public List<VisitaTutor> getVisitasPrevistasTutorAnio ( String idProfesor, Integer anio, Integer mes){
        log.debug("SERVICE request to getVisitasPrevistasTutorAnio");
        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorIdProfesorAndAnioAndMes(idProfesor,anio,mes);
        return  (List<VisitaTutor>) listaVisitas;
    }
    
    public List<VisitaTutor> getVisitasPrevistasTutorAnioTipoPractica ( String idProfesor, Integer anio, Integer mes, Long idTipoPractica){
        log.debug("SERVICE request to getVisitasPrevistasTutorAnioTipoPractica");
        Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorIdProfesorAndAnioAndMesAndTipoPracticaOrderByDiaAscMesAsc(idProfesor,anio,mes, getInstanceTipoPractica(idTipoPractica));
        return  (List<VisitaTutor>) listaVisitas;
    }

	public List<VisitaTutor> getVisitasTutorAnioTrimestre(String idProfesor, Integer anio,String idCiclo, Integer iniMes, Integer finMes) {
		log.debug("SERVICE request to getVisitasTutorAnioTrimestre");
		Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorIdProfesorAndAnioAndRealizadaAndCicloIdCicloAndMesBetweenOrderByDiaAscMesAsc(idProfesor,anio,true,idCiclo,iniMes, finMes);
		return  (List<VisitaTutor>) listaVisitas;
	}
	
	public List<VisitaTutor> getVisitasAutorizadasRealizadasTutorAnioTipoPracticaPeriodoLiquidacion(String idProfesor, Integer anio,String idCiclo, Integer iniMes, Integer finMes, Long idTipoPractica) {
		log.debug("SERVICE request to getVisitasAutorizadasRealizadasTutorAnioTipoPracticaPeriodoLiquidacion");
		Iterable <VisitaTutor> listaVisitas =  this.visitaTutorRepository.findAllByProfesorIdProfesorAndAnioAndAutorizadaAndRealizadaAndCicloIdCicloAndTipoPracticaIdTipoPracticaAndMesBetweenOrderByDiaAscMesAsc(idProfesor,anio,true,true,idCiclo,idTipoPractica,iniMes, finMes);
		return  (List<VisitaTutor>) listaVisitas;
	}
	
	
	
	
	private TipoPractica getInstanceTipoPractica(final Long idTipoPractica) {
		TipoPractica tipoPractica = new TipoPractica();
		tipoPractica.setIdTipoPractica(idTipoPractica);
		return tipoPractica;
	}

	private Profesor getInstanceProfesor(final String idProfesor) {
		Profesor profesor = new Profesor();
		 	profesor.setIdProfesor(idProfesor);
		return profesor;
	}
	
	private Ciclo getInstanceCiclo(final String idCiclo) {
		Ciclo ciclo = new Ciclo();
		 ciclo.setIdCiclo(idCiclo);
		 return ciclo;
	}
}

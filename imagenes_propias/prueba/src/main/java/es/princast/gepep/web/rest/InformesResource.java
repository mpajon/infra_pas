package es.princast.gepep.web.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import es.princast.gepep.domain.*;
import es.princast.gepep.service.*;
import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.princast.gepep.domain.reports.Calendario;
import es.princast.gepep.domain.reports.Curso;
import es.princast.gepep.domain.reports.FichaIndividualProg;
import es.princast.gepep.domain.reports.GastosAlumnosReport;
import es.princast.gepep.domain.reports.GastosProfesorReport;
import es.princast.gepep.domain.reports.Mes;
import es.princast.gepep.service.util.GepepHelper;
import es.princast.gepep.web.rest.errors.FileStorageException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;


@RestController
@RequestMapping("/api")
@Api(description = "Creacion de informes", tags = {"Informes"})
@Slf4j
public class InformesResource {

  
 	private static final String EF4_CODIGO = "EF4";

	private static final String EF3_CODIGO = "EF3";

	private static final String EF2_CODIGO = "EF2";

	private static final String EF1_CODIGO = "EF1";

	private static final String FOLDER_REPORTS_IMG = "reports/img";

	private static String MARCA_AGUA = "marca_agua.jpg";
    // private static String TP_FCT = "FCT";
    private static String TP_DUAL = "DUAL";
    private static String TP_DEPORTIVA = "DEPORTIVA";
    private static String TP_ART_SUPERIOR = "ARTISTICA SUPERIOR";
    private static String TP_ART_DISENO = "ARTISTICA DE ARTES Y DISEÑO";
    // private static String TP_PROFESORADO = "PROFESORADO";
    private static String FONDO_NO_LECTIVO = "#D7D7D7";
    private static String FONDO_LECTIVO = "#FFFFFF";

    @Autowired
    private TextosDocumentoService textosDocumentoService;
    @Autowired
    private DocumentoService documentoService;
    @Autowired
    private ConvenioService convenioService;
    @Autowired
    private CentroService centroService;
    @Autowired
    private ResponsableAreaService responsableAreaService;
    @Autowired
    private OfertasCentroService ofertasCentroService;
    @Autowired
    private OfertaFormativaService ofertaFormativaService;
    @Autowired
    private AnexoContratoService anexoContratoService;
    @Autowired
    private DistribucionService distribucionService;
    @Autowired
    private DistribucionPeriodoService distribucionPeriodoService;
    @Autowired
    private RealizacionService realizacionService;
    @Autowired
    private MatriculaService matriculaService;
    @Autowired
    private ProfesorService profesorService;
    @Autowired
    private VisitaTutorService visitaTutorService;
    @Autowired
    private PreciosService preciosService;
    @Autowired
    private CicloService cicloService; 
    @Autowired
    private TipoPracticaService tipoPracticaService;
    @Autowired
    private GastoAlumnoService gastoAlumnoService;
    @Autowired
    private PeriodoLiquidacionService periodoLiquidacionService;
    @Autowired
    private ImportesTipoGasto2Service importesTipoGasto2Service;
    @Autowired
    private UnidadService unidadService;
    @Autowired
    private AreaService areaService;

    @Autowired
    private PeriodoPracticaService periodoPracticaService;
    
    @Autowired
    ServletContext serveContext;
    @Autowired
    MessageSource configSource;
    @Autowired
    MessageSource reportsSource;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ContenidoEFService contenidoEFService;
    
    @GetMapping("/informes/convenio/{idDocumento}/{idConvenio}/{idTipoPractica}/{lugar}/{dia}/{mes}/{anio}")
    public ResponseEntity<byte[]> getConvenio( @PathVariable String idDocumento,@PathVariable String idConvenio, @PathVariable Long idTipoPractica, @PathVariable String lugar, @PathVariable String dia, @PathVariable String mes, @PathVariable String anio) throws  Exception{
        
    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	try {
    		
	        Convenio convenio = convenioService.getConvenio(Long.valueOf(idConvenio));
	        TipoPractica tipoPractica = tipoPracticaService.getTipoPractica(idTipoPractica);

            InputStream convenioReportStream=  getClass().getResourceAsStream("/reports/convenio.jasper");
            InputStream anexosReportStream;
            if (documento.getNombre().contains("Convenio B")){
                anexosReportStream = getClass().getResourceAsStream("/reports/convenio_anexosB.jasper");
            }else
                anexosReportStream = getClass().getResourceAsStream("/reports/convenio_anexos.jasper");
            
            
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(convenioReportStream);
	
	        Map<String, Object> parametros = new HashMap<>();
	
	        PersistentSet listaSet = (PersistentSet) documento.getClausulas();
	        List<Clausula> listaClausulas = new ArrayList<Clausula>(listaSet);
	
	        parametros = cargaParametrosConvenio(documento, convenio, lugar);
	       
	        //Cargamos la fecha
	        parametros.put("P_DIAEN",(dia==null)?"":dia);
	        // Obtienes el nombre del mes
	        Calendar c1 = Calendar.getInstance();
	        c1.set(Integer.valueOf(anio),Integer.valueOf(mes)-1, Integer.valueOf(dia)); // ajusta la fecha
	        String nombreDeMes = new SimpleDateFormat("MMMM", new Locale("es","ES")).format(c1.getTime());
	        parametros.put("P_CATEG_TIPO_PRACT",(tipoPractica.getCategoria().getCategoria()));
	        parametros.put("P_MESEN",(nombreDeMes==null)?"":nombreDeMes);
	        parametros.put("P_ANIOEN",(anio==null)?"":anio);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_CABECERA_CLAUSULAS",(documento.getEncabezadoClausulas()==null)?"":documento.getEncabezadoClausulas());
	        parametros.put("P_ENCABEZADO_FIRMA",(documento.getEncabezadoFirma()==null)?"":documento.getEncabezadoFirma());
	        parametros.put("P_LISTA_CLAUSULAS",new JRBeanCollectionDataSource(listaClausulas));
	        parametros.put("P_SUBREPORT", anexosReportStream);
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	
	        return generarPDF(jasperPrint,documento);
        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }        
    }

    private Map<String,Object> cargaParametrosConvenio(Documento documento, Convenio convenio, String  localidad) throws IOException {

        Map<String, Object> params = inicializarLogos(documento);
        
        params.put("P_ENCABEZADO1",(documento.getEncabezado()==null)?"":documento.getEncabezado());
        params.put("P_ENCABEZADO2",(documento.getEncabezado2()==null)?"":documento.getEncabezado2());
        params.put("P_TIPOIDENTIFICADOR", (documento.getTipoDocumento().getDescripcion()==null)?"":documento.getTipoDocumento().getDescripcion());
        params.put("P_CODCONVENIO",(convenio.getCodigo()==null)?"":convenio.getCodigo());
        params.put("P_CODCENTRO",(convenio.getCentro().getCodigo()==null)?"":convenio.getCentro().getCodigo());
        params.put("P_DIRECTOR",(convenio.getCentro().getDirector()==null)?"":convenio.getCentro().getDirector());
        params.put("P_NIFDIRECTOR",(convenio.getCentro().getNifDirector()==null)?"":convenio.getCentro().getNifDirector());
        params.put("P_NOMBRECENTRO",(convenio.getCentro().getNombre()==null)?"":convenio.getCentro().getNombre());
        params.put("P_LOCALIDAD",(convenio.getCentro().getLocalidad()==null)?"":convenio.getCentro().getLocalidad());
        params.put("P_DIRECCION",(convenio.getCentro().getDireccion()==null)?"":convenio.getCentro().getDireccion());
        params.put("P_CP",(convenio.getCentro().getCp()==null)?"":convenio.getCentro().getCp());
        params.put("P_CIF",(convenio.getCentro().getCif()==null)?"":convenio.getCentro().getCif());
        params.put("P_TELEFONO",(convenio.getCentro().getTelefono()==null)?"":convenio.getCentro().getTelefono());
        params.put("P_FAX",(convenio.getCentro().getFax()==null)?"":convenio.getCentro().getFax());
        params.put("P_EMAIL",(convenio.getCentro().getEmail()==null)?"":convenio.getCentro().getEmail());
        
       // Para cada tipo de práctica un visor (no obligatorio),

       if(convenio.getTipoPractica()!=null) {
    	   Visor visorDirGeneral = null;
    	   visorDirGeneral = convenio.getTipoPractica().getVisor();		
    	  params.put("P_NOMBREDIRGENERAL", visorDirGeneral!=null ? visorDirGeneral.getNombre() : "");
    	  params.put("P_APEDIRGENERAL", visorDirGeneral!=null ? visorDirGeneral.getApellidos() : "");
       }
       
       Area area = convenio.getArea();
       String responsablesString = "D./Dña:           Con D.N.I.: \n";
       String responsablesFirmaString = "";
       
       List<ResponsableArea> responsableAreaList =  (List<ResponsableArea>) responsableAreaService.getResponsablByArea(area.getIdArea());
       if (!responsableAreaList.isEmpty()) {
	   responsablesString = "";
	   int cont = 1;
	   for (ResponsableArea responsableArea : responsableAreaList) {
	       if(cont <= 5) {
		   responsablesString += "D./Dña: " + ((responsableArea.getNombre()==null)?"":responsableArea.getNombre()) + "   Con D.N.I.: " + ((responsableArea.getNif()==null)?"":responsableArea.getNif()) + "\n";
		   responsablesFirmaString += ((responsableArea.getNombre()==null)?"":responsableArea.getNombre());
	       }
	       if(cont < responsableAreaList.size()) {
	 	  responsablesFirmaString += "\n";
	       }
	       cont ++;
	   }
       }        
       
        params.put("P_RESPONSABLES",responsablesString);
        params.put("P_RESPONSABLES_FIRMA",responsablesFirmaString);
        params.put("P_NOMBREEMPRESA",( area.getEmpresa().getNombre()==null)?"": area.getEmpresa().getNombre());
        params.put("P_NOMBREAREA",( area.getNombre()==null)?"": area.getNombre());
        params.put("P_LOCALIDADEMPRESA",( area.getLocalidad()==null)?"": area.getLocalidad());
        params.put("P_PROVINCIAEMPRESA",( area.getProvincia()==null)?"": area.getProvincia());
        params.put("P_DIRECCIONEMPRESA",( area.getDireccion()==null)?"": area.getDireccion());
        params.put("P_CPEMPRESA",( area.getCp()==null)?"": area.getCp());
        params.put("P_CIFEMPRESA",( area.getEmpresa().getCif()==null)?"": area.getEmpresa().getCif());
        params.put("P_TELEFONOEMPRESA",( area.getTelefono()==null)?"": area.getTelefono());
        params.put("P_FAXEMPRESA",( area.getFax()==null)?"": area.getFax());
        params.put("P_EMAILEMPRESA",( area.getEmail()==null||area.getEmail().equals("NULL"))?"": area.getEmail());        


        TextosDocumento textosDocumento = new TextosDocumento();
        List<TextosDocumento> textoDocumentosList =  (List<TextosDocumento>) textosDocumentoService.getAllTextosDocumentoByDocumento(documento.getIdDocumento());
        if (!textoDocumentosList.isEmpty()) {
        	textosDocumento = textoDocumentosList.get(0);
        }

        params.put("P_EXPONEN",( textosDocumento.getExponen()==null)?"": textosDocumento.getExponen());
        params.put("P_ACUERDAN",( textosDocumento.getAcuerdan()==null)?"": textosDocumento.getAcuerdan());
        params.put("P_LOCALIDADEN",( localidad==null)?"": localidad);

        return params;
    }
    


    @GetMapping("/informes/relAlumnado/{idDocumento}/{idOferCenSel}/{idAnexo}/{curso}/{lugar}/{dia}/{mes}/{anio}")
    public ResponseEntity<byte[]> getRelAlumnado(@PathVariable String idDocumento,@PathVariable String idOferCenSel,
                                                 @PathVariable String idAnexo,@PathVariable Integer curso,
                                                 @PathVariable String lugar, @PathVariable Integer dia, @PathVariable Integer mes,
                                                 @PathVariable Integer anio) throws  Exception{
		
		Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
		   
		try {
		    	OfertasCentro ofertaCentro = ofertasCentroService.getOfertasCentro(idOferCenSel);
		        AnexoContrato anexoContrato = anexoContratoService.getAnexoContrato(Long.valueOf(idAnexo));
		
		        Map<String, Object> parametros = new HashMap<>();
		
		        // carga parámetros comunes
		        parametros = inicializarLogos(documento);
		        parametros.put("P_DOCUMENTO",documento);
		        List<AlumDisAnexoiii> listaAlumDisAnexo = (List<AlumDisAnexoiii>) distribucionService
                        .getListaAlumDisByAnexo(Integer.valueOf(idAnexo));
		        parametros.put("tabla_alumnos",new JRBeanCollectionDataSource(listaAlumDisAnexo));
		        parametros.put("P_MARCA_AGUA",getMarcaAgua());
		        parametros.put("P_FECHA_FDO",formatearFecha(dia,mes,anio,lugar));
		        parametros.put("P_CATEG_TIPO_PRACT",(documento.getTipoPractica().getCategoria().getCategoria()));
		        
		        //obtengo tutorEmpresa para pasarlo como parámetro.
		        		        
		        String tutorEmpresa =null;
		        if(listaAlumDisAnexo != null && !listaAlumDisAnexo.isEmpty())
		        	tutorEmpresa = listaAlumDisAnexo.get(0).getTutorEmpresa();
		
		        // comprobar tipo documento
		        String nomReport = new String("");
		
		        if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_ART_SUPERIOR)
		        	|| documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_ART_DISENO)){
		            nomReport = "relacionAlumnos.jasper";
		            parametros = cargaPropertiesReport("relalum",parametros);
		            parametros = cargaParametrosRelAlumnadoArtistico(documento, ofertaCentro, anexoContrato, lugar, curso,tutorEmpresa,parametros);
		        }
		        else if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DUAL)){
		            nomReport = "relacionAlumnos_dual.jasper";
		        }
		        else {  // FCT y resto de enseñanzas con mismo diseño
		            nomReport = "relacionAlumnos.jasper";
		            parametros = cargaPropertiesReport("relalum",parametros);
		            parametros = cargaParametrosRelAlumnadoFCT(documento, ofertaCentro, anexoContrato, lugar, curso,tutorEmpresa,parametros);
		        }
		
		        InputStream relacionAlumnosReportStream = getClass().getResourceAsStream("/reports/"+nomReport);
		       JasperReport jasperReport = (JasperReport) JRLoader.loadObject(relacionAlumnosReportStream);
		
		       JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(listaAlumDisAnexo));
		
		       return generarPDF(jasperPrint,documento);
		       
		} catch (Exception e) {
			log.error("Error generando informe" + documento.getNombre(), e);
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
		}        
    }

    private Map<String,Object> cargaParametrosRelAlumnadoArtistico(Documento documento, OfertasCentro ofertaCentro, AnexoContrato anexo, String lugar, Integer curso, String tutorEmpresa, Map<String,Object>params) throws IOException {
        Convenio convenio = new Convenio();
        Ciclo ciclo = new Ciclo();
        String cursoAcademico="";

        if (anexo.getConvenio()!=null) {
            convenio =	anexo.getConvenio();
        }

        Area area = convenio.getArea();
       /* ResponsableArea responsableArea1 = new ResponsableArea();
        List<ResponsableArea> responsableAreaList =  (List<ResponsableArea>) responsableAreaService.getResponsablByArea(area.getIdArea());
        if (!responsableAreaList.isEmpty()) {
            responsableArea1 = responsableAreaList.get(0);           
        }*/
        
        String responsablesString = "";
        
        List<ResponsableArea> responsableAreaList =  (List<ResponsableArea>) responsableAreaService.getResponsablByArea(area.getIdArea());
        if (!responsableAreaList.isEmpty()) {
 	   responsablesString = "";
 	   int cont = 1;
 	   for (ResponsableArea responsableArea : responsableAreaList) {
 	       if(cont<=5) {
 		   responsablesString += ((responsableArea.getNombre()==null)?"":responsableArea.getNombre());
 	       }
 	       if(cont < responsableAreaList.size()) {
 		  responsablesString += "\n";
 	       }
 	      cont ++;
 	   }
        }        
        
         params.put("P_RESPONSABLES",responsablesString);
        
        if(ofertaCentro.getOferta()!=null) {
            ciclo = ofertaCentro.getOferta().getCiclo();
        }

        params.put("P_IDCENTRO",(ofertaCentro.getCentro().getCodigo()==null)?"":ofertaCentro.getCentro().getCodigo());
        params.put("P_CODCONVENIO",(convenio.getCodigo()==null)?"":convenio.getCodigo());
        params.put("P_IDANEXO",(anexo.getCodAnexo()==null)?"":anexo.getCodAnexo().toString());

        String[] arg = new String[]{(convenio.getCodigo()==null)?"":convenio.getCodigo(),
                (convenio.getFechaConvenio()==null)?"":fechaToString(convenio.getFechaConvenio()),
                (ofertaCentro.getCentro().getNombre()==null)?"":ofertaCentro.getCentro().getNombre(),
                (area.getEmpresa().getNombre()==null)?"": area.getEmpresa().getNombre(),
                ( area.getNombre()==null)?"": area.getNombre()};
        params.put("P_PARRAFO1",reportsSource.getMessage("relalum.p.parrafo1.eeaass",arg,LocaleContextHolder.getLocale()));

        arg = new String[]{( ciclo.getNombre()==null)?"": ciclo.getNombre()};
        params.put("P_TEXTO1",reportsSource.getMessage("relalum.p.ciclo.formativo",arg,LocaleContextHolder.getLocale()));

        arg = new String[]{( ciclo.getFamilia()==null)?"": ciclo.getFamilia()};
        params.put("P_TEXTO3",reportsSource.getMessage("relalum.p.familia",arg,LocaleContextHolder.getLocale()));

        if(curso!=null) {
            cursoAcademico = curso.toString() + " - " + String.valueOf(curso+1);
        }
        arg = new String [] {""};
        params.put("P_TEXTO2",reportsSource.getMessage("relalum.p.otras.ensenanzas",arg,LocaleContextHolder.getLocale()));
        arg = new String[]{cursoAcademico};
        params.put("P_TEXTO4",reportsSource.getMessage("relalum.p.anio.academico",arg,LocaleContextHolder.getLocale()));

        //TODO: TUTOR NO ESTAN CARGADOS LOS DATOS Y ADEMAS LA TABLA SE CARGA A NIVEL DE MATRICULA
        // De momento sacamos el director
        
        //20190320_prod_INC12
        //En el Anexo III viene el nombre del representante legal de la empresa como nombre del tutor.
        //Debe reflejarse el nombre del tutor/a indicado en la distribución del alumnado
    
        arg = new String[]{(ofertaCentro.getCentro().getDirector()==null)?"":ofertaCentro.getCentro().getDirector(), // TUTOR
                            (tutorEmpresa==null)?"":tutorEmpresa};
        params.put("P_PARRAFO2",reportsSource.getMessage("relalum.p.parrafo2.eeaass",arg,LocaleContextHolder.getLocale()));

        
//        arg = new String[]{(responsableArea1.getNombre()==null)?"":responsableArea1.getNombre()};
//        params.put("P_FDO_RESPONSABLE",reportsSource.getMessage("relalum.p.fdo",arg,LocaleContextHolder.getLocale()));

        arg = new String[]{(ofertaCentro.getCentro().getDirector()==null)?"":ofertaCentro.getCentro().getDirector()};
        params.put("P_FDO_DIRECTOR",reportsSource.getMessage("relalum.p.fdo",arg,LocaleContextHolder.getLocale()));

        return params;
    }

    private Map<String,Object> cargaParametrosRelAlumnadoFCT(Documento documento, OfertasCentro ofertaCentro,AnexoContrato anexo, String  localidad, Integer curso, String tutorEmpresa,Map<String,Object>params) throws IOException {
    	
        Convenio convenio = new Convenio();
        Ciclo ciclo = new Ciclo();
        String cursoAcademico="";
      
        if (anexo.getConvenio()!=null) {
        	convenio =	anexo.getConvenio();
        }

        Area area = convenio.getArea();

        String responsablesString = "";
        
        List<ResponsableArea> responsableAreaList =  (List<ResponsableArea>) responsableAreaService.getResponsablByArea(area.getIdArea());
        if (!responsableAreaList.isEmpty()) {
 	   responsablesString = "";
 	   int cont = 1;
 	   for (ResponsableArea responsableArea : responsableAreaList) {
 	       if(cont<=5) {
 		   responsablesString += ((responsableArea.getNombre()==null)?"":responsableArea.getNombre());
 	       }
 	       if(cont < responsableAreaList.size()) {
 		  responsablesString += "\n";
 	       }
 	      cont ++;
 	   }
        }        
        
         params.put("P_RESPONSABLES",responsablesString);

        if(ofertaCentro.getOferta()!=null) {
            ciclo = ofertaCentro.getOferta().getCiclo();
        }

        params.put("P_IDCENTRO",(ofertaCentro.getCentro().getCodigo()==null)?"":ofertaCentro.getCentro().getCodigo());
        params.put("P_CODCONVENIO",(convenio.getCodigo()==null)?"":convenio.getCodigo());
        params.put("P_IDANEXO",(anexo.getCodAnexo()==null)?"":anexo.getCodAnexo().toString());
        
        String[] arg = new String[]{(convenio.getCodigo()==null)?"":convenio.getCodigo(),
                                    (convenio.getFechaConvenio()==null)?"":convenio.getFechaConvenio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    (ofertaCentro.getCentro().getNombre()==null)?"":ofertaCentro.getCentro().getNombre(),
                                    (area.getEmpresa().getNombre()==null)?"": area.getEmpresa().getNombre(),
                                    ( area.getNombre()==null)?"": area.getNombre()};
        
        String parrafo1 = reportsSource.getMessage("relalum.p.parrafo1.fct",arg,LocaleContextHolder.getLocale());
        
        if(documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DEPORTIVA)) {
            parrafo1= reportsSource.getMessage("relalum.p.parrafo1.deportivas",arg,LocaleContextHolder.getLocale());
        }
            
        params.put("P_PARRAFO1",parrafo1);

        if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DEPORTIVA))
        	arg = new String[]{( ciclo.getNombre()==null)?"": ofertaCentro.getOferta().getNombre()};
        else
        	arg = new String[]{( ciclo.getNombre()==null)?"": ciclo.getNombre()};
        params.put("P_TEXTO1",reportsSource.getMessage("relalum.p.ciclo.formativo",arg,LocaleContextHolder.getLocale()));

        if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DEPORTIVA))
        	arg = new String[]{( ciclo.getNombre()==null)?"": ofertaCentro.getOferta().getCodigo()};
        else
        	arg = new String[]{( ciclo.getCodigo()==null)?"": ciclo.getCodigo()};
        params.put("P_TEXTO2",reportsSource.getMessage("relalum.p.clave",arg,LocaleContextHolder.getLocale()));
        arg = new String[]{""};
        params.put("P_TEXTO3",reportsSource.getMessage("relalum.p.otras.ensenanzas",arg,LocaleContextHolder.getLocale()));

        if(curso!=null) {
            cursoAcademico = curso.toString() + " - " + String.valueOf(curso+1);
        }
        arg = new String[]{cursoAcademico};
        params.put("P_TEXTO4",reportsSource.getMessage("relalum.p.curso.academico",arg,LocaleContextHolder.getLocale()));

      
        List<Unidad> listaUnidades = unidadService.getAllUnidadesByOfertaCentroAndNombre(ofertaCentro.getIdOfertaCentro(), curso,anexo.getUnidad().getNombre());
        Unidad unidad = new Unidad();
        if(!listaUnidades.isEmpty()) {
        	 unidad = listaUnidades.get(0);
        }
        
        
        //20190320_prod_INC12
        //En el Anexo III viene el nombre del representante legal de la empresa como nombre del tutor.
        //Debe reflejarse el nombre del tutor/a indicado en la distribución del alumnado
        
        arg = new String[]{(unidad.getTutor() ==null)?"":unidad.getTutor().getNombreCompleto(), // TUTOR distribucion.
                            (tutorEmpresa==null)?"":tutorEmpresa};
        params.put("P_PARRAFO2",reportsSource.getMessage("relalum.p.parrafo2.fct",arg,LocaleContextHolder.getLocale()));

        arg = new String[]{(ofertaCentro.getCentro().getDirector()==null)?"":ofertaCentro.getCentro().getDirector()};
        params.put("P_FDO_DIRECTOR",reportsSource.getMessage("relalum.p.fdo",arg,LocaleContextHolder.getLocale()));

        return params;
    }

    @GetMapping("/informes/psv/{idDocumento}/{idDistribucion}/{idTipoPractica}")
    public ResponseEntity<byte[]> getPsv(@PathVariable String idDocumento,@PathVariable String idDistribucion,@PathVariable Long idTipoPractica) throws  Exception{
        

    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
	        
	    try {
	    	
	    
	    	Distribucion distribucion = distribucionService.getDistribucion(Long.valueOf(idDistribucion));
	    	TipoPractica tipoPractica = tipoPracticaService.getTipoPractica(documento.getTipoPractica().getIdTipoPractica());
	    	Ciclo ciclo = distribucion.getMatricula().getOfertaCentro().getOferta().getCiclo();
	    	
	    	List<ContenidoEF> contenidosEF = contenidoEFService.getContenidosEFByTipoPracticaCicloDist(tipoPractica, ciclo, distribucion);
	        
	        String nomReport = new String("");
	        if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DUAL)){
	            nomReport = "fichaIndividualProg_dual.jasper";
	        }
	        else{
	            nomReport = "fichaIndividualProg.jasper";
	        }
	
	        InputStream psvReportStream = getClass().getResourceAsStream("/reports/"+nomReport);
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(psvReportStream);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_CATEG_TIPO_PRACT",(tipoPractica.getCategoria().getCategoria()));
	
	        parametros.put("P_DISTRIBUCION",distribucion);
	        parametros.put("P_DIRECCION",formatoDireccion(distribucion));
	        parametros.put("P_HORARIOS",formatoHorarios(distribucion));
	        
	        // Parametros Elementos formativos //
	        ConfigElemFormativo[] configsEFArray = tipoPractica.getConfiguracionesEF().toArray(new ConfigElemFormativo[0]);
	        for (int i = 0; i < configsEFArray.length; i++) {
	            if(configsEFArray[i] != null && configsEFArray[i].getActivo() && configsEFArray[i].getDenominacion()!=null) {
	        	parametros.put("P_EF" + (i + 1) ,configsEFArray[i].getDenominacion().toUpperCase());
	            }else {
	        	parametros.put("P_EF" + (i + 1) ,"");
	            }
		}
	        
	        
	        // Horas de la distribucion, si no tiene las tomamos del ciclo //
	
	        Integer horasDistribucion = distribucionPeriodoService.getHorasEnDistribucion(distribucion.getMatricula().getIdMatricula(), Long.valueOf(idDistribucion), idTipoPractica);
	        if(horasDistribucion == null) {
	        	horasDistribucion = distribucion.getMatricula().getOfertaCentro().getOferta().getCiclo().getHorasPractica();
	        }
	        parametros.put("P_HORAS", horasDistribucion);
	        
	        List<FichaIndividualProg> listaAprendizaje = generaListaFichaIndividualProg(distribucion, distribucion.getAnexoContrato().getOfertaCentro().getOferta().getCiclo().getIdCiclo(), contenidosEF, tipoPractica);
	
	        // Si no hay datos de elementos formativos añadimos datos vacios para obtener el informe aunque sea vacío//
	        if ( listaAprendizaje.size() == 0){
	            listaAprendizaje.add(new FichaIndividualProg());
	        }
	
	        parametros.put("listaAprendizaje",listaAprendizaje);
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(listaAprendizaje));
	
	        return generarPDF(jasperPrint,documento);
        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
   	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
       }        
        
    }
    	

    private String formatoDireccion (Distribucion distribucion){
        String calle = (distribucion.getMatricula().getAlumno().getCalle()==null)?"":distribucion.getMatricula().getAlumno().getCalle();
        String numero = (distribucion.getMatricula().getAlumno().getNumero()==null)?"":distribucion.getMatricula().getAlumno().getNumero();
        String piso = (distribucion.getMatricula().getAlumno().getPiso()==null)?"":distribucion.getMatricula().getAlumno().getPiso();
        String letra = (distribucion.getMatricula().getAlumno().getLetra()==null)?"":distribucion.getMatricula().getAlumno().getLetra();
        //CRQ151434
        String localidad = (distribucion.getMatricula().getAlumno().getLocalidad()==null)?"":distribucion.getMatricula().getAlumno().getLocalidad();
        String municipio = (distribucion.getMatricula().getAlumno().getMunicipio()==null)?"":distribucion.getMatricula().getAlumno().getMunicipio();
        return calle + ", " + numero + " " + piso + " " + letra + " " + localidad + ", "+ municipio;
    }

    private String formatoHorarios (Distribucion distribucion){
        String salida = new String();

        String horaIniMan = (distribucion.getHEntradaMan()==null)?"":String.format("%02d",distribucion.getHEntradaMan());
        String minIniMan = (distribucion.getMinEntradaMan()==null)?"":String.format("%02d", distribucion.getMinEntradaMan());
        salida += "Mañanas Entrada: "+horaIniMan + ":"+ minIniMan;
        String horaFinMan = (distribucion.getHSalidaMan()==null)?"":String.format("%02d",distribucion.getHSalidaMan());
        String minFinMan = (distribucion.getMinSalidaMan()==null)?"":String.format("%02d",distribucion.getMinSalidaMan());
        salida += " Salida: "+ horaFinMan + ":"+ minFinMan;
        String horaIniTar = (distribucion.getHEntradaTard()==null)?"":String.format("%02d",distribucion.getHEntradaTard());
        String minIniTar = (distribucion.getMinEntradaTard()==null)?"":String.format("%02d",distribucion.getMinEntradaTard());
        salida += "- Tardes  Entrada: "+ horaIniTar+":"+ minIniTar;
        String horaFinTar = (distribucion.getHSalidaTard()==null)?"":String.format("%02d",distribucion.getHSalidaTard());
        String minFinTar = (distribucion.getMinSalidaTard()==null)?"":String.format("%02d",distribucion.getMinSalidaTard());
        salida += " Salida: "+horaFinTar +":"+minFinTar;
        return salida;
    }

    @GetMapping("/informes/hojaSeguimiento/{idDocumento}/{idDistribucion}/{diaDesde}/{mesDesde}/{anioDesde}/{diaHasta}/{mesHasta}/{anioHasta}")
    public ResponseEntity<byte[]> getHojaSeguimiento(@PathVariable String idDocumento,@PathVariable Long idDistribucion,@PathVariable String diaDesde,
    		@PathVariable String mesDesde, @PathVariable String anioDesde, @PathVariable String diaHasta, @PathVariable String mesHasta, @PathVariable String anioHasta) throws  Exception {

        Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));

        try {

	        Distribucion distribucion = distribucionService.getDistribucion(idDistribucion);
	        	
	        Map<String, Object> parametros = inicializarLogos(documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	        
	        parametros.put("P_CATEG_TIPO_PRACT",documento.getTipoPractica().getCategoria().getCategoria());
	
	        String nomReport = new String("");
	        if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DUAL)) {
	            nomReport = "hoja_seguimiento_semanal_dual.jasper";
	            parametros.put("P_FECHA_FDO","");
	        } else if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_ART_SUPERIOR)){
	            nomReport = "hoja_seguimiento_semanal.jasper";
	            parametros.put("P_TXT_COORDINADOR_TUTOR","TUTOR/A CENTRO DOCENTE:");
	            parametros.put("P_TXT_ENS_CICLO","ENSEÑANZA: ");
	            parametros.put("P_TXT_ESP_CICLO","ESPECIALIDAD: ");
	        }else if(documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DEPORTIVA)){
	            nomReport = "hoja_seguimiento_semanal.jasper";
	            parametros.put("P_TXT_COORDINADOR_TUTOR","TUTOR/A CENTRO DOCENTE: ");
	            parametros.put("P_TXT_ENS_CICLO","CICLO FORMATIVO: ");
	            parametros.put("P_TXT_ESP_CICLO","CLAVE DEL CICLO: ");
	        }else{
	            nomReport = "hoja_seguimiento_semanal.jasper";
	            parametros.put("P_TXT_COORDINADOR_TUTOR","TUTOR/A CENTRO DOCENTE: ");
	            parametros.put("P_TXT_ENS_CICLO","");
	            parametros.put("P_TXT_ESP_CICLO","CLAVE DEL CICLO: ");
	        }
	
	        InputStream hojaSeguimientoReport = getClass().getResourceAsStream("/reports/"+nomReport);
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(hojaSeguimientoReport);
	
	
	
	        String fechaDesde = diaDesde + "/" + mesDesde + "/" + anioDesde;
	        String fechaHasta = diaHasta + "/" + mesHasta + "/" + anioHasta;
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_RANGO_FECHAS", formatoFecha(fechaDesde,fechaHasta));
	
	        List<Distribucion> listaDistribuciones = new ArrayList<Distribucion>();
	        listaDistribuciones.add(distribucion);
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(listaDistribuciones));
	
	        return generarPDF(jasperPrint,documento);
        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
      	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
          }        
    }

    @GetMapping("/informes/hojaSeguimiento/{idDocumento}/{idCentro}/{curso}/{idOferta}/{idPeriodoPractica}/{idUnidad}/{tutor}/{diaDesde}/{mesDesde}/{anioDesde}/{diaHasta}/{mesHasta}/{anioHasta}")
    public ResponseEntity<byte[]> getHojaSeguimiento(@PathVariable String idDocumento,@PathVariable String idCentro,@PathVariable Integer curso,
                                                        @PathVariable String idOferta, @PathVariable Integer idPeriodoPractica,@PathVariable String idUnidad, @PathVariable String tutor,
                                                        @PathVariable String diaDesde, @PathVariable String mesDesde, @PathVariable String anioDesde, 
                                                		@PathVariable String diaHasta, @PathVariable String mesHasta, @PathVariable String anioHasta) throws  Exception{
    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	try {
    		
	    	List<AlumAnexov> listaAlumnos;
	    	
			if(idUnidad.equals("undefined")) {
				if(!tutor.equals("T0")) {
		    		 listaAlumnos = (List<AlumAnexov>) matriculaService.getListaAlumByAnexoByTutor(idCentro,curso,idOferta, idPeriodoPractica,tutor);
		    	}else {
		    		 listaAlumnos = (List<AlumAnexov>) matriculaService.getListaAlumByAnexo(idCentro,curso,idOferta, idPeriodoPractica);
		    	}
			}else {
				if(!tutor.equals("T0")) {
		    		 listaAlumnos = (List<AlumAnexov>) matriculaService.getListaAlumByUnidadAnexoByTutor(idCentro,curso,idOferta, idPeriodoPractica, idUnidad, tutor);
		    	}else {
		    		 listaAlumnos = (List<AlumAnexov>) matriculaService.getListaAlumByUnidadAnexo(idCentro,curso,idOferta, idPeriodoPractica, idUnidad);
		    	}
			}
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	        
	        parametros.put("P_CATEG_TIPO_PRACT",documento.getTipoPractica().getCategoria().getCategoria());
	
	        String nomReport = new String("");
	        if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DUAL)) {
	            nomReport = "hoja_seguimiento_semanal_dual.jasper";
	            parametros.put("P_FECHA_FDO","");
	        } else if (documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_ART_SUPERIOR)){
	            nomReport = "hoja_seguimiento_semanal.jasper";
	            parametros.put("P_TXT_COORDINADOR_TUTOR","TUTOR/A CENTRO DOCENTE:");
	            parametros.put("P_TXT_ENS_CICLO","ENSEÑANZA: ");
	            parametros.put("P_TXT_ESP_CICLO","ESPECIALIDAD: ");
	        }else if(documento.getTipoPractica().getCategoria().getCategoria().equalsIgnoreCase(TP_DEPORTIVA)){
	            nomReport = "hoja_seguimiento_semanal.jasper";
	            parametros.put("P_TXT_COORDINADOR_TUTOR","TUTOR/A CENTRO DOCENTE: ");
	            parametros.put("P_TXT_ENS_CICLO","CICLO FORMATIVO: ");
	            parametros.put("P_TXT_ESP_CICLO","CLAVE DEL CICLO: ");
	        }else{
	            nomReport = "hoja_seguimiento_semanal.jasper";
	            parametros.put("P_TXT_COORDINADOR_TUTOR","TUTOR/A CENTRO DOCENTE: ");
	            parametros.put("P_TXT_ENS_CICLO","");
	            parametros.put("P_TXT_ESP_CICLO","CLAVE DEL CICLO: ");
	        }
	
	        InputStream hojaSeguimientoReport = getClass().getResourceAsStream("/reports/"+nomReport);
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(hojaSeguimientoReport);
	
	        String fechaDesde = diaDesde + "/" + mesDesde + "/" + anioDesde;
	        String fechaHasta = diaHasta + "/" + mesHasta + "/" + anioHasta;
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_RANGO_FECHAS", formatoFecha(fechaDesde,fechaHasta));
	
	        List<Distribucion> listaDistribuciones = new ArrayList<Distribucion>();
	
	        // Carga de las distribuciones de cada alumno
	        for (AlumAnexov a: listaAlumnos){
	            listaDistribuciones.add(distribucionService.getDistribucion( Long.valueOf(a.getIdDistribucion())));
	        }
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(listaDistribuciones));
	
	        return generarPDF(jasperPrint,documento);
	        
		} catch (Exception e) {
			log.error("Error generando informe" + documento.getNombre(), e);
	  	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
	      }        
    }

    @GetMapping("/informes/comisionServicio/{idDocumento}/{idProfesor}/{idCentro}/{anio}/{biMestral}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getComisionServicio(@PathVariable String idDocumento, @PathVariable String idProfesor,
                                                      @PathVariable String idCentro, @PathVariable Integer anio,
                                                      @PathVariable int biMestral,@PathVariable Integer diaFirma,
                                                      @PathVariable Integer mesFirma,@PathVariable Integer anioFirma) throws  Exception{
    	
    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	
    	try {
    	
	    	// Si el bimestral es de Enero a Agosto consultamos sobre el año 2 del curso academico //
	    	Integer anioConsultaVisitas = anio;
	    	if(biMestral<=8) {
	    		anioConsultaVisitas++;
	    	}
	    	
	        List<VisitaTutor> lVisitaTutor = visitaTutorService.getVisitasTutorAnioBiMestral(idProfesor,anioConsultaVisitas,biMestral);
	        // Si no hay visitas se devuelve null para evitar que saque el report vacio
	        //if (lVisitaTutor.size()==0){
	        //    return null;
	        //}
	
	        Profesor profesor = profesorService.getProfesor(idProfesor);
	        Centro centro = centroService.getCentro(idCentro);
	        TipoPractica tipoPractica = documento.getTipoPractica();
	
	        Precios precios = preciosService.getPreciosByTipoPractica(Long.valueOf(documento.getTipoPractica().getIdTipoPractica()));
	
	        InputStream comisionServicio = getClass().getResourceAsStream("/reports/comision_servicio.jasper");
	        InputStream comisionServicioLista = getClass().getResourceAsStream("/reports/comision_servicio_lista_subreport.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(comisionServicio);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	        parametros.put("P_MARCA_AGUA", getMarcaAgua());
	
	        parametros = cargaParametrosComision(parametros,lVisitaTutor);
	
	        parametros.put("P_ENCABEZADO1", documento.getEncabezado());
	        parametros.put("P_CATEG_TIPO_PRACT",(tipoPractica.getCategoria().getCategoria()));
	        parametros.put("P_BIMESTRAL",formatoBimestral(biMestral));
	        parametros.put("P_FECHA_FIRMA", formatearFecha(diaFirma,mesFirma,anioFirma,centro.getLocalidad()));
	        parametros.put("P_LOC_CENTRO", centro.getLocalidad());
	        parametros.put("P_PROFESOR",profesor);
	        parametros.put("P_SECRETARIO",centro.getSecretario());
	        parametros.put("P_PRECIOS",precios);
	        parametros.put("P_ANIO", anioConsultaVisitas.toString());
	        parametros.put("P_CENTRO", centro);
	        parametros.put("P_PROFESOR", profesor);
	        parametros.put("P_TELEFONO", profesor.getTelefono());
	        if(centro.getSecretario()!=null) {
	        	parametros.put("P_SECRETARIO", centro.getSecretario());
	        }
	        cargarParametrosPreciosComision(parametros, lVisitaTutor);
	        
	        parametros.put("P_LISTA_COMISIONES",lVisitaTutor.size() > 0?new JRBeanCollectionDataSource(lVisitaTutor):null);
	        parametros.put("P_SUBREPORT",comisionServicioLista);
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	
	        return generarPDF(jasperPrint,documento);
	        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
     	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
         }        
    }

    // Carga de parámetros calculados para el report Comision Servicio
    private Map<String,Object> cargaParametrosComision(Map<String,Object> parametros, List<VisitaTutor> lVisitaTutor) {

        // Guardar transportes
        parametros.put("tPublico", (lVisitaTutor.stream().filter(vt-> vt.getTransportePublico() == true).findFirst()).isPresent());
        parametros.put("tPrivado", (lVisitaTutor.stream().filter(vt-> vt.getTransportePublico() == false).findFirst()).isPresent());

        return parametros;
    }

	private void cargarParametrosPreciosComision(Map<String, Object> parametros, List<VisitaTutor> lVisitaTutor) {
		// Calcular totales
        Float impKm = Float.valueOf(0);
        Float impTotal = Float.valueOf(0);
        Integer totalKm = Integer.valueOf(0);
        Float totalBilletes = Float.valueOf(0);
        Integer numManutencion = Integer.valueOf(0);

        for ( VisitaTutor vt: lVisitaTutor){
            impKm += vt.getImporteKm();
            impTotal += vt.getImporteTotal();
            totalBilletes += vt.getImporteBilletes() + vt.getImporteOtros();
            totalKm += vt.getKm();

            if (vt.getDietas()){
                numManutencion += 1;
            }
        }

        parametros.put("P_TOTAL_IMPORTE_KM",GepepHelper.redondearDecimalesFloat(impKm,2));
        parametros.put("P_TOTAL_IMPORTE",GepepHelper.redondearDecimalesFloat(impTotal, 2));
        parametros.put("P_TOTAL_BILLETES",totalBilletes);
        parametros.put("P_TOTAL_KM",totalKm);
        parametros.put("P_NUM_MANUTENCION",numManutencion);
	}

    // Formateo fecha Motivos
    private String formatearFecha(Integer diaFirma,Integer mesFirma, Integer anioFirma, String locCentro) {

        return "En "+locCentro+ ", a " + diaFirma + " de " +
                StringUtils.capitalize(Month.of(mesFirma).getDisplayName(TextStyle.FULL, new Locale("es","ES")))
                + " de " +anioFirma;
    }

    private String fechaToString(LocalDate fecha){
        return fecha.getDayOfMonth() + " de "+
                StringUtils.capitalize(fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es","ES")))+
                " de "+fecha.getYear();
    }

    // Formateo texto Bimestral
    private String formatoBimestral ( int bienio){
        return StringUtils.capitalize(Month.of((bienio-1)).getDisplayName(TextStyle.FULL, new Locale("es","ES"))) + "-"+
                StringUtils.capitalize(Month.of(bienio).getDisplayName(TextStyle.FULL, new Locale("es","ES")));
    }

    private String formatoFecha(String f1, String f2){
        return "del "+f1 + " hasta " + f2;
    }

    // función para trocear el contenido de los campos de texto y adaptarlos para el report
    private List<FichaIndividualProg> generaListaFichaIndividualProg ( Distribucion distribucion, String idCiclo, List<ContenidoEF> contenidosEF, TipoPractica tipoPractica){
        	
        
        List<FichaIndividualProg> lista = new ArrayList<FichaIndividualProg>();
        String regex = "[0-9].-";
        
        HashMap<String, ConfigElemFormativo> mapConfigs = new HashMap<>();
        
        List<String> itemsEF1 = new ArrayList<>();
        List<String> itemsEF2 = new ArrayList<>();
        List<String> itemsEF3 = new ArrayList<>();
        List<String> itemsEF4 = new ArrayList<>();
        
        // Detectar elementos formativos ligados: si existe una configuracion no ligada a EF1 los contenidos todos apareceran no ligados //
        boolean configLigadosActiva = true;
        for (ConfigElemFormativo config : tipoPractica.getConfiguracionesEF()) {
            if(!config.getElementoFormativo().getCodigo().equals(EF1_CODIGO) && config.getActivo() && !config.getLigado()) {
        	configLigadosActiva = false;
            }
        }
        
        
        for (ContenidoEF contenidoEF : contenidosEF) {
	    if(contenidoEF.getConfigElemFormativo().getActivo()) {
		
		if(contenidoEF.getConfigElemFormativo().getElementoFormativo().getCodigo().equals(EF1_CODIGO)) {
		    mapConfigs.put(EF1_CODIGO, contenidoEF.getConfigElemFormativo());
		    if(!configLigadosActiva) {
			itemsEF1.add(contenidoEF.getTexto());
		    }else {
			String [] splittedItems = (!StringUtils.isEmpty(contenidoEF.getTexto()) ? contenidoEF.getTexto().split(regex): new String[1]);
			itemsEF1 = new LinkedList<String>(Arrays.asList(splittedItems));
			itemsEF1.remove(0);
		    }
		}
		
		if(contenidoEF.getConfigElemFormativo().getElementoFormativo().getCodigo().equals(EF2_CODIGO)) {
		    mapConfigs.put(EF2_CODIGO, contenidoEF.getConfigElemFormativo());
		    if(!configLigadosActiva) {
			itemsEF2.add(contenidoEF.getTexto());
		    }else {
			String [] splittedItems = (!StringUtils.isEmpty(contenidoEF.getTexto()) ? contenidoEF.getTexto().split(regex): new String[1]);
			itemsEF2 = new LinkedList<String>(Arrays.asList(splittedItems));
			itemsEF2.remove(0);
		    }
		}
		
		if(contenidoEF.getConfigElemFormativo().getElementoFormativo().getCodigo().equals(EF3_CODIGO)) {
		    mapConfigs.put(EF3_CODIGO, contenidoEF.getConfigElemFormativo());
		    if(!configLigadosActiva) {
			itemsEF3.add(contenidoEF.getTexto());
		    }else {
			String [] splittedItems = (!StringUtils.isEmpty(contenidoEF.getTexto()) ? contenidoEF.getTexto().split(regex): new String[1]);
			itemsEF3 = new LinkedList<String>(Arrays.asList(splittedItems));
			itemsEF3.remove(0);
		    }
		}
		
		if(contenidoEF.getConfigElemFormativo().getElementoFormativo().getCodigo().equals(EF4_CODIGO)) {
		    mapConfigs.put(EF4_CODIGO, contenidoEF.getConfigElemFormativo());
		    if(!configLigadosActiva) {
			itemsEF4.add(contenidoEF.getTexto());
		    }else {
			String [] splittedItems = (!StringUtils.isEmpty(contenidoEF.getTexto()) ? contenidoEF.getTexto().split(regex): new String[1]);
			itemsEF4 = new LinkedList<String>(Arrays.asList(splittedItems));
			itemsEF4.remove(0);
		    }
		}
		
	    }
	    
	}

        
        // Calcular lista con mas valores
        List<Integer> listaVal = new ArrayList<Integer>();
        listaVal.add(itemsEF1.size());
        listaVal.add(itemsEF2.size());
        listaVal.add(itemsEF3.size());
        listaVal.add(itemsEF4.size());
        
        for (int i=0;i<Collections.max(listaVal); i++){
            FichaIndividualProg ficha = new FichaIndividualProg();
            
            if ( i < itemsEF1.size()){
        	if(configLigadosActiva) {
        		ficha.setTextoEF1((i+1)+".-"+itemsEF1.get(i));
        	}else {
        	    if(i==0)
        		ficha.setTextoEF1(itemsEF1.get(0));
        	}
            }
            if ( i < itemsEF2.size()){
        	if(configLigadosActiva) {
        		ficha.setTextoEF2((i+1)+".-"+itemsEF2.get(i));
        	}else {
        	    if(i==0)
        		ficha.setTextoEF2(itemsEF2.get(0));
        	}
            }
            if ( i < itemsEF3.size()){
        	if(configLigadosActiva) {
        		ficha.setTextoEF3((i+1)+".-"+itemsEF3.get(i));
        	}else {
        	    if(i==0)
        		ficha.setTextoEF3(itemsEF3.get(0));
        	}
            }
            if ( i < itemsEF4.size()){
        	if(configLigadosActiva) {
        		ficha.setTextoEF4((i+1)+".-"+itemsEF4.get(i));
        	}else {
        	    if(i==0)
        		ficha.setTextoEF4(itemsEF4.get(0));
        	}
            }

            lista.add(ficha);
        }
        
        // Elementos Formativos //
        
        
        return lista;
    }
    
    public static <T> List<T> convertArrayToList(T array[]) 
    { 
  
        // Create the List by passing the Array 
        // as parameter in the constructor 
        List<T> list = Arrays.asList(array); 
        return list; 
    } 

    @GetMapping("/informes/visitasAutorizadas/{idDocumento}/{idProfesor}/{idCentro}/{idCiclo}/{mes}/{curso}/{idTipoPractica}")
    public ResponseEntity<byte[]> getVisitasAutorizadas(@PathVariable String idDocumento,@PathVariable String idProfesor,@PathVariable String idCentro,@PathVariable String idCiclo, @PathVariable Integer mes, @PathVariable Integer curso, @PathVariable Long idTipoPractica ) throws  Exception{

    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	
    	try {
    	
	    	//Obtenemos la lista de las visitas del tutor , solo las autorizadas (true)
	
	        List<VisitaTutor> listaGastosTutor = (List<VisitaTutor>) visitaTutorService.getVisitaTutoriaByProfesorAndAutorizadaAndMesAndAnioAndTipoPractica(idProfesor, true, mes, curso, idTipoPractica);
	
	        Profesor profesor = profesorService.getProfesor(idProfesor);
	        Centro centro = centroService.getCentro(idCentro);
	        Ciclo ciclo = cicloService.getCiclo(idCiclo);
	        InputStream visitasAutorizadasReport = getClass().getResourceAsStream("/reports/visitasAutorizadas.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(visitasAutorizadasReport);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	     // Marca de agua
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	
	        parametros.put("P_ENCABEZADO1",(documento.getEncabezado()==null)?"":documento.getEncabezado());
	        parametros.put("P_ENCABEZADO2",(documento.getEncabezado2()==null)?"":documento.getEncabezado2());
	
	        parametros.put("P_NOMBRE",(profesor.getNombre()==null)?"":profesor.getNombre());
	        parametros.put("P_APELLIDO1",(profesor.getApellido1()==null)?"":profesor.getApellido1());
	        parametros.put("P_APELLIDO2",(profesor.getApellido2()==null)?"":profesor.getApellido2());
	        parametros.put("P_CODCEN",(centro.getCodigo()==null)?"":centro.getCodigo());
	        parametros.put("P_IDCICLO",ciclo.getCodigo() );
	        parametros.put("P_NOMBRECENTRO",(centro.getNombre()==null)?"":centro.getNombre());
	        parametros.put("P_MES", mes.toString());
	        parametros.put("P_ANIO", curso.toString());
	
	        parametros.put("tabla_gastos",new JRBeanCollectionDataSource(listaGastosTutor));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(listaGastosTutor));
	
	        return generarPDF(jasperPrint,documento);
        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    @GetMapping("/informes/programacionVisitas/{idDocumento}/{idProfesor}/{idCentro}/{idCiclo}/{anio}/{mes}/{diaFirma}/{mesFirma}/{anioFirma}/{idTipoPractica}/{localidad}")
    public ResponseEntity<byte[]> getProgramacionVisitasPrevistas(@PathVariable String idDocumento, @PathVariable String idProfesor,
                                                                  @PathVariable String idCentro, @PathVariable String idCiclo,                                                              
                                                                  @PathVariable Integer anio, @PathVariable Integer mes, @PathVariable Integer diaFirma,
                                                                  @PathVariable Integer mesFirma, @PathVariable Integer anioFirma, @PathVariable Long idTipoPractica, @PathVariable String localidad) throws  Exception{
    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	
    	try {
 
	        List<VisitaTutor> lVisitaTutor = (List<VisitaTutor>) visitaTutorService.getVisitaTutoriaByProfesorAndMesAndAnioAndTipoPractica(idProfesor, mes, anio, idTipoPractica);
	
	        Profesor profesor = profesorService.getProfesor(idProfesor);
	        Centro centro = centroService.getCentro(idCentro);
	        Ciclo ciclo = cicloService.getCiclo(idCiclo);	       
	        TipoPractica tipoPractica = tipoPracticaService.getTipoPractica(idTipoPractica);
	        
	        Precios precios = preciosService.getPreciosByTipoPractica(idTipoPractica);
	        
	        InputStream visitasPrevistasReport = getClass().getResourceAsStream("/reports/programacion_visitas.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(visitasPrevistasReport);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	        
	        parametros.put("P_LOCALIDAD",localidad);
	        parametros.put("P_CATEG_TIPO_PRACT",(tipoPractica.getCategoria().getCategoria()));
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_CENTRO", centro);
	        parametros.put("P_PROFESOR",profesor);
	        parametros.put("P_CICLO",ciclo);
	        
	        List<OfertaFormativa> lOfertas = new ArrayList<OfertaFormativa>();
	        
	        if (tipoPractica.getCategoria().getCategoria().equals(TP_DEPORTIVA)) {	        	
	        	lOfertas = ofertaFormativaService.getAllOfertaFormativasByCiclo(ciclo.getIdCiclo());
	        }
	        	
	        if(!lOfertas.isEmpty())
	        	 parametros.put("P_OFERTA",lOfertas.get(0));
	        else parametros.put("P_OFERTA","");	        
	
	        parametros.put("P_MES",mes+"/"+anio);
	        parametros.put("P_FECHA_FIRMA", diaFirma+"/"+mesFirma+"/"+anioFirma);
	        parametros.put("P_PRECIO_KM",precios.getPrecioKm());
	
	        parametros.put("P_VISOR",tipoPractica.getVisor());
	        parametros.put("P_IMPORTE_TOTAL", GepepHelper.redondearDecimalesFloat( new Float(lVisitaTutor.stream().mapToDouble(vt -> vt.getImporteTotal()).sum()),2));

	        parametros.put("P_ENCABEZADO_FIRMA",(documento.getEncabezadoFirma()==null)?"":documento.getEncabezadoFirma());
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lVisitaTutor));
	
	        return generarPDF(jasperPrint,documento);
        
	    } catch (Exception e) {
	    	log.error("Error generando informe" + documento.getNombre(), e);
		     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
	    }       
    }

    @GetMapping("/informes/gastosProfesorado/{idDocumento}/{idCentro}/{anio}/{idPeriodoLiq}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}/{idTipoPractica}")
    public ResponseEntity<byte[]> getGastosProfesorado(@PathVariable String idDocumento,@PathVariable String idCentro,
                                                       @PathVariable Integer anio, @PathVariable Integer idPeriodoLiq,
                                                       @PathVariable String localidad,@PathVariable Integer diaFirma,
                                                       @PathVariable Integer mesFirma, @PathVariable Integer anioFirma, @PathVariable Long idTipoPractica) throws  Exception{

    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	
    	try {
    		
	        PeriodoLiquidacion periodoLiquidacion = periodoLiquidacionService.getperiodoLiquidacion(idPeriodoLiq);
	        TipoPractica tipoPractica = documento.getTipoPractica();
	        
	        // Semestres
	        Integer iniMes = periodoLiquidacion.getMesIni();
	        Integer finMes = periodoLiquidacion.getMesFin();
	
	        List<GastosProfesorReport> lGastosProfesoradoReport =  getListaGastosProfesorado(idCentro, anio,iniMes,finMes, tipoPractica);
	
	        if(lGastosProfesoradoReport.size() == 0){
	            return null;
	        }
	        
	
	        Centro centro = centroService.getCentro(idCentro);
	
	        InputStream gastosProfesorado = getClass().getResourceAsStream("/reports/gastos_profesorado.jasper");
	
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(gastosProfesorado);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_CENTRO", centro);
	        parametros.put("P_SEMESTRE",formatoSemestre(iniMes,finMes,anio ));
	        parametros.put("P_FECHA_FIRMA", formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	        parametros.put("P_CATEG_TIPO_PRACT",(tipoPractica.getCategoria().getCategoria()));
	        parametros.put("P_GASTO_TOTAL", GepepHelper.redondearDecimalesFloat( new Float(lGastosProfesoradoReport.stream().mapToDouble(gp -> gp.getTotalGastos()).sum()),2));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lGastosProfesoradoReport));
	
	        return generarPDF(jasperPrint,documento);
        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }      
     
    }

    private String formatoSemestre(Integer iniMes, Integer finMes, Integer anio) {
        String semestre = new String("");

        semestre += StringUtils.capitalize(Month.of(iniMes).getDisplayName(TextStyle.FULL, new Locale("es","ES")));
        semestre += " - " + StringUtils.capitalize(Month.of(finMes).getDisplayName(TextStyle.FULL, new Locale("es","ES")));
        semestre += " (" + anio + "-" + (anio+1) + ")";
        return semestre;
    }

    private List<GastosProfesorReport> getListaGastosProfesorado(String idCentro, Integer anio, Integer iniMes, Integer finMes, TipoPractica tipoPractica) {
        List<GastosProfesorReport> lGastosProfesoradoReport = new ArrayList<GastosProfesorReport>();

        List<TutorCentro> lProfesores = new ArrayList<TutorCentro>();
        //FP: para deportivas no se recupera ni el nombre ni el codigo del ciclo, sino el de la oferta. Para el resto de prácticas nombre y codigo de ciclo
        //recupero categoria de tipopractica para saber a qué metodo llamar.
        // recuperar la lista de profesores por año y centro

        if(tipoPractica.getCategoria().getCategoria().equalsIgnoreCase(TP_DEPORTIVA))
        	lProfesores = profesorService.getTutoresByCentroAndAnioAndTipoPracticaDeportiva(idCentro, anio, tipoPractica.getIdTipoPractica());
        else
        	lProfesores = profesorService.getTutoresByCentroAndAnioAndTipoPractica(idCentro, anio, tipoPractica.getIdTipoPractica());

        // Limpiar la lista para eliminar idProfesor repetidos
        List<TutorCentro> lFiltrada =lProfesores.stream().filter(distinctByCampo(p -> p.getIdProfesor() )).collect(Collectors.toList());

        for(TutorCentro p: lFiltrada){
            // Buscar si tiene visitas realizadas:
        	
        	// Hay que tener en cuenta los semestres del curso para consultar las visitas //
       
        	List<VisitaTutor> lvisitasTutor;
        	
        	//INCIDENCIA INTEGRACIÓN : GASTOS DE PROFESORADO - SEMESTRES NATURAL
        	/*// Primer Semestre //
        	Integer anioSemestre1 = anio;
        	Integer mesIniSemestre1 = 9;
        	Integer mesFinSemestre1 = 12;
        	
        	// Segundo Semestre //
        	Integer anioSemestre2 = anio+1; 
        	Integer mesIniSemestre2 = 1;
        	Integer mesFinSemestre2 = 8;*/
        	
        	// Primer Semestre //
        	Integer anioSemestre1 = anio+1;
        	Integer mesIniSemestre1 = 1;
        	Integer mesFinSemestre1 = 6;
        	
        	// Segundo Semestre //
        	Integer anioSemestre2 = anio; 
        	Integer mesIniSemestre2 = 7;
        	Integer mesFinSemestre2 = 12;
        	
        	// Si iniMes en semestre1 //
        	if(iniMes>=mesIniSemestre1 && iniMes<=mesFinSemestre1) {
        		
        		// Si ademas finMes en semestre1 //
        		if(finMes>=mesIniSemestre1 && finMes<=mesFinSemestre1) {
        			lvisitasTutor = visitaTutorService.getVisitasAutorizadasRealizadasTutorAnioTipoPracticaPeriodoLiquidacion(p.idProfesor,anioSemestre1,p.getIdCiclo(),iniMes,finMes,tipoPractica.getIdTipoPractica());
        		}else {
        			// Entonces iniMes esta en semestre1 y el finMes en semestre2 (hay que consultar en ambos años) //
        			lvisitasTutor = visitaTutorService.getVisitasAutorizadasRealizadasTutorAnioTipoPracticaPeriodoLiquidacion(p.idProfesor,anioSemestre1,p.getIdCiclo(),iniMes,mesFinSemestre1,tipoPractica.getIdTipoPractica());        			
        			lvisitasTutor.addAll(visitaTutorService.getVisitasAutorizadasRealizadasTutorAnioTipoPracticaPeriodoLiquidacion(p.idProfesor,anioSemestre2,p.getIdCiclo(),mesIniSemestre2,finMes,tipoPractica.getIdTipoPractica()));
        		}
        		
        	}else {
        		// iniMes en semestre2 //
        		
        		// Si ademas finMes en semestre2 //
        		if(finMes>=mesIniSemestre2 && finMes<=mesFinSemestre2) {
        			lvisitasTutor = visitaTutorService.getVisitasAutorizadasRealizadasTutorAnioTipoPracticaPeriodoLiquidacion(p.idProfesor,anioSemestre2,p.getIdCiclo(),iniMes,finMes,tipoPractica.getIdTipoPractica());
        		}else {
        			lvisitasTutor = visitaTutorService.getVisitasAutorizadasRealizadasTutorAnioTipoPracticaPeriodoLiquidacion(p.idProfesor,anioSemestre2,p.getIdCiclo(),iniMes,mesFinSemestre2,tipoPractica.getIdTipoPractica());
        		}
        		
        	}
        

            if ( lvisitasTutor.size()>0){
                GastosProfesorReport gastosProfesorReport = new GastosProfesorReport();

                gastosProfesorReport.setNombreProfesor(p.getNombre());
                gastosProfesorReport.setApellido1Profesor(p.getApellido1());
                gastosProfesorReport.setApellido2Profesor(p.getApellido2());
                gastosProfesorReport.setNombreCiclo(p.getNombreCiclo());
                gastosProfesorReport.setCodigoCiclo(p.getCodCiclo());
                gastosProfesorReport.setIdCiclo(p.getIdCiclo());

                Float sumTransPublico = Float.valueOf(0);
                Float sumTransPrivado = Float.valueOf(0);
                Float sumTransDietas = Float.valueOf(0);
                Float sumTransOtros = Float.valueOf(0);
                Float sumTransGastos = Float.valueOf(0);

                for(VisitaTutor vt: lvisitasTutor){
                    sumTransPublico += vt.getImporteBilletes() ;
                    sumTransPrivado += vt.getImporteKm();
                    sumTransDietas += vt.getImporteDietas();
                    sumTransOtros += vt.getImporteOtros();
                    sumTransGastos += vt.getImporteTotal();
                }

                gastosProfesorReport.setTotalTransportePublico(GepepHelper.redondearDecimalesFloat(sumTransPublico,2));
                gastosProfesorReport.setTotalTransportePrivado(GepepHelper.redondearDecimalesFloat(sumTransPrivado,2));
                gastosProfesorReport.setTotalDietas(GepepHelper.redondearDecimalesFloat(sumTransDietas,2));
                gastosProfesorReport.setTotalOtros(GepepHelper.redondearDecimalesFloat(sumTransOtros,2));
                gastosProfesorReport.setTotalGastos(GepepHelper.redondearDecimalesFloat(sumTransGastos,2));

                lGastosProfesoradoReport.add(gastosProfesorReport);
            }
        }

        return lGastosProfesoradoReport;
    }

	

    // Método de filtrado personalizado para limpiar duplicados por atributo
    private static <T> Predicate<T> distinctByCampo(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @GetMapping("/informes/gastosAlumnos/{idDocumento}/{idCentro}/{idCiclo}/{periodo}/{curso}/{regimen}/{grupo}/{anio}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getGastosAlumnos(@PathVariable String idDocumento,@PathVariable String idCentro,
                                                   @PathVariable String idCiclo,@PathVariable Integer periodo,
                                                   @PathVariable Integer curso,
                                                   @PathVariable String regimen,@PathVariable String grupo,
                                                   @PathVariable Integer anio,@PathVariable String localidad, @PathVariable Integer diaFirma,
                                                   @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{

    	
    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	
    	try {

            /*Gastos de tipo 1 en el periodo seleccionado*/

	    	List<GastosAlumnosReport> lGastosAlumnoReports =  getListaGastosAlumnos(idCentro, idCiclo, periodo,curso,regimen,grupo,anio);
	
	        if(lGastosAlumnoReports.size() == 0){
	            return null;
	        }
	
	        Centro centro = centroService.getCentro(idCentro);
	        Ciclo ciclo = cicloService.getCiclo(idCiclo);
	        PeriodoLiquidacion periodoLiquidacion = periodoLiquidacionService.getperiodoLiquidacion(periodo);
	        Matricula matricula = matriculaService.getMatricula(lGastosAlumnoReports.get(0).getGastoAlumno().getMatricula().getIdMatricula());
	
	        InputStream gastosAlumnado = getClass().getResourceAsStream("/reports/gastos_alumnado.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(gastosAlumnado);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	        parametros.put("P_MARCA_AGUA", getMarcaAgua());
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_CENTRO", centro);
	        parametros.put("P_CICLO", ciclo);
	        parametros.put("P_SEMESTRE",periodoLiquidacion.getNombre());
	        parametros.put("P_FECHA_FIRMA", formatearFecha(diaFirma,mesFirma,anioFirma,centro.getLocalidad()));
	        parametros.put("P_CATEG_TIPO_PRACT",(documento.getTipoPractica().getCategoria().getCategoria()));
	        if(matricula.getUnidad() != null && matricula.getUnidad().getTutor() != null) {
		        String nombreTutor =  matricula.getUnidad().getTutor().getApellido1() + " " +  matricula.getUnidad().getTutor().getApellido2() + ", "+ matricula.getUnidad().getTutor().getNombre();
		        parametros.put("P_TUTOR",nombreTutor);
	        }
	        parametros.put("P_CURSO_FORMATO", new String(anio+"-"+(anio+1)));
	        parametros.put("P_GASTO_TOTAL", GepepHelper.redondearDecimalesFloat(new Float(lGastosAlumnoReports.stream().mapToDouble(gp -> gp.getTotalGastos()).sum()),2));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lGastosAlumnoReports));
	
	        return generarPDF(jasperPrint,documento);
        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
       }       

    }

    @GetMapping("/informes/gastosAlumnos2/{idDocumento}/{idCentro}/{idCiclo}/{periodo}/{curso}/{regimen}/{grupo}/{anio}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getGastosAlumnos2(@PathVariable String idDocumento,@PathVariable String idCentro,
                                                   @PathVariable String idCiclo,@PathVariable Integer periodo,
                                                   @PathVariable Integer curso,
                                                   @PathVariable String regimen,@PathVariable String grupo,
                                                   @PathVariable Integer anio,@PathVariable String localidad, @PathVariable Integer diaFirma,
                                                   @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{


        Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));

        try {

           /*solo se consideran gastos para periodos de tipo 2 y validados*/

            List<GastosAlumnosReport> lGastosAlumnoReports =  getListaGastosAlumnosTipo2(idCentro, idCiclo, periodo,curso,regimen,grupo,anio);

            if(lGastosAlumnoReports.size() == 0){
                return null;
            }

            Centro centro = centroService.getCentro(idCentro);
            Ciclo ciclo = cicloService.getCiclo(idCiclo);
            PeriodoLiquidacion periodoLiquidacion = periodoLiquidacionService.getperiodoLiquidacion(periodo);
            Matricula matricula = matriculaService.getMatricula(lGastosAlumnoReports.get(0).getGastoAlumno().getMatricula().getIdMatricula());

            InputStream gastosAlumnado = getClass().getResourceAsStream("/reports/gastos_alumnado2.jasper");

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(gastosAlumnado);

            Map<String, Object> parametros = inicializarLogos(documento);

            // Marca de agua
            parametros.put("P_MARCA_AGUA", getMarcaAgua());

            parametros.put("P_DOCUMENTO",documento);
            parametros.put("P_CENTRO", centro);
            parametros.put("P_CICLO", ciclo);
            parametros.put("P_SEMESTRE",periodoLiquidacion.getNombre());
            parametros.put("P_FECHA_FIRMA", formatearFecha(diaFirma,mesFirma,anioFirma,centro.getLocalidad()));
            parametros.put("P_CATEG_TIPO_PRACT",(documento.getTipoPractica().getCategoria().getCategoria()));
            if(matricula.getUnidad() != null && matricula.getUnidad().getTutor() != null) {
                String nombreTutor =  matricula.getUnidad().getTutor().getApellido1() + " " +  matricula.getUnidad().getTutor().getApellido2() + ", "+ matricula.getUnidad().getTutor().getNombre();
                parametros.put("P_TUTOR",nombreTutor);
            }
            parametros.put("P_CURSO_FORMATO", new String(anio+"-"+(anio+1)));
            parametros.put("P_GASTO_TOTAL", GepepHelper.redondearDecimalesFloat(new Float(lGastosAlumnoReports.stream().mapToDouble(gp -> gp.getTotalGastos()).sum()),2));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lGastosAlumnoReports));

            return generarPDF(jasperPrint,documento);

        } catch (Exception e) {
            log.error("Error generando informe" + documento.getNombre(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }

    }
    private List<GastosAlumnosReport> getListaGastosAlumnos(String idCentro, String idCiclo, Integer periodo, Integer curso,
                                                            String regimen, String grupo,Integer anio) {
        List<GastosAlumnosReport> lTotalGastosAlumnoReports = new ArrayList<GastosAlumnosReport>();

        // formateo del curso a partir del año de inicio
        // String anioAcademico = anio + "-"+(anio+1);
        //CRQ000000561434
        //List<GastoAlumno> lGastoAlumnos = gastoAlumnoService.getGastoAlumnoCurso(idCentro,idCiclo,periodo,curso,regimen,grupo,anioAcademico);
        List<GastoAlumno> lGastoAlumnos = gastoAlumnoService.getGastoAlumnoCursoNuevo(idCentro,idCiclo,periodo,curso,regimen,grupo,anio,1);
        for ( GastoAlumno ga: lGastoAlumnos){
            if (lGastoAlumnos.size()>0){
                GastosAlumnosReport gastosAlReport = new GastosAlumnosReport();

                Alumno a = ga.getMatricula().getAlumno();
                gastosAlReport.setNombreFormateado(new String(a.getApellido1()+ " "+a.getApellido2()+", "+a.getNombre()));

                gastosAlReport.setGastoAlumno(ga);
                if (ga.getDistanciaUnitaria() != null && ga.getDistanciaUnitaria() >0 && ga.getNumeroDias() != null &&  ga.getNumeroDias()>0) {
                        gastosAlReport.setTotalGastos(importesTipoGasto2Service.getImporteByKilometros(ga.getdistanciaTotal2()).floatValue());
                }
                else {
                    gastosAlReport.setTotalGastos(ga.calculaTotalGastos());
                }
                log.debug("************precio km :: " + ga.getPrecioKm().toString());

                lTotalGastosAlumnoReports.add(gastosAlReport);
            }
        }
        return lTotalGastosAlumnoReports;
    }

    private List<GastosAlumnosReport> getListaGastosAlumnosTipo2(String idCentro, String idCiclo, Integer periodo, Integer curso,
                                                            String regimen, String grupo,Integer anio) {
        List<GastosAlumnosReport> lTotalGastosAlumnoReports = new ArrayList<GastosAlumnosReport>();

         List<GastoAlumno> lGastoAlumnos = gastoAlumnoService.getGastoAlumnoCursoNuevo(idCentro,idCiclo,periodo,curso,regimen,grupo,anio,2);
        for ( GastoAlumno ga: lGastoAlumnos){
            if (lGastoAlumnos.size()>0){
                GastosAlumnosReport gastosAlReport = new GastosAlumnosReport();

                Alumno a = ga.getMatricula().getAlumno();
                gastosAlReport.setNombreFormateado(new String(a.getApellido1()+ " "+a.getApellido2()+", "+a.getNombre()));

                DistribucionPeriodo dp = distribucionPeriodoService.getDistribucionPeriodo(ga.getDistribucionPeriodo().getIdDistribucionPeriodo());
                Distribucion dist = dp.getDistribucion();
                gastosAlReport.setNombreCentroTrabajo(new String(dist.getCentro().toString().trim()));
                gastosAlReport.setGastoAlumno(ga);
                if (ga.getDistanciaUnitaria() != null && ga.getDistanciaUnitaria() >0 && ga.getNumeroDias() != null &&  ga.getNumeroDias()>0) {
                    gastosAlReport.setTotalGastos(importesTipoGasto2Service.getImporteByKilometros(ga.getdistanciaTotal2()).floatValue());
                }
                else {
                    gastosAlReport.setTotalGastos(ga.calculaTotalGastos());
                }
                log.debug("************precio km :: " + ga.getPrecioKm().toString());

                lTotalGastosAlumnoReports.add(gastosAlReport);
            }
        }
        return lTotalGastosAlumnoReports;
    }

    @GetMapping("/informes/becas/{idDocumento}/{idDistribucion}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getDocumentoBecas(@PathVariable Integer idDocumento, @PathVariable Integer idDistribucion,
                                                    @PathVariable String localidad,@PathVariable Integer diaFirma,
                                                    @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{

        Documento documento = documentoService.getDocumento(idDocumento.longValue());

        try {
        	
	        Distribucion dis = distribucionService.getDistribucion(idDistribucion.longValue());
	        Float importe = Float.valueOf(1500); // TODO: quitar valores de pega
	        Float importeMensual = Float.valueOf(560);
	
	        InputStream documentoBecas = getClass().getResourceAsStream("/reports/becas.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(documentoBecas);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	        parametros.put("P_MARCA_AGUA", getMarcaAgua());
	        parametros.put("P_DOCUMENTO",documento);
	
	        // cargar textos estáticos
	        parametros = cargaPropertiesReport("becas",parametros);
	        // carga de textos con variables
	        String[] params = new String[]{dis.getAnexoContrato().getConvenio().getArea().getEmpresa().getNombre(),
	                                        dis.getAnexoContrato().getOfertaCentro().getCentro().getNombre(),
	                                        dis.getAnexoContrato().getOfertaCentro().getCentro().getLocalidad(),
	                                        dis.getAnexoContrato().getOfertaCentro().getOferta().getCiclo().getIdCiclo(),
	                                        dis.getAnexoContrato().getOfertaCentro().getOferta().getCiclo().getNombre()};
	        parametros.put("P_PARRAFO1",reportsSource.getMessage("becas.p.parrafo1",params,LocaleContextHolder.getLocale()));
	
	        params = new String[]{importe.toString()};
	        parametros.put("P_PARRAFO5",reportsSource.getMessage("becas.p.parrafo5",params,LocaleContextHolder.getLocale()));
	        params = new String[]{importeMensual.toString()};
	        parametros.put("P_PARRAFO6",reportsSource.getMessage("becas.p.parrafo6",params,LocaleContextHolder.getLocale()));
	        params = new String[]{importe.toString()};
	        parametros.put("P_PARRAFO7",reportsSource.getMessage("becas.p.parrafo7",params,LocaleContextHolder.getLocale()));
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	        params = new String[]{dis.getTutorEmpresa()};
	        parametros.put("P_FDO",reportsSource.getMessage("becas.p.fdo",params,LocaleContextHolder.getLocale()));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       

    }

    @GetMapping("/informes/declaracionResponsable/{idDocumento}/{idArea}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getDeclaracionResponsable(@PathVariable Integer idDocumento,
                                                           @PathVariable Long idArea,
                                                            @PathVariable String localidad,@PathVariable Integer diaFirma,
                                                            @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{

        Documento documento = documentoService.getDocumento(idDocumento.longValue());

        try {
        	
	        Area area = areaService.getArea(idArea);
	
	        InputStream declaracionResponsable = getClass().getResourceAsStream("/reports/declaracion_responsable.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(declaracionResponsable);
	
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	
	        parametros = cargaPropertiesReport("decresp",parametros);
	        List<ResponsableArea> responsable = (List<ResponsableArea>) responsableAreaService.getResponsablByArea(area.getIdArea());
	
	        // Listado de par�metros para recoger los textos con variables
	        // Map<String,String> paramTexto = new HashMap<String, String>();
	        String[] params = new String[]{responsable.size() >0 ?responsable.get(0).getNombre(): " ",
	                responsable.size()>0 ?responsable.get(0).getNif(): " ",
	                !StringUtils.isEmpty(area.getEmpresa().getNombre())? area.getEmpresa().getNombre() : "",
	                !StringUtils.isEmpty(area.getEmpresa().getCif()) ? area.getEmpresa().getCif():" ",
	                !StringUtils.isEmpty(area.getDireccion()) ?area.getDireccion(): " "};
	        parametros.put("P_PARRAFO1",reportsSource.getMessage("decresp.p.parrafo1",params,LocaleContextHolder.getLocale()));
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	        params = new String[]{responsable.get(0).getNombre()};
	        parametros.put("P_FDO",reportsSource.getMessage("decresp.p.fdo",params,LocaleContextHolder.getLocale()));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	
	        return generarPDF(jasperPrint,documento);
	        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    @GetMapping("/informes/compromisoAceptacion/{idDocumento}/{idDistribucion}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getCompromisoAceptacion(@PathVariable Integer idDocumento,
                                                            @PathVariable Integer idDistribucion,@PathVariable String localidad,
                                                            @PathVariable Integer diaFirma,
                                                            @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{

        Documento documento = documentoService.getDocumento(idDocumento.longValue());
       
        try {
        	
	        Distribucion distribucion = distribucionService.getDistribucion(idDistribucion.longValue());
	        //OfertaFormativa oferta = oferta
	
	        InputStream compromisoAceptacion = getClass().getResourceAsStream("/reports/compromiso_aceptacion.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(compromisoAceptacion);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	        parametros.put("P_MARCA_AGUA", getMarcaAgua());
	        parametros.put("P_DOCUMENTO",documento);
	
	        parametros = cargaPropertiesReport("compacep",parametros);
	        String[] cursoAcademico = distribucion.getAnexoContrato().getCursoAcademico().split("-");
	
	        String[] params = new String[]{(distribucion.getMatricula().getAlumno().getNombre() + " " +distribucion.getMatricula().getAlumno().getApellido1() + " "+ distribucion.getMatricula().getAlumno().getApellido2()),
	                distribucion.getMatricula().getAlumno().getNif(),
	                distribucion.getMatricula().getOfertaCentro().getOferta().getCiclo().getCodigo(),
	                distribucion.getMatricula().getOfertaCentro().getOferta().getNombre(),
	                (cursoAcademico[0] + " / "+ cursoAcademico[1]),
	                distribucion.getMatricula().getOfertaCentro().getCentro().getNombre(),
	                distribucion.getMatricula().getOfertaCentro().getCentro().getLocalidad(),
	                distribucion.getMatricula().getOfertaCentro().getCentro().getDireccion(),
	                distribucion.getMatricula().getOfertaCentro().getCentro().getCp()};
	
	        parametros.put("P_PARRAFO1",reportsSource.getMessage("compacep.p.parrafo1",params,LocaleContextHolder.getLocale()));
	        // TODO completar tutor y formato matriculacion
	        params = new String []{ "", ""}; // tutor legal
	        parametros.put("P_PARRAFO3",reportsSource.getMessage("compacep.p.parrafo3",params,LocaleContextHolder.getLocale()));
	//            int anioInicio = distribucion.getMatricula().getOfertaCentro().getOferta().getAnioInicio();
	//            int anioFin = distribucion.getMatricula().getOfertaCentro().getOferta().getAnioFin();
	//            params = new String []{ anioInicio + " / "+(anioInicio+1) , anioFin + " / "+(anioFin+1)}; // anios
	        params = new String []{ "", ""};
	        parametros.put("P_PARRAFO4",reportsSource.getMessage("compacep.p.parrafo4",params,LocaleContextHolder.getLocale()));
	        Area area = distribucion.getAnexoContrato().getConvenio().getArea();
	        params = new String []{ area.getEmpresa().getNombre(),
	                                area.getLocalidad(),
	                                area.getProvincia(),
	                                area.getDireccion(),
	                                area.getCp(),
	                                area.getEmpresa().getCif()}; // empresa
	        parametros.put("P_PARRAFO5",reportsSource.getMessage("compacep.p.parrafo5",params,LocaleContextHolder.getLocale()));
	
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	        params = new String[]{distribucion.getMatricula().getAlumno().getNombre() + " " +distribucion.getMatricula().getAlumno().getApellido1() + " "+ distribucion.getMatricula().getAlumno().getApellido2()};
	        parametros.put("P_FDO_ALU",reportsSource.getMessage("compacep.p.fdo",params,LocaleContextHolder.getLocale()));
	        params = new String[]{""};
	        parametros.put("P_FDO_TUTOR",reportsSource.getMessage("compacep.p.fdo",params,LocaleContextHolder.getLocale()));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    @GetMapping("/informes/valoracionDesarrollo/{idDocumento}/{idConvenio}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getValoracionDesarrollo(@PathVariable Integer idDocumento,
                                                          @PathVariable Long idConvenio,@PathVariable String localidad,
                                                          @PathVariable Integer diaFirma,
                                                          @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{

        Documento documento = documentoService.getDocumento(idDocumento.longValue());
        
        try {
        	
	        Convenio convenio = convenioService.getConvenio(idConvenio);
	
	        InputStream valoracionDesarrollo = getClass().getResourceAsStream("/reports/valoracion_desarrollo.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(valoracionDesarrollo);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	        parametros.put("P_MARCA_AGUA", getMarcaAgua());
	        parametros.put("P_DOCUMENTO",documento);
	
	        parametros = cargaPropertiesReport("valdesa",parametros);
	
	        String[] params = new String[]{convenio.getCentro().getDirector()};
	        parametros.put("P_FDO_TUTOR_CEN",reportsSource.getMessage("valdesa.p.fdo",params,LocaleContextHolder.getLocale()));
	
	        List<ResponsableArea> responsableAreaList =  (List<ResponsableArea>) responsableAreaService.getResponsablByArea(convenio.getArea().getIdArea());
	
	        params = new String []{ !responsableAreaList.isEmpty()? responsableAreaList.get(0).getNombre() : ""};
	        parametros.put("P_FDO_TUTOR_EMP",reportsSource.getMessage("valdesa.p.fdo",params,LocaleContextHolder.getLocale()));
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       

    }

    @GetMapping("/informes/fichaValoracionDesarrollo/{idDocumento}/{idDistribucion}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getFichaValoraiconDesarrollo(
                                                    @PathVariable Integer idDocumento, @PathVariable Integer idDistribucion,
                                                    @PathVariable String localidad,@PathVariable Integer diaFirma,
                                                    @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{

        Documento documento = documentoService.getDocumento(idDocumento.longValue());
        
        try {
        
	        Distribucion distribucion = distribucionService.getDistribucion(idDistribucion.longValue());
	
	        InputStream fichaValDesarrollo = getClass().getResourceAsStream("/reports/ficha_valoracion_desarrollo_proyecto.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fichaValDesarrollo);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	
	        // Marca de agua
	        parametros.put("P_MARCA_AGUA", getMarcaAgua());
	        parametros.put("P_DOCUMENTO",documento);
	
	        parametros.put("P_DISTRIBUCION",distribucion);
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       

    }

    @GetMapping("/informes/certificadoEstudios/{idDocumento}/{idDistribucion}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getCertificadoEstudios(@PathVariable String idDocumento,@PathVariable Long idDistribucion,
                                                         @PathVariable String localidad, @PathVariable Integer diaFirma,
                                                         @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception{

    	Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
    	
    	try {
    	
	        Distribucion distribucion = distribucionService.getDistribucion(idDistribucion);
	
	        InputStream certificadoEstudiosReport = getClass().getResourceAsStream("/reports/certificado_estudios.jasper");
	        InputStream realizacionesSubreport = getClass().getResourceAsStream("/reports/certificado_estudios_realizaciones_subreport.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(certificadoEstudiosReport);
	
	        Map<String, Object> parametros = inicializarLogos(documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	
	        parametros.put("P_DOCUMENTO",documento);
	
	        parametros = cargaPropertiesReport("certestud",parametros);
	
	        String[] params = new String[]{distribucion.getMatricula().getOfertaCentro().getCentro().getSecretario(),
	                distribucion.getCentro()};
	        parametros.put("P_PARRAFO1",reportsSource.getMessage("certestud.p.parrafo1",params,LocaleContextHolder.getLocale()));
	
	        params = new String[]{distribucion.getMatricula().getAlumno().getNombreCompleto(),
	                distribucion.getMatricula().getAlumno().getNif(),
	                distribucion.getMatricula().getOfertaCentro().getOferta().getCiclo().getNombre(),
	                String.valueOf(distribucion.getMatricula().getOfertaCentro().getOferta().getCiclo().getHorasPractica()),
	                distribucion.getAnexoContrato().getConvenio().getArea().getEmpresa().getNombre()};
	        parametros.put("P_PARRAFO2",reportsSource.getMessage("certestud.p.parrafo2",params,LocaleContextHolder.getLocale()));
	
	        params = new String[]{distribucion.getAnexoContrato().getConvenio().getArea().getEmpresa().getNombre(),
	                distribucion.getMatricula().getAlumno().getNombreCompleto(),
	                "(positiva,satisfactoria,excelente...)",
	                "(puntualidad,responsabilidad,iniciativa,trabajo en equipo...)"};
	        parametros.put("P_PARRAFO3",reportsSource.getMessage("certestud.p.parrafo3",params,LocaleContextHolder.getLocale()));
	
	        params = new String[]{formatearFecha(diaFirma,mesFirma,anioFirma,localidad)};
	        parametros.put("P_PARRAFO_FIRMA",reportsSource.getMessage("certestud.p.parrafo.firma",params,LocaleContextHolder.getLocale()));
	
	        params = new String[]{distribucion.getAnexoContrato().getOfertaCentro().getCentro().getDirector()};
	        parametros.put("P_FDO_DIRECTOR",reportsSource.getMessage("certestud.p.fdo",params,LocaleContextHolder.getLocale()));
	        
	        params = new String[]{distribucion.getMatricula()!=null && distribucion.getMatricula().getUnidad() != null && distribucion.getMatricula().getUnidad().getTutor()!=null? 
	        	distribucion.getMatricula().getUnidad().getTutor().getNombre(): ""};
	        parametros.put("P_FDO_TUTOR_CEN",reportsSource.getMessage("certestud.p.fdo",params,LocaleContextHolder.getLocale()));
	
	        params = new String[]{distribucion.getTutorEmpresa()};
	        parametros.put("P_FDO_TUTOR_EMP",reportsSource.getMessage("certestud.p.fdo",params,LocaleContextHolder.getLocale()));
	
	        parametros.put("P_SUBREPORT",realizacionesSubreport);
	
	        String idCiclo = distribucion.getAnexoContrato().getOfertaCentro().getOferta().getCiclo().getIdCiclo();
	        parametros.put("P_LISTA_REALIZACIONES",new JRBeanCollectionDataSource(generaListaRealizaciones(idCiclo)));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	
	        return generarPDF(jasperPrint,documento);
	        
    	} catch (Exception e) {
    		log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    @GetMapping("/informes/informeFinal/{idDocumento}/{idDistribucion}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getInformeFinal(@PathVariable String idDocumento,@PathVariable String idDistribucion,
                                                    @PathVariable String localidad, @PathVariable Integer diaFirma,
                                                    @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception {
        Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
        
        try {
        
	        Distribucion distribucion = distribucionService.getDistribucion(Long.valueOf(idDistribucion));
	
	        InputStream informeFinalReport = getClass().getResourceAsStream("/reports/informe_final_tutor.jasper");
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(informeFinalReport);
	
	        Map<String,Object> parametros = inicializarLogos(documento);
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	        parametros.put("P_ANIO_ACADEMICO",distribucion.getMatricula().getAnio() + "/"+(distribucion.getMatricula().getAnio()+1));
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	
	        List<Distribucion> lDistribuciones = new ArrayList<Distribucion>();
	        lDistribuciones.add(distribucion);
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lDistribuciones));
	
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    @GetMapping("/informes/informeFinal/{idDocumento}/{idCentro}/{curso}/{idOferta}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getInformeFinal(@PathVariable String idDocumento,@PathVariable String idCentro,@PathVariable Integer curso,
                                                  @PathVariable String idOferta, @Param("idPeriodoPractica") Integer idPeriodoPractica, @Param("tutor") String tutor,
                                                  @PathVariable String localidad, @PathVariable Integer diaFirma,
                                                  @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception {
        Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
		
        try {
        	

	        List<AlumAnexov> listaAlumnos;
			if(tutor!= null && tutor!="") {
				listaAlumnos = (List<AlumAnexov>) matriculaService.getListaAlumByAnexoByTutor(idCentro,curso,idOferta, idPeriodoPractica,tutor);
			}else {
				listaAlumnos = (List<AlumAnexov>) matriculaService.getListaAlumByAnexo(idCentro,curso,idOferta, idPeriodoPractica);
			}
	     
	
	        // recuperar las distribuciones de los alumnos
	        List<Distribucion> lDistribuciones = new ArrayList<Distribucion>();
	
	        // Carga de las distribuciones de cada alumno
	        for (AlumAnexov a: listaAlumnos){
	            lDistribuciones.add(distribucionService.getDistribucion( Long.valueOf(a.getIdDistribucion())));
	        }
	
	        InputStream informeFinalReport = getClass().getResourceAsStream("/reports/informe_final_tutor.jasper");
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(informeFinalReport);
	
	        Map<String,Object> parametros = inicializarLogos(documento);
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	        parametros.put("P_ANIO_ACADEMICO",lDistribuciones.get(0).getMatricula().getAnio() + "/"+(lDistribuciones.get(0).getMatricula().getAnio()+1));
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lDistribuciones));
	
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    @GetMapping("/informes/planFormativo/{idDocumento}/{idDistribucion}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getPlanFormativo(@PathVariable String idDocumento,@PathVariable String idDistribucion,
                                                  @PathVariable String localidad, @PathVariable Integer diaFirma,
                                                  @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception {
        Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
       
        try {
        
	        Distribucion distribucion = distribucionService.getDistribucion(Long.valueOf(idDistribucion));
	
	        InputStream informeFinalReport = getClass().getResourceAsStream("/reports/plan_formativo.jasper");
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(informeFinalReport);
	
	        Map<String,Object> parametros = inicializarLogos(documento);
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	        parametros.put("P_ANIO_ACADEMICO",distribucion.getMatricula().getAnio() + "/"+(distribucion.getMatricula().getAnio()+1));
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	
	        List<Distribucion> lDistribuciones = new ArrayList<>();
	        lDistribuciones.add(distribucion);
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(lDistribuciones));
	
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    // Calendario y temporalizacion
    @GetMapping("/informes/calendarioTemporalizacion/{idDocumento}/{idDistribucion}/{localidad}/{diaFirma}/{mesFirma}/{anioFirma}")
    public ResponseEntity<byte[]> getCalendarioTemporalizacion(@PathVariable String idDocumento,@PathVariable String idDistribucion,
                                                               @PathVariable String localidad, @PathVariable Integer diaFirma,
                                                               @PathVariable Integer mesFirma, @PathVariable Integer anioFirma) throws  Exception {

        Documento documento = documentoService.getDocumento(Long.valueOf(idDocumento));
        
        try {
	        
	        Distribucion distribucion = distribucionService.getDistribucion(Long.valueOf(idDistribucion));
	
	        InputStream calendarioTemporalizacion = getClass().getResourceAsStream("/reports/calendario_temporalizacion.jasper");
	        InputStream calendarioCurso1 = getClass().getResourceAsStream("/reports/calendario_temporalizacion_curso1.jasper");
	        InputStream calendarioCurso2 = getClass().getResourceAsStream("/reports/calendario_temporalizacion_curso2.jasper");
	
	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(calendarioTemporalizacion);
	
	        Map<String,Object> parametros = inicializarLogos(documento);
	
	        parametros.put("P_DOCUMENTO",documento);
	        parametros.put("P_DISTRIBUCION",distribucion);
	//        parametros.put("P_MARCA_AGUA",getMarcaAgua());
	
	        Calendario calendario = generaCalendarioReport(distribucion);
	
	        parametros.put("P_CALENDARIO",calendario);
	        parametros.put("P_FECHA_FDO",formatearFecha(diaFirma,mesFirma,anioFirma,localidad));
	
	        //COLORES FONDO
	        parametros.put("P_COLOR_NO_LECTIVO", FONDO_NO_LECTIVO);
	        parametros.put("P_COLOR_LECTIVO", FONDO_LECTIVO);
	
	        parametros.put("P_SUBREPORT_CURSO1", calendarioCurso1);
	        parametros.put("P_SUBREPORT_CURSO2", calendarioCurso2);
	
	        // SUBREPORTS MESES
	        for (int i=1; i<=12; i++){
	            // Primer Curso
	            if (calendario.getCurso1().getLMeses().get(i)!=null){
	                if (i == 1 || i == 3 || i == 5 || i== 7 || i== 10 || i == 12){
	                    parametros.put("P_SUBREPORT_CURSO1_MES"+i,getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes31.jasper"));
	                }
	                else if ( i == 2){
	                    // Mes m= calendario.getCurso1().getLMeses().get(i);
	                    boolean bisiesto =   (((distribucion.getAnioIni()+1)% 4 == 0)  && ((distribucion.getAnioIni()+1) % 100 != 0)) || ((distribucion.getAnioIni()+1) % 400 == 0);
	                    parametros.put("P_SUBREPORT_CURSO1_MES"+i,bisiesto?getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes29.jasper"):getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes28.jasper"));
	                }
	                else if (i == 4 || i == 6 || i == 9 || i== 11 ){
	                    parametros.put("P_SUBREPORT_CURSO1_MES"+i,getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes30.jasper"));
	                }
	            }
	            // Segundo Curso
	            if ( calendario.getCurso2() != null && calendario.getCurso2().getLMeses().get(i)!=null){
	                if (i == 1 || i == 3 || i == 5 || i== 7 || i== 10 || i == 12){
	                    parametros.put("P_SUBREPORT_CURSO2_MES"+i,getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes31.jasper"));
	                }
	                else if ( i == 2){
	                    boolean bisiesto =   (((distribucion.getAnioIni()+1)% 4 == 0)  && ((distribucion.getAnioIni()+1) % 100 != 0)) || ((distribucion.getAnioIni()+1) % 400 == 0);
	                    parametros.put("P_SUBREPORT_CURSO2_MES"+i,bisiesto?getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes29.jasper"):getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes28.jasper"));
	                }
	                else if (i == 4 || i == 6 || i == 9 || i== 11 ){
	                    parametros.put("P_SUBREPORT_CURSO2_MES"+i,getClass().getResourceAsStream("/reports/calendario_temporalizacion_mes30.jasper"));
	                }
	            }
	        }
	
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
	
	        return generarPDF(jasperPrint,documento);
        
        } catch (Exception e) {
        	log.error("Error generando informe" + documento.getNombre(), e);
    	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createGepepErrorHeaders("Error generando " + documento.getNombre() , e.getMessage())).build();
        }       
    }

    private Calendario generaCalendarioReport(Distribucion dis) {

        Curso curso1 = new Curso();

        if (  dis.getMesIniDistribucion().intValue() >= 9 ){
            curso1.setAnioIni(dis.getAnioIni().intValue());
            curso1.setAnioFin( (dis.getAnioIni().intValue()+1));
        }
        else if ( (dis.getMesIniDistribucion().intValue() >= 1 && dis.getAnioIni().intValue() <= dis.getAnioFin().intValue())){
            curso1.setAnioIni((dis.getAnioIni().intValue()-1));
            curso1.setAnioFin(dis.getAnioFin().intValue());
        }

        curso1.setCursoAcademico(curso1.getAnioIni()+"/"+curso1.getAnioFin());
        List<Mes> lMesesAnio1 = new ArrayList<>();
        for (int i = 0; i <= 12; i++) { // inicializar para poder setear
            lMesesAnio1.add(null);
        }

        // Curso 1 inicio primer a�o
        if (dis.getMesIniDistribucion() >= 9 && dis.getAnioIni().intValue() == curso1.getAnioIni()){

            for (int i = dis.getMesIniDistribucion();i<= 12; i++){
                if ( ( dis.getMesFinDistribucion() < dis.getMesIniDistribucion()) || ( dis.getMesFinDistribucion() >= i )) {
                    Mes m = new Mes();
                    m.setNombre(StringUtils.capitalize(Month.of(i).getDisplayName(TextStyle.FULL, new Locale("es", "ES"))));

                    // TODO: Generaci�n dias no lectivos
                    if (i == dis.getMesIniDistribucion()) {
                        for (int j = 1; j <= dis.getDiaIni(); j++) {
                            m.getDiasLectivos()[j] = false;
                        }
                    }
                    lMesesAnio1.set(i, m);
                }
            }

            if (( dis.getAnioFin().intValue() > dis.getAnioIni().intValue())){
                for (int i = 1;i<= 7; i++){
                    if (dis.getAnioFin().intValue() > curso1.getAnioFin() || dis.getMesFinDistribucion().intValue()>= i ) {
                        Mes m = new Mes();
                        m.setNombre(StringUtils.capitalize(Month.of(i).getDisplayName(TextStyle.FULL, new Locale("es", "ES"))));
                        lMesesAnio1.set(i, m);
                    }
                }
            }
        }
        // Curso 1, inicio durante segundo a�o
        if(dis.getMesIniDistribucion() >= 1){
            for (int i = dis.getMesIniDistribucion();i<=( (dis.getAnioFin().intValue() > curso1.getAnioFin() || dis.getMesFinDistribucion().intValue() > 7)? 7:dis.getMesFinDistribucion() ); i++){
                Mes m = new Mes();
                m.setNombre(StringUtils.capitalize(Month.of(i).getDisplayName(TextStyle.FULL, new Locale("es","ES"))));
                lMesesAnio1.set(i,m);
            }
        }
        //Curso 2
        Curso curso2 = new Curso();
        curso2.setAnioIni(curso1.getAnioFin());
        curso2.setAnioFin((curso1.getAnioFin()+1));

        curso2.setCursoAcademico(curso2.getAnioIni()+"/"+curso2.getAnioFin());
        List<Mes> lMesesAnio2 = new ArrayList<>();
        for (int i = 0; i <= 12; i++) { // inicializar para poder setear
            lMesesAnio2.add(null);
        }

        // Curso 2 fin segundo a�o
        if (dis.getMesFinDistribucion().intValue() <= 7 && dis.getAnioFin().intValue() == curso2.getAnioFin()){
            for (int i = 9;i<= 12; i++){
                Mes m = new Mes();
                m.setNombre(StringUtils.capitalize(Month.of(i).getDisplayName(TextStyle.FULL, new Locale("es", "ES"))));
                lMesesAnio2.set(i, m);
            }

            for (int i = 1;i<=dis.getMesFinDistribucion(); i++){
                    Mes m = new Mes();
                    m.setNombre(StringUtils.capitalize(Month.of(i).getDisplayName(TextStyle.FULL, new Locale("es","ES"))));
                    lMesesAnio2.set(i,m);
            }
        } // Curso 2 fin primer a�o
        else if (dis.getMesFinDistribucion().intValue() >= 9 && dis.getAnioFin().intValue() == curso2.getAnioIni() ){
            for (int i = 9;i<= dis.getMesFinDistribucion(); i++){
                Mes m = new Mes();
                m.setNombre(StringUtils.capitalize(Month.of(i).getDisplayName(TextStyle.FULL, new Locale("es", "ES"))));
                lMesesAnio2.set(i, m);
            }
        }

        curso1.setLMeses(lMesesAnio1);
        curso2.setLMeses(lMesesAnio2);

        Calendario calendario = new Calendario();

        calendario.setCurso1(curso1);
        calendario.setCurso2(lMesesAnio2.stream().filter(m-> m!=null).parallel().findFirst().isPresent()?curso2:null);

        return calendario;
    }
    // funci�n para trocear las realizaciones en el certificado de estudios
    private List<FichaIndividualProg> generaListaRealizaciones ( String idCiclo){
        // se reaprovecha la clase FichaIndividualProg
        List<Realizacion> listaRealizaciones = realizacionService.getAllRealizacionesByCiclo(idCiclo,null);

        List<FichaIndividualProg> lista = new ArrayList<FichaIndividualProg>();
        String regex = "[0-9].- ";
        String [] splitRealizacionesEval  = listaRealizaciones.size() > 0 ? listaRealizaciones.get(0).getRealizacion().split(regex): new  String[0];

        for (int i=1;i<splitRealizacionesEval.length; i++){
            FichaIndividualProg ficha = new FichaIndividualProg();

            ficha.setRealizaciones(splitRealizacionesEval[i]);
            lista.add(ficha);
        }
        return lista;
    }


    private Map<String, Object> inicializarLogos(Documento documento) {
		Map<String, Object> params = new HashMap<>();

		try {
			if (documento.getLogo1() != null && !"".equals(documento.getLogo1())) {

				BufferedImage imagen = fileStorageService.loadFileAsBufferedImage(documento.getLogo1(), FOLDER_REPORTS_IMG);

				if (imagen != null) {
					params.put("P_LOGO1", imagen);
				}

			}
		} catch (FileStorageException fe) {
			log.error("Error cargando logo 1", fe);
			//throw fe;
		}

		try {
			if (documento.getLogo2() != null && !"".equals(documento.getLogo2())) {

				BufferedImage imagen = fileStorageService.loadFileAsBufferedImage(documento.getLogo2(),  FOLDER_REPORTS_IMG);

				if (imagen != null) {
					params.put("P_LOGO2", imagen);
				}
			}
		} catch (FileStorageException fe) {
			log.error("Error cargando logo 2", fe);
			//throw fe;
		}

		return params;
	}
	
	private BufferedImage getMarcaAgua() {
		try {

			BufferedImage imagen = fileStorageService.loadFileAsBufferedImage(MARCA_AGUA,  FOLDER_REPORTS_IMG);

			if (imagen != null) {
				return imagen;
			}

		} catch (FileStorageException fe) {
			log.error("Error cargando marca de agua", fe);
		}
		return null;
	}

	// m�todo para la carga gen�rica de los textos asociados a un report
	private Map<String, Object> cargaPropertiesReport(String reportProperties, Map<String, Object>  parametros){
        try {
        	String rutaFichero = new ClassPathResource("reports.properties").getFile().getAbsolutePath();
           // String rutaFichero = ResourceUtils.getFile("classpath:reports.properties").getAbsolutePath();
            String prep = reportProperties;
            try (
               Stream<String> lines = Files.lines(Paths.get(rutaFichero)).filter(line -> line.startsWith(prep))
                       .map(str -> str.substring(0, (str.indexOf("=") - 1)))
                       .parallel();
            ){
               for (String s : lines.collect(Collectors.toList())) {
                   if (!s.contains(reportProperties + ".p.")) { // excluir mensajes con par�metros
                       String param = "P_" + ((s.substring(s.indexOf(".") + 1))).replace(".", "_").toUpperCase();
                       parametros.put(param, reportsSource.getMessage(s, null, LocaleContextHolder.getLocale()));
                   }
               }
               lines.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parametros;
    }

    //  Generacion PDF
    private ResponseEntity<byte[]> generarPDF(JasperPrint jasperPrint, Documento documento){
        try {
            SimplePdfReportConfiguration reportConfig  = new SimplePdfReportConfiguration();
            reportConfig.setSizePageToContent(true);
            reportConfig.setForceLineBreakPolicy(false);

            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
           // exporter.setExporterOutput(new SimpleOutputStreamExporterOutput (documento.getNombre()+".pdf"));

            SimplePdfExporterConfiguration exportConfig  = new SimplePdfExporterConfiguration();
            exportConfig.setMetadataTitle(documento.getNombre());
            exportConfig.setDisplayMetadataTitle(true);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            OutputStreamExporterOutput outputStream = new SimpleOutputStreamExporterOutput(bos);
            
            exporter.setConfiguration(exportConfig);
            exporter.setExporterOutput(outputStream);
            

            exporter.exportReport();
            return ResponseEntity.ok(bos.toByteArray());
        } catch (Exception e) { //catch (JRException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }

}

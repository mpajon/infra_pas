package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.princast.gepep.domain.Documento;
import es.princast.gepep.service.DocumentoService;


/**
 * REST controller for managing Documento.
 */
@RestController
@RequestMapping("/api")
public class DocumentoResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoResource.class);


    @Autowired
    private DocumentoService documentoService;
   
 
    /**
     * POST  /documentos : Create a new documento.
     *
     * @param documento the documento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documento, or with status 400 (Bad Request) if the documento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documentos")
    @Timed
    public ResponseEntity<Object> createDocumento(@RequestBody Documento documento) throws URISyntaxException {
        log.debug("REST request to save Documento : {}", documento);
        
        try {
		    return ResponseEntity.ok(this.documentoService.createDocumento(documento));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }  
   
    }
    

    /**
     * PUT  /documentos : Updates an existing documento.
     *
     * @param documento the documento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documento,
     * or with status 400 (Bad Request) if the documento is not valid,
     * or with status 500 (Internal Server Error) if the documento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documentos")
    @Timed
    public ResponseEntity<String> updateDocumento(@RequestBody Documento documento) throws URISyntaxException {
        log.debug("REST request to update Documento : {}", documento);
        documentoService.updateDocumento(documento);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
     
    }
    
    /**
     * GET  /documentos : get all the documentos list sin relaciones jpa (para mejor rendimiento en los listados)
     *
     * @return the ResponseEntity with status 200 (OK) and the list of documentos in body
     */
    @GetMapping("/documentos/getAllDocumentosList")
    @Timed
    public  ResponseEntity <List<Documento>> getAllDocumentosList() {
        log.debug("REST request to getAllDocumentosList");
        Iterable<Documento> listaDocumentos = this.documentoService.getAllDocumentosList();
        return ResponseEntity.ok((List<Documento>)listaDocumentos);
    }

    /**
     * GET  /documentos : get all the documentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of documentos in body
     */
    @GetMapping("/documentos")
    @Timed
    public  ResponseEntity <List<Documento>> getAllDocumentos() {
        log.debug("REST request to get all Documentos");
        Iterable<Documento> listaDocumentos = this.documentoService.getAllDocumentos();
        return ResponseEntity.ok((List<Documento>)listaDocumentos);
    }
   
    /**
     * GET  /documentos/:id : get the "id" documento.
     *
     * @param id the id of the documento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documento, or with status 404 (Not Found)
     */
    @GetMapping("/documentos/{id}")
    @Timed
    public ResponseEntity<Documento> getDocumento(@PathVariable Long id) {
        log.debug("REST request to get Documento : {}", id);
        try {
            return ResponseEntity.ok(this.documentoService.getDocumento(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        
    }

    
    /**
     * DELETE  /documentos/:id : delete the "id" documento.
     *
     * @param id the id of the documento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documentos/{id}")
    @Timed
    public ResponseEntity<String> deleteDocumento(@PathVariable Long id) {
        log.debug("REST request to delete Documento : {}", id);
        try {
        documentoService.deleteDocumento(id);  
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }
    
   
    
   
    
    /*@PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)    
    public String singleFileUpload(@RequestParam("file") MultipartFile file)   throws URISyntaxException{    	
    	log.debug("REST subiendo imagen");
    
        if (file.isEmpty()) {
      
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
        	String realPathtoUploads =  request.getServletContext().getRealPath(UPLOADED_FOLDER);
            byte[] bytes = file.getBytes();
            Path path = Paths.get(realPathtoUploads+ file.getOriginalFilename());
            Files.write(path, bytes);

            return("You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }*/

  
    
    
    /********** OTRO EJEMPLO *******************/
    
    
 /*   @PostMapping(path="/uploadFile", consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)    
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/reports/imgs/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }*/

    
    
    
    /**
     * GET  /periodos-practicas/:id : get the "id" periodoPractica.
     * @param id the id of the periodoPractica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodoPractica, or with status 404 (Not Found)
     */
    @GetMapping("/tipos-practicas/{cn_tipo_practica}/documentos")
    @Timed
    public Iterable<Documento> getAllDocumentosByTipoPractica(@PathVariable (value="cn_tipo_practica") Long idTipoPractica) {
        log.debug("REST request to get Documentos by tipopractica : {}", idTipoPractica);
        return  documentoService.getAllDocumentosByTipoPractica(idTipoPractica);
        
    }    
    
    /**
     * POST  /periodos-practicas : Create a new documento.
     *
     * @param periodoPractica the periodoPractica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periodoPractica, or with status 400 (Bad Request) if the periodoPractica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipos-practica/{cn_tipo_practica}/documentos")
    @Timed
    public  ResponseEntity<Documento> createDocumentoWithTipoPractica(@PathVariable (value="cn_tipo_practica") Long id, @Valid @RequestBody Documento documento) throws URISyntaxException {
        log.debug("REST request to save PeriodoPractica : {}", documento);
        return documentoService.createDocumentoInTipoPractica(id,documento);
    }

 

    
    

}

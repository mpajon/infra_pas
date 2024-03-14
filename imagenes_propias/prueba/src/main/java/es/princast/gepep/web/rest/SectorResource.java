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

import es.princast.gepep.domain.Sector;
import es.princast.gepep.service.SectorService;

/**
 * REST controller for managing Sector.
 */
@RestController
@RequestMapping("/api")
public class SectorResource {

    private final Logger log = LoggerFactory.getLogger(SectorResource.class);


    @Autowired
    private SectorService sectorService;
    
	 
    /**
     * POST  /sectores : Create a new sector.
     *
     * @param sector the sector to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sector, or with status 400 (Bad Request) if the sector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sectores")
    @Timed
    public ResponseEntity<Object> createSector(@Valid @RequestBody Sector sector) throws URISyntaxException {
    	   log.debug("REST request to create Sector");    
    	   try {
    		    return ResponseEntity.ok(sectorService.createSector(sector));
    	   }
    	   catch (IllegalArgumentException e) {
    		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
           }
        
    }
    

    
    /**
     * PUT  /sectores : Updates an existing sector.
     *
     * @param sector the sector to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sector,
     * or with status 400 (Bad Request) if the sector is not valid,
     * or with status 500 (Internal Server Error) if the sector couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sectores")
    @Timed
    public ResponseEntity<String> updateSector(@Valid @RequestBody Sector sectorModificado) throws URISyntaxException {
      try {
       sectorService.updateSector(sectorModificado);
      }
     catch (IllegalArgumentException e) {
  	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        
    
    }

    /**
     * GET  /sectores : get all the sectors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sectors in body
     */
    @GetMapping("/sectores")
    @Timed
    public ResponseEntity<List<Sector>> getAllSectores() {
        log.debug("REST request to get all Sectores");
        Iterable<Sector> listaSectores = sectorService.getAllSectores();
        return ResponseEntity.ok((List<Sector>)listaSectores);
        }


    /**
     * GET  /sectores/:id : get the "id" sector.
     *
     * @param id the id of the sector to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sector, or with status 404 (Not Found)
     */
    @GetMapping("/sectores/{idSector}")
    @Timed
    public ResponseEntity<Sector> getSector (@PathVariable Long idSector) {
        log.debug("REST request to get Sector : {}", idSector);
        try {
        	 return ResponseEntity.ok(sectorService.getSector(idSector));        	
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }    
    }

    /**
     * DELETE  /sectores/:id : delete the "id" sector.
     *
     * @param id the id of the sector to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sectores/{idSector}")
    @Timed
    public ResponseEntity<String> deleteSector(@PathVariable Long idSector) {
        log.debug("REST request to delete Sector : {}", idSector);
        try {
            sectorService.deleteSector(idSector);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }
    
    
   

}

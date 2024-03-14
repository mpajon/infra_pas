package es.princast.gepep.web.rest;


import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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

import es.princast.gepep.domain.Area;
import es.princast.gepep.service.AreaService;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.gepep.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Area.
 */
@RestController
@RequestMapping("/api")
public class AreaResource {

    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private static final String ENTITY_NAME = "area";

    @Autowired
    private AreaService areaService;

     
    
    /**
     * POST  /areas : Create a new area.
     *
     * @param area the area to create
     * @return the ResponseEntity with status 201 (Created) and with body the new area, or with status 400 (Bad Request) if the area has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/areas")
    @Timed
    public ResponseEntity<Object> createArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to save Area : {}", area);
        try {
		    return ResponseEntity.ok(areaService.createArea(area));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
       
    }

    /**
     * PUT  /areas : Updates an existing area.
     *
     * @param area the area to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated area,
     * or with status 400 (Bad Request) if the area is not valid,
     * or with status 500 (Internal Server Error) if the area couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/areas")
    @Timed
    public ResponseEntity<String> updateArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to update Area : {}", area);
        try {
        	areaService.updateArea(area);
        }catch (IllegalArgumentException e) {
      	  return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * POST  /areas/paged : get all the areas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of areas in body
     */
    @PostMapping("/areas/paged")
    @Timed
    public ResponseEntity<List<Area>> getAllAreasPaged(@RequestBody Area areaFilter, Pageable pageable, boolean unpaged) {
        log.debug("REST request to get all Areas");
      
        Page<Area> page =  areaService.getAllAreasByCriteria(areaFilter, PaginationUtil.generatePageableSortIgnoreCase(pageable, unpaged));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/areas/paged");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);        
        }
    
    /**
     * GET  /areas : get all the areas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of areas in body
     */
    @GetMapping("/areas")
    @Timed
    public ResponseEntity<List<Area>> getAllAreas() {
        log.debug("REST request to get all Areas");
        Page<Area> page =  areaService.getAllAreas(Pageable.unpaged());
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);        
        }

    /**
     * GET  /areas/:id : get the "id" area.
     *
     * @param id the id of the area to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the area, or with status 404 (Not Found)
     */
    @GetMapping("/areas/{id}")
    @Timed
    public ResponseEntity<Area> getArea(@PathVariable Long id) {
        log.debug("REST request to get Area : {}", id);       
        
        try {
            return ResponseEntity.ok(areaService.getArea(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }
    
    @GetMapping("/empresas/{idEmpresa}/areas")
	@Timed
	public Iterable <Area> getAreasByEmpresa(@PathVariable (value="idEmpresa") Long idEmpresa) {
		log.debug("REST request to get Areas by Empresa : {}", idEmpresa);		  
	     return  areaService.getAreasByEmpresa(idEmpresa);
	}
    

    @GetMapping("/empresas/{idEmpresa}/areas/visada")
	@Timed
	public Iterable <Area> getAreasByEmpresaVisada(@PathVariable (value="idEmpresa") Long idEmpresa) {
		log.debug("REST request to get Areas by Empresa : {}", idEmpresa);		  
	     return  areaService.getAreasByEmpresaVisada(idEmpresa);
	}
    
    
    /**
     * DELETE  /areas/:id : delete the "id" area.
     *
     * @param id the id of the area to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/areas/{id}")
    @Timed
    public ResponseEntity<String> deleteArea(@PathVariable Long id) {
        log.debug("REST request to delete Area : {}", id);
        try {
        areaService.deleteArea(id);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

   

}

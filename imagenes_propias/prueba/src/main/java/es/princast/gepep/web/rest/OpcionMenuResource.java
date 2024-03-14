package es.princast.gepep.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import es.princast.gepep.domain.OpcionMenu;
import es.princast.gepep.repository.OpcionMenuRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.mntpa.back.web.rest.util.ResponseUtil;

/**
 * REST controller for managing OpcionMenu.
 */
@RestController
@RequestMapping("/api")
public class OpcionMenuResource {

    private final Logger log = LoggerFactory.getLogger(OpcionMenuResource.class);

    private static final String ENTITY_NAME = "opcionMenu";

    private final OpcionMenuRepository opcionMenuRepository;

    public OpcionMenuResource(OpcionMenuRepository opcionMenuRepository) {
        this.opcionMenuRepository = opcionMenuRepository;
    }

    /**
     * POST  /opcion-menus : Create a new opcionMenu.
     *
     * @param opcionMenu the opcionMenu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new opcionMenu, or with status 400 (Bad Request) if the opcionMenu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/opcion-menus")
    @Timed
    public ResponseEntity<OpcionMenu> createOpcionMenu(@Valid @RequestBody OpcionMenu opcionMenu) throws URISyntaxException {
        log.debug("REST request to save OpcionMenu : {}", opcionMenu);
        if (opcionMenu.getIdOpcionMenu() != null) {
            throw new BadRequestAlertException("A new opcionMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OpcionMenu result = opcionMenuRepository.save(opcionMenu);
        
        return ResponseEntity.created(new URI("/api/opcion-menus/" + result.getIdOpcionMenu()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdOpcionMenu().toString()))
            .body(result);
    }

    /**
     * PUT  /opcion-menus : Updates an existing opcionMenu.
     *
     * @param opcionMenu the opcionMenu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated opcionMenu,
     * or with status 400 (Bad Request) if the opcionMenu is not valid,
     * or with status 500 (Internal Server Error) if the opcionMenu couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/opcion-menus")
    @Timed
    public ResponseEntity<OpcionMenu> updateOpcionMenu(@Valid @RequestBody OpcionMenu opcionMenu) throws URISyntaxException {
        log.debug("REST request to update OpcionMenu : {}", opcionMenu);
        if (opcionMenu.getIdOpcionMenu() == null) {
            return createOpcionMenu(opcionMenu);
        }
        OpcionMenu result = opcionMenuRepository.save(opcionMenu);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, opcionMenu.getIdOpcionMenu().toString()))
            .body(result);
    }

    /**
     * GET  /opcion-menus : get all the opcionMenus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of opcionMenus in body
     */
    @GetMapping("/opcion-menus")
    @Timed
    public List<OpcionMenu> getAllOpcionMenus() {
        log.debug("REST request to get all OpcionMenus");
        return opcionMenuRepository.findAll();
        }

    /**
     * GET  /opcion-menus/:id : get the "id" opcionMenu.
     *
     * @param id the id of the opcionMenu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the opcionMenu, or with status 404 (Not Found)
     */
    @GetMapping("/opcion-menus/{id}")
    @Timed
    public ResponseEntity<OpcionMenu> getOpcionMenu(@PathVariable Long id) {
        log.debug("REST request to get OpcionMenu : {}", id);	
        return ResponseUtil.wrapOrNotFound(opcionMenuRepository.findById(id));
    }

    /**
     * DELETE  /opcion-menus/:id : delete the "id" opcionMenu.
     *
     * @param id the id of the opcionMenu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/opcion-menus/{id}")
    @Timed
    public ResponseEntity<Void> deleteOpcionMenu(@PathVariable Long id) {
        log.debug("REST request to delete OpcionMenu : {}", id);
        opcionMenuRepository.deleteById(id);
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

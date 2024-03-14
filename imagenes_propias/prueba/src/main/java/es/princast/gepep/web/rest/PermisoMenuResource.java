package es.princast.gepep.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

import es.princast.gepep.domain.PermisoMenu;
import es.princast.gepep.repository.PermisoMenuRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.mntpa.back.web.rest.util.ResponseUtil;

/**
 * REST controller for managing PermisoMenu.
 */
@RestController
@RequestMapping("/api")
public class PermisoMenuResource {

    private final Logger log = LoggerFactory.getLogger(PermisoMenuResource.class);

    private static final String ENTITY_NAME = "permisoMenu";

    private final PermisoMenuRepository permisoMenuRepository;

    public PermisoMenuResource(PermisoMenuRepository permisoMenuRepository) {
        this.permisoMenuRepository = permisoMenuRepository;
    }

    /**
     * POST  /permiso-menus : Create a new permisoMenu.
     *
     * @param permisoMenu the permisoMenu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new permisoMenu, or with status 400 (Bad Request) if the permisoMenu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/permiso-menus")
    @Timed
    public ResponseEntity<PermisoMenu> createPermisoMenu(@RequestBody PermisoMenu permisoMenu) throws URISyntaxException {
        log.debug("REST request to save PermisoMenu : {}", permisoMenu);
        if (permisoMenu.getIdPermisoMenu() != null) {
            throw new BadRequestAlertException("A new permisoMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PermisoMenu result = permisoMenuRepository.save(permisoMenu);
        
        return ResponseEntity.created(new URI("/api/permiso-menus/" + result.getIdPermisoMenu()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPermisoMenu().toString()))
            .body(result);
    }

    /**
     * PUT  /permiso-menus : Updates an existing permisoMenu.
     *
     * @param permisoMenu the permisoMenu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated permisoMenu,
     * or with status 400 (Bad Request) if the permisoMenu is not valid,
     * or with status 500 (Internal Server Error) if the permisoMenu couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/permiso-menus")
    @Timed
    public ResponseEntity<PermisoMenu> updatePermisoMenu(@RequestBody PermisoMenu permisoMenu) throws URISyntaxException {
        log.debug("REST request to update PermisoMenu : {}", permisoMenu);
        if (permisoMenu.getIdPermisoMenu() == null) {
            return createPermisoMenu(permisoMenu);
        }
        PermisoMenu result = permisoMenuRepository.save(permisoMenu);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, permisoMenu.getIdPermisoMenu().toString()))
            .body(result);
    }

    /**
     * GET  /permiso-menus : get all the permisoMenus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of permisoMenus in body
     */
    @GetMapping("/permiso-menus")
    @Timed
    public List<PermisoMenu> getAllPermisoMenus() {
        log.debug("REST request to get all PermisoMenus");
        return permisoMenuRepository.findAll();
        }

    /**
     * GET  /permiso-menus/:id : get the "id" permisoMenu.
     *
     * @param id the id of the permisoMenu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the permisoMenu, or with status 404 (Not Found)
     */
    @GetMapping("/permiso-menus/{id}")
    @Timed
    public ResponseEntity<PermisoMenu> getPermisoMenu(@PathVariable Long id) {
        log.debug("REST request to get PermisoMenu : {}", id);
        return ResponseUtil.wrapOrNotFound(permisoMenuRepository.findById(id));
    }

    /**
     * DELETE  /permiso-menus/:id : delete the "id" permisoMenu.
     *
     * @param id the id of the permisoMenu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/permiso-menus/{id}")
    @Timed
    public ResponseEntity<Void> deletePermisoMenu(@PathVariable Long id) {
        log.debug("REST request to delete PermisoMenu : {}", id);
        permisoMenuRepository.deleteById(id);
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

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

import es.princast.gepep.domain.Empresa;
import es.princast.gepep.service.EmpresaService;
import es.princast.gepep.web.rest.util.HeaderUtil;
import es.princast.gepep.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Empresa.
 */
@RestController
@RequestMapping("/api")
public class EmpresaResource {

	private final Logger log = LoggerFactory.getLogger(EmpresaResource.class);

	private static final String ENTITY_NAME = "empresa";

	@Autowired
	private EmpresaService empresaService;

	/**
	 * POST /empresas : Create a new empresa.
	 *
	 * @param empresa
	 *            the empresa to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         empresa, or with status 400 (Bad Request) if the empresa has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/empresas/create")
	@Timed
	public ResponseEntity<Object> createEmpresa(@Valid @RequestBody Empresa empresa) throws URISyntaxException {
		log.debug("REST request to save Empresa : {}", empresa);
		try {
		    return ResponseEntity.ok(empresaService.createEmpresa(empresa));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }     
	}

	/**
	 * PUT /empresas : Updates an existing empresa.
	 *
	 * @param empresa
	 *            the empresa to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         empresa, or with status 400 (Bad Request) if the empresa is not
	 *         valid, or with status 500 (Internal Server Error) if the empresa
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/empresas")
	@Timed
	public ResponseEntity<String> updateEmpresa(@Valid @RequestBody Empresa empresa) throws URISyntaxException {
		log.debug("REST request to update Empresa : {}", empresa);
		try {
			empresaService.updateEmpresa(empresa);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /empresas : get all the empresas.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of empresas in
	 *         body
	 */
	@GetMapping("/empresas")
	@Timed
	public ResponseEntity<List<Empresa>> getAllEmpresas(Pageable pageable) {
		log.debug("REST request to get all Empresas");
		Page<Empresa> page = empresaService.getAllEmpresas(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/empresas");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}
		
	 @PostMapping("/empresas")
	    @Timed
	    public ResponseEntity<List<Empresa>> getAllEmpresasByCriteria( @RequestBody Empresa empresa) {
	       return ResponseEntity.ok(empresaService.getAllEmpresasByCriteria(empresa));
		  //return ResponseEntity.ok(empresaService.getEmpresaByCif(empresa.getCif()));
	      }

	 
	 @GetMapping("/empresas-cif/{cif}")
		@Timed
		public ResponseEntity<Empresa> getEmpresa(@PathVariable String cif) {
			log.debug("REST request to get Empresa : {}", cif);
			try {
			return ResponseEntity.ok(empresaService.getEmpresaByCif(cif));
			} catch (IllegalArgumentException e) {
				return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
			}
		}
	
	/**
	 * GET /empresas/:id : get the "id" empresa.
	 *
	 * @param id
	 *            the id of the empresa to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the empresa, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/empresas/{id}")
	@Timed
	public ResponseEntity<Empresa> getEmpresa(@PathVariable Long id) {
		log.debug("REST request to get Empresa : {}", id);
		try {
			return ResponseEntity.ok(empresaService.getEmpresa(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	/**
	 * DELETE /empresas/:id : delete the "id" empresa.
	 *
	 * @param id
	 *            the id of the empresa to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/empresas/{id}")
	@Timed
	public ResponseEntity<String> deleteEmpresa(@PathVariable Long id) {
		log.debug("REST request to delete Empresa : {}", id);

		try {
			empresaService.deleteEmpresa(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}

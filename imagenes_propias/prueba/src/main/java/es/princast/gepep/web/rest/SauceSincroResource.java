package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.princast.gepep.domain.SauceSincro;
import es.princast.gepep.service.TareaLogService;
import es.princast.gepep.service.saucesincro.SauceSincroService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing SauceSincro.
 */
@RestController
@RequestMapping("/api")
public class SauceSincroResource {

	private final Logger log = LoggerFactory.getLogger(SauceSincroResource.class);

	
	@Value("${application.plaintsis.sincrosauce.registros}")
	private Integer maxRegistros;

	@Autowired
	private SauceSincroService sauceSincroService;

	@Autowired
	private TareaLogService tareaLogService;

	/**
	 * POST /sauceSincros : Create a new sauceSincro.
	 *
	 * @param sauceSincro
	 *            the sauceSincro to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         sauceSincro, or with status 400 (Bad Request) if the sauceSincro has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/sauceSincro")
	@Timed
	public ResponseEntity<SauceSincro> createSauceSincro(@Valid @RequestBody SauceSincro sauceSincro)
			throws URISyntaxException {
		log.debug("REST request to save SauceSincro : {}", sauceSincro);
		return sauceSincroService.createSauceSincro(sauceSincro);
	}

	/**
	 * PUT /sauceSincros : Updates an existing sauceSincro.
	 *
	 * @param sauceSincro
	 *            the sauceSincro to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         sauceSincro, or with status 400 (Bad Request) if the sauceSincro is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         sauceSincro couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/sauceSincro")
	@Timed
	public ResponseEntity<String> updateSauceSincro(@Valid @RequestBody SauceSincro sauceSincro)
			throws URISyntaxException {
		sauceSincroService.updateSauceSincro(sauceSincro);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /sauceSincros : get all the sauceSincros.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of sauceSincros
	 *         in body
	 */
	@GetMapping("/sauceSincros")
	@Timed
	public ResponseEntity<List<SauceSincro>> getAllSauceSincros() {
		log.debug("REST request to get a page of SauceSincroes");
		Iterable<SauceSincro> listaSauceSincroes = sauceSincroService.getAllSauceSincro();
		return ResponseEntity.ok((List<SauceSincro>) listaSauceSincroes);

	}

	/**
	 * GET /sauceSincros/:id : get the "id" sauceSincro.
	 *
	 * @param id
	 *            the id of the sauceSincro to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the sauceSincro, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/sauceSincros/{id}")
	@Timed
	public ResponseEntity<SauceSincro> getSauceSincro(@PathVariable String id) {
		log.debug("REST request to get SauceSincro : {}", id);
		try {
			return ResponseEntity.ok(sauceSincroService.getSauceSincro(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}

	}

	/**
	 * Put /sauceSincros-sincronizar : Importar enseñanzas de sauce.
	 * 
	 * @return the ResponseEntity with status 201 (Created) or with status 400 (Bad
	 *         Request) if there was an error
	 * 
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/sauceSincros-sincronizar")
	@Timed
	public ResponseEntity<String> sincronizar(@Valid @RequestBody SauceSincro sincro) throws URISyntaxException {

		try {
			log.debug("REST request to sincronizar con sauce Sauce : {}", sincro.getIdSincro());
			sauceSincroService.sincronizar(sincro.getIdSincro(), sincro.getFinicio(), sincro.getCursoAcademico());

		} catch (Exception e) {
			log.error("Error al importar datos de Sauce" + e.getMessage());
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Error al importar datos de Sauce");
		}

		// UPDATE FECHA
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	/**
	 * Método para la generación de informe excel.
	 *
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@GetMapping("/sauceSincros-export-excel-log")
	@Timed
	public ResponseEntity<byte[]> generateExportExcel() throws URISyntaxException {

		try {

			byte[] bytes = tareaLogService.generateExportExcel();

			return ResponseEntity.ok(bytes);

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil
					.createGepepErrorHeaders("Se ha producido un error en la generación del informe", e.getMessage()))
					.build();
		}
	}

}

package es.princast.gepep.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import es.princast.gepep.domain.VisitaTutor;
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

import es.princast.gepep.domain.GastoAlumno;
import es.princast.gepep.service.GastoAlumnoService;
import es.princast.gepep.web.rest.util.HeaderUtil;

/**
 * REST controller for managing GastoAlumno.
 */
@RestController
@RequestMapping("/api")
public class GastoAlumnoResource {

	private final Logger log = LoggerFactory.getLogger(GastoAlumnoResource.class);

	private static final String ENTITY_NAME = "gastoAlumno";

	@Autowired
	private GastoAlumnoService gastoAlumnoService;

	/**
	 * POST /gasto-alumnos : Create a new gastoAlumno.
	 *
	 * @param gastoAlumno
	 *            the gastoAlumno to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         gastoAlumno, or with status 400 (Bad Request) if the gastoAlumno has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/gasto-alumnos")
	@Timed
	public ResponseEntity<Object> createGastoAlumno(@Valid @RequestBody GastoAlumno gastoAlumno)
			throws URISyntaxException {
		log.debug("REST request to save GastoAlumno : {}", gastoAlumno);
		try {
		    return ResponseEntity.ok(gastoAlumnoService.createGastoAlumno(gastoAlumno));
	   }
	   catch (IllegalArgumentException e) {
		   return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
       }
	}

	/**
	 * PUT /gasto-alumnos : Updates an existing gastoAlumno.
	 *
	 * @param gastoAlumno
	 *            the gastoAlumno to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         gastoAlumno, or with status 400 (Bad Request) if the gastoAlumno is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         gastoAlumno couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/gasto-alumnos")
	@Timed
	public ResponseEntity<String> updateGastoAlumno(@Valid @RequestBody GastoAlumno gastoAlumno)
			throws URISyntaxException {
		log.debug("REST request to update GastoAlumno : {}", gastoAlumno);
		try {
			gastoAlumnoService.updateGastoAlumno(gastoAlumno);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	/**
	 * GET /gasto-alumnos : get all the gastoAlumnos.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of gastoAlumnos
	 *         in body
	 */
	@GetMapping("/gasto-alumnos")
	@Timed
	public ResponseEntity<List<GastoAlumno>> getAllGastoAlumnos() {
		log.debug("REST request to get all GastoAlumnos");
		Iterable<GastoAlumno> listaGasto = gastoAlumnoService.getAllGastoAlumnos();
		return ResponseEntity.ok((List<GastoAlumno>) listaGasto);
	}

	/**
	 * GET /gasto-alumnos/:id : get the "id" gastoAlumno.
	 *
	 * @param id
	 *            the id of the gastoAlumno to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         gastoAlumno, or with status 404 (Not Found)
	 */
	@GetMapping("/gasto-alumnos/{id}")
	@Timed
	public ResponseEntity<GastoAlumno> getGastoAlumno(@PathVariable Integer id) {
		log.debug("REST request to get GastoAlumno : {}", id);
		try {
			return ResponseEntity.ok(gastoAlumnoService.getGastoAlumno(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	@GetMapping("/gasto-alumnos/matricula/{idMatricula}")
	@Timed
	public ResponseEntity<List<GastoAlumno>> getGastoAlumnoByMatricula(@PathVariable String idMatricula) {
		log.debug("REST request to get GastoAlumno by Matricula");
		try {
			return ResponseEntity.ok(gastoAlumnoService.getAllByMatricula(idMatricula));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}
	
	@GetMapping("/gasto-alumnos/matricula/{idMatricula}/{idPeriodoLiquidacion}")
	@Timed
	public ResponseEntity<List<GastoAlumno>> getGastoAlumnoByMatriculaAndPeriodoLiquidacion(@PathVariable String idMatricula, @PathVariable Integer idPeriodoLiquidacion) {
		log.debug("REST request to get GastoAlumno by Matricula");
		try {
			return ResponseEntity.ok(gastoAlumnoService.getAllByMatriculaAndPeriodoLiquidacion(idMatricula, idPeriodoLiquidacion));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}
	

	@GetMapping("/gasto-alumnos/periodoLiquidacion/{idPeriodoLiquidacion}")
	@Timed
	public ResponseEntity<List<GastoAlumno>> getGastoAlumnoByPeriodoLiquidacion(@PathVariable Integer idPeriodoLiquidacion) {
		log.debug("REST request to get GastoAlumno by liquidacion");
		try {
			return ResponseEntity.ok(gastoAlumnoService.getAllByPeriodoLiquidacion(idPeriodoLiquidacion));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	@GetMapping("/gasto-alumnos/cursoCentro/{idCentro}/{curso}/{tutor}")
	@Timed
	public ResponseEntity<List<GastoAlumno>> getGastosAlumnosByCursoAndCentroAndTutor(@PathVariable String idCentro, @PathVariable Integer curso, @PathVariable String tutor) {
		log.debug("REST request to get getGastosAlumnosByCursoAndCentroAndTutor");
		try {
			return ResponseEntity.ok(gastoAlumnoService.getGastosAlumnosByCursoAndCentroAndTutor(idCentro,curso,tutor ));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}

	@GetMapping("/gasto-alumnos/periodo/{periodo}")
	@Timed
	public ResponseEntity<List<GastoAlumno>> getGastosPeriodo(@PathVariable Long periodo) {
		log.debug("REST request to get getGastosPeriodo");
		try {
			return ResponseEntity.ok(gastoAlumnoService.getGastosPeriodo(periodo));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		}
	}
	
	/**
	 * DELETE /gasto-alumnos/:id : delete the "id" gastoAlumno.
	 *
	 * @param id
	 *            the id of the gastoAlumno to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/gasto-alumnos/{id}")
	@Timed
	public ResponseEntity<String> deleteGastoAlumno(@PathVariable Integer id) {
		log.debug("REST request to delete GastoAlumno : {}", id);
		try {
			gastoAlumnoService.deleteGastoAlumno(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	@PutMapping("/gasto-alumnos/validar")
	@Timed
	public ResponseEntity<String> updateValidarGastoAlumno(@Valid @RequestBody GastoAlumno gastoAlumno)
			throws URISyntaxException {
		log.debug("REST request to update GastoAlumno : {}", gastoAlumno);
		try {
			gastoAlumnoService.updateValidarGasto(gastoAlumno);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

    @PutMapping("/gasto-alumnos/validarTodos/{idOfertaCentro}/{anio}/{idPeriodo}")
    @Timed
    public ResponseEntity<String> updateGastoAlumnoValidaTodos(@PathVariable String idOfertaCentro, @PathVariable Integer anio, @PathVariable Long idPeriodo)
            throws URISyntaxException {
        log.debug("REST request to validarTodos: {}");
        try {
            gastoAlumnoService.updateValidarTodosGastos(idOfertaCentro,anio,idPeriodo,true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/gasto-alumnos/noValidarTodos/{idOfertaCentro}/{anio}/{idPeriodo}")
    @Timed
    public ResponseEntity<String> updateGastoAlumnoInValidaTodos(@PathVariable String idOfertaCentro, @PathVariable Integer anio, @PathVariable Long idPeriodo)
            throws URISyntaxException {
        log.debug("REST request to invalidarTodos : {}");
		try {
			gastoAlumnoService.updateValidarTodosGastos(idOfertaCentro,anio,idPeriodo,false);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
		}

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}

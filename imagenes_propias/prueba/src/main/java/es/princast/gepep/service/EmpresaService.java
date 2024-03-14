package es.princast.gepep.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.Empresa;
import es.princast.gepep.repository.EmpresaRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EmpresaService {

	private static final String ENTITY_NAME = "empresa";

	@Autowired
	private EmpresaRepository empresaRepository;


	public ResponseEntity<Empresa> createEmpresa(final Empresa nuevaEmpresa) throws URISyntaxException {
		log.debug("SERVICE request to save Empresa : {}", nuevaEmpresa);

		if (nuevaEmpresa.getIdEmpresa() != null) {
			throw new BadRequestAlertException("A new empresa cannot already have an ID", ENTITY_NAME, "idexists");
		}

		Empresa result = empresaRepository.save(nuevaEmpresa);
		return ResponseEntity.created(new URI("/api/empresas/" + result.getIdEmpresa()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdEmpresa().toString())).body(result);
	}

	public Empresa getEmpresa(final Long idEmpresa) {
		Optional<Empresa> empresa = empresaRepository.findById(idEmpresa);
		if (!empresa.isPresent()) {
			throw new IllegalArgumentException("No existe una empresa con ese identificador.");
		}
		return empresa.get();
	}

	public Empresa getEmpresaByCif(final String cif) {
		//Optional<Empresa> empresa = empresaRepository.findByCif(cif);
		try {
				Optional<Empresa> empresa = empresaRepository.findByCifAndFechaBajaIsNull(cif);
				if (!empresa.isPresent()) {
						throw new IllegalArgumentException("No existe una empresa con ese CIF.");
					}
				return empresa.get();
			}
		catch (Exception ex){	
			
			List<Empresa> empresas = empresaRepository.findAllByCifAndFechaBajaIsNull(cif);
			if(empresas!=null && empresas.size()>1)
			{
				return empresas.get(0);
			}	
			else if(empresas.size() <1) {
					throw new IllegalArgumentException("No existe una empresa con ese CIF.");
			}
				
		}
		return null;
		
	} 

	private Sort sortByNombreAsc() {
		// 

		Sort.Order order = new Sort.Order(Sort.Direction.ASC, "nombre").ignoreCase();
		return Sort.by(order);
	}
	
	public Page<Empresa> getAllEmpresas(Pageable pageable) {
		return empresaRepository.findAll(pageable);
	}
	
	public List<Empresa> getAllEmpresasActivas() {
		return empresaRepository.findAllByFechaBajaIsNull();
	}

	public ResponseEntity<Empresa> updateEmpresa(final Empresa empresaModificada) throws URISyntaxException {
		log.debug("SERVICE request to update Empresa : {}", empresaModificada);
		if (empresaModificada.getIdEmpresa() == null) {
			return createEmpresa(empresaModificada);
		}
		Empresa result = empresaRepository.save(empresaModificada);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, empresaModificada.getIdEmpresa().toString()))
				.body(result);

	}

	public void deleteEmpresa(final Long idEmpresa) {
		Optional<Empresa> empresa = empresaRepository.findById(idEmpresa);
		if (!empresa.isPresent()) {
			throw new IllegalArgumentException("No existe una empresa con ese identificador.");
		}

		empresaRepository.deleteById(idEmpresa);
	}
	
	@SuppressWarnings("unchecked")
	public List<Empresa> getAllEmpresasByCriteria(final Empresa partialMatch) {
		return empresaRepository.findAll(new Specification<Empresa>() {
			@Override
			public Predicate toPredicate(Root<Empresa> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				if (!StringUtils.isEmpty(partialMatch.getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("nombre")),
							"%" + partialMatch.getNombre().toUpperCase() + "%"));
				}
				if (!StringUtils.isEmpty(partialMatch.getCif())) {
					predicates.add(builder.like(builder.upper(root.get("cif")),
							"" + partialMatch.getCif().toUpperCase() + ""));
				}
			
				query.orderBy(builder.asc(builder.upper(root.get("nombre"))));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		});
	}

}

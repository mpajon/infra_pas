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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.princast.gepep.domain.CursoAcademico;
import es.princast.gepep.domain.DistribucionPeriodo;
import es.princast.gepep.domain.Perfil;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.Tutor;
import es.princast.gepep.domain.Unidad;
import es.princast.gepep.domain.Usuario;
import es.princast.gepep.repository.CentroRepository;
import es.princast.gepep.repository.CursoAcademicoRepository;
import es.princast.gepep.repository.PerfilRepository;
import es.princast.gepep.repository.ProfesorRepository;
import es.princast.gepep.repository.TutorRepository;
import es.princast.gepep.repository.UnidadRepository;
import es.princast.gepep.repository.UsuarioRepository;
import es.princast.gepep.web.rest.errors.BadRequestAlertException;
import es.princast.gepep.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UsuarioService {

	private static final String ENTITY_NAME = "usuario";

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TutorRepository tutorRepository;

	@Autowired
	private ProfesorRepository profesorRepository;	 

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private UnidadRepository unidadRepository;

	@Autowired
	private CursoAcademicoRepository cursoAcademicoRepository;

	@Autowired
	private MessageSource messageSource;

	public ResponseEntity<Usuario> createUsuario(final Usuario nuevoUsuario) throws URISyntaxException {
		log.debug("SERVICE request to save Usuario : {}", nuevoUsuario);

		if (nuevoUsuario.getIdUsuario() != null) {
			throw new BadRequestAlertException("A new usuario cannot already have an ID", ENTITY_NAME, "idexists");
		}

		List<Usuario> usuarioDuplicado = this.usuarioRepository.findAllByLoginIgnoreCase(nuevoUsuario.getLogin());
		if (usuarioDuplicado.size() > 0) {
			throw new IllegalArgumentException(
					messageSource.getMessage("error.usuario.existe", null, LocaleContextHolder.getLocale()));
		}

		Usuario result = usuarioRepository.save(nuevoUsuario);
		return ResponseEntity.created(new URI("/api/usuarios/" + result.getIdUsuario()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdUsuario().toString()))
				.body(result);
	}

	public Usuario getUsuario(final Long idUsuario) {
		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
		if (!usuario.isPresent()) {
			throw new IllegalArgumentException("No existe una usuario con ese identificador.");
		}
		return usuario.get();
	}

	public Page<Usuario> getAllUsuarios(Pageable page) {
		return usuarioRepository.findAll(page);
	}

	public Usuario getUsuarioByLogin(final Usuario user) {
		Optional<Usuario> usuario = usuarioRepository.findOneByLoginIgnoreCase(user.getLogin());

		if (!usuario.isPresent()) {		//crea usuario si aplica, sino excepcion.	
			user.setDni(this.truncarNif(user.getDni()));
			Optional<Profesor> profesor = profesorRepository.findOneByNif(user.getDni());
			if (profesor.isPresent()) {
				Optional<CursoAcademico> curso = cursoAcademicoRepository.findByActual(Boolean.TRUE);
				//List<Unidad> unidad = unidadRepository.findAllByTutorAndAnio(profesor,
				//		curso.isPresent() ? curso.get().getIdAnio() : null);

				//INC000003216909
				List<Unidad> unidad = unidadRepository.findAllByTutorOrTutorAdicionalAndAnio ( profesor.get(), profesor.get(),curso.isPresent() ? curso.get().getIdAnio() : null);

				if (unidad.isEmpty()) {
					throw new IllegalArgumentException("No existe un usuario con este login en la aplicacion.");
				} else {
					user.setCentro(unidad.get(0).getOfertaCentro().getCentro());
					user.setActivo(Boolean.TRUE);
					if(user.getDni()!= null) {
						user.setTipoFiscal(this.calcularTipoFiscal(user.getDni()));
					}
					Optional<Perfil> perfil = perfilRepository.findById(Long.valueOf(2));
					user.setPerfil(perfil.isPresent() ? perfil.get() : null);
					return usuarioRepository.save(user);
				}
			} else {
				throw new IllegalArgumentException("No existe un usuario con este login en la aplicacion.");
			}	
		} else if ("ROLE_CENTRO".equals(usuario.get().getPerfil().getDescripcion().trim().toUpperCase())) {			
			if (!usuario.get().getActivo()) {				
				tratarNoActivosYCentro(usuario.get());
			} else // esta activo y es centro
			{
				String msg = tratarActivosYCentro(usuario.get());
				if (!"".equals(msg))
					throw new IllegalArgumentException(msg);
				else 
					 usuario = usuarioRepository.findOneByLoginIgnoreCase(user.getLogin());
			}
			return usuario.get();
		} else // Usuario existe, con rol Direccion pero no tiene centro asociado.
		if ("ROLE_DIRECTOR".equals(usuario.get().getPerfil().getDescripcion().trim().toUpperCase())
				&& usuario.get().getCentro() == null) {
			throw new IllegalArgumentException("102");
		}
		return usuario.get();

	}

 	/*
	 * Metodo para tratar los usuarios con rol centro y NO activos en Gepep.
	 * Si no existe en la tabla tutor --> error de que profesor no existe.
	 * Si existe, pero no tiene tutoria en el curso academico actual --> Muestro error.
	 * Si existe y tiene tutoria ( si no tiene centro le asigno el de la tutoria) --> Lo activo y accede. 
	 */
	public void tratarNoActivosYCentro (Usuario usuario) {
		
		Optional<Profesor> profesor = profesorRepository.findOneByNif(usuario.getDni());
		if (!profesor.isPresent()) { // 1. Ni esta activo ni es tutor.
			throw new IllegalArgumentException("101");
		} else { 
			Optional<CursoAcademico> curso = cursoAcademicoRepository.findByActual(Boolean.TRUE);
			List<Unidad> unidad = unidadRepository.findAllByTutorOrTutorAdicionalAndAnio(profesor.get(),profesor.get(),
					curso.isPresent() ? curso.get().getIdAnio() : null);
			if (unidad.isEmpty())
				throw new IllegalArgumentException("103");
			else {
				if (usuario.getCentro() == null) {							
					usuario.setCentro(unidad.get(0).getOfertaCentro().getCentro());
				}
				usuario.setActivo(Boolean.TRUE);
				usuarioRepository.save(usuario);
			}
		}
	}	
	

	/*
	 * Metodo para tratar los usuarios con rol centro y  activos en Gepep.
	 * Si no existe en la tabla tutor --> error de que profesor no existe. Desactivo usuario.
	 * Si existe, pero no tiene tutoria en el curso acadamico actual --> Muestro error. Desactivo usuario.
	 * Si existe y tiene tutoria ( si no tiene centro le asigno el de la tutoria) --> Accede. 
	 */
	public String tratarActivosYCentro (Usuario usuario) {
		
		String mensaje = "";		
		Optional<Profesor> profesor = profesorRepository.findOneByNif(usuario.getDni());
		if (!profesor.isPresent()) { 	
			mensaje = "104";
		} else {
			Optional<CursoAcademico> curso = cursoAcademicoRepository.findByActual(Boolean.TRUE);
			List<Unidad> unidad = unidadRepository.findAllByTutorOrTutorAdicionalAndAnio(profesor.get(), profesor.get(),
					curso.isPresent() ? curso.get().getIdAnio() : null);
			if (unidad.isEmpty()) {			
				mensaje = "103";
			} else {
				if (usuario.getCentro() == null) {
					usuario.setCentro(unidad.get(0).getOfertaCentro().getCentro());
					usuarioRepository.saveAndFlush(usuario);
				}
			}			
		}
		
		return mensaje;		
	}
	
	/*
	 * Metodo que se invoca desde Resource cuando hay excepcion que precisa desactivar al usuario.
	 */
	public void desactivarUsuario (final Usuario user) {
		Optional<Usuario> usuario = usuarioRepository.findOneByLoginIgnoreCase(user.getLogin());
		usuario.get().setActivo(Boolean.FALSE);
		usuarioRepository.save(usuario.get());		
	}

	public Usuario getUsuarioByLoginByPwd(final String login, final String password) {
		Optional<Usuario> usuario = usuarioRepository.findOneByLoginIgnoreCaseAndPwd(login, password);
		if (!usuario.isPresent()) {
			throw new IllegalArgumentException("No existe un usuario con ese login/password.");
		}
		return usuario.get();
	}

	public ResponseEntity<Usuario> updateUsuario(final Usuario usuarioModificado) throws URISyntaxException {
		log.debug("SERVICE request to update Usuario : {}", usuarioModificado);
		if (usuarioModificado.getIdUsuario() == null) {
			return createUsuario(usuarioModificado);
		}

		List<Usuario> usuarioDuplicado = this.usuarioRepository.findAllByLoginIgnoreCase(usuarioModificado.getLogin());
		if (usuarioDuplicado.size() > 1) {

			if (usuarioDuplicado.size() > 0) {
				if (usuarioDuplicado.size() == 1
						&& usuarioDuplicado.get(0).getIdUsuario() == usuarioModificado.getIdUsuario()) {
					Usuario result = usuarioRepository.save(usuarioModificado);
					return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
							usuarioModificado.getIdUsuario().toString())).body(result);
				} else {
					throw new IllegalArgumentException(
							messageSource.getMessage("error.usuario.existe", null, LocaleContextHolder.getLocale()));
				}
			}
		}

		Usuario result = usuarioRepository.save(usuarioModificado);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, usuarioModificado.getIdUsuario().toString()))
				.body(result);

	}
	
	public ResponseEntity<String> desactivarUsuarios() throws URISyntaxException {
		log.debug("SERVICE request to deactive Users");
		 
		Perfil perfil = perfilRepository.getOne(Long.valueOf(2));
		//obtengo tutores activos y sin fecha de baja.
		List<Usuario> usuariosTutores = this.usuarioRepository.findAllByPerfilAndFechaBajaIsNullAndActivoIsTrue(perfil);
		
		if (!usuariosTutores.isEmpty()) {
			//busco tutorias en el curso academico actual. Si no tiene , lo desactivo.
			for (Usuario tutor : usuariosTutores) {			
				Optional<Profesor> profesor = profesorRepository.findOneByNif(tutor.getDni());
				if (!profesor.isPresent()) { // 1. el usuario es no esta en la tabla profesores
					tutor.setActivo(Boolean.FALSE);
					usuarioRepository.save(tutor);					
				} else {
					// 2. Existe como tutor
					Optional<CursoAcademico> curso = cursoAcademicoRepository.findByActual(Boolean.TRUE);
					List<Unidad> unidad = unidadRepository.findAllByTutorAndAnio(profesor,
							curso.isPresent() ? curso.get().getIdAnio() : null);
					if (unidad.isEmpty()) {
						tutor.setActivo(Boolean.FALSE);
						usuarioRepository.save(tutor);
						
					}
				}
			}
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	

	public void deleteUsuario(final Long idUsuario) {
		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
		if (!usuario.isPresent()) {
			throw new IllegalArgumentException("No existe una usuario con ese identificador.");
		}

		List<Profesor> listaTutores = profesorRepository.findAllByUsuario(usuario.get());
		//List<Tutor> listaTutores = tutorRepository.findAllByUsuario(usuario.get());
		if (listaTutores.size() > 0) {
			// No se puede borrar porque esta referencenciado en tutor
			throw new IllegalArgumentException(messageSource.getMessage("error.usuario.referenciado.tutor", null,
					LocaleContextHolder.getLocale()));
		}

		usuarioRepository.deleteById(idUsuario);
	}

	public Page<Usuario> getAllUsuariosByCriteria(final Usuario partialMatch, Pageable pageable) {
		return usuarioRepository.findAll(new Specification<Usuario>() {

			@Override
			public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();

				if (!StringUtils.isEmpty(partialMatch.getNombre())) {
					predicates.add(builder.like(builder.upper(root.get("nombre")),
							"%" + partialMatch.getNombre().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getApellidos())) {
					predicates.add(builder.like(builder.upper(root.get("apellidos")),
							"%" + partialMatch.getApellidos().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getDni())) {
					predicates.add(builder.like(builder.upper(root.get("dni")),
							"%" + partialMatch.getDni().toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(partialMatch.getLogin())) {
					predicates.add(builder.like(builder.upper(root.get("login")),
							"%" + partialMatch.getLogin().toUpperCase() + "%"));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}

		}, pageable);
	}

	private String truncarNif(String nif) {
		if (nif != null && nif.trim().length() > 9) {
			return nif.trim().substring(1);
		}
		return nif;
	}

	private String calcularTipoFiscal(String nif) {
		while (nif.charAt(0) == '0') {
			nif = nif.substring(1);
		}

		if (Character.isLetter(nif.charAt(0))) {
			return "T";
		}

		return "N";
	}
}

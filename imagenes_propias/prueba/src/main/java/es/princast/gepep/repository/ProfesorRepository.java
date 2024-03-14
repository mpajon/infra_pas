package es.princast.gepep.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import es.princast.gepep.domain.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.OfertaFormativa;
import es.princast.gepep.domain.Profesor;
import es.princast.gepep.domain.Usuario;


/**
 * Spring Data JPA repository for the Profesor entity.
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String> , JpaSpecificationExecutor<Profesor>{
	
	List<Profesor> findAllByOferta(OfertaFormativa oferta);
	List<Profesor> findAllByNif (String nif);
	Optional<Profesor> findOneByNif(String nif);
	Optional<Profesor> findOneByUsuario(Usuario user);
	List<Profesor> findAllByUsuario(Usuario usuario);

	@Query("select distinct profesor from Profesor as profesor where profesor.nif = :nif and (profesor.fechaBaja is null or profesor.fechaBaja > :fechaBaja)")
	Optional<Profesor> getProfesorActivoByNifAndFeBaja(@Param("nif") String nif, @Param("fechaBaja") LocalDate fechaBaja);
	
	@Query("select distinct profesor from Unidad as unidad inner join unidad.ofertaCentro as oferCen inner join unidad.tutor as profesor inner join oferCen.centro as centro where centro.idCentro = :centro")
	List<Profesor> getProfesoresByCentro(@Param("centro") String centro);
	
	@Query("select distinct profesor from Unidad as unidad inner join unidad.ofertaCentro as oferCen inner join unidad.tutor as profesor inner join oferCen.centro as centro where centro.idCentro = :centro and unidad.anio = :anio")
	List<Profesor> getProfesoresByCentroAndAnio(@Param("centro") String centro, @Param("anio") Integer anio);

}

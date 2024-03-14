package es.princast.gepep.repository;

import es.princast.gepep.domain.Actividad;
import es.princast.gepep.domain.Area;
import es.princast.gepep.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Area entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaRepository extends JpaRepository<Area, Long>, JpaSpecificationExecutor<Area> {

    Iterable<Area> findAllByEmpresa(Empresa empresa);
    Iterable<Area> findAllByEmpresaAndVisadaIsTrue(Empresa empresa);
    List<Area> findAllByActividad(Actividad actividad);

	@Modifying
    @Query("update Area a set a.nombreEmpresaPro = :nomEmpresa where a.empresa.idEmpresa = :idEmpresa")
    void updateAreasNombreEmpresaProByEmpresa(@Param("nomEmpresa") String nombreEmpresaPro, @Param("idEmpresa") Long idEmpresa);
}

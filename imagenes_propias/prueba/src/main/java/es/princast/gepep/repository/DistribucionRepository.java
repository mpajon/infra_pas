package es.princast.gepep.repository;

import java.util.List;
import java.util.Optional;

import es.princast.gepep.domain.dto.DistribucionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.princast.gepep.domain.AnexoContrato;
import es.princast.gepep.domain.Distribucion;
import es.princast.gepep.domain.Matricula;


/**
 * Spring Data JPA repository for the Distribucion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistribucionRepository extends JpaRepository<Distribucion, Long> {

	Iterable<Distribucion> findAllByAnexoContrato(AnexoContrato anexo);
	Iterable<Distribucion> findAllByMatricula(Matricula matricula);

	Optional<Distribucion> getOneByMatricula(Matricula matricula);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE  es.princast.gepep.domain.Distribucion dist  SET dist.fechaBaja= :#{#distribucion.fechaBaja}  WHERE dist.idDistribucion = :#{#distribucion.idDistribucion}")
	Integer updateBaja(@Param ("distribucion") Distribucion dist);

	@Query(value = "SELECT m.nu_anio as anio, \n" +
			"\td.cn_distribucion as idDistribucion, d.dc_centro as centro, \n" +
			"\td.nu_h_entrada_man as hentradaMan, d.nu_min_entrada_man as minEntradaMan, d.nu_h_salida_man as hsalidaMan, d.nu_min_salida_man as minSalidaMan,\n" +
			"\td.nu_h_entrada_tard as hentradaTard, d.nu_min_entrada_tard as minEntradaTard, d.nu_h_salida_tard as hsalidaTard, d.nu_min_salida_tard as minSalidaTard,\n" +
			"\td.fe_fbaja as fechaBaja, d.fe_finicio as fechaInicio, d.fe_ffin as fechaFin, d.te_realizacion as realizacion, d.dc_tutor_empresa as tutorEmpresa,\n" +
			"\td.te_horario_flex as horarioFlexible,\n" +
			"\tac.cn_anexo as idAnexo, ac.ca_cursoaca as cursoAcademico,\n" +
			"\ttp.dc_nombre as nombreTipoPractica, pp.dc_nombre as nombrePeriodo,\n" +
			"\ta.if_nif as nifAlumno, a.dc_nombre as nombreAlumno, a.dl_apellido1 as apellido1Alumno, a.dl_apellido2 as apellido2Alumno, a.fe_fnacimiento as fechaNacimientoAlumno,\n" +
			"\ta.dl_pais as paisAlumno, a.dl_provincia as provinciaAlumno, a.dl_municipio as municipioAlumno,\n" +
			"\tu.ca_nombre as nombreUnidad, u.ca_turno as turno,\n" +
			"\tc.dc_codigo as codigoCentro, c.dc_nombre as nombreCentro,\n" +
			"\to.dc_nombre as nombreOferta, o.dc_codigo as codigoOferta,\n" +
			"\tcl.dc_nombre as nombreCiclo, cl.dc_codigo as codigoCiclo, cl.dc_familia as familia,\n" +
			"\te.dc_nombre as nombreEnsenanza,\n" +
			"\ttp2.dc_nombre as nombreTipoPracticaConvenio,\n" +
			"\tcv.nu_numero as numeroConvenio, cv.dc_codigo as codigoConvenio,\n" +
			"\tar.dc_nombre as nombreArea, em.if_cif as cifEmpresa, em.dc_nombre as nombreEmpresa,\ta2.dc_nombre as nombreActividad,\n" +
			"\tac.ca_codigo as codAnexo,\n" +
			"\tpp.nu_horas as horasPeriodoPractica, dpe.cn_horas as horas,\n" +
			"\ttut.if_nif as nifTutor, tut.dc_nombre as nombreTutor, tut.dl_apellido1 as apellido1Tutor, tut.dl_apellido2 as apellido2Tutor,\n" +
			"\ttut2.if_nif as nifTutorAdicional, tut2.dc_nombre as nombreTutorAdicional, tut2.dl_apellido1 as apellido1TutorAdicional, tut2.dl_apellido2 as apellido2TutorAdicional\n" +
			"FROM Matricula m\n" +
			"INNER JOIN Distribucion d ON d.ca_matricula = m.ca_matricula\n" +
			"INNER JOIN Distribucion_Periodo dpe ON d.cn_distribucion = dpe.cn_distribucion\n" +
			"INNER JOIN periodo_practica pp ON pp.cn_periodo = dpe.cn_periodo\n" +
			"INNER JOIN tipo_practica tp ON tp.cn_tipo_practica = pp.cn_tipo_practica\n" +
			"inner join anexo_contrato ac on ac.cn_anexo = d.cn_anexo \n" +
			"inner join Alumno a on m.ca_alumno = a.ca_alumno\n" +
			"inner join Unidad u on u.ca_unidad = m.ca_unidad\n" +
			"inner join ofertas_centro oc on oc.ca_ofercen = u.ca_ofercen\n" +
			"inner join centro c on c.ca_centro = oc.ca_centro \n" +
			"inner join oferta_formativa o on o.ca_oferta_formativa = oc.ca_oferta_formativa\n" +
			"inner join ciclo cl on cl.ca_ciclo = o.ca_ciclo\n" +
			"inner join ensenanza e on e.ca_ensenanza = cl.ca_ensenanza\n" +
			"inner join convenio cv on cv.cn_convenio = ac.cn_convenio\n" +
			"inner join tipo_practica tp2 on tp2.cn_tipo_practica = cv.cn_tipo_practica\n" +
			"inner join area ar on ar.cn_area = cv.cn_area\n" +
			"inner join empresa em on em.cn_empresa = ar.cn_empresa\n" +
			"inner join actividad a2 on a2.cn_actividad = ar.cn_actividad\n" +
			"left join profesor tut on tut.ca_profesor = u.ca_tutor\n" +
			"left join profesor tut2 on tut2.ca_profesor = u.ca_tutor_adicional \n" +
			"WHERE m.nu_anio = :#{#anio}\n" +
			"AND (:idCentro is null or :idCentro = 'undefined' or c.ca_centro = :idCentro)\n" +
			"AND (:tutor is null or :tutor = 'undefined' or tut.ca_profesor = :tutor or tut2.ca_profesor= :tutor)\n" +
			"ORDER BY tp.dc_nombre\n" +
			"\t,pp.dc_nombre asc", nativeQuery = true)
	List<DistribucionDTO> findDistribucionesDTOByCentroAnioTutor(@Param ("idCentro") String idCentro,
																 @Param ("anio") Integer anio,
																 @Param ("tutor") String tutor);
}


